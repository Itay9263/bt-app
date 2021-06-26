package com.syu.geometry;

public interface Area extends CShape {
    boolean contains(double d, double d2);

    boolean contains(CPoint cPoint);
}
