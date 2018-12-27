//import java.util
//
//import breeze.util.{Encoder, Index}
//import com.esotericsoftware.kryo.io.{Input, Output}
//import com.twitter.chill.ScalaKryoInstantiator
//import kafka.serializer.Decoder
//import org.apache.kafka.common.serialization.Serializer
//
//class SessionDataSerializer extends Encoder[SessionData] with Decoder[SessionData] with Serializer[SessionData] {
//
//  def toBytes(data: SessionData): Array[Byte] = {
//    val instantiator = new ScalaKryoInstantiator
//    instantiator.setRegistrationRequired(false)
//    val kryo = instantiator.newKryo()
//
//    val buffer = new Array[Byte](4096)
//    val output = new Output(buffer)
//    kryo.writeObject(output, data)
//    buffer
//  }
//
//  override val index: Index[SessionData] = ???
//
//  override def fromBytes(buffer: Array[Byte]): SessionData = {
//    val instantiator = new ScalaKryoInstantiator
//    instantiator.setRegistrationRequired(false)
//    val kryo = instantiator.newKryo()
//    val input = new Input(buffer)
//    val sessionData = kryo.readObject(input, classOf[SessionData])
//      .asInstanceOf[SessionData]
//    sessionData
//  }
//
//  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {}
//
//  override def serialize(topic: String, data: SessionData): Array[Byte] = toBytes(data)
//
//  override def close(): Unit = {}
//}
