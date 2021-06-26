package defpackage;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import com.syu.app.App;
import com.syu.ipcself.module.main.Main;
import com.syu.jni.SyuJniNative;
import java.util.Locale;

/* renamed from: bi  reason: default package */
public class bi {
    private static bi e;
    View a;
    public boolean b = false;
    public Runnable c = new Runnable() {
        public void run() {
            if (!bi.this.b) {
                bi.this.a = new View(App.getApp());
                if (Locale.getDefault().getLanguage().toString().contains("zh")) {
                    bi.this.a.setBackgroundResource(App.getApp().getIdDrawable("zh_tip"));
                } else if (Locale.getDefault().getLanguage().toString().contains("ru")) {
                    bi.this.a.setBackgroundResource(App.getApp().getIdDrawable("ru_tip"));
                } else {
                    bi.this.a.setBackgroundResource(App.getApp().getIdDrawable("en_tip"));
                }
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = 40;
                layoutParams.type = 2002;
                layoutParams.gravity = 16;
                layoutParams.format = 1;
                layoutParams.width = -1;
                layoutParams.height = -1;
                bi.this.b = true;
                App app = App.getApp();
                App.getApp();
                ((WindowManager) app.getSystemService("window")).addView(bi.this.a, layoutParams);
            }
        }
    };
    public Runnable d = new Runnable() {
        public void run() {
            if (bi.this.b) {
                App app = App.getApp();
                App.getApp();
                WindowManager windowManager = (WindowManager) app.getSystemService("window");
                if (bi.this.a != null) {
                    windowManager.removeView(bi.this.a);
                }
                bi.this.b = false;
            }
        }
    };
    /* access modifiers changed from: private */
    public int f = -1256;

    public static bi a() {
        if (e == null) {
            e = new bi();
        }
        return e;
    }

    public void a(boolean z) {
        Main.removeRunnable_Ui(this.c);
        Main.removeRunnable_Ui(this.d);
        Runnable runnable = z ? this.c : this.d;
        if (runnable == null) {
            return;
        }
        if (d()) {
            runnable.run();
        } else {
            Main.postRunnable_Ui(true, runnable);
        }
    }

    public void b() {
        if (App.bSpeValue && !this.b) {
            Main.postRunnable_Ui(false, new Runnable() {
                public void run() {
                    boolean z = true;
                    Bundle bundle = new Bundle();
                    Bundle bundle2 = new Bundle();
                    bi.this.f = -256;
                    bundle.putInt("areaindex", 256);
                    SyuJniNative instance = SyuJniNative.getInstance();
                    if (instance != null && SyuJniNative.bLoadLibOnce) {
                        bi.this.f = instance.syu_jni_command(112, bundle, bundle2);
                        if (bundle2 != null && bi.this.f == 0) {
                            bi.this.f = bundle2.getInt("isactived", 55);
                        }
                    }
                    if (bi.this.f != -256) {
                        bi biVar = bi.this;
                        if (bi.this.f == 1) {
                            z = false;
                        }
                        biVar.a(z);
                    }
                }
            }, 0);
        }
    }

    public void c() {
        if (App.bSpeValue) {
            a(false);
        }
    }

    public boolean d() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
