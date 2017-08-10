package com.spark.study.spark.java.SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;

/**
 * Created by yg on 2017/8/10.
 */
public class HiveDataSource {
    // scp lib/mysql-connector-java-5.1.32-bin.jar root@spark001:/usr/soft/jdk1.7.0_71/jre/lib/ext/
    public static void main (String[] args){
        SparkConf conf = new SparkConf().setAppName("a").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(conf);
        HiveContext hiveContext = new HiveContext(sc.sc());
        hiveContext.sql("DROP TABLE IF EXISTS t_ord_Saleorder");
        hiveContext.sql("CREATE TABLE IF NOT EXISTS t_ord_saleorder (number STRING,customer STRING)");
        hiveContext.sql("LOAD DATA LOCAL INPATH '/use/hdaoop/spark/t_ord_saleorder.text' INTO TABLE t_ord_saleorder");
        hiveContext.sql("LOAD DATA LOCAL INPATH '/use/hdaoop/spark/t_ord_saleorderentry.text' INTO TABLE " +
                "t_ord_saleorder");
        DataFrame salorderDF = hiveContext.sql("select * from T_INV_PaperInWarehsBill t1\n" +
                "left join t_inv_paperinwarehsbillentry t2 on t2.fparentid =t1.fid ");
        Row[] results =salorderDF.collect();
        for(Row row :results){
            System.out.println(row);
        }
        // 我们得到的这个数据是不是还得存回HIVE表中啊

        hiveContext.sql("DROP TABLE IF EXISTS T_INV_PaperInWarehsBill ");
        salorderDF.saveAsTable("T_INV_PaperInWarehsBill");
        DataFrame temp = hiveContext.table("T_INV_PaperInWarehsBill");		// 然后如果是一个HIVE表我们怎么给它读入进来变成一个DataFrame呢

        Row[] rows = temp.collect();
        for(Row row : rows){
            System.out.println(row);

        }
        sc.close();


    }
}
