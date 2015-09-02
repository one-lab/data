package org.onelab.data;

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
    config.setUrl("jdbc:mysql://127.0.0.1:3306/sm?useUnicode=true&amp;characterEncoding=UTF-8");
    config.setUser("root");
    config.setPassword("root");
    config.setMinPoolSize(5);
    config.setMaxPoolSize(15);
    final ConnectionPool connectionPool = new ConnectionPool(config);
    final Set<Connection> connectionSet = new HashSet<Connection>();
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    Runnable runnable = new Runnable() {
      public void run() {
        Connection connection = connectionPool.getConnection();
        connectionSet.add(connection);
        int size = connectionSet.size();
        int pullSize = connectionPool.connectionPool.size();
        System.out.println("total source:" + size + " pool size:" + pullSize);
        connectionPool.close();
      }
    };
    while (true){
      executorService.execute(runnable);
    }
  }
}
