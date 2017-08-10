package com.spark.study.spark.java.SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.SQLContext;

/**
 * Created by yg on 2017/8/10.
 */
public class LoadSave {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("dataframe").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);

        DataFrame usersDF = sqlContext.read().load("users.parquet");
        usersDF.printSchema();
        usersDF.show();

        usersDF.select("name","favorite_color").write().save("namesAndColors.parquet");
    }
}
