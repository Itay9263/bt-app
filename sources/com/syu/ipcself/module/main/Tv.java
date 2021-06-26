package com.syu.ipcself.module.main;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;
import java.text.DecimalFormat;

public class Tv extends IModuleCallback.Stub implements IConnStateListener {
    public static final int CHANNEL_CNT_MAX = 256;
    public static int[] CHANNEL_FREQ = new int[256];
    public static String[] CHANNEL_FREQ_STR = new String[256];
    public static int[] DATA = new int[30];
    private static Tv INSTANCE = new Tv();
    public static ModuleProxy PROXY = new ModuleProxy();
    public static UiNotifyEvent mUiNotifyEvent = new UiNotifyEvent();
    public static int sChannelCnt;

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
            if (this.updateCode >= 0 && this.updateCode < 30) {
                switch (this.updateCode) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                        Tv.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 4:
                        Tv.channelCnt(this.updateCode, this.ints, this.flts, this.strs);
                        Tv.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    default:
                        Tv.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                }
            }
        }
    }

    private Tv() {
    }

    public static void channelCnt(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1) && sChannelCnt != iArr[0]) {
            if (iArr[0] == 0) {
                sChannelCnt = iArr[0];
            }
            if (iArr[0] > 256) {
                iArr[0] = 256;
            }
            while (sChannelCnt < iArr[0]) {
                CHANNEL_FREQ[sChannelCnt] = PROXY.getI(0, sChannelCnt, 0);
                float f = ((float) CHANNEL_FREQ[sChannelCnt]) / 1000.0f;
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                if (f == 0.0f) {
                    CHANNEL_FREQ_STR[sChannelCnt] = "0.00";
                } else {
                    CHANNEL_FREQ_STR[sChannelCnt] = decimalFormat.format((double) f);
                }
                sChannelCnt++;
            }
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static Tv getInstance() {
        return INSTANCE;
    }

    public static void updateNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1) && DATA[i] != iArr[0]) {
            DATA[i] = iArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(6));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Tv instance = getInstance();
        for (int i = 0; i < 30; i++) {
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
