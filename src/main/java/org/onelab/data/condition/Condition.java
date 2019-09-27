package org.onelab.data.condition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Chunliang.Han on 2019-09-26.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Condition {

  /**
   * 查询字段的名字
   */
  String name() default "";

  /**
   * 查询结果的类型
   */
  Type type() default Type.DEFAULT;

  /**
   * 筛选的操作类型
   */
  Operation opt() default Operation.EQUAL;

  /**
   * 用于描述时间类型字符串的SimpleDateFormat格式
   */
  String dateFormat() default "yyyy-MM-dd HH:mm:ss";
}
