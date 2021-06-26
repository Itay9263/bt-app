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

public class Page_PairedItem extends Page {
    ActBt actGrid;

    public Page_PairedItem(JPage jPage, ActBt actBt) {
        super(jPage);
        this.actGrid = actBt;
    }

    public void ResponseClick(View view) {
        JGridView jGridView;
        SparseArray sparseArray;
        switch (view.getId()) {
            case 294:
                JPage page = this.actGrid.ui.getPage(12);
                if (page != null && (jGridView = (JGridView) page.getChildViewByid(287)) != null && (sparseArray = jGridView.getList().get(getPage().getChildTag())) != null) {
                    String str = (String) sparseArray.get(290);
                    if (!TextUtils.isEmpty(str)) {
                        IpcObj.deleteConnectedDevice(str);
                        App.mBtInfo.mListHasPaired.remove(sparseArray);
                        IpcObj.setChoiceAddr(FinalChip.BSP_PLATFORM_Null);
                        if (IpcObj.isConnect() && str.equals(Bt.sPhoneAddr)) {
                            IpcObj.cut();
                        }
                        Page_Pair pagePair = this.actGrid.getPagePair();
                        if (pagePair != null) {
                            pagePair.refreshPairedList();
                            return;
                        }
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
