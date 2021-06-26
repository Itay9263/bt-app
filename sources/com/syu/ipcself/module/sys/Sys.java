package com.syu.ipcself.module.sys;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.ipcself.module.main.Main;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class Sys extends IModuleCallback.Stub implements IConnStateListener {
    public static int[] DATA = new int[10];
    private static Sys INSTANCE = new Sys();
    public static ModuleProxy PROXY = new ModuleProxy();
    public static UiNotifyEvent mUiNotifyEvent = new UiNotifyEvent();
    public static int simCardState = 0;

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
            if (this.updateCode >= 0 && this.updateCode < 10) {
                if (IpcUtil.intsOk(this.ints, 1) && Sys.DATA[this.updateCode] != this.ints[0]) {
                    Sys.DATA[this.updateCode] = this.ints[0];
                }
                Sys.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
            }
        }
    }

    private Sys() {
    }

    public static Sys getInstance() {
        return INSTANCE;
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(0));
            Sys instance = getInstance();
            for (int i = 0; i < 10; i++) {
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
