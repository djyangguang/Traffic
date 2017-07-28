package com.spark.study.spark.java.scala

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
