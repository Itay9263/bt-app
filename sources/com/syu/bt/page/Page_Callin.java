package com.syu.bt.page;

import android.text.TextUtils;
import android.view.View;
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
import java.util.ArrayList;

public class Page_Callin extends Page {
    public static final int KEYBOARD_InCall = 2;
    public static final int KEYBOARD_SmallKeyBoard = 4;
    public static final int KEYBOARD_Talking = 3;
    public static final int KEYBORAD_Dial = 1;
    public ActBt actBt;
    JButton mBtnVoice = null;
    View mCallin_HideKeyboard = null;
    View mCallin_ShowCall = null;
    View mCallin_ShowDial = null;
    View mCallin_ShowKeyboard = null;
    public SwipeCallingView mSwipeCalling;
    View mViewCallinFullScreen;
    View mViewCallinHalfScreen;
    View mViewShowGps;

    public class RunnableSwitchFull implements Runnable {
        public RunnableSwitchFull() {
        }

        public void run() {
            if (Page_Callin.this.actBt.mPageCurrent == Page_Callin.this.getPage()) {
                if (Page_Callin.this.actBt.isHalf()) {
                    if (Page_Callin.this.mViewCallinFullScreen != null) {
                        Page_Callin.this.mViewCallinFullScreen.setVisibility(8);
                    }
                    if (Page_Callin.this.mViewCallinHalfScreen != null) {
                        Page_Callin.this.mViewCallinHalfScreen.setVisibility(0);
                    }
                } else {
                    if (Page_Callin.this.mViewCallinHalfScreen != null) {
                        Page_Callin.this.mViewCallinHalfScreen.setVisibility(8);
                    }
                    if (Page_Callin.this.mViewCallinFullScreen != null) {
                        Page_Callin.this.mViewCallinFullScreen.setVisibility(0);
                    }
                }
                switch (Bt.DATA[9]) {
                    case 3:
                        if (!App.bShowKeyFlag) {
                            Page_Callin.this.Switch2Keyboard(1);
                            break;
                        }
                        break;
                    case 4:
                        if (!App.bShowKeyFlag) {
                            Page_Callin.this.Switch2Keyboard(2);
                            break;
                        }
                        break;
                    case 5:
                        if (!App.bShowKeyFlag) {
                            Page_Callin.this.Switch2Keyboard(3);
                            break;
                        }
                        break;
                }
                if (App.bShowKeyFlag) {
                    Page_Callin.this.Switch2Keyboard(4);
                }
            }
        }
    }

    public Page_Callin(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    private boolean btnClick(View view) {
        int id = view.getId();
        switch (id) {
            case 95:
                Switch2Keyboard(4);
                return true;
            case 96:
                switch (Bt.DATA[9]) {
                    case 3:
                        Switch2Keyboard(1);
                        break;
                    case 4:
                        Switch2Keyboard(2);
                        break;
                    case 5:
                        Switch2Keyboard(3);
                        break;
                }
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
            case 348:
                this.actBt.Func_Back();
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

    public void Switch2Keyboard(int i) {
        switch (i) {
            case 1:
                if (this.mCallin_ShowDial != null) {
                    this.mCallin_ShowDial.setVisibility(0);
                }
                if (this.mCallin_ShowCall != null) {
                    this.mCallin_ShowCall.setVisibility(8);
                }
                if (this.mCallin_ShowKeyboard != null) {
                    this.mCallin_ShowKeyboard.setVisibility(8);
                }
                if (this.mCallin_HideKeyboard != null) {
                    this.mCallin_HideKeyboard.setVisibility(8);
                    break;
                }
                break;
            case 2:
                if (this.mCallin_ShowDial != null) {
                    this.mCallin_ShowDial.setVisibility(8);
                }
                if (this.mCallin_ShowCall != null) {
                    this.mCallin_ShowCall.setVisibility(0);
                }
                if (this.mCallin_ShowKeyboard != null) {
                    this.mCallin_ShowKeyboard.setVisibility(8);
                }
                if (this.mCallin_HideKeyboard != null) {
                    this.mCallin_HideKeyboard.setVisibility(8);
                    break;
                }
                break;
            case 3:
                if (this.mCallin_ShowDial != null) {
                    this.mCallin_ShowDial.setVisibility(8);
                }
                if (this.mCallin_ShowCall != null) {
                    this.mCallin_ShowCall.setVisibility(8);
                }
                if (this.mCallin_ShowKeyboard != null) {
                    this.mCallin_ShowKeyboard.setVisibility(8);
                }
                if (this.mCallin_HideKeyboard != null) {
                    this.mCallin_HideKeyboard.setVisibility(0);
                    break;
                }
                break;
            case 4:
                if (this.mCallin_ShowDial != null) {
                    this.mCallin_ShowDial.setVisibility(8);
                }
                if (this.mCallin_ShowCall != null) {
                    this.mCallin_ShowCall.setVisibility(8);
                }
                if (this.mCallin_ShowKeyboard != null) {
                    this.mCallin_ShowKeyboard.setVisibility(0);
                }
                if (this.mCallin_HideKeyboard != null) {
                    this.mCallin_HideKeyboard.setVisibility(8);
                    break;
                }
                break;
        }
        if (App.mIdCustomer != 55) {
            return;
        }
        if (i == 4) {
            App.bCallinHideKeyTextFlag = true;
        } else {
            App.bCallinHideKeyTextFlag = false;
        }
    }

    public void init() {
        this.mCallin_ShowKeyboard = getPage().getChildViewByid(28);
        this.mCallin_HideKeyboard = getPage().getChildViewByid(29);
        this.mCallin_ShowDial = getPage().getChildViewByid(26);
        this.mCallin_ShowCall = getPage().getChildViewByid(27);
        Switch2Keyboard(1);
        this.mViewCallinFullScreen = getPage().getChildViewByid(87);
        this.mViewCallinHalfScreen = getPage().getChildViewByid(86);
        this.mViewShowGps = getPage().getChildViewByid(97);
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
            App.getApp().mIpcObj.updateNotify_Callin();
            if (App.getApp().bPopBtRingSet) {
                App.getApp().popBtRingSet(false);
            }
            if (App.mEnableHalfScreen) {
                switchFull();
                if (this.mViewShowGps != null) {
                    if (App.getApp().isHalfScreenAble()) {
                        this.mViewShowGps.setVisibility(0);
                    } else {
                        this.mViewShowGps.setVisibility(8);
                    }
                }
            }
            if (App.bDelMenuFromBtAv && (jPage = this.actBt.ui.mPages.get(2)) != null && jPage.getVisibility() != 0) {
                jPage.setVisibility(0);
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
