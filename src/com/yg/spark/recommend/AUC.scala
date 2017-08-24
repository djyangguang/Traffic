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
LogisticRegressionDataGenerator//����������
    val conf: SparkConf = new SparkConf().setAppName("test").setMaster("local")
    val sc = new SparkContext(conf)
    //��������ʹ��LogisticRegressionDataGenerator�������ù�MLUtils���ټ�������
    val trainData: RDD[LabeledPoint] = MLUtils.loadLabeledPoints(sc,"D:\\bigdata\\recommend\\trainAUC")
    val testData: RDD[LabeledPoint] = MLUtils.loadLabeledPoints(sc,"D:\\bigdata\\recommend\\test")
    //ģ��ѵ��
    val model: LogisticRegressionModel = LogisticRegressionWithSGD.train(trainData,10,0.1)
    //�Ѳ������ݵı�ǩ������ֵ�ֿ�
    val test =testData.map(_.features)
    val label: RDD[Double] = testData.map(_.label)//���еı�ǩ
    //Ԥ����Ϊresult
    val result: RDD[Double] = model.predict(test)
    //����׼ȷ�ʣ��Ա�Ԥ������ԭ�������ͬ�Ĺ���������Ԥ��Եģ���������Ϊ׼ȷ��
    val acc: Double =label.zip(result).filter(x=>{x._1.equals(x._2)}).count()/label.count().toDouble
    println("acc============="+acc)

    //ͨ��ʹ��sigmod�����õ�����,ÿ�����ݸ��������ǩ��Ŀ����Ϊ�˺���������������ǲ�������
    //���򣬴�С��������������±����rankֵ
    /**
      *  ����AOCֵ��ʱ��ҪԤ�� ���õ����� ������ ��������λ�����
      */
    val scores  = test.map(features =>{
      val margin = dot(features, model.weights) + model.intercept
      val score = 1.0 / (1.0 + math.exp(-margin))
      score
    }).zip(label).sortBy(_._1).zipWithIndex()
    //���������Ժ������ж����������ص�λ�����±꣬����Ϊ����������0����������������λ�ú�
    val indexSum = scores.map(x=>{
      if(x._1._2.equals("1.0")){
        x._2
      }else{
        0
      }
    }).sum()
    //label������1.0������0.0���ԼӺ;�����������
    val M = label.sum()
    val N = label.count()-M
    //���빫ʽ
    val auc = (indexSum - (M*(M+1)/2))/(M*N).toDouble
    println("auc============"+auc)
  }

}
