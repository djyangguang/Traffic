package com.spark.study.spark.java.Streaming;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * Created by yg on 2017/8/4.
 */
public class ConnectionPool {
    private static LinkedList<Connection> connectionQueue;
    static{
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public synchronized  static Connection getConnection(){
        try{

            if(connectionQueue==null){
                connectionQueue = new LinkedList<Connection>();
                for(int i = 0;i<10;i++){
                    Connection conn = DriverManager.getConnection("jdbc:mysql://node1:3306/spark",
                                        "root","yang123");
                }
            }

        }  catch(SQLException e) {
            e.printStackTrace();
        }
        return connectionQueue.poll();
    }
    public static void returnConnection(Connection conn){
       connectionQueue.push(conn);
    }
}
