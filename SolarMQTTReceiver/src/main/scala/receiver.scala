import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.SparkConf
import org.apache.spark.streaming.mqtt._
import com.datastax.spark.connector._

object MQTTWordCount {

  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println(
        "Usage: MQTTWordCount <MqttbrokerUrl> <topic>")
      System.exit(1)
    }

    val Conf = new SparkConf(true).set("spark.cassandra.connection.host", "172.31.1.42").setMaster("spark://172.31.1.42:7077").setAppName("FirstStream").set("spark.executor.memory", "1g")
   // val sc = new SparkContext("spark://172.31.1.42:7077", "test", Conf)

    val Seq(brokerUrl, topic) = args.toSeq
  //val sparkConf = new SparkConf2().setAppName("MQTTWordCount")
    val ssc = new StreamingContext(Conf, Seconds(2))
   // val lines = MQTTUtils.createStream(ssc, brokerUrl, topic, StorageLevel.MEMORY_ONLY_SER_2)
    val lines = MQTTUtilsCustom.createStream(ssc, brokerUrl, topic)

    println("start counting")
   // println(lines.print())
    val words = lines.flatMap(x => x.toString.split(" "))
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)

    //val collection = sc.parallelize(Seq(wordCounts))
    //collection.saveToCassandra("test", "wordcount",SomeColumns("word", "count"))
    //wordCounts.print()
    wordCounts.foreachRDD(rdd => {
        rdd.saveToCassandra("test", "wordcount",SomeColumns("word", "count"))

      })
     println("received message")


    ssc.start()
    ssc.awaitTermination()
  }
}
