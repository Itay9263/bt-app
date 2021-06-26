package com.syu.geometry;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class ShapeReader {
    public static void main(String[] strArr) throws Exception {
        VList readRecord = readRecord(new DataInputStream(new FileInputStream("F:/Temp2016/l2c/cityregion.shp")));
        System.out.println("polygon cnt = " + readRecord.size());
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < readRecord.size()) {
                CPolygon cPolygon = (CPolygon) readRecord.get(i2);
                System.out.println(cPolygon.getBox().toString());
                if (cPolygon.contains(113.23333d, 23.16667d)) {
                    System.out.println("i = " + i2);
                    return;
                }
                i = i2 + 1;
            } else {
                return;
            }
        }
    }

    public static void outputAllTypes(DataInputStream dataInputStream) throws IOException {
        boolean z = true;
        while (z) {
            int readInt = dataInputStream.readInt();
            dataInputStream.read(new byte[4], 0, 4);
            if (dataInputStream.available() <= (readInt * 2) - 4) {
                z = false;
            } else {
                dataInputStream.skip((long) ((readInt * 2) - 4));
            }
        }
    }

    public static CBox readBox(DataInputStream dataInputStream) throws IOException {
        byte[] bArr = new byte[8];
        dataInputStream.read(bArr, 0, 8);
        double readDouble = LittleEndianReader.readDouble(bArr);
        dataInputStream.read(bArr, 0, 8);
        double readDouble2 = LittleEndianReader.readDouble(bArr);
        dataInputStream.read(bArr, 0, 8);
        double readDouble3 = LittleEndianReader.readDouble(bArr);
        dataInputStream.read(bArr, 0, 8);
        return new CBox(readDouble, readDouble2, readDouble3, LittleEndianReader.readDouble(bArr));
    }

    public static MultiPoint readMultiPoint(DataInputStream dataInputStream) throws IOException {
        CBox readBox = readBox(dataInputStream);
        byte[] bArr = new byte[4];
        dataInputStream.read(bArr, 0, 4);
        int readInt = LittleEndianReader.readInt(bArr);
        CPoint[] cPointArr = new CPoint[readInt];
        for (int i = 0; i < readInt; i++) {
            cPointArr[i] = readPoint(dataInputStream);
        }
        return new MultiPoint(readBox, readInt, cPointArr);
    }

    public static CPoint readPoint(DataInputStream dataInputStream) throws IOException {
        byte[] bArr = new byte[8];
        dataInputStream.read(bArr, 0, 8);
        double readDouble = LittleEndianReader.readDouble(bArr);
        dataInputStream.read(bArr, 0, 8);
        return new CPoint(readDouble, LittleEndianReader.readDouble(bArr));
    }

    public static CPolyLine readPolyLine(DataInputStream dataInputStream) throws IOException {
        CBox readBox = readBox(dataInputStream);
        byte[] bArr = new byte[4];
        dataInputStream.read(bArr, 0, 4);
        int readInt = LittleEndianReader.readInt(bArr);
        dataInputStream.read(bArr, 0, 4);
        int readInt2 = LittleEndianReader.readInt(bArr);
        int[] iArr = new int[readInt];
        for (int i = 0; i < readInt; i++) {
            dataInputStream.read(bArr, 0, 4);
            iArr[i] = LittleEndianReader.readInt(bArr);
        }
        CPoint[] cPointArr = new CPoint[readInt2];
        for (int i2 = 0; i2 < readInt2; i2++) {
            cPointArr[i2] = readPoint(dataInputStream);
        }
        return new CPolyLine(readBox, readInt, readInt2, iArr, cPointArr);
    }

    public static CPolygon readPolygon(DataInputStream dataInputStream) throws IOException {
        CBox readBox = readBox(dataInputStream);
        byte[] bArr = new byte[4];
        dataInputStream.read(bArr, 0, 4);
        int readInt = LittleEndianReader.readInt(bArr);
        dataInputStream.read(bArr, 0, 4);
        int readInt2 = LittleEndianReader.readInt(bArr);
        int[] iArr = new int[readInt];
        for (int i = 0; i < readInt; i++) {
            dataInputStream.read(bArr, 0, 4);
            iArr[i] = LittleEndianReader.readInt(bArr);
        }
        CPoint[] cPointArr = new CPoint[readInt2];
        for (int i2 = 0; i2 < readInt2; i2++) {
            cPointArr[i2] = readPoint(dataInputStream);
        }
        return new CPolygon(readBox, readInt, readInt2, iArr, cPointArr);
    }

    public static VList readRecord(DataInputStream dataInputStream) throws IOException {
        VArrayList vArrayList = new VArrayList();
        while (dataInputStream.available() > 0) {
            vArrayList.add(readRecordXOffset(0, dataInputStream));
        }
        return vArrayList;
    }

    public static Object readRecordXOffset(int i, DataInputStream dataInputStream) throws IOException {
        dataInputStream.skip((long) (i * 2));
        byte[] bArr = new byte[4];
        dataInputStream.read(bArr, 0, 4);
        switch (LittleEndianReader.readInt(bArr)) {
            case 1:
                return readPoint(dataInputStream);
            case 3:
                return readPolyLine(dataInputStream);
            case 5:
                return readPolygon(dataInputStream);
            default:
                return null;
        }
    }

    public static ShapefileHeader readerHeader(DataInputStream dataInputStream) throws IOException {
        int readInt = dataInputStream.readInt();
        dataInputStream.skipBytes(20);
        int readInt2 = dataInputStream.readInt();
        byte[] bArr = new byte[4];
        dataInputStream.read(bArr, 0, 4);
        int readInt3 = LittleEndianReader.readInt(bArr);
        dataInputStream.read(bArr, 0, 4);
        int readInt4 = LittleEndianReader.readInt(bArr);
        byte[] bArr2 = new byte[8];
        dataInputStream.read(bArr2, 0, 8);
        double readDouble = LittleEndianReader.readDouble(bArr2);
        dataInputStream.read(bArr2, 0, 8);
        double readDouble2 = LittleEndianReader.readDouble(bArr2);
        dataInputStream.read(bArr2, 0, 8);
        double readDouble3 = LittleEndianReader.readDouble(bArr2);
        dataInputStream.read(bArr2, 0, 8);
        double readDouble4 = LittleEndianReader.readDouble(bArr2);
        ShapefileHeader shapefileHeader = new ShapefileHeader();
        shapefileHeader.setFileCode(readInt);
        shapefileHeader.setFileLength(readInt2);
        shapefileHeader.setVersion(readInt3);
        shapefileHeader.setShpType(readInt4);
        shapefileHeader.setBox(new CBox(readDouble, readDouble2, readDouble3, readDouble4));
        dataInputStream.skip(32);
        return shapefileHeader;
    }
}
