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
		cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("teste");
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
