package com.syu.geometry;

public class Extent implements Area {
    private static final long serialVersionUID = 1;
    protected CPoint mleftdown;
    protected CPoint mrightup;

    public Extent(double d, double d2, double d3, double d4) {
        this(new CPoint(d, d2), new CPoint(d3, d4));
    }

    public Extent(CPoint cPoint, CPoint cPoint2) {
        this.mleftdown = null;
        this.mrightup = null;
        this.mleftdown = cPoint;
        this.mrightup = cPoint2;
    }

    public Extent changeExtent(double d) {
        double width = (getWidth() * (d - 1.0d)) / 2.0d;
        double height = (getHeight() * (d - 1.0d)) / 2.0d;
        CPoint cPoint = new CPoint(this.mleftdown);
        cPoint.x -= width;
        cPoint.y -= height;
        CPoint cPoint2 = new CPoint(this.mrightup);
        cPoint2.x = width + cPoint2.x;
        cPoint2.y += height;
        return new Extent(cPoint, cPoint2);
    }

    public Object clone() {
        return new Extent(getMinX(), getMinY(), getMaxX(), getMaxY());
    }

    public boolean contains(double d, double d2) {
        return contains(new CPoint(d, d2));
    }

    public boolean contains(CPoint cPoint) {
        return this.mleftdown.x <= cPoint.x && cPoint.x <= this.mrightup.x && this.mleftdown.y <= cPoint.y && cPoint.y <= this.mrightup.y;
    }

    public boolean covers(Extent extent) {
        return getMinX() <= extent.getMinX() && getMinY() <= extent.getMinY() && getMaxX() >= extent.getMaxX() && getMaxY() >= extent.getMaxY();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Extent) {
            Extent extent = (Extent) obj;
            return extent.getLeftdown().equals(getLeftdown()) && extent.getRightup().equals(getRightup());
        } else if (obj instanceof Ring) {
            return toRing().equals((Ring) obj);
        } else {
            return false;
        }
    }

    public double getArea() {
        return getWidth() * getHeight();
    }

    public Extent[] getDivisionOfSelf(Extent extent) {
        return null;
    }

    public Extent getExtent() {
        return this;
    }

    public double getHeight() {
        return Math.abs(this.mrightup.y - this.mleftdown.y);
    }

    public Extent getIntersection(Extent extent) {
        if (intersected(extent)) {
            return null;
        }
        if (!covers(extent)) {
            extent = extent.covers(this) ? this : null;
        }
        return extent;
    }

    public CPoint getLeftUp() {
        return new CPoint(this.mleftdown.x, this.mrightup.y);
    }

    public CPoint getLeftdown() {
        return this.mleftdown;
    }

    public Extent getMaxExtent(Extent extent) {
        return extent == null ? this : new Extent(Math.min(getMinX(), extent.getMinX()), Math.min(getMinY(), extent.getMinY()), Math.max(getMaxX(), extent.getMaxX()), Math.max(getMaxY(), extent.getMaxY()));
    }

    public double getMaxX() {
        return this.mrightup.x;
    }

    public double getMaxY() {
        return this.mrightup.y;
    }

    public double getMinX() {
        return this.mleftdown.x;
    }

    public double getMinY() {
        return this.mleftdown.y;
    }

    public CPoint getRightDown() {
        return new CPoint(this.mrightup.x, this.mleftdown.y);
    }

    public CPoint getRightup() {
        return this.mrightup;
    }

    public double getWidth() {
        return Math.abs(this.mrightup.x - this.mleftdown.x);
    }

    public boolean hitTest(double d, double d2, double d3) {
        return contains(d, d2);
    }

    public boolean hitTest(CPoint cPoint, double d) {
        return hitTest(cPoint.x, cPoint.y, d);
    }

    public boolean intersected(Extent extent) {
        return !((getMaxX() > extent.getMinX() ? 1 : (getMaxX() == extent.getMinX() ? 0 : -1)) < 0 || (getMinX() > extent.getMaxX() ? 1 : (getMinX() == extent.getMaxX() ? 0 : -1)) > 0 || (getMaxY() > extent.getMinY() ? 1 : (getMaxY() == extent.getMinY() ? 0 : -1)) < 0 || (getMinY() > extent.getMaxY() ? 1 : (getMinY() == extent.getMaxY() ? 0 : -1)) > 0) || covers(extent) || extent.covers(this);
    }

    public void setLeftdown(CPoint cPoint) {
        this.mleftdown = cPoint;
    }

    public void setRightup(CPoint cPoint) {
        this.mrightup = cPoint;
    }

    public Ring toRing() {
        CPoint leftdown = getLeftdown();
        CPoint rightup = getRightup();
        CPoint cPoint = new CPoint(rightup.x, leftdown.y);
        CPoint cPoint2 = new CPoint(leftdown.x, rightup.y);
        Ring ring = new Ring();
        ring.add(leftdown);
        ring.add(cPoint);
        ring.add(rightup);
        ring.add(cPoint2);
        return ring;
    }

    public String toString() {
        return "Extent :(" + this.mleftdown.x + "," + this.mleftdown.y + "," + this.mrightup.x + "," + this.mrightup.y + ")";
    }

    public Extent translate(double d, double d2) {
        return new Extent(new CPoint(getLeftdown().x - d, getLeftdown().y - d2), new CPoint(getRightup().x - d, getRightup().y - d2));
    }
}
