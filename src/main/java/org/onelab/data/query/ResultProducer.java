package org.onelab.data.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 查询结果处理器
 * 将查询结果ResultSet的指定行转换成指定类型的对象
 * @author Chunliang.Han on 15/7/16.
 */
public interface ResultProducer<T> {

  T produce(ResultSetMetaData resultSetMetaData, ResultSet resultSet, int columnCount, int rowIndex)
      throws SQLException;
}
