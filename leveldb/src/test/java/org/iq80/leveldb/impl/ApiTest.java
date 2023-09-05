package org.iq80.leveldb.impl;

import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBException;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.util.FileUtils;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ApiTest {

  private static final File dbfile = FileUtils.createTempDir("leveldb");
  private static final DBFactory factory = Iq80DBFactory.factory;
  private static DB db;

  @BeforeAll
  public static void initAll() {
    Options options = new Options()
        .createIfMissing(true)
        .compressionType(CompressionType.NONE);

    try {
      if (dbfile.exists()) {
        factory.destroy(dbfile, options);
        dbfile.mkdir();
      }
      db = factory.open(dbfile, options);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testCompaction() {
    // Adding
    for (int i = 0; i < 1000 * 1000; i++) {
      db.put(bytes("key" + i), bytes("value" + i));
    }

    // Deleting
    for (int i = 0; i < 1000 * 1000; i++) {
      db.delete(bytes("key" + i));
    }

    // Adding
    for (int i = 0; i < 1000 * 1000; i++) {
      db.put(bytes("key" + i), bytes("value" + i));
    }
  }

  @AfterAll
  public static void tearDownAll() {
    try {
      db.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
