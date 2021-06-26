package com.syu.bt.page;

import android.view.View;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.page.Page;
import com.syu.util.FuncUtils;

public class Page_DialItem extends Page {
    ActBt actBt;

    public Page_DialItem(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void ResponseClick(View view) {
        JPage page;
        JGridView jGridView;
        if (!bt.a().d()) {
            switch (view.getId()) {
                case 124:
                    if (!FuncUtils.isFastDoubleClick() && (page = this.actBt.ui.getPage(3)) != null && (jGridView = (JGridView) page.getChildViewByid(157)) != null) {
                        String str = (String) jGridView.getList().get(getPage().getChildTag()).get(180);
                        IpcObj.setNum(str);
                        IpcObj.itemDial(str);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
