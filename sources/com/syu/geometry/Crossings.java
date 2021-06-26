package com.syu.geometry;

public final class Crossings {
    public static final boolean debug = false;
    int limit;
    double xhi;
    double xlo;
    double yhi;
    double ylo;
    double[] yranges;

    public Crossings(double d, double d2, double d3, double d4) {
        this.limit = 0;
        this.yranges = null;
        this.xlo = 0.0d;
        this.ylo = 0.0d;
        this.xhi = 0.0d;
        this.yhi = 0.0d;
        this.limit = 0;
        this.yranges = new double[10];
        this.xlo = d;
        this.ylo = d2;
        this.xhi = d3;
        this.yhi = d4;
    }

    public boolean accumulateLine(double d, double d2, double d3, double d4) {
        return d2 <= d4 ? accumulateLine(d, d2, d3, d4, 1) : accumulateLine(d3, d4, d, d2, -1);
    }

    public boolean accumulateLine(double d, double d2, double d3, double d4, int i) {
        double d5;
        double d6;
        double d7;
        if (this.yhi <= d2 || this.ylo >= d4) {
            return false;
        }
        if (d >= this.xhi && d3 >= this.xhi) {
            return false;
        }
        if (d2 == d4) {
            return d >= this.xlo || d3 >= this.xlo;
        }
        double d8 = d3 - d;
        double d9 = d4 - d2;
        if (d2 < this.ylo) {
            d6 = (((this.ylo - d2) * d8) / d9) + d;
            d5 = this.ylo;
        } else {
            d5 = d2;
            d6 = d;
        }
        if (this.yhi < d4) {
            d3 = d + ((d8 * (this.yhi - d2)) / d9);
            d7 = this.yhi;
        } else {
            d7 = d4;
        }
        if (d6 >= this.xhi && d3 >= this.xhi) {
            return false;
        }
        if (d6 > this.xlo || d3 > this.xlo) {
            return true;
        }
        record(d5, d7, i);
        return false;
    }

    public final double getXHi() {
        return this.xhi;
    }

    public final double getXLo() {
        return this.xlo;
    }

    public final double getYHi() {
        return this.yhi;
    }

    public final double getYLo() {
        return this.ylo;
    }

    public final boolean isEmpty() {
        return this.limit == 0;
    }

    public void record(double d, double d2, int i) {
        int i2;
        double d3;
        double d4;
        double d5;
        double d6;
        double d7;
        if (d < d2) {
            int i3 = 0;
            while (i3 < this.limit && d > this.yranges[i3 + 1]) {
                i3 += 2;
            }
            int i4 = i3;
            int i5 = i3;
            while (true) {
                if (i5 < this.limit) {
                    int i6 = i5 + 1;
                    double d8 = this.yranges[i5];
                    i5 = i6 + 1;
                    double d9 = this.yranges[i6];
                    if (d2 >= d8) {
                        if (d < d8) {
                            d3 = d8;
                            d4 = d;
                        } else {
                            d3 = d;
                            d4 = d8;
                        }
                        if (d2 < d9) {
                            d5 = d9;
                            d6 = d2;
                        } else {
                            d5 = d2;
                            d6 = d9;
                        }
                        if (d3 == d6) {
                            i2 = i4;
                            d2 = d5;
                            d = d4;
                        } else {
                            if (d3 > d6) {
                                d = d3;
                                d7 = d6;
                            } else {
                                d = d6;
                                d7 = d3;
                            }
                            if (d4 != d7) {
                                int i7 = i4 + 1;
                                this.yranges[i4] = d4;
                                i4 = i7 + 1;
                                this.yranges[i7] = d7;
                            }
                            i2 = i4;
                            d2 = d5;
                        }
                        if (d >= d2) {
                            break;
                        }
                        i4 = i2;
                    } else {
                        int i8 = i4 + 1;
                        this.yranges[i4] = d;
                        this.yranges[i8] = d2;
                        i4 = i8 + 1;
                        d2 = d9;
                        d = d8;
                    }
                } else {
                    i2 = i4;
                    break;
                }
            }
            if (i2 < i5 && i5 < this.limit) {
                System.arraycopy(this.yranges, i5, this.yranges, i2, this.limit - i5);
            }
            int i9 = i2 + (this.limit - i5);
            if (d < d2) {
                if (i9 >= this.yranges.length) {
                    double[] dArr = new double[(i9 + 10)];
                    System.arraycopy(this.yranges, 0, dArr, 0, i9);
                    this.yranges = dArr;
                }
                int i10 = i9 + 1;
                this.yranges[i9] = d;
                i9 = i10 + 1;
                this.yranges[i10] = d2;
            }
            this.limit = i9;
        }
    }
}
