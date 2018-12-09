name := """java-play-akka-spark-cassandra"""
organization := "com.dionisio"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.12"

libraryDependencies += guice
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.6"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.18"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.6.0"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.6.0"
