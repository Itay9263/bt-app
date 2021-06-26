package defpackage;

import android.app.Activity;
import android.content.Intent;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.ipcself.module.main.Main;

/* renamed from: bv  reason: default package */
public class bv {
    public static void a(CharSequence charSequence, Activity activity) {
        if (charSequence != null && activity != null && charSequence.toString().equals("51243204")) {
            Intent intent = new Intent();
            intent.setClassName("com.syu.settings", "com.syu.settings.MainActivity");
            intent.setFlags(268435456);
            intent.putExtra("page", 6);
            activity.startActivity(intent);
            IpcObj.clearKey(true);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = com.syu.ipcself.module.main.Bt.DATA[9];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a() {
        /*
            int r0 = com.syu.app.App.mIdCustomer
            r1 = 63
            if (r0 != r1) goto L_0x0017
            int[] r0 = com.syu.ipcself.module.main.Bt.DATA
            r1 = 9
            r0 = r0[r1]
            r1 = 3
            if (r0 == r1) goto L_0x0015
            r1 = 5
            if (r0 == r1) goto L_0x0015
            r1 = 4
            if (r0 != r1) goto L_0x0017
        L_0x0015:
            r0 = 0
        L_0x0016:
            return r0
        L_0x0017:
            r0 = 1
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.bv.a():boolean");
    }

    public static boolean b() {
        return App.mIdCustomer != 41;
    }

    public static boolean c() {
        if ((App.mIdCustomer == 63 || App.mIdCustomer == 94) && App.bHistoryLogAllFlag) {
        }
        return true;
    }

    public static boolean d() {
        return App.mIdCustomer == 53 && Main.mConf_PlatForm == 8;
    }

    public static boolean e() {
        return (App.mIdCustomer == 95 || App.mIdCustomer == 107) && Main.mConf_PlatForm == 8;
    }

    public static boolean f() {
        return App.mIdCustomer == 105 && Main.mConf_PlatForm == 8;
    }

    public static boolean g() {
        return App.mIdCustomer == 62 && Main.mConf_PlatForm == 8;
    }

    public static boolean h() {
        return App.mIdCustomer == 112 && Main.mConf_PlatForm == 8;
    }

    public static boolean i() {
        return App.mIdCustomer == 100 && Main.mConf_PlatForm == 8;
    }

    public static boolean j() {
        return App.mStrCustomer.equalsIgnoreCase("zhtd");
    }

    public static boolean k() {
        switch (App.mIdPlatForm) {
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
                return true;
            default:
                return false;
        }
    }

    public static boolean l() {
        switch (App.mIdPlatForm) {
            case 50:
                return true;
            default:
                return false;
        }
    }
}
