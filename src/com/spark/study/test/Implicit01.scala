package com.spark.study.test

/**
  * Created by yg on 2017/7/26.
  * 隐式转换 特殊的接口 只接受学生老人
  * 类型不对尝试 隐式转换
  * SpecialPerson 能使用接口
  */
class SpecialPerson(val name : String)
class Student(val name : String)
class Older(val name :String)
class Teacher(val name:String)
object Implicit01{
  implicit def object2SpecialPerson(obj:Object):SpecialPerson={//object2SpecialPerson( 什么对象转成了什么类 默认的写法
    if(obj.getClass==classOf[Student]){ //l类相等
      val stu =obj.asInstanceOf[Student]//类转换
      new SpecialPerson(stu.name)
    }
    else if(obj.getClass==classOf[Older]){
        val Older =obj.asInstanceOf[Older]
      new SpecialPerson(Older.name)
    }
    else{
      Nil
    }
  }
  var ticketNumber =0;
  def buySpecialTicket(p:SpecialPerson)={
    ticketNumber+=1
    "T_"+ticketNumber
  }

  def main(args: Array[String]): Unit = {
    val laoyang = new Student("laoyang")
    println(buySpecialTicket(laoyang))

  }
}
