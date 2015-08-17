package org.onelab.data.sql;

import org.onelab.data.sql.parser.SqlParser;

/**
 * SQL生成器
 *
 * @author Chunliang.Han on 15/8/10.
 */
public class SqlRander {

  /**
   * 根据渲染类型生成特定SQL
   *
   * @param sqlType sql类型
   * @param clazz   实体类型
   * @param entity  参数对象
   */
  public static Sql rander(SqlType sqlType, Class clazz, Object entity) {
    return SqlParser.buildParser(sqlType, clazz, entity).parseSql();
  }
}
