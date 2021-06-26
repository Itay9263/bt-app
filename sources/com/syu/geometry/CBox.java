package com.syu.geometry;

public class CBox {
    private double maxx;
    private double maxy;
    private double minx;
    private double miny;

    public CBox(double d, double d2, double d3, double d4) {
        this.minx = d;
        this.miny = d2;
        this.maxx = d3;
        this.maxy = d4;
    }

    public double getMaxx() {
        return this.maxx;
    }

    public double getMaxy() {
        return this.maxy;
    }

    public double getMinx() {
        return this.minx;
    }

    public double getMiny() {
        return this.miny;
    }

    public void setMaxx(double d) {
        this.maxx = d;
    }

    public void setMaxy(double d) {
        this.maxy = d;
    }

    public void setMinx(double d) {
        this.minx = d;
    }

    public void setMiny(double d) {
        this.miny = d;
    }

    public String toString() {
        return new StringBuffer().append("minx=").append(this.minx).append(",miny=").append(this.miny).append(",maxx=").append(this.maxx).append(",maxy=").append(this.maxy).toString();
    }
}
