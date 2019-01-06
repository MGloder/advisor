import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.{SparkConf, SparkContext}

object NaiveBayesExample {
  def main(args: Array[String]): Unit ={
    val dataPath = "resource/student"
    // source file
    val mathFileName = "student-mat.csv"
    val fileName2 = "student-por.csv"
    val mathInstances = Utilities.readAndEncodeData(dataPath, mathFileName)

    val portugeseInstances = Utilities.readAndEncodeData(dataPath, fileName2)
    val allInstnaces = mathInstances ++ portugeseInstances

    val conf = new SparkConf(false).setMaster("local[2]").setAppName("StudentGrads")
    val sc = new SparkContext(conf)
    val data = sc.parallelize(allInstnaces)

    val splits = data.randomSplit(Array(0.6, 0.4), seed = 11L)
    val trainData = splits(0).cache()
    val testData = splits(1)
    val NLambdas = 100
    val output = (1 to NLambdas) map {
      i => {
        val lambda = 1.0 * i / NLambdas
        val model = NaiveBayes.train(trainData, lambda = lambda)
        val trainResult = Utilities.evaluate(model, trainData)
        val testResult = Utilities.evaluate(model, testData)
        (lambda, trainResult, testResult)
      }
    }
    Utilities.writeToFile("resource/student_output/naive-bayes.csv", output)
  }
}
