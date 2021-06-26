package com.syu.geometry;

public interface VList extends VCollection {
    void add(int i, Object obj);

    boolean add(Object obj);

    boolean addAll(int i, VCollection vCollection);

    boolean addAll(VCollection vCollection);

    void clear();

    boolean contains(Object obj);

    boolean containsAll(VCollection vCollection);

    boolean equals(Object obj);

    Object get(int i);

    int hashCode();

    int indexOf(Object obj);

    boolean isEmpty();

    VIterator iterator();

    int lastIndexOf(Object obj);

    VListIterator listIterator();

    VListIterator listIterator(int i);

    Object remove(int i);

    boolean remove(Object obj);

    boolean removeAll(VCollection vCollection);

    boolean retainAll(VCollection vCollection);

    Object set(int i, Object obj);

    int size();

    VList subList(int i, int i2);

    Object[] toArray();

    Object[] toArray(Object[] objArr);
}
