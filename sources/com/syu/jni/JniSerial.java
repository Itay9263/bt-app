package com.syu.jni;

public class JniSerial {
    static {
        try {
            System.loadLibrary("jni_serial");
        } catch (Throwable th) {
        }
    }

    public static native void close(int i);

    public static native int open(String str);

    public static native byte[] read(int i, int i2, int i3);

    public static native int setup(int i, int i2, int i3, int i4, int i5);

    public static native void write(int i, byte[] bArr, int i2, int i3);
}
