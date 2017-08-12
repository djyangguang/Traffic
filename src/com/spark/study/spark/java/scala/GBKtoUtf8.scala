package com.spark.study.spark.java.scala
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapred.TextInputFormat
import org.apache.spark.rdd.RDD

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yg on 2017/8/12.
  */
object GBKtoUtf8 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("d")
    val sc = new  SparkContext(conf)
    transfer(sc,"D:\\bigdata\\spark\\ºÃÓÑ¹ØÏµ.txt")
      .flatMap(x => x.split(" "))

    .foreach( tup => println(tup))

  }
  def transfer(sc:SparkContext,path:String):RDD[String]={
    sc.hadoopFile(path,classOf[TextInputFormat],classOf[LongWritable],classOf[Text],1)
      .map(p => new String(p._2.getBytes, 0, p._2.getLength, "GBK"))


  }
}
