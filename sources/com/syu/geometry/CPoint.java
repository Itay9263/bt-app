package com.syu.geometry;

public class CPoint implements CShape {
    private static final long serialVersionUID = 1;
    public double x;
    public double y;

    public CPoint(double d, double d2) {
        this.x = d;
        this.y = d2;
    }

    public CPoint(CPoint cPoint) {
        this.x = cPoint.x;
        this.y = cPoint.y;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CPoint)) {
            return false;
        }
        CPoint cPoint = (CPoint) obj;
        return this.x == cPoint.x && this.y == cPoint.y;
    }

    public double getDistance(CPoint cPoint) {
        double d = this.x - cPoint.x;
        double d2 = this.y - cPoint.y;
        return Math.sqrt((d * d) + (d2 * d2));
    }

    public Extent getExtent() {
        return new Extent(this, this);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public boolean hitTest(double d, double d2, double d3) {
        return hitTest(new CPoint(d, d2), d3);
    }

    public boolean hitTest(CPoint cPoint, double d) {
        return getDistance(cPoint) < d;
    }

    public void setX(double d) {
        this.x = d;
    }

    public void setY(double d) {
        this.y = d;
    }

    public String toString() {
        return "point(" + getX() + "," + getY() + ")";
    }
}
