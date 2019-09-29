package org.onelab.data.sugar.model;

import org.onelab.data.condition.Condition;
import org.onelab.data.condition.Operation;
import org.onelab.data.Type;

/**
 * 开闭区间校验
 *
 * @author Chunliang.Han on 2019-09-27.
 */
public class ConditionBetween {

  //_0_1 >= ? and _0_1 <= ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _0_1 = "[2018-08-08,2019-09-09]";

  //_0_2 > ? and _0_2 < ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _0_2 = "(2018-08-08,2019-09-09)";

  //_0_3 > ? and _0_3 <= ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _0_3 = "(2018-08-08,2019-09-09]";

  //_0_4 >= ? and _0_4 < ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _0_4 = "[2018-08-08,2019-09-09)";

  //_1_1 >= ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _1_1 = "[2018-08-08,]";

  //_1_2 > ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _1_2 = "(2018-08-08,)";

  //and _1_3 > ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _1_3 = "(2018-08-08,]";

  //_1_4 >= ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _1_4 = "[2018-08-08,)";

  //_2_1 <= ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _2_1 = "[,2019-09-09]";

  //_2_2 < ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _2_2 = "(,2019-09-09)";

  //_2_3 <= ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _2_3 = "(,2019-09-09]";

  //and _2_4 < ?
  @Condition(opt = Operation.BETWEEN, type = Type.DATE, dateFormat = "yyyy-MM-dd")
  private String _2_4 = "[,2019-09-09)";
}
