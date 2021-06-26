package com.syu.ipcself.module.main;

import android.content.ContentValues;
import android.os.RemoteException;
import com.syu.data.FinalChip;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class Bt extends IModuleCallback.Stub implements IConnStateListener {
    public static int[] DATA = new int[50];
    private static Bt INSTANCE = new Bt();
    public static ModuleProxy PROXY = new ModuleProxy();
    public static UiNotifyEvent mUiNotifyEvent = new UiNotifyEvent();
    public static String sChoiceAddr = FinalChip.BSP_PLATFORM_Null;
    public static String sDevAddr = FinalChip.BSP_PLATFORM_Null;
    public static String sDevName = FinalChip.BSP_PLATFORM_Null;
    public static String sDevPin = FinalChip.BSP_PLATFORM_Null;
    public static String sID3Album;
    public static String sId3Artist;
    public static String sId3Title;
    public static int sLastPhoneState;
    public static String sPhoneAddr = FinalChip.BSP_PLATFORM_Null;
    public static String sPhoneName = FinalChip.BSP_PLATFORM_Null;
    public static String sPhoneNumber = FinalChip.BSP_PLATFORM_Null;
    public static String sPhoneNumberHoldOn = FinalChip.BSP_PLATFORM_Null;
    public static ContentValues values = new ContentValues();
    public static ContentValues values3rd = new ContentValues();

    private class Runnable_Update implements Runnable {
        public float[] flts;
        public int[] ints;
        public String[] strs;
        public int updateCode;

        public Runnable_Update(int i, int[] iArr, float[] fArr, String[] strArr) {
            this.updateCode = i;
            this.ints = iArr;
            this.flts = fArr;
            this.strs = strArr;
        }

        public void run() {
            if (this.updateCode >= 0 && this.updateCode < 50) {
                switch (this.updateCode) {
                    case 0:
                        Bt.id3Title(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 1:
                        Bt.id3Artist(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 2:
                    case 11:
                    case 12:
                    case 13:
                    case 20:
                    case 21:
                    case 28:
                    case 29:
                        Bt.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 3:
                    case 4:
                        return;
                    case 6:
                        Bt.phoneAddr(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 7:
                        Bt.phoneName(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 8:
                        Bt.phoneNumber(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 14:
                        Bt.localAddr(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 15:
                        Bt.devName(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 16:
                        Bt.devPin(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 22:
                        if (IpcUtil.strsOk(this.strs, 2)) {
                            Bt.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                            return;
                        }
                        return;
                    case 26:
                        Bt.id3Album(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    default:
                        if (IpcUtil.intsOk(this.ints, 1) && this.ints[0] != Bt.DATA[this.updateCode]) {
                            if (this.updateCode == 9) {
                                Bt.sLastPhoneState = Bt.DATA[this.updateCode];
                            }
                            Bt.DATA[this.updateCode] = this.ints[0];
                        }
                        Bt.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                }
            }
        }
    }

    private Bt() {
    }

    public static void devName(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            sDevName = FinalChip.BSP_PLATFORM_Null;
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(sDevName)) {
            sDevName = strArr[0];
            sDevName = sDevName.replace("\n", FinalChip.BSP_PLATFORM_Null);
            sDevName = sDevName.replace("\r", FinalChip.BSP_PLATFORM_Null);
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void devPin(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            sDevPin = FinalChip.BSP_PLATFORM_Null;
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(sDevPin)) {
            sDevPin = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static Bt getInstance() {
        return INSTANCE;
    }

    public static void id3Album(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            sID3Album = FinalChip.BSP_PLATFORM_Null;
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(sID3Album)) {
            sID3Album = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void id3Artist(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            sId3Artist = FinalChip.BSP_PLATFORM_Null;
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(sId3Artist)) {
            sId3Artist = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void id3Title(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            sId3Title = FinalChip.BSP_PLATFORM_Null;
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(sId3Title)) {
            sId3Title = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void localAddr(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            sDevAddr = FinalChip.BSP_PLATFORM_Null;
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(sDevAddr)) {
            sDevAddr = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void phoneAddr(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            sPhoneAddr = FinalChip.BSP_PLATFORM_Null;
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(sPhoneAddr)) {
            sPhoneAddr = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void phoneName(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            sPhoneName = FinalChip.BSP_PLATFORM_Null;
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(sPhoneName)) {
            sPhoneName = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void phoneNumber(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.strsOk(strArr, 1)) {
            if (IpcUtil.strsOk(strArr, 2)) {
                if (strArr[0] == null) {
                    sPhoneNumberHoldOn = FinalChip.BSP_PLATFORM_Null;
                } else {
                    sPhoneNumberHoldOn = strArr[0];
                }
                if (strArr[1] == null) {
                    sPhoneNumber = FinalChip.BSP_PLATFORM_Null;
                } else {
                    sPhoneNumber = strArr[1];
                }
            } else {
                sPhoneNumberHoldOn = FinalChip.BSP_PLATFORM_Null;
                if (strArr[0] == null) {
                    sPhoneNumber = FinalChip.BSP_PLATFORM_Null;
                } else {
                    sPhoneNumber = strArr[0];
                }
            }
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void updateNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1) && DATA[i] != iArr[0]) {
            DATA[i] = iArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(2));
            Bt instance = getInstance();
            for (int i = 0; i < 50; i++) {
                PROXY.register(instance, i, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDisconnected() {
        PROXY.setRemoteModule((IRemoteModule) null);
    }

    public void update(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        Main.postRunnable_Ui(false, new Runnable_Update(i, iArr, fArr, strArr));
    }
}
