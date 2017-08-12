package com.spark.study.spark.java

import com.google.gson.annotations.Until
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
object wc02 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("ss").setMaster("local[1]")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("D:\\bigdata\\spark\\Traffic\\data\\2014082013_all_column_test.txt")
    val words = lines.flatMap(_.split(","))
    val pairs = words.map((_, 1)).reduceByKey(_ + _)
    pairs.foreach(println)
    // val result =pairs.reduceByKey(_+_)
    //result.saveAsTextFile("")
    //val sort =result.sortByKey().collect()
    // pairs.foreach(println)


  }
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
  object wc4{
    def main(args:Array[String]):Unit={

      val conf = new SparkConf().setAppName("d").setMaster("local[2]");
      val sc = new SparkContext(conf);
      val lines = sc.textFile("D:\\bigdata\\spark\\spark-1.6.1\\README.md");
      val words = lines.flatMap(_.split(" "));
      val pairs = words.map(words=>(words,1))
      val wc= pairs.reduceByKey(_+_)
      //val sort = wc.sortBy(y=>y._2,false);
      //val sort =wc.sortByKey().collect();
      val sort =wc.map(x => (x._2,x._1)).sortByKey(false).map(x=>(x._2,x._1)).collect(); // µÈÓÚ wc.sortBy(y=>y._2,false);
      /*(,67)
      (the,21)
      (to,14)
      (Spark,13)
      (for,11)
      (and,10)
      (##,8)*/
      sort.foreach(println)

    }


}
object wc5 {
  def main (args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("d").setMaster("local[3]");
    val sc = new SparkContext(conf)
    val lines = sc.textFile("");
    val word =lines.flatMap(_.split(" "));
    val pairs = word.map((_,1)).reduceByKey(_+_);
  }
}
object wc6 {
  def main (args:Array[String]):Unit={

  }
}
