package org.onelab.data;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Chunliang.Han on 2019-09-26.
 */
public enum Type {

  DATE, TIMESTAMP, STRING, BIGDECIMAL, BOOLEAN, BYTE, SHORT, INT, LONG, FLOAT,BYTES, DOUBLE, DEFAULT;

  public static final String defaultFormat = "yyyy-MM-dd HH:mm:ss";

  public static Type of(Class t){
    if (t != null){
      if (t == Boolean.class){
        return BOOLEAN;
      }
      if (t == Byte.class){
        return BYTE;
      }
      if (t == Short.class){
        return SHORT;
      }
      if (t == Integer.class){
        return INT;
      }
      if (t == Long.class){
        return LONG;
      }
      if (t == Float.class){
        return FLOAT;
      }
      if (t == Double.class){
        return DOUBLE;
      }
      if (t == BigDecimal.class){
        return BIGDECIMAL;
      }
      if (t == String.class){
        return STRING;
      }
      if (t == Date.class){
        return DATE;
      }
      if (t == Timestamp.class){
        return TIMESTAMP;
      }
      if (t == byte[].class){
        return BYTES;
      }
    }
    return DEFAULT;
  }

  public static boolean isDate(Type type){
    return type==DATE || type==TIMESTAMP;
  }

  public static String dateToString(Date o, String format) {
    if (format == null || format.length() == 0) {
      format = defaultFormat;
    }
    return new SimpleDateFormat(format).format(o);
  }

  public static String byteToString(byte[] o) {
    try {
      return new String(o, "utf-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

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
      case BYTES:
        if (o == null){
          return null;
        }
        if (o instanceof String){
          return ((String) o).getBytes();
        }
    }
    return o;
  }

  public Date parseDate(String o, String format) {
    if (format == null || format.length() == 0) {
      format = defaultFormat;
    }
    try {
      if (this == TIMESTAMP){
        return new Timestamp(new SimpleDateFormat(format).parse(o).getTime());
      }
      return new SimpleDateFormat(format).parse(o);
    } catch (ParseException e) {
      throw new RuntimeException("字符串转日期出错：format=" + format + " value=" + o, e);
    }
  }
}