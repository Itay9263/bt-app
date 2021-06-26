package defpackage;

import android.os.Handler;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.data.FinalChip;

/* renamed from: bu  reason: default package */
public class bu {
    private static bu e;
    private String a = "bttest";
    private String b = "BTAddr.txt";
    private String[] c = {"/mnt/external_sd0/", "/mnt/external_sd1/", "/mnt/usb_storage1/", "/mnt/usb_storage1/", "/mnt/usb_storage2/", "/mnt/usb_storage3/", "/mnt/usb_storage4/"};
    /* access modifiers changed from: private */
    public String d = FinalChip.BSP_PLATFORM_Null;

    public static bu a() {
        if (e == null) {
            e = new bu();
        }
        return e;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0058 A[SYNTHETIC, Splitter:B:22:0x0058] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0065 A[SYNTHETIC, Splitter:B:29:0x0065] */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean c() {
        /*
            r9 = this;
            r3 = 0
            r0 = 1
            r1 = 0
            java.lang.String[] r5 = r9.c
            int r6 = r5.length
            r2 = r1
            r4 = r3
        L_0x0008:
            if (r2 < r6) goto L_0x0026
            r2 = r1
        L_0x000b:
            if (r2 == 0) goto L_0x0073
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0051, all -> 0x0061 }
            java.io.FileReader r1 = new java.io.FileReader     // Catch:{ IOException -> 0x0051, all -> 0x0061 }
            r1.<init>(r4)     // Catch:{ IOException -> 0x0051, all -> 0x0061 }
            r2.<init>(r1)     // Catch:{ IOException -> 0x0051, all -> 0x0061 }
            java.lang.String r1 = r2.readLine()     // Catch:{ IOException -> 0x0077 }
            r9.d = r1     // Catch:{ IOException -> 0x0077 }
            r2.close()     // Catch:{ IOException -> 0x0077 }
            if (r2 == 0) goto L_0x0025
            r2.close()     // Catch:{ IOException -> 0x006e }
        L_0x0025:
            return r0
        L_0x0026:
            r7 = r5[r2]
            java.io.File r4 = new java.io.File
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r7 = java.lang.String.valueOf(r7)
            r8.<init>(r7)
            java.lang.String r7 = r9.b
            java.lang.StringBuilder r7 = r8.append(r7)
            java.lang.String r7 = r7.toString()
            r4.<init>(r7)
            boolean r7 = r4.isFile()
            if (r7 == 0) goto L_0x004e
            boolean r7 = r4.exists()
            if (r7 == 0) goto L_0x004e
            r2 = r0
            goto L_0x000b
        L_0x004e:
            int r2 = r2 + 1
            goto L_0x0008
        L_0x0051:
            r1 = move-exception
            r2 = r3
        L_0x0053:
            r1.printStackTrace()     // Catch:{ all -> 0x0075 }
            if (r2 == 0) goto L_0x0025
            r2.close()     // Catch:{ IOException -> 0x005c }
            goto L_0x0025
        L_0x005c:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0025
        L_0x0061:
            r0 = move-exception
            r2 = r3
        L_0x0063:
            if (r2 == 0) goto L_0x0068
            r2.close()     // Catch:{ IOException -> 0x0069 }
        L_0x0068:
            throw r0
        L_0x0069:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0068
        L_0x006e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0025
        L_0x0073:
            r0 = r1
            goto L_0x0025
        L_0x0075:
            r0 = move-exception
            goto L_0x0063
        L_0x0077:
            r1 = move-exception
            goto L_0x0053
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.bu.c():boolean");
    }

    public void b() {
        if (c() && !this.d.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (App.getApp().isAppTop() && (IpcObj.isInCall() || !IpcObj.isDisConnect())) {
                        cb.a().a("蓝牙已连接或在通话中");
                    }
                    IpcObj.connectDevice(bu.this.d.toUpperCase());
                }
            }, 200);
        }
    }
}
