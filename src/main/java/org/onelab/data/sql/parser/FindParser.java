package org.onelab.data.sql.parser;

import org.onelab.data.meta.EntityMetaManager;
import org.onelab.data.sql.Sql;

import java.util.List;

import javax.persistence.Column;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class FindParser extends SqlParser {

  public static final String SELECT = "select ";
  public static final String FROM = " from ";
  public static final String WHERE = " where ";

  @Override
  public Sql parseSql() {
    StringBuilder sb = new StringBuilder(SELECT);
    List<Column> columns = EntityMetaManager.getAllColumns(entityMeta);
    for (int i=0,len=columns.size();i<len;i++){
      sb.append(columns.get(i).name()).append(",");
    }
    sb.replace(sb.length() - 1, sb.length(), "");
    sb.append(FROM)
        .append(EntityMetaManager.getTableName(entityMeta))
        .append(WHERE)
        .append(EntityMetaManager.getIdName(entityMeta))
        .append("=?");
    Sql sql = new Sql();
    sql.setSql(sb.toString());
    sql.setParams(new Object[]{entity});
    return sql;
  }
}
