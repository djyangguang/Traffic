package com.spark.study.test

/**
  * Created by yg on 2017/7/25.
  */
object TestMap {
  def main(args: Array[String]): Unit = {

    //var m1 = Map[String, Int](("a" , 1), ("b" , 2)); //也行
    var m1 = Map[String, Int]("a" -> 1, "b" -> 2);
    println(m1("a"))//1
    m1 +=("c"->3)
    print(m1)
    m1.foreach(a=>{
      println(a+""+a._1+""+a._2)
    })
  }
}
