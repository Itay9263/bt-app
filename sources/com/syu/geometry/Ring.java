package com.syu.geometry;

import java.util.Vector;

public class Ring extends AbstractShape implements Area {
    private static final long serialVersionUID = 1;

    public Ring() {
    }

    public Ring(CCollection cCollection) {
        this.collection.addAll(cCollection);
    }

    public Ring(int[] iArr, int[] iArr2, int i) {
        if (i < 0) {
            throw new NegativeArraySizeException();
        }
        i = i > iArr.length ? iArr.length : i;
        i = i > iArr2.length ? iArr2.length : i;
        for (int i2 = 0; i2 < i; i2++) {
            this.collection.add(new CPoint((double) iArr[i2], (double) iArr2[i2]));
        }
    }

    private void addPoint(Vector<CPoint> vector, CPoint[] cPointArr, CPoint[] cPointArr2, int i, CPoint cPoint) {
        int i2 = 0;
        while (i2 < cPointArr.length) {
            if (i >= cPointArr.length) {
                i = (i - cPointArr.length) + 1;
            }
            if (cPointArr[i].equals(cPoint)) {
                vector.addElement(cPointArr[i]);
                return;
            }
            int isInsert = isInsert(cPointArr[i], cPointArr2);
            if (isInsert == -1) {
                vector.addElement(cPointArr[i]);
                i++;
                i2++;
            } else {
                vector.addElement(cPointArr[i]);
                addPoint(vector, cPointArr2, cPointArr, isInsert + 1, cPoint);
                return;
            }
        }
    }

    private int direction(CPoint[] cPointArr, CPoint[] cPointArr2) {
        int i = -2;
        int length = cPointArr.length;
        int length2 = cPointArr2.length;
        boolean z = false;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = 0;
            while (true) {
                if (i3 >= length2) {
                    break;
                }
                if (cPointArr[i2].equals(cPointArr2[i3])) {
                    if (i2 == 0) {
                        if (i3 == 0) {
                            if (cPointArr[i2 + 1].equals(cPointArr2[i3 + 1])) {
                                z = true;
                                i = -1;
                                break;
                            } else if (cPointArr[length - 1].equals(cPointArr2[i3 + 1]) || cPointArr[i2 + 1].equals(cPointArr2[length2 - 1])) {
                                z = true;
                                i = 1;
                            } else {
                                i = 0;
                            }
                        } else if (i3 == length2 - 1) {
                            if (cPointArr[i2 + 1].equals(cPointArr2[0])) {
                                z = true;
                                i = -1;
                                break;
                            } else if (cPointArr[length - 1].equals(cPointArr2[0]) || cPointArr[i2 + 1].equals(cPointArr2[i3 - 1])) {
                                z = true;
                                i = 1;
                            } else {
                                i = 0;
                            }
                        } else if (cPointArr[i2 + 1].equals(cPointArr2[i3 + 1])) {
                            z = true;
                            i = -1;
                            break;
                        } else if (cPointArr[length - 1].equals(cPointArr2[i3 + 1]) || cPointArr[i2 + 1].equals(cPointArr2[i3 - 1])) {
                            z = true;
                            i = 1;
                        } else {
                            i = 0;
                        }
                    } else if (i2 == length - 1) {
                        if (i3 == 0) {
                            if (cPointArr[0].equals(cPointArr2[i3 + 1])) {
                                z = true;
                                i = -1;
                                break;
                            } else if (cPointArr[i2 - 1].equals(cPointArr2[i3 + 1]) || cPointArr[0].equals(cPointArr2[length2 - 1])) {
                                z = true;
                                i = 1;
                            } else {
                                i = 0;
                            }
                        } else if (i3 == length2 - 1) {
                            if (cPointArr[0].equals(cPointArr2[0])) {
                                z = true;
                                i = -1;
                                break;
                            } else if (cPointArr[i2 - 1].equals(cPointArr2[0]) || cPointArr[0].equals(cPointArr2[i3 - 1])) {
                                z = true;
                                i = 1;
                            } else {
                                i = 0;
                            }
                        } else if (cPointArr[0].equals(cPointArr2[i3 + 1])) {
                            z = true;
                            i = -1;
                            break;
                        } else if (cPointArr[i2 - 1].equals(cPointArr2[i3 + 1]) || cPointArr[0].equals(cPointArr2[i3 - 1])) {
                            z = true;
                            i = 1;
                        } else {
                            i = 0;
                        }
                    } else if (i3 == 0) {
                        if (cPointArr[i2 + 1].equals(cPointArr2[i3 + 1])) {
                            z = true;
                            i = -1;
                            break;
                        } else if (cPointArr[i2 - 1].equals(cPointArr2[i3 + 1]) || cPointArr[i2 + 1].equals(cPointArr2[length2 - 1])) {
                            z = true;
                            i = 1;
                        } else {
                            i = 0;
                        }
                    } else if (i3 == length2 - 1) {
                        if (cPointArr[i2 + 1].equals(cPointArr2[0])) {
                            z = true;
                            i = -1;
                            break;
                        } else if (cPointArr[i2 - 1].equals(cPointArr2[0]) || cPointArr[i2 + 1].equals(cPointArr2[i3 - 1])) {
                            z = true;
                            i = 1;
                        } else {
                            i = 0;
                        }
                    } else if (cPointArr[i2 + 1].equals(cPointArr2[i3 + 1])) {
                        z = true;
                        i = -1;
                        break;
                    } else if (cPointArr[i2 - 1].equals(cPointArr2[i3 + 1]) || cPointArr[i2 + 1].equals(cPointArr2[i3 - 1])) {
                        z = true;
                        i = 1;
                    } else {
                        i = 0;
                    }
                }
                i3++;
            }
            if (z) {
                break;
            }
        }
        return i;
    }

    private Crossings getCrossings(double d, double d2, double d3, double d4) {
        Crossings crossings = new Crossings(d, d2, d3, d4);
        int size = getPoints().size();
        Object[] array = getPoints().toArray();
        double x = ((CPoint) array[size - 1]).getX();
        double y = ((CPoint) array[size - 1]).getY();
        int i = 0;
        while (i < size) {
            double x2 = ((CPoint) array[i]).getX();
            double y2 = ((CPoint) array[i]).getY();
            if (crossings.accumulateLine(x, y, x2, y2)) {
                return null;
            }
            i++;
            y = y2;
            x = x2;
        }
        return crossings;
    }

    private int isInsert(CPoint cPoint, CPoint[] cPointArr) {
        int i = -1;
        for (int i2 = 0; i2 < cPointArr.length; i2++) {
            if (cPoint.equals(cPointArr[i2])) {
                i = i2;
            }
        }
        return i;
    }

    public boolean contains(double d, double d2) {
        double d3;
        double d4;
        double d5;
        int size = size();
        if (size <= 2 || !super.getExtent().contains(new CPoint(d, d2))) {
            return false;
        }
        Object[] array = toArray();
        double[] dArr = new double[size];
        double[] dArr2 = new double[size];
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= size) {
                break;
            }
            CPoint cPoint = (CPoint) array[i2];
            dArr[i2] = cPoint.x;
            dArr2[i2] = cPoint.y;
            i = i2 + 1;
        }
        int i3 = 0;
        double d6 = dArr[size - 1];
        int i4 = 0;
        double d7 = dArr2[size - 1];
        while (i4 < size) {
            double d8 = dArr[i4];
            double d9 = dArr2[i4];
            if (d9 != d7) {
                if (d8 < d6) {
                    if (d < d6) {
                        d3 = d8;
                    }
                } else if (d < d8) {
                    d3 = d6;
                }
                if (d9 < d7) {
                    if (d2 >= d9 && d2 < d7) {
                        if (d < d3) {
                            i3++;
                        } else {
                            d4 = d - d8;
                            d5 = d2 - d9;
                        }
                    }
                } else if (d2 >= d7 && d2 < d9) {
                    if (d < d3) {
                        i3++;
                    } else {
                        d4 = d - d6;
                        d5 = d2 - d7;
                    }
                }
                if (d4 < (d5 / (d7 - d9)) * (d6 - d8)) {
                    i3++;
                }
            }
            i4++;
            d7 = d9;
            d6 = d8;
        }
        return (i3 & 1) != 0;
    }

    public boolean contains(CPoint cPoint) {
        return contains(cPoint.x, cPoint.y);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Ring) {
            return ((Ring) obj).getPoints().equals(getPoints());
        }
        return false;
    }

    public double getAreaOfRing() {
        double d = 0.0d;
        int i = 0;
        CPoint cPoint = (CPoint) getPoints().iterator().next();
        CPoint cPoint2 = (CPoint) getPoints().iterator().next();
        while (i < getPoints().size() - 1) {
            d += ((cPoint2.getX() - cPoint.getX()) * (cPoint2.getY() + cPoint.getY())) / 2.0d;
            i++;
            cPoint2 = cPoint;
            cPoint = (CPoint) getPoints().iterator().next();
        }
        return d;
    }

    public boolean hitTest(double d, double d2, double d3) {
        return contains(d, d2);
    }

    public boolean intersects(double d, double d2, double d3, double d4) {
        Crossings crossings = getCrossings(d, d2, d3, d4);
        return crossings == null || !crossings.isEmpty();
    }

    public boolean intersects(Extent extent) {
        if (!super.getExtent().intersected(extent)) {
            return false;
        }
        double minX = extent.getMinX();
        double minY = extent.getMinY();
        double maxX = extent.getMaxX();
        double maxY = extent.getMaxY();
        if (minX == maxX && minY == maxY && contains(new CPoint(minX, minY))) {
            return true;
        }
        return intersects(minX, minY, maxX, maxY);
    }

    public String toString() {
        CIterator it = this.collection.iterator();
        String str = "Ring include " + this.collection.size() + " points:\n";
        while (true) {
            String str2 = str;
            if (!it.hasNext()) {
                return str2;
            }
            CPoint cPoint = (CPoint) it.next();
            str = String.valueOf(str2) + "x=" + cPoint.getX() + "\ty=" + cPoint.getY() + "\n";
        }
    }

    public CShape unites(Ring ring) {
        Ring ring2;
        CPoint[] cPointArr;
        int i;
        if (ring == null) {
            return this;
        }
        Object[] array = toArray();
        CPoint[] cPointArr2 = new CPoint[array.length];
        for (int i2 = 0; i2 < array.length; i2++) {
            cPointArr2[i2] = (CPoint) array[i2];
        }
        Object[] array2 = ring.toArray();
        CPoint[] cPointArr3 = new CPoint[array2.length];
        for (int i3 = 0; i3 < array2.length; i3++) {
            cPointArr3[i3] = (CPoint) array2[i3];
        }
        int length = cPointArr3.length;
        int direction = direction(cPointArr2, cPointArr3);
        if (direction == -2 || direction == 0) {
            MultiPolygon multiPolygon = new MultiPolygon();
            multiPolygon.add(this);
            multiPolygon.add(ring);
            ring2 = multiPolygon;
        } else {
            if (direction == -1) {
                cPointArr = new CPoint[length];
                for (int i4 = 0; i4 < length; i4++) {
                    cPointArr[i4] = cPointArr3[length - (i4 + 1)];
                }
            } else {
                cPointArr = cPointArr3;
            }
            ring2 = new Ring();
            CPoint cPoint = null;
            Vector vector = new Vector(10, 10);
            int i5 = 0;
            while (true) {
                if (i5 >= cPointArr2.length) {
                    i = -1;
                    break;
                } else if (isInsert(cPointArr2[i5], cPointArr) == -1) {
                    vector.addElement(cPointArr2[i5]);
                    cPoint = cPointArr2[i5];
                    i = i5 + 1;
                    break;
                } else {
                    i5++;
                }
            }
            addPoint(vector, cPointArr2, cPointArr, i, cPoint);
            for (int i6 = 0; i6 < vector.size(); i6++) {
                ring2.add((CPoint) vector.elementAt(i6));
            }
        }
        return ring2;
    }
}
