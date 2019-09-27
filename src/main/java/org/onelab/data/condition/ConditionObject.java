package org.onelab.data.condition;

import java.lang.reflect.Field;

/**
 * @author Chunliang.Han on 2019-09-26.
 */
public class ConditionObject {

  private Field field;

  private Object value;

  private Condition condition;


  public ConditionObject(Field field, Object value, Condition condition) {
    this.field = field;
    this.value = value;
    this.condition = condition;
  }

  public Field getField() {
    return field;
  }

  public String getName(){
    if (condition.name().equals("")){
      return field.getName();
    } else {
      return condition.name();
    }
  }

  public Object getValue() {
    return value;
  }

  public Condition getCondition() {
    return condition;
  }
}
