package org.onelab.data.meta.store;

import org.onelab.data.meta.EntityMeta;
import org.onelab.data.meta.IDMeta;
import org.onelab.data.meta.ObjectMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 类描述对象生成存储器
 *
 * @author Chunliang.Han on 15/8/10.
 */
public class EntityMetaStore {

  public static final Map<Class, EntityMeta> local = new HashMap<Class, EntityMeta>(0);

  private static EntityMeta createEntityMeta(Class clazz) {
    EntityMeta entityMeta = new EntityMeta();
    entityMeta.setEntityClass(clazz);
    entityMeta.setTable((Table) clazz.getAnnotation(Table.class));
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      entityMeta.putFieldWithName(field.getName(), field);
      Column column = field.getAnnotation(Column.class);
      if (column != null) {
        entityMeta.putFieldWithColumn(column, field);
        Id id = field.getAnnotation(Id.class);
        if (id != null) {
          IDMeta idMeta = new IDMeta();
          idMeta.setIdColumn(column);
          idMeta.setIdField(field);
          entityMeta.setIdMeta(idMeta);
        } else {
          entityMeta.getNormalColumns().add(column);
        }
      }
    }
    return entityMeta;
  }

  public static EntityMeta getEntityMeta(Class clazz) {
    EntityMeta entityMeta = local.get(clazz);
    if (entityMeta == null) {
      entityMeta = createEntityMeta(clazz);
      local.put(clazz, entityMeta);
    }
    if (entityMeta.getTable() == null) {
      throw new RuntimeException("entity " + clazz + " must has Column annotation !");
    }
    return entityMeta;
  }
}
