package org.onelab.data;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class SessionTest {
  public static void main(String[] args) {
    Config config = new Config();
    config.setUrl("jdbc:mysql://127.0.0.1:3306/sm?useUnicode=true&amp;characterEncoding=UTF-8");
    config.setUser("root");
    config.setPassword("root");
    config.setMinPoolSize(5);
    config.setMaxPoolSize(15);
    config.setMaxConnectionsNum(20);
    Session session = new Session(new ConnectionPool(config));
  }
}
