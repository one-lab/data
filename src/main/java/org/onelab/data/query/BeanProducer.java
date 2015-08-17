package org.onelab.data.query;

import org.onelab.data.BeanUtil;
import org.onelab.data.meta.BeanMeta;
import org.onelab.data.meta.store.BeanMetaStore;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 对象处理器
 * 将查询结果ResultSet的指定行转换成相应对象
 * @author Chunliang.Han on 15/8/17.
 */
public class BeanProducer<T> implements ResultProducer<T> {

  private Class<T> tClass;

  public BeanProducer(Class<T> tClass) {
    this.tClass = tClass;
  }

  public T produce(ResultSetMetaData resultSetMetaData, ResultSet resultSet, int columnCount,
                   int rowIndex) throws SQLException {
    BeanMeta entityMeta = BeanMetaStore.getClassMeta(tClass);
    T res = BeanUtil.newInstance(tClass);
    for (int i = 0; i < columnCount; i++) {
      String columnName = resultSetMetaData.getColumnName(i + 1).toLowerCase();
      Object value = resultSet.getObject(i + 1);
      Field field = entityMeta.getFieldWithName(columnName);
      BeanUtil.set(field, res, value);
    }
    return res;
  }
}
