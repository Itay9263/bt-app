package com.syu.geometry;

public class MultiPoint extends MultiShape {
    private static final long serialVersionUID = 1;
    private CBox box = null;
    private int numPoints;
    private CPoint[] pts = null;

    public MultiPoint() {
    }

    public MultiPoint(CBox cBox, int i, CPoint[] cPointArr) {
        this.box = cBox;
        this.numPoints = i;
        this.pts = cPointArr;
    }

    public boolean equals(Object obj) {
        return false;
    }

    public CBox getBox() {
        return this.box;
    }

    public /* bridge */ /* synthetic */ Extent getExtent() {
        return super.getExtent();
    }

    public int getNumPoints() {
        return this.numPoints;
    }

    public CPoint[] getPts() {
        return this.pts;
    }

    public boolean hitTest(double d, double d2, double d3) {
        return false;
    }

    public /* bridge */ /* synthetic */ boolean hitTest(CPoint cPoint, double d) {
        return super.hitTest(cPoint, d);
    }

    public void setBox(CBox cBox) {
        this.box = cBox;
    }

    public /* bridge */ /* synthetic */ void setExtent(Extent extent) {
        super.setExtent(extent);
    }

    public void setNumPoints(int i) {
        this.numPoints = i;
    }

    public void setPts(CPoint[] cPointArr) {
        this.pts = cPointArr;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        StringBuffer stringBuffer3 = new StringBuffer();
        CIterator it = iterator();
        int i = 0;
        int i2 = 0;
        while (it.hasNext()) {
            CShape cShape = (CShape) it.next();
            if (cShape instanceof CPoint) {
                i2++;
                stringBuffer2.append("\t" + ((CPoint) cShape).toString() + "\n");
            } else if (cShape instanceof MultiPoint) {
                i++;
                stringBuffer3.append("\t" + ((MultiPoint) cShape).toString() + "\n");
            }
        }
        stringBuffer.append("MultiPoint include " + i2 + " points ," + i + " MultiPoint\n");
        stringBuffer.append(String.valueOf(stringBuffer2.toString()) + "\n");
        stringBuffer.append(String.valueOf(stringBuffer3.toString()) + "\n");
        return stringBuffer.toString();
    }

    public /* bridge */ /* synthetic */ void update() {
        super.update();
    }
}
