package org.onelab.data.sugar.model;

import org.onelab.data.condition.Condition;
import org.onelab.data.condition.Operation;
import org.onelab.data.Type;

/**
 * 空值校验
 *
 * @author Chunliang.Han on 2019-09-27.
 */
public class ConditionNull {

  /**
   * 不生成条件 时间-日期 无区间值
   */
  @Condition(opt = Operation.BETWEEN, type = Type.DATE)
  private String _1_1 = "[,]";

  /**
   * 不生成条件 字符串-日期 无区间值
   */
  @Condition(opt = Operation.BETWEEN, type = Type.STRING)
  private String _1_2 = "[,]";

  /**
   * 不生成条件 数字-日期 无区间值
   */
  @Condition(opt = Operation.BETWEEN, type = Type.LONG)
  private String _1_3 = "[,]";

}
