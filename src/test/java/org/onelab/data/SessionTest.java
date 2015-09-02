package org.onelab.data;

import java.util.Arrays;
import java.util.List;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class SessionTest {
  static Session session;
  public static void main(String[] args) {
    testInsert();
    testUpdateAndFind();
    testFindAll();
    testQueryOneBean();
    testQueryListBean();
    testQueryOneMap();
    testQueryListMap();
    testQueryOneArray();
    testQueryListArray();
    testQuerySingleValue();
  }
  public static Session getSession(){
    if (session==null){
      Config config = new Config();
      config.setUrl("jdbc:mysql://127.0.0.1:3306/sm?useUnicode=true&amp;characterEncoding=UTF-8");
      config.setUser("root");
      config.setPassword("root");
      config.setMinPoolSize(5);
      config.setMaxPoolSize(15);
      session = new Session(new ConnectionPool(config));
    }
    return session;
  }
  public static void testInsert(){
    User user = new User();
    getSession().insert(user);
    System.out.println(user.getId());
  }
  public static void testUpdateAndFind(){
    User user = new User();
    user = getSession().insert(user);
    user.setName("name12");
    getSession().update(user);
    User user1 = getSession().find(User.class,user.getId());
    System.out.println(user1.getId().equals(user.getId()));
    System.out.println(user1.getName().equals(user.getName()));
    System.out.println(user1.getAge() == user.getAge());
    System.out.println(user.getId());
  }
  public static void testFindAll(){
    for (User user:getSession().findAll(User.class)){
      System.out.println(user);
    }
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
    Integer age = getSession().querySingleValue("select age from sm_user where id=?",new Object[]{"100"});
    System.out.println(age);
  }
}
