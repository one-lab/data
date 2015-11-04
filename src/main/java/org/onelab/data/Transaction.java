package org.onelab.data;

import java.sql.Connection;

/**
 * 简单的事务处理器（暂时不支持嵌套事务）
 * @author Chunliang.Han on 15/9/4.
 */
public class Transaction {
  Connection connection;
  ConnectionWrap connectionWrap;
  ConnectionPool connectionPool;
  boolean isSubmit = false;
  public Transaction(ConnectionPool connectionPool){
    this.connectionPool = connectionPool;
    this.connectionWrap = connectionPool.connectionWrap;
    this.connection = connectionPool.getConnection();
  }
  public boolean begin(){
    if (connectionWrap.isAutoCommit(connection)){
      connectionWrap.setAutoCommitFalse(connection);
      return true;
    }
    return false;
  }
  public void submit(){
    if (!connectionWrap.isAutoCommit(connection)){
      connectionWrap.commit(connection);
      isSubmit = true;
    }
  }
  public void end(){
    try {
      if (!isSubmit) {
        connectionWrap.rollback(connection);
      }
      connectionWrap.setAutoCommitTrue(connection);
    } catch (Throwable t){
      connectionWrap.close(connection);
      throw new RuntimeException(t);
    } finally {
      connectionPool.close(connection);
    }
  }
}
