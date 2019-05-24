package com.machinedoll.advisor.core

import com.machinedoll.advisor.utils.SparkSessionManager

object Example{
  def main(args: Array[String]): Unit ={
    val ssm = new SparkSessionManager()
    val ssc = ssm.createSparkStreamContext()
    val lines = ssc.socketTextStream("localhost", 9999)
    val words = lines.flatMap(_.split(" "))
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)

    // Print the first ten elements of each RDD generated in this DStream to the console
    println("Example")
    wordCounts.print()
    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate
  }
}
