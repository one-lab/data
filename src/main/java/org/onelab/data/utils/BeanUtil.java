package org.onelab.data.utils;

import org.onelab.data.DateFormat;
import org.onelab.data.Type;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * 对象处理工具类
 * @author Chunliang.Han on 15/8/17.
 */
public class BeanUtil {

  /**
   * 创建对象 需要对象存在可访问的默认构造子
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
   */
  public static void set(Field field, Object o, Object value) {

    try {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }

      if (value == null){
        field.set(o, null);
        return;
      }

      Class fieldType = field.getType();
      if (fieldType.isAssignableFrom(value.getClass())) {
        field.set(o, value);
        return;
      }

      Type type = Type.of(field.getType());

      if (type == Type.STRING){
        // Date类型 包含 Timestamp
        if (value instanceof Date ){
          String format = Type.defaultFormat;
          DateFormat dateFormat = field.getAnnotation(DateFormat.class);
          if (dateFormat != null){
            format = dateFormat.value();
          }
          field.set(o, Type.dateToString((Date) value, format));
          return;
        }
        // byte[] 类型
        if (value instanceof byte[]){
          field.set(o, Type.byteToString((byte[]) value));
          return;
        }
      }

      field.set(o, type.parse(value));

    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
