package org.onelab.data.query;

import org.onelab.data.meta.EntityMeta;
import org.onelab.data.meta.EntityMetaStore;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class EntityProducer<T> implements ResultProducer<T> {

  private Class tClass;

  public EntityProducer(Class tClass){
    this.tClass = tClass;
  }

  public T produce(ResultSetMetaData resultSetMetaData, ResultSet resultSet, int columnCount,
                   int rowIndex) throws SQLException {
    EntityMeta entityMeta = EntityMetaStore.getEntityMeta(tClass);
    try {
      T res = (T)tClass.newInstance();
      for (int i=0;i<columnCount;i++){
        String columnName = resultSetMetaData.getColumnName(i+1).toLowerCase();
        Object value = resultSet.getObject(i + 1);
        Field field = entityMeta.getFieldWithName(columnName);
        if (!field.isAccessible()) {
          field.setAccessible(true);
        }
        field.set(res,value);
      }
      return res;
    } catch (InstantiationException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
