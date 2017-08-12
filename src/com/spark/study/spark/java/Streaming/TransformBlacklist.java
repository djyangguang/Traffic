package com.spark.study.spark.java.Streaming;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import com.google.common.base.Optional;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yg on 2017/8/2.
 */
public class TransformBlacklist {


    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("wordcount").setMaster("local[2]");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));


        // �û���������վ�ϵĹ����Խ��е��,���֮���ǲ���Ҫ����ʵʱ�Ʒ�,���Ƕ���ˢ������,������һ��������
        // ֻҪ�Ǻ������е��û�����Ĺ��,���Ǿ͸����˵�

        // ��ģ��һ�ݺ�����RDD,true˵������,false˵��������
        List<Tuple2<String,Boolean>> blacklist = new ArrayList<Tuple2<String, Boolean>>();
        blacklist.add(new Tuple2<String, Boolean>("laoyang", true));

        @SuppressWarnings("deprecation")
        final JavaPairRDD<String,Boolean> blacklistRDD = jssc.sc().parallelizePairs(blacklist);//�ѱ��ص�����ת����RDD java
        // ��û����ʿת��

        // ��־�����ʽ��,��˼���˾Ϳ���,����date username�ķ�ʽ
        JavaReceiverInputDStream<String> adsClickLogDStream = jssc.socketTextStream("spark001", 9999);

        // ����Ҫ�ȶ���������ݽ���ת��������� (username, date username) �Ա��������������е�RDD�Ͷ���õĺ�����RDD����join����
        JavaPairDStream<String, String> userAdsClickLogDStream = adsClickLogDStream.mapToPair(

                new PairFunction<String, String, String>(){

                    private static final long serialVersionUID = 1L;

                    @Override
                    public Tuple2<String, String> call(String adsClickLog)
                            throws Exception {
                        return new Tuple2<String,String>(adsClickLog.split(" ")[1], adsClickLog);
                    }


                });

        // ʵʱ���к���������,ִ��transform����,��ÿ��batch��RDD,�������RDD����join
        JavaDStream<String> validAdsClickLogDStream = userAdsClickLogDStream.transform(
                new Function<JavaPairRDD<String,String>, JavaRDD<String>>(){ //JavaRDD<String>> �����Ҫ�Ľ�� ֻ��Ҫ��־

                    private static final long serialVersionUID = 1L;

                    @Override
                    public JavaRDD<String> call(JavaPairRDD<String, String> userAdsClickLogRDD)
                            throws Exception {
                        // ����Ϊʲô����������,��Ϊ������ÿ���û����ں�������,����ֱ����join,��ôû���ں������е�����,�޷�join���ͻᶪ��
                        // string���û�,string����־,�Ƿ��ں���������Optional
                        JavaPairRDD<String, Tuple2<String, Optional<Boolean>>> joinedRDD =
                                userAdsClickLogRDD.leftOuterJoin(blacklistRDD); //�Ѻ��������˹��˵�


                        JavaPairRDD<String, Tuple2<String, Optional<Boolean>>> filteredRDD =
                                joinedRDD.filter(
                                        new Function<Tuple2<String,
                                                Tuple2<String, Optional<Boolean>>>, Boolean>(){

                                            private static final long serialVersionUID = 1L;

                                            @Override
                                            public Boolean call(
                                                    Tuple2<String, Tuple2<String, Optional<Boolean>>> tuple)
                                                    throws Exception {
                                                // ����tuple����ÿ���û���Ӧ�ķ�����־���ں�������״̬
                                                // tuple._2._2.==Tuple2<String, Optional<Boolean>>>
                                                if(tuple._2._2.isPresent() && tuple._2._2.get()){
                                                    return false;
                                                }
                                                return true;
                                            }

                                        });
                        // ����Ϊֹ,filteredRDD�о�ֻʣ��û�б����˵������û���,��map����ת��������Ҫ�ĸ�ʽ,����ֻҪ�����־
                        JavaRDD<String> validAdsClickLogRDD = filteredRDD.map(
                                new Function<Tuple2<String, Tuple2<String, Optional<Boolean>>>,String>(){

                                    private static final long serialVersionUID = 1L;

                                    @Override
                                    public String call(
                                            Tuple2<String, Tuple2<String, Optional<Boolean>>> tuple)
                                            throws Exception {
                                        return tuple._2._1;
                                    }

                                });
                        return validAdsClickLogRDD; //���ݹ��˵��������ĵ����־
                    }

                });

        // �����Ϳ���д��Kafka�м����Ϣ����,��Ϊ���Ʒѷ������Ч���������
        validAdsClickLogDStream.print();

        jssc.start();
        jssc.awaitTermination();
        jssc.close();
    }
}
