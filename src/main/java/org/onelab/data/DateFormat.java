package org.onelab.data;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日期转字符串格式
 *
 * @author Chunliang.Han on 2019-09-29.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateFormat {
  String value() default "yyyy-MM-dd HH:mm:ss";
}
