package defpackage;

import android.util.SparseArray;
import com.syu.app.App;
import com.syu.bt.page.Page_Contact;
import com.syu.ipcself.module.main.Main;
import java.util.List;

/* renamed from: bp  reason: default package */
public class bp implements Runnable {
    String a;
    String b;
    List<SparseArray<String>> c;
    List<SparseArray<String>> d;
    Page_Contact e;

    public bp(String str, String str2, List<SparseArray<String>> list, List<SparseArray<String>> list2, Page_Contact page_Contact) {
        this.a = str;
        this.b = str2;
        this.c = list;
        this.d = list2;
        this.e = page_Contact;
    }

    public void run() {
        if (App.mBtInfo.getMapContact(this.a, this.b, this.d) == null) {
            SparseArray<String> newMapContact = App.getNewMapContact(this.a, this.b);
            Main.postRunnable_Ui(false, new bl(newMapContact));
            if (App.mBtInfo.getMapContact(this.a, this.b, this.c) == null) {
                Main.postRunnable_Ui(false, new bm(newMapContact, this.e));
            }
        }
    }
}
