package org.onelab.data.query;

import org.onelab.data.BeanUtil;
import org.onelab.data.meta.ObjectMeta;
import org.onelab.data.meta.store.ObjectMetaStore;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Chunliang.Han on 15/8/17.
 */
public class ObjectProducer<T> implements ResultProducer<T> {

  private Class<T> tClass;

  public ObjectProducer(Class<T> tClass) {
    this.tClass = tClass;
  }

  public T produce(ResultSetMetaData resultSetMetaData, ResultSet resultSet, int columnCount,
                   int rowIndex) throws SQLException {
    ObjectMeta entityMeta = ObjectMetaStore.getClassMeta(tClass);
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
