package com.syu.bt.page;

import android.view.View;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JPage;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import com.syu.util.FuncUtils;

public class Page_Dial_HalfScreen extends Page {
    public ActBt actBt;

    public Page_Dial_HalfScreen(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    private boolean btnClick(View view) {
        switch (view.getId()) {
            case 45:
                App.getApp().setFullScreenMode(1);
                return true;
            case 46:
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
            case 115:
                if (FuncUtils.isFastDoubleClick()) {
                    return true;
                }
                if (Bt.DATA[9] == 0 || (!(Main.mConf_PlatForm == 5 || Main.mConf_PlatForm == 6 || Main.mConf_PlatForm == 7 || Main.mConf_PlatForm == 8) || !IpcObj.isInCall())) {
                    IpcObj.linkCut();
                    return true;
                } else if (!App.bHFP) {
                    return true;
                } else {
                    this.actBt.popDeleteContacts(6, App.getApp().getString("str_note_swith2htp"));
                    return true;
                }
            case 116:
                if (FuncUtils.isFastDoubleClick()) {
                    return true;
                }
                IpcObj.link();
                return true;
            case 117:
                if (FuncUtils.isFastDoubleClick()) {
                    return true;
                }
                if (Main.mConf_PlatForm != 5 || !IpcObj.isInCall()) {
                    IpcObj.cut();
                    return true;
                } else if (!App.bHFP) {
                    return true;
                } else {
                    this.actBt.popDeleteContacts(6, App.getApp().getString("str_note_swith2htp"));
                    return true;
                }
            case 118:
                if (FuncUtils.isFastDoubleClick()) {
                    return true;
                }
                IpcObj.VoiceSwitch();
                return true;
            case 122:
                App.getApp().setFullScreenMode(1);
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
            case 120:
                IpcObj.clearKey(true);
                return false;
            default:
                return false;
        }
    }

    public void init() {
    }

    public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        App.getApp().mIpcObj.onNotify(this, i, iArr, fArr, strArr);
    }

    public void pause() {
        App.bResumeByDial = false;
        super.pause();
    }

    public void resume() {
        JPage jPage;
        if (this.actBt.bTop) {
            if (!App.bResumeByDial) {
                App.getApp().mIpcObj.requestAppIdRight(3);
            }
            App.getApp().mIpcObj.updateNotify_Dial_HalfScreen();
            if (App.getApp().bPopBtRingSet) {
                App.getApp().popBtRingSet(false);
            }
            if (App.bDelMenuFromBtAv && (jPage = this.actBt.ui.mPages.get(2)) != null && jPage.getVisibility() != 0) {
                jPage.setVisibility(0);
            }
        }
    }
}
