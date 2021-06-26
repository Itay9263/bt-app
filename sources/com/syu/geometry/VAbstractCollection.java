package com.syu.geometry;

import java.lang.reflect.Array;

public abstract class VAbstractCollection implements VCollection {
    protected VAbstractCollection() {
    }

    public boolean add(Object obj) {
        return false;
    }

    public boolean addAll(VCollection vCollection) {
        boolean z = false;
        VIterator it = vCollection.iterator();
        while (it.hasNext()) {
            if (add(it.next())) {
                z = true;
            }
        }
        return z;
    }

    public void clear() {
        VIterator it = iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }

    public boolean contains(Object obj) {
        VIterator it = iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (obj.equals(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsAll(VCollection vCollection) {
        VIterator it = vCollection.iterator();
        while (it.hasNext()) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public abstract VIterator iterator();

    public boolean remove(Object obj) {
        VIterator it = iterator();
        if (obj == null) {
            while (it.hasNext()) {
                if (it.next() == null) {
                    it.remove();
                    return true;
                }
            }
        } else {
            while (it.hasNext()) {
                if (obj.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean removeAll(VCollection vCollection) {
        boolean z = false;
        VIterator it = iterator();
        while (it.hasNext()) {
            if (vCollection.contains(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public boolean retainAll(VCollection vCollection) {
        boolean z = false;
        VIterator it = iterator();
        while (it.hasNext()) {
            if (!vCollection.contains(it.next())) {
                it.remove();
                z = true;
            }
        }
        return z;
    }

    public abstract int size();

    public Object[] toArray() {
        Object[] objArr = new Object[size()];
        VIterator it = iterator();
        int i = 0;
        while (it.hasNext()) {
            objArr[i] = it.next();
            i++;
        }
        return objArr;
    }

    public Object[] toArray(Object[] objArr) {
        int size = size();
        Object[] objArr2 = objArr.length < size ? (Object[]) Array.newInstance(objArr.getClass().getComponentType(), size) : objArr;
        VIterator it = iterator();
        for (int i = 0; i < size; i++) {
            objArr2[i] = it.next();
        }
        if (objArr2.length > size) {
            objArr2[size] = null;
        }
        return objArr2;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        VIterator it = iterator();
        boolean hasNext = it.hasNext();
        while (hasNext) {
            Object next = it.next();
            stringBuffer.append(next == this ? "(this Collection)" : String.valueOf(next));
            hasNext = it.hasNext();
            if (hasNext) {
                stringBuffer.append(", ");
            }
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
