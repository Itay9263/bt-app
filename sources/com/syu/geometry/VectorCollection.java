package com.syu.geometry;

import java.util.Vector;

public class VectorCollection implements CCollection {
    private static final long serialVersionUID = 1;
    Vector<Object> v;

    protected class VectorIterator implements CIterator {
        int pointer = 0;
        Vector<Object> v = null;

        public VectorIterator(Vector<Object> vector) {
            this.v = vector;
        }

        public boolean hasNext() {
            return this.pointer < this.v.size();
        }

        public Object next() {
            Vector<Object> vector = this.v;
            int i = this.pointer;
            this.pointer = i + 1;
            return vector.elementAt(i);
        }

        public void reset() {
            this.pointer = 0;
        }
    }

    public VectorCollection() {
        this(10);
    }

    public VectorCollection(int i) {
        this(i, 10);
    }

    public VectorCollection(int i, int i2) {
        this.v = new Vector<>(i, i2);
    }

    public VectorCollection(Vector<Object> vector) {
        this.v = vector;
    }

    public static void main(String[] strArr) {
    }

    public void add(Object obj) {
        this.v.addElement(obj);
    }

    public void addAll(CCollection cCollection) {
        CIterator it = cCollection.iterator();
        while (it.hasNext()) {
            this.v.addElement(it.next());
        }
    }

    public void clear() {
        this.v.removeAllElements();
    }

    public boolean contains(Object obj) {
        return this.v.contains(obj);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CCollection)) {
            return false;
        }
        CCollection cCollection = (CCollection) obj;
        if (cCollection.size() != size()) {
            return false;
        }
        CIterator it = iterator();
        CIterator it2 = cCollection.iterator();
        while (it.hasNext()) {
            if (!it.next().equals(it2.next())) {
                return false;
            }
        }
        return true;
    }

    public int get(Object obj) {
        return this.v.indexOf(obj);
    }

    public Object get(int i) {
        return this.v.elementAt(i);
    }

    public boolean isEmpty() {
        return this.v.isEmpty();
    }

    public CIterator iterator() {
        return new VectorIterator(this.v);
    }

    public void remove(Object obj) {
        this.v.removeElement(obj);
    }

    public void removeAll(CCollection cCollection) {
        CIterator it = cCollection.iterator();
        while (it.hasNext()) {
            this.v.removeElement(it.next());
        }
    }

    public int size() {
        return this.v.size();
    }

    public void sort() {
    }

    public Object[] toArray() {
        Object[] objArr = new Object[this.v.size()];
        this.v.copyInto(objArr);
        return objArr;
    }

    public Object[] toArray(Object[] objArr) {
        return null;
    }
}
