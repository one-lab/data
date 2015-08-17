package org.onelab.data.sql.parser;

import org.onelab.data.meta.EntityMetaManager;
import org.onelab.data.sql.Sql;
import java.util.List;
import javax.persistence.Column;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class UpdateParser extends SqlParser {

  static final String UPDATE = "update ";
  static final String SET = " set ";
  static final String WHERE = " where ";

  @Override
  public Sql parseSql() {
    Sql sql = new Sql();
    sql.setSql(getSql());
    sql.setParams(getParams());
    return sql;
  }

  private String getSql() {
    String table = entityMeta.getTable().name();
    StringBuilder sql = new StringBuilder(UPDATE)
        .append(table)
        .append(SET)
        .append(values())
        .append(WHERE)
        .append(EntityMetaManager.getIdName(entityMeta)).append("=?");
    return sql.toString();
  }

  private StringBuilder values(){
    StringBuilder sb = new StringBuilder();
    List<Column> columns = entityMeta.getNormalColumns();
    for (Column column:columns){
      sb.append(column.name()).append("=?,");
    }
    sb.replace(sb.length() - 1, sb.length(), "");
    return sb;
  }

  public Object[] getParams() {
    Object[] params = new Object[entityMeta.columnLength()];
    List<Column> columns = entityMeta.getNormalColumns();
    int i=0;
    for (Column column:columns){
      params[i] = EntityMetaManager.getValueByColumn(entityMeta,entity,column);
      i++;
    }
    params[i] = EntityMetaManager.getIdValue(entityMeta,entity);
    return params;
  }
}
