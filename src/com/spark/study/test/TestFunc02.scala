package com.spark.study.test

/**
  * Created by yg on 2017/7/25.
  */
object TestFunc02 {
  def test(): Unit = {
    for(i<-1.to(100)){ //1-100
      println(i)
    }
    for(i<-1 until 100){ //1-99
      println(i)
    }
  }
  def test3(): Unit ={
    for (i <- 0 to 100 if (i % 2 )==1;if (i%5)>3){
      println("I----"+i)//9,19,29,39,49,....99
    }

  }
  def test04(n:Int)={
    n match {
      case 1=>println()
      case 2 =>println()
      case _ =>println()//  其他
    }
  }

  def main(args: Array[String]): Unit = {
    test3()
  }
}
