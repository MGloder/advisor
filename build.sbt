name := "Advisor"

version := "0.1"

scalaVersion := "2.11.8"

val sparkVersion = "2.4.4"


resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)
//Spark related
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion
)

// Kafka
//libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"


libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
