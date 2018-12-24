import java.io.File
import scala.io.Source

object Utilities {
  def loadLocationData(locationFile: File): Array[Restaurant] = {
    val src = Source.fromFile(locationFile)
    val locationFeatures = src.getLines.map{
      line =>
        val entry = line.split("\t")
        val restaurantId = entry(0)
        val restaurantName = entry(1)
        val features = entry(2)
        val f = features.trim().split(" ")
        val city = locationFile.getName().replaceAll(".txt", "")
        Restaurant(restaurantId, restaurantName, f, city)
    }.toArray
    locationFeatures
  }

  def loadFeaturesMap(featuresFile: String): Map[String, String] = {
    val src = Source.fromFile(featuresFile)
    val featuresMap = src.getLines.map {
      line =>
        val entry = line.split("\t").map(_.trim())
        entry(0) -> entry(1)
    }.toMap
    featuresMap
  }

  def loadSessionData(sessionFile: String) = {
    val src = Source.fromFile(sessionFile)
    val sessions = src.getLines.map { line =>
      val entry = line.split("\t").map(_.trim())
      val datetime = entry(0)
      val ip = entry(1)
      val entryPoint = entry(2)
      val navigations = entry.drop(3).dropRight(1)
      val endPoint = entry.last
      SessionData(datetime, ip, entryPoint, navigations, endPoint)
    }.toArray
    sessions
  }
}
