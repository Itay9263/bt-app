package defpackage;

import android.text.TextUtils;
import android.util.SparseArray;
import com.syu.app.App;
import com.syu.bt.page.Page_Contact;
import com.syu.bt.page.pop.Page_PopBt_Book;
import com.syu.ctrl.JPage;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import java.util.Iterator;

/* renamed from: bn  reason: default package */
public class bn implements Runnable {
    Page_Contact a;

    public bn(Page_Contact page_Contact) {
        this.a = page_Contact;
    }

    public void run() {
        JPage jPage;
        Page_PopBt_Book page_PopBt_Book;
        if (App.mBtInfo.mListContactDownload.size() > 0) {
            App.mBtInfo.mListContact.clear();
            App.mBtInfo.mListContact.addAll(App.mBtInfo.mListContactDownload);
            App.mBtInfo.mListContactDownload.clear();
            App.mBtInfo.sortContact();
            App.queryCallLog();
            if (this.a != null) {
                this.a.endDownload();
            }
            if (App.bAutoDownPhoneBook || App.bAutoSavePhoneBook) {
                App.mBtInfo.savePhoneBook();
            }
            if (!(!App.getApp().bPopBtBook || (jPage = App.uiApp.mPages.get(4)) == null || (page_PopBt_Book = (Page_PopBt_Book) jPage.getNotify()) == null)) {
                page_PopBt_Book.queryContacts(Bt.sPhoneNumber);
            }
            if (bv.e()) {
                Iterator<SparseArray<String>> it = App.mBtInfo.mListFavContact.iterator();
                while (it.hasNext()) {
                    SparseArray next = it.next();
                    if (next != null && TextUtils.isEmpty((CharSequence) next.get(184))) {
                        it.remove();
                    }
                }
                bt.a().b(App.mBtInfo.mListFavContact, Bt.sPhoneAddr.replace(":", FinalChip.BSP_PLATFORM_Null));
            }
            App.getApp().updatePhoneName();
        }
    }
}
