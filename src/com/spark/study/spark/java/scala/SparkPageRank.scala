package com.spark.study.spark.java.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yg on 2017/7/27.
  * 权重最大
  */
object SparkPageRank {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("PageRank").setMaster("local[100]")
    val iters = 20; //计算20次 算出一个收敛的值也行
    val ctx = new SparkContext(sparkConf)
    val lines = ctx.textFile("D:\\bigdata\\spark\\pageRank.txt")
    //根据边的关系数据生成邻接表(1,(2,3,4))(2,(1,3))..
    //distinc()重复的边去掉
    //groupByKey() 根据key分组
    //cache() 需要不停的迭代所以把这数据缓存
    val links = lines.map {
      s =>
      val parts = s.split("\\s+")
      (parts(0), parts(1))//空格的前面是0 空格的后面是1
    }.distinct().groupByKey().cache()
    links.foreach(println)
    //mapValues (1,1.0)(2,1.0) 初始值每个人的权重是1.0
    var ranks = links.mapValues(v => 1.0)
    ranks.foreach(println);
    for (i <- 1 to iters) {
      //(1,((2.3.4.5),1.0)) 一个人的好友列表 加上初始值
      /**
        * join :2个RDD可以合成一个
        * .values=((2.3.4.5),1.0))
        * case (urls, rank)  如果不是这样的格式(1,2,3),1.0 就不在方法里执行
        *  urls.size= (2.3.4.5)
        *  urls.map(url => (url, rank / size)) 操作 (2.3.4.5)变成元祖
        *  rank / size = 1.0 /4
        */
      val contribs = links.join(ranks).values.flatMap { case (urls, rank) =>
        val size = urls.size
        urls.map(url => (url, rank / size))

      }
      //把所有对 2.3.4.5 的贡献都要计算出来 这个值来计算 （url, rank / size)
      ranks = contribs.reduceByKey(_ + _).mapValues(0.15 + 0.85 * _)
    }
    val output = ranks.collect()
    output.foreach(tup => println(tup._1+"====="+tup._2))
    ctx.stop()
  }
}
