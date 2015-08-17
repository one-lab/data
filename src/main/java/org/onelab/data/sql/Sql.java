package org.onelab.data.sql;

/**
 * @author Chunliang.Han on 15/8/10.
 */
public class Sql {

  private String sql;
  private Object[] params;

  public Sql() {
  }

  public Sql(String sql, Object[] params) {
    this.sql = sql;
    this.params = params;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public Object[] getParams() {
    return params;
  }

  public void setParams(Object[] params) {
    this.params = params;
  }
}
