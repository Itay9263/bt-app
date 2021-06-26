package com.syu.bt.page;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;

public class Page_Callin_HideKey extends Page {
    public ActBt actBt;
    JButton mBtnVoice = null;
    View mCallin_HideKeyboard = null;
    View mCallin_ShowCall = null;
    View mCallin_ShowDial = null;
    View mCallin_ShowKeyboard = null;
    public int mFullSwitch = 0;
    View mViewCallinFullScreen;
    View mViewCallinHalfScreen;
    View mViewShowGps;
    Page_Callin page_Callin;
    public TextView tvTxtName;
    public TextView tvTxtNumber;

    public class RunnableSwitchFull implements Runnable {
        public RunnableSwitchFull() {
        }

        public void run() {
            if (Page_Callin_HideKey.this.actBt.mPageCurrent != Page_Callin_HideKey.this.getPage()) {
                return;
            }
            if (Page_Callin_HideKey.this.actBt.isHalf()) {
                if (Page_Callin_HideKey.this.mViewCallinFullScreen != null) {
                    Page_Callin_HideKey.this.mViewCallinFullScreen.setVisibility(8);
                }
                if (Page_Callin_HideKey.this.mViewCallinHalfScreen != null) {
                    Page_Callin_HideKey.this.mViewCallinHalfScreen.setVisibility(0);
                    return;
                }
                return;
            }
            if (Page_Callin_HideKey.this.mViewCallinHalfScreen != null) {
                Page_Callin_HideKey.this.mViewCallinHalfScreen.setVisibility(8);
            }
            if (Page_Callin_HideKey.this.mViewCallinFullScreen != null) {
                Page_Callin_HideKey.this.mViewCallinFullScreen.setVisibility(0);
            }
            Page_Callin_HideKey.this.page_Callin = (Page_Callin) Page_Callin_HideKey.this.actBt.mPageCurrent.getNotify();
            switch (Bt.DATA[9]) {
                case 3:
                    if (!App.bShowKeyFlag && Page_Callin_HideKey.this.page_Callin != null) {
                        Page_Callin_HideKey.this.page_Callin.Switch2Keyboard(1);
                        break;
                    }
                case 4:
                    if (!App.bShowKeyFlag && Page_Callin_HideKey.this.page_Callin != null) {
                        Page_Callin_HideKey.this.page_Callin.Switch2Keyboard(2);
                        break;
                    }
                case 5:
                    if (!App.bShowKeyFlag && Page_Callin_HideKey.this.page_Callin != null) {
                        Page_Callin_HideKey.this.page_Callin.Switch2Keyboard(3);
                        break;
                    }
            }
            if (App.bShowKeyFlag && Page_Callin_HideKey.this.page_Callin != null) {
                Page_Callin_HideKey.this.page_Callin.Switch2Keyboard(4);
            }
        }
    }

    public Page_Callin_HideKey(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00d1, code lost:
        r0 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00f9, code lost:
        r0 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean btnClick(android.view.View r6) {
        /*
            r5 = this;
            r3 = 5
            r4 = 6
            r0 = 0
            r1 = 1
            int r2 = r6.getId()
            switch(r2) {
                case 95: goto L_0x00c6;
                case 96: goto L_0x000b;
                case 97: goto L_0x00e5;
                case 98: goto L_0x000b;
                case 99: goto L_0x000b;
                case 100: goto L_0x000b;
                case 101: goto L_0x001f;
                case 102: goto L_0x0030;
                case 103: goto L_0x000d;
                case 104: goto L_0x000d;
                case 105: goto L_0x000d;
                case 106: goto L_0x000d;
                case 107: goto L_0x000d;
                case 108: goto L_0x000d;
                case 109: goto L_0x000d;
                case 110: goto L_0x000d;
                case 111: goto L_0x000d;
                case 112: goto L_0x000d;
                case 113: goto L_0x000d;
                case 114: goto L_0x000d;
                case 115: goto L_0x0041;
                case 116: goto L_0x0082;
                case 117: goto L_0x008e;
                case 118: goto L_0x00ba;
                case 119: goto L_0x000b;
                case 120: goto L_0x001a;
                default: goto L_0x000b;
            }
        L_0x000b:
            r1 = r0
        L_0x000c:
            return r1
        L_0x000d:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            com.syu.app.ipc.IpcObj r0 = r0.mIpcObj
            int r2 = r2 + -103
            r0.cmdNum(r2)
            r0 = r1
            goto L_0x000b
        L_0x001a:
            com.syu.app.ipc.IpcObj.clearKey(r0)
            r0 = r1
            goto L_0x000b
        L_0x001f:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000c
            com.syu.app.App r0 = com.syu.app.App.getApp()
            com.syu.app.ipc.IpcObj r0 = r0.mIpcObj
            r0.FuncDial()
            r0 = r1
            goto L_0x000b
        L_0x0030:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000c
            boolean r0 = com.syu.app.ipc.IpcObj.isInCall()
            if (r0 == 0) goto L_0x00f9
            com.syu.app.ipc.IpcObj.hang()
            r0 = r1
            goto L_0x000b
        L_0x0041:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000c
            int[] r0 = com.syu.ipcself.module.main.Bt.DATA
            r2 = 9
            r0 = r0[r2]
            if (r0 == 0) goto L_0x007d
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            if (r0 == r3) goto L_0x0062
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            if (r0 == r4) goto L_0x0062
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            r2 = 7
            if (r0 == r2) goto L_0x0062
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            r2 = 8
            if (r0 != r2) goto L_0x007d
        L_0x0062:
            boolean r0 = com.syu.app.ipc.IpcObj.isInCall()
            if (r0 == 0) goto L_0x007d
            boolean r0 = com.syu.app.App.bHFP
            if (r0 == 0) goto L_0x00f9
            com.syu.bt.act.ActBt r0 = r5.actBt
            com.syu.app.App r2 = com.syu.app.App.getApp()
            java.lang.String r3 = "str_note_swith2htp"
            java.lang.String r2 = r2.getString(r3)
            r0.popDeleteContacts(r4, r2)
            r0 = r1
            goto L_0x000b
        L_0x007d:
            com.syu.app.ipc.IpcObj.linkCut()
            r0 = r1
            goto L_0x000b
        L_0x0082:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000c
            com.syu.app.ipc.IpcObj.link()
            r0 = r1
            goto L_0x000b
        L_0x008e:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000c
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            if (r0 != r3) goto L_0x00b4
            boolean r0 = com.syu.app.ipc.IpcObj.isInCall()
            if (r0 == 0) goto L_0x00b4
            boolean r0 = com.syu.app.App.bHFP
            if (r0 == 0) goto L_0x00f9
            com.syu.bt.act.ActBt r0 = r5.actBt
            com.syu.app.App r2 = com.syu.app.App.getApp()
            java.lang.String r3 = "str_note_swith2htp"
            java.lang.String r2 = r2.getString(r3)
            r0.popDeleteContacts(r4, r2)
            r0 = r1
            goto L_0x000b
        L_0x00b4:
            com.syu.app.ipc.IpcObj.cut()
            r0 = r1
            goto L_0x000b
        L_0x00ba:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000c
            com.syu.app.ipc.IpcObj.VoiceSwitch()
            r0 = r1
            goto L_0x000b
        L_0x00c6:
            com.syu.bt.act.ActBt r0 = r5.actBt
            com.syu.ctrl.JPage r0 = r0.mPageCurrent
            int r0 = r0.getId()
            switch(r0) {
                case 22: goto L_0x00d4;
                default: goto L_0x00d1;
            }
        L_0x00d1:
            r0 = r1
            goto L_0x000b
        L_0x00d4:
            com.syu.bt.act.ActBt r0 = r5.actBt
            com.syu.ctrl.JPage r0 = r0.mPageCurrent
            com.syu.page.IPageNotify r0 = r0.getNotify()
            com.syu.bt.page.Page_Callin r0 = (com.syu.bt.page.Page_Callin) r0
            if (r0 == 0) goto L_0x00f9
            r2 = 4
            r0.Switch2Keyboard(r2)
            goto L_0x00d1
        L_0x00e5:
            com.syu.app.App r2 = com.syu.app.App.getApp()
            boolean r2 = r2.isHalfScreenAble()
            if (r2 == 0) goto L_0x00f9
            com.syu.app.App r2 = com.syu.app.App.getApp()
            r2.setFullScreenMode(r0)
            r0 = r1
            goto L_0x000b
        L_0x00f9:
            r0 = r1
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.page.Page_Callin_HideKey.btnClick(android.view.View):boolean");
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
        this.mViewCallinFullScreen = getPage().getChildViewByid(87);
        this.mViewCallinHalfScreen = getPage().getChildViewByid(86);
        this.mViewShowGps = getPage().getChildViewByid(97);
        this.mBtnVoice = (JButton) getPage().getChildViewByid(118);
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
            if (this.actBt.mPageCurrent == getPage()) {
                this.page_Callin = (Page_Callin) this.actBt.mPageCurrent.getNotify();
                if (App.bShowKeyFlag && this.page_Callin != null) {
                    this.page_Callin.Switch2Keyboard(4);
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
}
