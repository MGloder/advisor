import org.apache.spark.mllib.clustering.GaussianMixture
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.{SparkConf, SparkContext}

val conf = new SparkConf().setAppName("Example").setMaster("Master")
val sc = new SparkContext(conf)

val data = sc.textFile("data/mllib/gmm_data.txt")
val parsedData = data.map(s => Vectors.dense(s.trim.split(' ').map(_.toDouble))).cache()
//println(parsedData.id)
// Cluster the data into two classes using GaussianMixture
//val gmm = new GaussianMixture().setK(2).run(parsedData)

//var rdd = sc.parallelize(data)

Statistics.colStats(parsedData)
