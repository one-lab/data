package org.onelab.data.sugar.model;

import org.onelab.data.condition.Condition;
import org.onelab.data.condition.Operation;
import org.onelab.data.condition.Type;

/**
 * 开闭区间校验
 *
 * @author Chunliang.Han on 2019-09-27.
 */
public class ConditionBetweenNumber {

  //_0_1 >= ? and _0_1 <= ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _0_1 = "[11,33]";

  //_0_2 > ? and _0_2 < ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _0_2 = "(11,33)";

  //_0_3 > ? and _0_3 <= ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _0_3 = "(11,33]";

  //_0_4 >= ? and _0_4 < ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _0_4 = "[11,33)";

  //_1_1 >= ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _1_1 = "[11,]";

  //_1_2 > ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _1_2 = "(11,)";

  //and _1_3 > ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _1_3 = "(11,]";

  //_1_4 >= ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _1_4 = "[11,)";

  //_2_1 <= ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _2_1 = "[,33]";

  //_2_2 < ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _2_2 = "(,33)";

  //_2_3 <= ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _2_3 = "(,33]";

  //and _2_4 < ?
  @Condition(opt = Operation.BETWEEN, type = Type.INT)
  private String _2_4 = "[,33)";
}
