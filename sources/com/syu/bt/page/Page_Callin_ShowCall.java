package com.syu.bt.page;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.bt.ctrl.SwipeCallingView;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import com.syu.util.FuncUtils;

public class Page_Callin_ShowCall extends Page {
    public ActBt actBt;
    JButton mBtnVoice = null;
    View mCallin_HideKeyboard = null;
    View mCallin_ShowCall = null;
    View mCallin_ShowDial = null;
    View mCallin_ShowKeyboard = null;
    public int mFullSwitch = 0;
    public SwipeCallingView mSwipeCalling2;
    View mViewCallinFullScreen;
    View mViewCallinHalfScreen;
    View mViewShowGps;
    public TextView tvTxtName;
    public TextView tvTxtNumber;

    public class RunnableSwitchFull implements Runnable {
        public RunnableSwitchFull() {
        }

        public void run() {
            if (Page_Callin_ShowCall.this.actBt.mPageCurrent != Page_Callin_ShowCall.this.getPage()) {
                return;
            }
            if (Page_Callin_ShowCall.this.actBt.isHalf()) {
                if (Page_Callin_ShowCall.this.mViewCallinFullScreen != null) {
                    Page_Callin_ShowCall.this.mViewCallinFullScreen.setVisibility(8);
                }
                if (Page_Callin_ShowCall.this.mViewCallinHalfScreen != null) {
                    Page_Callin_ShowCall.this.mViewCallinHalfScreen.setVisibility(0);
                    return;
                }
                return;
            }
            if (Page_Callin_ShowCall.this.mViewCallinHalfScreen != null) {
                Page_Callin_ShowCall.this.mViewCallinHalfScreen.setVisibility(8);
            }
            if (Page_Callin_ShowCall.this.mViewCallinFullScreen != null) {
                Page_Callin_ShowCall.this.mViewCallinFullScreen.setVisibility(0);
            }
        }
    }

    public Page_Callin_ShowCall(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    private boolean btnClick(View view) {
        int id = view.getId();
        switch (id) {
            case 95:
                return true;
            case 96:
                App.bShowKeyFlag = false;
                return true;
            case 97:
                if (!App.getApp().isHalfScreenAble()) {
                    return true;
                }
                App.getApp().setFullScreenMode(0);
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
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
                App.getApp().mIpcObj.cmdNum(id - 103);
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
            case 120:
                IpcObj.clearKey(false);
                return true;
            default:
                return false;
        }
    }

    public void ResponseClick(View view) {
        if (bt.a().d() || App.bPop3rdPhone || App.bPop3rdPhone_YF || btnClick(view)) {
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
        this.mCallin_ShowKeyboard = getPage().getChildViewByid(93);
        this.mCallin_HideKeyboard = getPage().getChildViewByid(94);
        this.mCallin_ShowDial = getPage().getChildViewByid(92);
        this.mCallin_ShowCall = getPage().getChildViewByid(91);
        this.mViewCallinFullScreen = getPage().getChildViewByid(87);
        this.mViewCallinHalfScreen = getPage().getChildViewByid(86);
        this.mViewShowGps = getPage().getChildViewByid(97);
        this.mBtnVoice = (JButton) getPage().getChildViewByid(118);
        this.mSwipeCalling2 = (SwipeCallingView) getPage().getChildViewByid(344);
        if (this.mSwipeCalling2 != null) {
            this.mSwipeCalling2.setOnSwipeEventListner(new SwipeCallingView.setEvent() {
                public void setEventLeft() {
                    IpcObj.hang();
                }

                public void setEventRight() {
                    IpcObj.dial();
                    Page_Callin_ShowCall.this.mSwipeCalling2.setVisibility(8);
                    Page_Callin_ShowCall.this.mSwipeCalling2.removeDialView();
                }
            });
        }
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
            App.getApp().mIpcObj.updateNotify_Callin();
            if (App.mEnableHalfScreen) {
                switchFull();
                if (this.mViewShowGps == null) {
                    return;
                }
                if (App.getApp().isHalfScreenAble()) {
                    this.mViewShowGps.setVisibility(0);
                } else {
                    this.mViewShowGps.setVisibility(8);
                }
            }
        }
    }

    public void switchFull() {
        if (App.getApp().fytGetState != null) {
            Main.postRunnable_Ui(false, new RunnableSwitchFull());
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

    public void updateSwipeCallingBtns() {
        if (IpcObj.isInCall() && IpcObj.isRing()) {
            this.mSwipeCalling2.setVisibility(0);
            this.mSwipeCalling2.startCalling(true);
        }
    }
}
