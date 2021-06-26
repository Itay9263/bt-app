package com.syu.geometry;

public class CPolygon extends Ring {
    static int i = 0;
    private static final long serialVersionUID = 1;
    CBox box = null;
    int contentLength = 0;
    protected CCollection holes = new VectorCollection();
    int numParts;
    int numPoints;
    int[] partIndex;
    CPoint[] pts = null;
    int recordNum = 0;

    public CPolygon() {
    }

    public CPolygon(CBox cBox, int i2, int i3, int[] iArr, CPoint[] cPointArr) {
        this.box = cBox;
        this.numParts = i2;
        this.numPoints = i3;
        this.partIndex = iArr;
        this.pts = cPointArr;
    }

    public CPolygon(CCollection cCollection) {
        this.collection.addAll(cCollection);
    }

    public CPolygon(int[] iArr, int[] iArr2, int i2) {
        if (i2 < 0) {
            throw new NegativeArraySizeException();
        }
        i2 = i2 > iArr.length ? iArr.length : i2;
        i2 = i2 > iArr2.length ? iArr2.length : i2;
        for (int i3 = 0; i3 < i2; i3++) {
            this.collection.add(new CPoint((double) iArr[i3], (double) iArr2[i3]));
        }
    }

    public void addHole(Ring ring) {
        this.holes.add(ring);
    }

    public boolean contains(double d, double d2) {
        int i2 = 0;
        while (i2 < this.numParts) {
            VectorCollection vectorCollection = new VectorCollection(20, 10);
            int i3 = i2 == this.numParts + -1 ? this.numPoints : this.partIndex[i2 + 1];
            for (int i4 = this.partIndex[i2]; i4 < i3; i4++) {
                vectorCollection.add(this.pts[i4]);
            }
            if (new Ring(vectorCollection).contains(d, d2)) {
                CIterator it = this.holes.iterator();
                while (it.hasNext()) {
                    if (((Ring) it.next()).contains(d, d2)) {
                        return false;
                    }
                }
                return true;
            }
            i2++;
        }
        return false;
    }

    public boolean contains(CPoint cPoint) {
        return contains(cPoint.x, cPoint.y);
    }

    public boolean equals(Object obj) {
        if (obj instanceof CPolygon) {
            CPolygon cPolygon = (CPolygon) obj;
            return getPoints() == cPolygon.getPoints() && getHoles() == cPolygon.getHoles();
        } else if (!(obj instanceof Ring) || getHoles().size() != 0) {
            return false;
        } else {
            return getPoints() == ((Ring) obj).getPoints();
        }
    }

    public CBox getBox() {
        return this.box;
    }

    public int getContentLength() {
        return this.contentLength;
    }

    public CCollection getHoles() {
        return this.holes;
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

    public CPoint[] getPoints(int i2) {
        int i3 = i2 == this.partIndex.length + -1 ? this.numPoints : this.partIndex[i2 + 1];
        CPoint[] cPointArr = new CPoint[(i3 - this.partIndex[i2])];
        int i4 = 0;
        int i5 = this.partIndex[i2];
        while (i5 < i3) {
            cPointArr[i4] = this.pts[i5];
            i5++;
            i4++;
        }
        return cPointArr;
    }

    public CPoint[] getPts() {
        return this.pts;
    }

    public int getRecordNum() {
        return this.recordNum;
    }

    public void removeAllHoles() {
        this.holes.clear();
    }

    public void removeHole(Ring ring) {
        this.holes.remove(ring);
    }

    public void setBox(CBox cBox) {
        this.box = cBox;
    }

    public void setContentLength(int i2) {
        this.contentLength = i2;
    }

    public void setNumParts(int i2) {
        this.numParts = i2;
    }

    public void setNumPoints(int i2) {
        this.numPoints = i2;
    }

    public void setPartIndex(int[] iArr) {
        this.partIndex = iArr;
    }

    public void setPts(CPoint[] cPointArr) {
        this.pts = cPointArr;
    }

    public void setRecordNum(int i2) {
        this.recordNum = i2;
    }

    public String toString() {
        String str;
        CIterator it = this.collection.iterator();
        String str2 = "Polygon include " + this.collection.size() + " points:\n";
        while (true) {
            str = str2;
            if (!it.hasNext()) {
                break;
            }
            CPoint cPoint = (CPoint) it.next();
            str2 = String.valueOf(str) + "\tx=" + cPoint.getX() + "\ty=" + cPoint.getY() + "\n";
        }
        if (this.holes.size() <= 0) {
            return String.valueOf(str) + "\tinclude 0 holes:\n";
        }
        String str3 = String.valueOf(str) + "include " + this.holes.size() + " holes:\n";
        CIterator it2 = this.holes.iterator();
        int i2 = 1;
        while (true) {
            int i3 = i2;
            if (!it2.hasNext()) {
                return str3;
            }
            str3 = String.valueOf(str3) + "\tHoles " + i3 + "\n:";
            CIterator it3 = ((Ring) it2.next()).getPoints().iterator();
            while (it3.hasNext()) {
                CPoint cPoint2 = (CPoint) it3.next();
                str3 = String.valueOf(str3) + "x=" + cPoint2.getX() + "\ty=" + cPoint2.getY() + "\n";
            }
            i2 = i3 + 1;
        }
    }
}
