package com.syu.geometry;

import java.io.Serializable;

public interface CCollection extends Serializable {
    void add(Object obj);

    void addAll(CCollection cCollection);

    void clear();

    boolean contains(Object obj);

    boolean equals(Object obj);

    int hashCode();

    boolean isEmpty();

    CIterator iterator();

    void remove(Object obj);

    void removeAll(CCollection cCollection);

    int size();

    void sort();

    Object[] toArray();

    Object[] toArray(Object[] objArr);
}
