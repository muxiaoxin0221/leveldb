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

import org.junit.jupiter.api.Test;

public class ApiTest {
  private final File databaseDir = FileUtils.createTempDir("leveldb");

  private final DBFactory factory = Iq80DBFactory.factory;

  File getTestDirectory(String name)
      throws IOException {
    File rc = new File(databaseDir, name);
    factory.destroy(rc, new Options().createIfMissing(true));
    rc.mkdirs();
    return rc;
  }

  @Test
  public void testCompaction() throws IOException, DBException {
    Options options = new Options().createIfMissing(true).compressionType(CompressionType.NONE);

    File path = getTestDirectory("testCompaction");
    DB db = factory.open(path, options);

    System.out.println("Adding");
    for (int i = 0; i < 1000 * 1000; i++) {
      if (i % 100000 == 0) {
        System.out.println("  at: " + i);
      }
      db.put(bytes("key" + i), bytes("value" + i));
    }

    db.close();
    db = factory.open(path, options);

    System.out.println("Deleting");
    for (int i = 0; i < 1000 * 1000; i++) {
      if (i % 100000 == 0) {
        System.out.println("  at: " + i);
      }
      db.delete(bytes("key" + i));
    }

    db.close();
    db = factory.open(path, options);

    System.out.println("Adding");
    for (int i = 0; i < 1000 * 1000; i++) {
      if (i % 100000 == 0) {
        System.out.println("  at: " + i);
      }
      db.put(bytes("key" + i), bytes("value" + i));
    }

    db.close();
  }
}
