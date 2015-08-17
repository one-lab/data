package org.onelab.data;

import java.lang.reflect.Field;

/**
 * @author Chunliang.Han on 15/8/17.
 */
public class BeanUtil {

  public static <T> T newInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

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

  public static void set(Field field, Object o, Object value) {
    try {
      if (!field.isAccessible()) {
        field.setAccessible(true);
      }
      field.set(o, value);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}
