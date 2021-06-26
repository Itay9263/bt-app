package com.syu.jni;

public class JniI2c {
    static {
        try {
            System.loadLibrary("jni_i2c");
        } catch (Throwable th) {
        }
    }

    public static native int open(String str);

    public static native int readRk(int i, int i2, int i3);

    public static native int write(int i, int i2, int i3, int i4);

    public static native int writeRk(int i, int i2, int i3, int i4);
}
