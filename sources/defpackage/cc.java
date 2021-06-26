package defpackage;

import android.app.Activity;
import android.graphics.Typeface;
import com.syu.app.App;

/* renamed from: cc  reason: default package */
public class cc {
    public Activity a;
    public by b;

    public cc(Activity activity) {
        this.a = activity;
    }

    public void a() {
        if (this.b != null) {
            this.b.cancel();
        }
    }

    public void a(String str) {
        try {
            if (this.b == null) {
                if (this.a != null) {
                    this.b = new by(this.a, App.getApp().UIUtil_XY[0], App.getApp().UIUtil_XY[1]);
                } else {
                    return;
                }
            }
            if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
                this.b.a(str, -1, 25, (Typeface) null);
                this.b.show();
            } else if (!this.b.isShowing()) {
                if (bv.e() || bv.h()) {
                    this.b.a(str, -1, 22, (Typeface) null);
                } else {
                    this.b.a(str, -16711936, 22, (Typeface) null);
                }
                this.b.show();
            } else {
                this.b.a(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String b() {
        if (this.b != null) {
            return this.b.a();
        }
        return null;
    }
}
