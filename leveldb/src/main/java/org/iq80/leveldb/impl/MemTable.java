package org.iq80.leveldb.impl;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import org.iq80.leveldb.util.InternalIterator;
import org.iq80.leveldb.util.Slice;

import static org.iq80.leveldb.util.SizeOf.SIZE_OF_LONG;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import static com.google.common.base.Preconditions.checkNotNull;

public class MemTable implements SeekingIterable<InternalKey, Slice> {

  private final ConcurrentSkipListMap<InternalKey, Slice> table;
  private final AtomicLong approximateMemoryUsage = new AtomicLong();

  public MemTable(InternalKeyComparator internalKeyComparator) {
    table = new ConcurrentSkipListMap<>(internalKeyComparator);
  }

  public boolean isEmpty() {
    return table.isEmpty();
  }

  public long approximateMemoryUsage() {
    return approximateMemoryUsage.get();
  }

  public void add(long sequenceNumber, ValueType valueType, Slice key, Slice value) {
    checkNotNull(valueType);
    checkNotNull(key);
    checkNotNull(valueType);

    InternalKey internalKey = new InternalKey(key, sequenceNumber, valueType);
    table.put(internalKey, value);

    approximateMemoryUsage.addAndGet(key.length() + SIZE_OF_LONG + value.length());
  }

  public LookupResult get(LookupKey key) {
    checkNotNull(key);

    InternalKey internalKey = key.getInternalKey();
    Entry<InternalKey, Slice> entry = table.ceilingEntry(internalKey);
    if (entry == null) {
      return null;
    }

    InternalKey entryKey = entry.getKey();
    if (entryKey.getUserKey().equals(key.getUserKey())) {
      if (entryKey.getValueType() == ValueType.DELETION) {
        return LookupResult.deleted(key);
      } else {
        return LookupResult.ok(key, entry.getValue());
      }
    }
    return null;
  }

  @Override
  public MemTableIterator iterator() {
    return new MemTableIterator();
  }

  public class MemTableIterator
      implements InternalIterator {
    private PeekingIterator<Entry<InternalKey, Slice>> iterator;

    public MemTableIterator() {
      iterator = Iterators.peekingIterator(table.entrySet().iterator());
    }

    @Override
    public boolean hasNext() {
      return iterator.hasNext();
    }

    @Override
    public void seekToFirst() {
      iterator = Iterators.peekingIterator(table.entrySet().iterator());
    }

    @Override
    public void seek(InternalKey targetKey) {
      iterator = Iterators.peekingIterator(table.tailMap(targetKey).entrySet().iterator());
    }

    @Override
    public InternalEntry peek() {
      Entry<InternalKey, Slice> entry = iterator.peek();
      return new InternalEntry(entry.getKey(), entry.getValue());
    }

    @Override
    public InternalEntry next() {
      Entry<InternalKey, Slice> entry = iterator.next();
      return new InternalEntry(entry.getKey(), entry.getValue());
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
