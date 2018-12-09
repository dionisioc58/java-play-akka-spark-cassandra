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
				.setMaster("spark://diohp.casa:7077");
		JavaSparkContext sc = new JavaSparkContext(conf);
		List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
		JavaRDD<Integer> distData = sc.parallelize(data);
		System.out.println(distData);
		sc.close();
		
		//Testar Akka
		ActorRef helloActor = system.actorOf(HelloActor.getProps());
		return FutureConverters.toJava(ask(helloActor, msg, 2000))//
				.thenApply(response -> ok(actor.render((String) response)));
	}
}
