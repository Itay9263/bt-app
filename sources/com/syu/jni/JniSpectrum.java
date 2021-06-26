package com.syu.jni;

public class JniSpectrum {
    static {
        try {
            System.loadLibrary("jni_spectrum");
        } catch (Throwable th) {
        }
    }

    public static native int open(String str);

    public static native int read(int i);
}
