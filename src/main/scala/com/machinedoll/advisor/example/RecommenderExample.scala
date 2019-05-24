package com.machinedoll.advisor.example

import com.machinedoll.advisor.utils.SparkSessionManager
import org.apache.spark.ml.recommendation.ALS

object RecommenderExample {
  def main(args: Array[String]) = {
    val ssm = new SparkSessionManager
    val spark = ssm.createSparkSession()
    val rating = spark.read.textFile("/Users/xyan/Learning/Scala/Spark-The-Definitive-Guide/data/sample_movielens_ratings.txt").selectExpr("split(value, '::') as col").selectExpr("cast(col[0] as int) as userId", "cast(col[1] as int) as movieId", "cast(col[2] as float) as rating", "cast(col[3] as long) as timestamp")

    val Array(training, testing) = rating.randomSplit(Array(0.8, 0.2))
    val als = new ALS().setMaxIter(5).setRegParam(0.01).setUserCol("userId").setItemCol("movieId").setRatingCol("rating")

//    println(als.explainParams())

    val alsModel = als.fit(training)

    val perdiction = alsModel.transform(testing)
    alsModel
      .recommendForAllUsers( 10)
      .selectExpr( "userId", "explode(recommendations)")
      .show()


    //    println(result)
//    println(perdiction.head)


  }
}
