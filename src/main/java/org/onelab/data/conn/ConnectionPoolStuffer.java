package org.onelab.data.conn;

import java.sql.Connection;

/**
 * 链接池管理器
 *
 * @author Chunliang.Han on 15/8/18.
 */
public class ConnectionPoolStuffer {

  final Thread executor ;

  final ConnectionPool pool ;

  public ConnectionPoolStuffer(ConnectionPool connectionPool){
    pool = connectionPool;
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

      InitPoolThread(){
        super("InitPoolThread");
      }

      @Override
      public void run() {
        while (true){
          try {
            //最小连接数小于等于0，睡眠5秒，跳过本次执行
            if (pool.minPoolSize <= 0){
              Thread.sleep(5000);
              continue;
            }
            while (pool.size() < pool.minPoolSize) {
              pool.connPool.offer(pool.connWrap.getConnection());
            }
            pool.waitToInitPool();
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

      InvalidPoolThread(){
        super("InvalidPoolThread");
      }

      /**
       * 上次更新连接池时间
       */
      long lastClearTime = System.currentTimeMillis();

      @Override
      public void run() {
        while (true){
          try {
            //睡眠5秒
            Thread.sleep(5000);
            if (pool.invalidTime <= 0){
              continue;
            }
            //当前时间-上次更新时间>失效时间 : 清空当前连接池
            if (System.currentTimeMillis() - lastClearTime > pool.invalidTime*1000){
              //当前连接数
              int poolSize = pool.size();
              while (poolSize-- > 0){
                Connection conn = pool.connPool.poll();
                if (conn == null){
                  break;
                }
                pool.connWrap.close(conn);
              }
              lastClearTime = System.currentTimeMillis();
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
