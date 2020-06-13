package com.wonderland.trail

import com.databricks.spark.xml._
import com.wonderland.trail.Utils.SparkSessionManager
import org.slf4j.LoggerFactory

object GameEndpoint {
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionManager.getSparkSession
    val logger = LoggerFactory.getLogger("Game Endpoint...")
    logger.info("reading xml file example")

    val selectedData = spark.read
      .option("rootTag", "mediawiki")
      .option("rowTag", "page")
      .xml("/Users/xyan/Projects/advisor/data/enwiki-latest-pages-articles14.xml-p7697595p7744800")

    selectedData.show()

    selectedData.printSchema()

  }
}