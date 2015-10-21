package org.onelab.data;

import java.util.Arrays;
import java.util.List;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class SessionTest {
  static Session session;
  public static void main(String[] args) {
//    clear();
//    testInsert();
//    testUpdateAndFind();
//    testFindAll();
//    testDelete();
//    testFindAll();
//    testQueryOneBean();
//    testQueryListBean();
//    testQueryOneMap();
//    testQueryListMap();
//    testQueryOneArray();
//    testQueryListArray();
    testQuerySingleValue();
//    testTtansaction_1();

  }
  public static void clear(){
    String sql = "delete from sm_user";
    getSession().executeUpdate(sql,null);
  }
  public static Session getSession(){
    if (session==null){
      Config config = new Config();
      config.setUrl("jdbc:mysql://127.0.0.1:3306/sm?useUnicode=true&amp;characterEncoding=UTF-8");
      config.setUser("root");
      config.setPassword("root");
      config.setMinPoolSize(1);
      config.setMaxPoolSize(1);
      session = new Session(new ConnectionPool(config));
    }
    return session;
  }
  public static void testInsert(){
    User user = new User();
    user.setId(123454321);
    user.setAge(12);
    user.setName("ceshi");
    getSession().insert(user);
    System.out.println(user.getId());
  }
  public static void testUpdateAndFind(){
    User user = new User();
    user = getSession().insert(user);
    user.setName("name12");
    getSession().update(user);
    User user1 = getSession().find(User.class,user.getId());
    System.out.println(user1.getId()==user.getId());
    System.out.println(user1.getName().equals(user.getName()));
    System.out.println(user1.getAge() == user.getAge());
    System.out.println(user.getId());
  }
  public static void testFindAll(){
    int i=0;
    for (User user:getSession().findAll(User.class)){
      i++;
      System.out.println(user);
    }
    System.out.println("total:"+i);
  }
  public static void testQueryOneBean(){
    System.out.println(getSession().queryOneBean(User.class, "select * from sm_user where id=?",
                                                 new Object[]{4}));
  }
  public static void testQueryListBean(){
    System.out.println(getSession().queryListBean(User.class, "select * from sm_user", null));

  }
  public static void testQueryOneMap(){
    System.out.println(getSession().queryOneMap("select * from sm_user where id=?", new Object[]{4}));
  }
  public static void testQueryListMap(){
    System.out.println(getSession().queryListMap("select * from sm_user", null));

  }
  public static void testQueryOneArray(){
    List list = Arrays.asList(
        getSession().queryOneArray("select * from sm_user where id=?", new Object[]{4}));
    System.out.println(list);
  }
  public static void testQueryListArray(){
    List<Object[]> list = getSession().queryListArray("select * from sm_user", null);
    for (Object[] o:list){
      System.out.println(Arrays.asList(o));
    }
  }
  public static void testQuerySingleValue(){
    clear();
    User user = new User();
    user.setId(123454321);
    user.setAge(12);
    user.setName("ceshi");
    getSession().insert(user);
    System.out.println(user.getId());
    Integer age = getSession().queryObject("select age from sm_user where id=?",
                                           new Object[]{123454321});
    System.out.println(age);
  }
  public static void testDelete(){
    getSession().delete(User.class, "30d2b00b-ff31-4d31-b1c3-9a923153e3a3");
  }
  public static void testTtansaction_1(){
    for (User user:getSession().findAll(User.class)){
      getSession().delete(User.class,user.getId());
    }
    Transaction transaction = getSession().getTransaction();
    transaction.begin();
    User user=new User();
    try {
      user.setId(1231234);
      getSession().insert(user);
      user = getSession().find(User.class,"1231234");
      System.out.println("1:" + user);
      user.setName("123");
      getSession().update(user);
    } catch (Exception e){
      e.printStackTrace();
    }finally {
      transaction.end();
    }
    user = getSession().find(User.class,"1231234");
    System.out.println("2" + user);
    System.out.println("==========");
    transaction = getSession().getTransaction();
    transaction.begin();
    user=new User();
    try {
      user.setId(1231234567);
      getSession().insert(user);
      user = getSession().find(User.class,"1231234567");
      System.out.println("1:" + user);
      user.setName("123");
      getSession().update(user);
      transaction.submit();
    } finally {
      transaction.end();
    }
    user = getSession().find(User.class,"1231234567");
    System.out.println("2" + user);
    System.out.println("==========");
    transaction = getSession().getTransaction();
    transaction.begin();
    user=new User();
    try {
      user.setId(12312345);
      getSession().insert(user);
      user = getSession().find(User.class,"12312345");
      System.out.println("1:" + user);
      user.setName("123");
      getSession().update(user);
      transaction.submit();
    } finally {
      transaction.end();
    }
    user = getSession().find(User.class,"12312345678");
    System.out.println("2" + user);
  }
}
