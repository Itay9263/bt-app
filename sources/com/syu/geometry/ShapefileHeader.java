package com.syu.geometry;

public class ShapefileHeader {
    private CBox box = null;
    public int fileCode = 0;
    public int fileLength = 0;
    public int shpType = 0;
    public int version = 0;

    public CBox getBox() {
        return this.box;
    }

    public int getFileCode() {
        return this.fileCode;
    }

    public int getFileLength() {
        return this.fileLength;
    }

    public int getShpType() {
        return this.shpType;
    }

    public int getVersion() {
        return this.version;
    }

    public void setBox(CBox cBox) {
        this.box = cBox;
    }

    public void setFileCode(int i) {
        this.fileCode = i;
    }

    public void setFileLength(int i) {
        this.fileLength = i;
    }

    public void setShpType(int i) {
        this.shpType = i;
    }

    public void setVersion(int i) {
        this.version = i;
    }
}
