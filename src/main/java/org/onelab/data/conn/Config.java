package org.onelab.data.conn;

/**
 * 配置
 * @author Chunliang.Han on 15/8/15.
 */
public class Config {

  /**
   * 配置ID
   */
  private String id;
  /**
   * 数据库地址
   */
  private String url;
  /**
   * 用户名
   */
  private String user;
  /**
   * 密码
   */
  private String password;
  /**
   * 连接池最大容量
   */
  private int maxPoolSize;
  /**
   * 连接池中的最小连接数
   */
  private int minPoolSize;
  /**
   * 连接失效时间
   */
  private int invalidTime;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getMaxPoolSize() {
    return maxPoolSize;
  }

  public void setMaxPoolSize(int maxPoolSize) {
    this.maxPoolSize = maxPoolSize;
  }

  public int getMinPoolSize() {
    return minPoolSize;
  }

  public void setMinPoolSize(int minPoolSize) {
    this.minPoolSize = minPoolSize;
  }

  public int getInvalidTime() {
    return invalidTime;
  }

  public void setInvalidTime(int invalidTime) {
    this.invalidTime = invalidTime;
  }
}
