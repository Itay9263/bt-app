package com.syu.app.ipc;

import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import com.syu.app.App;
import com.syu.bt.act.ActBt;
import com.syu.bt.act.InterfaceBt;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipc.module.Bt786;
import com.syu.ipc.module.Main786;
import com.syu.ipc.module.Sound786;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.util.IUiNotify;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class Ipc_786 {
    public static final int[] NOTIFYCODE_Bt = {2, 18, 19, 1, 14, 5, 9, 17};
    public static final int[] NOTIFYCODE_BtAV = {2, 18, 19, 1};
    static final int[] NOTIFYCODE_Bt_Will = {12, 13, 23, 21};
    public static final int[] NOTIFYCODE_DIAL = {1, 14, 5, 9};
    public static final int[] NOTIFYCODE_Main_Will;
    static final int[] NOTIFYCODE_PopBt = {1, 5, 9};
    public static final int[] NOTIFYCODE_SET = {6, 8, 11};
    static final int[] NumCmd = {23, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24};
    public Bt786 mBtConn = null;
    public Main786 mMainConn = null;
    public Ipc_786NotifyPage mNotify = new Ipc_786NotifyPage();
    public Sound786 mSoundConn = null;
    IUiNotify notify_Bt = new IUiNotify() {
        public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
            switch (i) {
                case 1:
                    Ipc_786.this.phoneState();
                    Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                    while (it.hasNext()) {
                        it.next().phoneState();
                    }
                    break;
                case 6:
                    String diyName = App.getDiyName();
                    String diyPin = App.getDiyPin();
                    if (!TextUtils.isEmpty(diyName) && !diyName.equals(Bt.sDevName)) {
                        Ipc_786.setDevName(diyName);
                    }
                    if (!TextUtils.isEmpty(diyPin) && !diyPin.equals(Bt.sDevPin)) {
                        Ipc_786.setDevPin(diyPin);
                        break;
                    }
            }
            Iterator<InterfaceBt> it2 = App.mInterfaceBt.iterator();
            while (it2.hasNext()) {
                it2.next().onNotify(i, iArr, fArr, strArr);
            }
            if (Ipc_786.this.notify_Bt_Pop != null) {
                Ipc_786.this.notify_Bt_Pop.onNotify(i, iArr, fArr, strArr);
            }
        }
    };
    IUiNotify notify_Bt_Pop = null;
    IUiNotify notify_Main = new IUiNotify() {
        public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
            switch (i) {
                case 0:
                    IpcObj.mcuOn(i, Main.DATA[i]);
                    return;
                case 3:
                    Ipc_786.appId();
                    return;
                default:
                    return;
            }
        }
    };
    private IUiNotify notify_PopBt = new IUiNotify() {
        public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
            JText jText;
            JText jText2;
            char c = 2;
            JPage jPage = App.uiApp.mPages.get(1);
            JPage jPage2 = jPage == null ? App.uiApp.mPages.get(3) : jPage;
            if (jPage2 != null) {
                switch (i) {
                    case 1:
                        int i2 = Bt.DATA[i];
                        switch (i2) {
                            case 0:
                                c = 0;
                                break;
                            case 1:
                                break;
                            case 2:
                                c = 6;
                                break;
                            case 3:
                                c = 1;
                                break;
                            case 4:
                                c = 4;
                                break;
                            case 5:
                                c = 5;
                                break;
                            case 6:
                                c = 3;
                                break;
                            default:
                                c = 65535;
                                break;
                        }
                        if (c >= 0 && (jText2 = (JText) jPage2.getChildViewByid(13)) != null) {
                            jText2.setText(App.getApp().getString(App.mStrStates[c]));
                        }
                        if (!Ipc_786.isTalk() && (jText = (JText) jPage2.getChildViewByid(16)) != null) {
                            jText.setText(FinalChip.BSP_PLATFORM_Null);
                        }
                        if (!Ipc_786.this.isCalling(Bt.sLastPhoneState)) {
                            return;
                        }
                        if (i2 == 1 || i2 == 0) {
                            App.getApp().popBt(false, false);
                            return;
                        }
                        return;
                    case 5:
                        JText jText3 = (JText) jPage2.getChildViewByid(14);
                        if (jText3 != null) {
                            jText3.setText(Bt.sPhoneNumber);
                        }
                        JText jText4 = (JText) jPage2.getChildViewByid(17);
                        if (jText4 == null) {
                            return;
                        }
                        if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                            jText4.setText(FinalChip.BSP_PLATFORM_Null);
                            return;
                        }
                        ArrayList arrayList = new ArrayList();
                        arrayList.addAll(App.mBtInfo.mListContact);
                        App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText4, App.getApp().getString(EnvironmentCompat.MEDIA_UNKNOWN), false), false, 5);
                        return;
                    case 9:
                        JText jText5 = (JText) jPage2.getChildViewByid(16);
                        if (jText5 != null) {
                            int i3 = (Bt.DATA[i] + 100) / 1000;
                            if (!Ipc_786.isTalk()) {
                                jText5.setText(FinalChip.BSP_PLATFORM_Null);
                                return;
                            } else {
                                jText5.setText(String.format(Locale.US, "%02d:%02d", new Object[]{Integer.valueOf(i3 / 60), Integer.valueOf(i3 % 60)}));
                                return;
                            }
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    };

    static {
        int[] iArr = new int[4];
        iArr[0] = 3;
        iArr[2] = 41;
        iArr[3] = 43;
        NOTIFYCODE_Main_Will = iArr;
    }

    public static void LongClick0() {
        setNumKey(25);
    }

    public static void Reset() {
        Bt786.cmdBt(36, -2, (String) null);
        setAutoAnswer(false);
    }

    public static void appId() {
        boolean isAppIdBtAv = isAppIdBtAv();
        if (!IpcObj.bIsAppIdBtAv && isAppIdBtAv) {
            avPlay();
        }
        IpcObj.bIsAppIdBtAv = isAppIdBtAv;
    }

    public static void avNext() {
        Bt786.cmdBt(31, -2, (String) null);
    }

    public static void avPause() {
        Bt786.cmdBt(28, -2, (String) null);
    }

    public static void avPlay() {
        Bt786.cmdBt(27, -2, (String) null);
    }

    public static void avPlayPause() {
        Bt786.cmdBt(29, -2, (String) null);
    }

    public static void avPrev() {
        Bt786.cmdBt(26, -2, (String) null);
    }

    public static void avStop() {
        Bt786.cmdBt(30, -2, (String) null);
    }

    public static void clearKey(boolean z) {
        if (z) {
            int length = Bt.sPhoneNumber.length();
            for (int i = 0; i < length; i++) {
                Bt786.cmdBt(0, -2, (String) null);
            }
            return;
        }
        Bt786.cmdBt(0, -2, (String) null);
    }

    public static void connectDevice(String str) {
        Bt786.cmdBt(42, -2, str);
    }

    public static void connectOBD(String str) {
        Bt786.cmdBt(43, -2, str);
    }

    public static void cut() {
        Bt786.cmdBt(11, -2, (String) null);
    }

    public static void dial() {
        Bt786.cmdBt(4, -2, (String) null);
    }

    public static void downloadBook() {
        Bt786.cmdBt(37, -2, (String) null);
    }

    public static void hang() {
        Bt786.cmdBt(6, -2, (String) null);
    }

    public static void hfp() {
        Bt786.cmdBt(9, -2, (String) null);
    }

    public static boolean isAppIdBtAv() {
        return Main.DATA[3] == 9;
    }

    public static boolean isAppIdBtPhone() {
        return Main.DATA[3] == 7;
    }

    public static boolean isConnect() {
        return Bt.DATA[1] != 0;
    }

    public static boolean isDisConnect() {
        return isDisConnect(Bt.DATA[1]);
    }

    public static boolean isDisConnect(int i) {
        switch (i) {
            case 0:
            case 2:
            case 3:
                return true;
            default:
                return false;
        }
    }

    public static boolean isInCall() {
        switch (Bt.DATA[1]) {
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    public static boolean isPairing() {
        return Bt.DATA[1] == 2;
    }

    public static boolean isRightAppId() {
        return Main.mConf_PlatForm == 1 || isAppIdBtAv() || isAppIdBtPhone();
    }

    public static boolean isRing() {
        return Bt.DATA[1] == 4;
    }

    public static boolean isStateConnect() {
        return Bt.DATA[1] == 1;
    }

    public static boolean isStateDialAble() {
        switch (Bt.DATA[1]) {
            case 1:
            case 4:
                return true;
            default:
                return false;
        }
    }

    public static boolean isStateDisconnect() {
        return Bt.DATA[1] == 0;
    }

    public static boolean isTalk() {
        return Bt.DATA[1] == 5;
    }

    public static void link() {
        Bt786.cmdBt(10, -2, (String) null);
    }

    public static void queryPairList() {
        Bt786.cmdBt(41, -2, (String) null);
    }

    public static void redial() {
        Bt786.cmdBt(5, -2, (String) null);
    }

    public static void setAutoAnswer(boolean z) {
        Bt786.cmdBt(33, z ? 5 : -1, (String) null);
    }

    public static void setDevName(String str) {
        Bt786.cmdBt(35, -2, str);
    }

    public static void setDevPin(String str) {
        Bt786.cmdBt(34, -2, str);
    }

    public static void setNum(String str) {
        Bt786.cmdBt(3, -2, IpcObj.filterNumber(str));
    }

    public static void setNumKey(int i) {
        Bt786.cmdBt(i, -2, (String) null);
    }

    public static void startDiscover() {
        Bt786.cmdBt(39, -2, (String) null);
    }

    public static void stopDiscover() {
        Bt786.cmdBt(40, -2, (String) null);
    }

    public static void test(String str) {
        Bt786.cmdBt(44, -2, str);
    }

    public static void updateBt(String str) {
        Bt786.cmdBt(44, -2, str);
    }

    public void FuncDial() {
        if (Bt.sPhoneNumber.startsWith("#") && Bt.sPhoneNumber.endsWith("#") && Bt.sPhoneNumber.length() == 5 && Bt.DATA[1] == 0) {
            test(Bt.sPhoneNumber.substring(1, 4));
        } else if (Bt.sPhoneNumber.equals("*") && Bt.DATA[1] == 0) {
            test((String) null);
        } else if (Bt.DATA[1] != 1 && Bt.DATA[1] != 4) {
            cb.a().a(App.getApp().getString("bt_state_cannot_dial"));
        } else if (!FinalChip.BSP_PLATFORM_Null.equals(Bt.sPhoneNumber)) {
            dial();
        } else if (Bt.DATA[1] == 1) {
            redial();
        }
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
                Main.postRunnable_Ui(true, App.getApp().mIpcObj.mRunnable_scanDB);
            }
        }
    }

    public void cmdNum(int i) {
        setNumKey(NumCmd[i]);
    }

    public void initIpc() {
        this.mMainConn = new Main786(App.getApp(), App.getApp());
        this.mSoundConn = new Sound786(App.getApp(), App.getApp());
        this.mBtConn = new Bt786(App.getApp(), App.getApp());
        Bt.mUiNotifyEvent.uiNotify = this.notify_Bt;
        Main.mUiNotifyEvent.uiNotify = this.notify_Main;
    }

    public boolean isCalling() {
        return isCalling(Bt.DATA[1]);
    }

    public boolean isCalling(int i) {
        switch (i) {
            case 4:
            case 5:
            case 6:
                return true;
            default:
                return false;
        }
    }

    public boolean isMute() {
        return Main.DATA[1] == 1 || Main.DATA[2] == 0;
    }

    public boolean isPBAPStateLoad() {
        return Bt.DATA[1] == 8;
    }

    public boolean isPauseDraw() {
        return isDisConnect() || Bt.DATA[4] != 1;
    }

    public void phoneState() {
        checkFirstDialOrRing();
        switch (Bt.DATA[1]) {
            case 0:
            case 1:
                if (isCalling(Bt.sLastPhoneState)) {
                    clearKey(true);
                    App.mBtInfo.insertCallLog(Bt.values, true);
                    return;
                }
                return;
            case 4:
                Bt.values.clear();
                Main.postRunnable_Ui(false, new Runnable() {
                    public void run() {
                        Bt.values.put("type", 3);
                        Bt.values.put("date", Long.valueOf(System.currentTimeMillis()));
                        Bt.values.put("number", Bt.sPhoneNumber);
                    }
                }, 500);
                return;
            case 5:
                if (Bt.values.getAsInteger("type") != null && Bt.values.getAsInteger("type").intValue() == 3) {
                    Bt.values.put("type", 1);
                    return;
                }
                return;
            case 6:
                Bt.values.clear();
                Main.postRunnable_Ui(false, new Runnable() {
                    public void run() {
                        Bt.values.put("type", 2);
                        Bt.values.put("date", Long.valueOf(System.currentTimeMillis()));
                        Bt.values.put("number", Bt.sPhoneNumber);
                    }
                }, 500);
                return;
            default:
                return;
        }
    }

    public void phoneState(ActBt actBt) {
        boolean z;
        int i = Bt.DATA[1];
        if (!isCalling(Bt.sLastPhoneState) || isCalling(i)) {
            if (Bt.sLastPhoneState != 0 && i == 0 && !App.sContactsSaveFlag) {
                actBt.ClearAllContact();
            }
        } else if (App.sBackFlag != 0) {
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
            if (App.sPage != 3) {
                App.sPageBak = App.sPage;
                App.sBackFlag |= 1;
            }
            actBt.goPage(3, true);
        }
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
        Main.requestAppIdByOnTop(9);
    }

    public void requestAppIdBtPhone() {
        Main.requestAppIdByOnTop(7);
    }

    public void requestAppIdNull() {
        if (isAppIdBtAv()) {
            avStop();
        }
        if (isRightAppId()) {
            Main.requestAppId(11);
        }
    }

    public void updateNotify_BtAv() {
        for (int i : NOTIFYCODE_BtAV) {
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

    public void updateNotify_Set() {
        for (int i : NOTIFYCODE_SET) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().onNotify(i, (int[]) null, (float[]) null, (String[]) null);
            }
        }
    }
}
