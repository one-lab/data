package org.onelab.data.meta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述对象
 * 存储属性名和属性的对应关系
 * @author Chunliang.Han on 15/8/17.
 */
public class BeanMeta {

  /**
   * 对应 data 的 class
   */
  private Class beanClass;
  /**
   * 属性名称(大小写不敏感)-属性对照表
   */
  private final Map<String, Field> nameFieldMap = new HashMap<String, Field>(0);

  public void putFieldWithName(String fieldName, Field field) {
    if (fieldName.contains("_")){
      fieldName = fieldName.replaceAll("_","");
    }
    nameFieldMap.put(fieldName.toLowerCase(), field);
  }

  public Field getFieldWithName(String fieldName) {
    if (fieldName.contains("_")){
      fieldName = fieldName.replaceAll("_","");
    }
    return nameFieldMap.get(fieldName.toLowerCase());
  }

  public Class getBeanClass() {
    return beanClass;
  }

  public void setBeanClass(Class beanClass) {
    this.beanClass = beanClass;
  }

  public Map<String, Field> getNameFieldMap() {
    return nameFieldMap;
  }
}
