package org.onelab.data.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class ArrayProducer implements ResultProducer<Object[]> {

  public Object[] produce(ResultSetMetaData resultSetMetaData, ResultSet resultSet, int columnCount,
                          int rowIndex) throws SQLException {
    Object[] result = new Object[columnCount];
    for (int i = 0; i < columnCount; i++) {
      result[i] = resultSet.getObject(i + 1);
    }
    return result;
  }
}
