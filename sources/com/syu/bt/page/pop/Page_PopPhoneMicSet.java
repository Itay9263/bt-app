package com.syu.bt.page.pop;

import android.view.View;
import com.syu.app.App;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JSeekBar;
import com.syu.ctrl.JText;
import com.syu.page.Page;

public class Page_PopPhoneMicSet extends Page {
    public JSeekBar mSB_BtPhone;
    public String strPhoneMicSet;
    public JText tvPhoneMicSet;

    public Page_PopPhoneMicSet(JPage jPage) {
        super(jPage);
    }

    public void ResponseClick(View view) {
        App.iTimerCnt = 5;
        App.iPhoneMicSet = this.mSB_BtPhone.getValue();
        App.getApp().mIpcObj.sendCmdVolBal(2, App.iPhoneMicSet);
        this.strPhoneMicSet = String.valueOf(App.iPhoneMicSet);
        if (this.tvPhoneMicSet != null) {
            this.tvPhoneMicSet.setText(this.strPhoneMicSet);
        }
    }

    public void init() {
        this.mSB_BtPhone = (JSeekBar) getPage().getChildViewByid(47);
        if (this.mSB_BtPhone != null) {
            this.mSB_BtPhone.setProgressMax(20);
        }
        this.tvPhoneMicSet = (JText) getPage().getChildViewByid(48);
    }

    public void pause() {
        if (App.getApp().bPopPhoneMicSet) {
            App.getApp().popPhoneMicSet(false);
        }
        super.pause();
    }

    public void resume() {
        super.resume();
        App.SB = App.getApp().mIpcObj.getSB();
        App.iPhoneMicSet = App.SB[2];
        this.mSB_BtPhone.setProgress(App.iPhoneMicSet);
        this.strPhoneMicSet = String.valueOf(App.iPhoneMicSet);
        if (this.tvPhoneMicSet != null) {
            this.tvPhoneMicSet.setText(this.strPhoneMicSet);
        }
    }
}
