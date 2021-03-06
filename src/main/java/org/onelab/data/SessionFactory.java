package org.onelab.data;

import org.onelab.data.conn.Config;
import org.onelab.data.conn.ConnectionPool;

import java.util.HashMap;
import java.util.Map;

/**
 * 会话工厂
 * 相同配置ID的会话只创建一个
 * @author Chunliang.Han on 15/7/16.
 */
public class SessionFactory {

  private static final Object sessionLock = new Object();

  private static Map<String, Session> sessionMap = new HashMap<String, Session>(4);

  private static ConnectionPool getConnectionPool(Config config) {
    ConnectionPool connectionPool = new ConnectionPool(config);
    try {
      connectionPool.getConnection();
    } catch (Throwable t) {
      throw new RuntimeException("数据库连接获取失败", t);
    } finally {
      connectionPool.close();
    }
    return connectionPool;
  }

  private static Session createSession(Config config) {
    ConnectionPool connectionPool = getConnectionPool(config);
    return new Session(connectionPool);
  }

  /**
   * 获取 Session
   */
  public static Session getSession(Config config) {
    String id = config.getId();
    if (id == null) {
      throw new RuntimeException("the config id is not allowed null !");
    }
    Session session = sessionMap.get(id);
    if (session == null) {
      synchronized (sessionLock){
        if (session == null){
          session = createSession(config);
          sessionMap.put(id, session);
        }
      }
    }
    return session;
  }
}