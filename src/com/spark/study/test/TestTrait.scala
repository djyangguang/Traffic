package com.spark.study.test

/**
  * Created by yg on 2017/7/25.
  */
trait TestTrait {
  val name : String
  def TestTrait()={
    println("you friend"+name);
  }


}
trait Read{
  val name : String;
  def Read()={
    println("you Read" +name)
  }
}
trait Speak{
  val name : String;
  def  Speak()={
    println("you Speak"+name)
  }
}
class Human(val name : String){
  def listen()={
    println(name + "is listen")
  }
}

class Animal

class Cat(override val name :String) extends  Animal with Read with Speak{
  override def toString: String = "123";

}
