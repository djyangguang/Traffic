package com.spark.study.test

/**
  * Created by yg on 2017/7/25.
  */
object TestFunc {
  def sayMyname(name :String ="laoy"): Unit ={
    println(name)
  }
  def sumMoreParameters(elem :Int*): Unit ={//多个参数 Int*
    var sum =0;
    for (e <- elem){
        println(e)
      sum+=e
    }
    sum
  }
  def add(a:Int,b:Int) =a+b //方法体 a+b
  def add2 =add(_:Int,2)//传进来参数 和2累加
  //f(n)=F(n)*f(n-1)
  def fac(n:Int):Int =if(n<=0)1 else n*fac(n-1)//递归 阶乘
  def mulitply(x:Int)(y:Int)=x*y //克里化函数
  def m2 =mulitply(2)_
  //****匿名函数****/
  val t = () => 222//生命了一个函数对象付给t
  val t1 =() => 1
  //() => Int 匿名函数的类型 不能传参数的 返回值是Int类型  c 要传进来个匿名函数
  def testfunc02(c : () => Int)={ //=有返回值
     println(c())//c() 真正去调用传进来的函数
    1000
  }
  def d1 =(a:Int)=> a+100
  //***
  def testf1(callback :(Int,Int)=>Int)={//需要这种 (Int,Int)= 参数 返回的是Int类型
     println(callback(123,123))//实际的数据 不是传进来的 传进来操作数据的函数
  }

  //嵌套函数
  def add3(x:Int,y:Int,z:Int) : Int = { //前2个值相加的结果 在和第3个数相加
    def add2(x:Int,y:Int):Int={
      x + y
    }
    add2(add2(x,y),2)
  }

  //@ f:Int => Int) 函数输入的参数是个匿名函数 输入输出都是Int
  //(Int,Int)=>Int= 函数的返回类型呢
  //=后面是方法体
  def sum(f:Int => Int):(Int,Int)=>Int ={
    def sumf(a:Int,b:Int):Int =
      if(a>b) 0 else f(a)+sumf(a+1,b)
    sumf
  }




  def main(args: Array[String]): Unit = {
    //println(sumMoreParameters(1,2,3,4,5))
   // println(add(1,2))
    //println(add2(add(1,2)))
    //println(fac(5))
    //println(mulitply(123)(123))
   // println(m2(123))
    //  println(testfunc02(t))
    //testf1((a : Int ,b:Int)=>{println(a*b);a*b})//实际的计算逻辑
   // testf1(add)

    def sumf =sum((z)=>z+1)
    println(sumf(1,2))
  }

}
