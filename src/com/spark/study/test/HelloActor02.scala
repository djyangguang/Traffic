package com.spark.study.test
import scala.actors.Actor

/**
  * Created by yg on 2017/7/26.
  * g给 Actor 发送case class 的消息
  * case 关键字 专门做模式匹配的
  * 启动一个集群  master 跟数量slave  通讯 让他干事亲
  *spark 就是这样 发送类过去 进行匹配操作
  *
  * */


  case class Register(username: String, password: String)

  case class Login(username: String, password: String)

  class UserManagerActor extends Actor {
    def act {
      while (true) {
        receive{
        case Login(username, password) => println("Login"+username+password)
        case Register(usernam, password) => println("Register"+usernam+password)
      }
    }
  }

}
  object UserManagerActor{
    def main(args: Array[String]): Unit = {
      val userActor = new UserManagerActor
      userActor.start()
      userActor ! Register("laoyang","123")
      userActor ! Login("laoyang2","345")
    }
  }


