package org.onelab.data.sql.parser;

import org.onelab.data.meta.EntityMetaManager;
import org.onelab.data.sql.Sql;

import java.util.List;

import javax.persistence.Column;

/**
 * 插入语句生成器
 * @author Chunliang.Han on 15/8/10.
 */
public class InsertParser extends SqlParser {

  static final String INSERT_INTO = "insert into ";
  static final String VALUES = " values";

  @Override
  public Sql parseSql() {
    Sql sql = new Sql();
    Object[] params = getParams();
    sql.setParams(params);
    sql.setSql(getSql(params.length));
    return sql;
  }

  private String getSql(int paramsLen) {
    StringBuilder sql = new StringBuilder(INSERT_INTO)
        .append(EntityMetaManager.getTableName(entityMeta))
        .append(fields(paramsLen))
        .append(VALUES)
        .append(values(paramsLen));
    return sql.toString();
  }

  private StringBuilder fields(int paramsLen) {
    StringBuilder sb = new StringBuilder("(");
    List<Column> columns = EntityMetaManager.getAllColumns(entityMeta);
    if (paramsLen == columns.size()-1){
      columns = entityMeta.getNormalColumns();
    }
    for (Column column : columns) {
      sb.append(column.name()).append(",");
    }
    sb.replace(sb.length() - 1, sb.length(), ")");
    return sb;
  }

  private StringBuilder values(int paramsLen) {
    StringBuilder sb = new StringBuilder("(");
    while (paramsLen-- > 0) {
      sb.append("?").append(",");
    }
    sb.replace(sb.length() - 1, sb.length(), ")");
    return sb;
  }

  //TODO 判断ID是否存在的方法需要调整（ID生成策略）
  private Object[] getParams() {
    Object[] params = new Object[entityMeta.columnLength()];
    List<Column> columns = EntityMetaManager.getAllColumns(entityMeta);
    for (int i = 0, len = columns.size(); i < len; i++) {
      params[i] = EntityMetaManager
          .getValueByColumn(entityMeta, data, columns.get(i));
    }
    if (params[0] == null || params[0].toString().equals("0")) {
      Object[] res = new Object[params.length-1];
      System.arraycopy(params, 1, res,0,res.length);
      return res;
    }
    return params;
  }
}
