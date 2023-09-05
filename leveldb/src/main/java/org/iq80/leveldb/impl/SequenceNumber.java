package org.iq80.leveldb.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public final class SequenceNumber {
  // We leave eight bits empty at the bottom so a type and sequence#
  // can be packed together into 64-bits.
  public static final long MAX_SEQUENCE_NUMBER = ((0x1L << 56) - 1);

  private SequenceNumber() {
  }

  public static long packSequenceAndValueType(long sequence, ValueType valueType) {
    checkArgument(sequence <= MAX_SEQUENCE_NUMBER, "Sequence number is greater than MAX_SEQUENCE_NUMBER");
    checkNotNull(valueType);
    return (sequence << 8) | valueType.getPersistentId();
  }

  public static ValueType unpackValueType(long num) {
    return ValueType.getValueType((byte) num);
  }

  public static long unpackSequenceNumber(long num) {
    return num >>> 8;
  }
}
