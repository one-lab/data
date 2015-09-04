package org.onelab.data;

import java.sql.Connection;

/**
 * 简单的事务处理器
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
    if (isSubmit){
      try {
        connectionWrap.setAutoCommitTrue(connection);
      } catch (Exception e){
        connectionWrap.close(connection);
        throw new RuntimeException(e);
      } finally {
        connectionPool.close(connection);
      }
    } else {
      connectionWrap.close(connection);
      connectionPool.close(connection);
    }
  }
}
