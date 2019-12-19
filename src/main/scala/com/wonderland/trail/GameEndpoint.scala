package com.wonderland.trail

import java.sql.Date

import com.wonderland.trail.Utils.SparkSessionManager
import com.wonderland.trail.model.TestData
import com.wonderland.trail.schema.Source
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Dataset, Row}

object GameEndpoint {
  def main(args: Array[String]): Unit = {
    val spark = SparkSessionManager.getSparkSession

    import spark.implicits._


    val testDataSet: Dataset[TestData] = spark
      .read
      .schema(Source.testDataSchema)
      .option("delimiter", ",")
      .option("header", false)
      .csv("/tmp/sample-data-TestData.csv")
      .as[TestData]


    //    val joinDataSet: Dataset[JoinData] = spark
    //      .read
    //      .schema(Source.testJoinSchema)
    //      .option("delimiter", ",")
    //      .option("header", false)
    //      .csv("/tmp/sample-data-JoinData.csv")
    //      .as[JoinData]


    //     print(testDataSet.join(joinDataSet, "id").explain())

    //    val exmaple = testDataSet.groupBy("id")

    //    val countResult = exmaple.count()


    //    val repar = testDataSet.repartition(10)
    //    val write = testDataSet.write.format("com.databricks.spark.csv").option("header", true).mode("overwrite").save("example.csv")

    val customMax = new FoundAndReplaceTime()
    spark.udf.register("customMax", new FoundAndReplaceTime)
    val customMaxData = testDataSet.groupBy("id")
      .agg(
        customMax(testDataSet.col("dateOfBirth")).as("maxDate")
      )

    val cleanedData = testDataSet.join(customMaxData, "id")

    val finalData = cleanedData
      .drop("dateOfBirth")
      .withColumnRenamed("maxDate", "dateOfBirth")

    finalData.cache()

    finalData.printSchema()
    print("count final data" + finalData.count())
    print(finalData.explain())

    finalData
      .write.format("com.databricks.spark.csv").option("header", true)
      .mode("overwrite").save("maxed_data.csv")


  }
}

class FoundAndReplaceTime() extends UserDefinedAggregateFunction {
  override def inputSchema: StructType = StructType(Array(
    StructField("dateOfBirth", DateType)
  ))

  override def bufferSchema: StructType = StructType(Array(
    StructField("max", DateType)
  ))

  override def dataType: DataType = DateType

  override def deterministic: Boolean = true

  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = Date.valueOf("1900-01-01")
  }

  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    if (buffer.getDate(0).compareTo(input.getDate(0)) < 0) {
      buffer(0) = input.getDate(0)
    }
  }

  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    if (buffer1.getDate(0).compareTo(buffer2.getDate(0)) < 0) {
      buffer1(0) = buffer2.getDate(0)
    }
  }

  override def evaluate(buffer: Row): Any = {
    buffer.getDate(0)
  }
}
