package org.onelab.data.meta.store;

import org.onelab.data.meta.EntityMeta;
import org.onelab.data.meta.IDMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 实体类描述对象生成存储器
 *
 * @author Chunliang.Han on 15/8/10.
 */
public class EntityMetaStore {

  private static final Object localLock = new Object();

  public static final Map<Class, EntityMeta> local = new HashMap<Class, EntityMeta>();

  /**
   * 生成实体描述对象
   * @param clazz
   * @return
   */
  private static EntityMeta createEntityMeta(Class clazz) {
    EntityMeta entityMeta = new EntityMeta();
    entityMeta.setBeanClass(clazz);
    entityMeta.setTable((Table) clazz.getAnnotation(Table.class));
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      Column column = field.getAnnotation(Column.class);
      if (column != null) {
        String columnName = column.name();
        if (columnName == null || columnName.length()==0){
          throw new RuntimeException("Entity " + clazz + ", Field "+field.getName()+" must be have Column name !");
        }
        entityMeta.putFieldWithName(columnName, field);
        entityMeta.putFieldWithColumn(column, field);
        Id id = field.getAnnotation(Id.class);
        if (id != null) {
          IDMeta idMeta = new IDMeta(field, column, field.getAnnotation(GeneratedValue.class));
          entityMeta.setIdMeta(idMeta);
        } else {
          entityMeta.getNormalColumns().add(column);
        }
      }
    }
    return entityMeta;
  }

  /**
   * 获取实体描述对象
   * 如果本地缓存中没有则创建并装入缓存
   * @param clazz
   * @return
   */
  public static EntityMeta getEntityMeta(Class clazz) {
    EntityMeta entityMeta = local.get(clazz);
    if (entityMeta == null) {
      synchronized (localLock){
        if (entityMeta == null){
          entityMeta = createEntityMeta(clazz);
          if (entityMeta.getTable() == null) {
            throw new RuntimeException("Entity " + clazz + " must be have Table annotation !");
          }
          String tableName = entityMeta.getTable().name();
          if (tableName == null || tableName.length()==0){
            throw new RuntimeException("Entity " + clazz + " must be have Table Name !");
          }
          if (!entityMeta.hasId()){
            throw new RuntimeException("Entity " + clazz + " must be have Id annotation !");
          }
          local.put(clazz, entityMeta);
        }
      }
    }
    return entityMeta;
  }
}
