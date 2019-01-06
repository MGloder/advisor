import java.io.File

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.stat.Statistics

object Stats {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Example").setMaster("local[2]")
    val sc = new SparkContext(conf)

    val data = sc.textFile("resource/gmm_data.txt")
    val parsedData = data.map(s => Vectors.dense(s.trim.split(' ').map(_.toDouble))).cache()
    //println(parsedData.id)
    // Cluster the data into two classes using GaussianMixture
    //val gmm = new GaussianMixture().setK(2).run(parsedData)

    //var rdd = sc.parallelize(data)

    val summary = Statistics.colStats(parsedData)
    println("-" * 10,"Summary ", "-" * 10)
    println("Count:", summary.count)
    println("Max:", summary.max)
    println("Mean:", summary.mean)
    println("Min:", summary.min)

    println("Exist")

//    val entreeDataPath = args(0)
//    val config = DataConfig(entreeDataPath)
//    val featuresMap = Utilities.loadFeaturesMap(config.featuresFile)
//    val restaurants = config.locations.flatMap {
//      location => {
//        Utilities.loadLocationData(new File(s"${config.dataPath}/" + location))
//      }
//    }
//    println("City and their restaurant count")
//    println("-" * 50)
//
//
//    val grouped = restaurants.groupBy(_.city).map{ case (k, v) => k -> v.size }
//
//    val citiesSorted = grouped.keys.toArray.sorted
//    citiesSorted foreach { city =>
//      val count = grouped(city)
//      println(f"$city%20s has $count%6d restaurants")
//    }
//    println()
//
//    // top 5 popular features in each city
//    val cityAndTop5 = restaurants.groupBy(_.city).map { r =>
//      val (city, restaurantList) = r
//      val partialSum = restaurantList.flatMap(_.features).map(_ -> 1)
//      val allsum = partialSum.groupBy(_._1).map {
//        case (k, v) => k -> v.map(_._2).sum
//      }.toSeq.sortBy(_._2).reverse
//      val top5 = allsum.take(5)
//      city -> top5
//    }
//
//    citiesSorted.foreach { city =>
//      val top5 = cityAndTop5(city)
//      println(s"City: $city")
//      println("-" * 50)
//      top5 foreach { entry =>
//        val (f, count) = entry
//        println(f"${featuresMap(f)}%25s at $count%6d restaurants")
//      }
//      println()
//    }

  }
}
