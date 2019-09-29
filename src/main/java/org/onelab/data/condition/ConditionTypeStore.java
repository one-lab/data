package org.onelab.data.condition;

import org.onelab.data.utils.BeanUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * ConditionTypeStore 缓存
 *
 * @author Chunliang.Han on 2019-09-26.
 */
public class ConditionTypeStore {

  public static final Map<Class, List<Field>> store = new HashMap<Class, List<Field>>();

  public static List<ConditionObject> getConditionObjects(Object object) {

    if (object == null){
      return Collections.emptyList();
    }

    Class conditionType = object.getClass();

    List<Field> fieldList = store.get(conditionType);

    if (fieldList == null) {
      synchronized (store) {
        fieldList = store.get(conditionType);
        if (fieldList == null) {
          fieldList = getConditionList(conditionType);
          store.put(conditionType, fieldList);
        }
      }
    }

    return getConditionObjects(object, fieldList);
  }

  private static List<ConditionObject> getConditionObjects(Object object, List<Field> fieldList) {

    if (fieldList.isEmpty()){
      return Collections.emptyList();
    }

    List<ConditionObject> conditionObjectList = new ArrayList<ConditionObject>(fieldList.size());

    for (Field field : fieldList){
      ConditionObject conditionObject = new ConditionObject(field,
                                                            BeanUtil.get(field, object),
                                                            field.getAnnotation(Condition.class));
      conditionObjectList.add(conditionObject);
    }

    return conditionObjectList;
  }

  private static List<Field> getConditionList(Class conditionType) {

    List<Field> list = Collections.emptyList();

    Field[] fields = conditionType.getDeclaredFields();

    if (fields != null && fields.length > 0) {
      list = new ArrayList<Field>();
      for (Field field : fields) {
        Condition condition = field.getAnnotation(Condition.class);
        if (condition != null) {
          list.add(field);
        }
      }
    }

    return list;
  }
}
