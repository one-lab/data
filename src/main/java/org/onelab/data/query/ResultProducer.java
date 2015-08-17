package org.onelab.data.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author Chunliang.Han on 15/7/16.
 */
public interface ResultProducer<T> {
  T produce(ResultSetMetaData resultSetMetaData,ResultSet resultSet,int columnCount,int rowIndex)
      throws SQLException;
}
