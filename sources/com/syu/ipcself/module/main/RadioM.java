package com.syu.ipcself.module.main;

import android.os.RemoteException;
import com.syu.data.FinalChip;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class RadioM extends IModuleCallback.Stub implements IConnStateListener {
    public static int[] DATA = new int[100];
    public static int[] FREQ_AM = new int[12];
    public static final int FREQ_AM_CNT = 12;
    public static String[] FREQ_AM_TEXT = new String[12];
    public static int[] FREQ_FM = new int[18];
    public static final int FREQ_FM_CNT = 18;
    public static String[] FREQ_FM_TEXT = new String[18];
    private static RadioM INSTANCE = new RadioM();
    public static ModuleProxy PROXY = new ModuleProxy();
    public static final String[] PTY_Display = {" NO PTY ", "  News  ", "Affairs ", "  Info  ", " Sport  ", "Educate ", " Drama  ", "Culture ", "Science ", " Varied ", " Pop M  ", " Rock M ", " Easy M ", "Light M ", "Classics", "Other M ", "Weather ", "Finance ", "Children", " Social ", "Religion", "Phone In", " Travel ", "Leisure ", "  Jazz  ", "Country ", "Nation M", " Oldies ", " Folk M ", "Document", "  Test  ", " Alarm  ", "PTY Seek", "        ", " TA ON  ", " TA OFF ", " TA SEEK", " AF ON  ", " AF OFF ", " AF SEEK", "PTY SEEK"};
    public static int[] mMaxAm = {1720, 1620, 1620, 1620, 1629};
    public static int[] mMaxFm = {10790, 10800, 10800, mMaxFm1, 9000};
    public static int mMaxFm1 = 7400;
    public static int mMaxFm2 = 10800;
    public static int[] mMinAm = {530, 520, 522, 522, 522};
    public static int[] mMinFm = {8750, 8750, 8750, mMinFm1, 7600};
    public static int mMinFm1 = 6500;
    public static int mMinFm2 = 8750;
    public static int[] mStepAm = {10, 10, 9, 9, 9};
    public static int[] mStepFm = {20, 5, 5, mStepFm1, 10};
    public static int mStepFm1 = 3;
    public static int mStepFm2 = 10;
    public static int[] mTotalAm = {119, 110, 122, 122, 123};
    public static int[] mTotalFm = {102, 410, 410, 300, 140};
    public static UiNotifyEvent mUiNotifyEvent = new UiNotifyEvent();
    public static int sFreqMax;
    public static int sFreqMin;
    public static int sFreqStepCnt;
    public static int sFreqStepLen;
    public static String sPsText = FinalChip.BSP_PLATFORM_Null;
    public static String sRdsText = FinalChip.BSP_PLATFORM_Null;

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
            if (this.updateCode >= 0 && this.updateCode < 100) {
                switch (this.updateCode) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 15:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                        RadioM.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 4:
                        RadioM.channelFreq(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 13:
                        RadioM.rdsText(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 14:
                        RadioM.channelText(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 16:
                        RadioM.extraFreqInfo(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 26:
                        RadioM.psText(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    default:
                        if (IpcUtil.intsOk(this.ints, 1)) {
                            RadioM.DATA[this.updateCode] = this.ints[0];
                        }
                        RadioM.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                }
            }
        }
    }

    private RadioM() {
    }

    public static void channelFreq(int i, int[] iArr, float[] fArr, String[] strArr) {
        int i2;
        if (IpcUtil.intsOk(iArr, 2)) {
            int i3 = iArr[0];
            int i4 = iArr[1];
            if (i3 >= 65536 && i3 <= 131072) {
                int i5 = i3 - 65536;
                if (i5 < 18 && FREQ_FM[i5] != i4) {
                    FREQ_FM[i5] = i4;
                    mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
                }
            } else if (i3 >= 0 && i3 <= 65536 && (i2 = i3 + 0) < 12 && FREQ_AM[i2] != i4) {
                FREQ_AM[i2] = i4;
                mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
            }
        }
    }

    public static void channelText(int i, int[] iArr, float[] fArr, String[] strArr) {
        int i2;
        if (IpcUtil.intsOk(iArr, 1) && IpcUtil.strsOk(strArr, 1)) {
            int i3 = iArr[0];
            String str = strArr[0];
            if (str == null) {
                return;
            }
            if (i3 >= 65536 && i3 <= 131072) {
                int i4 = i3 - 65536;
                if (i4 < 18 && !str.equals(FREQ_FM_TEXT[i4])) {
                    FREQ_FM_TEXT[i4] = str;
                    mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
                }
            } else if (i3 >= 0 && i3 <= 65536 && (i2 = i3 + 0) < 12 && !str.equals(FREQ_AM_TEXT[i2])) {
                FREQ_AM_TEXT[i2] = str;
                mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
            }
        }
    }

    public static void extraFreqInfo(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 4)) {
            sFreqMin = iArr[0];
            sFreqMax = iArr[1];
            sFreqStepLen = iArr[2];
            sFreqStepCnt = iArr[3];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static RadioM getInstance() {
        return INSTANCE;
    }

    public static void psText(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.strsOk(strArr, 1)) {
            sPsText = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void rdsText(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.strsOk(strArr, 1)) {
            sRdsText = strArr[0];
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
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RadioM instance = getInstance();
        for (int i = 0; i < 100; i++) {
            PROXY.register(instance, i, 1);
        }
    }

    public void onDisconnected() {
        PROXY.setRemoteModule((IRemoteModule) null);
    }

    public void update(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        Main.postRunnable_Ui(false, new Runnable_Update(i, iArr, fArr, strArr));
    }
}
