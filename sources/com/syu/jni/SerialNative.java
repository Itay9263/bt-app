package com.syu.jni;

public class SerialNative {
    static {
        try {
            System.loadLibrary("sqlserial");
        } catch (Throwable th) {
        }
    }

    public static native void native_close(int i);

    public static native int native_open(String str);

    public static native int native_open_mbx();

    public static native byte[] native_read(int i, int i2, int i3);

    public static native int native_setup(int i, int i2, int i3, int i4, int i5);

    public static native void native_write(int i, byte[] bArr, int i2);
}
