package com.syu.geometry;

class SubList extends VAbstractList {
    /* access modifiers changed from: private */
    public int expectedModCount;
    /* access modifiers changed from: private */
    public VAbstractList l;
    /* access modifiers changed from: private */
    public int offset;
    /* access modifiers changed from: private */
    public int size;

    SubList(VAbstractList vAbstractList, int i, int i2) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("fromIndex = " + i);
        } else if (i2 > vAbstractList.size()) {
            throw new IndexOutOfBoundsException("toIndex = " + i2);
        } else if (i > i2) {
            throw new IllegalArgumentException("fromIndex(" + i + ") > toIndex(" + i2 + ")");
        } else {
            this.l = vAbstractList;
            this.offset = i;
            this.size = i2 - i;
            this.expectedModCount = this.l.modCount;
        }
    }

    private void checkForComodification() {
        if (this.l.modCount != this.expectedModCount) {
            throw new RuntimeException();
        }
    }

    private void rangeCheck(int i) {
        if (i < 0 || i >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + i + ",Size: " + this.size);
        }
    }

    public void add(int i, Object obj) {
        if (i < 0 || i > this.size) {
            throw new IndexOutOfBoundsException();
        }
        checkForComodification();
        this.l.add(this.offset + i, obj);
        this.expectedModCount = this.l.modCount;
        this.size++;
        this.modCount++;
    }

    public boolean addAll(int i, VCollection vCollection) {
        if (i < 0 || i > this.size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + this.size);
        }
        int size2 = vCollection.size();
        if (size2 == 0) {
            return false;
        }
        checkForComodification();
        this.l.addAll(this.offset + i, vCollection);
        this.expectedModCount = this.l.modCount;
        this.size = size2 + this.size;
        this.modCount++;
        return true;
    }

    public boolean addAll(VCollection vCollection) {
        return addAll(this.size, vCollection);
    }

    public Object get(int i) {
        rangeCheck(i);
        checkForComodification();
        return this.l.get(this.offset + i);
    }

    public VIterator iterator() {
        return listIterator();
    }

    public VListIterator listIterator(int i) {
        checkForComodification();
        if (i >= 0 && i <= this.size) {
            return new VListIterator(i) {
                private VListIterator i;

                {
                    this.i = SubList.this.l.listIterator(SubList.this.offset + r4);
                }

                public void add(Object obj) {
                    this.i.add(obj);
                    SubList.this.expectedModCount = SubList.this.l.modCount;
                    SubList subList = SubList.this;
                    subList.size = subList.size + 1;
                    SubList.this.modCount++;
                }

                public boolean hasNext() {
                    return nextIndex() < SubList.this.size;
                }

                public boolean hasPrevious() {
                    return previousIndex() >= 0;
                }

                public Object next() {
                    if (hasNext()) {
                        return this.i.next();
                    }
                    throw new RuntimeException();
                }

                public int nextIndex() {
                    return this.i.nextIndex() - SubList.this.offset;
                }

                public Object previous() {
                    if (hasPrevious()) {
                        return this.i.previous();
                    }
                    throw new RuntimeException();
                }

                public int previousIndex() {
                    return this.i.previousIndex() - SubList.this.offset;
                }

                public void remove() {
                    this.i.remove();
                    SubList.this.expectedModCount = SubList.this.l.modCount;
                    SubList subList = SubList.this;
                    subList.size = subList.size - 1;
                    SubList.this.modCount++;
                }

                public void set(Object obj) {
                    this.i.set(obj);
                }
            };
        }
        throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + this.size);
    }

    public Object remove(int i) {
        rangeCheck(i);
        checkForComodification();
        Object remove = this.l.remove(this.offset + i);
        this.expectedModCount = this.l.modCount;
        this.size--;
        this.modCount++;
        return remove;
    }

    /* access modifiers changed from: protected */
    public void removeRange(int i, int i2) {
        checkForComodification();
        this.l.removeRange(this.offset + i, this.offset + i2);
        this.expectedModCount = this.l.modCount;
        this.size -= i2 - i;
        this.modCount++;
    }

    public Object set(int i, Object obj) {
        rangeCheck(i);
        checkForComodification();
        return this.l.set(this.offset + i, obj);
    }

    public int size() {
        checkForComodification();
        return this.size;
    }

    public VList subList(int i, int i2) {
        return new SubList(this, i, i2);
    }
}
