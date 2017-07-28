package com.spark.study.test

import scala.actors.Actor


/**
  * Created by yg on 2017/7/25.
  */
class HelloActor extends Actor {
  //override def receive: Receive = ???

  def act() = {
    while (true) {
      receive {
        case name: String => println("Hello" + name)
        case money: Int => println("How much" + money)
      }
    }
  }




}
  object  HelloActor{
    def main(args: Array[String]): Unit = {
      val helloActor = new HelloActor
      helloActor.start()
      helloActor ! 100
    }
  }


