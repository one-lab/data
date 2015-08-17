package org.onelab.data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Chunliang.Han on 15/8/15.
 */
@Table(name = "sm_user")
public class User {
  @Id
  @Column(name = "id")
  private String id;
  @Column(name = "name")
  private String name;
  @Column(name = "age")
  private int age;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return new StringBuilder("id=").append(id).append(",name=").append(name).append(",age=").append(age).toString();
  }
}
