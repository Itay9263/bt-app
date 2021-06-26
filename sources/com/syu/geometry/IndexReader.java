package com.syu.geometry;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class IndexReader {
    public static int[] getIndex(int i, DataInputStream dataInputStream) throws IOException {
        return (int[]) readIndex(dataInputStream).get(i);
    }

    public static void main(String[] strArr) throws Exception {
        VList readIndex = readIndex(new DataInputStream(new FileInputStream("F:/Temp2016/l2c/cityregion.shx")));
        for (int i = 0; i < readIndex.size(); i++) {
            int[] iArr = (int[]) readIndex.get(i);
            System.out.println("offset = " + iArr[0] + " length = " + iArr[1]);
        }
    }

    public static VList readIndex(DataInputStream dataInputStream) throws IOException {
        VArrayList vArrayList = new VArrayList();
        while (dataInputStream.available() > 0) {
            vArrayList.add(new int[]{dataInputStream.readInt(), dataInputStream.readInt()});
        }
        return vArrayList;
    }
}
