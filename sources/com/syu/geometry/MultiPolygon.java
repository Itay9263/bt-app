package com.syu.geometry;

public class MultiPolygon extends MultiShape implements Area {
    private static final long serialVersionUID = 1;

    public MultiPolygon() {
    }

    public MultiPolygon(CCollection cCollection) {
        addAll(cCollection);
    }

    public boolean contains(double d, double d2) {
        return contains(new CPoint(d, d2));
    }

    public boolean contains(CPoint cPoint) {
        CIterator it = iterator();
        while (it.hasNext()) {
            if (((Area) it.next()).contains(cPoint)) {
                return true;
            }
        }
        return false;
    }

    public /* bridge */ /* synthetic */ Extent getExtent() {
        return super.getExtent();
    }

    public boolean hitTest(double d, double d2, double d3) {
        CIterator it = iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof MultiPolygon) {
                if (((MultiPolygon) next).hitTest(d, d2, d3)) {
                    return true;
                }
            } else if (next instanceof CPolygon) {
                if (((CPolygon) next).hitTest(d, d2, d3)) {
                    return true;
                }
            } else if ((next instanceof Ring) && ((Ring) next).hitTest(d, d2, d3)) {
                return true;
            }
        }
        return false;
    }

    public /* bridge */ /* synthetic */ boolean hitTest(CPoint cPoint, double d) {
        return super.hitTest(cPoint, d);
    }

    public boolean intersects(Extent extent) {
        CIterator it = iterator();
        while (it.hasNext()) {
            if (((Ring) it.next()).intersects(extent)) {
                return true;
            }
        }
        return false;
    }

    public /* bridge */ /* synthetic */ void setExtent(Extent extent) {
        super.setExtent(extent);
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
            if (cShape instanceof Ring) {
                i2++;
                stringBuffer2.append("\t" + cShape.toString() + "\n");
            } else if (cShape instanceof MultiPolygon) {
                i++;
                stringBuffer3.append("\t" + ((MultiPolygon) cShape).toString() + "\n");
            }
        }
        stringBuffer.append("MultiPolygon include " + i2 + " polygons ," + i + " MultiPolygon\n");
        stringBuffer.append(String.valueOf(stringBuffer2.toString()) + "\n");
        stringBuffer.append(String.valueOf(stringBuffer3.toString()) + "\n");
        return stringBuffer.toString();
    }

    public CShape unites(MultiPolygon multiPolygon) {
        if (multiPolygon == null) {
            return this;
        }
        int size = multiPolygon.size();
        Ring[] ringArr = new Ring[size];
        int i = 0;
        CShape cShape = this;
        while (i < size) {
            ringArr[i] = (Ring) multiPolygon.get(i);
            CShape unites = cShape instanceof Ring ? ((Ring) cShape).unites(ringArr[i]) : cShape instanceof MultiPolygon ? ((MultiPolygon) cShape).unites(ringArr[i]) : cShape;
            i++;
            cShape = unites;
        }
        return cShape;
    }

    public CShape unites(Ring ring) {
        Ring ring2;
        CShape cShape = null;
        if (ring == null) {
            return this;
        }
        int size = size();
        Ring[] ringArr = new Ring[size];
        int i = 0;
        Ring ring3 = ring;
        while (i < size - 1) {
            ringArr[i] = (Ring) get(i);
            CShape unites = ringArr[i].unites(ring3);
            if (unites instanceof Ring) {
                ring2 = (Ring) unites;
            } else {
                if (unites instanceof MultiPolygon) {
                    if (cShape == null) {
                        cShape = new MultiPolygon();
                    }
                    ((MultiPolygon) cShape).add(ringArr[i]);
                }
                ring2 = ring3;
            }
            i++;
            ring3 = ring2;
        }
        ringArr[size - 1] = (Ring) get(size - 1);
        CShape unites2 = ringArr[size - 1].unites(ring3);
        if (cShape == null) {
            cShape = unites2;
        } else if (unites2 instanceof Ring) {
            ((MultiPolygon) cShape).add((Ring) unites2);
        } else if (unites2 instanceof MultiPolygon) {
            ((MultiPolygon) cShape).add(ringArr[size - 1]);
            ((MultiPolygon) cShape).add(ring3);
        }
        return cShape;
    }

    public /* bridge */ /* synthetic */ void update() {
        super.update();
    }
}
