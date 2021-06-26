package defpackage;

import android.util.SparseArray;
import com.syu.app.App;

/* renamed from: bl  reason: default package */
public class bl implements Runnable {
    SparseArray<String> a;

    public bl(SparseArray<String> sparseArray) {
        this.a = sparseArray;
    }

    public void run() {
        App.mBtInfo.mListContactDownload.add(this.a);
    }
}
