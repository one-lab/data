package org.onelab.data;

import java.util.List;
import java.util.Map;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class SessionTest {
  public static void main(String[] args){
    Config config = new Config();
    config.setUrl("jdbc:mysql://127.0.0.1:3306/sm?useUnicode=true&amp;characterEncoding=UTF-8");
    config.setUser("root");
    config.setPassword("root");
    config.setMinPoolSize(5);
    config.setMaxPoolSize(15);
    config.setMaxConnectionsNum(20);
    Session session = new Session(new ConnectionPool(config));
//    User user = new User();
//    user.setId("5");
//    session.insert(user);
//    System.out.println(user);
//    System.out.println("===");
//    user = session.find(User.class,"5");
//    System.out.println(user);
//    System.out.println("===");
//    user.setAge(12);
//    session.update(user);
//    user = session.find(User.class,"5");
//    System.out.println(user);
//    System.out.println("===");
//    session.insert(user);
    List<User> users = session.findAll(User.class);
    for (User u:users){
      System.out.println(u);
    }
    System.out.println("===");
    List<Map<String,Object>> userMapList = session.queryAsMap("select * from sm_user",null);
    for (Map u:userMapList){
      System.out.println(u);
    }
    System.out.println("===");
  }
}
