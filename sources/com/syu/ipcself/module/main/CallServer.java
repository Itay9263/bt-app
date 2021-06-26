package com.syu.ipcself.module.main;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class CallServer extends IModuleCallback.Stub implements IConnStateListener {
    public static int[] DATA = new int[2];
    private static CallServer INSTANCE = new CallServer();
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
            if (this.updateCode >= 0 && this.updateCode < 2) {
                CallServer.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
            }
        }
    }

    private CallServer() {
    }

    public static CallServer getInstance() {
        return INSTANCE;
    }

    public static void updateNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.intsOk(iArr, 1)) {
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (DATA[i] != iArr[0]) {
            DATA[i] = iArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(0));
            CallServer instance = getInstance();
            for (int i = 1; i < 2; i++) {
                PROXY.register(instance, i, 1);
            }
        } catch (RemoteException e) {
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
