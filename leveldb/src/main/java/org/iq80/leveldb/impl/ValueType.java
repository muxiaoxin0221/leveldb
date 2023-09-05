package org.iq80.leveldb.impl;

public enum ValueType {
  DELETION(0x00),
  VALUE(0x01);

  private final int persistentId;

  ValueType(int persistentId) {
    this.persistentId = persistentId;
  }

  public int getPersistentId() {
    return persistentId;
  }

  public static ValueType getValueType(int persistentId) {
    return switch (persistentId) {
      case 0 -> DELETION;
      case 1 -> VALUE;
      default -> throw new IllegalArgumentException("Unknown persistentId " + persistentId);
    };
  }
}
