package org.onelab.data.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据库连接管理器
 * @author Chunliang.Han on 15/8/18.
 */
public class ConnectionWrap {

  private final String url;
  private final String user;
  private final String password;
  public static Set<String> drivers;

  private synchronized static void initDriver(String driver){
    if (drivers == null) {
      drivers = new HashSet<String>();
    }
    if (drivers.add(driver)){
      try {
        Class.forName(driver);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException("DriverManager加载失败 : "+driver, e);
      }
    }
  }


  public ConnectionWrap(String driver, String url, String user, String password) {
    this.url = url;
    this.user = user;
    this.password = password;
    initDriver(driver);
  }

  public Connection getConnection(){
    try {
      return DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
      throw new RuntimeException("创建数据库连接失败",e);
    }
  }

  public boolean close(Connection connection){
    try {
      connection.close();
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  public boolean isClosed(Connection connection){
    try {
      return connection.isClosed();
    } catch (SQLException e) {
      return true;
    }
  }

  public boolean isAutoCommit(Connection connection){
    try {
      return connection.getAutoCommit();
    } catch (SQLException e) {
      throw new RuntimeException("判断是否已开启事务失败",e);
    }
  }

  public void commit(Connection connection){
    try {
      connection.commit();
    } catch (SQLException e) {
      throw new RuntimeException("提交事务失败",e);
    }
  }

  public void rollback(Connection connection){
    try {
      connection.rollback();
    } catch (SQLException e) {
      throw new RuntimeException("回滚失败", e);
    }
  }

  public void setAutoCommitTrue(Connection connection) {
    try {
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new RuntimeException("setAutoCommitTrue失败", e);
    }
  }

  public void setAutoCommitFalse(Connection connection) {
    try {
      connection.setAutoCommit(false);
    } catch (SQLException e) {
      throw new RuntimeException("setAutoCommitFalse失败", e);
    }
  }
}
