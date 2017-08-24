package com.yg.spark.recommend

import org.apache.spark.mllib.classification.{LogisticRegressionModel, LogisticRegressionWithSGD}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.LogisticRegressionDataGenerator
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
/**
  * Created by yg on 2017/8/17.
  */
class AUC {

}
object AUC{
  def main(args: Array[String]): Unit = {
LogisticRegressionDataGenerator//数据生成器
    val conf: SparkConf = new SparkConf().setAppName("test").setMaster("local")
    val sc = new SparkContext(conf)
    //数据生成使用LogisticRegressionDataGenerator，可以用过MLUtils快速加载数据
    val trainData: RDD[LabeledPoint] = MLUtils.loadLabeledPoints(sc,"D:\\bigdata\\recommend\\trainAUC")
    val testData: RDD[LabeledPoint] = MLUtils.loadLabeledPoints(sc,"D:\\bigdata\\recommend\\test")
    //模型训练
    val model: LogisticRegressionModel = LogisticRegressionWithSGD.train(trainData,10,0.1)
    //把测试数据的标签和特征值分开
    val test =testData.map(_.features)
    val label: RDD[Double] = testData.map(_.label)//所有的标签
    //预测结果为result
    val result: RDD[Double] = model.predict(test)
    //计算准确率，对比预测结果和原结果，相同的过来出来是预测对的，除以总数为准确率
    val acc: Double =label.zip(result).filter(x=>{x._1.equals(x._2)}).count()/label.count().toDouble
    println("acc============="+acc)

    //通过使用sigmod函数得到评分,每条数据附带分类标签，目的是为了后面排序后能区分是不是正例
    //排序，从小到大排序，排完的下标就是rank值
    /**
      *  计算AOC值的时候要预测 后拿到评分 后排序 找正例的位置相加
      */
    val scores  = test.map(features =>{
      val margin = dot(features, model.weights) + model.intercept
      val score = 1.0 / (1.0 + math.exp(-margin))
      score
    }).zip(label).sortBy(_._1).zipWithIndex()
    //上面排序以后，这里判断是正例返回的位置是下标，否则为负例，返回0，加起来就是正例位置和
    val indexSum = scores.map(x=>{
      if(x._1._2.equals("1.0")){
        x._2
      }else{
        0
      }
    }).sum()
    //label正例是1.0负例是0.0所以加和就是正例个数
    val M = label.sum()
    val N = label.count()-M
    //带入公式
    val auc = (indexSum - (M*(M+1)/2))/(M*N).toDouble
    println("auc============"+auc)
  }

}
