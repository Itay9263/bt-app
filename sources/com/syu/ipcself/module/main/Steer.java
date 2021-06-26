package com.syu.ipcself.module.main;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class Steer extends IModuleCallback.Stub implements IConnStateListener {
    public static int[] DATA = new int[20];
    private static Steer INSTANCE = new Steer();
    public static ModuleProxy PROXY = new ModuleProxy();
    public static int[] mCurrentAdcs = new int[50];
    public static int[] mCurrentVal = new int[10];
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
            if (this.updateCode >= 0 && this.updateCode < 20) {
                if (this.updateCode == 1) {
                    if (IpcUtil.intsOk(this.ints, 2) && this.ints[0] < Steer.mCurrentAdcs.length) {
                        Steer.mCurrentAdcs[this.ints[0]] = this.ints[1];
                    }
                } else if (this.updateCode == 3) {
                    if (IpcUtil.intsOk(this.ints, 2) && this.ints[0] < Steer.mCurrentVal.length) {
                        Steer.mCurrentVal[this.ints[0]] = this.ints[1];
                    }
                } else if (IpcUtil.intsOk(this.ints, 1)) {
                    Steer.DATA[this.updateCode] = this.ints[0];
                }
                Steer.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
            }
        }
    }

    private Steer() {
    }

    public static Steer getInstance() {
        return INSTANCE;
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(10));
            Steer instance = getInstance();
            for (int i = 0; i < 20; i++) {
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
