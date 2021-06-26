package com.syu.geometry;

public interface VCollection {
    boolean add(Object obj);

    boolean addAll(VCollection vCollection);

    void clear();

    boolean contains(Object obj);

    boolean containsAll(VCollection vCollection);

    boolean equals(Object obj);

    int hashCode();

    boolean isEmpty();

    VIterator iterator();

    boolean remove(Object obj);

    boolean removeAll(VCollection vCollection);

    boolean retainAll(VCollection vCollection);

    int size();

    Object[] toArray();

    Object[] toArray(Object[] objArr);
}
