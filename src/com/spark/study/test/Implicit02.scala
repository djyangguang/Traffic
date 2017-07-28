package com.spark.study.test

/**
  * Created by yg on 2017/7/26.
  * 隐士参数
  */
class SingnPen{
  def write(content :String) =println(content)
}
object ImplicitContext{
  implicit  val singnPen = new SingnPen
}
//自己带笔和使用公用的笔
object Implicit02 {
  def  signForExam(name:String)(implicit singnPen: SingnPen): Unit ={//克里化 名字是 笔是公用的
    singnPen.write(name +"VBVCVCV")
  }

  def main(args: Array[String]): Unit = {
    import ImplicitContext._ //这是把 singnPen 定义到外边
    //也可以这样
    //val singnPen = new SingnPen
    //signForExam("laoyang")(singnPen)
    signForExam("laoyang")
  }
}
