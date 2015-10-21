package org.onelab.data.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Chunliang.Han on 15/10/21.
 */
public class BaseTypeProducer<T> implements ResultProducer<T> {

  public T produce(ResultSetMetaData resultSetMetaData, ResultSet resultSet, int columnCount,
                   int rowIndex) throws SQLException {
    return (T)resultSet.getObject(1);
  }
}
