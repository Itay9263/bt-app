package defpackage;

import android.telephony.TelephonyManager;
import com.syu.app.App;

/* renamed from: cd  reason: default package */
public class cd {
    private static cd e = new cd();
    String a;
    String b;
    String c;
    String d;

    private cd() {
    }

    public static cd a() {
        return e;
    }

    public void a(String str) {
        this.a = str;
    }

    public void b() {
        App.mTelephonyManager = (TelephonyManager) App.getApp().getSystemService("phone");
        if (App.mTelephonyManager != null) {
            a(App.mTelephonyManager.getDeviceId());
            b(App.mTelephonyManager.getLine1Number());
            c(App.mTelephonyManager.getSimSerialNumber());
            d(App.mTelephonyManager.getSubscriberId());
        }
    }

    public void b(String str) {
        this.b = str;
    }

    public void c(String str) {
        this.c = str;
    }

    public void d(String str) {
        this.d = str;
    }

    public String toString() {
        return "PhoneInfo [deviceid=" + this.a + ", tel=" + this.b + ", iccid=" + this.c + ", imsi=" + this.d + "]";
    }
}
