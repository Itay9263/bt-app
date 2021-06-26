package com.syu.ipcself.module.main;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class Canbox extends IModuleCallback.Stub implements IConnStateListener {
    public static int[] DATA = new int[120];
    private static Canbox INSTANCE = new Canbox();
    public static ModuleProxy PROXY = new ModuleProxy();
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
            if (this.updateCode >= 0 && this.updateCode < 120) {
                if (IpcUtil.intsOk(this.ints, 1)) {
                    Canbox.DATA[this.updateCode] = this.ints[0];
                }
                Canbox.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
            }
        }
    }

    private Canbox() {
    }

    public static Canbox getInstance() {
        return INSTANCE;
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(14));
            Canbox instance = getInstance();
            for (int i = 100; i < 120; i++) {
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
