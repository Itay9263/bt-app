package defpackage;

import com.syu.bt.page.Page_Contact;
import com.syu.ipcself.module.main.Main;

/* renamed from: bo  reason: default package */
public class bo implements Runnable {
    Page_Contact a;

    public bo(Page_Contact page_Contact) {
        this.a = page_Contact;
    }

    public void run() {
        Main.postRunnable_Ui(false, new bn(this.a));
    }
}
