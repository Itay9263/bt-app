package com.syu.app.ipc;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.syu.app.App;
import com.syu.broadcast.MyService;
import com.syu.bt.act.ActBt;
import com.syu.bt.act.InterfaceBt;
import com.syu.bt.page.Page_Contact;
import com.syu.bt.page.Page_History;
import com.syu.bt.page.Page_Pair;
import com.syu.bt.page.pop.Page_Pop3rdPhone;
import com.syu.bt.page.pop.Page_PopBt;
import com.syu.bt.page.pop.Page_PopBt_Book;
import com.syu.bt.page.pop.Page_PopSms;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.Conn;
import com.syu.ipcself.ConnSys;
import com.syu.ipcself.ConnUniCar;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.ipcself.module.main.Sound;
import com.syu.ipcself.module.sys.Sys;
import com.syu.ipcself.module.unicar.UniCar;
import com.syu.util.IUiNotify;
import com.syu.util.IpcUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class Ipc_New {
    static final int[] NOTIFYCODE_Bt;
    public static final int[] NOTIFYCODE_BtAV;
    static final int[] NOTIFYCODE_Bt_Will = {21, 22, 25, 29, 24};
    public static final int[] NOTIFYCODE_DIAL = {9, 7, 8, 12, 14};
    static final int[] NOTIFYCODE_Main_Will;
    static final int[] NOTIFYCODE_PopBt = {9, 8, 12};
    public static final int[] NOTIFYCODE_SET = {15, 16, 20, 33, 31, 12, 36};
    public static final int[] NOTIFYCODE_Sound = {24, 12, 13, 11, 10};
    public static final int[] NOTIFYCODE_Sys_Will = new int[1];
    public static final int[] NOTIFYCODE_UniCar;
    static final int[] NumCmd = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14};
    public Conn mConn = null;
    public ConnSys mConnSys = null;
    public ConnUniCar mConnUniCar = null;
    public Ipc_NewNotifyPage mNotify = new Ipc_NewNotifyPage();
    private IUiNotify notify_Bt = new IUiNotify() {
        public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
            switch (i) {
                case 0:
                case 1:
                case 13:
                case 26:
                case 34:
                    App.getApp().mIpcObj.updateNotify_BtAv();
                    break;
                case 6:
                    if (bt.a().e()) {
                        Main.postRunnable_Ui(true, App.getApp().mIpcObj.mRunnable_scanDB);
                        break;
                    }
                    break;
                case 8:
                    if (bt.a().e()) {
                        if (Bt.values.getAsInteger("type") != null && TextUtils.isEmpty((String) Bt.values.get("number"))) {
                            Bt.values.put("number", Bt.sPhoneNumber);
                            if (Bt.values.getAsInteger("type").intValue() == 2) {
                                App.strNumberBak = Bt.sPhoneNumber;
                            }
                        }
                        if (!TextUtils.isEmpty(Bt.sPhoneNumberHoldOn) && Bt.values3rd.getAsInteger("type") == null) {
                            Bt.values3rd.put("type", 3);
                            Bt.values3rd.put("date", Long.valueOf(System.currentTimeMillis()));
                            if (!Bt.sPhoneNumberHoldOn.equals(Bt.values.get("number"))) {
                                Bt.values3rd.put("number", Bt.sPhoneNumberHoldOn);
                            } else {
                                Bt.values3rd.put("number", Bt.sPhoneNumber);
                            }
                        }
                        if (Ipc_New.isTalk()) {
                            if (Bt.values.getAsInteger("type") != null && Bt.sPhoneNumber.equals(Bt.values.get("number")) && Bt.values.getAsInteger("type").intValue() == 3) {
                                Bt.values.put("type", 1);
                            }
                            if (Bt.values3rd.getAsInteger("type") != null && Bt.sPhoneNumber.equals(Bt.values3rd.get("number"))) {
                                Bt.values3rd.put("type", 1);
                            }
                        }
                        if (TextUtils.isEmpty(Bt.sPhoneNumberHoldOn) && Bt.values3rd.getAsInteger("type") != null) {
                            if (Bt.sPhoneNumber.equals(Bt.values3rd.get("number"))) {
                                if (Bt.values.getAsInteger("type") != null) {
                                    App.mBtInfo.insertCallLog(Bt.values, true);
                                    break;
                                }
                            } else {
                                Main.postRunnable_Ui(false, new Runnable() {
                                    public void run() {
                                        App.mBtInfo.insertCallLog(Bt.values3rd, true);
                                    }
                                }, 500);
                                break;
                            }
                        }
                    }
                    break;
                case 9:
                    if (bt.a().e()) {
                        Ipc_New.this.phoneState();
                        Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                        while (it.hasNext()) {
                            it.next().phoneState();
                        }
                        break;
                    }
                    break;
                case 15:
                    String diyName = App.getDiyName();
                    String diyPin = App.getDiyPin();
                    if (!TextUtils.isEmpty(diyName) && !diyName.equals(Bt.sDevName)) {
                        Ipc_New.setDevName(diyName);
                    }
                    if (!TextUtils.isEmpty(diyPin) && !diyPin.equals(Bt.sDevPin)) {
                        Ipc_New.setDevPin(diyPin);
                        break;
                    }
                case 18:
                    App.bHFP = Bt.DATA[i] == 1;
                    break;
                case 31:
                    App.bBtPowerOnOff = Bt.DATA[i] > 0;
                    break;
                case 33:
                    App.iRingLevel = Bt.DATA[i];
                    break;
                case 43:
                    if (Bt.DATA[i] == 1) {
                        App.getApp().popPhoneVoice(true);
                    } else if (App.getApp().bPopPhoneVoice && App.getApp().bPopPhoneFirstValue > 0) {
                        App.getApp().popPhoneVoice(false);
                    }
                    App.getApp().bPopPhoneFirstValue = 2;
                    break;
                case 44:
                    if (strArr != null && strArr.length > 0) {
                        if (!TextUtils.isEmpty(strArr[0])) {
                            App.mPhoneBatterty = Math.min(Math.max(Integer.parseInt(strArr[0]), 1), 5);
                        } else {
                            App.mPhoneBatterty = 0;
                        }
                    }
                    App.getApp().mIpcObj.updateNotify_BtAv();
                    break;
                case 47:
                    if (iArr != null && iArr.length > 0) {
                        App.mPhoneCoding = iArr[0];
                    }
                    App.getApp().mIpcObj.updateNotify_BtAv();
                    break;
            }
            if (i == 8) {
                if (IpcUtil.strsOk(strArr, 2)) {
                    if (!bv.h()) {
                        App.getApp().pop3rdPhone(true);
                    }
                    App.bPop3rdPhone_YF = true;
                } else if (IpcUtil.strsOk(strArr, 1)) {
                    App.bPop3rdPhone_YF = false;
                    if (!bv.h()) {
                        App.getApp().pop3rdPhone(false);
                    }
                }
            }
            if (App.mInterfaceBt.size() == 0) {
                App.getApp().mIpcObj.onNotify((Page_Contact) null, i, iArr, fArr, strArr);
                App.getApp().mIpcObj.onNotify((Page_Pair) null, i, iArr, fArr, strArr);
                App.getApp().mIpcObj.onNotify((Page_History) null, i, iArr, fArr, strArr);
            }
            Iterator<InterfaceBt> it2 = App.mInterfaceBt.iterator();
            while (it2.hasNext()) {
                it2.next().onNotify(i, iArr, fArr, strArr);
            }
            if (Ipc_New.this.notify_Bt_Pop != null) {
                Ipc_New.this.notify_Bt_Pop.onNotify(i, iArr, fArr, strArr);
            }
        }
    };
    IUiNotify notify_Bt_Pop = null;
    private IUiNotify notify_Main = new IUiNotify() {
        boolean bIsRightAppId = false;

        public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
            switch (i) {
                case 0:
                    Ipc_New.appId();
                    if (App.getApp().isAppTop() && IpcUtil.intsOk(iArr, 1) && this.bIsRightAppId && !Ipc_New.isRightAppId()) {
                        Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                        while (it.hasNext()) {
                            InterfaceBt next = it.next();
                            if ((bv.k() || Main.mConf_PlatForm == 6) && App.mEnableHalfScreen) {
                                next.moveTaskToBack();
                            } else if (!next.isHalf()) {
                                next.moveTaskToBack();
                            }
                        }
                    }
                    this.bIsRightAppId = Ipc_New.isRightAppId();
                    if (!this.bIsRightAppId) {
                        App.sPage = 3;
                        App.sPageBak = App.sPage;
                        App.sBackFlag = 0;
                        return;
                    }
                    return;
                case 1:
                    IpcObj.mcuOn(i, Main.DATA[i]);
                    return;
                case 12:
                    if (iArr != null && iArr.length > 0) {
                        if (iArr[0] == 1) {
                            App.bBackCarFlag = true;
                            if (Main.mConf_PlatForm != 8) {
                                App.getApp().popBt(false, false);
                                return;
                            }
                            return;
                        }
                        App.bBackCarFlag = false;
                        return;
                    }
                    return;
                case 34:
                    if (strArr != null && strArr.length > 0 && bv.e() && !strArr[0].contains(bt.a().a)) {
                        bi.a().b();
                        return;
                    }
                    return;
                case 50:
                    if (iArr != null && iArr.length > 0 && iArr[0] == 1) {
                        App.bBtAcc_On = true;
                        Main.postRunnable_Ui(true, App.getApp().mIpcObj.mRunnable_scanDB, 3000);
                        if (bv.h()) {
                            Main.removeRunnable_Ui(App.getApp().mIpcObj.mRunnableStartPlayAv);
                            Main.postRunnable_Ui(true, App.getApp().mIpcObj.mRunnableStartPlayAv, 4000);
                            return;
                        }
                        return;
                    }
                    return;
                case 60:
                    if (IpcUtil.intsOk(iArr, 1) && App.getApp().isAppTop()) {
                        switch (iArr[0]) {
                            case 0:
                                Iterator<InterfaceBt> it2 = App.mInterfaceBt.iterator();
                                while (it2.hasNext()) {
                                    it2.next().scrollOk();
                                }
                                return;
                            case 1:
                                Iterator<InterfaceBt> it3 = App.mInterfaceBt.iterator();
                                while (it3.hasNext()) {
                                    it3.next().onKeyUp();
                                }
                                return;
                            case 2:
                                Iterator<InterfaceBt> it4 = App.mInterfaceBt.iterator();
                                while (it4.hasNext()) {
                                    it4.next().onKeyDown();
                                }
                                return;
                            case 3:
                                Iterator<InterfaceBt> it5 = App.mInterfaceBt.iterator();
                                while (it5.hasNext()) {
                                    it5.next().onKeyLeft();
                                }
                                return;
                            case 4:
                                Iterator<InterfaceBt> it6 = App.mInterfaceBt.iterator();
                                while (it6.hasNext()) {
                                    it6.next().onKeyRight();
                                }
                                return;
                            case 5:
                                Iterator<InterfaceBt> it7 = App.mInterfaceBt.iterator();
                                while (it7.hasNext()) {
                                    it7.next().scrollToPrev();
                                }
                                return;
                            case 6:
                                Iterator<InterfaceBt> it8 = App.mInterfaceBt.iterator();
                                while (it8.hasNext()) {
                                    it8.next().scrollToNext();
                                }
                                return;
                            case 7:
                                if (App.getApp().mIpcObj.isCalling()) {
                                    cb.a().a(App.getApp().getString("bt_state_using"));
                                    return;
                                } else {
                                    Main.postRunnable_Ui(true, MyService.c);
                                    return;
                                }
                            case 8:
                                Iterator<InterfaceBt> it9 = App.mInterfaceBt.iterator();
                                while (it9.hasNext()) {
                                    it9.next().DialByKey();
                                }
                                return;
                            default:
                                return;
                        }
                    } else {
                        return;
                    }
                case 113:
                    if (iArr != null && iArr.length > 0 && bv.d() && Ipc_New.isInCall() && !App.getApp().isAppTop()) {
                        App.getApp().popBt(true, false);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private IUiNotify notify_PopBt = new IUiNotify() {
        public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
            JPage jPage;
            Page_PopBt page_PopBt;
            JText jText;
            JText jText2;
            Page_PopBt page_PopBt2;
            Page_PopBt page_PopBt3;
            JText jText3;
            JText jText4;
            JPage jPage2;
            boolean z = true;
            if (App.getApp().bPopBtBook && (jPage2 = App.uiApp.mPages.get(4)) != null) {
                switch (i) {
                    case 8:
                        Page_PopBt_Book page_PopBt_Book = (Page_PopBt_Book) jPage2.getNotify();
                        if (page_PopBt_Book != null) {
                            page_PopBt_Book.queryContacts(Bt.sPhoneNumber);
                            break;
                        }
                        break;
                }
            }
            JPage jPage3 = App.uiApp.mPages.get(10);
            if (jPage3 != null) {
                switch (i) {
                    case 8:
                        Page_Pop3rdPhone page_Pop3rdPhone = (Page_Pop3rdPhone) jPage3.getNotify();
                        if (page_Pop3rdPhone != null) {
                            page_Pop3rdPhone.updatePhoneName();
                            break;
                        }
                        break;
                    case 9:
                        Page_Pop3rdPhone page_Pop3rdPhone2 = (Page_Pop3rdPhone) jPage3.getNotify();
                        if (page_Pop3rdPhone2 != null) {
                            page_Pop3rdPhone2.updateThirdPhoneView();
                            page_Pop3rdPhone2.updateBtnAccept();
                            page_Pop3rdPhone2.updatePhoneName();
                            page_Pop3rdPhone2.updateMergerPhoneName();
                            break;
                        }
                        break;
                    case 12:
                        Page_Pop3rdPhone page_Pop3rdPhone3 = (Page_Pop3rdPhone) jPage3.getNotify();
                        if (page_Pop3rdPhone3 != null) {
                            page_Pop3rdPhone3.updateMergerTime((Bt.DATA[i] + 100) / 1000);
                            break;
                        }
                        break;
                }
            }
            JPage jPage4 = App.uiApp.mPages.get(1);
            JPage jPage5 = jPage4 == null ? App.uiApp.mPages.get(3) : jPage4;
            if (jPage5 == null) {
                if (App.bShowBtInNaviFloatBtn && i == 9) {
                    switch (Bt.DATA[9]) {
                        case 0:
                        case 2:
                            if (Ipc_New.this.isCalling(Bt.sLastPhoneState)) {
                                App.getApp().showFloatBtn(false);
                                break;
                            }
                            break;
                    }
                }
            } else {
                switch (i) {
                    case 8:
                        JText jText5 = (JText) jPage5.getChildViewByid(14);
                        JText jText6 = (JText) jPage5.getChildViewByid(15);
                        if (jText5 != null) {
                            if (!bv.h()) {
                                jText5.setText(Bt.sPhoneNumber);
                            } else if (TextUtils.isEmpty(Bt.sPhoneNumberHoldOn) || Bt.DATA[9] != 4) {
                                jText5.setText(Bt.sPhoneNumber);
                            } else {
                                jText5.setText(Bt.sPhoneNumberHoldOn);
                            }
                            if (jText6 != null) {
                                jText6.setText(jText5.getText());
                            }
                        }
                        JText jText7 = (JText) jPage5.getChildViewByid(17);
                        JText jText8 = (JText) jPage5.getChildViewByid(18);
                        if (jText7 != null) {
                            if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                                jText7.setText(FinalChip.BSP_PLATFORM_Null);
                                if (jText8 != null) {
                                    jText8.setText(FinalChip.BSP_PLATFORM_Null);
                                }
                            } else {
                                ArrayList arrayList = new ArrayList();
                                arrayList.addAll(App.mBtInfo.mListContact);
                                if (jText8 == null || !bv.h()) {
                                    App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText7, FinalChip.BSP_PLATFORM_Null, false), false, 5);
                                } else {
                                    String str = Bt.sPhoneNumber;
                                    if (Ipc_New.isRing()) {
                                        str = Bt.sPhoneNumberHoldOn;
                                    }
                                    App.startThread(App.StrThreadGetNameByNumber, new bs(arrayList, jText7, jText8, str, str, true), false, 5);
                                }
                            }
                        }
                        if (bv.h() && (page_PopBt2 = (Page_PopBt) jPage5.getNotify()) != null) {
                            page_PopBt2.updateView(Bt.DATA[9]);
                            break;
                        }
                        break;
                    case 9:
                        int i2 = Bt.DATA[9];
                        if (i2 >= 0 && i2 <= 6 && (jText4 = (JText) jPage5.getChildViewByid(13)) != null) {
                            jText4.setText(App.getApp().getString(App.mStrStates[i2]));
                        }
                        if (!Ipc_New.isTalk()) {
                            JText jText9 = (JText) jPage5.getChildViewByid(16);
                            if (jText9 != null) {
                                jText9.setText(FinalChip.BSP_PLATFORM_Null);
                            }
                            if (!(!App.mStrCustomer.equalsIgnoreCase("TZY_NEW") || (jText3 = (JText) jPage5.getChildViewByid(17)) == null || jText3.getVisibility() == 0)) {
                                jText3.setVisibility(0);
                            }
                        }
                        if (!Ipc_New.this.isCalling(Bt.sLastPhoneState) || (i2 != 2 && i2 != 0)) {
                            if (bv.h() && (page_PopBt3 = (Page_PopBt) jPage5.getNotify()) != null) {
                                page_PopBt3.updateView(Bt.DATA[9]);
                                break;
                            }
                        } else {
                            App.getApp().popBt(false, false);
                            break;
                        }
                    case 12:
                        JText jText10 = (JText) jPage5.getChildViewByid(16);
                        if (jText10 != null) {
                            int i3 = (Bt.DATA[i] + 100) / 1000;
                            if (Ipc_New.isTalk()) {
                                jText10.setText(String.format(Locale.US, "%02d:%02d", new Object[]{Integer.valueOf(i3 / 60), Integer.valueOf(i3 % 60)}));
                                if (!(!App.mStrCustomer.equalsIgnoreCase("TZY_NEW") || (jText = (JText) jPage5.getChildViewByid(17)) == null || jText.getVisibility() == 8)) {
                                    jText.setVisibility(8);
                                    break;
                                }
                            } else {
                                jText10.setText(FinalChip.BSP_PLATFORM_Null);
                                if (!(!App.mStrCustomer.equalsIgnoreCase("TZY_NEW") || (jText2 = (JText) jPage5.getChildViewByid(17)) == null || jText2.getVisibility() == 0)) {
                                    jText2.setVisibility(0);
                                    break;
                                }
                            }
                        }
                        break;
                    case 18:
                        if (Bt.DATA[i] != 1) {
                            z = false;
                        }
                        App.bHFP = z;
                        if ((bv.h() || bv.d()) && (page_PopBt = (Page_PopBt) jPage5.getNotify()) != null) {
                            page_PopBt.updateBtnVoiceSwitch(App.bHFP);
                            break;
                        }
                }
            }
            if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW") && (jPage = App.uiApp.mPages.get(12)) != null) {
                switch (i) {
                    case 8:
                        Page_PopSms page_PopSms = (Page_PopSms) jPage.getNotify();
                        return;
                    default:
                        return;
                }
            }
        }
    };
    IUiNotify notify_Sound = new IUiNotify() {
        public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
            switch (i) {
                case 2:
                    App.bSoundVol = Sound.DATA[i];
                    Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                    while (it.hasNext()) {
                        it.next().updateSoundVol();
                    }
                    break;
                case 10:
                    App.bSoundMode = Sound.DATA[i];
                    Iterator<InterfaceBt> it2 = App.mInterfaceBt.iterator();
                    while (it2.hasNext()) {
                        it2.next().updateSoundMode();
                    }
                    break;
                case 11:
                    App.bSoundLoud = Sound.DATA[i];
                    Iterator<InterfaceBt> it3 = App.mInterfaceBt.iterator();
                    while (it3.hasNext()) {
                        it3.next().updateSoundLoud();
                    }
                    break;
                case 12:
                    App.SB = App.getApp().mIpcObj.getSB();
                    App.iPhoneMicSet = App.SB[2];
                    App.iBtAvMicSet = App.SB[3];
                    break;
                case 13:
                    App.bSoundAMP = Sound.DATA[i];
                    Iterator<InterfaceBt> it4 = App.mInterfaceBt.iterator();
                    while (it4.hasNext()) {
                        it4.next().updateSoundAmp();
                    }
                    break;
                case 24:
                    if (App.bEnableSpec && App.getApp().isAppTop() && Sound.DATA[i] == 0) {
                        Ipc_New.this.EnableGainSpec();
                        break;
                    }
            }
            Iterator<InterfaceBt> it5 = App.mInterfaceBt.iterator();
            while (it5.hasNext()) {
                it5.next().onNotify_Sound(i, iArr, fArr, strArr);
            }
        }
    };
    private IUiNotify notify_Sys = new IUiNotify() {
        public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
            switch (i) {
                case 0:
                    if (IpcUtil.strsOk(strArr, 1) && App.mPkgName.equals(strArr[0])) {
                        if (!App.bChangeAppIdWhenTalking) {
                            App.getApp().mIpcObj.requestAppIdNull();
                            return;
                        } else if (Main.DATA[0] == 3 || Main.DATA[0] == 2) {
                            Main.requestAppId(0);
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private IUiNotify notify_UniCar = new IUiNotify() {
        public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify_UniCar(i, iArr, fArr, strArr);
            }
        }
    };

    static {
        int[] iArr = new int[7];
        iArr[1] = 1;
        iArr[2] = 2;
        iArr[3] = 12;
        iArr[4] = 50;
        iArr[5] = 43;
        iArr[6] = 60;
        NOTIFYCODE_Main_Will = iArr;
        int[] iArr2 = new int[19];
        iArr2[0] = 13;
        iArr2[2] = 1;
        iArr2[3] = 26;
        iArr2[4] = 28;
        iArr2[5] = 2;
        iArr2[6] = 9;
        iArr2[7] = 34;
        iArr2[8] = 7;
        iArr2[9] = 8;
        iArr2[10] = 12;
        iArr2[11] = 6;
        iArr2[12] = 14;
        iArr2[13] = 31;
        iArr2[14] = 18;
        iArr2[15] = 15;
        iArr2[16] = 42;
        iArr2[17] = 43;
        iArr2[18] = 44;
        NOTIFYCODE_Bt = iArr2;
        int[] iArr3 = new int[7];
        iArr3[0] = 13;
        iArr3[2] = 1;
        iArr3[3] = 34;
        iArr3[4] = 9;
        iArr3[5] = 44;
        iArr3[6] = 47;
        NOTIFYCODE_BtAV = iArr3;
        int[] iArr4 = new int[4];
        iArr4[1] = 1;
        iArr4[2] = 2;
        iArr4[3] = 3;
        NOTIFYCODE_UniCar = iArr4;
    }

    public static void LongClick0() {
        setNumKey(12);
    }

    public static void Reset() {
        Bt.PROXY.cmd(17);
        setAutoAnswer(false);
    }

    public static void SetBlackScreen() {
        Main.PROXY.cmd(16, 1);
    }

    public static void SetJumpNavi() {
        Main.PROXY.cmd(24, 0);
    }

    public static void SetMcuOff() {
        Main.PROXY.cmd(17, 0);
    }

    public static void SetRingLevel(int i) {
        Bt.PROXY.cmd(30, i);
    }

    public static void SetStandBy() {
        Main.PROXY.cmd(18, 1);
    }

    public static void appId() {
        if (!isAppIdBtPhone() && App.bChangeAppIdWhenTalking) {
            IpcObj.bAppIdBefore = Main.DATA[0];
        }
        boolean isAppIdBtAv = isAppIdBtAv();
        if ((App.mBtType == 0 || App.mIdPlatForm == 41) && IpcObj.bIsAppIdBtAv) {
        }
        IpcObj.bIsAppIdBtAv = isAppIdBtAv;
        checkAppIdIsRight(1500);
    }

    public static void avCut() {
        Bt.PROXY.cmd(31, 0);
    }

    public static void avLink() {
        Bt.PROXY.cmd(31, 1);
    }

    public static void avNext() {
        Bt.PROXY.cmd(1);
    }

    public static void avPause() {
        Bt.PROXY.cmd(3);
    }

    public static void avPlay() {
        Bt.PROXY.cmd(27);
    }

    public static void avPlayPause() {
        Bt.PROXY.cmd(2);
    }

    public static void avPrev() {
        Bt.PROXY.cmd(0);
    }

    public static void avStop() {
        Bt.PROXY.cmd(4);
    }

    public static void checkAppIdIsRight(int i) {
        Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
        while (it.hasNext()) {
            it.next().checkAppIdIsRight(i);
        }
    }

    public static void clearKey(boolean z) {
        Bt.PROXY.cmd(15, z ? 1 : 0);
    }

    public static void connectDevice(String str) {
        Bt.PROXY.cmd(24, (int[]) null, (float[]) null, new String[]{str});
    }

    public static void connectOBD(String str) {
        Bt.PROXY.cmd(25, (int[]) null, (float[]) null, new String[]{str});
    }

    public static void cut() {
        if (bv.l()) {
            Bt.PROXY.cmd(13, 0);
            return;
        }
        switch (Main.mConf_PlatForm) {
            case 3:
            case 5:
                Bt.PROXY.cmd(13, 0);
                return;
            case 6:
                if (App.mIdCustomer == 53) {
                    Bt.PROXY.cmd(13, 1);
                    return;
                } else {
                    Bt.PROXY.cmd(13, 0);
                    return;
                }
            default:
                Bt.PROXY.cmd(13, 1);
                return;
        }
    }

    public static void deleteConnectedDevice(String str) {
        Bt.PROXY.cmd(40, (int[]) null, (float[]) null, new String[]{str});
    }

    public static void dial() {
        Bt.PROXY.cmd(6, 0);
    }

    public static void dial(String str) {
        Bt.PROXY.cmd(7, (int[]) null, (float[]) null, new String[]{str});
    }

    public static void downDialLog() {
        Bt.PROXY.cmd(35);
    }

    public static void downloadBook() {
        Bt.PROXY.cmd(26);
    }

    public static void hang() {
        Bt.PROXY.cmd(6, 1);
    }

    public static void hang3rdPhoneCurrent() {
        Bt.PROXY.cmd(34);
    }

    public static void hang3rdPhoneHold() {
        Bt.PROXY.cmd(33);
    }

    public static void hangIpPhone() {
        UniCar.PROXY.cmd(1);
    }

    public static void hfp() {
        Bt.PROXY.cmd(14, 2);
    }

    public static boolean isAppIdBtAv() {
        return Main.DATA[0] == 3;
    }

    public static boolean isAppIdBtPhone() {
        return Main.DATA[0] == 2;
    }

    public static boolean isConnect() {
        return Bt.DATA[9] != 0;
    }

    public static boolean isDisConnect() {
        return isDisConnect(Bt.DATA[9]);
    }

    public static boolean isDisConnect(int i) {
        switch (i) {
            case 0:
            case 1:
            case 6:
                return true;
            default:
                return false;
        }
    }

    public static boolean isInCall() {
        switch (Bt.DATA[9]) {
            case 3:
            case 4:
            case 5:
            case 7:
                return true;
            default:
                return false;
        }
    }

    public static boolean isIpPhoneConnect() {
        return UniCar.DATA[3] == 5;
    }

    public static boolean isMerger() {
        return Bt.DATA[9] == 7;
    }

    public static boolean isPairing() {
        return Bt.DATA[9] == 6;
    }

    public static boolean isPhoneConnect() {
        if (Bt.DATA[9] == 2) {
            return true;
        }
        return isInCall();
    }

    public static boolean isRightAppId() {
        return Main.mConf_PlatForm == 1 || App.bChangeAppIdWhenTalking || isAppIdBtAv() || isAppIdBtPhone();
    }

    public static boolean isRing() {
        return Bt.DATA[9] == 4;
    }

    public static boolean isStateConnect() {
        return isStateConnect(Bt.DATA[9]);
    }

    public static boolean isStateConnect(int i) {
        return i == 2;
    }

    public static boolean isStateDialAble() {
        switch (Bt.DATA[9]) {
            case 2:
            case 4:
                return true;
            default:
                return false;
        }
    }

    public static boolean isStateDisconnect() {
        return Bt.DATA[9] == 0;
    }

    public static boolean isTalk() {
        return Bt.DATA[9] == 5;
    }

    public static void link() {
        if (!bv.l()) {
            switch (Main.mConf_PlatForm) {
                case 7:
                case 8:
                    Bt.PROXY.cmd(13, 0);
                    return;
                default:
                    if (isDisConnect()) {
                        switch (Main.mConf_PlatForm) {
                            case 3:
                            case 5:
                                Bt.PROXY.cmd(13, 1);
                                return;
                            case 6:
                                if (App.mIdCustomer == 53) {
                                    Bt.PROXY.cmd(13, 0);
                                    return;
                                } else {
                                    Bt.PROXY.cmd(13, 1);
                                    return;
                                }
                            default:
                                Bt.PROXY.cmd(13, 0);
                                return;
                        }
                    } else {
                        return;
                    }
            }
        } else if (isDisConnect()) {
            Bt.PROXY.cmd(13, 1);
        }
    }

    public static void merge3rdPhone() {
        Bt.PROXY.cmd(39);
    }

    public static void micSwitch() {
        Bt.PROXY.cmd(42);
    }

    public static void queryPairList() {
        Bt.PROXY.cmd(5, 0);
    }

    public static void redial() {
        Bt.PROXY.cmd(8);
    }

    public static void setAutoAnswer(boolean z) {
        Bt.PROXY.cmd(28, z ? 5 : -1);
    }

    public static void setBtSwitch(boolean z) {
        Bt.PROXY.cmd(29, z ? 1 : 0);
    }

    public static void setDevName(String str) {
        Bt.PROXY.cmd(19, (int[]) null, (float[]) null, new String[]{str});
    }

    public static void setDevPin(String str) {
        Bt.PROXY.cmd(18, (int[]) null, (float[]) null, new String[]{str});
    }

    public static void setNum(String str) {
        Bt.PROXY.cmd(12, (int[]) null, (float[]) null, new String[]{str});
    }

    public static void setNumKey(int i) {
        Bt.PROXY.cmd(6, i);
    }

    public static void startDiscover() {
        Bt.PROXY.cmd(20, 1);
    }

    public static void stopDiscover() {
        Bt.PROXY.cmd(20, 0);
    }

    public static void switch3rdPhone() {
        Bt.PROXY.cmd(32);
    }

    public static void test(String str) {
        if (TextUtils.isEmpty(str)) {
            Bt.PROXY.cmd(21);
            return;
        }
        Bt.PROXY.cmd(21, (int[]) null, (float[]) null, new String[]{str});
    }

    public static void updateBt(String str) {
        Bt.PROXY.cmd(38, (int[]) null, (float[]) null, new String[]{str});
        bt.a().b(FinalChip.BSP_PLATFORM_Null);
    }

    public void DisableGainSpec() {
        Sound.PROXY.cmd(21, 0);
    }

    public void EnableGainSpec() {
        Sound.PROXY.cmd(21, 1);
    }

    public void FuncDial() {
        if (Bt.sPhoneNumber.startsWith("#") && Bt.sPhoneNumber.endsWith("#") && Bt.sPhoneNumber.length() == 5 && Bt.DATA[9] == 0) {
            test(Bt.sPhoneNumber.substring(1, 4));
        } else if (!Bt.sPhoneNumber.equals("*") || Bt.DATA[9] != 0) {
            if (Bt.DATA[9] != 2 && Bt.DATA[9] != 4) {
                cb.a().a(App.getApp().getString("bt_state_cannot_dial"));
            } else if (!FinalChip.BSP_PLATFORM_Null.equals(Bt.sPhoneNumber) && Bt.DATA[9] != 5) {
                dial();
            } else if (Bt.DATA[9] == 2) {
                redial();
            }
        } else if (Main.mConf_PlatForm == 5) {
            if (!App.bFactoryTest) {
                String readStringFromFile = readStringFromFile();
                App.bFactoryTest = true;
                App.startThread(App.StrThreadFactoryTest, new Runnable() {
                    public void run() {
                        while (App.bFactoryTest) {
                            if (App.iOverTime < 10) {
                                App.iOverTime++;
                                if (App.iOverTime == 10) {
                                    App.iOverTime = 0;
                                    App.bFactoryTest = false;
                                    return;
                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, false, 1);
                test(readStringFromFile);
            }
        } else if (Main.mConf_PlatForm == 7 || Main.mConf_PlatForm == 8) {
            test("test");
        } else {
            test((String) null);
        }
    }

    public boolean FuncDialIpPhone() {
        if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
            return false;
        }
        if (isConnect()) {
            App.bCutBtByIpPhone = true;
            cut();
        }
        UniCar.PROXY.cmd(0, Bt.sPhoneNumber);
        return true;
    }

    public void LauncherRequestAppIdBtAv() {
        Main.requestAppId(3);
    }

    public void addNotify_PopBt() {
        this.notify_Bt_Pop = this.notify_PopBt;
    }

    public void checkFirstDialOrRing() {
        if (!isDisConnect(Bt.sLastPhoneState) && isDisConnect()) {
            Main.postRunnable_Ui(true, App.getApp().mIpcObj.mRunnable_ClearContactAndHistory_Disp);
        } else if (isDisConnect()) {
        } else {
            if (isDisConnect(Bt.sLastPhoneState) || !App.bHaveScanDB) {
                Main.postRunnable_Ui(true, App.getApp().mIpcObj.mRunnable_scanDB, 3000);
            }
        }
    }

    public void cmdNum(int i) {
        setNumKey(NumCmd[i]);
    }

    public float getElectricValue() {
        if (isPauseDraw()) {
            return 0.0f;
        }
        return (float) Sound.getSpectrumValue();
    }

    public int[] getSB() {
        return Sound.SB;
    }

    public void initIpc() {
        Bt.mUiNotifyEvent.uiNotify = this.notify_Bt;
        Main.mUiNotifyEvent.uiNotify = this.notify_Main;
        Sound.mUiNotifyEvent.uiNotify = this.notify_Sound;
        this.mConn = new Conn(App.getApp(), App.getApp());
        this.mConn.addObserver(Bt.getInstance());
        this.mConn.addObserver(Sound.getInstance());
        this.mConnSys = new ConnSys(App.getApp(), App.getApp());
        Sys.mUiNotifyEvent.uiNotify = this.notify_Sys;
    }

    public boolean isCalling() {
        return isCalling(Bt.DATA[9]);
    }

    public boolean isCalling(int i) {
        switch (i) {
            case 3:
            case 4:
            case 5:
                return true;
            default:
                return false;
        }
    }

    public boolean isMute() {
        return Sound.DATA[3] == 1 || Sound.DATA[2] == 0;
    }

    public boolean isPBAPStateLoad() {
        return Bt.DATA[29] == 2;
    }

    public boolean isPauseDraw() {
        return isDisConnect() || Bt.DATA[13] != 1;
    }

    public void phoneState() {
        if (App.bIsAppAmapAssistInstall) {
            if (isDisConnect()) {
                App.getApp().sendCmd2AmapAssist("bt_disconnect");
            } else {
                App.getApp().sendCmd2AmapAssist("bt_connect");
            }
        }
        checkFirstDialOrRing();
        switch (Bt.DATA[9]) {
            case 0:
                if (isCalling(Bt.sLastPhoneState)) {
                    clearKey(true);
                    if (Bt.values.getAsInteger("type") != null) {
                        App.mBtInfo.insertCallLog(Bt.values, true);
                        break;
                    }
                }
                break;
            case 2:
                if (isCalling(Bt.sLastPhoneState)) {
                    clearKey(true);
                    if (Bt.values.getAsInteger("type") != null) {
                        App.mBtInfo.insertCallLog(Bt.values, true);
                    }
                }
                if (isDisConnect(Bt.sLastPhoneState)) {
                    bt.a(App.getApp(), Bt.DATA[9], 0);
                    break;
                }
                break;
            case 3:
                Bt.values.clear();
                Bt.values.put("type", 2);
                Bt.values.put("date", Long.valueOf(System.currentTimeMillis()));
                Bt.values.put("number", Bt.sPhoneNumber);
                if (!TextUtils.isEmpty(Bt.sPhoneNumber)) {
                    App.strNumberBak = Bt.sPhoneNumber;
                    break;
                }
                break;
            case 4:
                if (Bt.values.getAsInteger("type") == null) {
                    Bt.values.clear();
                    Bt.values.put("type", 3);
                    Bt.values.put("date", Long.valueOf(System.currentTimeMillis()));
                    Bt.values.put("number", Bt.sPhoneNumber);
                    break;
                }
                break;
            case 5:
                if (Bt.values.getAsInteger("type") != null && Bt.sPhoneNumber.equals(Bt.values.get("number")) && Bt.values.getAsInteger("type").intValue() == 3) {
                    Bt.values.put("type", 1);
                }
                if (Bt.values3rd.getAsInteger("type") != null && Bt.sPhoneNumber.equals(Bt.values3rd.get("number"))) {
                    Bt.values3rd.put("type", 1);
                    break;
                }
        }
        switch (App.mIdCustomer) {
            case 66:
                Bundle bundle = new Bundle();
                bundle.putString("bt_phone_state", (Bt.DATA[9] == 2 || Bt.DATA[9] == 3 || Bt.DATA[9] == 4 || Bt.DATA[9] == 5) ? "1" : "0");
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setAction("com.syu.widget.BtavService");
                intent.setPackage("com.android.launcher3");
                App.getApp().startService(intent);
                App.getApp().mIpcObj.updateNotify_BtAv();
                return;
            default:
                return;
        }
    }

    public void phoneState(ActBt actBt) {
        boolean z;
        if (!App.bFirstBtAvPage) {
            int i = Bt.DATA[9];
            if (!isCalling(Bt.sLastPhoneState) || isCalling(i)) {
                if (Bt.sLastPhoneState != 0 && i == 0 && !App.sContactsSaveFlag) {
                    actBt.ClearAllContact();
                }
            } else if (App.sBackFlag != 0) {
                if (App.mEnableHalfScreen) {
                    if ((App.sBackFlag & 4) != 0) {
                        App.getApp().setFullScreenMode(2);
                    } else if ((App.sBackFlag & 16) != 0) {
                        App.getApp().setFullScreenMode(1);
                    }
                }
                if (App.bHidePopBt && (App.sBackFlag & 32) != 0) {
                    SetJumpNavi();
                }
                if ((App.sBackFlag & 2) != 0) {
                    actBt.moveTaskToBack(true);
                    z = true;
                } else {
                    z = false;
                }
                if ((App.sBackFlag & 1) != 0) {
                    if (!z) {
                        actBt.goPage(App.sPageBak, true);
                    }
                    App.sPage = App.sPageBak;
                }
                App.sBackFlag = 0;
            }
            if (isCalling()) {
                int i2 = 3;
                if (App.mEnableHalfScreen || App.bCallinPageFlag) {
                    i2 = 22;
                }
                actBt.DismissPopContacts();
                if (App.sPage != i2) {
                    App.sPageBak = App.sPage;
                    App.sBackFlag |= 1;
                    actBt.goPage(i2, true);
                }
            }
            if (App.hideBtnWhenDisconnect) {
                actBt.setMenuBtnEnable(App.sPage, i);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0041, code lost:
        r1 = new java.io.File("/mnt/usb_storage/USB_DISK" + r3 + "/udisk0/BTAddr.txt");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String readStringFromFile() {
        /*
            r5 = this;
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0087 }
            java.lang.String r2 = "/mnt/external_sd/BTAddr.txt"
            r1.<init>(r2)     // Catch:{ Exception -> 0x0087 }
            boolean r2 = r1.exists()     // Catch:{ Exception -> 0x0087 }
            if (r2 != 0) goto L_0x0019
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0087 }
            java.lang.String r2 = "/mnt/external_sdio/BTAddr.txt"
            r1.<init>(r2)     // Catch:{ Exception -> 0x0087 }
            boolean r2 = r1.exists()     // Catch:{ Exception -> 0x0087 }
        L_0x0019:
            if (r2 != 0) goto L_0x0026
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0087 }
            java.lang.String r2 = "/mnt/usb_storage/BTAddr.txt"
            r1.<init>(r2)     // Catch:{ Exception -> 0x0087 }
            boolean r2 = r1.exists()     // Catch:{ Exception -> 0x0087 }
        L_0x0026:
            if (r2 != 0) goto L_0x002d
            r3 = 0
        L_0x0029:
            r4 = 21
            if (r3 < r4) goto L_0x0041
        L_0x002d:
            if (r2 != 0) goto L_0x0064
            cb r1 = defpackage.cb.a()     // Catch:{ Exception -> 0x0087 }
            com.syu.app.App r2 = com.syu.app.App.getApp()     // Catch:{ Exception -> 0x0087 }
            java.lang.String r3 = "bt_notinsertdevice"
            java.lang.String r2 = r2.getString(r3)     // Catch:{ Exception -> 0x0087 }
            r1.a(r2)     // Catch:{ Exception -> 0x0087 }
        L_0x0040:
            return r0
        L_0x0041:
            java.io.File r1 = new java.io.File     // Catch:{ Exception -> 0x0087 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0087 }
            java.lang.String r4 = "/mnt/usb_storage/USB_DISK"
            r2.<init>(r4)     // Catch:{ Exception -> 0x0087 }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Exception -> 0x0087 }
            java.lang.String r4 = "/udisk0/BTAddr.txt"
            java.lang.StringBuilder r2 = r2.append(r4)     // Catch:{ Exception -> 0x0087 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0087 }
            r1.<init>(r2)     // Catch:{ Exception -> 0x0087 }
            boolean r2 = r1.exists()     // Catch:{ Exception -> 0x0087 }
            if (r2 != 0) goto L_0x002d
            int r3 = r3 + 1
            goto L_0x0029
        L_0x0064:
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0087 }
            java.io.FileReader r3 = new java.io.FileReader     // Catch:{ Exception -> 0x0087 }
            r3.<init>(r1)     // Catch:{ Exception -> 0x0087 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x0087 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0087 }
            r1.<init>()     // Catch:{ Exception -> 0x0087 }
        L_0x0073:
            java.lang.String r3 = r2.readLine()     // Catch:{ Exception -> 0x0087 }
            if (r3 != 0) goto L_0x0083
            if (r2 == 0) goto L_0x007e
            r2.close()     // Catch:{ Exception -> 0x0087 }
        L_0x007e:
            java.lang.String r0 = r1.toString()     // Catch:{ Exception -> 0x0087 }
            goto L_0x0040
        L_0x0083:
            r1.append(r3)     // Catch:{ Exception -> 0x0087 }
            goto L_0x0073
        L_0x0087:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0040
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.app.ipc.Ipc_New.readStringFromFile():java.lang.String");
    }

    public void recoverAppId() {
        if (!isRightAppId()) {
            requestAppIdBtPhone();
        }
    }

    public void removeNotify_PopBt() {
        this.notify_Bt_Pop = null;
    }

    public void requestAppIdBtAv() {
        Main.requestAppIdByOnTop(3);
    }

    public void requestAppIdBtPhone() {
        if (!App.bChangeAppIdWhenTalking) {
            Main.requestAppIdByOnTop(2);
        } else if (isInCall()) {
            Main.requestAppIdByOnTop(2);
        } else if (IpcObj.bAppIdBefore >= 0) {
            Main.requestAppIdByOnTop(IpcObj.bAppIdBefore);
        }
    }

    public void requestAppIdNull() {
        if (isAppIdBtAv()) {
            avPause();
            if (!bv.c()) {
                avStop();
            }
        }
        if (isRightAppId()) {
            Main.requestAppId(0);
        }
    }

    public void requestAppIdNull(int i) {
        if (!App.bIsLauncher_2Ico) {
            if (isAppIdBtAv()) {
                avPause();
                avStop();
            }
            if (isAppIdBtAv() || isAppIdBtPhone()) {
                Main.requestAppId(0);
            }
        } else if (isAppIdBtPhone()) {
            Main.requestAppId(0);
        } else if (isAppIdBtAv() && i == 10) {
            avPause();
            avStop();
            Main.requestAppId(0);
        }
    }

    public void sendCmdAmp(int i) {
        Sound.PROXY.cmd(7, i);
    }

    public void sendCmdLoud(int i) {
        Sound.PROXY.cmd(5, i);
    }

    public void sendCmdVolBal(int i, int i2) {
        Sound.PROXY.cmd(6, i, i2);
    }

    public void updateNotify_BtAv() {
        for (int i : NOTIFYCODE_BtAV) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_BtAv_HalfScreen() {
        for (int i : NOTIFYCODE_BtAV) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Callin() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Callin_HalfScreen() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Callin_HideKey() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Callin_ShowCall() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Callin_ShowCall_HalfScreen() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Callin_ShowDial() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Callin_ShowDial_HalfScreen() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Callin_ShowKey() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Callin_ShowKey_HalfScreen() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Dial() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Dial_HalfScreen() {
        for (int i : NOTIFYCODE_DIAL) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }

    public void updateNotify_Set() {
        for (int i : NOTIFYCODE_SET) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }
}
