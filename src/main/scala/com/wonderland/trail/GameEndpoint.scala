package com.wonderland.trail

import java.sql.Date

import com.wonderland.trail.Utils.SparkSessionManager
import com.wonderland.trail.model.TestData
import com.wonderland.trail.schema.Source
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Dataset, Row}
import org.slf4j.LoggerFactory
import com.databricks.spark.xml._

object GameEndpoint {
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionManager.getSparkSession
    val logger = LoggerFactory.getLogger("Game Endpoint...")
    import spark.implicits._
    logger.info("reading xml file example")

    val selectedData = spark.read
      .option("rootTag", "mediawiki")
      .option("rowTag", "page")
      .xml("/Users/xyan/Projects/advisor/data/enwiki-latest-pages-articles14.xml-p7697595p7744800")

    selectedData.show()

    selectedData.printSchema()

  }
}