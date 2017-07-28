package com.spark.study.spark.java.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yg on 2017/7/27.
  */
object SparkPageRank {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("PageRank").setMaster("local[100]")
    val iters = 20;
    val ctx = new SparkContext(sparkConf)
    val lines = ctx.textFile("D:\\bigdata\\spark\\pageRank.txt")
    val links = lines.map { s =>
      val parts = s.split("\\s+")
      (parts(0), parts(1))
    }.distinct().groupByKey().cache()
    links.foreach(println)
    //(1,1.0) 在
    var ranks = links.mapValues(v => 1.0)
    ranks.foreach(println);
    for (i <- 1 to iters) {

      val contribs = links.join(ranks).values.flatMap { case (urls, rank) =>
        val size = urls.size
        urls.map(url => (url, rank / size))

      }
      ranks = contribs.reduceByKey(_ + _).mapValues(0.15 + 0.85 * _)
    }
    val output = ranks.collect()
    output.foreach(tup => println(tup._1+"====="+tup._2))
    ctx.stop()
  }
}
