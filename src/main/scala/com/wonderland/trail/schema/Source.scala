package com.wonderland.trail.schema

import org.apache.spark.sql.types._

object Source {
  val testJoinSchema = StructType(Array(
    StructField("id", LongType, true),
    StructField("salary", DoubleType, true)))

//  id: Long,
//  firstName: String,
//  lastName: String,
//  nickName: String,
//  dateOfBirth: Date,
//  gender: String,
//  ssn: String

  val testDataSchema = StructType(Array(
    StructField("id", LongType, true),
    StructField("firstName", StringType, true),
    StructField("lastName", StringType, true),
    StructField("gender", StringType, true),
    StructField("nickName", StringType, true),
    StructField("dateOfBirth", DateType, true),
    StructField("ssn", StringType, true)
  ))


}
