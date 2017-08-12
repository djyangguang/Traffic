package com.spark.study.test

/**
  * Created by yg on 2017/7/25.
  */
object TestFunc {
  def sayMyname(name :String ="laoy"): Unit ={
    println(name)
  }
  def sumMoreParameters(elem :Int*): Unit ={//������� Int*
    var sum =0;
    for (e <- elem){
        println(e)
      sum+=e
    }
    sum
  }
  def add(a:Int,b:Int) =a+b //������ a+b

  def add2 =add(_:Int,2)//���������� ��2�ۼ�
  //f(n)=F(n)*f(n-1)
  def fac(n:Int):Int =if(n<=0)1 else n*fac(n-1)//�ݹ� �׳�
  def mulitply(x:Int)(y:Int)=x*y //���ﻯ����
  def m2 =mulitply(2)_
  //****��������****/
  val t = () => 222//������һ���������󸶸�t
  val t1 =() => 1
  //() => Int �������������� ���ܴ������� ����ֵ��Int����  c Ҫ����������������
  def testfunc02(c : () => Int)={ //=�з���ֵ
     println(c())//c() ����ȥ���ô������ĺ���
    1000
  }
  def d1 =(a:Int)=> a+100

  //***
  def testf1(callback :(Int,Int)=>Int)={//��Ҫ���� (Int,Int)= ���� ���ص���Int����
     println(callback(123,123))//ʵ�ʵ����� ���Ǵ������� �������������ݵĺ���
  }

  //Ƕ�׺���
  def add3(x:Int,y:Int,z:Int) : Int = { //ǰ2��ֵ��ӵĽ�� �ں͵�3�������
    def add2(x:Int,y:Int):Int={
      x + y
    }
    add2(add2(x,y),2)
  }

  //@ f:Int => Int) ��������Ĳ����Ǹ��������� �����������Int
  //(Int,Int)=>Int= �����ķ���������
  //=�����Ƿ�����
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
    //testf1((a : Int ,b:Int)=>{println(a*b);a*b})//ʵ�ʵļ����߼�
   // testf1(add)

    def sumf =sum((z)=>z+1)
    println(sumf(1,2))
  }

}
