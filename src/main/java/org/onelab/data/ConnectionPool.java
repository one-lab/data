package org.onelab.data;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chunliang.Han on 15/8/10.
 */
public class ConnectionPool {

  static Logger logger = LoggerFactory.getLogger(SessionFactory.class);
  final ThreadLocal<Connection> connectionLocal = new ThreadLocal<Connection>();
  final LinkedBlockingQueue<Connection> connectionPool ;
  final AtomicInteger currConnections = new AtomicInteger(0);

  private final String url;
  private final String user;
  private final String password;
  private final int maxPoolSize;
  private final int minPoolSize;
  private final int maxConnectionsNum;

  final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

  static {
    try {
      Class.forName(DriverManager.class.getName());
    } catch (ClassNotFoundException e) {
      logger.error("DriverManager加载失败",e);
      throw new RuntimeException(e);
    }
  }

  public ConnectionPool(Config config){
    this.url = config.getUrl();
    this.user = config.getUser();
    this.password = config.getPassword();
    this.maxPoolSize = config.getMaxPoolSize();
    this.minPoolSize = config.getMinPoolSize();
    this.maxConnectionsNum = config.getMaxConnectionsNum();
    connectionPool = new LinkedBlockingQueue<Connection>(this.maxPoolSize);
  }

  public void openTransaction() {
    Connection connection = getConnection();
    try {
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      logger.error("开启事务失败",e);
      throw new RuntimeException(e);
    }
  }

  public void closeTransation() {
    close(getConnection());
  }

  public void commit() {
    Connection connection = getConnection();
    try {
      connection.commit();
    } catch (SQLException e) {
      logger.error("提交事务失败",e);
      throw new RuntimeException(e);
    }
  }

  public void rollback() {
    Connection connection = getConnection();
    try {
      connection.rollback();
    } catch (SQLException e) {
      logger.error("回滚失败",e);
      throw new RuntimeException(e);
    }
  }

  public void close(PreparedStatement pstm, ResultSet resultSet) {
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (SQLException e) {
        logger.error("ResultSet 关闭异常", e);
      }
    }
    close(pstm);
  }

  private void close(Connection connection){
    if (connection != null) {
      connectionLocal.remove();
      currConnections.decrementAndGet();
      try {
        //如果连接已关闭直接返回
        if (connection.isClosed()){
          return;
        }
        //尝试将连接放回池中，若成功当前连接数加一
        if (connectionPool.offer(connection)){
          currConnections.incrementAndGet();
        }
        //放回池中失败，关闭连接
        else {
          connection.close();
        }
        return;
      } catch (SQLException e){
        logger.error("有可能是数据库连接异常", e);
        return;
      }
    }
  }

  public void close(PreparedStatement pstm) {
    if (pstm != null) {
      try {
        pstm.close();
      } catch (SQLException e) {
        logger.error("PreparedStatement关闭异常",e);
      }
    }
    closeConnection();
  }

  public void closeConnection() {
    Connection connection = getConnection();
    boolean autoCommit;
    try {
      autoCommit = connection.getAutoCommit();
    } catch (SQLException e) {
      logger.error("获取事务是否开启失败",e);
      throw new RuntimeException(e);
    }
    if (autoCommit){
      close(connection);
    }
  }

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
      logger.error("PreparedStatement 开启失败。",e);
      throw new RuntimeException(e);
    }
  }

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

  private Connection getConnectionFromPool() {
    if (connectionPool.size()<=minPoolSize && currConnections.get()<maxConnectionsNum){
      executorService.execute(new ConnectionCreator());
    }
    Connection connection = connectionPool.poll();
    if (connection==null){
      connection = createConnection();
    }
    return connection;
  }

  class ConnectionCreator implements Runnable{

    public void run() {
      while (connectionPool.size()<=maxPoolSize && currConnections.get()<maxConnectionsNum){
        connectionPool.offer(createConnection());
      }
    }
  }

  private Connection createConnection(){
    try {
      int currSize = currConnections.incrementAndGet();
      if (currSize <= maxConnectionsNum){
        return DriverManager.getConnection(url, user, password);
      } else {
        currConnections.decrementAndGet();
        throw new RuntimeException("too many connections , it must <= "+maxConnectionsNum);
      }
    } catch (SQLException e) {
      currConnections.decrementAndGet();
      throw new RuntimeException("connection 获取失败！",e);
    }
  }
}
