package org.onelab.data.condition;

import org.onelab.data.Statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Chunliang.Han on 2019-09-27.
 */
public class ConditionStatementBuilder {

  private final ConditionObject co;

  private final Statement statement = Statement.init();

  public static final Pattern BETWEEN_PATTERN = Pattern.compile("([\\[\\(])(.*),(.*)([])])");

  public static final String AND = " and ";

  private ConditionStatementBuilder(ConditionObject co) {
    this.co = co;
  }

  public static Statement build(ConditionObject co) {
    return new ConditionStatementBuilder(co).build();
  }

  public Statement build() {

    Object value = co.getValue();

    //简单考虑，目前认为值为空则条件不存在
    if (value == null) {
      return statement;
    }

    String name = co.getName();

    switch (co.getCondition().opt()) {
      case EQUAL:
        statement
            .appendContent(name).appendContent(" = ?").appendParams(sqlValue(value));
        break;
      case LIKE:
        statement
            .appendContent(name).appendContent(" like ?").appendParams(sqlValue(value));
        break;
      case BETWEEN:
        if (!(value instanceof String)) {
          throw new RuntimeException("语法错误：操作符 BETWEEN 的值类型必须是字符串");
        }
        String[] bt = getBetweenDesc((String) value);
        if (bt == null) {
          throw new RuntimeException("语法错误：操作符 BETWEEN 的值写法有误 : " + value);
        }
        statement.append(getStatement(bt, (String) value, name));
    }

    return statement;
  }

  private Statement getStatement(String[] bt, String value, String name) {

    Statement statement = Statement.init();

    if (bt[1].length() > 0) {
      statement.appendContent(name).appendContent(" >");
      if (bt[0].equals("[")) {
        statement.appendContent("=");
      }
      statement.appendContent(" ?");
      statement.appendParams(sqlValue(bt[1]));
    }

    if (bt[2].length() > 0) {
      if (!statement.isEmpty()) {
        statement.appendContent(AND);
      }
      statement.appendContent(name).appendContent(" <");
      if (bt[3].equals("]")) {
        statement.appendContent("=");
      }
      statement.appendContent(" ?");
      statement.appendParams(sqlValue(bt[2]));
    }

    return statement;
  }

  private Object sqlValue(Object value) {
    Condition c = co.getCondition();
    if (c.type() == Type.DATE && value instanceof String) {
      return c.type().parseDate(value.toString(), c.dateFormat());
    }
    return c.type().parse(value);
  }

  private static String[] getBetweenDesc(String between) {
    if (between != null) {
      Matcher matcher = BETWEEN_PATTERN.matcher(between);
      if (matcher.find()) {
        String[] desc = new String[4];
        desc[0] = matcher.group(1).trim();
        desc[1] = matcher.group(2).trim();
        desc[2] = matcher.group(3).trim();
        desc[3] = matcher.group(4).trim();
        return desc;
      }
    }
    return null;
  }
}
