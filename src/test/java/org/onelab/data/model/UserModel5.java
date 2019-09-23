package org.onelab.data.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Chunliang.Han on 15/8/15.
 */
@Table(name = "sm_user")
public class UserModel5 {

  @Id
  @Column(name = "iD")
  private long id;

  private String name;

  private int age;

  private String ignore;

  private Date mTiMe;

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public String getIgnore() {
    return ignore;
  }

  public Date getmTiMe() {
    return mTiMe;
  }

  @Override
  public String toString() {
    return new StringBuilder("id=").append(id).append(",name=").append(name).append(",age=").append(age).toString();
  }
}
