package org.iq80.leveldb;

public enum CompressionType {

  NONE(0x00),
  SNAPPY(0x01);

  public static CompressionType getCompressionType(int persistentId) {
    for (CompressionType compressionType : CompressionType.values()) {
      if (compressionType.persistentId == persistentId) {
        return compressionType;
      }
    }
    throw new IllegalArgumentException("Unknown persistentId " + persistentId);
  }

  private final int persistentId;

  CompressionType(int persistentId) {
    this.persistentId = persistentId;
  }

  public int persistentId() {
    return persistentId;
  }
}
