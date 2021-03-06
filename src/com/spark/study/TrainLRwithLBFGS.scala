package com.spark.study

import java.text.SimpleDateFormat
import java.util
import java.util.{Date}

import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Administrator on 2015/6/4.
 */
object TrainLRwithLBFGS {

    val sparkConf = new SparkConf().setAppName("laoY traffic").setMaster("local")
    val sc = new SparkContext(sparkConf)

    // create the date/time formatters
    val dayFormat = new SimpleDateFormat("yyyyMMdd")
    val minuteFormat = new SimpleDateFormat("HHmm")

    def main(args: Array[String]) {

        // fetch data from redis
        val jedis = RedisClient.pool.getResource //Hbase
        jedis.select(1)

        // find relative road monitors for specified road
        // val camera_ids = List("310999003001","310999003102","310999000106","310999000205","310999007204")
        val camera_ids = List("310999003001","310999003102")
        val camera_relations:Map[String,Array[String]] = Map[String,Array[String]]( //一个路段的影响它的相关路段
            "310999003001" -> Array("310999003001","310999003102","310999000106","310999000205","310999007204"),
            "310999003102" -> Array("310999003001","310999003102","310999000106","310999000205","310999007204")
        )

        val temp = camera_ids.map({ camera_id =>
            val hours = 3
            val nowtimelong = System.currentTimeMillis();
            val now = new Date(nowtimelong)
            val day = dayFormat.format(now)
            // Option Some None
            //卡扣相关路段的列表
            val list = camera_relations.get(camera_id).get

            val relations = list.map({ camera_id =>
                println(camera_id)
                // fetch records of one camera for three hours ago
                (camera_id, jedis.hgetAll(day + "_" + camera_id))//取一天卡扣的全部数据 key 卡扣id value:一天的每分钟数据

            })

            relations.foreach(println)

            // organize above records per minute to train data set format (MLUtils.loadLibSVMFile)
            val trainSet = ArrayBuffer[LabeledPoint]()//等于 AttayList LabeledPoint 做回归使用的类型
            // start begin at index 3
            for(i <- Range(60*hours-3,0,-1)){// 180 到1 循环 ；往训练集中填每行的数据 180行  -1 没有0

                val featuresX = ArrayBuffer[Double]() //平均速度 x0~Xn
                val featureY = ArrayBuffer[Double]()//Y 一个数
                // get current minute and recent two minutes
                for(index <- 0 to 2){ //3ge 时段的相关数据 3维数据

                    val tempOne = nowtimelong - 60 * 1000 * (i-index)//当前时间－180分钟 前3小时
                    val d = new Date(tempOne)
                    val tempMinute = minuteFormat.format(d)
                    val tempNext = tempOne - 60 * 1000 * (-1)
                    val dNext = new Date(tempNext)
                    val tempMinuteNext = minuteFormat.format(dNext)

                    for((k,v) <- relations){
                        // k->camera_id ; v->HashMap
                        val map = v
                        if(index == 2 && k == camera_id){
                            if (map.containsKey(tempMinuteNext)) {
                                val info = map.get(tempMinuteNext).split("_")
                                val f = info(0).toFloat / info(1).toFloat
                                featureY += f
                            }
                        }
                        if (map.containsKey(tempMinute)){
                            val info = map.get(tempMinute).split("_")
                            val f = info(0).toFloat / info(1).toFloat
                            featuresX += f
                        } else{
                            featuresX += -1.0
                        }
                    }
                }

                if(featureY.toArray.length == 1 ){
                    val label = (featureY.toArray).head
                    val record = LabeledPoint(if ((label.toInt/10)<10) (label.toInt/10) else 10.0, Vectors.dense(featuresX.toArray))
                    //                    println(record)
                    trainSet += record
                }
            }
            trainSet.foreach(println)
            println(trainSet.length)

            val data = sc.parallelize(trainSet)
            println(data)

            // Split data into training (60%) and test (40%).
            val splits = data.randomSplit(Array(0.6, 0.4), seed = 1000L)
            val training = splits(0)
            val test = splits(1)

            if(!data.isEmpty()){

                // Run training algorithm to build the model
                val model = new LogisticRegressionWithLBFGS() //逻辑回归
                  .setNumClasses(11)
                  .run(data)

                // Compute raw scores on the test set.
                val predictionAndLabels = test.map { case LabeledPoint(label, features) =>
                    val prediction = model.predict(features)
                    (prediction, label)
                }

                predictionAndLabels.foreach(x=> println(x))

                // Get evaluation metrics.
                val metrics = new MulticlassMetrics(predictionAndLabels)
                val precision = metrics.precision
                println("Precision = " + precision)

                if(precision > 0.8){
                    val path = "hdfs://spark001:9000/model_"+camera_id+"_"+nowtimelong
                    model.save(sc, path)
                    println("saved model to "+ path)
                    jedis.hset("model", camera_id , path)
                }


            }
        })

        RedisClient.pool.returnResource(jedis)
    }
}

