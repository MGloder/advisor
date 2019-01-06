name := "Advisor"

version := "0.1"

scalaVersion := "2.11.8"

val sparkVersion = "2.3.0"


resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)
//Spark related
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "mysql" % "mysql-connector-java" % "5.1.6"
)

// Mongo DB
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.5.0"

// Kafka
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"

// Akka
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.19",
  "com.typesafe.akka" %% "akka-persistence" % "2.5.19",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.19" % Test
)

// Incompatible Jackson version: 2.9.7
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7"

