package com.syu.jni;

public class JniToolkit {
    static {
        try {
            System.loadLibrary("jni_toolkit");
        } catch (Throwable th) {
        }
    }

    public static native int openQst();

    public static native int setBrightness(int i);
}
