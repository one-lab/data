package org.onelab.data;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 数据库连接管理器
 * @author Chunliang.Han on 15/8/10.
 */
public class ConnectionPool {

  /**
   * 数据库连接管理器
   */
  final ConnectionWrap connectionWrap;

  /**
   * 线程绑定数据库连接
   */
  final ThreadLocal<Connection> connectionLocal;

  /**
   * 数据库连接缓存区
   */
  final LinkedBlockingQueue<Connection> connectionPool;

  /**
   * 最大连接数
   */
  private final int maxPoolSize;

  /**
   * 最小连接数
   */
  private final int minPoolSize;

  public ConnectionPool(Config config) {
    maxPoolSize = config.getMaxPoolSize();
    minPoolSize = config.getMinPoolSize();
    connectionWrap = new ConnectionWrap(config.getUrl(),config.getUser(),config.getPassword());
    connectionLocal = new ThreadLocal<Connection>();
    connectionPool = new LinkedBlockingQueue<Connection>(maxPoolSize);
    ConnectionPoolStuffer.init(connectionWrap,connectionPool,minPoolSize);
  }

  /**
   * 关闭当前线程中连接。
   * 实际上执行的是将连接还回缓冲区的操作
   * 如果当前连接存在且已开启自动提交则执行相关操作，否则直接返回
   * 从线程变量中将连接删除
   * 如果已经关闭，则直接返回
   * 尝试将连接放回缓存区
   * 如果还回缓冲区失败则尝试关闭连接
   */
  public void close() {
    Connection connection = connectionLocal.get();
    if (connection != null &&
        connectionWrap.isAutoCommit(connection)) {
      connectionLocal.remove();
      //如果连接已关闭直接返回
      if (connectionWrap.isClosed(connection)) {
        return;
      }
      //尝试将连接放回池中
      if (!connectionPool.offer(connection)) {
        //放回池中失败，关闭连接
        connectionWrap.close(connection);
      }
    }
  }

  /**
   * 关闭会话
   * @param pstm
   */
  public void close(PreparedStatement pstm) {
    if (pstm != null) {
      try {
        pstm.close();
      } catch (SQLException e) {}
    }
    close();
  }

  /**
   * 关闭查询同时关闭会话
   * @param pstm
   * @param resultSet
   */
  public void close(PreparedStatement pstm, ResultSet resultSet) {
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (SQLException e) {}
    }
    close(pstm);
  }

  /**
   * 获取会话
   * @param sql
   * @param params
   * @return
   */
  public PreparedStatement getPreparedStatement(String sql, Object[] params) {
    try {
      PreparedStatement pstm = getConnection().prepareStatement(sql);
      if (params != null) {
        for (int i = 0; i < params.length; i++) {
          pstm.setObject(i + 1, params[i]);
        }
      }
      return pstm;
    } catch (SQLException e) {
      throw new RuntimeException("PreparedStatement 开启失败。", e);
    }
  }

  /**
   * 获取数据库连接
   * @return
   */
  public Connection getConnection() {
    Connection connection = connectionLocal.get();
    if (connection != null) {
      return connection;
    }
    connection = getConnectionFromPool();
    if (connection != null) {
      connectionLocal.set(connection);
    }
    return connection;
  }

  /**
   * 从缓冲区中获取连接
   * 如果没有则创建连接
   * @return
   */
  private Connection getConnectionFromPool() {
    Connection connection = connectionPool.poll();
    if (connection == null) {
      ConnectionPoolStuffer.execute();
      connection = connectionWrap.getConnection();
    }
    return connection;
  }
}