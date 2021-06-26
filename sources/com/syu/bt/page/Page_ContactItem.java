package com.syu.bt.page;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.page.Page;
import com.syu.util.FuncUtils;
import java.util.Iterator;

public class Page_ContactItem extends Page {
    ActBt actBt;

    public Page_ContactItem(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void ResponseClick(View view) {
        JPage page;
        JGridView jGridView;
        boolean z;
        boolean z2;
        JPage page2;
        JGridView jGridView2;
        boolean z3 = true;
        if (!bt.a().d()) {
            switch (view.getId()) {
                case 187:
                    if (!FuncUtils.isFastDoubleClick() && (page2 = this.actBt.ui.getPage(5)) != null && (jGridView2 = (JGridView) page2.getChildViewByid(171)) != null) {
                        IpcObj.itemDial((String) jGridView2.getList().get(getPage().getChildTag()).get(180));
                        return;
                    }
                    return;
                case 188:
                    if (!FuncUtils.isFastDoubleClick() && (page = this.actBt.ui.getPage(5)) != null && (jGridView = (JGridView) page.getChildViewByid(171)) != null) {
                        SparseArray sparseArray = jGridView.getList().get(getPage().getChildTag());
                        String str = (String) sparseArray.get(183);
                        if (TextUtils.isEmpty(str)) {
                            if (!bv.e() || App.mBtInfo.mListFavContact.size() < 6) {
                                z = true;
                            } else {
                                cb.a().a(App.getApp().getString("bt_save_number"));
                                z = false;
                            }
                            if (z) {
                                sparseArray.put(183, "collect");
                                Iterator<SparseArray<String>> it = App.mBtInfo.mListFavContact.iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        z3 = false;
                                    } else {
                                        SparseArray next = it.next();
                                        if (next == null || !((String) next.get(180)).equals(sparseArray.get(180)) || !((String) next.get(178)).equals(sparseArray.get(178))) {
                                        }
                                    }
                                }
                                if (!z3) {
                                    App.mBtInfo.mListFavContact.add(sparseArray);
                                }
                            }
                            z2 = z;
                        } else if (str.equals("collect")) {
                            sparseArray.put(183, FinalChip.BSP_PLATFORM_Null);
                            Iterator<SparseArray<String>> it2 = App.mBtInfo.mListFavContact.iterator();
                            while (true) {
                                if (!it2.hasNext()) {
                                    z2 = true;
                                } else {
                                    SparseArray next2 = it2.next();
                                    if (next2 != null && ((String) next2.get(180)).equals(sparseArray.get(180)) && ((String) next2.get(178)).equals(sparseArray.get(178))) {
                                        App.mBtInfo.mListFavContact.remove(next2);
                                        z2 = true;
                                    }
                                }
                            }
                        } else {
                            z2 = true;
                        }
                        this.actBt.getPageContact().notifyDataSetChanged();
                        if (!z2) {
                            return;
                        }
                        if (bv.h() || bv.e()) {
                            bt.a().b(App.mBtInfo.mListFavContact, Bt.sPhoneAddr.replace(":", FinalChip.BSP_PLATFORM_Null));
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
