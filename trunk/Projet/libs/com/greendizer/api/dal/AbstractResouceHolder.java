package com.greendizer.api.dal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.greendizer.api.resource.AbstractResource;

/**
 * Represents the base class for resource holding management.
 * It handles a dictionary of resources referenced by their ids to be manipulated as a list.
 * @param <Type> A resource type.
 */
public abstract class AbstractResouceHolder<Type extends AbstractResource> implements List<Type> {

  protected LinkedHashMap<String, Type> resources;

  /**
   * Creates a new resource holder.
   */
  public AbstractResouceHolder() {
    this.resources = new LinkedHashMap<String, Type>();
  }

  /**
   * Returns the resource referenced in the resource holder by the given id, null if not found.
   * @param id Reference id in the resource holder of the resource to be retrieved.
   * @return Resource referenced in the resource holder by the given id, null if not found.
   */
  public Type getById(String id) {
    return resources.get(id);
  }

  /**
   * Returns the number of resources in the resource holder.
   * @return Number of resources in the resource holder.
   */
  @Override
  public int size() {
    return resources.size();
  }

  /**
   * Clears all the resources of the resource holder.
   */
  @Override
  public void clear() {
    resources.clear();
  }

  /**
   * Returns true if the resource holder is empty, false otherwise.
   * @return True if the resource holder is empty, false otherwise.
   */
  @Override
  public boolean isEmpty() {
    return resources.isEmpty();
  }

  /**
   * Returns true if the resource holder contains the given resource, false otherwise.
   * @param o Resource to check against.
   * @return True if the resource holder contains the given resource, false otherwise.
   */
  @Override
  public boolean contains(Object o) {
    return resources.containsValue(o);
  }

  /**
   * Returns true if the resource holder contains all the resources in the given {@code java.util.Collection}, false otherwise.
   * @param o {@code java.util.Collection} of resources to check against.
   * @return True if the resource holder contains all the resources in the given {@code java.util.Collection}, false otherwise.
   */
  @Override
  public boolean containsAll(java.util.Collection<?> c) {
    for (Object resource : c) {
      if (!resources.containsValue(resource)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the index-th resource in the resource holder if found, null otherwise.
   * @param index Index to check against.
   * @return Index-th resource in the resource holder if found, null otherwise.
   */
  @Override
  public Type get(int index) {
    if (isEmpty()) {
      throw new RuntimeException("the resource holder is empty.");
    }
    int i = 0;
    for (String id : resources.keySet()) {
      if (i++ == index) {
        return resources.get(id);
      }
    }
    return null;
  }

  /**
   * Returns the first resource in the resource holder if found, null otherwise.
   * @return First resource in the resource holder if found, null otherwise.
   */
  public Type getFirst() {
    return get(0);
  }

  /**
   * Returns the last resource in the resource holder if found, null otherwise.
   * @return Last resource in the resource holder if found, null otherwise.
   */
  public Type getLast() {
    return get(size() - 1);
  }

  /**
   * Returns the index of the given resource if found in the resource holder, -1 otherwise.
   * @param o Resource to check against.
   * @return Index of the given resource if found in the resource holder, -1 otherwise.
   */
  @Override
  public int indexOf(Object o) {
    int i = 0;
    for (String id : resources.keySet()) {
      if (o.equals(resources.get(id))) {
        return i++;
      }
    }
    return -1;
  }

  /**
   * Returns the last index of the given resource if found in the resource holder, -1 otherwise.
   * This method is equivalent to <code>indexOf()</code> as of this implementation.
   * @param o Resource to check against.
   * @return Index of last the given resource if found in the resource holder, -1 otherwise.
   */
  @Override
  public int lastIndexOf(Object o) {
    return indexOf(o);
  }

  /**
   * Returns an iterator over the resources in the resource holder in proper sequence.
   * @return Iterator over the resources in the resource holder in proper sequence.
   */
  @Override
  public Iterator<Type> iterator() {
    return resources.values().iterator();
  }

  /**
   * Returns a view of the portion of this collection between the specified <code>fromIndex</code>, inclusive, and <code>toIndex</code>, exclusive.
   * @param fromIndex low endpoint (inclusive) of the subList.
   * @param toIndex high endpoint (exclusive) of the subList.
   * @return View of the portion of this collection between the specified <code>fromIndex</code>, inclusive, and <code>toIndex</code>, exclusive.
   */
  @Override
  public List<Type> subList(int fromIndex, int toIndex) {
    List<Type> list = new ArrayList<Type>();
    int i = 0;
    for (String id : resources.keySet()) {
      if (i >= fromIndex) {
        list.add(resources.get(id));
      }
      if (++i == toIndex) {
        break;
      }
    }
    return list;
  }

  /**
   * Returns an array containing all of the elements in the resource holder in proper sequence (from first to last element).
   * @return Array containing all of the elements in the resource holder in proper sequence (from first to last element).
   */
  @Override
  public Object[] toArray() {
    return resources.values().toArray();
  }

  /**
   * Returns an array containing all of the elements in the resource holder in proper sequence (from first to last element).
   * @param the array into which the elements of the resource holder are to be stored, if it is big enough; otherwise, a new array of the same runtime type is allocated for this purpose.
   * @return Array containing all of the elements in the resource holder in proper sequence (from first to last element).
   */
  @Override
  public <T> T[] toArray(T[] array) {
    return resources.values().toArray(array);
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public ListIterator<Type> listIterator() {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public ListIterator<Type> listIterator(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public boolean remove(Object arg0) {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public Type remove(int arg0) {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public boolean removeAll(java.util.Collection<?> arg0) {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public boolean retainAll(java.util.Collection<?> arg0) {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public Type set(int arg0, Type arg1) {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public boolean add(Type arg0) {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public void add(int arg0, Type arg1) {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public boolean addAll(java.util.Collection<? extends Type> arg0) {
    throw new UnsupportedOperationException();
  }

  /**
   * Defined in {@code java.util.List} but not supported in this implementation.
   */
  @Override
  public boolean addAll(int arg0, java.util.Collection<? extends Type> arg1) {
    throw new UnsupportedOperationException();
  }

  public Map<String, Type> getResources() {
    return resources;
  }
}
