package org.onelab.data;

import org.onelab.data.condition.ConditionObject;
import org.onelab.data.condition.ConditionStatementBuilder;
import org.onelab.data.condition.ConditionTypeStore;

import java.util.List;

/**
 * 条件处理器
 *
 * @author Chunliang.Han on 2019-09-26.
 */
public class ConditionHelper {

  public static final String AND = " and ";

  public static final String WHERE = "where ";

  /**
   * 条件对象
   */
  private Object condition;

  private ConditionHelper() {
  }

  public static ConditionHelper build(Object condition) {
    ConditionHelper helper = new ConditionHelper();
    helper.condition = condition;
    return helper;
  }

  public Statement toWhereStatement() {
    Statement statement = getSqlStatement();
    if (!statement.isEmpty()) {
      return Statement.init(WHERE).append(statement);
    }
    return statement;
  }

  /**
   * 生成条件字符串
   */
  public Statement getSqlStatement() {

    Statement s = Statement.init();

    List<ConditionObject> conditionObjects = ConditionTypeStore.getConditionObjects(condition);

    if (conditionObjects != null) {
      for (ConditionObject conditionObject : conditionObjects) {
        if (s.isEmpty()) {
          s = ConditionStatementBuilder.build(conditionObject);
        } else {
          Statement statement = ConditionStatementBuilder.build(conditionObject);
          if (!statement.isEmpty()) {
            s.appendContent(AND).append(statement);
          }
        }
      }
    }

    return s;
  }
}
