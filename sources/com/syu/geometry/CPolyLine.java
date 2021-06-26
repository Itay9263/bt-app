package com.syu.geometry;

public class CPolyLine {
    CBox box = null;
    int numParts;
    int numPoints;
    int[] partIndex;
    CPoint[] pts = null;

    public CPolyLine(CBox cBox, int i, int i2, int[] iArr, CPoint[] cPointArr) {
        this.box = cBox;
        this.numParts = i;
        this.numPoints = i2;
        this.partIndex = iArr;
        this.pts = cPointArr;
    }

    public CBox getBox() {
        return this.box;
    }

    public int getNumParts() {
        return this.numParts;
    }

    public int getNumPoints() {
        return this.numPoints;
    }

    public int[] getPartIndex() {
        return this.partIndex;
    }

    public CPoint[] getPts() {
        return this.pts;
    }
}
