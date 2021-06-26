package com.syu.geometry;

public abstract class AbstractShape implements CCollection, CShape {
    private static final long serialVersionUID = 1954213836045783416L;
    CCollection collection = new VectorCollection(20, 10);
    protected Extent savedExtent = null;

    public void add(Object obj) {
        this.collection.add(obj);
        update();
    }

    public void addAll(CCollection cCollection) {
        cCollection.addAll(cCollection);
        update();
    }

    public void clear() {
        this.collection.clear();
    }

    public boolean contains(Object obj) {
        return this.collection.contains(obj);
    }

    public boolean equals(Object obj) {
        return false;
    }

    public Extent getExtent() {
        double d;
        double d2 = Double.MAX_VALUE;
        if (this.savedExtent == null) {
            CIterator it = this.collection.iterator();
            double d3 = -1.7976931348623157E308d;
            double d4 = -1.7976931348623157E308d;
            double d5 = Double.MAX_VALUE;
            while (true) {
                d = d2;
                if (!it.hasNext()) {
                    break;
                }
                CPoint cPoint = (CPoint) it.next();
                double d6 = cPoint.x;
                double d7 = cPoint.y;
                d2 = d > d6 ? d6 : d;
                if (d5 > d7) {
                    d5 = d7;
                }
                if (d4 < d6) {
                    d4 = d6;
                }
                if (d3 >= d7) {
                    d7 = d3;
                }
                d3 = d7;
            }
            this.savedExtent = new Extent(new CPoint(d, d5), new CPoint(d4, d3));
        }
        return this.savedExtent;
    }

    public CCollection getPoints() {
        return this.collection;
    }

    public abstract boolean hitTest(double d, double d2, double d3);

    public boolean hitTest(CPoint cPoint, double d) {
        return hitTest(cPoint.x, cPoint.y, d);
    }

    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    public CIterator iterator() {
        return this.collection.iterator();
    }

    public void remove(Object obj) {
        this.collection.remove(obj);
        update();
    }

    public void removeAll(CCollection cCollection) {
        cCollection.remove(cCollection);
        update();
    }

    public void setExtent(Extent extent) {
        this.savedExtent = extent;
    }

    public int size() {
        return this.collection.size();
    }

    public void sort() {
    }

    public Object[] toArray() {
        return this.collection.toArray();
    }

    public Object[] toArray(Object[] objArr) {
        return null;
    }

    public void update() {
        this.savedExtent = null;
    }
}
