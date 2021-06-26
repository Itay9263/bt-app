package com.syu.ipcself.module.main;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.Conn;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class Sound extends IModuleCallback.Stub implements IConnStateListener {
    public static int[] DATA = new int[50];
    private static Sound INSTANCE = new Sound();
    public static ModuleProxy PROXY = new ModuleProxy();
    public static int[] SB = new int[48];
    public static int[] mBarVals = new int[36];
    public static int mFieldX;
    public static int mFieldY;
    public static int[] mSpectrum = new int[7];
    public static UiNotifyEvent mUiNotifyEvent = new UiNotifyEvent();

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
                        if (IpcUtil.intsOk(this.ints, 7)) {
                            Sound.mSpectrum = this.ints;
                            return;
                        }
                        return;
                    case 2:
                    case 3:
                    case 7:
                    case 10:
                        Sound.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 8:
                        if (!IpcUtil.intsOk(this.ints, 2)) {
                            return;
                        }
                        if (Sound.mFieldX != this.ints[0] || Sound.mFieldY != this.ints[1]) {
                            Sound.mFieldX = this.ints[0];
                            Sound.mFieldY = this.ints[1];
                            Sound.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                            return;
                        }
                        return;
                    case 9:
                        if (IpcUtil.intsOk(this.ints, 2)) {
                            if (this.ints[0] < Sound.mBarVals.length) {
                                Sound.mBarVals[this.ints[0]] = this.ints[1];
                            }
                            Sound.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                            return;
                        }
                        return;
                    case 12:
                        if (IpcUtil.intsOk(this.ints, 2)) {
                            Sound.sb(this.updateCode, this.ints, this.flts, this.strs);
                            return;
                        }
                        return;
                    default:
                        if (IpcUtil.intsOk(this.ints, 1)) {
                            Sound.DATA[this.updateCode] = this.ints[0];
                        }
                        Sound.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                }
            }
        }
    }

    private Sound() {
    }

    public static Sound getInstance() {
        return INSTANCE;
    }

    public static int getSpectrumValue() {
        if (DATA[2] <= 0 || DATA[3] != 0) {
            return 0;
        }
        return mSpectrum[2] * mSpectrum[2];
    }

    public static void sb(int i, int[] iArr, float[] fArr, String[] strArr) {
        int i2;
        if (IpcUtil.intsOk(iArr, 2) && (i2 = iArr[0]) >= 0 && i2 < 48 && SB[i2] != iArr[1]) {
            SB[i2] = iArr[1];
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
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(4));
            Sound instance = getInstance();
            for (int i = 0; i < 50; i++) {
                PROXY.register(instance, i, 1);
            }
            if (Conn.mInterfaceApp != null) {
                Conn.mInterfaceApp.onConnected_Sound();
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
