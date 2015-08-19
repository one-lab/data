package org.onelab.data;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 链接池填充器，只允许一个线程执行 填充链接池中的连接到给定界线
 *
 * @author Chunliang.Han on 15/8/18.
 */
public class ConnectionPoolStuffer {

  static boolean isProcessing = false;

  static ExecutorService stuffService = Executors.newSingleThreadExecutor();

  static Runnable runner;

  public static void init(ConnectionWrap connectionWrap,
                          LinkedBlockingQueue<Connection> connectionPool, int bound) {
    runner = new Runner(connectionWrap, connectionPool, bound);
  }

  /**
   * 为给定池添加连接
   */
  public static void execute() {
    stuffService.execute(runner);
  }

  private synchronized static boolean lock() {
    if (isProcessing) {
      return false;
    }
    isProcessing = true;
    return true;
  }

  private static void unLock() {
    isProcessing = false;
  }

  static class Runner implements java.lang.Runnable {

    ConnectionWrap connectionWrap;
    LinkedBlockingQueue<Connection> connectionPool;
    int bound;

    public Runner(ConnectionWrap connectionWrap,
                  LinkedBlockingQueue<Connection> connectionPool, int bound) {
      this.connectionWrap = connectionWrap;
      this.connectionPool = connectionPool;
      this.bound = bound;
    }

    public void run() {
      if (lock()) {
        try {
          while (connectionPool.size() < bound) {
            connectionPool.offer(connectionWrap.getConnection());
          }
        } finally {
          unLock();
        }
      }
    }
  }
}
