package org.iq80.leveldb.impl;

import com.google.common.collect.Maps;
import org.iq80.leveldb.WriteBatch;
import org.iq80.leveldb.util.Slice;
import org.iq80.leveldb.util.Slices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import static com.google.common.base.Preconditions.checkNotNull;

public class WriteBatchImpl implements WriteBatch {
  private final List<Entry<Slice, Slice>> batch = new ArrayList<>();
  private int approximateSize;

  public int getApproximateSize() {
    return approximateSize;
  }

  public int size() {
    return batch.size();
  }

  @Override
  public WriteBatchImpl put(byte[] key, byte[] value) {
    checkNotNull(key);
    checkNotNull(value);
    batch.add(Maps.immutableEntry(Slices.wrappedBuffer(key), Slices.wrappedBuffer(value)));
    approximateSize += 12 + key.length + value.length;
    return this;
  }

  public WriteBatchImpl put(Slice key, Slice value) {
    checkNotNull(key);
    checkNotNull(value);
    batch.add(Maps.immutableEntry(key, value));
    approximateSize += 12 + key.length() + value.length();
    return this;
  }

  @Override
  public WriteBatchImpl delete(byte[] key) {
    checkNotNull(key);
    batch.add(Maps.immutableEntry(Slices.wrappedBuffer(key), null));
    approximateSize += 6 + key.length;
    return this;
  }

  public WriteBatchImpl delete(Slice key) {
    checkNotNull(key);
    batch.add(Maps.immutableEntry(key, null));
    approximateSize += 6 + key.length();
    return this;
  }

  @Override
  public void close() {
  }

  public void forEach(Handler handler) {
    for (Entry<Slice, Slice> entry : batch) {
      Slice key = entry.getKey();
      Slice value = entry.getValue();
      if (value != null) {
        handler.put(key, value);
      } else {
        handler.delete(key);
      }
    }
  }

  public interface Handler {
    void put(Slice key, Slice value);

    void delete(Slice key);
  }
}
