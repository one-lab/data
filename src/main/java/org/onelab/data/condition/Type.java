package org.onelab.data.condition;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Chunliang.Han on 2019-09-26.
 */
public enum Type {

  DATE, STRING, BIGDECIMAL, BOOLEAN, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE, DEFAULT;

  public Object parse(Object o) {
    switch (this) {
      case DEFAULT:
        return o;
      case BOOLEAN:
        return o == null ? null : Boolean.valueOf(o.toString());
      case BYTE:
        return o == null ? null : Byte.valueOf(o.toString());
      case SHORT:
        return o == null ? null : Short.valueOf(o.toString());
      case INT:
        return o == null ? null : Integer.valueOf(o.toString());
      case LONG:
        return o == null ? null : Long.valueOf(o.toString());
      case FLOAT:
        return o == null ? null : Float.valueOf(o.toString());
      case DOUBLE:
        return o == null ? null : Double.valueOf(o.toString());
      case BIGDECIMAL:
        return o == null ? null : new BigDecimal(o.toString());
      case STRING:
        return o == null ? null : o.toString();
    }
    return o;
  }

  public Object parseDate(String o, String format) {
    if (format == null || format.length() == 0) {
      format = "yyyy-MM-dd HH:mm:ss";
    }
    try {
      return new SimpleDateFormat(format).parse(o);
    } catch (ParseException e) {
      throw new RuntimeException("字符串转日期出错：format=" + format + " value=" + o, e);
    }
  }
}