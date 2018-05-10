package org.onelab.data.conn;

import java.sql.Connection;

/**
 * 链接池管理器
 *
 * @author Chunliang.Han on 15/8/18.
 */
public class ConnectionPoolStuffer {

  /**
   * 上次更新连接池时间
   */
  long lastClearTime ;

  final Thread executor ;

  final ConnectionPool pool ;

  public ConnectionPoolStuffer(ConnectionPool connectionPool){
    pool = connectionPool;
    lastClearTime = System.currentTimeMillis();
    executor = new Thread(new Runner());
    executor.setDaemon(true);
    executor.start();
  }

  class Runner implements java.lang.Runnable {

    public void run() {
      new InitPoolThread().start();
      new InvalidPoolThread().start();
    }

    /**
     * 保证连接池中连接数维持在最小连接数
     */
    class InitPoolThread extends Thread {

      @Override
      public void run() {
        while (true){
          try {
            Thread.sleep(100);
            while (pool.size() < pool.minPoolSize) {
              pool.connPool.offer(pool.connWrap.getConnection());
            }
          } catch (Throwable t){
            t.printStackTrace();
            try {
              //发生异常,睡眠1分钟
              Thread.sleep(60000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }

    /**
     * 定期更新连接
     */
    class InvalidPoolThread extends Thread {

      @Override
      public void run() {
        while (true){
          try {
            //睡眠3秒
            Thread.sleep(3000);
            //当前时间-上次更新时间>失效时间 : 清空当前连接池
            long invalidTime = pool.invalidTime*60*1000;
            if (invalidTime > 0){
              if (System.currentTimeMillis() - lastClearTime > invalidTime){
                //当前连接数
                int poolSize = pool.size();
                while (--poolSize>0){
                  Connection conn = pool.connPool.poll();
                  if (conn == null){
                    break;
                  }
                  pool.connWrap.close(conn);
                }
                lastClearTime = System.currentTimeMillis();
              }
            }
          } catch (Throwable t) {
            t.printStackTrace();
            try {
              //发生异常,睡眠1分钟
              Thread.sleep(60000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      }
    }
  }
}
