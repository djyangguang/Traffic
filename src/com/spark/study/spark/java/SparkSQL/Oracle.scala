package com.spark.study.spark.java.SparkSQL


import java.io.{FileOutputStream, PrintWriter}
import java.sql.DriverManager

import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.apache.spark.sql.Row
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yg on 2017/8/24.
  */
object Oracle1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("g").setMaster("local")
    val sc = new SparkContext(conf)
    import org.apache.spark.sql.SQLContext
    val sqlContext = new SQLContext(sc)
    val jdbcMap = Map("url" -> "jdbc:oracle:thin:@//192.168.2.106:1521/easdb",
      "user" -> "dbo_djeas",
      "password" -> "dbo_djeas",
      "dbtable" -> "t_tra_truckassembleentry",
      "driver" -> "oracle.jdbc.driver.OracleDriver")
    val jdbcDF = sqlContext.read.options(jdbcMap).format("jdbc").load
    jdbcDF.registerTempTable("patient")

    val t0 = System.nanoTime : Double

    val res = sqlContext.sql("select * from patient where FPRODUCTID like '+++7nudAp0OaQ2i/FRKFI+0kyzc=%'")
    val t1 = System.nanoTime : Double
    println("Elapsed time " + (t1 - t0) / 1000000.0 + " msecs")
    res.foreach(println)


    sc.stop()

  }

}
object  oracle2 {
  def main(args: Array[String]): Unit = {

  //val pw = new PrintWriter(new FileOutputStream("D:\\bigdata\\recommend\\资料一\\testdata\\oo.text",true))
  val conf = new SparkConf().setAppName("yt").setMaster("local")
  val sc = new SparkContext(conf)
  val sql = " select d.fproductid,d.ss from (select t2.fproductid fproductid, " +
    " t3.fnumber||t3.fid ss from T_TRA_TruckAssemble t1" +
    " left join t_tra_truckassembleentry t2 on t2.fparentid=t1.fid" +
    " left join  T_TRA_TruckInfo t3 on t3.fid=t1.ftruckid" +
    " left join t_ord_saleorderentry t4 on t4.fid=t2.forderentryid" +
    " left join t_ord_saleorder t5 on t5.fid=t4.fparentid " +
    " where t5.fnumber not like 'B%'" +
    " order by t2.fproductid ) d  WHERE 1 = ? AND rownum < ?"
  val rdd: RDD[String] = new JdbcRDD(
  sc,
  () => {
  Class.forName("oracle.jdbc.driver.OracleDriver").newInstance()
  DriverManager.getConnection("jdbc:oracle:thin:@//192.168.2.106:1521/easdb","dbo_djeas","dbo_djeas")
},
  //"SELECT * FROM t_tra_truckassembleentry WHERE 1 = ? AND rownum < ?",
  sql, 1, 10000000, 10,
  x => x.getString(1)+"\t"+x.getString(2)).cache()
  rdd.foreach(println)
  //  pw.write(str.toString());
        //  pw.println()//rdd.foreach(tup => println(tup + "==============" ))

  rdd.coalesce(1).saveAsTextFile("D:\\bigdata\\recommend\\资料一\\testdata\\oo.text")
 // rdd.collect().foreach(println)
  sc.stop()
}
}


