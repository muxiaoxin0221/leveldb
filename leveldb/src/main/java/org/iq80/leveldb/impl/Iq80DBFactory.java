package org.iq80.leveldb.impl;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBFactory;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.util.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Iq80DBFactory implements DBFactory {

  public static final int CPU_DATA_MODEL;

  static {
    boolean is64bit = System.getProperty("os.arch").contains("64");
    CPU_DATA_MODEL = is64bit ? 64 : 32;
  }

  // We only use MMAP on 64 bit systems since it's really easy to run out of
  // virtual address space on a 32 bit system when all the data is getting mapped
  // into memory.  If you really want to use MMAP anyways, use -Dleveldb.mmap=true
  public static final boolean USE_MMAP = Boolean.parseBoolean(System.getProperty("leveldb.mmap", "" + (CPU_DATA_MODEL > 32)));

  public static final String VERSION;

  static {
    String version = "unknown";
    try (InputStream is = Iq80DBFactory.class.getResourceAsStream("version.txt")) {
      version = new BufferedReader(new InputStreamReader(is, UTF_8)).readLine();
    } catch (Throwable e) {
    }
    VERSION = version;
  }

  public static final Iq80DBFactory factory = new Iq80DBFactory();

  @Override
  public DB open(File dbfile, Options options) throws IOException {
    return new DbImpl(options, dbfile);
  }

  @Override
  public void destroy(File path, Options options) {
    FileUtils.deleteRecursively(path);
  }

  @Override
  public String toString() {
    return String.format("iq80 leveldb version %s", VERSION);
  }

  public static byte[] bytes(String value) {
    return (value == null) ? null : value.getBytes(UTF_8);
  }

  public static String asString(byte[] value) {
    return (value == null) ? null : new String(value, UTF_8);
  }
}
