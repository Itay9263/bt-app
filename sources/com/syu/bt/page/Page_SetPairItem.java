package com.syu.bt.page;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.page.Page;
import com.syu.util.FuncUtils;

public class Page_SetPairItem extends Page {
    ActBt actBt;

    public Page_SetPairItem(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void ResponseClick(View view) {
        JPage page;
        JGridView jGridView;
        switch (view.getId()) {
            case 301:
                if (!FuncUtils.isFastDoubleClick() && (page = this.actBt.ui.getPage(11)) != null && (jGridView = (JGridView) page.getChildViewByid(295)) != null) {
                    IpcObj.setChoiceAddr((String) jGridView.getList().get(getPage().getChildTag()).get(298));
                    App.getApp().mIpcObj.FuncPairLink(this);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public SparseArray<String> getChoiceName(String str) {
        if (App.mBtInfo.mListSetPair != null) {
            for (SparseArray<String> next : App.mBtInfo.mListSetPair) {
                if (!TextUtils.isEmpty(next.get(298)) && next.get(298).equals(str)) {
                    return next;
                }
            }
        }
        return null;
    }

    public void popPwd() {
        Dialog popDlg = this.actBt.ui.getPopDlg(18, App.getApp().getIdStyle("pop_add_contacts_anim"), true);
        if (popDlg != null) {
            popDlg.show();
        }
    }
}
