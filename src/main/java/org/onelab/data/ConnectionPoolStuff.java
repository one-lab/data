package org.onelab.data;

import java.sql.Connection;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 链接池填充器，只允许一个线程执行
 * 填充链接池中的连接到给定界线
 * @author Chunliang.Han on 15/8/18.
 */
public class ConnectionPoolStuff {

  static boolean isProcessing = false;

  /**
   * 为给定池添加连接
   * @param connectionWrap
   * @param connectionPool 连接池
   * @param bound 填充到多少为止
   */
  public static void start(ConnectionWrap connectionWrap, LinkedBlockingQueue<Connection> connectionPool,int bound){
    if(lock()){
      try {
        while (connectionPool.size()<bound){
          connectionPool.offer(connectionWrap.getConnection());
        }
      } finally {
        unLock();
      }
    }
  }

  private synchronized static boolean lock(){
    if (isProcessing){
      return false;
    }
    isProcessing = true;
    return true;
  }
  private static void unLock(){
    isProcessing = false;
  }
}
