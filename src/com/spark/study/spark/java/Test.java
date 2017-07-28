package com.spark.study.spark.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.scalastyle.Lines;

/**
 * Created by yg on 2017/7/26.
 */
public class Test {
    public static void main(String[] args){
        SparkConf conf = new SparkConf().setAppName("cache").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile("D:\\bigdata\\spark\\Traffic\\data\\2014082013_all_column_test.txt").cache();
        long beginTime =System.currentTimeMillis();
        long count =lines.count();//启动个job
        long endTime =System.currentTimeMillis();
        System.out.println("===============>cost"+(endTime-beginTime));
        beginTime=System.currentTimeMillis();
        count = lines.count();
        endTime=System.currentTimeMillis();
        System.out.println("cost"+(endTime-beginTime));
        sc.close();

    }
}
