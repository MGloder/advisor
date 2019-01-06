import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.{SparkConf, SparkContext}

object StudentsGradsExample {
  def main(args: Array[String]): Unit = {
    val dataPath = "resource/student"
    // source file
    val mathFileName = "student-mat.csv"
    val fileName2 = "student-por.csv"
    val mathInstances = Utilities.readAndEncodeData(dataPath, mathFileName)

    val portugeseInstances = Utilities.readAndEncodeData(dataPath, mathFileName)
    val allInstnaces = mathInstances ++ portugeseInstances

    val conf = new SparkConf(false).setMaster("local[2]").setAppName("StudentGrads")
    val sc = new SparkContext(conf)
    val data = sc.parallelize(allInstnaces)

    val splits = data.randomSplit(Array(0.6, 0.4), seed = 11L)
    val trainData = splits(0).cache()
    val testData = splits(1)

    val nbClassifier = {
      val lambda = 0.5
      val model = NaiveBayes.train(trainData, lambda = lambda)
      val trainResult = Utilities.evaluate(model, trainData)
      val testResult = Utilities.evaluate(model, testData)
      (0.0, trainResult, testResult)
    }

    val dtreeClassifier = {
      val numClasses = 21
      val catgoricalFeaturesInfo = Map[Int, Int]()
      val impurity = "gini"
      val maxDepth = 5
      val maxBins = 32
      val model = DecisionTree.trainClassifier(trainData, numClasses, catgoricalFeaturesInfo, impurity, maxDepth, maxBins)
      val trainResult = Utilities.evaluate(model, trainData)
      val testResult = Utilities.evaluate(model, testData)
      (1.0, trainResult, testResult)
    }

    val dtreeRegressor = {
      val categoricalFeaturesInfo = Map[Int, Int]()
      val impurity = "variance"
      val maxDepth = 5
      val maxBins = 32
      val model = DecisionTree.trainRegressor(trainData, categoricalFeaturesInfo, impurity, maxDepth, maxBins)
      val trainResult = Utilities.evaluate(model, trainData)
      val testResult = Utilities.evaluate(model, testData)
      (2.0, trainResult, testResult)
    }

    val output = List(nbClassifier, dtreeClassifier, dtreeRegressor)

    Utilities.writeToFile("resource/student_output/StudentOutput-Comparison.csv", output)
  }
}
