package utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A hacky queue with O(1) lookup that also enforces uniqueness.
 * @author Kida
 *
 * @param <T>
 */
public class HackyQueue<T> implements Collection<T> {
  List<T> items;
  Set<T> itemSet;

  public HackyQueue() {
    items = new LinkedList<T>();
    itemSet = new HashSet<T>();
  }

  @Override
  public boolean remove(Object o) {
    return items.remove(o) && itemSet.remove(o);
  }

  public T removeByIndex(int index) {
    T t = items.remove(index);
    itemSet.remove(t);
    return t;
  }

  public T removeFirst() {
    return removeByIndex(0);
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  @Override
  public boolean add(T t) {
    if (!contains(t)) {
      itemSet.add(t);
      items.add(t);
      return true;
    }
    return false;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean wasModified = false;
    for (T t : c) {
      wasModified |= add(t);
    }
    return wasModified;
  }

  @Override
  public void clear() {
    items.clear();
    itemSet.clear();
  }

  @Override
  public String toString() {
    return items.toString();
  }

  @Override
  public boolean contains(Object o) {
    return itemSet.contains(o);
  }

  @Override
  public Iterator<T> iterator() {
    return items.iterator();
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size() {
    return items.size();
  }

  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    throw new UnsupportedOperationException();
  }

}
