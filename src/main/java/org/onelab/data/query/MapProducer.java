package org.onelab.data.query;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 映射表处理器
 * 将查询结果ResultSet的指定行转换成相应映射表
 * @author Chunliang.Han on 15/8/15.
 */
public class MapProducer implements ResultProducer<Map<String, Object>> {

  public Map<String, Object> produce(ResultSetMetaData resultSetMetaData,
                                     ResultSet resultSet, int columnCount, int rowIndex)
      throws SQLException {
    Map<String, Object> map = new HashMap<String, Object>();
    for (int i = 0; i < columnCount; i++) {
      String label = resultSetMetaData.getColumnLabel(i + 1);
      Object value = resultSet.getObject(i + 1);
      map.put(label, value);
    }
    return map;
  }
}
