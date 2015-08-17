package org.onelab.data.sql.parser;

import org.onelab.data.meta.EntityMetaManager;
import org.onelab.data.sql.Sql;

/**
 * 删除语句生成器
 * @author Chunliang.Han on 15/8/15.
 */
public class DeleteParser extends SqlParser {

  public static final String DELETE = "delete from ";
  public static final String WHERE = " where ";

  @Override
  public Sql parseSql() {
    StringBuilder sb = new StringBuilder(DELETE);
    sb.append(EntityMetaManager.getTableName(entityMeta))
        .append(WHERE)
        .append(EntityMetaManager.getTableName(entityMeta))
        .append("=?");
    Sql sql = new Sql();
    sql.setSql(sb.toString());
    sql.setParams(new Object[]{data});
    return sql;
  }
}
