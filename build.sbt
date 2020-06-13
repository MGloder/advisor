name := "Advisor"

version := "0.1"

scalaVersion := "2.12.4"

val sparkVersion = "2.4.4"


resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/"
)
//Spark related
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "com.databricks" %% "spark-xml" % "0.9.0"

)

// MongoDB

libraryDependencies ++= Seq(
  "org.mongodb.spark" %% "mongo-spark-connector" % "2.4.1"
)
// Kafka
//libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"


libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
