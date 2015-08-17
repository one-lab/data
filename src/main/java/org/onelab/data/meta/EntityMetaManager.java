package org.onelab.data.meta;

import org.onelab.data.BeanUtil;
import org.onelab.data.meta.store.EntityMetaStore;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

/**
 * 实体描述对象管理器
 * @author Chunliang.Han on 15/8/15.
 */
public class EntityMetaManager {

  /**
   * 根据实体描述对象获取表名
   * @param entityMeta
   * @return
   */
  public static String getTableName(EntityMeta entityMeta) {
    return entityMeta.getTable().name();
  }

  /**
   * 根据实体描述对象获取实体的所有列，如果存在ID列，则ID列会是第一列
   * @param entityMeta
   * @return
   */
  public static List<Column> getAllColumns(EntityMeta entityMeta) {
    List<Column> columns = new ArrayList<Column>(entityMeta.columnLength());
    if (entityMeta.hasId()) {
      columns.add(entityMeta.getIdMeta().getIdColumn());
    }
    columns.addAll(entityMeta.getNormalColumns());
    return columns;
  }

  /**
   * 获取相应列的值
   * @param entityMeta
   * @param entity
   * @param column
   * @return
   */
  public static Object getValueByColumn(EntityMeta entityMeta, Object entity, Column column) {
    Field field = entityMeta.getFieldWithColumn(column);
    return BeanUtil.get(field, entity);
  }

  /**
   * 根据实体描述对象获取ID列的名称
   * @param entityMeta
   * @return
   */
  public static String getIdName(EntityMeta entityMeta) {
    return entityMeta.getIdMeta().getIdColumn().name();
  }

  /**
   * 根据实体描述对象获取ID列的值
   * @param entityMeta
   * @param entity
   * @return
   */
  public static Object getIdValue(EntityMeta entityMeta, Object entity) {
    if (entityMeta.hasId()){
      Field field = entityMeta.getIdMeta().getIdField();
      return BeanUtil.get(field, entity);
    } else {
      throw new RuntimeException("实体："+entityMeta.getClass()+"无ID");
    }
  }

  /**
   * 尝试给实体ID赋值
   * @param entity
   * @param idValue
   * @param <T>
   */
  public static <T> void testInsertId(T entity, Object idValue) {
    EntityMeta entityMeta = EntityMetaStore.getEntityMeta(entity.getClass());
    if (entityMeta.hasId()) {
      Field field = entityMeta.getIdMeta().getIdField();
      BeanUtil.set(field, entity, idValue);
    } else {
      throw new RuntimeException("实体："+entityMeta.getClass()+"无ID");
    }
  }
}
