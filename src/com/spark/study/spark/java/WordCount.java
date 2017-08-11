package com.spark.study.spark.java;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;
import scala.actors.threadpool.Arrays;


/**
 * Created by yg on 2017/7/26.
 */
public class WordCount {
    public static void main (String [] args){
        SparkConf conf = new SparkConf().setAppName("WC").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> lines = sc.textFile("D:\\bigdata\\spark\\Traffic\\data\\2014082013_all_column_test.txt").cache();
        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterable<String> call(String s) throws Exception {
                return Arrays.asList(s.split("  "));
            }
        });
        JavaPairRDD<String,Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String,Integer>(s,1); //元祖里面2个值
            }
        });
        JavaPairRDD<String,Integer> wcs =pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) throws Exception {
                return integer+integer2;
            }
        });
        JavaPairRDD<Integer,String> tempwcs =wcs.mapToPair(new PairFunction<Tuple2<String, Integer>, Integer, String>() {
            @Override
            //Tuple2 元祖里面有2个值得是叫tuple2 一直到tuple23
            public Tuple2<Integer, String> call(Tuple2<String, Integer> integerIntegerTuple2) throws Exception {
                return new Tuple2<Integer,String>(integerIntegerTuple2._2,integerIntegerTuple2._1);
            }
        });
        JavaPairRDD<Integer,String> sortedwc =tempwcs.sortByKey(false);
        JavaPairRDD<String,Integer> resultwc =sortedwc.mapToPair(new PairFunction<Tuple2<Integer, String>, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(Tuple2<Integer, String> integerStringTuple2) throws Exception {
                return new Tuple2<String,Integer>(integerStringTuple2._2,integerStringTuple2._1);
            }
        });
        resultwc.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
                System.out.println(stringIntegerTuple2._1+"===="+stringIntegerTuple2._2);
            }
        });
        sc.close();
    }
}
