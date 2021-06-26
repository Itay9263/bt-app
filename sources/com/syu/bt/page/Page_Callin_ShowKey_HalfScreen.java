package com.syu.bt.page;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.page.Page;
import com.syu.util.FuncUtils;

public class Page_Callin_ShowKey_HalfScreen extends Page {
    public ActBt actBt;
    View mCallin_Half_ShowCall = null;
    View mCallin_Half_ShowDial = null;
    View mCallin_Half_ShowKey = null;
    public TextView tvTxtName;
    public TextView tvTxtNumber;

    public Page_Callin_ShowKey_HalfScreen(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    private boolean btnClick(View view) {
        switch (view.getId()) {
            case 95:
                App.getApp().setFullScreenMode(1);
                App.bShowKeyFlag = true;
                return true;
            case 98:
                App.getApp().setFullScreenMode(1);
                return true;
            case 101:
                if (FuncUtils.isFastDoubleClick()) {
                    return true;
                }
                App.getApp().mIpcObj.FuncDial();
                return true;
            case 102:
                if (FuncUtils.isFastDoubleClick() || !IpcObj.isInCall()) {
                    return true;
                }
                IpcObj.hang();
                return true;
            case 118:
                if (FuncUtils.isFastDoubleClick()) {
                    return true;
                }
                IpcObj.VoiceSwitch();
                return true;
            default:
                return false;
        }
    }

    public void ResponseClick(View view) {
        if (bt.a().d() || btnClick(view)) {
            return;
        }
        if (App.getApp().mIpcObj.isCalling()) {
            cb.a().a(App.getApp().getString("bt_state_using"));
        } else {
            if (this.actBt.MenuClick(view)) {
            }
        }
    }

    public boolean ResponseLongClick(View view) {
        switch (view.getId()) {
            case 103:
                IpcObj.LongClick0();
                return false;
            default:
                return false;
        }
    }

    public void init() {
        this.mCallin_Half_ShowKey = getPage().getChildViewByid(90);
        this.mCallin_Half_ShowDial = getPage().getChildViewByid(89);
        this.mCallin_Half_ShowCall = getPage().getChildViewByid(88);
    }

    public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        App.getApp().mIpcObj.onNotify(this, i, iArr, fArr, strArr);
    }

    public void pause() {
        App.bResumeByDial = false;
        super.pause();
    }

    public void resume() {
        if (this.actBt.bTop) {
            if (!App.bResumeByDial) {
                App.getApp().mIpcObj.requestAppIdRight(3);
            }
            App.getApp().mIpcObj.updateNotify_Callin_HalfScreen();
            if (App.getApp().bPopBtRingSet) {
                App.getApp().popBtRingSet(false);
            }
        }
    }

    public void updatePhoneName() {
        JText jText = (JText) getPage().getChildViewByid(57);
        JText jText2 = (JText) getPage().getChildViewByid(64);
        if (jText != null) {
            if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                jText.setText(FinalChip.BSP_PLATFORM_Null);
            } else {
                String nameByNumber = App.mBtInfo.getNameByNumber(Bt.sPhoneNumber);
                if (!TextUtils.isEmpty(nameByNumber)) {
                    jText.setText(nameByNumber);
                } else if (!App.getApp().mIpcObj.isCalling()) {
                    jText.setText(FinalChip.BSP_PLATFORM_Null);
                }
            }
            if (jText2 != null) {
                jText2.setText(jText.getText());
            }
        }
    }
}
