package com.spark.study.spark.java.SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;

/**
 * Created by yg on 2017/8/10.
 */
public class SaveModeTest {
    public static void main (String [] args){
        SparkConf conf = new SparkConf().setAppName("a").setMaster("local[1]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        DataFrame pDF = sqlContext.read().format("json").load("order.json");
        pDF.save("order.json", SaveMode.ErrorIfExists)
        ;
        pDF.select("name").write().format("parquet").save("peopleName.parquet");

    }
}
