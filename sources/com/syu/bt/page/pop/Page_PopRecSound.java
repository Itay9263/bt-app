package com.syu.bt.page.pop;

import android.view.View;
import com.syu.app.App;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.page.Page;

public class Page_PopRecSound extends Page {
    ActBt actBt;
    private JButton mBtnStopRec;
    private JText mTxtTip;

    public Page_PopRecSound(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void ResponseClick(View view) {
        switch (view.getId()) {
            case 322:
                this.actBt.mRecSound.b();
                if (this.mTxtTip != null) {
                    this.mTxtTip.setText(App.getApp().getString("str_playing"));
                }
                if (this.mBtnStopRec != null) {
                    this.mBtnStopRec.setVisibility(8);
                }
                this.actBt.mRecSound.c();
                return;
            default:
                return;
        }
    }

    public void init() {
        this.mTxtTip = (JText) getPage().getChildViewByid(321);
        this.mBtnStopRec = (JButton) getPage().getChildViewByid(322);
    }

    public void onDismiss() {
        this.actBt.mRecSound.b();
        super.onDismiss();
    }

    public void resume() {
        if (this.mTxtTip != null) {
            this.mTxtTip.setText(App.getApp().getString("str_speak"));
        }
        if (this.mBtnStopRec != null) {
            this.mBtnStopRec.setVisibility(0);
        }
        this.actBt.mRecSound.a();
    }
}
