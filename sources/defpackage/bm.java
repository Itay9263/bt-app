package defpackage;

import android.util.SparseArray;
import com.syu.app.App;
import com.syu.bt.page.Page_Contact;
import com.syu.ctrl.JGridView;

/* renamed from: bm  reason: default package */
public class bm implements Runnable {
    SparseArray<String> a;
    Page_Contact b;

    public bm(SparseArray<String> sparseArray, Page_Contact page_Contact) {
        this.a = sparseArray;
        this.b = page_Contact;
    }

    public void run() {
        App.iDownloadCnt++;
        App.mBtInfo.mListContact.add(this.a);
        if (this.b != null) {
            App.add((JGridView) this.b.getPage().getChildViewByid(171), this.a);
            this.b.setContactNum(Integer.toString(App.iDownloadCnt));
        }
    }
}
