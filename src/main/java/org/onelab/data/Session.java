package org.onelab.data;

import org.onelab.data.meta.EntityMetaManager;
import org.onelab.data.query.ArrayProducer;
import org.onelab.data.query.MapProducer;
import org.onelab.data.query.EntityProducer;
import org.onelab.data.query.BeanProducer;
import org.onelab.data.query.ResultProducer;
import org.onelab.data.sql.Sql;
import org.onelab.data.sql.SqlRander;
import org.onelab.data.sql.SqlType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 会话
 * @author Chunliang.Han on 15/8/10.
 */
public class Session {

  ConnectionPool connectionPool = null;

  /**
   * 创建会话，只允许通过连接池创建会话，不提供无参构造子
   * @param connectionPool
   */
  public Session(ConnectionPool connectionPool) {
    if (connectionPool == null) {
      throw new RuntimeException("ConnectionPool is null");
    }
    this.connectionPool = connectionPool;
  }

  /**
   * 获取事务
   * @return
   */
  public Transaction getTransaction(){
    return new Transaction(connectionPool);
  }

  /**
   * 基本更新
   * @param sql
   * @param params
   * @return
   */
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

  /**
   * 需要数据库自动生成ID时用到的插入方法
   * @param sql
   * @param params
   * @return 数据库生成的ID
   */
  public Object executeInsert(String sql, Object[] params) {
    String insert = sql.trim().substring(0,6).toLowerCase();
    if (!insert.equals("insert")){
      throw new RuntimeException("插入数据失败[不是插入语句]:sql is " + sql+"");
    }
    PreparedStatement pstm = null;
    ResultSet rs = null;
    try {
      pstm = connectionPool.getPreparedStatement(sql, params, Statement.RETURN_GENERATED_KEYS);
      pstm.executeUpdate();
      rs = pstm.getGeneratedKeys();
      Object res = null;
      if (rs.next()){
        res = rs.getObject(1);
      }
      return res;
    } catch (SQLException e) {
      throw new RuntimeException("插入数据失败:sql is " + sql, e);
    } finally {
      connectionPool.close(pstm,rs);
    }
  }

  /**
   * 基本查询
   * @param sql
   * @param params
   * @param producer
   * @param <T>
   * @return
   */
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

  /**
   * 插入实体
   * @param entity
   * @param <T>
   * @return
   */
  public <T> T insert(T entity) {
    Sql sql = SqlRander.rander(SqlType.INSERT, entity.getClass(), entity);
    Object idValue = executeInsert(sql.getSql(), sql.getParams());
    EntityMetaManager.insertId(entity, idValue);
    return entity;
  }

  /**
   * 更新实体
   * @param entity
   * @param <T>
   */
  public <T> void update(T entity) {
    Sql sql = SqlRander.rander(SqlType.UPDATE, entity.getClass(), entity);
    executeUpdate(sql.getSql(), sql.getParams());
  }

  /**
   * 删除实体
   * @param clazz
   * @param id
   * @param <T>
   */
  public <T> void delete(Class<T> clazz, Object id) {
    Sql sql = SqlRander.rander(SqlType.DELETE, clazz, id);
    executeUpdate(sql.getSql(), sql.getParams());
  }

  /**
   * 根据ID查询实体
   * @param clazz
   * @param id
   * @param <T>
   * @return
   */
  public <T> T find(Class<T> clazz, Object id) {
    Sql sql = SqlRander.rander(SqlType.FIND, clazz, id);
    List<T> resultList = executeQuery(sql.getSql(), sql.getParams(), new EntityProducer<T>(clazz));
    if (resultList.size() > 0) {
      return resultList.get(0);
    }
    return null;
  }

  /**
   * 查询全部实体
   * @param clazz
   * @param <T>
   * @return
   */
  public <T> List<T> findAll(Class<T> clazz) {
    Sql sql = SqlRander.rander(SqlType.FIND_ALL, clazz, null);
    return executeQuery(sql.getSql(), sql.getParams(), new EntityProducer<T>(clazz));
  }

  /**
   * 返回条目为Map的查询
   * @param sql
   * @param params
   * @return
   */
  public List<Map<String, Object>> queryListMap(String sql, Object[] params) {
    return executeQuery(sql, params, new MapProducer());
  }

  public Map<String, Object> queryOneMap(String sql, Object[] params) {
    List<Map<String, Object>> resultList = queryListMap(sql, params);
    return resultList.size()>0?resultList.get(0):null;
  }

  /**
   * 返回条目为数组的查询
   * @param sql
   * @param params
   * @return
   */
  public List<Object[]> queryListArray(String sql, Object[] params) {
    return executeQuery(sql, params, new ArrayProducer());
  }

  public Object[] queryOneArray(String sql, Object[] params) {
    List<Object[]> resultList = queryListArray(sql, params);
    return resultList.size()>0?resultList.get(0):null;
  }

  /**
   * 返回条目为对象的查询
   * @param clazz
   * @param sql
   * @param params
   * @param <T>
   * @return
   */
  public <T> List<T> queryListBean(Class<T> clazz, String sql, Object[] params) {
    return executeQuery(sql, params, new BeanProducer<T>(clazz));
  }

  public <T> T queryOneBean(Class<T> clazz, String sql, Object[] params) {
    List<T> resultList = queryListBean(clazz, sql, params);
    return resultList.size()>0?resultList.get(0):null;
  }

  /**
   * 获取单一值
   * @param sql
   * @param params
   * @param <T>
   * @return
   */
  public <T> T querySingleValue(String sql, Object[] params){
    Object[] res = queryOneArray(sql, params);
    if (res!=null){
      return (T) res[0];
    }
    return null;
  }
}
