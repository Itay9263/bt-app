package com.syu.geometry;

import java.io.Serializable;

public interface CShape extends Serializable {
    boolean equals(Object obj);

    Extent getExtent();

    boolean hitTest(double d, double d2, double d3);

    boolean hitTest(CPoint cPoint, double d);
}
