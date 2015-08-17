package org.onelab.data.meta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class EntityMetaManager {

  public static String getTableName(EntityMeta entityMeta){
    return entityMeta.getTable().name();
  }

  public static List<Column> getAllColumns(EntityMeta entityMeta){
    List<Column> columns = new ArrayList<Column>(entityMeta.columnLength());
    if (entityMeta.hasId()){
      columns.add(entityMeta.getIdMeta().getIdColumn());
    }
    columns.addAll(entityMeta.getNormalColumns());
    return columns;
  }

  public static Object getValueByColumn(EntityMeta entityMeta, Object entity, Column column){
    try {
      Field field = entityMeta.getFieldWithColumn(column);
      if (!field.isAccessible()){
        field.setAccessible(true);
      }
      return field.get(entity);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getIdName(EntityMeta entityMeta){
    return entityMeta.getIdMeta().getIdColumn().name();
  }

  public static Object getIdValue(EntityMeta entityMeta, Object entity){
    try {
      Field field = entityMeta.getIdMeta().getIdField();
      if (!field.isAccessible()){
        field.setAccessible(true);
      }
      return field.get(entity);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
