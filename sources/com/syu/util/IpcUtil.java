package com.syu.util;

import java.util.Locale;

public class IpcUtil {
    public static boolean fltsOk(float[] fArr, int i) {
        return fArr != null && fArr.length >= i;
    }

    public static int freqStr2Int(String str) {
        return (int) (Float.valueOf(str).floatValue() * 100.0f);
    }

    public static String int2freqStr(int i) {
        if (i < 20000) {
            return String.format(Locale.US, "%02d.%02d", new Object[]{Integer.valueOf(i / 100), Integer.valueOf(i % 100)});
        }
        return String.format(Locale.US, "%d", new Object[]{Integer.valueOf(i / 100)});
    }

    public static boolean intsOk(int[] iArr, int i) {
        return iArr != null && iArr.length >= i;
    }

    public static boolean strsOk(Object[] objArr, int i) {
        return objArr != null && objArr.length >= i;
    }
}
