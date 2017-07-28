package com.spark.study.test

/**
  * Created by yg on 2017/7/25.
  */
class TestObject private {
  val t2 ="laoy";
   var t =123
  def func01() ={
      println("gaga")

  }
}

object TestObject {
  val t1 =123;
  var sgaga =123123
  val single = new TestObject();
  def func02()={
    println("gaga");
  }

  def main(args: Array[String]): Unit = {
    val t1 = new TestObject()
    println(t1.t2);
    t1.func01();
    TestObject.func02();
    val t2 = new Human("dd")
    t2.listen()


  }
}
