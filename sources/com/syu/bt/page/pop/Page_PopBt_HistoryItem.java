package com.syu.bt.page.pop;

import android.view.View;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.ctrl.JPage;
import com.syu.page.Page;
import com.syu.util.FuncUtils;

public class Page_PopBt_HistoryItem extends Page {
    public Page_PopBt_HistoryItem(JPage jPage) {
        super(jPage);
    }

    public void ResponseClick(View view) {
        JPage jPage;
        Page_PopBt_Book page_PopBt_Book;
        switch (view.getId()) {
            case 67:
                if (!FuncUtils.isFastDoubleClick() && (jPage = App.uiApp.mPages.get(4)) != null && (page_PopBt_Book = (Page_PopBt_Book) jPage.getNotify()) != null && page_PopBt_Book.mGridHistory != null) {
                    IpcObj.itemDial((String) page_PopBt_Book.mGridHistory.getList().get(getPage().getChildTag()).get(203));
                    return;
                }
                return;
            default:
                return;
        }
    }
}
