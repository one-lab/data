package org.onelab.data.sugar;

import org.junit.Test;
import org.onelab.data.ConditionHelper;
import org.onelab.data.Statement;
import org.onelab.data.sugar.model.ConditionBetween;
import org.onelab.data.sugar.model.ConditionBetweenNumber;
import org.onelab.data.sugar.model.ConditionModel;
import org.onelab.data.sugar.model.ConditionNull;

/**
 * @author Chunliang.Han on 2019-09-27.
 */
public class ConditionHelperTest {

  @Test
  public void conditionNullTest(){

    System.out.println("为空测试");

    ConditionNull model = new ConditionNull();
    printStatement(ConditionHelper.build(model).toWhereStatement());
  }

  @Test
  public void conditionBetweenTest_时间(){

    System.out.println("区间测试-时间");

    ConditionBetween model = new ConditionBetween();
    printStatement(ConditionHelper.build(model).toWhereStatement());
  }

  @Test
  public void conditionBetweenTest_数字(){

    System.out.println("区间测试-数字");

    ConditionBetweenNumber model = new ConditionBetweenNumber();
    printStatement(ConditionHelper.build(model).toWhereStatement());
  }

  @Test
  public void conditionModelTest_复合(){

    System.out.println("复合测试");

    ConditionModel model = new ConditionModel();
    printStatement(ConditionHelper.build(model).toWhereStatement());
  }

  private void printStatement(Statement statement){

    System.out.println(statement.content());

    int i=0;

    for (Object param : statement.params()){
      System.out.println("参数" + (++i) + " 类型:" + param.getClass() + ", 值:"+param);
    }
  }
}
