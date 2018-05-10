package org.onelab.data;

import org.onelab.data.conn.Config;
import org.onelab.data.conn.ConnectionPool;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class ConnectionPoolTest {
  public static void main(String[] args){
    Config config = new Config();
    config.setDriver("com.mysql.jdbc.Driver");
    config.setUrl("jdbc:mysql://192.168.0.249:3306/Biz?useUnicode=true&amp;characterEncoding=UTF-8");
    config.setUser("root");
    config.setPassword("root");
    config.setMinPoolSize(5);
    config.setMaxPoolSize(15);
    config.setInvalidTime(1);
    final ConnectionPool connectionPool = new ConnectionPool(config);
    final Set<Connection> connectionSet = new HashSet<Connection>();
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    Runnable runnable = new Runnable() {
      public void run() {
        Connection connection = connectionPool.getConnection();
        connectionSet.add(connection);
        int size = connectionSet.size();
        int pullSize = connectionPool.size();
        System.out.println("total source:" + size + " pool size:" + pullSize);
        connectionPool.close();
      }
    };
    new ConnectionPool(config).getConnection();
    System.out.print(22);
    new ConnectionPool(config).getConnection();
    System.out.print(22);
//    while (true){
//      executorService.execute(runnable);
//    }
    executorService.execute(runnable);
    try {
      Thread.sleep(100000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    executorService.shutdown();
  }
}
