package org.onelab.data;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Chunliang.Han on 2019-09-27.
 */
public class Statement {

  private final StringBuilder content = new StringBuilder();

  private final LinkedList<Object> params = new LinkedList<Object>();

  public static Statement init() {
    return new Statement();
  }

  public static Statement init(CharSequence content) {
    return init().appendContent(content);
  }

  public List params() {
    return params;
  }

  public StringBuilder content() {
    return content;
  }

  public Statement appendParams(Object... params) {
    if (params != null) {
      for (Object param : params) {
        this.params.add(param);
      }
    }
    return this;
  }

  public Statement appendParams(Collection params) {
    if (params != null) {
      this.params.addAll(params);
    }
    return this;
  }

  public Statement appendContent(CharSequence content) {
    if (content != null) {
      this.content.append(content);
    }
    return this;
  }

  public Statement append(Statement statement) {
    return appendContent(statement.content).appendParams(statement.params);
  }

  public boolean isEmpty() {
    return content.length() == 0;
  }
}
