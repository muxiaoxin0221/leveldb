package org.iq80.leveldb.impl;

import org.iq80.leveldb.util.Slice;
import org.iq80.leveldb.util.SliceOutput;
import org.iq80.leveldb.util.Slices;

import java.util.Objects;

import static org.iq80.leveldb.util.SizeOf.SIZE_OF_LONG;

import static com.google.common.base.Preconditions.checkArgument;
import static java.nio.charset.StandardCharsets.UTF_8;
import static com.google.common.base.Preconditions.checkNotNull;


public class InternalKey {

  private final Slice userKey;
  private final long sequenceNumber;
  private final ValueType valueType;
  private int hash;

  public InternalKey(Slice userKey, long sequenceNumber, ValueType valueType) {
    checkNotNull(userKey);
    checkNotNull(valueType);
    checkArgument(sequenceNumber >= 0);

    this.userKey = userKey;
    this.sequenceNumber = sequenceNumber;
    this.valueType = valueType;
  }

  public InternalKey(Slice data) {
    checkNotNull(data);
    checkArgument(data.length() >= SIZE_OF_LONG, "data must be at least %s bytes", SIZE_OF_LONG);

    this.userKey = getUserKey(data);
    long packedSequenceAndType = data.getLong(data.length() - SIZE_OF_LONG);
    this.sequenceNumber = SequenceNumber.unpackSequenceNumber(packedSequenceAndType);
    this.valueType = SequenceNumber.unpackValueType(packedSequenceAndType);
  }

  public InternalKey(byte[] data) {
    this(Slices.wrappedBuffer(data));
  }

  public Slice getUserKey() {
    return userKey;
  }

  public long getSequenceNumber() {
    return sequenceNumber;
  }

  public ValueType getValueType() {
    return valueType;
  }

  public Slice encode() {
    Slice slice = Slices.allocate(userKey.length() + SIZE_OF_LONG);
    SliceOutput sliceOutput = slice.output();
    sliceOutput.writeBytes(userKey);
    sliceOutput.writeLong(SequenceNumber.packSequenceAndValueType(sequenceNumber, valueType));
    return slice;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;

    InternalKey that = (InternalKey) obj;
    if (sequenceNumber != that.sequenceNumber) {
      return false;
    }
    if (!Objects.equals(userKey, that.userKey)) {
      return false;
    }
    return valueType == that.valueType;
  }

  @Override
  public int hashCode() {
    if (hash == 0) {
      int result = userKey != null ? userKey.hashCode() : 0;
      result = 31 * result + (int) (sequenceNumber ^ (sequenceNumber >>> 32));
      result = 31 * result + (valueType != null ? valueType.hashCode() : 0);
      if (result == 0) {
        result = 1;
      }
      hash = result;
    }
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("InternalKey");
    sb.append("{key=").append(getUserKey().toString(UTF_8));      // todo don't print the real value
    sb.append(", sequenceNumber=").append(getSequenceNumber());
    sb.append(", valueType=").append(getValueType());
    sb.append('}');
    return sb.toString();
  }

  private static Slice getUserKey(Slice data) {
    return data.slice(0, data.length() - SIZE_OF_LONG);
  }
}
