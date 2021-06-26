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
import java.util.ArrayList;

public class Page_Callin_HalfScreen extends Page {
    public static final int Mode_Dial = 1;
    public static final int Mode_InCall = 2;
    public static final int Mode_Talking = 3;
    public ActBt actBt;
    View mCallin_Half_ShowCall = null;
    View mCallin_Half_ShowDial = null;
    View mCallin_Half_ShowKey = null;
    public TextView tvTxtName;
    public TextView tvTxtNumber;

    public Page_Callin_HalfScreen(JPage jPage, ActBt actBt2) {
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

    public void Switch2Key(int i) {
        switch (i) {
            case 1:
                if (this.mCallin_Half_ShowDial != null) {
                    this.mCallin_Half_ShowDial.setVisibility(0);
                }
                if (this.mCallin_Half_ShowCall != null) {
                    this.mCallin_Half_ShowCall.setVisibility(8);
                }
                if (this.mCallin_Half_ShowKey != null) {
                    this.mCallin_Half_ShowKey.setVisibility(8);
                    return;
                }
                return;
            case 2:
                if (this.mCallin_Half_ShowDial != null) {
                    this.mCallin_Half_ShowDial.setVisibility(8);
                }
                if (this.mCallin_Half_ShowCall != null) {
                    this.mCallin_Half_ShowCall.setVisibility(0);
                }
                if (this.mCallin_Half_ShowKey != null) {
                    this.mCallin_Half_ShowKey.setVisibility(8);
                    return;
                }
                return;
            case 3:
                if (this.mCallin_Half_ShowDial != null) {
                    this.mCallin_Half_ShowDial.setVisibility(8);
                }
                if (this.mCallin_Half_ShowCall != null) {
                    this.mCallin_Half_ShowCall.setVisibility(8);
                }
                if (this.mCallin_Half_ShowKey != null) {
                    this.mCallin_Half_ShowKey.setVisibility(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void init() {
        this.mCallin_Half_ShowKey = getPage().getChildViewByid(32);
        this.mCallin_Half_ShowDial = getPage().getChildViewByid(30);
        this.mCallin_Half_ShowCall = getPage().getChildViewByid(31);
        Switch2Key(1);
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
        if (jText != null) {
            if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                jText.setText(FinalChip.BSP_PLATFORM_Null);
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(App.mBtInfo.mListContact);
                App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText, FinalChip.BSP_PLATFORM_Null, true), false, 5);
            }
            JText jText2 = (JText) getPage().getChildViewByid(64);
            if (jText2 != null) {
                jText2.setText(jText.getText());
            }
        }
    }
}
