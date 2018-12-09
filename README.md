# java-play-akka-spark-cassandra
Basic java project for integration with Play, Akka, Spark and Cassandra

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<title></title>
	<meta name="generator" content="LibreOffice 5.2.7.2 (Linux)"/>
	<meta name="created" content="2018-11-29T20:31:25.175100768"/>
	<meta name="changed" content="2018-12-09T00:11:00.416057852"/>
	<style type="text/css">
		@page { margin: 2cm }
		p { margin-bottom: 0.25cm; line-height: 120% }
		a:link { so-language: zxx }
	</style>
</head>
<body lang="pt-BR" dir="ltr">
<ol>
	<p align="center" style="margin-bottom: 0cm; line-height: 100%"><font size="4" style="font-size: 16pt"><b>Começando
	projeto play-akka-spark-cassandra-java</b></font></p>
</ol>
<p align="center" style="margin-bottom: 0cm; line-height: 100%"><br/>

</p>
<ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">Colocar os servidores do
	spark em execução (executar no prompt):</font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">./pasta_do_spark/sbin/start-master.sh</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">//Para verificar endereço e
		porta do servidor após o comando acima, acessar:
		<a href="http://localhost:8080/">http://localhost:8080</a> (Tem uma
		URL com o link que será servido pelo spark.</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">./pasta_do_spark/sbin/start-slave.sh
		spark://localhost:7077  //Trocar localhost:7077 pela URL encontrada
		no link acima</font></p>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">O servidor Cassandra também
	já deve estar em execução após a sua instalação. Além disso, 
	criar um keyspace e uma tabela com dados para teste:</font></p>
	<ol>
		<ol>
			<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
			<font size="3" style="font-size: 12pt">CREATE KEYSPACE teste WITH
			REPLICATION = { 'class': 'SimpleStrategy', 'replication_factor': 1
			};</font></p>
			<li/>
<p style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
			<font size="3" style="font-size: 12pt">CREATE TABLE teste.users
			(user_id int, fname TEXT, lname TEXT, PRIMARY KEY (user_id) );</font></p>
			<li/>
<p style="margin-bottom: 0cm; line-height: 100%"><font size="3" style="font-size: 12pt"><span style="font-weight: normal">INSERT
			INTO </span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">teste.</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">users(user_id,
			fname, lname) VALUES(1, '</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">Nome1</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">',
			'</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">Sobrenome1</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">');</span></font></p>
			<li/>
<p style="margin-bottom: 0cm; line-height: 100%"><font size="3" style="font-size: 12pt"><span style="font-weight: normal">INSERT
			INTO </span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">teste.</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">users(user_id,
			fname, lname) VALUES(</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">2</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">,
			'</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">Nome2</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">',
			'</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">Sobrenome2</span></font><font size="3" style="font-size: 12pt"><span style="font-weight: normal">');</span></font></p>
		</ol>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">sbt new
	playframework/play-java-seed.g8</font></p>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">cd ./pasta criada</font></p>
	<li/>
<p style="margin-bottom: 0cm; line-height: 100%">Alterar
	./project/plugins.sbt adicionando:</p>
	<ol>
		<li value="1"/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">addSbtPlugin(&quot;com.typesafe.play&quot;
		% &quot;sbt-plugin&quot; % &quot;2.6.20&quot;)</font></p>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">acrescentar ao build.sbt:</font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">libraryDependencies +=
		&quot;com.fasterxml.jackson.module&quot; %% &quot;jackson-module-scala&quot;
		% &quot;2.9.6&quot;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">libraryDependencies +=
		&quot;com.typesafe.akka&quot; %% &quot;akka-actor&quot; % &quot;2.5.18&quot;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">libraryDependencies +=
		&quot;org.apache.spark&quot; %% &quot;spark-core&quot; % &quot;2.4.0&quot;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">libraryDependencies +=
		&quot;com.datastax.cassandra&quot; % &quot;cassandra-driver-core&quot;
		% &quot;3.6.0&quot;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">libraryDependencies +=
		&quot;com.datastax.cassandra&quot; % &quot;cassandra-driver-mapping&quot;
		% &quot;3.6.0&quot;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">Alterar a versão do scala:</font></p>
		<ol>
			<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
			<font size="3" style="font-size: 12pt">scalaVersion := “2.11.12”</font></p>
		</ol>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">digitar “sbt” no prompt
	para iniciar (sempre que alterar o build.sbt e já estiver no
	console, executar reload ou fechar o console e entrar novamente</font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">clean</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">update</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">compile</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">eclipse</font></p>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">Para editar o projeto no
	eclipse:</font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import existing gradle
		project no eclipse</font></p>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">Alterar arquivo de
	configuração da aplicação (application.conf):</font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">akka {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		  <font size="3" style="font-size: 12pt">loglevel = &quot;ERROR&quot;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">}</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">akka.default-dispatcher.fork-join-executor.pool-size-max=64</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">akka.actor.debug.receive =
		on</font></p>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">Criar uma nova classe java
	(por exemplo: ActorController.java) dentro do pacote controllers e
	adicionar:</font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">package controllers;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import static
		akka.pattern.Patterns.ask;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import play.mvc.*;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import views.html.*;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		org.apache.spark.api.java.JavaSparkContext;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import akka.actor.ActorRef;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		akka.actor.ActorSystem;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		org.apache.spark.api.java.JavaRDD;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import java.util.Arrays;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import java.util.List;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		java.util.concurrent.CompletionStage;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		org.apache.spark.SparkConf;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		javax.inject.Singleton;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import javax.inject.*;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		scala.compat.java8.FutureConverters;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		</p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">@Singleton</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">public class ActorController
		extends Controller {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	static private ActorSystem
		system;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	@Inject</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	public
		ActorController(ActorSystem system) {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		this.system = system;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	}</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	public
		CompletionStage&lt;Result&gt; meuMetodo(String msg) {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		//Testar Spark</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		SparkConf conf = new
		SparkConf(true)//</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">				.setAppName(&quot;MinhaApp&quot;)//</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">				.setMaster(&quot;spark://meucomp.local:7077&quot;);</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		JavaSparkContext sc = new
		JavaSparkContext(conf);</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		List&lt;Integer&gt; data =
		Arrays.asList(1, 2, 3, 4, 5);</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		JavaRDD&lt;Integer&gt;
		distData = sc.parallelize(data);</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		System.out.println(distData);</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		sc.close();</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		//Testar Akka</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		ActorRef helloActor =
		system.actorOf(HelloActor.getProps());</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		return
		FutureConverters.toJava(ask(helloActor, msg, 2000))//</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">				.thenApply(response -&gt;
		ok(actor.render((String) response)));</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	}</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">}</font></p>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">Duplicar o arquivo
	app/views/index.scala.html e renomear para “actor.scala.html” e:</font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">Colocar o parâmetro de
		entrada na primeira linha: @(msg: String)</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">Exibir o parâmetro em algum
		lugar (exemplo):  &lt;h2&gt;@msg&lt;/h2&gt;</font></p>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">Adicionar uma rota para o
	novo método em conf/routes:</font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">GET
			/actor/*msg		controllers.ActorController.meuMetodo(msg: String)</font></p>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
	<font size="3" style="font-size: 12pt">Criar uma nova classe (por
	exemplo: HelloActor.java) que extende de AbstractActor:</font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">package controllers;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		</p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import akka.actor.*;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		com.datastax.driver.core.Cluster;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		com.datastax.driver.core.Session;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		com.datastax.driver.core.ResultSet;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import
		com.datastax.driver.core.Row;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">import java.util.Iterator;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		</p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">public class HelloActor
		extends AbstractActor {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	private Cluster cluster;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	private Session session;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	public static Props
		getProps() {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		        <font size="3" style="font-size: 12pt">return
		Props.create(HelloActor.class);</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		    <font size="3" style="font-size: 12pt">}</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	public HelloActor() {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		cluster =
		Cluster.builder().addContactPoint(&quot;127.0.0.1&quot;).build();
		//Alterar 127.0.0.1 pelo endereço IP do servidor Cassandra.</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		        <font size="3" style="font-size: 12pt">session =
		cluster.connect(&quot;teste&quot;); //Utilizar o nome do keyspace
		criado no Cassandra</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	}</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		</p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	@Override</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	public Receive
		createReceive() {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">		return receiveBuilder()//</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                <font size="3" style="font-size: 12pt">.match(String.class,
		s -&gt; {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                <font size="3" style="font-size: 12pt">	ResultSet
		results = session.execute(&quot;SELECT * FROM users WHERE user_id =
		&quot; + s);</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                    <font size="3" style="font-size: 12pt">String
		line = &quot;&quot;;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                    <font size="3" style="font-size: 12pt">for
		(Iterator&lt;Row&gt; iterator = results.iterator();
		iterator.hasNext();) {</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                        <font size="3" style="font-size: 12pt">Row
		row = iterator.next();</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                        <font size="3" style="font-size: 12pt">line
		+= row.getString(&quot;fname&quot;) + &quot; &quot; +
		row.getString(&quot;lname&quot;)</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                                <font size="3" style="font-size: 12pt">+
		&quot;\n&quot;;</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                        <font size="3" style="font-size: 12pt">System.out.println(line);</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                    <font size="3" style="font-size: 12pt">}</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                    <font size="3" style="font-size: 12pt">sender().tell(line,
		self());</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		                <font size="3" style="font-size: 12pt">}).build();</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">	}</font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt">}</font></p>
	</ol>
	<li/>
<p align="justify" style="margin-bottom: 0cm; line-height: 100%">
	<font size="3" style="font-size: 12pt"><b>Testar o funcionamento:</b></font></p>
	<ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; line-height: 100%">
		<font size="3" style="font-size: 12pt"><b>No console do sbt digitar
		“run”</b></font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt"><b>Abrir uma página:
		<a href="http://localhost:9000/">http://localhost:9000</a></b></font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; font-weight: normal; line-height: 100%">
		<font size="3" style="font-size: 12pt"><b>Abrir uma página:
		<a href="http://localhost:9000/actor/1">http://localhost:9000/actor/</a></b><b><a href="http://localhost:9000/actor/1">1</a>
		//Onde 1 é o id de um usuário do banco</b></font></p>
		<li/>
<p align="justify" style="margin-bottom: 0cm; line-height: 100%">
		<font size="3" style="font-size: 12pt"><b>No console deve aparece
		uma mensagem sobre o resultado do spark:</b></font></p>
		<ol>
			<li/>
<p align="justify" style="margin-bottom: 0cm; line-height: 100%">
			<font size="3" style="font-size: 12pt"><b>ParallelCollectionRDD[0]
			at parallelize at ActorController.java:39</b></font></p>
		</ol>
		<li/>
<p align="justify" style="margin-bottom: 0cm; line-height: 100%">
		<font size="3" style="font-size: 12pt"><b>No navegador deve
		aparecer um nome e sobrenome de usuário que estão no banco.</b></font></p>
	</ol>
</ol>
<p align="justify" style="margin-bottom: 0cm; line-height: 100%"><br/>

</p>
<p style="margin-top: 0.42cm; margin-bottom: 0.21cm; line-height: 100%; page-break-after: avoid">
<font face="Liberation Sans, sans-serif"><font size="4" style="font-size: 14pt">Pronto!
Seu projeto básico Play com Akka, Spark e Cassandra está
funcionando.</font></font></p>
</body>
</html>