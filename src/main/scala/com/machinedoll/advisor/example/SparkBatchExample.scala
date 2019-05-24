package com.machinedoll.advisor.example

import com.machinedoll.advisor.utils.SparkSessionManager

object SparkBatchExample {
  def main(args: Array[String]): Unit = {
    val ssm = new SparkSessionManager
    val spark = ssm.createSparkSession()
    val static = spark.read.json("/Users/xyan/Learning/Scala/Advisor/src/main/resources/activity-data/")

    val counts = static.groupBy("gt").count()
    println(counts.show())
  }
}
