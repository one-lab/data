package org.onelab.data.meta;

import java.lang.reflect.Field;

import javax.persistence.Column;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class IDMeta {

  /**
   * entity的ID
   */
  private Field idField;

  /**
   * ID对应的类
   */
  private Column idColumn;

  public Field getIdField() {
    return idField;
  }

  public void setIdField(Field idField) {
    this.idField = idField;
  }

  public Column getIdColumn() {
    return idColumn;
  }

  public void setIdColumn(Column idColumn) {
    this.idColumn = idColumn;
  }
}
