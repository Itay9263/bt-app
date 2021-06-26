package com.syu.geometry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;

public class VArrayList extends VAbstractList implements VList, VRandomAccess, Serializable, Cloneable {
    private static final long serialVersionUID = 8683452581122892189L;
    private transient Object[] elementData;
    private int size;

    public VArrayList() {
        this(10);
    }

    public VArrayList(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + i);
        }
        this.elementData = new Object[i];
    }

    public VArrayList(VCollection vCollection) {
        this.size = vCollection.size();
        this.elementData = new Object[((int) Math.min((((long) this.size) * 110) / 100, 2147483647L))];
        vCollection.toArray(this.elementData);
    }

    private void RangeCheck(int i) {
        if (i >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + this.size);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.elementData = new Object[objectInputStream.readInt()];
        for (int i = 0; i < this.size; i++) {
            this.elementData[i] = objectInputStream.readObject();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.elementData.length);
        for (int i = 0; i < this.size; i++) {
            objectOutputStream.writeObject(this.elementData[i]);
        }
    }

    public void add(int i, Object obj) {
        if (i > this.size || i < 0) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + this.size);
        }
        ensureCapacity(this.size + 1);
        System.arraycopy(this.elementData, i, this.elementData, i + 1, this.size - i);
        this.elementData[i] = obj;
        this.size++;
    }

    public boolean add(Object obj) {
        ensureCapacity(this.size + 1);
        Object[] objArr = this.elementData;
        int i = this.size;
        this.size = i + 1;
        objArr[i] = obj;
        return true;
    }

    public boolean addAll(int i, VCollection vCollection) {
        if (i > this.size || i < 0) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + this.size);
        }
        Object[] array = vCollection.toArray();
        int length = array.length;
        ensureCapacity(this.size + length);
        int i2 = this.size - i;
        if (i2 > 0) {
            System.arraycopy(this.elementData, i, this.elementData, i + length, i2);
        }
        System.arraycopy(array, 0, this.elementData, i, length);
        this.size += length;
        return length != 0;
    }

    public boolean addAll(VCollection vCollection) {
        Object[] array = vCollection.toArray();
        int length = array.length;
        ensureCapacity(this.size + length);
        System.arraycopy(array, 0, this.elementData, this.size, length);
        this.size += length;
        return length != 0;
    }

    public void clear() {
        this.modCount++;
        for (int i = 0; i < this.size; i++) {
            this.elementData[i] = null;
        }
        this.size = 0;
    }

    public Object clone() {
        try {
            VArrayList vArrayList = (VArrayList) super.clone();
            vArrayList.elementData = new Object[this.size];
            System.arraycopy(this.elementData, 0, vArrayList.elementData, 0, this.size);
            vArrayList.modCount = 0;
            return vArrayList;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    public void ensureCapacity(int i) {
        this.modCount++;
        int length = this.elementData.length;
        if (i > length) {
            Object[] objArr = this.elementData;
            int i2 = ((length * 3) / 2) + 1;
            if (i2 >= i) {
                i = i2;
            }
            this.elementData = new Object[i];
            System.arraycopy(objArr, 0, this.elementData, 0, this.size);
        }
    }

    public Object get(int i) {
        RangeCheck(i);
        return this.elementData[i];
    }

    public int indexOf(Object obj) {
        int i = 0;
        if (obj == null) {
            while (i < this.size) {
                if (this.elementData[i] == null) {
                    return i;
                }
                i++;
            }
        } else {
            while (i < this.size) {
                if (obj.equals(this.elementData[i])) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int lastIndexOf(Object obj) {
        if (obj == null) {
            for (int i = this.size - 1; i >= 0; i--) {
                if (this.elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i2 = this.size - 1; i2 >= 0; i2--) {
                if (obj.equals(this.elementData[i2])) {
                    return i2;
                }
            }
        }
        return -1;
    }

    public Object remove(int i) {
        RangeCheck(i);
        this.modCount++;
        Object obj = this.elementData[i];
        int i2 = (this.size - i) - 1;
        if (i2 > 0) {
            System.arraycopy(this.elementData, i + 1, this.elementData, i, i2);
        }
        Object[] objArr = this.elementData;
        int i3 = this.size - 1;
        this.size = i3;
        objArr[i3] = null;
        return obj;
    }

    /* access modifiers changed from: protected */
    public void removeRange(int i, int i2) {
        this.modCount++;
        System.arraycopy(this.elementData, i2, this.elementData, i, this.size - i2);
        int i3 = this.size - (i2 - i);
        while (this.size != i3) {
            Object[] objArr = this.elementData;
            int i4 = this.size - 1;
            this.size = i4;
            objArr[i4] = null;
        }
    }

    public Object set(int i, Object obj) {
        RangeCheck(i);
        Object obj2 = this.elementData[i];
        this.elementData[i] = obj;
        return obj2;
    }

    public int size() {
        return this.size;
    }

    public Object[] toArray() {
        Object[] objArr = new Object[this.size];
        System.arraycopy(this.elementData, 0, objArr, 0, this.size);
        return objArr;
    }

    public Object[] toArray(Object[] objArr) {
        Object[] objArr2 = objArr.length < this.size ? (Object[]) Array.newInstance(objArr.getClass().getComponentType(), this.size) : objArr;
        System.arraycopy(this.elementData, 0, objArr2, 0, this.size);
        if (objArr2.length > this.size) {
            objArr2[this.size] = null;
        }
        return objArr2;
    }

    public void trimToSize() {
        this.modCount++;
        if (this.size < this.elementData.length) {
            Object[] objArr = this.elementData;
            this.elementData = new Object[this.size];
            System.arraycopy(objArr, 0, this.elementData, 0, this.size);
        }
    }
}
