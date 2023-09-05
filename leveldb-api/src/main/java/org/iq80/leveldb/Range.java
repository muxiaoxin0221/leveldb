package org.iq80.leveldb;

import com.google.common.base.Preconditions;

public class Range {
  private final byte[] start;
  private final byte[] limit;

  public byte[] limit() {
    return limit;
  }

  public byte[] start() {
    return start;
  }

  public Range(byte[] start, byte[] limit) {
    Preconditions.checkNotNull(start);
    Preconditions.checkNotNull(limit);
    this.limit = limit;
    this.start = start;
  }
}
