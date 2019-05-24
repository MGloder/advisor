package com.machinedoll.advisor.example

import com.machinedoll.advisor.utils.SparkSessionManager

object SparkStreamingExample{
  def main(args: Array[String]): Unit ={
    /*
      Structured Streaming
     */
    val ssm = new SparkSessionManager
    val spark = ssm.createSparkSession()


    val static = spark.read.json("/Users/xyan/Learning/Scala/Advisor/src/main/resources/activity-data/")
    val dataSchema = static.schema
    println(static.schema)
//
    val streaming = spark.readStream.schema(dataSchema).option("maxFilesPerTrigger", 10000).json("/Users/xyan/Learning/Scala/Advisor/src/main/resources/activity-data/")
    val activityCounts = streaming.groupBy("gt").count()
//
    val activityQuery = activityCounts.writeStream.queryName("activities_count").format("console").outputMode("complete").start()
//

    activityQuery.awaitTermination()

//    val examleCount = streaming.groupBy("Device").count()
//    val exampleQuery = examleCount.writeStream.queryName("example_count").format("console").outputMode("complete").start()
//
//    exampleQuery.awaitTermination()
//
//    println(spark.streams.active)

//    println("example output:")
//    println(static.groupBy("gt").count().show())
//    println("example output termination")
//    for (i <- 1 to 5 ){
//      spark.sql("select * from activities_count").show()
//      Thread.sleep(1000)
//    }

  }
}
