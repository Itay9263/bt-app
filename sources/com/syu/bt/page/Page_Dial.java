package com.syu.bt.page;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.os.EnvironmentCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import com.syu.app.App;
import com.syu.app.MyUiItem;
import com.syu.app.ipc.IpcObj;
import com.syu.app.ipc.Ipc_New;
import com.syu.bt.act.ActBt;
import com.syu.bt.ctrl.CircleMenuLayout;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JSeekBar;
import com.syu.ctrl.JText;
import com.syu.ctrl.JView;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import com.syu.util.FuncUtils;
import com.syu.util.Runnable_AnimDrawable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class Page_Dial extends Page {
    public static final int FLAG_ANSWER = 1;
    public static final int FLAG_DIAL = 3;
    public static final int FLAG_HANGUP = 2;
    public static final int FLAG_RING_THIRD = 4;
    public static final int FLAG_TALK_THIRD = 5;
    public JText Contact_add1;
    public JText Contact_add2;
    public JText Contact_add3;
    public JText Contact_add4;
    public JText Contact_add5;
    public JText Contact_add6;
    public JText Contact_name1;
    public JText Contact_name2;
    public JText Contact_name3;
    public JText Contact_name4;
    public JText Contact_name5;
    public JText Contact_name6;
    public ActBt actBt;
    boolean bTop = false;
    private String checkPhoneNumber;
    /* access modifiers changed from: private */
    public String dialThrdNumber = FinalChip.BSP_PLATFORM_Null;
    boolean dialThrdPhone = false;
    AnimationDrawable mAnimationDrawable;
    public int mFullSwitch = 0;
    JGridView mGridView;
    JText mHeadTime;
    private int mPosFocusCpy = -1;
    private int mPosMoveTo = -1;
    RunnableIpPhoneTimer mRunnableIpPhoneTimer = null;
    Runnable_AnimDrawable mRunnable_AnimDrawable = null;
    public Runnable mRunnable_updateSysTime = new Runnable() {
        public void run() {
            Page_Dial.this.initTime();
        }
    };
    public JSeekBar mSB_BtVol;
    View mViewContact = null;
    View mViewDialFullScreen;
    View mViewDialHalfScreen;
    View mViewDialHideKeyboard;
    View mViewDialHideNumber;
    View mViewDialShowKeyboard;
    View mViewInCall;
    View mViewNoSim = null;
    View mViewShowCall = null;
    View mViewShowCall_Third = null;
    View mViewShowDial = null;
    View mViewShowGps;
    View mViewShowHang = null;
    View mViewShowSave = null;
    View mViewShowTalk_Third = null;
    View mViewTalking = null;
    View mViewWithSim = null;
    public String strNumber;
    public JText tvDevName2;
    public JText tvDialAnswer;
    public JText tvDialHangup;
    public JText tvTxtName;
    public JText tvTxtNumber;
    public JText tvmPin2;

    public class MyTextWatcher implements TextWatcher {
        private JText jTxtNumber;

        public MyTextWatcher(JText jText) {
            this.jTxtNumber = jText;
        }

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (Page_Dial.this.mGridView != null) {
                Page_Dial.this.strNumber = this.jTxtNumber.getText().toString();
                Page_Dial.this.queryContacts(Page_Dial.this.strNumber);
            }
            if (App.mIdCustomer == 75) {
                bv.a(charSequence, Page_Dial.this.actBt);
            }
        }
    }

    public class RunnableIpPhoneTimer implements Runnable {
        public boolean bRun = true;
        public int iTimer = 0;

        public RunnableIpPhoneTimer() {
        }

        public void run() {
            if (this.bRun) {
                this.iTimer++;
                JText jText = (JText) Page_Dial.this.getPage().getChildViewByid(58);
                JText jText2 = (JText) Page_Dial.this.getPage().getChildViewByid(68);
                if (jText != null) {
                    jText.setText(String.format(Locale.US, "%02d:%02d", new Object[]{Integer.valueOf(this.iTimer / 60), Integer.valueOf(this.iTimer % 60)}));
                    if (jText2 != null) {
                        jText2.setText(jText.getText());
                    }
                }
                Main.postRunnable_Ui(true, this, 1000);
            }
        }

        public void stopRun() {
            this.bRun = false;
        }
    }

    public class RunnableSwitchFull implements Runnable {
        public RunnableSwitchFull() {
        }

        public void run() {
            boolean z = true;
            if (App.getApp().isAppTop() && App.getApp().getFullScreenMode() != 0) {
                z = false;
            }
            if (Page_Dial.this.actBt.mPageCurrent == Page_Dial.this.getPage()) {
                if (!Page_Dial.this.actBt.isHalf() || !z) {
                    if (Page_Dial.this.mViewDialHalfScreen != null) {
                        Page_Dial.this.mViewDialHalfScreen.setVisibility(8);
                    }
                    if (Page_Dial.this.mViewDialFullScreen != null) {
                        Page_Dial.this.mViewDialFullScreen.setVisibility(0);
                        return;
                    }
                    return;
                }
                if (Page_Dial.this.mViewDialFullScreen != null) {
                    Page_Dial.this.mViewDialFullScreen.setVisibility(8);
                }
                if (Page_Dial.this.mViewDialHalfScreen != null) {
                    Page_Dial.this.mViewDialHalfScreen.setVisibility(0);
                }
            } else if (!Page_Dial.this.actBt.isHalf() || !z) {
                if (!(Page_Dial.this.mViewDialHalfScreen == null || Page_Dial.this.mViewDialHalfScreen.getVisibility() == 8)) {
                    Page_Dial.this.mViewDialHalfScreen.setVisibility(8);
                }
                if (Page_Dial.this.mViewDialFullScreen != null && Page_Dial.this.mViewDialFullScreen.getVisibility() != 0) {
                    Page_Dial.this.mViewDialFullScreen.setVisibility(0);
                }
            } else {
                if (!(Page_Dial.this.mViewDialHalfScreen == null || Page_Dial.this.mViewDialHalfScreen.getVisibility() == 0)) {
                    Page_Dial.this.mViewDialHalfScreen.setVisibility(0);
                }
                if (Page_Dial.this.mViewDialFullScreen != null && Page_Dial.this.mViewDialFullScreen.getVisibility() != 8) {
                    Page_Dial.this.mViewDialFullScreen.setVisibility(8);
                }
            }
        }
    }

    public Page_Dial(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:202:0x04fb, code lost:
        r0 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean btnClick(android.view.View r7) {
        /*
            r6 = this;
            r5 = 5
            r4 = 178(0xb2, float:2.5E-43)
            r1 = 1
            r0 = 0
            r3 = 180(0xb4, float:2.52E-43)
            int r2 = r7.getId()
            switch(r2) {
                case 97: goto L_0x02eb;
                case 99: goto L_0x0156;
                case 100: goto L_0x0170;
                case 101: goto L_0x0118;
                case 102: goto L_0x01a6;
                case 103: goto L_0x0010;
                case 104: goto L_0x0010;
                case 105: goto L_0x0010;
                case 106: goto L_0x0010;
                case 107: goto L_0x0010;
                case 108: goto L_0x0010;
                case 109: goto L_0x0010;
                case 110: goto L_0x0010;
                case 111: goto L_0x0010;
                case 112: goto L_0x0010;
                case 113: goto L_0x0010;
                case 114: goto L_0x0010;
                case 115: goto L_0x020e;
                case 116: goto L_0x0253;
                case 117: goto L_0x025f;
                case 118: goto L_0x0296;
                case 119: goto L_0x02a2;
                case 120: goto L_0x00c8;
                case 121: goto L_0x02ae;
                case 122: goto L_0x02b4;
                case 123: goto L_0x02ba;
                case 133: goto L_0x0118;
                case 134: goto L_0x01a6;
                case 135: goto L_0x0296;
                case 136: goto L_0x01a6;
                case 145: goto L_0x0067;
                case 146: goto L_0x0067;
                case 147: goto L_0x0067;
                case 148: goto L_0x0067;
                case 149: goto L_0x0067;
                case 150: goto L_0x0067;
                case 151: goto L_0x0067;
                case 152: goto L_0x0067;
                case 153: goto L_0x0067;
                case 154: goto L_0x0067;
                case 155: goto L_0x0067;
                case 156: goto L_0x0067;
                case 161: goto L_0x01dd;
                case 237: goto L_0x04ed;
                case 348: goto L_0x030b;
                case 364: goto L_0x0313;
                case 365: goto L_0x0369;
                case 366: goto L_0x03bf;
                case 367: goto L_0x0417;
                case 368: goto L_0x046f;
                case 369: goto L_0x04c7;
                default: goto L_0x000e;
            }
        L_0x000e:
            r1 = r0
        L_0x000f:
            return r1
        L_0x0010:
            int r0 = com.syu.app.App.mIdCustomer
            r3 = 34
            if (r0 != r3) goto L_0x002d
            java.lang.String r0 = com.syu.ipcself.module.main.Bt.sPhoneNumber
            int r0 = r0.length()
            r3 = 17
            if (r0 >= r3) goto L_0x04fb
            com.syu.app.App r0 = com.syu.app.App.getApp()
            com.syu.app.ipc.IpcObj r0 = r0.mIpcObj
            int r2 = r2 + -103
            r0.cmdNum(r2)
            r0 = r1
            goto L_0x000e
        L_0x002d:
            boolean r0 = r6.dialThrdPhone
            if (r0 == 0) goto L_0x005a
            java.lang.String r0 = r6.dialThrdNumber
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r0 = java.lang.String.valueOf(r0)
            r3.<init>(r0)
            int r0 = r2 + -103
            java.lang.String r0 = java.lang.Integer.toString(r0)
            java.lang.StringBuilder r0 = r3.append(r0)
            java.lang.String r0 = r0.toString()
            r6.dialThrdNumber = r0
            com.syu.ctrl.JText r0 = r6.tvTxtNumber
            java.lang.String r2 = r6.dialThrdNumber
            r0.setText(r2)
            java.lang.String r0 = r6.dialThrdNumber
            r6.updatePhoneName(r0)
            r0 = r1
            goto L_0x000e
        L_0x005a:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            com.syu.app.ipc.IpcObj r0 = r0.mIpcObj
            int r2 = r2 + -103
            r0.cmdNum(r2)
            r0 = r1
            goto L_0x000e
        L_0x0067:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            com.syu.app.ipc.IpcObj r0 = r0.mIpcObj
            int r3 = r2 + -145
            r0.cmdNum(r3)
            int r0 = r2 + -145
            r2 = 10
            if (r0 != r2) goto L_0x0092
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = com.syu.app.App.sDialInput
            java.lang.String r2 = java.lang.String.valueOf(r2)
            r0.<init>(r2)
            java.lang.String r2 = "*"
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r0 = r0.toString()
            com.syu.app.App.sDialInput = r0
            r0 = r1
            goto L_0x000e
        L_0x0092:
            r2 = 11
            if (r0 != r2) goto L_0x00b0
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = com.syu.app.App.sDialInput
            java.lang.String r2 = java.lang.String.valueOf(r2)
            r0.<init>(r2)
            java.lang.String r2 = "#"
            java.lang.StringBuilder r0 = r0.append(r2)
            java.lang.String r0 = r0.toString()
            com.syu.app.App.sDialInput = r0
            r0 = r1
            goto L_0x000e
        L_0x00b0:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = com.syu.app.App.sDialInput
            java.lang.String r3 = java.lang.String.valueOf(r3)
            r2.<init>(r3)
            java.lang.StringBuilder r0 = r2.append(r0)
            java.lang.String r0 = r0.toString()
            com.syu.app.App.sDialInput = r0
            r0 = r1
            goto L_0x000e
        L_0x00c8:
            boolean r2 = defpackage.bv.a()
            if (r2 == 0) goto L_0x00d8
            boolean r2 = r6.dialThrdPhone
            if (r2 != 0) goto L_0x00d8
            com.syu.app.ipc.IpcObj.clearKey(r0)
            r0 = r1
            goto L_0x000e
        L_0x00d8:
            boolean r2 = r6.dialThrdPhone
            if (r2 == 0) goto L_0x04fb
            java.lang.String r2 = r6.dialThrdNumber
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x00f5
            r6.dialThrdPhone = r0
            java.lang.String r0 = com.syu.ipcself.module.main.Bt.sPhoneNumber
            r6.updatePhoneName(r0)
            com.syu.ctrl.JText r0 = r6.tvTxtNumber
            java.lang.String r2 = com.syu.ipcself.module.main.Bt.sPhoneNumber
            r0.setText(r2)
            r0 = r1
            goto L_0x000e
        L_0x00f5:
            java.lang.String r2 = r6.dialThrdNumber
            java.lang.String r3 = r6.dialThrdNumber
            int r3 = r3.length()
            int r3 = r3 + -1
            java.lang.CharSequence r0 = r2.subSequence(r0, r3)
            java.lang.String r0 = r0.toString()
            r6.dialThrdNumber = r0
            java.lang.String r0 = r6.dialThrdNumber
            r6.updatePhoneName(r0)
            com.syu.ctrl.JText r0 = r6.tvTxtNumber
            java.lang.String r2 = r6.dialThrdNumber
            r0.setText(r2)
            r0 = r1
            goto L_0x000e
        L_0x0118:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            boolean r0 = r6.dialThrdPhone
            if (r0 == 0) goto L_0x0132
            java.lang.String r0 = r6.dialThrdNumber
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0132
            java.lang.String r0 = r6.dialThrdNumber
            com.syu.app.ipc.IpcObj.dial(r0)
            r0 = r1
            goto L_0x000e
        L_0x0132:
            java.lang.String r0 = com.syu.app.App.strNumberBak
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x014a
            java.lang.String r0 = com.syu.ipcself.module.main.Bt.sPhoneNumber
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x014a
            java.lang.String r0 = com.syu.app.App.strNumberBak
            com.syu.app.ipc.IpcObj.setNum(r0)
            r0 = r1
            goto L_0x000e
        L_0x014a:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            com.syu.app.ipc.IpcObj r0 = r0.mIpcObj
            r0.FuncDial()
            r0 = r1
            goto L_0x000e
        L_0x0156:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            com.syu.app.App r0 = com.syu.app.App.getApp()
            com.syu.app.ipc.IpcObj r0 = r0.mIpcObj
            boolean r0 = r0.FuncDialIpPhone()
            if (r0 == 0) goto L_0x04fb
            com.syu.bt.act.ActBt r0 = r6.actBt
            r0.showProgressIpPhoneDlg()
            r0 = r1
            goto L_0x000e
        L_0x0170:
            boolean r2 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r2 != 0) goto L_0x000f
            boolean r2 = com.syu.app.ipc.IpcObj.isTalk()
            if (r2 == 0) goto L_0x0191
            boolean r2 = r6.dialThrdPhone
            if (r2 != 0) goto L_0x0191
            r6.dialThrdPhone = r1
            com.syu.ctrl.JText r0 = r6.tvTxtNumber
            java.lang.String r2 = r6.dialThrdNumber
            r0.setText(r2)
            java.lang.String r0 = r6.dialThrdNumber
            r6.updatePhoneName(r0)
            r0 = r1
            goto L_0x000e
        L_0x0191:
            boolean r2 = r6.dialThrdPhone
            if (r2 == 0) goto L_0x04fb
            r6.dialThrdPhone = r0
            com.syu.ctrl.JText r0 = r6.tvTxtNumber
            java.lang.String r2 = com.syu.ipcself.module.main.Bt.sPhoneNumber
            r0.setText(r2)
            java.lang.String r0 = com.syu.ipcself.module.main.Bt.sPhoneNumber
            r6.updatePhoneName(r0)
            r0 = r1
            goto L_0x000e
        L_0x01a6:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            com.syu.app.ipc.IpcObj.hangIpPhone()
            r6.stopIpPhoneTimer()
            boolean r0 = r6.dialThrdPhone
            if (r0 == 0) goto L_0x01c5
            int[] r0 = com.syu.ipcself.module.main.Bt.DATA
            r2 = 9
            r0 = r0[r2]
            r2 = 3
            if (r0 != r2) goto L_0x01c5
            com.syu.app.ipc.Ipc_New.hang3rdPhoneCurrent()
            r0 = r1
            goto L_0x000e
        L_0x01c5:
            boolean r0 = com.syu.app.ipc.IpcObj.isInCall()
            if (r0 == 0) goto L_0x01d1
            com.syu.app.ipc.IpcObj.hang()
            r0 = r1
            goto L_0x000e
        L_0x01d1:
            boolean r0 = defpackage.bv.k()
            if (r0 == 0) goto L_0x04fb
            com.syu.app.ipc.IpcObj.hang()
            r0 = r1
            goto L_0x000e
        L_0x01dd:
            boolean r0 = com.syu.app.ipc.IpcObj.isInCall()
            if (r0 == 0) goto L_0x01e6
            r0 = r1
            goto L_0x000e
        L_0x01e6:
            android.view.View r0 = r6.mViewShowSave
            if (r0 == 0) goto L_0x04fb
            r6.showLoveNumber()
            android.view.View r0 = r6.mViewShowSave
            android.view.ViewPropertyAnimator r0 = r0.animate()
            r2 = 1065353216(0x3f800000, float:1.0)
            android.view.ViewPropertyAnimator r0 = r0.alpha(r2)
            r2 = 300(0x12c, double:1.48E-321)
            android.view.ViewPropertyAnimator r0 = r0.setDuration(r2)
            android.view.ViewPropertyAnimator r0 = r0.withLayer()
            com.syu.bt.page.Page_Dial$3 r2 = new com.syu.bt.page.Page_Dial$3
            r2.<init>()
            r0.withStartAction(r2)
            r0 = r1
            goto L_0x000e
        L_0x020e:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            int[] r0 = com.syu.ipcself.module.main.Bt.DATA
            r2 = 9
            r0 = r0[r2]
            if (r0 == 0) goto L_0x024d
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            if (r0 == r5) goto L_0x0230
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            r2 = 6
            if (r0 == r2) goto L_0x0230
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            r2 = 7
            if (r0 == r2) goto L_0x0230
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            r2 = 8
            if (r0 != r2) goto L_0x024d
        L_0x0230:
            boolean r0 = com.syu.app.ipc.IpcObj.isInCall()
            if (r0 == 0) goto L_0x024d
            boolean r0 = com.syu.app.App.bHFP
            if (r0 == 0) goto L_0x04fb
            com.syu.bt.act.ActBt r0 = r6.actBt
            r2 = 6
            com.syu.app.App r3 = com.syu.app.App.getApp()
            java.lang.String r4 = "str_note_swith2htp"
            java.lang.String r3 = r3.getString(r4)
            r0.popDeleteContacts(r2, r3)
            r0 = r1
            goto L_0x000e
        L_0x024d:
            com.syu.app.ipc.IpcObj.linkCut()
            r0 = r1
            goto L_0x000e
        L_0x0253:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            com.syu.app.ipc.IpcObj.link()
            r0 = r1
            goto L_0x000e
        L_0x025f:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            if (r0 == r5) goto L_0x0273
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            r2 = 6
            if (r0 == r2) goto L_0x0273
            int r0 = com.syu.ipcself.module.main.Main.mConf_PlatForm
            r2 = 7
            if (r0 != r2) goto L_0x0290
        L_0x0273:
            boolean r0 = com.syu.app.ipc.IpcObj.isInCall()
            if (r0 == 0) goto L_0x0290
            boolean r0 = com.syu.app.App.bHFP
            if (r0 == 0) goto L_0x04fb
            com.syu.bt.act.ActBt r0 = r6.actBt
            r2 = 6
            com.syu.app.App r3 = com.syu.app.App.getApp()
            java.lang.String r4 = "str_note_swith2htp"
            java.lang.String r3 = r3.getString(r4)
            r0.popDeleteContacts(r2, r3)
            r0 = r1
            goto L_0x000e
        L_0x0290:
            com.syu.app.ipc.IpcObj.cut()
            r0 = r1
            goto L_0x000e
        L_0x0296:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            com.syu.app.ipc.IpcObj.VoiceSwitch()
            r0 = r1
            goto L_0x000e
        L_0x02a2:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            com.syu.app.ipc.IpcObj.MicSwitch()
            r0 = r1
            goto L_0x000e
        L_0x02ae:
            r6.EnableHideKeyboard(r1)
            r0 = r1
            goto L_0x000e
        L_0x02b4:
            r6.EnableHideKeyboard(r0)
            r0 = r1
            goto L_0x000e
        L_0x02ba:
            boolean r2 = defpackage.bv.c()
            if (r2 == 0) goto L_0x02e0
            android.view.View r2 = r6.mViewDialHideNumber
            if (r2 == 0) goto L_0x04fb
            android.view.View r2 = r6.mViewDialHideNumber
            int r2 = r2.getVisibility()
            r3 = 8
            if (r2 != r3) goto L_0x02d6
            android.view.View r2 = r6.mViewDialHideNumber
            r2.setVisibility(r0)
            r0 = r1
            goto L_0x000e
        L_0x02d6:
            android.view.View r0 = r6.mViewDialHideNumber
            r2 = 8
            r0.setVisibility(r2)
            r0 = r1
            goto L_0x000e
        L_0x02e0:
            java.lang.String r0 = ""
            com.syu.app.ipc.IpcObj.setNum(r0)
            r6.EnableHideNumber(r1)
            r0 = r1
            goto L_0x000e
        L_0x02eb:
            boolean r2 = defpackage.bv.d()
            if (r2 == 0) goto L_0x02f7
            com.syu.app.ipc.IpcObj.SetJumpNavi()
            r0 = r1
            goto L_0x000e
        L_0x02f7:
            com.syu.app.App r2 = com.syu.app.App.getApp()
            boolean r2 = r2.isHalfScreenAble()
            if (r2 == 0) goto L_0x04fb
            com.syu.app.App r2 = com.syu.app.App.getApp()
            r2.setFullScreenMode(r0)
            r0 = r1
            goto L_0x000e
        L_0x030b:
            com.syu.bt.act.ActBt r0 = r6.actBt
            r0.Func_Back()
            r0 = r1
            goto L_0x000e
        L_0x0313:
            boolean r2 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r2 != 0) goto L_0x000f
            boolean r2 = defpackage.bv.c()
            if (r2 == 0) goto L_0x0349
            java.lang.String r0 = "contact_save1"
            android.util.SparseArray r2 = defpackage.bz.a(r0)
            java.lang.Object r0 = r2.get(r4)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x0349:
            com.syu.bt.Bt_Info r2 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r2 = r2.mListFavContact
            int r2 = r2.size()
            if (r2 <= 0) goto L_0x04fb
            com.syu.bt.Bt_Info r2 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r2 = r2.mListFavContact
            java.lang.Object r0 = r2.get(r0)
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x0369:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            boolean r0 = defpackage.bv.c()
            if (r0 == 0) goto L_0x039f
            java.lang.String r0 = "contact_save2"
            android.util.SparseArray r2 = defpackage.bz.a(r0)
            java.lang.Object r0 = r2.get(r4)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x039f:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            int r0 = r0.size()
            if (r0 <= r1) goto L_0x04fb
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            java.lang.Object r0 = r0.get(r1)
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x03bf:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            boolean r0 = defpackage.bv.c()
            if (r0 == 0) goto L_0x03f5
            java.lang.String r0 = "contact_save3"
            android.util.SparseArray r2 = defpackage.bz.a(r0)
            java.lang.Object r0 = r2.get(r4)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x03f5:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            int r0 = r0.size()
            r2 = 2
            if (r0 <= r2) goto L_0x04fb
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            r2 = 2
            java.lang.Object r0 = r0.get(r2)
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x0417:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            boolean r0 = defpackage.bv.c()
            if (r0 == 0) goto L_0x044d
            java.lang.String r0 = "contact_save4"
            android.util.SparseArray r2 = defpackage.bz.a(r0)
            java.lang.Object r0 = r2.get(r4)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x044d:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            int r0 = r0.size()
            r2 = 3
            if (r0 <= r2) goto L_0x04fb
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            r2 = 3
            java.lang.Object r0 = r0.get(r2)
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x046f:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            boolean r0 = defpackage.bv.c()
            if (r0 == 0) goto L_0x04a5
            java.lang.String r0 = "contact_save5"
            android.util.SparseArray r2 = defpackage.bz.a(r0)
            java.lang.Object r0 = r2.get(r4)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x04fb
            java.lang.Object r0 = r2.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x04a5:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            int r0 = r0.size()
            r2 = 4
            if (r0 <= r2) goto L_0x04fb
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            r2 = 4
            java.lang.Object r0 = r0.get(r2)
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x04c7:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x000f
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            int r0 = r0.size()
            if (r0 <= r5) goto L_0x04fb
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListFavContact
            java.lang.Object r0 = r0.get(r5)
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            r0 = r1
            goto L_0x000e
        L_0x04ed:
            com.syu.ipcself.ModuleProxy r2 = com.syu.ipcself.module.main.Sound.PROXY
            com.syu.ctrl.JSeekBar r3 = r6.mSB_BtVol
            int r3 = r3.getValue()
            r2.cmd((int) r0, (int) r3)
            r0 = r1
            goto L_0x000e
        L_0x04fb:
            r0 = r1
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.page.Page_Dial.btnClick(android.view.View):boolean");
    }

    private boolean btnClick_Third(View view) {
        switch (view.getId()) {
            case 102:
                if (!App.bPop3rdPhone && !App.bPop3rdPhone_YF) {
                    return false;
                }
                if (!Ipc_New.isTalk()) {
                    return true;
                }
                Ipc_New.hang3rdPhoneCurrent();
                return true;
            case 118:
            case 135:
                if (FuncUtils.isFastDoubleClick()) {
                    return true;
                }
                IpcObj.VoiceSwitch();
                return true;
            case 119:
                if (FuncUtils.isFastDoubleClick()) {
                    return true;
                }
                IpcObj.MicSwitch();
                return true;
            case 125:
            case 127:
                Ipc_New.switch3rdPhone();
                return true;
            case 126:
                if (Ipc_New.isRing()) {
                    Ipc_New.hang3rdPhoneHold();
                    return true;
                }
                Ipc_New.hang3rdPhoneCurrent();
                return true;
            case 134:
            case 136:
                if (!App.bPop3rdPhone && !App.bPop3rdPhone_YF) {
                    return false;
                }
                if (Ipc_New.isRing()) {
                    Ipc_New.hang3rdPhoneHold();
                    return true;
                }
                Ipc_New.hang3rdPhoneCurrent();
                return true;
            default:
                return false;
        }
    }

    private void initMyInfo() {
        if (bv.c()) {
            this.mHeadTime = (JText) getPage().getChildViewByid(351);
            this.mViewTalking = getPage().getChildViewByid(349);
            this.mViewContact = getPage().getChildViewByid(350);
            this.Contact_add1 = (JText) getPage().getChildViewByid(352);
            this.Contact_add2 = (JText) getPage().getChildViewByid(353);
            this.Contact_add3 = (JText) getPage().getChildViewByid(354);
            this.Contact_add4 = (JText) getPage().getChildViewByid(355);
            this.Contact_add5 = (JText) getPage().getChildViewByid(356);
            this.Contact_name1 = (JText) getPage().getChildViewByid(358);
            this.Contact_name2 = (JText) getPage().getChildViewByid(359);
            this.Contact_name3 = (JText) getPage().getChildViewByid(360);
            this.Contact_name4 = (JText) getPage().getChildViewByid(361);
            this.Contact_name5 = (JText) getPage().getChildViewByid(362);
        } else if (bv.e()) {
            this.Contact_add1 = (JText) getPage().getChildViewByid(352);
            this.Contact_add2 = (JText) getPage().getChildViewByid(353);
            this.Contact_add3 = (JText) getPage().getChildViewByid(354);
            this.Contact_add4 = (JText) getPage().getChildViewByid(355);
            this.Contact_add5 = (JText) getPage().getChildViewByid(356);
            this.Contact_add6 = (JText) getPage().getChildViewByid(357);
            this.Contact_name1 = (JText) getPage().getChildViewByid(358);
            this.Contact_name2 = (JText) getPage().getChildViewByid(359);
            this.Contact_name3 = (JText) getPage().getChildViewByid(360);
            this.Contact_name4 = (JText) getPage().getChildViewByid(361);
            this.Contact_name5 = (JText) getPage().getChildViewByid(362);
            this.Contact_name6 = (JText) getPage().getChildViewByid(363);
            if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
                this.Contact_add1.setTypeface(bt.a((Context) this.actBt));
                this.Contact_add2.setTypeface(bt.a((Context) this.actBt));
                this.Contact_add3.setTypeface(bt.a((Context) this.actBt));
                this.Contact_add4.setTypeface(bt.a((Context) this.actBt));
                this.Contact_add5.setTypeface(bt.a((Context) this.actBt));
                this.Contact_add6.setTypeface(bt.a((Context) this.actBt));
                this.Contact_name1.setTypeface(bt.a((Context) this.actBt));
                this.Contact_name2.setTypeface(bt.a((Context) this.actBt));
                this.Contact_name3.setTypeface(bt.a((Context) this.actBt));
                this.Contact_name4.setTypeface(bt.a((Context) this.actBt));
                this.Contact_name5.setTypeface(bt.a((Context) this.actBt));
                this.Contact_name6.setTypeface(bt.a((Context) this.actBt));
            }
        }
    }

    public void EnableHideKeyboard(boolean z) {
        if (z) {
            if (this.mViewDialHideKeyboard != null) {
                this.mViewDialHideKeyboard.setVisibility(0);
            }
            if (this.mViewDialShowKeyboard != null) {
                this.mViewDialShowKeyboard.setVisibility(8);
                return;
            }
            return;
        }
        if (this.mViewDialHideKeyboard != null) {
            this.mViewDialHideKeyboard.setVisibility(8);
        }
        if (this.mViewDialShowKeyboard != null) {
            this.mViewDialShowKeyboard.setVisibility(0);
        }
    }

    public void EnableHideNumber(boolean z) {
        if (z) {
            if (this.mViewDialHideNumber != null) {
                this.mViewDialHideNumber.setVisibility(8);
            }
            if (this.mGridView != null) {
                this.mGridView.setVisibility(8);
                return;
            }
            return;
        }
        if (this.mViewDialHideNumber != null) {
            this.mViewDialHideNumber.setVisibility(0);
        }
        if (this.mGridView != null) {
            this.mGridView.setVisibility(0);
        }
    }

    public void GridClick(JGridView jGridView) {
        switch (jGridView.getId()) {
            case 157:
                if (!FuncUtils.isFastDoubleClick()) {
                    SparseArray sparseArray = jGridView.getList().get(jGridView.getPosition());
                    if (!TextUtils.isEmpty((CharSequence) sparseArray.get(180))) {
                        IpcObj.itemDial((String) sparseArray.get(180));
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void GridPressed(JGridView jGridView, JPage jPage, boolean z) {
        if (jGridView.mPageInfos != null) {
            Iterator<WeakReference<JPage>> it = jGridView.mPageInfos.iterator();
            while (it.hasNext()) {
                JPage jPage2 = (JPage) it.next().get();
                if (jPage2 != null) {
                    pressGridItem(jPage2, jPage, z);
                }
            }
        }
    }

    public void HideOrShowNumberPad() {
        int i = 0;
        if (this.mViewDialHideNumber != null && !bv.c()) {
            this.mViewDialHideNumber.setVisibility(Bt.sPhoneNumber.length() != 0 ? 0 : 8);
        }
        if (this.mGridView != null) {
            JGridView jGridView = this.mGridView;
            if (Bt.sPhoneNumber.length() == 0 || App.mBtInfo.mListContact.size() <= 0) {
                i = 8;
            }
            jGridView.setVisibility(i);
        }
    }

    public void InitGridItem(JGridView jGridView, JPage jPage, SparseArray<String> sparseArray, int i) {
        switch (jGridView.getId()) {
            case 157:
                View childViewByid = jPage.getChildViewByid(158);
                if (childViewByid != null) {
                    if (sparseArray.size() == 0) {
                        childViewByid.setVisibility(8);
                    } else {
                        childViewByid.setVisibility(0);
                    }
                }
                if (sparseArray.size() > 0) {
                    JText jText = (JText) jPage.getChildViewByid(160);
                    JText jText2 = (JText) jPage.getChildViewByid(159);
                    String str = sparseArray.get(180);
                    String str2 = sparseArray.get(178);
                    if (jText != null) {
                        jText.setText(str);
                    }
                    if (jText2 != null) {
                        jText2.setText(str2);
                    }
                    if (!TextUtils.isEmpty(str) && this.mPosMoveTo >= 0 && this.mPosMoveTo == i) {
                        this.checkPhoneNumber = str;
                        pressGridItem(jPage, jPage, true);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void ResponseClick(View view) {
        if (bt.a().d() || btnClick_Third(view) || App.bPop3rdPhone || App.bPop3rdPhone_YF || btnClick(view)) {
            return;
        }
        if (!App.getApp().mIpcObj.isCalling() || (this.dialThrdPhone && view.getId() == 36)) {
            if (this.actBt.MenuClick(view)) {
            }
        } else {
            cb.a().a(App.getApp().getString("bt_state_using"));
        }
    }

    public boolean ResponseLongClick(View view) {
        switch (view.getId()) {
            case 103:
                IpcObj.LongClick0();
                break;
            case 120:
                if (!bv.a() || this.dialThrdPhone) {
                    if (this.dialThrdPhone) {
                        this.dialThrdNumber = FinalChip.BSP_PLATFORM_Null;
                        updatePhoneName(this.dialThrdNumber);
                        this.tvTxtNumber.setText(this.dialThrdNumber);
                        break;
                    }
                } else {
                    IpcObj.clearKey(true);
                    break;
                }
                break;
            case 364:
                if (bv.c()) {
                    if (TextUtils.isEmpty(bz.a("contact_save1").get(178))) {
                        if (IpcObj.isConnect() && App.mBtInfo.mListContact.size() > 0) {
                            App.mSelectContact = 1;
                            this.actBt.goPage(5, false);
                            break;
                        }
                    } else {
                        App.mSelectContact = 1;
                        this.actBt.popDeleteContacts(10, App.getApp().getString("bt_is_delete_one"));
                        break;
                    }
                }
                break;
            case 365:
                if (bv.c()) {
                    if (TextUtils.isEmpty(bz.a("contact_save2").get(178))) {
                        if (IpcObj.isConnect() && App.mBtInfo.mListContact.size() > 0) {
                            App.mSelectContact = 2;
                            this.actBt.goPage(5, false);
                            break;
                        }
                    } else {
                        App.mSelectContact = 2;
                        this.actBt.popDeleteContacts(10, App.getApp().getString("bt_is_delete_one"));
                        break;
                    }
                }
                break;
            case 366:
                if (bv.c()) {
                    if (TextUtils.isEmpty(bz.a("contact_save3").get(178))) {
                        if (IpcObj.isConnect() && App.mBtInfo.mListContact.size() > 0) {
                            App.mSelectContact = 3;
                            this.actBt.goPage(5, false);
                            break;
                        }
                    } else {
                        App.mSelectContact = 3;
                        this.actBt.popDeleteContacts(10, App.getApp().getString("bt_is_delete_one"));
                        break;
                    }
                }
                break;
            case 367:
                if (bv.c()) {
                    if (TextUtils.isEmpty(bz.a("contact_save4").get(178))) {
                        if (IpcObj.isConnect() && App.mBtInfo.mListContact.size() > 0) {
                            App.mSelectContact = 4;
                            this.actBt.goPage(5, false);
                            break;
                        }
                    } else {
                        App.mSelectContact = 4;
                        this.actBt.popDeleteContacts(10, App.getApp().getString("bt_is_delete_one"));
                        break;
                    }
                }
                break;
            case 368:
                if (bv.c()) {
                    if (TextUtils.isEmpty(bz.a("contact_save5").get(178))) {
                        if (IpcObj.isConnect() && App.mBtInfo.mListContact.size() > 0) {
                            App.mSelectContact = 5;
                            this.actBt.goPage(5, false);
                            break;
                        }
                    } else {
                        App.mSelectContact = 5;
                        this.actBt.popDeleteContacts(10, App.getApp().getString("bt_is_delete_one"));
                        break;
                    }
                }
                break;
        }
        return false;
    }

    public void controlDialInfo(int i) {
        if (bv.c()) {
            showLoveNumber();
            switch (i) {
                case 0:
                case 1:
                case 6:
                    updateSimLayout(0);
                    EnableHideNumber(true);
                    if (this.mViewTalking != null) {
                        this.mViewTalking.setVisibility(8);
                    }
                    if (this.mViewContact != null) {
                        this.mViewContact.setVisibility(0);
                        return;
                    }
                    return;
                case 2:
                    updateSimLayout(1);
                    EnableHideNumber(true);
                    if (this.mViewTalking != null) {
                        this.mViewTalking.setVisibility(8);
                    }
                    if (this.mViewContact != null) {
                        this.mViewContact.setVisibility(0);
                        return;
                    }
                    return;
                case 3:
                case 4:
                case 5:
                    updateSimLayout(1);
                    if (this.mViewTalking != null) {
                        this.mViewTalking.setVisibility(0);
                    }
                    if (this.mViewContact != null) {
                        this.mViewContact.setVisibility(8);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public boolean dialByKey() {
        if (!this.bTop) {
            return false;
        }
        if (Bt.DATA[9] == 4) {
            Bt.PROXY.cmd(9);
        } else if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
            IpcObj.redial();
        } else {
            IpcObj.itemDial(Bt.sPhoneNumber);
        }
        return true;
    }

    public void disableSaveView(boolean z) {
        boolean z2;
        int i = 0;
        if (bv.e() && App.mStrCustomer.equals("TZY4")) {
            JButton jButton = (JButton) getPage().getChildViewByid(102);
            JButton jButton2 = App.mStrCustomer.equals("TZY_NEW") ? (JButton) getPage().getChildViewByid(101) : (JButton) getPage().getChildViewByid(161);
            switch (Bt.DATA[9]) {
                case 2:
                    z2 = true;
                    break;
                default:
                    z2 = false;
                    break;
            }
            if (jButton != null) {
                jButton.setVisibility(z2 ? 8 : 0);
            }
            if (jButton2 != null) {
                if (!z2) {
                    i = 8;
                }
                jButton2.setVisibility(i);
            }
        }
        if (this.mViewShowSave != null && this.mViewShowSave.getVisibility() == 0) {
            if (z) {
                this.mViewShowSave.animate().alpha(0.0f).setDuration(300).withLayer().withEndAction(new Runnable() {
                    public void run() {
                        Page_Dial.this.mViewShowSave.setVisibility(8);
                    }
                });
                return;
            }
            this.mViewShowSave.animate().alpha(0.0f).withLayer();
            this.mViewShowSave.setVisibility(8);
        }
    }

    public boolean getSaveViewState() {
        return this.mViewShowSave != null && this.mViewShowSave.getVisibility() == 0;
    }

    public void hideInCallAnim() {
        if (this.mViewInCall != null && this.mViewInCall.getVisibility() != 8) {
            this.mViewInCall.setVisibility(8);
            if (this.mRunnable_AnimDrawable != null) {
                Main.removeRunnable_Ui(this.mRunnable_AnimDrawable);
                this.mRunnable_AnimDrawable.stopRun();
                this.mRunnable_AnimDrawable = null;
            }
        }
    }

    public void init() {
        String[] strDrawableExtra;
        this.mViewNoSim = getPage().getChildViewByid(128);
        this.mViewWithSim = getPage().getChildViewByid(129);
        this.mViewDialFullScreen = getPage().getChildViewByid(141);
        this.mViewDialHalfScreen = getPage().getChildViewByid(140);
        this.mViewShowGps = getPage().getChildViewByid(97);
        this.mViewDialHideNumber = getPage().getChildViewByid(142);
        this.mViewDialHideKeyboard = getPage().getChildViewByid(143);
        this.mViewDialShowKeyboard = getPage().getChildViewByid(144);
        EnableHideNumber(true);
        EnableHideKeyboard(false);
        updateSimLayout(0);
        this.mViewShowDial = getPage().getChildViewByid(130);
        this.mViewShowCall = getPage().getChildViewByid(131);
        this.mViewShowHang = getPage().getChildViewByid(132);
        this.mViewShowCall_Third = getPage().getChildViewByid(137);
        this.mViewShowTalk_Third = getPage().getChildViewByid(138);
        this.mViewShowSave = getPage().getChildViewByid(139);
        this.tvDialAnswer = (JText) getPage().getChildViewByid(71);
        this.tvDialHangup = (JText) getPage().getChildViewByid(72);
        this.tvDevName2 = (JText) getPage().getChildViewByid(264);
        if (this.tvDevName2 != null) {
            this.tvDevName2.setText(Bt.sDevName);
        }
        this.tvmPin2 = (JText) getPage().getChildViewByid(266);
        if (this.tvmPin2 != null) {
            this.tvmPin2.setText(Bt.sDevPin);
        }
        this.tvTxtNumber = (JText) getPage().getChildViewByid(56);
        if (this.tvTxtNumber != null) {
            this.tvTxtNumber.addTextChangedListener(new MyTextWatcher(this.tvTxtNumber));
        }
        this.mGridView = (JGridView) getPage().getChildViewByid(157);
        this.mViewInCall = getPage().getChildViewByid(76);
        if (!(this.mViewInCall == null || (strDrawableExtra = ((MyUiItem) this.mViewInCall.getTag()).getStrDrawableExtra()) == null || strDrawableExtra.length <= 0)) {
            this.mAnimationDrawable = (AnimationDrawable) this.actBt.ui.getDrawableFromPath(strDrawableExtra[0]);
        }
        initMyInfo();
        if (bv.h()) {
            this.mSB_BtVol = (JSeekBar) getPage().getChildViewByid(237);
            if (this.mSB_BtVol != null) {
                this.mSB_BtVol.setProgressMax(36);
            }
        }
        if (bv.j()) {
            initMenuLayout();
        }
        setButtonColor(App.color);
        updateShowDial();
        initTypeface();
    }

    public void initMenuLayout() {
        CircleMenuLayout circleMenuLayout = (CircleMenuLayout) getPage().getChildViewByid(77);
        if (circleMenuLayout != null) {
            circleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
                public void itemClick(View view, int i) {
                    switch (i) {
                        case 101:
                            if (Page_Dial.this.dialThrdPhone && !TextUtils.isEmpty(Page_Dial.this.dialThrdNumber)) {
                                IpcObj.dial(Page_Dial.this.dialThrdNumber);
                                return;
                            } else if (TextUtils.isEmpty(App.strNumberBak) || !TextUtils.isEmpty(Bt.sPhoneNumber)) {
                                App.getApp().mIpcObj.FuncDial();
                                return;
                            } else {
                                IpcObj.setNum(App.strNumberBak);
                                return;
                            }
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
                            JText jText = (JText) Page_Dial.this.getPage().getChildViewByid(78);
                            if (jText != null) {
                                if (i - 103 == 10) {
                                    jText.setText("*");
                                } else if (i - 103 == 11) {
                                    jText.setText("#");
                                } else {
                                    jText.setText(Integer.toString(i - 103));
                                }
                            }
                            if (Page_Dial.this.dialThrdPhone) {
                                Page_Dial page_Dial = Page_Dial.this;
                                page_Dial.dialThrdNumber = String.valueOf(page_Dial.dialThrdNumber) + Integer.toString(i - 103);
                                Page_Dial.this.tvTxtNumber.setText(Page_Dial.this.dialThrdNumber);
                                Page_Dial.this.updatePhoneName(Page_Dial.this.dialThrdNumber);
                                return;
                            }
                            App.getApp().mIpcObj.cmdNum(i - 103);
                            return;
                        case 120:
                            if (bv.a() && !Page_Dial.this.dialThrdPhone) {
                                IpcObj.clearKey(false);
                                return;
                            } else if (!Page_Dial.this.dialThrdPhone) {
                                return;
                            } else {
                                if (TextUtils.isEmpty(Page_Dial.this.dialThrdNumber)) {
                                    Page_Dial.this.dialThrdPhone = false;
                                    Page_Dial.this.updatePhoneName(Bt.sPhoneNumber);
                                    Page_Dial.this.tvTxtNumber.setText(Bt.sPhoneNumber);
                                    return;
                                }
                                Page_Dial.this.dialThrdNumber = Page_Dial.this.dialThrdNumber.subSequence(0, Page_Dial.this.dialThrdNumber.length() - 1).toString();
                                Page_Dial.this.updatePhoneName(Page_Dial.this.dialThrdNumber);
                                Page_Dial.this.tvTxtNumber.setText(Page_Dial.this.dialThrdNumber);
                                return;
                            }
                        default:
                            return;
                    }
                }
            });
            circleMenuLayout.setOnMenuItemLongClickListener(new CircleMenuLayout.OnMenuItemLongClickListener() {
                public void itemLongClick(View view, int i) {
                    switch (i) {
                        case 101:
                            if (Page_Dial.this.dialThrdPhone && !TextUtils.isEmpty(Page_Dial.this.dialThrdNumber)) {
                                IpcObj.dial(Page_Dial.this.dialThrdNumber);
                                return;
                            } else if (TextUtils.isEmpty(App.strNumberBak) || !TextUtils.isEmpty(Bt.sPhoneNumber)) {
                                App.getApp().mIpcObj.FuncDial();
                                return;
                            } else {
                                IpcObj.setNum(App.strNumberBak);
                                return;
                            }
                        case 103:
                            IpcObj.LongClick0();
                            JText jText = (JText) Page_Dial.this.getPage().getChildViewByid(78);
                            if (jText != null) {
                                jText.setText("+");
                                return;
                            }
                            return;
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
                            JText jText2 = (JText) Page_Dial.this.getPage().getChildViewByid(78);
                            if (jText2 != null) {
                                if (i - 103 == 10) {
                                    jText2.setText("*");
                                } else if (i - 103 == 11) {
                                    jText2.setText("#");
                                } else {
                                    jText2.setText(Integer.toString(i - 103));
                                }
                            }
                            if (Page_Dial.this.dialThrdPhone) {
                                Page_Dial page_Dial = Page_Dial.this;
                                page_Dial.dialThrdNumber = String.valueOf(page_Dial.dialThrdNumber) + Integer.toString(i - 103);
                                Page_Dial.this.tvTxtNumber.setText(Page_Dial.this.dialThrdNumber);
                                Page_Dial.this.updatePhoneName(Page_Dial.this.dialThrdNumber);
                                return;
                            }
                            App.getApp().mIpcObj.cmdNum(i - 103);
                            return;
                        case 120:
                            if (bv.a() && !Page_Dial.this.dialThrdPhone) {
                                IpcObj.clearKey(true);
                                return;
                            } else if (Page_Dial.this.dialThrdPhone) {
                                Page_Dial.this.dialThrdNumber = FinalChip.BSP_PLATFORM_Null;
                                Page_Dial.this.updatePhoneName(Page_Dial.this.dialThrdNumber);
                                Page_Dial.this.tvTxtNumber.setText(Page_Dial.this.dialThrdNumber);
                                return;
                            } else {
                                return;
                            }
                        default:
                            return;
                    }
                }
            });
        }
    }

    public void initTime() {
        if (this.mHeadTime != null) {
            CharSequence format = DateFormat.is24HourFormat(App.getApp()) ? DateFormat.format("k:mm", Calendar.getInstance()) : DateFormat.format("h:mm", Calendar.getInstance());
            if (format.subSequence(0, 1).equals("0")) {
                format = "0" + format;
            }
            this.mHeadTime.setText(format);
        }
    }

    public void initTypeface() {
        if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
            JText jText = (JText) getPage().getChildViewByid(59);
            if (jText != null) {
                jText.setTypeface(bt.a((Context) this.actBt));
            }
            JText jText2 = (JText) getPage().getChildViewByid(55);
            if (jText2 != null) {
                jText2.setTypeface(bt.a((Context) this.actBt));
            }
            JText jText3 = (JText) getPage().getChildViewByid(58);
            if (jText3 != null) {
                jText3.setTypeface(bt.a((Context) this.actBt));
            }
            if (this.tvTxtNumber != null) {
                this.tvTxtNumber.setTypeface(bt.a((Context) this.actBt));
            }
            JText jText4 = (JText) getPage().getChildViewByid(57);
            if (jText4 != null) {
                jText4.setTypeface(bt.a((Context) this.actBt));
            }
        }
    }

    public boolean isNumber(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        App.getApp().mIpcObj.onNotify(this, i, iArr, fArr, strArr);
    }

    public void onNotify_UniCar(int i, int[] iArr, float[] fArr, String[] strArr) {
        App.getApp().mIpcObj.onNotify_UniCar(this, i, iArr, fArr, strArr);
    }

    public void pause() {
        super.pause();
        this.bTop = false;
        App.bResumeByDial = false;
    }

    public void popTestDlg() {
        Dialog popDlg;
        if (Main.mConf_PlatForm == 5 && (popDlg = this.actBt.ui.getPopDlg(19, App.getApp().getIdStyle("pop_add_contacts_anim"), true)) != null) {
            JPage page = this.actBt.ui.getPage(19);
            if (page != null) {
                page.resume();
            }
            popDlg.show();
        }
    }

    public void pressGridItem(JPage jPage, JPage jPage2, boolean z) {
        boolean z2 = z && jPage == jPage2;
        JText jText = (JText) jPage.getChildViewByid(160);
        if (jText != null) {
            jText.setFocus(z2);
        }
        JText jText2 = (JText) jPage.getChildViewByid(159);
        if (jText2 != null) {
            jText2.setFocus(z2);
        }
    }

    public void queryContacts(String str) {
        if (TextUtils.isEmpty(str)) {
            resetList(App.mBtInfo.mListContact);
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (isNumber(str)) {
            for (SparseArray next : App.mBtInfo.mListContact) {
                if (((String) next.get(180)).contains(str)) {
                    arrayList.add(next);
                }
            }
            resetList(arrayList);
        }
    }

    public void resetBtnEnable() {
        boolean z = true;
        if (App.hideBtnWhenDisconnect) {
            boolean z2 = IpcObj.isConnect();
            int i = 97;
            while (true) {
                int i2 = i;
                if (i2 >= 120) {
                    break;
                }
                JButton jButton = (JButton) getPage().getChildViewByid(i2);
                if (jButton != null) {
                    jButton.setEnabled(z2);
                }
                i = i2 + 1;
            }
        }
        if (bv.f() && !bv.k()) {
            if (!IpcObj.isConnect()) {
                z = false;
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(118);
            if (jButton2 != null) {
                jButton2.setEnabled(z);
            }
        }
        updateSoundVol();
    }

    public void resetList() {
        if (bv.e()) {
            showLoveNumber();
            return;
        }
        if (this.mGridView != null) {
            App.mBtInfo.sortContact();
        }
        resetList(App.mBtInfo.mListContact);
    }

    public void resetList(List<SparseArray<String>> list) {
        if (this.mGridView == null) {
            return;
        }
        if (!IpcObj.isConnect()) {
            App.resetList(this.mGridView, (List<SparseArray<String>>) null);
        } else {
            App.resetList(this.mGridView, list);
        }
    }

    public void resetMenuLayoutAngle(int i) {
        CircleMenuLayout circleMenuLayout = (CircleMenuLayout) getPage().getChildViewByid(77);
        if (circleMenuLayout != null) {
            circleMenuLayout.setCircleAngle(i);
        }
    }

    public void resetThridDial(int i) {
        JView jView;
        switch (i) {
            case 0:
            case 1:
            case 2:
            case 6:
                this.dialThrdPhone = false;
                this.dialThrdNumber = FinalChip.BSP_PLATFORM_Null;
                break;
        }
        if (App.mStrCustomer.equalsIgnoreCase("XINGKE") && (jView = (JView) getPage().getChildViewByid(54)) != null) {
            if (i == 0) {
                jView.setBackgroundResource(App.getApp().getIdDrawable("disconnect"));
            } else {
                jView.setBackgroundResource(App.getApp().getIdDrawable("connect"));
            }
        }
    }

    public void resume() {
        JPage jPage;
        if (this.actBt.bTop) {
            this.bTop = true;
            if (!App.bResumeByDial) {
                App.getApp().mIpcObj.requestAppIdRight(3);
            }
            App.getApp().mIpcObj.updateNotify_Dial();
            if (this.tvDevName2 != null) {
                this.tvDevName2.setText(Bt.sDevName);
            }
            if (this.tvmPin2 != null) {
                this.tvmPin2.setText(Bt.sDevPin);
            }
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
            if (App.bHistoryLogAllFlag) {
                resetList();
            }
            if (!(!App.bDelMenuFromBtAv || (jPage = this.actBt.ui.mPages.get(2)) == null || jPage.getVisibility() == 0)) {
                jPage.setVisibility(0);
            }
            controlDialInfo(Bt.DATA[9]);
            if (App.bShowTime) {
                initTime();
            }
            resetBtnEnable();
            disableSaveView(false);
        }
    }

    public boolean scrollOk() {
        if (!this.bTop || !this.actBt.isFocus(this.mGridView)) {
            return false;
        }
        if (!TextUtils.isEmpty(this.checkPhoneNumber)) {
            IpcObj.itemDial(this.checkPhoneNumber);
        }
        return true;
    }

    public boolean scrollToNext() {
        if (!this.bTop || !this.actBt.isFocus(this.mGridView)) {
            return false;
        }
        if (this.mPosFocusCpy != this.mGridView.mIndexFocus) {
            this.mPosFocusCpy = this.mGridView.mIndexFocus;
            this.mPosMoveTo = this.mPosFocusCpy;
        }
        if (this.mPosMoveTo < 0) {
            this.mPosMoveTo = 0;
        } else if (this.mPosMoveTo + 1 >= this.mGridView.getList().size()) {
            return false;
        } else {
            this.mPosMoveTo++;
        }
        this.mGridView.scrollToPosition(this.mPosMoveTo);
        return true;
    }

    public boolean scrollToPrev() {
        if (!this.bTop || !this.actBt.isFocus(this.mGridView)) {
            return false;
        }
        if (this.mPosFocusCpy != this.mGridView.mIndexFocus) {
            this.mPosFocusCpy = this.mGridView.mIndexFocus;
            this.mPosMoveTo = this.mPosFocusCpy;
        }
        if (this.mPosMoveTo < 0) {
            this.mPosMoveTo = 0;
        } else if (this.mPosMoveTo <= 0) {
            return false;
        } else {
            this.mPosMoveTo--;
        }
        this.mGridView.scrollToPosition(this.mPosMoveTo);
        return true;
    }

    public void setButtonColor(int i) {
        if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
            int[] iArr = {i};
            int[][] iArr2 = {new int[]{16842919}};
            ImageView imageView = (ImageView) getPage().getChildViewByid(406);
            if (imageView != null) {
                imageView.setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
                imageView.setImageResource(App.getApp().getIdDrawable("bk0"));
            }
            JButton jButton = (JButton) getPage().getChildViewByid(120);
            if (jButton != null) {
                jButton.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(104);
            if (jButton2 != null) {
                jButton2.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton2.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton3 = (JButton) getPage().getChildViewByid(105);
            if (jButton3 != null) {
                jButton3.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton3.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton4 = (JButton) getPage().getChildViewByid(106);
            if (jButton4 != null) {
                jButton4.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton4.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton5 = (JButton) getPage().getChildViewByid(107);
            if (jButton5 != null) {
                jButton5.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton5.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton6 = (JButton) getPage().getChildViewByid(108);
            if (jButton6 != null) {
                jButton6.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton6.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton7 = (JButton) getPage().getChildViewByid(109);
            if (jButton7 != null) {
                jButton7.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton7.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton8 = (JButton) getPage().getChildViewByid(110);
            if (jButton8 != null) {
                jButton8.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton8.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton9 = (JButton) getPage().getChildViewByid(111);
            if (jButton9 != null) {
                jButton9.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton9.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton10 = (JButton) getPage().getChildViewByid(112);
            if (jButton10 != null) {
                jButton10.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton10.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton11 = (JButton) getPage().getChildViewByid(114);
            if (jButton11 != null) {
                jButton11.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton11.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton12 = (JButton) getPage().getChildViewByid(113);
            if (jButton12 != null) {
                jButton12.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton12.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton13 = (JButton) getPage().getChildViewByid(103);
            if (jButton13 != null) {
                jButton13.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton13.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton14 = (JButton) getPage().getChildViewByid(118);
            if (jButton14 != null) {
                jButton14.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton14.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton15 = (JButton) getPage().getChildViewByid(101);
            if (jButton15 != null) {
                jButton15.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton15.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton16 = (JButton) getPage().getChildViewByid(102);
            if (jButton16 != null) {
                jButton16.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton16.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton17 = (JButton) getPage().getChildViewByid(115);
            if (jButton17 != null) {
                jButton17.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton17.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void showInCallAnim() {
        if (this.mViewInCall != null && this.mViewInCall.getVisibility() != 0) {
            this.mViewInCall.setVisibility(0);
            if (this.mRunnable_AnimDrawable != null) {
                Main.removeRunnable_Ui(this.mRunnable_AnimDrawable);
                this.mRunnable_AnimDrawable.stopRun();
                this.mRunnable_AnimDrawable = null;
            }
            this.mRunnable_AnimDrawable = new Runnable_AnimDrawable(this.mViewInCall, this.mAnimationDrawable, 200);
            Main.postRunnable_Ui(false, this.mRunnable_AnimDrawable);
        }
    }

    public void showLoveNumber() {
        String str;
        String str2;
        boolean z;
        boolean z2 = true;
        int i = 8;
        if (IpcObj.isConnect()) {
            if (bv.c()) {
                new SparseArray();
                SparseArray<String> a = bz.a("contact_save1");
                String str3 = a.get(178);
                boolean z3 = !TextUtils.isEmpty(a.get(178)) && IpcObj.isConnect();
                if (this.Contact_name1 != null) {
                    JText jText = this.Contact_name1;
                    if (!z3) {
                        str3 = FinalChip.BSP_PLATFORM_Null;
                    }
                    jText.setText(str3);
                    this.Contact_name1.setVisibility(z3 ? 0 : 8);
                }
                if (this.Contact_add1 != null) {
                    this.Contact_add1.setVisibility(z3 ? 8 : 0);
                }
                SparseArray<String> a2 = bz.a("contact_save2");
                String str4 = a2.get(178);
                boolean z4 = !TextUtils.isEmpty(a2.get(178)) && IpcObj.isConnect();
                if (this.Contact_name2 != null) {
                    JText jText2 = this.Contact_name2;
                    if (!z4) {
                        str4 = FinalChip.BSP_PLATFORM_Null;
                    }
                    jText2.setText(str4);
                    this.Contact_name2.setVisibility(z4 ? 0 : 8);
                }
                if (this.Contact_add2 != null) {
                    this.Contact_add2.setVisibility(z4 ? 8 : 0);
                }
                SparseArray<String> a3 = bz.a("contact_save3");
                String str5 = a3.get(178);
                boolean z5 = !TextUtils.isEmpty(a3.get(178)) && IpcObj.isConnect();
                if (this.Contact_name3 != null) {
                    JText jText3 = this.Contact_name3;
                    if (!z5) {
                        str5 = FinalChip.BSP_PLATFORM_Null;
                    }
                    jText3.setText(str5);
                    this.Contact_name3.setVisibility(z5 ? 0 : 8);
                }
                if (this.Contact_add3 != null) {
                    this.Contact_add3.setVisibility(z5 ? 8 : 0);
                }
                SparseArray<String> a4 = bz.a("contact_save4");
                String str6 = a4.get(178);
                boolean z6 = !TextUtils.isEmpty(a4.get(178)) && IpcObj.isConnect();
                if (this.Contact_name4 != null) {
                    JText jText4 = this.Contact_name4;
                    if (!z6) {
                        str6 = FinalChip.BSP_PLATFORM_Null;
                    }
                    jText4.setText(str6);
                    this.Contact_name4.setVisibility(z6 ? 0 : 8);
                }
                if (this.Contact_add4 != null) {
                    this.Contact_add4.setVisibility(z6 ? 8 : 0);
                }
                SparseArray<String> a5 = bz.a("contact_save5");
                String str7 = a5.get(178);
                if (TextUtils.isEmpty(a5.get(178)) || !IpcObj.isConnect()) {
                    z2 = false;
                }
                if (this.Contact_name5 != null) {
                    JText jText5 = this.Contact_name5;
                    if (!z2) {
                        str7 = FinalChip.BSP_PLATFORM_Null;
                    }
                    jText5.setText(str7);
                    this.Contact_name5.setVisibility(z2 ? 0 : 8);
                }
                if (this.Contact_add5 != null) {
                    JText jText6 = this.Contact_add5;
                    if (!z2) {
                        i = 0;
                    }
                    jText6.setVisibility(i);
                }
            } else if (bv.e()) {
                int size = App.mBtInfo.mListFavContact.size();
                for (int i2 = 0; i2 <= 5; i2++) {
                    if (i2 < size) {
                        str = (String) App.mBtInfo.mListFavContact.get(i2).get(180);
                        str2 = (String) App.mBtInfo.mListFavContact.get(i2).get(178);
                        z = false;
                    } else {
                        str = FinalChip.BSP_PLATFORM_Null;
                        str2 = FinalChip.BSP_PLATFORM_Null;
                        z = true;
                    }
                    JButton jButton = (JButton) getPage().getChildViewByid(i2 + 364);
                    if (jButton != null) {
                        jButton.setVisibility(z ? 8 : 0);
                    }
                    JText jText7 = (JText) getPage().getChildViewByid(i2 + 358);
                    JText jText8 = (JText) getPage().getChildViewByid(i2 + 352);
                    if (jText7 != null) {
                        jText7.setVisibility(z ? 8 : 0);
                        jText7.setText(str2);
                    }
                    if (jText8 != null) {
                        jText8.setVisibility(z ? 8 : 0);
                        jText8.setText(str);
                    }
                }
            }
        }
    }

    public void startIpPhoneTimer() {
        stopIpPhoneTimer();
        this.mRunnableIpPhoneTimer = new RunnableIpPhoneTimer();
        if (this.mRunnableIpPhoneTimer != null) {
            Main.postRunnable_Ui(true, this.mRunnableIpPhoneTimer);
        }
    }

    public void stopIpPhoneTimer() {
        if (this.mRunnableIpPhoneTimer != null) {
            Main.removeRunnable_Ui(this.mRunnableIpPhoneTimer);
            this.mRunnableIpPhoneTimer.stopRun();
            this.mRunnableIpPhoneTimer = null;
        }
    }

    public void switchFull() {
        if (App.getApp().fytGetState != null) {
            Main.postRunnable_Ui(false, new RunnableSwitchFull());
        }
    }

    public void updateBtnVoiceSwitch(boolean z) {
        JButton jButton = (JButton) getPage().getChildViewByid(118);
        if (jButton != null) {
            MyUiItem myUiItem = (MyUiItem) jButton.getTag();
            if (myUiItem.getParaStr() != null) {
                String strDrawable = !z ? myUiItem.getStrDrawable() : myUiItem.getParaStr() != null ? myUiItem.getParaStr()[0] : null;
                if (jButton.getStrDrawable() != strDrawable && strDrawable != null) {
                    jButton.setStrDrawable(strDrawable, true);
                }
            }
        }
    }

    public void updatePhoneName() {
        updatePhoneName(Bt.sPhoneNumber);
    }

    public void updatePhoneName(String str) {
        JText jText = (JText) getPage().getChildViewByid(57);
        JText jText2 = (JText) getPage().getChildViewByid(64);
        JText jText3 = (JText) getPage().getChildViewByid(65);
        if (!TextUtils.isEmpty(str)) {
            String nameByNumber = App.mBtInfo.getNameByNumber(str);
            if (jText != null) {
                if (!TextUtils.isEmpty(nameByNumber)) {
                    jText.setText(nameByNumber);
                } else if (!App.getApp().mIpcObj.isCalling()) {
                    jText.setText(FinalChip.BSP_PLATFORM_Null);
                } else if (bv.h()) {
                    jText.setText(App.getApp().getString(EnvironmentCompat.MEDIA_UNKNOWN));
                } else {
                    jText.setText(str);
                }
            }
            if (jText2 != null) {
                if (!TextUtils.isEmpty(nameByNumber)) {
                    jText2.setText(nameByNumber);
                } else if (!App.getApp().mIpcObj.isCalling()) {
                    jText2.setText(FinalChip.BSP_PLATFORM_Null);
                } else if (bv.h()) {
                    jText2.setText(App.getApp().getString(EnvironmentCompat.MEDIA_UNKNOWN));
                } else {
                    jText2.setText(str);
                }
            }
            if (jText3 == null) {
                return;
            }
            if (!TextUtils.isEmpty(nameByNumber)) {
                jText3.setText(nameByNumber);
            } else if (!App.getApp().mIpcObj.isCalling()) {
                jText3.setText(FinalChip.BSP_PLATFORM_Null);
            } else {
                jText3.setText(str);
            }
        } else {
            if (jText != null) {
                jText.setText(FinalChip.BSP_PLATFORM_Null);
            }
            if (jText2 != null) {
                jText2.setText(FinalChip.BSP_PLATFORM_Null);
            }
            if (jText3 != null) {
                jText3.setText(FinalChip.BSP_PLATFORM_Null);
            }
        }
    }

    public void updateShowDial() {
        char c;
        boolean z = true;
        char c2 = 3;
        if (!App.bPop3rdPhone_YF) {
            switch (Bt.DATA[9]) {
                case 3:
                    if (!bv.h() && !App.mStrCustomer.equals("TZY_NEW")) {
                        c = 1;
                        break;
                    } else {
                        c = 2;
                        break;
                    }
                case 4:
                    if (!bv.h()) {
                        c = 1;
                        break;
                    } else {
                        c = 2;
                        break;
                    }
                case 5:
                    c = 2;
                    break;
                default:
                    c = 3;
                    break;
            }
        } else {
            switch (Bt.DATA[9]) {
                case 3:
                    c = 4;
                    break;
                case 4:
                    c = 4;
                    break;
                case 5:
                    c2 = 5;
                    break;
            }
            c = c2;
        }
        if (bv.h()) {
            if (Bt.DATA[9] != 4 || App.bPop3rdPhone_YF) {
                z = false;
            }
            JButton jButton = (JButton) getPage().getChildViewByid(102);
            JButton jButton2 = (JButton) getPage().getChildViewByid(134);
            JButton jButton3 = (JButton) getPage().getChildViewByid(133);
            if (!(jButton == null || jButton2 == null || jButton3 == null)) {
                jButton.setVisibility(z ? 8 : 0);
                jButton2.setVisibility(z ? 0 : 8);
                jButton3.setVisibility(z ? 0 : 8);
            }
        }
        disableSaveView(false);
        switch (c) {
            case 1:
                if (this.mViewShowDial != null) {
                    this.mViewShowDial.setVisibility(8);
                }
                if (this.mViewShowCall != null) {
                    this.mViewShowCall.setVisibility(0);
                }
                if (this.mViewShowHang != null) {
                    this.mViewShowHang.setVisibility(8);
                }
                if (this.tvDialAnswer != null) {
                    this.tvDialAnswer.setVisibility(8);
                }
                if (this.tvDialHangup != null) {
                    this.tvDialHangup.setVisibility(8);
                }
                if (this.mViewShowCall_Third != null) {
                    this.mViewShowCall_Third.setVisibility(8);
                }
                if (this.mViewShowTalk_Third != null) {
                    this.mViewShowTalk_Third.setVisibility(8);
                    return;
                }
                return;
            case 2:
                if (this.mViewShowDial != null) {
                    this.mViewShowDial.setVisibility(8);
                }
                if (this.mViewShowCall != null) {
                    this.mViewShowCall.setVisibility(8);
                }
                if (this.mViewShowHang != null) {
                    this.mViewShowHang.setVisibility(0);
                }
                if (this.tvDialHangup != null) {
                    this.tvDialHangup.setVisibility(0);
                    this.tvDialHangup.setText(App.getApp().getString("bt_hangup"));
                }
                if (this.mViewShowCall_Third != null) {
                    this.mViewShowCall_Third.setVisibility(8);
                }
                if (this.mViewShowTalk_Third != null) {
                    this.mViewShowTalk_Third.setVisibility(8);
                    return;
                }
                return;
            case 3:
                if (this.mViewShowDial != null) {
                    this.mViewShowDial.setVisibility(0);
                }
                if (this.mViewShowCall != null) {
                    this.mViewShowCall.setVisibility(8);
                }
                if (this.mViewShowHang != null) {
                    this.mViewShowHang.setVisibility(8);
                }
                if (this.tvDialAnswer != null) {
                    this.tvDialAnswer.setVisibility(0);
                    this.tvDialAnswer.setText(App.getApp().getString("bt_page_dial"));
                }
                if (this.mViewShowCall_Third != null) {
                    this.mViewShowCall_Third.setVisibility(8);
                }
                if (this.mViewShowTalk_Third != null) {
                    this.mViewShowTalk_Third.setVisibility(8);
                    return;
                }
                return;
            case 4:
                if (this.mViewShowDial != null) {
                    this.mViewShowDial.setVisibility(8);
                }
                if (this.mViewShowCall != null) {
                    this.mViewShowCall.setVisibility(8);
                }
                if (this.mViewShowHang != null) {
                    this.mViewShowHang.setVisibility(0);
                }
                if (this.mViewShowCall_Third != null) {
                    this.mViewShowCall_Third.setVisibility(0);
                }
                if (this.mViewShowTalk_Third != null) {
                    this.mViewShowTalk_Third.setVisibility(8);
                    return;
                }
                return;
            case 5:
                if (this.mViewShowDial != null) {
                    this.mViewShowDial.setVisibility(8);
                }
                if (this.mViewShowCall != null) {
                    this.mViewShowCall.setVisibility(8);
                }
                if (this.mViewShowHang != null) {
                    this.mViewShowHang.setVisibility(0);
                }
                if (this.mViewShowCall_Third != null) {
                    this.mViewShowCall_Third.setVisibility(8);
                }
                if (this.mViewShowTalk_Third != null) {
                    this.mViewShowTalk_Third.setVisibility(0);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void updateSimLayout(int i) {
        boolean z = Ipc_New.isIpPhoneConnect() && i == 1;
        if (bv.c() && i == 1) {
            z = true;
        }
        if (z) {
            if (this.mViewNoSim != null) {
                this.mViewNoSim.setVisibility(8);
            }
            if (this.mViewWithSim != null) {
                this.mViewWithSim.setVisibility(0);
                return;
            }
            return;
        }
        if (this.mViewNoSim != null) {
            this.mViewNoSim.setVisibility(0);
        }
        if (this.mViewWithSim != null) {
            this.mViewWithSim.setVisibility(8);
        }
    }

    public void updateSoundVol() {
        if (this.mSB_BtVol != null) {
            this.mSB_BtVol.setProgress(App.bSoundVol);
        }
    }

    public void updateSystemTime() {
        Main.postRunnable_Ui(true, this.mRunnable_updateSysTime);
    }

    public void updateThirdPhoneName() {
        if (bv.h()) {
            JText jText = (JText) getPage().getChildViewByid(66);
            JText jText2 = (JText) getPage().getChildViewByid(67);
            if (jText != null) {
                String str = Bt.sPhoneNumberHoldOn;
                String nameByNumber = App.mBtInfo.getNameByNumber(str);
                if (jText != null) {
                    if (!TextUtils.isEmpty(nameByNumber)) {
                        jText.setText(nameByNumber);
                    } else {
                        jText.setText(str);
                    }
                }
                if (jText2 == null) {
                    return;
                }
                if (!TextUtils.isEmpty(nameByNumber)) {
                    jText2.setText(nameByNumber);
                } else {
                    jText2.setText(str);
                }
            }
        }
    }
}
