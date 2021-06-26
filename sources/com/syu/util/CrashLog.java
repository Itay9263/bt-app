package com.syu.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import java.lang.Thread;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class CrashLog implements Thread.UncaughtExceptionHandler {
    public static final String CARSH_DIR_PATH = "/sdcard/crash";
    static CrashLog mInstance;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
    HashMap<String, String> infos;
    Context mContext;
    Thread.UncaughtExceptionHandler mDefaultHandler;
    String pkgName;

    public CrashLog(Context context) {
        this.mContext = context.getApplicationContext();
        this.pkgName = this.mContext.getPackageName().replace(".", "_");
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static CrashLog getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CrashLog(context);
        }
        return mInstance;
    }

    private boolean handleException(Throwable th) {
        if (th == null) {
            return false;
        }
        collectInfo();
        saveCrashException(th);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void collectInfo() {
        if (this.infos == null) {
            this.infos = new HashMap<>();
        }
        try {
            PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 1);
            if (packageInfo != null) {
                String str = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                String sb = new StringBuilder(String.valueOf(packageInfo.versionCode)).toString();
                this.infos.put("versionName", str);
                this.infos.put("versionCode", sb);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00c0 A[SYNTHETIC, Splitter:B:20:0x00c0] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0117 A[SYNTHETIC, Splitter:B:37:0x0117] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x012c A[SYNTHETIC, Splitter:B:48:0x012c] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0138 A[SYNTHETIC, Splitter:B:54:0x0138] */
    /* JADX WARNING: Removed duplicated region for block: B:70:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:71:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void saveCrashException(java.lang.Throwable r8) {
        /*
            r7 = this;
            r2 = 0
            java.text.DateFormat r0 = r7.dateFormat
            java.util.Date r1 = new java.util.Date
            r1.<init>()
            java.lang.String r3 = r0.format(r1)
            java.lang.StringBuffer r4 = new java.lang.StringBuffer
            r4.<init>()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = r7.pkgName
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r0.<init>(r1)
            java.lang.String r1 = "\n"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r4.append(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = java.lang.String.valueOf(r3)
            r0.<init>(r1)
            java.lang.String r1 = "\n"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r4.append(r0)
            java.util.HashMap<java.lang.String, java.lang.String> r0 = r7.infos
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r5 = r0.iterator()
        L_0x0049:
            boolean r0 = r5.hasNext()
            if (r0 != 0) goto L_0x00c4
            java.io.StringWriter r5 = new java.io.StringWriter
            r5.<init>()
            java.io.PrintWriter r1 = new java.io.PrintWriter     // Catch:{ Exception -> 0x0101, all -> 0x0113 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0101, all -> 0x0113 }
            r8.printStackTrace(r1)     // Catch:{ Exception -> 0x014f }
            java.lang.Throwable r0 = r8.getCause()     // Catch:{ Exception -> 0x014f }
        L_0x0060:
            if (r0 != 0) goto L_0x00f8
            if (r1 == 0) goto L_0x0067
            r1.close()     // Catch:{ Exception -> 0x0120 }
        L_0x0067:
            java.lang.String r0 = r5.toString()
            r4.append(r0)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "crash-"
            r0.<init>(r1)
            java.lang.String r1 = r7.pkgName
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = "-"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r3)
            java.lang.String r1 = ".txt"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.io.File r1 = new java.io.File
            java.lang.String r3 = "/sdcard/crash"
            r1.<init>(r3)
            boolean r3 = r1.exists()
            if (r3 != 0) goto L_0x009f
            r1.mkdirs()
        L_0x009f:
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0126 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0126 }
            java.lang.String r5 = "/sdcard/crash/"
            r3.<init>(r5)     // Catch:{ Exception -> 0x0126 }
            java.lang.StringBuilder r0 = r3.append(r0)     // Catch:{ Exception -> 0x0126 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0126 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0126 }
            java.lang.String r0 = r4.toString()     // Catch:{ Exception -> 0x014a, all -> 0x0147 }
            byte[] r0 = r0.getBytes()     // Catch:{ Exception -> 0x014a, all -> 0x0147 }
            r1.write(r0)     // Catch:{ Exception -> 0x014a, all -> 0x0147 }
            if (r1 == 0) goto L_0x00c3
            r1.close()     // Catch:{ Exception -> 0x0141 }
        L_0x00c3:
            return
        L_0x00c4:
            java.lang.Object r0 = r5.next()
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0
            java.lang.Object r1 = r0.getKey()
            java.lang.String r1 = (java.lang.String) r1
            java.lang.Object r0 = r0.getValue()
            java.lang.String r0 = (java.lang.String) r0
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r6.<init>(r1)
            java.lang.String r1 = "="
            java.lang.StringBuilder r1 = r6.append(r1)
            java.lang.StringBuilder r0 = r1.append(r0)
            java.lang.String r1 = "\n"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            r4.append(r0)
            goto L_0x0049
        L_0x00f8:
            r0.printStackTrace(r1)     // Catch:{ Exception -> 0x014f }
            java.lang.Throwable r0 = r0.getCause()     // Catch:{ Exception -> 0x014f }
            goto L_0x0060
        L_0x0101:
            r0 = move-exception
            r1 = r2
        L_0x0103:
            r0.printStackTrace()     // Catch:{ all -> 0x014d }
            if (r1 == 0) goto L_0x0067
            r1.close()     // Catch:{ Exception -> 0x010d }
            goto L_0x0067
        L_0x010d:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0067
        L_0x0113:
            r0 = move-exception
            r1 = r2
        L_0x0115:
            if (r1 == 0) goto L_0x011a
            r1.close()     // Catch:{ Exception -> 0x011b }
        L_0x011a:
            throw r0
        L_0x011b:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x011a
        L_0x0120:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0067
        L_0x0126:
            r0 = move-exception
        L_0x0127:
            r0.printStackTrace()     // Catch:{ all -> 0x0135 }
            if (r2 == 0) goto L_0x00c3
            r2.close()     // Catch:{ Exception -> 0x0130 }
            goto L_0x00c3
        L_0x0130:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00c3
        L_0x0135:
            r0 = move-exception
        L_0x0136:
            if (r2 == 0) goto L_0x013b
            r2.close()     // Catch:{ Exception -> 0x013c }
        L_0x013b:
            throw r0
        L_0x013c:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x013b
        L_0x0141:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00c3
        L_0x0147:
            r0 = move-exception
            r2 = r1
            goto L_0x0136
        L_0x014a:
            r0 = move-exception
            r2 = r1
            goto L_0x0127
        L_0x014d:
            r0 = move-exception
            goto L_0x0115
        L_0x014f:
            r0 = move-exception
            goto L_0x0103
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.CrashLog.saveCrashException(java.lang.Throwable):void");
    }

    public void uncaughtException(Thread thread, Throwable th) {
        if (!handleException(th)) {
            if (this.mDefaultHandler == null) {
                this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
            }
            this.mDefaultHandler.uncaughtException(thread, th);
            return;
        }
        Process.killProcess(Process.myPid());
        System.exit(1);
    }
}
