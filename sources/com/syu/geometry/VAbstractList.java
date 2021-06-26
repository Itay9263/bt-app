package com.syu.geometry;

public abstract class VAbstractList extends VAbstractCollection implements VList {
    protected transient int modCount = 0;

    private class Itr implements VIterator {
        int cursor;
        int expectedModCount;
        int lastRet;

        private Itr() {
            this.cursor = 0;
            this.lastRet = -1;
            this.expectedModCount = VAbstractList.this.modCount;
        }

        /* synthetic */ Itr(VAbstractList vAbstractList, Itr itr) {
            this();
        }

        /* synthetic */ Itr(VAbstractList vAbstractList, Itr itr, Itr itr2) {
            this();
        }

        /* access modifiers changed from: package-private */
        public final void checkForComodification() {
            if (VAbstractList.this.modCount != this.expectedModCount) {
                throw new RuntimeException();
            }
        }

        public boolean hasNext() {
            return this.cursor != VAbstractList.this.size();
        }

        public Object next() {
            checkForComodification();
            try {
                Object obj = VAbstractList.this.get(this.cursor);
                int i = this.cursor;
                this.cursor = i + 1;
                this.lastRet = i;
                return obj;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new RuntimeException();
            }
        }

        public void remove() {
            if (this.lastRet == -1) {
                throw new IllegalStateException();
            }
            checkForComodification();
            try {
                VAbstractList.this.remove(this.lastRet);
                if (this.lastRet < this.cursor) {
                    this.cursor--;
                }
                this.lastRet = -1;
                this.expectedModCount = VAbstractList.this.modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new RuntimeException();
            }
        }
    }

    private class ListItr extends Itr implements VListIterator {
        ListItr(int i) {
            super(VAbstractList.this, (Itr) null);
            this.cursor = i;
        }

        public void add(Object obj) {
            checkForComodification();
            try {
                VAbstractList vAbstractList = VAbstractList.this;
                int i = this.cursor;
                this.cursor = i + 1;
                vAbstractList.add(i, obj);
                this.lastRet = -1;
                this.expectedModCount = VAbstractList.this.modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new RuntimeException();
            }
        }

        public boolean hasPrevious() {
            return this.cursor != 0;
        }

        public int nextIndex() {
            return this.cursor;
        }

        public Object previous() {
            checkForComodification();
            try {
                int i = this.cursor - 1;
                Object obj = VAbstractList.this.get(i);
                this.cursor = i;
                this.lastRet = i;
                return obj;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new RuntimeException();
            }
        }

        public int previousIndex() {
            return this.cursor - 1;
        }

        public void set(Object obj) {
            if (this.lastRet == -1) {
                throw new IllegalStateException();
            }
            checkForComodification();
            try {
                VAbstractList.this.set(this.lastRet, obj);
                this.expectedModCount = VAbstractList.this.modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new RuntimeException();
            }
        }
    }

    protected VAbstractList() {
    }

    public void add(int i, Object obj) {
    }

    public boolean add(Object obj) {
        add(size(), obj);
        return true;
    }

    public boolean addAll(int i, VCollection vCollection) {
        boolean z = false;
        VIterator it = vCollection.iterator();
        while (it.hasNext()) {
            add(i, it.next());
            z = true;
            i++;
        }
        return z;
    }

    public void clear() {
        removeRange(0, size());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof VList)) {
            return false;
        }
        VListIterator listIterator = listIterator();
        VListIterator listIterator2 = ((VList) obj).listIterator();
        while (listIterator.hasNext() && listIterator2.hasNext()) {
            Object next = listIterator.next();
            Object next2 = listIterator2.next();
            if (next == null) {
                if (next2 != null) {
                }
            } else if (!next.equals(next2)) {
            }
            return false;
        }
        return !listIterator.hasNext() && !listIterator2.hasNext();
    }

    public abstract Object get(int i);

    public int hashCode() {
        int i = 1;
        VIterator it = iterator();
        while (it.hasNext()) {
            Object next = it.next();
            i = (next == null ? 0 : next.hashCode()) + (i * 31);
        }
        return i;
    }

    public int indexOf(Object obj) {
        VListIterator listIterator = listIterator();
        if (obj == null) {
            while (listIterator.hasNext()) {
                if (listIterator.next() == null) {
                    return listIterator.previousIndex();
                }
            }
        } else {
            while (listIterator.hasNext()) {
                if (obj.equals(listIterator.next())) {
                    return listIterator.previousIndex();
                }
            }
        }
        return -1;
    }

    public VIterator iterator() {
        return new Itr(this, (Itr) null, (Itr) null);
    }

    public int lastIndexOf(Object obj) {
        VListIterator listIterator = listIterator(size());
        if (obj == null) {
            while (listIterator.hasPrevious()) {
                if (listIterator.previous() == null) {
                    return listIterator.nextIndex();
                }
            }
        } else {
            while (listIterator.hasPrevious()) {
                if (obj.equals(listIterator.previous())) {
                    return listIterator.nextIndex();
                }
            }
        }
        return -1;
    }

    public VListIterator listIterator() {
        return listIterator(0);
    }

    public VListIterator listIterator(int i) {
        if (i >= 0 && i < size()) {
            return new ListItr(i);
        }
        throw new IndexOutOfBoundsException("Index: " + i);
    }

    public Object remove(int i) {
        return null;
    }

    /* access modifiers changed from: protected */
    public void removeRange(int i, int i2) {
        VListIterator listIterator = listIterator(i);
        int i3 = i2 - i;
        for (int i4 = 0; i4 < i3; i4++) {
            listIterator.next();
            listIterator.remove();
        }
    }

    public Object set(int i, Object obj) {
        return null;
    }

    public VList subList(int i, int i2) {
        return this instanceof VRandomAccess ? new RandomAccessSubList(this, i, i2) : new SubList(this, i, i2);
    }
}
