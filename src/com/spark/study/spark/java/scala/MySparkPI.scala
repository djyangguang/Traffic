package com.spark.study.spark.java.scala

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yg on 2017/7/27.
  */
object MySparkPI {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Spark Pi")
    val spark = new SparkContext("local[2]","Spark Pi") //
    val slices =10;
    val n =1000*slices
        //parallelize() 本地的数据集合并行化
    //1到10000并行到10个里面去 每个跑 1000
    val count =spark.parallelize(1 to n,slices).map({
      i=>

        /**
          * 蒙特卡洛算法 概率
          */

        def random : Double =java.lang.Math.random()
        val x =random * 2 -1
        val y =random * 2 -1
        println(x + "============="+ y)
        if(x*x +y*y <1 ) 1 else 0

    }).reduce(_+_)
    println("PI"+4.0*count/n)
    spark.stop()
  }
}
