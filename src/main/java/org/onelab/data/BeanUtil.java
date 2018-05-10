package org.onelab.data;

import java.lang.reflect.Field;

/**
 * 对象处理工具类
 * @author Chunliang.Han on 15/8/17.
 */
public class BeanUtil {

  /**
   * 创建对象
   * 需要对象存在可访问的默认构造子
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T newInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 获取对象属性值
   * @param field
   * @param o
   * @return
   */
  public static Object get(Field field, Object o) {
    try {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      return field.get(o);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 为对象属性赋值
   * @param field
   * @param o
   * @param value
   */
  public static void set(Field field, Object o, Object value) {
    try {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      // TODO: 2018/5/10  先单独处理GigInteger类型
      if (field.getType() == long.class || field.getType() == Long.class){
        if (value.getClass() != long.class || field.getType() != Long.class){
          value = Long.parseLong(value==null ? "0" : value.toString());
        }
      }
      field.set(o, value);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
