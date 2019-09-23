package org.onelab.data.conn;

import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class ConnectionPoolTest {

  volatile int max;

  public ConnectionPool getConnectionPool(){
    Config config = new Config();
    config.setDriver("com.mysql.jdbc.Driver");
    config.setUrl("jdbc:mysql://127.0.0.1:3306/sm?useUnicode=true&amp;characterEncoding=UTF-8");
    config.setUser("root");
    config.setPassword("root");
    config.setMinPoolSize(3);
    config.setMaxPoolSize(11);
    config.setInvalidTime(30);
    return new ConnectionPool(config);
  }

  @Test
  public void testConnection() throws InterruptedException {

    final ConnectionPool connectionPool = getConnectionPool();

    int t = 100;

    ExecutorService executorService = Executors.newFixedThreadPool(t);

    final CountDownLatch countDownLatch = new CountDownLatch(t);

    final AtomicInteger integer = new AtomicInteger();

    long start = System.currentTimeMillis();

    final int cnt = 1000;

    for (int i=0;i<t;i++){
      executorService.execute(new Runnable() {
        public void run() {
          int n = cnt;
          while (n-->0){
            Connection connection = connectionPool.getConnection();
            int curr = integer.incrementAndGet();
            if (curr > max){
              max = curr;
            }
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            connectionPool.close(connection);
            integer.decrementAndGet();
          }
          countDownLatch.countDown();
        }
      });
    }
    countDownLatch.await();
    System.out.print((System.currentTimeMillis() - start)/1000);
    System.out.println(" : " + t*cnt + " : " + max);
    System.out.println("pool size : " + connectionPool.size());
    assertEquals(max<=t, true);
    Thread.sleep(30000);
    System.out.println("pool size : " + connectionPool.size());
  }

  @Test
  public void testCreate() throws InterruptedException, SQLException {

    //创建连接池
    ConnectionPool connectionPool = getConnectionPool();

    //连接池中存有的数量=3
    assertEquals(connectionPool.size()<3,true);

    //等待1s，此时连接池中存有的数量=3
    Thread.sleep(1000);
    assertEquals(connectionPool.size(),3);

    //获取一个连接，此时连接池中存有的数量=2
    Connection connection = connectionPool.getConnection();
    assertEquals(connectionPool.size(),2);
    assertEquals(connectionPool.connPool.contains(connection),false);

    Thread.sleep(1000);

    //归还连接，此时连接数4
    connectionPool.close(connection);
    assertEquals(connectionPool.size(),4);
    assertEquals(connectionPool.connPool.contains(connection),true);


    //睡眠35秒，此时原有连接池中连接应该都销毁掉了
    Thread.sleep(35000);
    assertEquals(connectionPool.connPool.size(), 0);
    assertEquals(connection.isClosed(), true);
    assertEquals(connectionPool.connPool.contains(connection), false);

    connection = connectionPool.getConnection();
    assertEquals(connectionPool.size(),0);
    Thread.sleep(1000);
    assertEquals(connectionPool.size(),3);
  }
}
