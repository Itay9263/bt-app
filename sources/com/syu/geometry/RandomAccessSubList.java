package com.syu.geometry;

class RandomAccessSubList extends SubList implements VRandomAccess {
    RandomAccessSubList(VAbstractList vAbstractList, int i, int i2) {
        super(vAbstractList, i, i2);
    }

    public VList subList(int i, int i2) {
        return new RandomAccessSubList(this, i, i2);
    }
}
