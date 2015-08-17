package org.onelab.data.meta;

import org.onelab.data.BeanUtil;
import org.onelab.data.meta.store.EntityMetaStore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class EntityMetaManager {

  public static String getTableName(EntityMeta entityMeta) {
    return entityMeta.getTable().name();
  }

  public static List<Column> getAllColumns(EntityMeta entityMeta) {
    List<Column> columns = new ArrayList<Column>(entityMeta.columnLength());
    if (entityMeta.hasId()) {
      columns.add(entityMeta.getIdMeta().getIdColumn());
    }
    columns.addAll(entityMeta.getNormalColumns());
    return columns;
  }

  public static Object getValueByColumn(EntityMeta entityMeta, Object entity, Column column) {
    try {
      Field field = entityMeta.getFieldWithColumn(column);
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      return field.get(entity);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getIdName(EntityMeta entityMeta) {
    return entityMeta.getIdMeta().getIdColumn().name();
  }

  public static Object getIdValue(EntityMeta entityMeta, Object entity) {
    Field field = entityMeta.getIdMeta().getIdField();
    return BeanUtil.get(field, entity);
  }

  public static <T> void testInsertId(T entity, Object[] params) {
    EntityMeta entityMeta = EntityMetaStore.getEntityMeta(entity.getClass());
    if (entityMeta.hasId()) {
      Object value = getIdValue(entityMeta, entity);
      if (value == null) {
        Field field = entityMeta.getIdMeta().getIdField();
        BeanUtil.set(field, entity, params[0]);
      }
    }
  }
}
