package com.syu.bt.page;

import android.view.View;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.page.Page;
import com.syu.util.FuncUtils;

public class Page_HistoryItem extends Page {
    ActBt actBt;

    public Page_HistoryItem(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void ResponseClick(View view) {
        JPage page;
        JGridView jGridView;
        if (!bt.a().d()) {
            switch (view.getId()) {
                case 206:
                    if (!FuncUtils.isFastDoubleClick() && (page = this.actBt.ui.getPage(8)) != null && (jGridView = (JGridView) page.getChildViewByid(196)) != null) {
                        IpcObj.itemDial((String) jGridView.getList().get(getPage().getChildTag()).get(203));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
