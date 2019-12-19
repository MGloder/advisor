package com.wonderland.trail.Utils

import org.apache.spark.sql.SparkSession

object SparkSessionManager {
  def getSparkSession: SparkSession = {
    val spark = SparkSession
      .builder()
      .appName(SparkSessionManager.getClass.getSimpleName)
      .master("local[*]")
      .getOrCreate()
    spark
  }
}
