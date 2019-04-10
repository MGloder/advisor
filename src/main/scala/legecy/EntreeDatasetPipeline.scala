import java.io.{File, FilenameFilter}
import java.util.Properties

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import com.mongodb.{MongoClientSettings, ServerAddress}
import org.apache.log4j.{Level, Logger}
import org.mongodb.scala.connection.ClusterSettings
import org.mongodb.scala.{MongoClient, MongoCollection}

import scala.xml.Document

object QueueConfig {
  val producerProps = Map(
    "metadata.broker.list" -> "localhost:9092",
    "serializer.class" -> "kafka.serializer.DefaultEncoder",
    "key.serializer" -> "org.apache.kafka.common.serialization.StringSerializer",
    "value.serializer" -> "SessionDataSerializer",
    "bootstrap.servers" -> "localhost:9092"
  )

  val zkConnection = "127.0.0.1:2181"
  val groupId = "group"
  val topic = "topic"
  val kafkaServerURL = "localhost"
  val kafkaServerPort = 9092
  val kafkaProducerBufferSize = 64 * 1024
  val connectionTimeOut = 100000
  val reconnectInterval = 10000
  val clientId = "EntreeClient"

  val consummerProps = {
    val props = new Properties()
    props.put("zookeeper.connect", zkConnection)
    props.put("group.id", groupId)
    props.put("zookeeper.session.timeout.ms", "400")
    props.put("zookeeper.sync.time.ms", "200")
    props.put("auto.commit.interval.ms", "1000")
    props
  }
}

object DBConifg {
  val dbName = "entree"
  val restaurants = "restaurants"
  val sessions = "sessions"
  val dbHost = "localhost"
  val dbPort = 27017
  //"mongodb://localhost:27017"
  val dbUri = s"mongodb://$dbHost:$dbPort"
}

object Database {
  def insertSession(sessionData: SessionData): Unit = ???

  def insertRestaurant(restaurant: Restaurant): Unit = ???

  def getCollection(collectionName: String): MongoCollection[Document] = {
    val mongoClient = MongoClient(DBConifg.dbUri)
    val db = mongoClient.getDatabase(DBConifg.dbName)
    db.getCollection(collectionName)
  }

  lazy val restaurantCollection = getCollection(DBConifg.restaurants)
  lazy val sessionCollection = getCollection(DBConifg.sessions)

  def deleteAll(): Unit = {
    restaurantCollection.drop()
    sessionCollection.drop()
  }
}

//class DBPersistenceActor extends Actor{
//  override def receive: Receive = {
//    case restaurant: Restaurant => {
//      Database.insertRestaurant(restaurant)
//    }
//    case sessionData: SessionData => {
//      Database.insertSession(sessionData)
//    }
//  }
//}

case class Cmd(data: String)
case class Evt(data: String)

case class DBState(events: List[String] = Nil) {
  def updated(evt: Evt): DBState = copy(evt.data :: events)
  def size: Int = events.length
  override def toString: String = events.reverse.toString
}


class DBPersistenceActor extends PersistentActor {
  override def persistenceId = "db-persistence-id-1"

  var state = DBState()

  def updateState(event: Evt): Unit =
    state = state.updated(event)

  def numEvents =
    state.size

  val receiveRecover: Receive = {
    case evt: Evt => updateState(evt)
    case SnapshotOffer(_, snapshot: DBState) => state = snapshot
  }

  val snapShotInterval = 1000
  val receiveCommand: Receive = {
    case Cmd(data) =>
      persist(Evt(s"$data-$numEvents")) { event =>
        updateState(event)
        context.system.eventStream.publish(event)
        if (lastSequenceNr % snapShotInterval == 0 && lastSequenceNr != 0)
          saveSnapshot(state)
      }
    case "print" => println(state)
  }
}

//class MainActor(config: DataConfig) extends Actor{
//  import Utilities._
//  println("loading Main Actor")
//  val dbPersister: ActorRef = context.actorOf(Props[DBPersistenceActor], "dbPersister")
////  val messageProducer: ActorRef = context.actorOf(Props[MessageProductorActor], name="messageProducer")
//
//  override def receive: Receive = {
//    case "begin" => {
//      val featuresMap = loadFeaturesMap(config.featuresFile)
//      val restaurants = config.locations.flatMap {
//        location =>
//          loadLocationData(new File(s"${config.dataPath}/" + location))
//      }
//      println(s"Number of restaurants: ${restaurants.size}")
//
//      // store all location to MongoDB
//      for (restaurant: Restaurant <- restaurants) {
//        dbPersister ! restaurant
//      }
//
//      // load session data
//      val sessionFiles = new File(config.sessionPath).list(
//        new FilenameFilter {
//          override def accept(dir: File, name: String): Boolean =
//            name.startsWith("session.")
//        })
//
//      println(sessionFiles.toList)
//
//      for (sf <- sessionFiles){
//        val sessionFile = s"${config.sessionPath}/$sf"
//        println(s"Loading session from: $sessionFile")
//        val sessionData = loadSessionData(sessionFile)
//        println(s"\t\tNumber of recorded sessions: ${sessionData.size}")
//        for (session <- sessionData){
//          dbPersister ! session
//          // messageProducer ! session
//        }
//        Thread.sleep(2 * 1000)
//      }
//    }
//    case "exit" => {
//      println("Exiting now ...")
//      System.exit(0)
//    }
//  }
//}

//class MessageProduceActor extends Actor{}

//object EntreeDatasetPipeline {
//  def main(args: Array[String]): Unit ={
//    val system: ActorSystem = ActorSystem("AdvisorSystem")
//    val entreeDataPath = args(0)
//    val config = DataConfig(entreeDataPath)
//
////    Database.deleteAll()
//
//    val props = Props(new MainActor(config))
//    val mainActor = system.actorOf(props)
//
//    /*
//     * Pipeline:
//     * 1. Entree Text files -> Database -> Spark Streaming
//     * 2. Entree Text files -> Kafka -> Spark Streaming
//     * 3. Entree Text files -> Spark Streaming
//     * 4. Entree Text files -> Database -> Kafka -> Spark Streaming
//     */
//
//    // start Actor
//    mainActor ! "Begin"
//
//    // logger
//    Logger.getLogger("org").setLevel(Level.INFO)
//    Logger.getLogger("akka").setLevel(Level.INFO)
//
//    // no spark streaming for now, just print to console
////    val conf = new SparkConf(false).setMaster("local[2]").setAppName("Entree")
////    val ssc = new StreamingContext(conf, Seconds(2))
////
////    val receiver = new SessionDataReceiver()
////    val sessionDataStream = ssc.receiverStream(receiver)
//    Thread.sleep(10000)
//    system.terminate()
//
//  }
//}
