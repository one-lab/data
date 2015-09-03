package org.onelab.data.query;

import org.onelab.data.BeanUtil;
import org.onelab.data.meta.EntityMeta;
import org.onelab.data.meta.store.EntityMetaStore;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 实体处理器
 * 将查询结果ResultSet的指定行转换成相应实体
 * @author Chunliang.Han on 15/8/15.
 */
public class EntityProducer<T> implements ResultProducer<T> {

  private Class<T> tClass;

  public EntityProducer(Class<T> tClass) {
    this.tClass = tClass;
  }

  public T produce(ResultSetMetaData resultSetMetaData, ResultSet resultSet, int columnCount,
                   int rowIndex) throws SQLException {
    EntityMeta entityMeta = EntityMetaStore.getEntityMeta(tClass);
    T res = BeanUtil.newInstance(tClass);
    for (int i = 0; i < columnCount; i++) {
      String columnName = resultSetMetaData.getColumnName(i + 1).toLowerCase();
      Field field = entityMeta.getFieldWithName(columnName);
      if (field == null){
        continue;
      }
      Object value = resultSet.getObject(i + 1);
      BeanUtil.set(field, res, value);
    }
    return res;
  }
}
