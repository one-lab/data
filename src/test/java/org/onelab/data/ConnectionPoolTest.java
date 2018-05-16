package org.onelab.data;

import org.onelab.data.conn.Config;
import org.onelab.data.conn.ConnectionPool;

import java.sql.Connection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class ConnectionPoolTest {
  public static void main(String[] args) throws InterruptedException {
    Config config = new Config();
    config.setDriver("com.mysql.jdbc.Driver");
    config.setUrl("jdbc:mysql://192.168.0.249:3306/Biz?useUnicode=true&amp;characterEncoding=UTF-8");
    config.setUser("root");
    config.setPassword("root");
    config.setMinPoolSize(10);
    config.setMaxPoolSize(40);
    config.setInvalidTime(1);
    final ConnectionPool connectionPool = new ConnectionPool(config);
    final Set<Connection> connectionSet = new HashSet<Connection>();
    final long lastTime = System.currentTimeMillis()/1000;
    ExecutorService executorService = Executors.newFixedThreadPool(1000);
    Runnable runnable = new Runnable() {
      AtomicInteger printTime = new AtomicInteger();
      AtomicLong count = new AtomicLong(0);
      public void run() {
        try {
          Connection connection = connectionPool.getConnection();
          connectionSet.add(connection);
          connectionPool.close();
          count.addAndGet(1);
          if (System.currentTimeMillis()/1000-lastTime >= printTime.intValue()){
            printTime.addAndGet(1);
            System.out.println("total:"+count.longValue()+
                               " tps:"+count.longValue()/(System.currentTimeMillis()/1000-lastTime)+
                               " source:" + connectionSet.size() +
                               " pool size:" + connectionPool.size());
          }
        } catch (Throwable t){
          t.printStackTrace();
        }
      }
    };
    while (true){
//      Thread.sleep(1);
      executorService.execute(runnable);
    }
  }
}
