package com.syu.bt.page.pop;

import android.view.View;
import com.syu.app.App;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JSeekBar;
import com.syu.ctrl.JText;
import com.syu.page.Page;

public class Page_PopBtAvMicSet extends Page {
    public JSeekBar mSB_BtMusic;
    public String strBtAvMicSet;
    public JText tvBtAvMicSet;

    public Page_PopBtAvMicSet(JPage jPage) {
        super(jPage);
    }

    public void ResponseClick(View view) {
        App.iTimerCnt = 5;
        App.iBtAvMicSet = this.mSB_BtMusic.getValue();
        App.getApp().mIpcObj.sendCmdVolBal(3, App.iBtAvMicSet);
        this.strBtAvMicSet = String.valueOf(App.iBtAvMicSet);
        if (this.tvBtAvMicSet != null) {
            this.tvBtAvMicSet.setText(this.strBtAvMicSet);
        }
    }

    public void init() {
        this.mSB_BtMusic = (JSeekBar) getPage().getChildViewByid(49);
        if (this.mSB_BtMusic != null) {
            this.mSB_BtMusic.setProgressMax(10);
        }
        this.tvBtAvMicSet = (JText) getPage().getChildViewByid(50);
    }

    public void pause() {
        if (App.getApp().bPopBtAvMicSet) {
            App.getApp().popBtAvMicSet(false);
        }
        super.pause();
    }

    public void resume() {
        super.resume();
        App.SB = App.getApp().mIpcObj.getSB();
        App.iBtAvMicSet = App.SB[3];
        this.mSB_BtMusic.setProgress(App.iBtAvMicSet);
        this.strBtAvMicSet = String.valueOf(App.iBtAvMicSet);
        if (this.tvBtAvMicSet != null) {
            this.tvBtAvMicSet.setText(this.strBtAvMicSet);
        }
    }
}
