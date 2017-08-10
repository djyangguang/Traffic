package com.spark.study.spark.java.SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

/**
 * Created by yg on 2017/8/10.
 */
public class DataFramCreate {
    public static void main (String [] args){
        SparkConf conf = new SparkConf().setAppName("a").setMaster("local[2]");
        JavaSparkContext sc  = new JavaSparkContext(conf);
        SQLContext sqlcon = new SQLContext(sc);
        DataFrame df = sqlcon.read().json("hdfs://node1:9000/laoyang.json");
        df.show();
    }
}
