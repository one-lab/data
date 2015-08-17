package org.onelab.data;

/**
 * @author Chunliang.Han on 15/8/15.
 */
public class Config {

  private String id;
  private String url;
  private String user;
  private String password;
  private int maxPoolSize;
  private int minPoolSize;
  private int maxConnectionsNum;

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

  public int getMaxConnectionsNum() {
    return maxConnectionsNum;
  }

  public void setMaxConnectionsNum(int maxConnectionsNum) {
    this.maxConnectionsNum = maxConnectionsNum;
  }
}
