package org.onelab.data.sql.parser;

import org.onelab.data.meta.EntityMetaManager;
import org.onelab.data.sql.Sql;

import java.util.List;

import javax.persistence.Column;

/**
 * 查询全部语句生成器
 * @author Chunliang.Han on 15/8/15.
 */
public class FindAllParser extends SqlParser {

  public static final String SELECT = "select ";
  public static final String FROM = " from ";

  @Override
  public Sql parseSql() {
    StringBuilder sb = new StringBuilder(SELECT);
    List<Column> columns = EntityMetaManager.getAllColumns(entityMeta);
    for (int i = 0, len = columns.size(); i < len; i++) {
      sb.append(columns.get(i).name()).append(",");
    }
    sb.replace(sb.length() - 1, sb.length(), "");
    sb.append(FROM).append(EntityMetaManager.getTableName(entityMeta));
    Sql sql = new Sql();
    sql.setSql(sb.toString());
    return sql;
  }
}
