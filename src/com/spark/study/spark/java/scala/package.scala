package com.spark.study.spark.java

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yg on 2017/7/26.
  */
object  WC{
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("WC").setMaster("local")
    val sc = new SparkContext(conf)
    val lines =sc.textFile("D:\\bigdata\\spark\\Traffic\\data\\2014082013_all_column_test.txt")
    val words =lines.flatMap(line => line.split(","))
    val pairs =words.map(word => (word,1))
    val wordcounts =pairs.reduceByKey{_+_}
    val sortewordcounts =wordcounts.sortBy(x=>x._2,false)
    sortewordcounts.foreach{x=>println(x._1+"=====>"+x._2)}
  }
}
object wc02{
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("ss").setMaster("local[1]")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("D:\\bigdata\\spark\\Traffic\\data\\2014082013_all_column_test.txt")
    val words =lines.flatMap(_.split(","))
    val pairs =words.map((_,1)).reduceByKey(_+_)
    pairs.foreach(println)
   // val result =pairs.reduceByKey(_+_)
    //result.saveAsTextFile("")
    //val sort =result.sortByKey().collect()
   // pairs.foreach(println)


  }
  object wc3{
    def mian (args:Array[String] ): Unit ={
      val conf = new SparkConf().setAppName("s").setMaster("local[]")
      val sc = new SparkContext(conf)
      val lines = sc.textFile("")
      val words = lines.flatMap( lines => lines.split(""))
      val pairs = words.map( words =>(words,1))
      val wordcount = pairs.reduceByKey(_+_)

    }
  }

}
