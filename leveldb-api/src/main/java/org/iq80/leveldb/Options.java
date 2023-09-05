package org.iq80.leveldb;

import com.google.common.base.Preconditions;

public class Options {

  private boolean createIfMissing = true;
  private boolean errorIfExists = false;
  private int writeBufferSize = 4 << 20;
  private int maxOpenFiles = 1000;
  private int blockRestartInterval = 16;
  private int blockSize = 4 * 1024;
  private CompressionType compressionType = CompressionType.SNAPPY;
  private boolean verifyChecksums = true;
  private DBComparator comparator;

  public boolean createIfMissing() {
    return createIfMissing;
  }

  public Options createIfMissing(boolean createIfMissing) {
    this.createIfMissing = createIfMissing;
    return this;
  }

  public boolean errorIfExists() {
    return errorIfExists;
  }

  public Options errorIfExists(boolean errorIfExists) {
    this.errorIfExists = errorIfExists;
    return this;
  }

  public int writeBufferSize() {
    return writeBufferSize;
  }

  public Options writeBufferSize(int writeBufferSize) {
    this.writeBufferSize = writeBufferSize;
    return this;
  }

  public int maxOpenFiles() {
    return maxOpenFiles;
  }

  public Options maxOpenFiles(int maxOpenFiles) {
    this.maxOpenFiles = maxOpenFiles;
    return this;
  }

  public int blockRestartInterval() {
    return blockRestartInterval;
  }

  public Options blockRestartInterval(int blockRestartInterval) {
    this.blockRestartInterval = blockRestartInterval;
    return this;
  }

  public int blockSize() {
    return blockSize;
  }

  public Options blockSize(int blockSize) {
    this.blockSize = blockSize;
    return this;
  }

  public CompressionType compressionType() {
    return compressionType;
  }

  public Options compressionType(CompressionType compressionType) {
    Preconditions.checkNotNull(compressionType);
    this.compressionType = compressionType;
    return this;
  }

  public boolean verifyChecksums() {
    return verifyChecksums;
  }

  public Options verifyChecksums(boolean verifyChecksums) {
    this.verifyChecksums = verifyChecksums;
    return this;
  }

  public DBComparator comparator() {
    return comparator;
  }

  public Options comparator(DBComparator comparator) {
    this.comparator = comparator;
    return this;
  }
}
