package com.syu.geometry;

import android.content.res.AssetManager;
import java.io.DataInputStream;
import java.io.InputStream;

public class GetCity {
    public static AssetManager asset;
    public static boolean bInitOver = false;

    private static int[] boxIndex(double d, double d2) {
        if (ToolData.box == null) {
            return null;
        }
        int[] iArr = new int[ToolData.box.length];
        int i = 0;
        for (int i2 = 0; i2 < ToolData.box.length; i2++) {
            if (d >= ToolData.box[i2].getMinx() && d <= ToolData.box[i2].getMaxx() && d2 >= ToolData.box[i2].getMiny() && d2 <= ToolData.box[i2].getMaxy()) {
                iArr[i] = i2;
                i++;
            }
        }
        int[] iArr2 = new int[i];
        System.arraycopy(iArr, 0, iArr2, 0, i);
        return iArr2;
    }

    public static String getCity(double d, double d2) throws Exception {
        if (!bInitOver) {
            return null;
        }
        if (ToolData.box == null) {
            return null;
        }
        int[] boxIndex = boxIndex(d, d2);
        if (boxIndex != null) {
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 >= boxIndex.length) {
                    break;
                }
                InputStream open = asset.open("temp/cityregion.shp");
                DataInputStream dataInputStream = new DataInputStream(open);
                dataInputStream.close();
                open.close();
                if (((CPolygon) ShapeReader.readRecordXOffset(ToolData.offset[boxIndex[i2]], dataInputStream)).contains(d, d2)) {
                    return ToolData.city[boxIndex[i2]];
                }
                i = i2 + 1;
            }
        }
        return null;
    }

    public static void init(final AssetManager assetManager) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    GetCity.asset = assetManager;
                    InputStream open = GetCity.asset.open("temp/cityregion.shx");
                    DataInputStream dataInputStream = new DataInputStream(open);
                    VList readIndex = IndexReader.readIndex(dataInputStream);
                    ToolData.offset = new int[readIndex.size()];
                    ToolData.length = new int[ToolData.offset.length];
                    for (int i = 0; i < ToolData.offset.length; i++) {
                        int[] iArr = (int[]) readIndex.get(i);
                        ToolData.offset[i] = iArr[0];
                        ToolData.length[i] = iArr[1];
                    }
                    dataInputStream.close();
                    open.close();
                    ToolData.box = new CBox[ToolData.offset.length];
                    for (int i2 = 0; i2 < ToolData.offset.length; i2++) {
                        InputStream open2 = GetCity.asset.open("temp/cityregion.shp");
                        DataInputStream dataInputStream2 = new DataInputStream(open2);
                        dataInputStream2.skip((long) ((ToolData.offset[i2] * 2) + 12));
                        ToolData.box[i2] = ShapeReader.readBox(dataInputStream2);
                        dataInputStream2.close();
                        open2.close();
                    }
                    GetCity.bInitOver = true;
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public static void release() {
        ToolData.offset = null;
        ToolData.city = null;
        ToolData.box = null;
    }
}
