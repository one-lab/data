package org.onelab.data.meta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chunliang.Han on 15/8/17.
 */
public class ObjectMeta {

  /**
   * 对应 entity 的 class
   */
  private Class entityClass;
  /**
   * 属性名称(大小写不敏感)-属性对照表
   */
  private final Map<String, Field> nameFieldMap = new HashMap<String, Field>(0);

  public void putFieldWithName(String fieldName, Field field) {
    nameFieldMap.put(fieldName.toLowerCase(), field);
  }

  public Field getFieldWithName(String fieldName) {
    return nameFieldMap.get(fieldName.toLowerCase());
  }

  public Class getEntityClass() {
    return entityClass;
  }

  public void setEntityClass(Class entityClass) {
    this.entityClass = entityClass;
  }

  public Map<String, Field> getNameFieldMap() {
    return nameFieldMap;
  }
}
