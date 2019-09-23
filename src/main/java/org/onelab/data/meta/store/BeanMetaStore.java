package org.onelab.data.meta.store;

import org.onelab.data.meta.BeanMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述对象生成存储器
 * @author Chunliang.Han on 15/8/17.
 */
public class BeanMetaStore {

  private static final Object localLock = new Object();

  /**
   * 类描述对象本地缓存
   */
  public static final Map<Class, BeanMeta> local = new HashMap<Class, BeanMeta>();

  /**
   * 生成指定类的描述对象
   * @param clazz
   * @return
   */
  private static BeanMeta createBeanMeta(Class clazz) {
    BeanMeta beanMeta = new BeanMeta();
    beanMeta.setBeanClass(clazz);
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      beanMeta.putFieldWithName(field.getName(), field);
    }
    return beanMeta;
  }

  /**
   * 获取指定类的描述对象
   * 如果本地缓存中没有则创建并装入缓存
   * @param clazz
   * @return
   */
  public static BeanMeta getBeanMeta(Class clazz) {
    BeanMeta objectMeta = local.get(clazz);
    if (objectMeta == null) {
      synchronized (localLock){
        if (objectMeta == null){
          objectMeta = createBeanMeta(clazz);
          local.put(clazz, objectMeta);
        }
      }
    }
    return objectMeta;
  }
}
