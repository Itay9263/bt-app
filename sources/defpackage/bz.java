package defpackage;

import android.content.SharedPreferences;
import android.util.SparseArray;
import com.syu.app.App;
import com.syu.data.FinalChip;

/* renamed from: bz  reason: default package */
public class bz {
    private static SharedPreferences a = null;

    public static SparseArray<String> a(String str) {
        if (a == null) {
            a = App.getApp().getSharedPreferences("bt_data", 0);
        }
        SparseArray<String> sparseArray = new SparseArray<>();
        sparseArray.put(178, a.getString(String.valueOf(str) + "name", FinalChip.BSP_PLATFORM_Null));
        sparseArray.put(180, a.getString(String.valueOf(str) + "number", FinalChip.BSP_PLATFORM_Null));
        return sparseArray;
    }

    public static void a() {
        if (a == null) {
            a = App.getApp().getSharedPreferences("bt_data", 0);
        }
        a.edit().clear().commit();
    }

    public static void a(String str, String str2) {
        if (a == null) {
            a = App.getApp().getSharedPreferences("bt_data", 0);
        }
        a.edit().putString(str, str2).commit();
    }

    public static boolean a(String str, SparseArray<String> sparseArray) {
        if (a == null) {
            a = App.getApp().getSharedPreferences("bt_data", 0);
        }
        SharedPreferences.Editor edit = a.edit();
        edit.putString(String.valueOf(str) + "name", sparseArray.get(178));
        edit.putString(String.valueOf(str) + "number", sparseArray.get(180));
        return edit.commit();
    }

    public static boolean b() {
        if (a == null) {
            a = App.getApp().getSharedPreferences("bt_data", 0);
        }
        SharedPreferences.Editor edit = a.edit();
        for (int i = 1; i < 6; i++) {
            edit.putString("contact_save" + i + "name", FinalChip.BSP_PLATFORM_Null);
            edit.putString("contact_save" + i + "number", FinalChip.BSP_PLATFORM_Null);
        }
        return edit.commit();
    }
}
