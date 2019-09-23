package org.onelab.data;

import org.junit.Test;
import static org.junit.Assert.*;
import org.onelab.data.conn.ConnectionPoolTest;
import org.onelab.data.conn.Transaction;
import org.onelab.data.model.UserEntity;
import org.onelab.data.model.UserEntity1;
import org.onelab.data.model.UserModel;
import org.onelab.data.model.UserModel1;
import org.onelab.data.model.UserModel2;
import org.onelab.data.model.UserModel3;
import org.onelab.data.model.UserModel4;
import org.onelab.data.model.UserModel5;

import java.util.Arrays;
import java.util.List;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class SessionTest {

  ConnectionPoolTest connectionPoolTest = new ConnectionPoolTest();

  Session session = new Session(connectionPoolTest.getConnectionPool());

  public Session getSession(){
    return session;
  }

  public void clear(){
    String sql = "truncate table sm_user";
    getSession().executeUpdate(sql,null);
  }

  public UserEntity createUser(String name, int age){
    UserEntity user = new UserEntity();
    user.setAge(age);
    user.setName(name);
    return user;
  }

  public void createUsers(int n){
    int i = 0;
    while (i++ < n){
      getSession().insert(createUser("u-"+i, 10+i));
    }
  }

  @Test
  public void testInsert_ID自增(){
    clear();
    UserEntity user = createUser("ceshi", 12);
    user.setId(10);
    System.out.println(user);
    getSession().insert(user);
    System.out.println(user);
    UserEntity u1 = getSession().find(UserEntity.class, 1);
    assertNotEquals(user.getId(), 10);
    assertEquals(user.getId(), u1.getId());
    assertEquals(user.getAge(), u1.getAge());
    assertEquals(user.getName(), u1.getName());
  }

  @Test
  public void testInsert_ID手动(){
    clear();
    UserEntity1 user = new UserEntity1();
    user.setId(10);
    user.setAge(12);
    user.setName("ceshi1");
    System.out.println(user);
    getSession().insert(user);
    System.out.println(user);
    UserEntity1 u1 = getSession().find(UserEntity1.class, 10);
    assertEquals(user.getId(), u1.getId());
    assertEquals(user.getAge(), u1.getAge());
    assertEquals(user.getName(), u1.getName());
  }

  @Test
  public void testUpdateAndFind(){
    clear();
    UserEntity user = new UserEntity();
    user = getSession().insert(user);
    user.setAge(25);
    user.setName("name12");
    getSession().update(user);
    UserEntity user1 = getSession().find(UserEntity.class, user.getId());
    assertEquals(user.getAge(), user1.getAge());
    assertEquals(user.getName(), user1.getName());

    Exception ex = null;
    try {
      getSession().find(UserModel.class, user.getId());
    } catch (Exception e){
      ex = e;
    }
    assertNotNull(ex);
    assertEquals(ex.getMessage(),"Entity class org.onelab.data.model.UserModel must be have Table annotation !");

    try {
      getSession().find(UserModel1.class, user.getId());
    } catch (Exception e){
      ex = e;
    }
    assertNotNull(ex);
    assertEquals(ex.getMessage(),"Entity class org.onelab.data.model.UserModel1 must be have Table Name !");

    try {
      getSession().find(UserModel2.class, user.getId());
    } catch (Exception e){
      ex = e;
    }
    assertNotNull(ex);
    assertEquals(ex.getMessage(),"Entity class org.onelab.data.model.UserModel2 must be have Id annotation !");

    try {
      getSession().find(UserModel3.class, user.getId());
    } catch (Exception e){
      ex = e;
    }
    assertNotNull(ex);
    assertEquals(ex.getMessage(),"Entity class org.onelab.data.model.UserModel3 must be have Id annotation !");

    try {
      getSession().find(UserModel4.class, user.getId());
    } catch (Exception e){
      ex = e;
    }
    assertNotNull(ex);
    assertEquals(ex.getMessage(),"Entity class org.onelab.data.model.UserModel4, Field id must be have Column name !");

    UserModel5 userModel = getSession().find(UserModel5.class, user.getId());

    assertNotNull(userModel.getId());
    assertNull(userModel.getName());
    assertEquals(userModel.getAge(), 0);
  }

  @Test
  public void testFindAll(){
    clear();
    createUsers(10);
    int i=0;
    for (UserEntity user:getSession().findAll(UserEntity.class)){
      i++;
      assertEquals(user.getName(), "u-"+i);
      assertEquals(user.getAge(), 10+i);
    }
    assertEquals(10, i);
  }

  @Test
  public void testQueryOneBean(){
    clear();
    createUsers(10);
    UserModel user = getSession()
        .queryOneBean(UserModel.class, "select * from sm_user where id=?", new Object[]{4});
    assertEquals(user.getName(), "u-"+4);
    assertEquals(user.getAge(), 10+4);
    assertNull(user.getIgnore());
    assertNotNull(user.getmTiMe());
  }

  @Test
  public void testQueryListBean(){
    clear();
    createUsers(10);
    System.out.println(getSession().queryListBean(UserModel.class, "select * from sm_user", null));

  }

  @Test
  public void testQueryOneMap(){
    clear();
    createUsers(10);
    System.out.println(getSession().queryOneMap("select * from sm_user where id=?", new Object[]{4}));
  }

  @Test
  public void testQueryListMap(){
    clear();
    createUsers(10);
    System.out.println(getSession().queryListMap("select * from sm_user", null));

  }

  @Test
  public void testQueryOneArray(){
    clear();
    createUsers(10);
    List list = Arrays.asList(
        getSession().queryOneArray("select * from sm_user where id=?", new Object[]{4}));
    System.out.println(list);
  }

  @Test
  public void testQueryListArray(){
    clear();
    createUsers(10);
    List<Object[]> list = getSession().queryListArray("select * from sm_user", null);
    for (Object[] o:list){
      System.out.println(Arrays.asList(o));
    }
  }

  @Test
  public void testQuerySingleValue(){
    clear();
    UserEntity1 user = new UserEntity1();
    user.setId(123454321);
    user.setAge(12);
    user.setName("ceshi");
    getSession().insert(user);
    System.out.println(user.getId());
    Integer age = getSession().queryObject("select age from sm_user where id=?",
                                           new Object[]{123454321});
    System.out.println(age);

    age = getSession().queryObject("select age from sm_user where id=?",
                                   new Object[]{123454322});
    System.out.println(age);

    Long count = getSession().queryObject("select count(1) from sm_user", null);

    System.out.println(count);

  }

  @Test
  public void testTtansaction_1(){

    clear();
    createUsers(10);
    for (UserEntity user:getSession().findAll(UserEntity.class)){
      getSession().delete(UserEntity.class, user.getId());
    }
    Transaction transaction = getSession().getTransaction();
    transaction.begin();
    UserEntity1 user=new UserEntity1();
    try {
      user.setId(1231234);
      getSession().insert(user);
      user = getSession().find(UserEntity1.class, "1231234");
      System.out.println("1:" + user);
      user.setName("123");
      getSession().update(user);
    } catch (Exception e){
      e.printStackTrace();
    }finally {
      transaction.end();
    }
    user = getSession().find(UserEntity1.class, "1231234");
    System.out.println("2" + user);
    System.out.println("==========");
    transaction = getSession().getTransaction();
    transaction.begin();
    user=new UserEntity1();
    try {
      user.setId(1231234567);
      getSession().insert(user);
      user = getSession().find(UserEntity1.class, "1231234567");
      System.out.println("1:" + user);
      user.setName("123");
      getSession().update(user);
      transaction.submit();
    } finally {
      transaction.end();
    }
    user = getSession().find(UserEntity1.class, "1231234567");
    System.out.println("2" + user);
    System.out.println("==========");
    transaction = getSession().getTransaction();
    transaction.begin();
    user=new UserEntity1();
    try {
      user.setId(12312345);
      getSession().insert(user);
      user = getSession().find(UserEntity1.class, "12312345");
      System.out.println("1:" + user);
      user.setName("123");
      getSession().update(user);
      transaction.submit();
    } finally {
      transaction.end();
    }
    user = getSession().find(UserEntity1.class, "12312345");
    System.out.println("2" + user);
  }
}
