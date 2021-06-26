package com.syu.geometry;

import java.io.IOException;

public class LittleEndianReader {
    public static void main(String[] strArr) {
    }

    public static double readDouble(byte[] bArr) throws IOException {
        return Double.longBitsToDouble(((long) (bArr[7] << 56)) | ((long) ((bArr[6] & 255) << 48)) | ((long) ((bArr[5] & 255) << 40)) | ((long) ((bArr[4] & 255) << 32)) | ((long) ((bArr[3] & 255) << 24)) | ((long) ((bArr[2] & 255) << 16)) | ((long) ((bArr[1] & 255) << 8)) | ((long) (bArr[0] & 255)));
    }

    public static int readInt(byte[] bArr) throws IOException {
        return (bArr[3] << 24) | ((bArr[2] & 255) << 16) | ((bArr[1] & 255) << 8) | (bArr[0] & 255);
    }
}
