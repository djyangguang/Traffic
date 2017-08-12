package com.spark.study.spark.java.scala

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapred.TextInputFormat
import org.apache.spark.rdd.RDD

import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by yg on 2017/7/28.
  */
object pageRank01 {

  def main (args: Array[String]): Unit ={
     val conf = new SparkConf().setAppName("1").setMaster("local[0]")
    val iters =20;
    val sc = new SparkContext(conf)
    val lines = sc.textFile("a");
    val links =lines.map{
      s=>
        val parts = s.split("\\s+")
        (parts(0),parts(1))
    }.distinct().groupByKey().cache()
    links.foreach(println)
    var ranks =links.mapValues(x=> 1.0)

    for(i<- 1 to  iters){
      val contribs = links.join (ranks).values.flatMap{
        case (urls,rank)=>
          val size =urls.size
          urls.map(url => (url,rank/size))
      }
      ranks = contribs.reduceByKey(_+_).mapValues(0.15 +0.85*_)
    }
    val outPut = ranks.collect()


  }


}
object PageRank02{

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val sparkconf = new SparkConf().setAppName("1").setMaster("local[2]");
    val sc = new SparkContext(sparkconf);
    val iters =20
   // val lines = sc.textFile("D:\\bigdata\\spark\\好友关系.txt");
   // val line = sc.hadoopFile[LongWritable,Text, TextInputFormat]("D:\\bigdata\\spark\\好友关系.txt")
//     val l= sc.hadoopFile("",classOf[TextInputFormat],classOf[LongWritable],classOf[Text],1)
//      .map(p => new String(p._2.getBytes, 0, p._2.getLength, "GBK"))
    val lines =transfer(sc,"D:\\bigdata\\spark\\好友关系.txt")
    val links =lines.map{
      s =>
        val parts = s.split("\\s+")
        (parts(0),parts(1))
    }.distinct().groupByKey().cache()
    println("===根据边的关系数据生成邻接表====")
    links.foreach(println)
    var ranks = links.mapValues(v=>1.0)
    println("每个人的权重是1.0")
    ranks.foreach(println)
    for (i<-1 to iters){
      val contribs = links.join(ranks)
      println("第"+i+"次2个RDD合并成一个")
      contribs.foreach(println)
        val con1 =contribs.values.flatMap{
         case(urls,rank)=>
          val size=urls.size
          urls.map(url => (url,rank/size))

      }
      println("第"+i+"操作Values")
      con1.foreach(println)
      ranks= con1.reduceByKey(_+_).mapValues(0.15+0.85*_)
      println("第"+i+"0.15+0.85*_)")
      ranks.foreach(println)
    }
    val output = ranks.sortBy(y=>y._2,false)
    output.foreach(tup=>println(tup._1+"=============="+tup._2))
    sc.stop();
  }
  def transfer(sc:SparkContext,path:String):RDD[String]={
    sc.hadoopFile(path,classOf[TextInputFormat],classOf[LongWritable],classOf[Text],1)
      .map(p => new String(p._2.getBytes, 0, p._2.getLength, "GBK"))


  }

}

