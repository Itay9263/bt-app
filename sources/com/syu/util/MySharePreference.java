package com.syu.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.syu.data.FinalChip;
import java.util.Set;

public class MySharePreference {
    public static Context mContext;
    private static SharedPreferences mSp;
    public static String mStrPath;

    public static void clear() {
        if (mSp != null) {
            SharedPreferences.Editor edit = mSp.edit();
            edit.clear();
            commit(edit);
        }
    }

    public static void commit(SharedPreferences.Editor editor) {
        if (editor != null) {
            editor.commit();
            sync();
        }
    }

    public static boolean getBooleanValue(String str, boolean z) {
        return mSp != null ? mSp.getBoolean(str, z) : z;
    }

    public static int getIntValue(String str, int i) {
        return mSp != null ? mSp.getInt(str, i) : i;
    }

    public static Set<String> getStringSet(String str, Set<String> set) {
        return mSp != null ? mSp.getStringSet(str, set) : set;
    }

    public static String getStringValue(String str) {
        return mSp != null ? mSp.getString(str, FinalChip.BSP_PLATFORM_Null) : FinalChip.BSP_PLATFORM_Null;
    }

    public static void init(Context context, String str) {
        mContext = context;
        mSp = context.getSharedPreferences(str, 0);
        mStrPath = "/data/data/" + context.getPackageName() + "/shared_prefs/" + str + ".xml";
    }

    public static void saveBooleanValue(String str, boolean z) {
        if (getBooleanValue(str, !z) != z && mSp != null) {
            SharedPreferences.Editor edit = mSp.edit();
            edit.putBoolean(str, z);
            commit(edit);
        }
    }

    public static void saveIntValue(String str, int i) {
        if (getIntValue(str, i + 1) != i && mSp != null) {
            SharedPreferences.Editor edit = mSp.edit();
            edit.putInt(str, i);
            commit(edit);
        }
    }

    public static void saveStringSet(String str, Set<String> set) {
        if (mSp != null) {
            SharedPreferences.Editor edit = mSp.edit();
            edit.putStringSet(str, set);
            commit(edit);
        }
    }

    public static void saveStringValue(String str, String str2) {
        if (getStringValue(str) != str2 && mSp != null) {
            SharedPreferences.Editor edit = mSp.edit();
            edit.putString(str, str2);
            commit(edit);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0027 A[SYNTHETIC, Splitter:B:15:0x0027] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0034 A[SYNTHETIC, Splitter:B:22:0x0034] */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void sync() {
        /*
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 24
            if (r0 >= r1) goto L_0x001f
            java.io.File r0 = new java.io.File
            java.lang.String r1 = mStrPath
            r0.<init>(r1)
            r2 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0020, all -> 0x0030 }
            r1.<init>(r0)     // Catch:{ Exception -> 0x0020, all -> 0x0030 }
            java.io.FileDescriptor r0 = r1.getFD()     // Catch:{ Exception -> 0x0044 }
            r0.sync()     // Catch:{ Exception -> 0x0044 }
            if (r1 == 0) goto L_0x001f
            r1.close()     // Catch:{ Exception -> 0x003d }
        L_0x001f:
            return
        L_0x0020:
            r0 = move-exception
            r1 = r2
        L_0x0022:
            r0.printStackTrace()     // Catch:{ all -> 0x0042 }
            if (r1 == 0) goto L_0x001f
            r1.close()     // Catch:{ Exception -> 0x002b }
            goto L_0x001f
        L_0x002b:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x001f
        L_0x0030:
            r0 = move-exception
            r1 = r2
        L_0x0032:
            if (r1 == 0) goto L_0x0037
            r1.close()     // Catch:{ Exception -> 0x0038 }
        L_0x0037:
            throw r0
        L_0x0038:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0037
        L_0x003d:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x001f
        L_0x0042:
            r0 = move-exception
            goto L_0x0032
        L_0x0044:
            r0 = move-exception
            goto L_0x0022
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.MySharePreference.sync():void");
    }
}
