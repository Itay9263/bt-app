package defpackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;

/* renamed from: ca  reason: default package */
public class ca {
    public static void a() {
        if (IpcObj.isAppIdBtPhone() && IpcObj.isRing()) {
            IpcObj.dial();
        }
    }

    public static void a(Context context, String str) {
        Bundle bundle = new Bundle();
        bundle.putString("contacts", str);
        Intent intent = new Intent("com.fyt.speech.temp_contact");
        intent.putExtras(bundle);
        intent.setPackage(App.mPkgName);
        App.getApp().startServiceSafely(intent);
        if (TextUtils.isEmpty(str)) {
            bz.a();
        } else {
            bz.a("tmp_contacts", str.toString());
        }
    }

    public static void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            c(str);
        }
    }

    public static void b() {
        if (IpcObj.isInCall()) {
            IpcObj.hang();
        }
    }

    public static void b(String str) {
        if (!TextUtils.isEmpty(str)) {
            String queryNumberByName = App.mBtInfo.queryNumberByName(str);
            if (TextUtils.isEmpty(queryNumberByName)) {
                cb.a().a(App.getApp().getString("bt_match_number_fail"));
            } else {
                c(queryNumberByName);
            }
        }
    }

    public static void c() {
        if (IpcObj.isConnect()) {
            IpcObj.cut();
        }
    }

    private static void c(String str) {
        if (TextUtils.isEmpty(str)) {
            cb.a().a(App.getApp().getString("bt_match_number_fail"));
        } else {
            App.getApp().mIpcObj.dialOut(str);
        }
    }

    public static void d() {
        if (!IpcObj.isConnect()) {
            IpcObj.link();
        }
    }

    public static void e() {
        cb.a().a(App.getApp().getString("bt_cmd_invalid"));
    }
}
