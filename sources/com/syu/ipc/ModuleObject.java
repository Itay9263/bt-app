package com.syu.ipc;

public class ModuleObject {
    public float[] flts;
    public int[] ints;
    public String[] strs;

    public ModuleObject() {
    }

    public ModuleObject(int i) {
        this.ints = new int[]{i};
    }

    public ModuleObject(int i, String str) {
        this.ints = new int[]{i};
        this.strs = new String[]{str};
    }

    public ModuleObject(String str) {
        this.strs = new String[]{str};
    }

    public ModuleObject(int[] iArr) {
        this.ints = iArr;
    }
}
