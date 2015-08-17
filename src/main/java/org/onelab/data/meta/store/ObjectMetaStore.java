package org.onelab.data.meta.store;

import org.onelab.data.meta.ObjectMeta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Chunliang.Han on 15/8/17.
 */
public class ObjectMetaStore {

  public static final Map<Class, ObjectMeta> local = new HashMap<Class, ObjectMeta>(0);

  private static ObjectMeta createObjectMeta(Class clazz) {
    ObjectMeta entityMeta = new ObjectMeta();
    entityMeta.setEntityClass(clazz);
    Field[] fields = clazz.getDeclaredFields();
    for (Field field : fields) {
      entityMeta.putFieldWithName(field.getName(), field);
    }
    return entityMeta;
  }

  public static ObjectMeta getClassMeta(Class clazz) {
    ObjectMeta objectMeta = local.get(clazz);
    if (objectMeta == null) {
      objectMeta = createObjectMeta(clazz);
      local.put(clazz, objectMeta);
    }
    return objectMeta;
  }
}
