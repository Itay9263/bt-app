package defpackage;

import android.util.SparseArray;
import com.syu.app.App;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import java.util.List;

/* renamed from: br  reason: default package */
public class br implements Runnable {
    List<SparseArray<String>> a;
    JText b;
    JText c;
    JText d;
    String e = FinalChip.BSP_PLATFORM_Null;
    boolean f;

    public br(List<SparseArray<String>> list, JText jText, JText jText2, JText jText3, String str, boolean z) {
        this.a = list;
        this.b = jText;
        this.c = jText2;
        this.d = jText3;
        this.e = str;
        this.f = z;
    }

    public br(List<SparseArray<String>> list, JText jText, String str, boolean z) {
        this.a = list;
        this.b = jText;
        this.e = str;
        this.f = z;
    }

    public void run() {
        if (this.b != null || this.c != null || this.d != null) {
            Main.postRunnable_Ui(false, new bq(this.b, this.c, this.d, App.mBtInfo.getNameByNumber(Bt.sPhoneNumber, this.a), this.e, this.f));
        }
    }
}
