package org.onelab.data.sql.parser;

import org.onelab.data.meta.EntityMetaManager;
import org.onelab.data.sql.Sql;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;

/**
 * @author Chunliang.Han on 15/8/10.
 */
public class InsertParser extends SqlParser {

  static final String INSERT_INTO = "insert into ";
  static final String VALUES = " values";

  @Override
  public Sql parseSql() {
    Sql sql = new Sql();
    sql.setSql(getSql());
    sql.setParams(getParams());
    return sql;
  }

  private String getSql() {
    StringBuilder sql = new StringBuilder(INSERT_INTO)
        .append(EntityMetaManager.getTableName(entityMeta))
        .append(fields())
        .append(VALUES)
        .append(values());
    return sql.toString();
  }

  private StringBuilder fields() {
    StringBuilder sb = new StringBuilder("(");
    List<Column> columns = EntityMetaManager.getAllColumns(entityMeta);
    for (Column column : columns) {
      sb.append(column.name()).append(",");
    }
    sb.replace(sb.length() - 1, sb.length(), ")");
    return sb;
  }

  private StringBuilder values() {
    StringBuilder sb = new StringBuilder("(");
    int len = entityMeta.columnLength();
    while (len-- > 0) {
      sb.append("?").append(",");
    }
    sb.replace(sb.length() - 1, sb.length(), ")");
    return sb;
  }

  private Object[] getParams() {
    Object[] params = new Object[entityMeta.columnLength()];
    List<Column> columns = EntityMetaManager.getAllColumns(entityMeta);
    for (int i = 0, len = columns.size(); i < len; i++) {
      params[i] = EntityMetaManager
          .getValueByColumn(entityMeta, entity, columns.get(i));
    }
    if (params[0] == null) {
      params[0] = getIdValue();
    }
    return params;
  }

  /**
   * 获取ID的值
   */
  private Object getIdValue() {
    Object idValue = EntityMetaManager.getIdValue(entityMeta, entity);
    // TODO 将来此处判断ID生成策略
    if (idValue == null) {
      idValue = UUID.randomUUID().toString();
    }
    return idValue;
  }
}
