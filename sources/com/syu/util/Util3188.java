package com.syu.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util3188 {
    /* JADX WARNING: Removed duplicated region for block: B:18:0x002b A[SYNTHETIC, Splitter:B:18:0x002b] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0035 A[SYNTHETIC, Splitter:B:24:0x0035] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getBackSignal() {
        /*
            r0 = 0
            java.io.File r1 = new java.io.File
            java.lang.String r2 = "dev/vehicle"
            r1.<init>(r2)
            r3 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0024, all -> 0x0031 }
            r2.<init>(r1)     // Catch:{ IOException -> 0x0024, all -> 0x0031 }
            if (r2 == 0) goto L_0x0039
            r1 = 8
            byte[] r1 = new byte[r1]     // Catch:{ IOException -> 0x0047 }
            int r3 = r2.read(r1)     // Catch:{ IOException -> 0x0047 }
            r4 = 1
            if (r3 <= r4) goto L_0x0039
            r3 = 0
            byte r0 = r1[r3]     // Catch:{ IOException -> 0x0047 }
            if (r2 == 0) goto L_0x0023
            r2.close()     // Catch:{ Exception -> 0x0041 }
        L_0x0023:
            return r0
        L_0x0024:
            r1 = move-exception
            r2 = r3
        L_0x0026:
            r1.printStackTrace()     // Catch:{ all -> 0x0045 }
            if (r2 == 0) goto L_0x0023
            r2.close()     // Catch:{ Exception -> 0x002f }
            goto L_0x0023
        L_0x002f:
            r1 = move-exception
            goto L_0x0023
        L_0x0031:
            r0 = move-exception
            r2 = r3
        L_0x0033:
            if (r2 == 0) goto L_0x0038
            r2.close()     // Catch:{ Exception -> 0x0043 }
        L_0x0038:
            throw r0
        L_0x0039:
            if (r2 == 0) goto L_0x0023
            r2.close()     // Catch:{ Exception -> 0x003f }
            goto L_0x0023
        L_0x003f:
            r1 = move-exception
            goto L_0x0023
        L_0x0041:
            r1 = move-exception
            goto L_0x0023
        L_0x0043:
            r1 = move-exception
            goto L_0x0038
        L_0x0045:
            r0 = move-exception
            goto L_0x0033
        L_0x0047:
            r1 = move-exception
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.Util3188.getBackSignal():int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x002a A[SYNTHETIC, Splitter:B:17:0x002a] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0033 A[SYNTHETIC, Splitter:B:23:0x0033] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getBspSignal() {
        /*
            r3 = 1
            java.io.File r0 = new java.io.File
            java.lang.String r1 = "dev/vehicle"
            r0.<init>(r1)
            r2 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0023, all -> 0x002f }
            r1.<init>(r0)     // Catch:{ IOException -> 0x0023, all -> 0x002f }
            if (r1 == 0) goto L_0x0037
            r0 = 8
            byte[] r0 = new byte[r0]     // Catch:{ IOException -> 0x0047 }
            int r2 = r1.read(r0)     // Catch:{ IOException -> 0x0047 }
            if (r2 <= r3) goto L_0x0037
            r2 = 1
            byte r0 = r0[r2]     // Catch:{ IOException -> 0x0047 }
            if (r1 == 0) goto L_0x0022
            r1.close()     // Catch:{ Exception -> 0x003f }
        L_0x0022:
            return r0
        L_0x0023:
            r0 = move-exception
            r1 = r2
        L_0x0025:
            r0.printStackTrace()     // Catch:{ all -> 0x0045 }
            if (r1 == 0) goto L_0x002d
            r1.close()     // Catch:{ Exception -> 0x0041 }
        L_0x002d:
            r0 = 0
            goto L_0x0022
        L_0x002f:
            r0 = move-exception
            r1 = r2
        L_0x0031:
            if (r1 == 0) goto L_0x0036
            r1.close()     // Catch:{ Exception -> 0x0043 }
        L_0x0036:
            throw r0
        L_0x0037:
            if (r1 == 0) goto L_0x002d
            r1.close()     // Catch:{ Exception -> 0x003d }
            goto L_0x002d
        L_0x003d:
            r0 = move-exception
            goto L_0x002d
        L_0x003f:
            r1 = move-exception
            goto L_0x0022
        L_0x0041:
            r0 = move-exception
            goto L_0x002d
        L_0x0043:
            r1 = move-exception
            goto L_0x0036
        L_0x0045:
            r0 = move-exception
            goto L_0x0031
        L_0x0047:
            r0 = move-exception
            goto L_0x0025
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.Util3188.getBspSignal():int");
    }

    public static void setVehicle(int i) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("dev/vehicle"));
            if (fileOutputStream != null) {
                fileOutputStream.write(new byte[]{(byte) i});
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
