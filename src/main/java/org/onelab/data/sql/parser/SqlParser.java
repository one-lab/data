package org.onelab.data.sql.parser;

import org.onelab.data.meta.EntityMeta;
import org.onelab.data.meta.store.EntityMetaStore;
import org.onelab.data.sql.Sql;
import org.onelab.data.sql.SqlType;

/**
 * @author Chunliang.Han on 15/8/10.
 */
public abstract class SqlParser {

  protected Object entity;
  protected EntityMeta entityMeta;

  public static SqlParser buildParser(SqlType sqlType, Class clazz, Object entity) {
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
      parser.entity = entity;
      parser.entityMeta = EntityMetaStore.getEntityMeta(clazz);
    }
    return parser;
  }

  /**
   * 生成SQL
   */
  abstract public Sql parseSql();
}
