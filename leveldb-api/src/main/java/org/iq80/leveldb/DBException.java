package org.iq80.leveldb;

public class DBException extends RuntimeException {

  public DBException() {
  }

  public DBException(String s) {
    super(s);
  }

  public DBException(Throwable throwable) {
    super(throwable);
  }
}
