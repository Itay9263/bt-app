package defpackage;

import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;
import com.syu.app.App;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Main;

/* renamed from: cb  reason: default package */
public class cb {
    private static cb d = null;
    Toast a;
    TextView b;
    String c;

    public cb() {
        Main.postRunnable_Ui(false, new Runnable() {
            public void run() {
                cb.this.b = new TextView(App.getApp());
                cb.this.b.setBackgroundResource(App.getApp().getIdDrawable("toast_bg"));
                cb.this.b.setMaxWidth((int) (600.0f * App.mScale));
                cb.this.b.setMaxHeight((int) (360.0f * App.mScale));
                cb.this.b.setGravity(17);
                if (bv.h()) {
                    cb.this.b.setTextColor(Color.rgb(255, 255, 255));
                } else if (bv.e()) {
                    cb.this.b.setTextColor(-1);
                } else {
                    cb.this.b.setTextColor(-16711936);
                }
                cb.this.b.setTextSize(0, 25.0f * App.mScale);
                cb.this.a = new Toast(App.getApp());
                cb.this.a.setDuration(0);
                cb.this.a.setGravity(17, App.getApp().ToastUtil_XY[0], (int) (60.0f * App.mScale));
                cb.this.a.setView(cb.this.b);
            }
        });
    }

    public static cb a() {
        if (d == null) {
            d = new cb();
        }
        return d;
    }

    public void a(String str) {
        this.c = str;
        if (this.c == null) {
            this.c = FinalChip.BSP_PLATFORM_Null;
        }
        Main.postRunnable_Ui(false, new Runnable() {
            public void run() {
                cb.this.b.setText(cb.this.c);
                cb.this.a.show();
            }
        });
    }
}
