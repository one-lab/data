package org.onelab.data;

import org.onelab.data.meta.EntityMetaManager;
import org.onelab.data.query.ArrayProducer;
import org.onelab.data.query.MapProducer;
import org.onelab.data.query.EntityProducer;
import org.onelab.data.query.ObjectProducer;
import org.onelab.data.query.ResultProducer;
import org.onelab.data.sql.Sql;
import org.onelab.data.sql.SqlRander;
import org.onelab.data.sql.SqlType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Chunliang.Han on 15/8/10.
 */
public class Session {

  ConnectionPool connectionPool = null;

  public Session(ConnectionPool connectionPool) {
    if (connectionPool == null) {
      throw new RuntimeException("ConnectionPool is null");
    }
    this.connectionPool = connectionPool;
  }

  public int executeUpdate(String sql, Object[] params) {

    PreparedStatement pstm = connectionPool.getPreparedStatement(sql, params);
    try {
      int res = pstm.executeUpdate();
      return res;
    } catch (SQLException e) {
      throw new RuntimeException("更新数据失败:sql is " + sql, e);
    } finally {
      connectionPool.close(pstm);
    }
  }

  public <T> List<T> executeQuery(String sql, Object[] params, ResultProducer<T> producer) {
    List<T> resultList = new ArrayList<T>(0);
    PreparedStatement pstm = connectionPool.getPreparedStatement(sql, params);
    ResultSet resultSet = null;
    try {
      resultSet = pstm.executeQuery();
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      int len = resultSetMetaData.getColumnCount();
      int i = 0;
      while (resultSet.next()) {
        resultList.add(producer.produce(resultSetMetaData, resultSet, len, i++));
      }
    } catch (SQLException e) {
      throw new RuntimeException("查询数据失败:sql[" + sql + "]", e);
    } finally {
      connectionPool.close(pstm, resultSet);
    }
    return resultList;
  }

  public <T> T insert(T entity) {
    Sql sql = SqlRander.rander(SqlType.INSERT, entity.getClass(), entity);
    executeUpdate(sql.getSql(), sql.getParams());
    EntityMetaManager.testInsertId(entity, sql.getParams());
    return entity;
  }

  public <T> void update(T entity) {
    Sql sql = SqlRander.rander(SqlType.UPDATE, entity.getClass(), entity);
    executeUpdate(sql.getSql(), sql.getParams());
  }

  public <T> void delete(Class<T> clazz, Object id) {
    Sql sql = SqlRander.rander(SqlType.DELETE, clazz, id);
    executeUpdate(sql.getSql(), sql.getParams());
  }

  public <T> T find(Class<T> clazz, Object id) {
    Sql sql = SqlRander.rander(SqlType.FIND, clazz, id);
    List<T> resultList = executeQuery(sql.getSql(), sql.getParams(), new EntityProducer<T>(clazz));
    if (resultList.size() > 0) {
      return resultList.get(0);
    }
    return null;
  }

  public <T> List<T> findAll(Class<T> clazz) {
    Sql sql = SqlRander.rander(SqlType.FIND_ALL, clazz, null);
    return executeQuery(sql.getSql(), sql.getParams(), new EntityProducer<T>(clazz));
  }

  public List<Map<String, Object>> queryAsMap(String sql, Object[] params) {
    return executeQuery(sql, params, new MapProducer());
  }

  public List<Object[]> queryAsArray(String sql, Object[] params) {
    return executeQuery(sql, params, new ArrayProducer());
  }

  public <T> List<T> queryAsObject(Class<T> clazz, String sql, Object[] params) {
    return executeQuery(sql, params, new ObjectProducer<T>(clazz));
  }
}
