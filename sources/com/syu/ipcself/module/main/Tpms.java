package com.syu.ipcself.module.main;

import android.os.RemoteException;
import com.syu.data.FinalTpms;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class Tpms extends IModuleCallback.Stub implements IConnStateListener {
    public static final int[] DATA = new int[FinalTpms.U_CNT_MAX];
    private static final Tpms INSTANCE = new Tpms();
    public static final ModuleProxy PROXY = new ModuleProxy();
    public static boolean isFirstConn;
    public static boolean isMTUSerialObdEnable;
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
                if (IpcUtil.intsOk(this.ints, 1)) {
                    Tpms.DATA[this.updateCode] = this.ints[0];
                }
                Tpms.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
            }
        }
    }

    public static Tpms getInstance() {
        return INSTANCE;
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(8));
            Tpms instance = getInstance();
            PROXY.register(instance, 1, 1);
            PROXY.register(instance, 2, 1);
            PROXY.register(instance, 3, 1);
            PROXY.register(instance, 4, 1);
            PROXY.register(instance, 5, 1);
            PROXY.register(instance, 6, 1);
            PROXY.register(instance, 7, 1);
            PROXY.register(instance, 8, 1);
            PROXY.register(instance, 9, 1);
            isFirstConn = true;
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
