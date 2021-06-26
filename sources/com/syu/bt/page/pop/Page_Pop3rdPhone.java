package com.syu.bt.page.pop;

import android.view.View;
import com.syu.app.App;
import com.syu.app.MyUiItem;
import com.syu.app.ipc.IpcObj;
import com.syu.app.ipc.Ipc_New;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.ipcself.module.main.Bt;
import com.syu.page.Page;
import java.util.ArrayList;
import java.util.Locale;

public class Page_Pop3rdPhone extends Page {
    JButton mBtnAccept;
    JText mTxtMergerName1;
    JText mTxtMergerName2;
    JText mTxtMergerTime;
    JText mTxtName;
    View mViewMerger = null;
    View mViewThirdphone = null;

    public Page_Pop3rdPhone(JPage jPage) {
        super(jPage);
    }

    public void ResponseClick(View view) {
        switch (view.getId()) {
            case 54:
                Ipc_New.switch3rdPhone();
                return;
            case 55:
                if (Ipc_New.isRing()) {
                    Ipc_New.hang3rdPhoneHold();
                    return;
                } else {
                    Ipc_New.hang3rdPhoneCurrent();
                    return;
                }
            case 57:
                Ipc_New.merge3rdPhone();
                return;
            case 61:
                if (Ipc_New.isMerger()) {
                    IpcObj.hang();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void init() {
        super.init();
        this.mBtnAccept = (JButton) getPage().getChildViewByid(54);
        this.mTxtName = (JText) getPage().getChildViewByid(53);
        this.mTxtMergerName1 = (JText) getPage().getChildViewByid(58);
        this.mTxtMergerName2 = (JText) getPage().getChildViewByid(59);
        this.mTxtMergerTime = (JText) getPage().getChildViewByid(60);
        this.mViewThirdphone = getPage().getChildViewByid(51);
        this.mViewMerger = getPage().getChildViewByid(52);
    }

    public void resume() {
        super.resume();
        updateThirdPhoneView();
        updateMergerPhoneName();
        updatePhoneName();
        updateBtnAccept();
    }

    public void updateBtnAccept() {
        if (!Ipc_New.isMerger() && this.mBtnAccept != null) {
            MyUiItem myUiItem = (MyUiItem) this.mBtnAccept.getTag();
            if (myUiItem.getParaStr() != null) {
                String strDrawable = Ipc_New.isRing() ? myUiItem.getStrDrawable() : myUiItem.getParaStr() != null ? myUiItem.getParaStr()[0] : null;
                if (this.mBtnAccept.getStrDrawable() != strDrawable && strDrawable != null) {
                    this.mBtnAccept.setStrDrawable(strDrawable, true);
                }
            }
        }
    }

    public void updateMergerPhoneName() {
        if (Ipc_New.isMerger() && this.mTxtMergerName1 != null && this.mTxtMergerName2 != null) {
            String str = Bt.sPhoneNumber;
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(App.mBtInfo.mListContact);
            App.startThread(App.StrThreadGetNameByNumber, new bs(arrayList, this.mTxtMergerName1, str, str, true), false, 5);
            String str2 = Bt.sPhoneNumberHoldOn;
            App.startThread(App.StrThreadGetNameByNumber, new bs(arrayList, this.mTxtMergerName2, str2, str2, true), false, 5);
        }
    }

    public void updateMergerTime(int i) {
        if (Ipc_New.isMerger() && this.mTxtMergerTime != null) {
            this.mTxtMergerTime.setText(String.format(Locale.US, "%02d:%02d", new Object[]{Integer.valueOf(i / 60), Integer.valueOf(i % 60)}));
        }
    }

    public void updatePhoneName() {
        if (!Ipc_New.isMerger() && this.mTxtName != null) {
            String str = Bt.sPhoneNumber;
            if (Ipc_New.isRing()) {
                str = Bt.sPhoneNumberHoldOn;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(App.mBtInfo.mListContact);
            App.startThread(App.StrThreadGetNameByNumber, new bs(arrayList, this.mTxtName, str, str, true), false, 5);
        }
    }

    public void updateThirdPhoneView() {
        int i = 0;
        if (this.mViewThirdphone != null) {
            this.mViewThirdphone.setVisibility(Ipc_New.isMerger() ? 8 : 0);
        }
        if (this.mViewMerger != null) {
            View view = this.mViewMerger;
            if (!Ipc_New.isMerger()) {
                i = 8;
            }
            view.setVisibility(i);
        }
    }
}
