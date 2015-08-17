package org.onelab.data.sql.parser;

import org.onelab.data.meta.EntityMeta;
import org.onelab.data.meta.store.EntityMetaStore;
import org.onelab.data.sql.Sql;
import org.onelab.data.sql.SqlType;

/**
 * SQL生成器
 * 根据实体描述对象和相应数据生成指定SQL
 * @author Chunliang.Han on 15/8/10.
 */
public abstract class SqlParser {

  /**
   * 相关数据
   */
  protected Object data;
  /**
   * 实体描述对象
   */
  protected EntityMeta entityMeta;

  public static SqlParser buildParser(SqlType sqlType, Class clazz, Object data) {
    SqlParser parser = null;
    switch (sqlType) {
      case INSERT:
        parser = new InsertParser();
        break;
      case UPDATE:
        parser = new UpdateParser();
        break;
      case DELETE:
        parser = new DeleteParser();
        break;
      case FIND:
        parser = new FindParser();
        break;
      case FIND_ALL:
        parser = new FindAllParser();
    }
    if (parser != null) {
      parser.data = data;
      parser.entityMeta = EntityMetaStore.getEntityMeta(clazz);
    }
    return parser;
  }

  /**
   * 生成SQL
   */
  abstract public Sql parseSql();
}
