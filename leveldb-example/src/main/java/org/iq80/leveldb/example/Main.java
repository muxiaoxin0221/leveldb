package org.iq80.leveldb.example;

import org.iq80.leveldb.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import java.io.*;

public class Main {
  public static void main(String[] args) {
    Options options = new Options();
    options.createIfMissing(true);
    try (DB db = factory.open(new File("example"), options)) {
      db.put(bytes("Tampa"), bytes("rocks"));
      String value = asString(db.get(bytes("Tampa")));
      WriteOptions wo = new WriteOptions();
      db.delete(bytes("Tampa"), wo);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}