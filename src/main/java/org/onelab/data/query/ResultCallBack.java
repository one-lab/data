package org.onelab.data.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 结果集完全交由用户处理
 * @author Chunliang.Han on 15/10/23.
 */
public interface ResultCallBack {

  /**
   * 回调的方法
   * @param resultSetMetaData
   * @param resultSet
   * @param columnCount
   * @param rowIndex
   * @throws SQLException
   */
  void call(ResultSetMetaData resultSetMetaData, ResultSet resultSet, int columnCount, int rowIndex)
      throws SQLException;
}
