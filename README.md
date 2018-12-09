# java-play-akka-spark-cassandra
Basic java project for integration with Play, Akka, Spark and Cassandra

## Começando projeto play-akka-spark-cassandra-java

Colocar os servidores do spark em execução (executar no prompt):
    ./pasta_do_spark/sbin/start-master.sh
    //Para verificar endereço e porta do servidor após o comando acima, acessar: http://localhost:8080 (Tem uma URL com o link que será servido pelo spark)
    ./pasta_do_spark/sbin/start-slave.sh spark://localhost:7077  //Trocar localhost:7077 pela URL encontrada no link acima

O servidor Cassandra também já deve estar em execução após a sua instalação. Além disso,  criar um keyspace e uma tabela com dados para teste:
    CREATE KEYSPACE teste WITH REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 1 };
    CREATE TABLE teste.users (user_id int, fname TEXT, lname TEXT, PRIMARY KEY (user_id) );
    INSERT INTO teste.users(user_id, fname, lname) VALUES(1, 'Nome1', 'Sobrenome1');
    INSERT INTO teste.users(user_id, fname, lname) VALUES(2, 'Nome2', 'Sobrenome2');

sbt new playframework/play-java-seed.g8

cd ./pasta criada

Alterar ./project/plugins.sbt adicionando:
    addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.20")

Acrescentar ao build.sbt:
    libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.6"
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.18"
    libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"
    libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.6.0"
    libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.6.0"
    Alterar a versão do scala:
        scalaVersion := “2.11.12”

Digitar “sbt” no prompt para iniciar (sempre que alterar o build.sbt e já estiver no console, executar reload ou fechar o console e entrar novamente
    clean
    update
    compile
    eclipse

Para ver o projecto no eclipse:
    Import existing gradle project no eclipse

Alterar arquivo de configuração da aplicação (application.conf):
    akka {
      #Options: OFF, ERROR, WARNING, INFO, DEBUG
      loglevel = "ERROR"
    }
    akka.default-dispatcher.fork-join-executor.pool-size-max=64
    akka.actor.debug.receive = on

Criar uma nova classe java (por exemplo: ActorController.java) dentro do pacote controllers e adicionar:
    package controllers;
    import static akka.pattern.Patterns.ask;
    import play.mvc.*;
    import views.html.*;
    import org.apache.spark.api.java.JavaSparkContext;
    import akka.actor.ActorRef;
    import akka.actor.ActorSystem;
    import org.apache.spark.api.java.JavaRDD;
    import java.util.Arrays;
    import java.util.List;
    import java.util.concurrent.CompletionStage;
    import org.apache.spark.SparkConf;
    import javax.inject.Singleton;
    import javax.inject.*;
    import scala.compat.java8.FutureConverters;
    
    @Singleton
    public class ActorController extends Controller {
    	static private ActorSystem system;
    	
    	@Inject
    	public ActorController(ActorSystem system) {
    		this.system = system;
    	}
    	
    	public CompletionStage<Result> meuMetodo(String msg) {
    		//Testar Spark
    		SparkConf conf = new SparkConf(true)//
    				.setAppName("MinhaApp")//
    				.setMaster("spark://meucomp.local:7077"); //Verificar a URL novamente
    		JavaSparkContext sc = new JavaSparkContext(conf);
    		List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
    		JavaRDD<Integer> distData = sc.parallelize(data);
    		System.out.println(distData);
    		sc.close();
    		
    		//Testar Akka
    		ActorRef helloActor = system.actorOf(HelloActor.getProps());
    		return FutureConverters.toJava(ask(helloActor, msg, 2000))//Timeout 2000ms
    				.thenApply(response -> ok(actor.render((String) response)));
    	}
    }

Duplicar o arquivo app/views/index.scala.html e renomear para “actor.scala.html” e:
    Colocar o parâmetro de entrada na primeira linha: @(msg: String)
    Exibir o parâmetro em algum lugar (exemplo):  <h2>@msg</h2>

Adicionar uma rota para o novo método em conf/routes:
    GET 	/actor/*msg		controllers.ActorController.meuMetodo(msg: String)

Criar uma nova classe (por exemplo: HelloActor.java) que extende de AbstractActor:
    package controllers;
    
    import akka.actor.*;
    import com.datastax.driver.core.Cluster;
    import com.datastax.driver.core.Session;
    import com.datastax.driver.core.ResultSet;
    import com.datastax.driver.core.Row;
    import java.util.Iterator;
    
    public class HelloActor extends AbstractActor {
    	private Cluster cluster;
    	private Session session;
    	
    	public static Props getProps() {
            return Props.create(HelloActor.class);
        }
    	
    	public HelloActor() {
    		cluster = Cluster.builder().addContactPoint("127.0.0.1").build(); //Alterar 127.0.0.1 pelo endereço IP do servidor Cassandra.
            session = cluster.connect("teste"); //Utilizar o nome do keyspace criado no Cassandra
    	}
    
    	@Override
    	public Receive createReceive() {
    		return receiveBuilder()//
                    .match(String.class, s -> {
                    	ResultSet results = session.execute("SELECT * FROM users WHERE user_id = " + s);
                        String line = "";
                        for (Iterator<Row> iterator = results.iterator(); iterator.hasNext();) {
                            Row row = iterator.next();
                            line += row.getString("fname") + " " + row.getString("lname")
                                    + "\n";
                            System.out.println(line);
                        }
                        sender().tell(line, self());
                    }).build();
    	}
    }
    
Testar o funcionamento:
    No console do sbt digitar “run”
    Abrir uma página: http://localhost:9000
    Abrir uma página: http://localhost:9000/actor/1 //Onde 1 é o id de um usuário do banco
    No console deve aparece uma mensagem sobre o resultado do spark:
        ParallelCollectionRDD[0] at parallelize at ActorController.java:39
    No navegador deve aparecer um nome e sobrenome de usuário que estão no banco.

## Pronto! Seu projeto básico Play com Akka, Spark e Cassandra está funcionando.