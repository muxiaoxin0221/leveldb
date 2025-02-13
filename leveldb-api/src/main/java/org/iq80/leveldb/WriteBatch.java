package org.iq80.leveldb;

import java.io.Closeable;

public interface WriteBatch extends Closeable {

  WriteBatch put(byte[] key, byte[] value);

  WriteBatch delete(byte[] key);

}
