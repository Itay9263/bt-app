package defpackage;

import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import com.syu.app.App;
import com.syu.ctrl.JText;
import com.syu.ipcself.module.main.Bt;

/* renamed from: bq  reason: default package */
public class bq implements Runnable {
    JText a;
    JText b;
    JText c;
    String d;
    String e;
    boolean f;

    public bq(JText jText, JText jText2, JText jText3, String str, String str2, boolean z) {
        this.a = jText;
        this.b = jText2;
        this.c = jText3;
        this.d = str;
        this.e = str2;
        this.f = z;
    }

    public bq(JText jText, String str, String str2, boolean z) {
        this.a = jText;
        this.d = str;
        this.e = str2;
        this.f = z;
    }

    public void run() {
        if (this.a != null) {
            if (!TextUtils.isEmpty(this.d)) {
                this.a.setText(this.d);
            } else if (App.getApp().mIpcObj.isCalling()) {
                if (bv.h()) {
                    this.a.setText(App.getApp().getString(EnvironmentCompat.MEDIA_UNKNOWN));
                } else if (TextUtils.isEmpty(Bt.sPhoneNumberHoldOn) || Bt.DATA[9] != 4) {
                    this.a.setText(Bt.sPhoneNumber);
                } else {
                    this.a.setText(Bt.sPhoneNumberHoldOn);
                }
            } else if (this.f || !App.getApp().mIpcObj.isCalling()) {
                this.a.setText(this.e);
            }
        }
        if (this.b != null) {
            if (!TextUtils.isEmpty(this.d)) {
                this.b.setText(this.d);
            } else if (App.getApp().mIpcObj.isCalling()) {
                if (bv.h()) {
                    this.b.setText(App.getApp().getString(EnvironmentCompat.MEDIA_UNKNOWN));
                } else if (TextUtils.isEmpty(Bt.sPhoneNumberHoldOn) || Bt.DATA[9] != 4) {
                    this.b.setText(Bt.sPhoneNumber);
                } else {
                    this.b.setText(Bt.sPhoneNumberHoldOn);
                }
            } else if (this.f || !App.getApp().mIpcObj.isCalling()) {
                this.b.setText(this.e);
            }
        }
        if (this.c == null) {
            return;
        }
        if (!TextUtils.isEmpty(this.d)) {
            this.c.setText(this.d);
        } else if (App.getApp().mIpcObj.isCalling()) {
            if (TextUtils.isEmpty(Bt.sPhoneNumberHoldOn) || Bt.DATA[9] != 4) {
                this.c.setText(Bt.sPhoneNumber);
            } else {
                this.c.setText(Bt.sPhoneNumberHoldOn);
            }
        } else if (this.f || !App.getApp().mIpcObj.isCalling()) {
            this.c.setText(this.e);
        }
    }
}
