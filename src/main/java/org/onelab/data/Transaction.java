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
      //设置自动提交
      connectionWrap.setAutoCommitTrue(connection);
      //关闭连接
      connectionPool.close(connection);
    } else {
      connectionWrap.rollback(connection);
      connectionWrap.close(connection);
      connectionPool.close(connection);
    }
  }
}
