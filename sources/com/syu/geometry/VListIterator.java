package com.syu.geometry;

public interface VListIterator extends VIterator {
    void add(Object obj);

    boolean hasNext();

    boolean hasPrevious();

    Object next();

    int nextIndex();

    Object previous();

    int previousIndex();

    void remove();

    void set(Object obj);
}
