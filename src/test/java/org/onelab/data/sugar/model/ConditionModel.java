package org.onelab.data.sugar.model;

import org.onelab.data.condition.Condition;
import org.onelab.data.condition.Operation;
import org.onelab.data.condition.Type;

/**
 * @author Chunliang.Han on 2019-09-27.
 */
public class ConditionModel {

  @Condition(type = Type.LONG)
  private int _0_LONG = 1333;

  @Condition(type = Type.STRING)
  private String _1_STRING = "_1_Str";

  @Condition
  private String _2_String = "_2_Str";

  @Condition(opt = Operation.LIKE)
  private String _3_LIKE = "con3value";

  @Condition(opt = Operation.BETWEEN, type = Type.STRING)
  private String _4_BETWEEN= "[2017-01-02 00:00:00,2018-01-02 00:00:00]";
}
