package org.onelab.data.meta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 实体描述对象
 *
 * @author Chunliang.Han on 15/8/10.
 * @note 1.实体属性名称不区分大小写 2.认为有Column注解的字段为表字段。
 */
public class EntityMeta extends ObjectMeta {

  /**
   * ID 描述信息
   */
  private IDMeta idMeta;

  /**
   * 对应entity的表
   */
  private Table table;

  /**
   * column列表，不包含ID列
   */
  private final List<Column> normalColumns = new ArrayList<Column>(0);

  /**
   * 字段名-熟悉对照表
   */
  private final Map<Column, Field> columnFieldMap = new HashMap<Column, Field>(0);

  public void putFieldWithColumn(Column column, Field field) {
    columnFieldMap.put(column, field);
  }

  public Field getFieldWithColumn(Column column) {
    return columnFieldMap.get(column);
  }

  public IDMeta getIdMeta() {
    return idMeta;
  }

  public void setIdMeta(IDMeta idMeta) {
    this.idMeta = idMeta;
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public List<Column> getNormalColumns() {
    return normalColumns;
  }

  public boolean hasId() {
    return idMeta != null;
  }

  public int columnLength() {
    return columnFieldMap.size();
  }
}
