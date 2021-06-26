package defpackage;

import android.util.SparseArray;
import com.syu.app.App;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Main;
import java.util.List;

/* renamed from: bs  reason: default package */
public class bs implements Runnable {
    List<SparseArray<String>> a;
    JText b;
    JText c;
    String d = FinalChip.BSP_PLATFORM_Null;
    String e = FinalChip.BSP_PLATFORM_Null;
    boolean f;

    public bs(List<SparseArray<String>> list, JText jText, JText jText2, String str, String str2, boolean z) {
        this.a = list;
        this.b = jText;
        this.c = jText2;
        this.e = str;
        this.d = str2;
        this.f = z;
    }

    public bs(List<SparseArray<String>> list, JText jText, String str, String str2, boolean z) {
        this.a = list;
        this.b = jText;
        this.e = str;
        this.d = str2;
        this.f = z;
    }

    public void run() {
        if (this.b != null) {
            String nameByNumber = App.mBtInfo.getNameByNumber(this.e, this.a);
            if (this.c != null) {
                Main.postRunnable_Ui(false, new bq(this.b, this.c, (JText) null, nameByNumber, this.d, this.f));
            } else {
                Main.postRunnable_Ui(false, new bq(this.b, nameByNumber, this.d, this.f));
            }
        }
    }
}
