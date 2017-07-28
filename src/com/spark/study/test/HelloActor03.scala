package com.spark.study.test

import scala.actors.Actor

/**
  * Created by yg on 2017/7/26.
  */
case class Message(content :String,sender:Actor)
class Laoyang extends Actor {
  override def act(): Unit = {
    while (true) {
      receive {
        case Message(content, sender) => {
          println("我收到")
        }
      }
    }

  }
}
  class Laoyang01Actor(val laoyang: Laoyang) extends Actor{
    override def act(): Unit = {
        laoyang ! Message("aaa",this)//给laoyang发消息
      while (true){
        receive{
          case response : String =>print("lao"+response)
        }
      }

    }
  }
  object Message{
    def main(args: Array[String]): Unit = {
      val laoyang = new Laoyang
      val laoyang01Actor = new Laoyang01Actor(laoyang)
      laoyang.start()
      laoyang01Actor.start()
    }
  }


