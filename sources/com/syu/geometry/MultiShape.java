package com.syu.geometry;

abstract class MultiShape extends VectorCollection implements CShape {
    private static final long serialVersionUID = 1;
    protected Extent savedExtent = null;

    public Extent getExtent() {
        if (this.savedExtent == null) {
            CIterator it = super.iterator();
            Extent extent = null;
            while (it.hasNext()) {
                Object next = it.next();
                if (next instanceof CShape) {
                    extent = ((CShape) next).getExtent().getMaxExtent(extent);
                }
            }
            this.savedExtent = extent;
        }
        return this.savedExtent;
    }

    public abstract boolean hitTest(double d, double d2, double d3);

    public boolean hitTest(CPoint cPoint, double d) {
        return hitTest(cPoint.x, cPoint.y, d);
    }

    public void setExtent(Extent extent) {
        this.savedExtent = extent;
    }

    public void update() {
        this.savedExtent = null;
    }
}
