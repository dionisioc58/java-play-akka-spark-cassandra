# java-play-akka-spark-cassandra
Basic java project for integration with Play, Akka, Spark and Cassandra

## Começando projeto play-akka-spark-cassandra-java

1. Colocar os servidores do spark em execução (executar no prompt):
    1. ./pasta_do_spark/sbin/start-master.sh
    2. //Para verificar endereço e porta do servidor após o comando acima, acessar: http://localhost:8080 (Tem uma URL com o link que será servido pelo spark)
    3. ./pasta_do_spark/sbin/start-slave.sh spark://localhost:7077  //Trocar localhost:7077 pela URL encontrada no link acima
2. O servidor Cassandra também já deve estar em execução após a sua instalação. Além disso,  criar um keyspace e uma tabela com dados para teste:
    1. CREATE KEYSPACE teste WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 1 };
    2. CREATE TABLE teste.users (user_id int, fname TEXT, lname TEXT, PRIMARY KEY (user_id) );
    3. INSERT INTO teste.users(user_id, fname, lname) VALUES(1, 'Nome1', 'Sobrenome1');
    4. INSERT INTO teste.users(user_id, fname, lname) VALUES(2, 'Nome2', 'Sobrenome2');
3. sbt new playframework/play-java-seed.g8
4. cd ./pasta criada
5. Alterar ./project/plugins.sbt adicionando:
    1. addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.20")
6. acrescentar ao build.sbt:
    1. libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.6"
    2. libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.18"
    3. libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"
    4. libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.6.0"
    5. libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.6.0"
    6. Alterar a versão do scala:
        1. scalaVersion := “2.11.12”
7. digitar “sbt” no prompt para iniciar (sempre que alterar o build.sbt e já estiver no console, executar reload ou fechar o console e entrar novamente
    1. clean
    2. update
    3. compile
    4. eclipse
8. import existing gradle project no eclipse
9. Alterar arquivo de configuração da aplicação (application.conf):
    1. akka {
    2.   #Options: OFF, ERROR, WARNING, INFO, DEBUG
    3.   loglevel = "ERROR"
    4. }
    5. akka.default-dispatcher.fork-join-executor.pool-size-max=64
    6. akka.actor.debug.receive = on
10. Criar uma nova classe java (por exemplo: ActorController.java) dentro do pacote controllers e adicionar:
    1. package controllers;
    2. import static akka.pattern.Patterns.ask;
    3. import play.mvc.*;
    4. import views.html.*;
    5. import org.apache.spark.api.java.JavaSparkContext;
    6. import akka.actor.ActorRef;
    7. import akka.actor.ActorSystem;
    8. import org.apache.spark.api.java.JavaRDD;
    9. import java.util.Arrays;
    10. import java.util.List;
    11. import java.util.concurrent.CompletionStage;
    12. import org.apache.spark.SparkConf;
    13. import javax.inject.Singleton;
    14. import javax.inject.*;
    15. import scala.compat.java8.FutureConverters;
    16. 
    17. @Singleton
    18. public class ActorController extends Controller {
    19. 	static private ActorSystem system;
    20. 	
    21. 	@Inject
    22. 	public ActorController(ActorSystem system) {
    23. 		this.system = system;
    24. 	}
    25. 	
    26. 	public CompletionStage<Result> meuMetodo(String msg) {
    27. 		//Testar Spark
    28. 		SparkConf conf = new SparkConf(true)//
    29. 				.setAppName("MinhaApp")//
    30. 				.setMaster("spark://meucomp.local:7077"); //Verificar a URL novamente
    31. 		JavaSparkContext sc = new JavaSparkContext(conf);
    32. 		List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
    33. 		JavaRDD<Integer> distData = sc.parallelize(data);
    34. 		System.out.println(distData);
    35. 		sc.close();
    36. 		
    37. 		//Testar Akka
    38. 		ActorRef helloActor = system.actorOf(HelloActor.getProps());
    39. 		return FutureConverters.toJava(ask(helloActor, msg, 2000))//Timeout 2000ms
    40. 				.thenApply(response -> ok(actor.render((String) response)));
    41. 	}
    42. }
11. Duplicar o arquivo app/views/index.scala.html e renomear para “actor.scala.html” e:
    1. Colocar o parâmetro de entrada na primeira linha: @(msg: String)
    2. Exibir o parâmetro em algum lugar (exemplo):  <h2>@msg</h2>
12. Adicionar uma rota para o novo método em conf/routes:
    1. GET 	/actor/*msg		controllers.ActorController.meuMetodo(msg: String)
13. Criar uma nova classe (por exemplo: HelloActor.java) que extende de AbstractActor:
    1. package controllers;
    2. 
    3. import akka.actor.*;
    4. import com.datastax.driver.core.Cluster;
    5. import com.datastax.driver.core.Session;
    6. import com.datastax.driver.core.ResultSet;
    7. import com.datastax.driver.core.Row;
    8. import java.util.Iterator;
    9. 
    10. public class HelloActor extends AbstractActor {
    11. 	private Cluster cluster;
    12. 	private Session session;
    13. 	
    14. 	public static Props getProps() {
    15.         return Props.create(HelloActor.class);
    16.     }
    17. 	
    18. 	public HelloActor() {
    19. 		cluster = Cluster.builder().addContactPoint("127.0.0.1").build(); //Alterar 127.0.0.1 pelo endereço IP do servidor Cassandra.
    20.         session = cluster.connect("teste"); //Utilizar o nome do keyspace criado no Cassandra
    21. 	}
    22. 
    23. 	@Override
    24. 	public Receive createReceive() {
    25. 		return receiveBuilder()//
    26.                 .match(String.class, s -> {
    27.                 	ResultSet results = session.execute("SELECT * FROM users WHERE user_id = " + s);
    28.                     String line = "";
    29.                     for (Iterator<Row> iterator = results.iterator(); iterator.hasNext();) {
    30.                         Row row = iterator.next();
    31.                         line += row.getString("fname") + " " + row.getString("lname")
    32.                                 + "\n";
    33.                         System.out.println(line);
    34.                     }
    35.                     sender().tell(line, self());
    36.                 }).build();
    37. 	}
    38. }
14. Testar o funcionamento:
    1. No console do sbt digitar “run”
    2. Abrir uma página: http://localhost:9000
    3. Abrir uma página: http://localhost:9000/actor/1 //Onde 1 é o id de um usuário do banco
    4. No console deve aparece uma mensagem sobre o resultado do spark:
        1. ParallelCollectionRDD[0] at parallelize at ActorController.java:39
    5. No navegador deve aparecer um nome e sobrenome de usuário que estão no banco.

## Pronto! Seu projeto básico Play com Akka, Spark e Cassandra está funcionando.