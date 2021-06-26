package com.syu.ipcself.module.main;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class Gsensor extends IModuleCallback.Stub implements IConnStateListener {
    public static final int[] DATA = new int[20];
    private static Gsensor INSTANCE = new Gsensor();
    public static final ModuleProxy PROXY = new ModuleProxy();
    public static final int U_GSENSOR_ON = 3;
    public static final int U_LOCK_RECORD = 1;
    public static final int U_SLEEP_30S_COLLIDE_RECORD = 8;
    public static final int U_SLEEP_STOP_RECORD = 2;
    public static final int U_STANDBY_COLLIDE_SCREENON = 7;
    public static final int U_STANDBY_RECORD_TIME = 6;
    public static final int U_STANDBY_SENSITIVITY = 5;
    public static final int U_WORK_SENSITIVITY = 4;
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
                switch (this.updateCode) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 12:
                    case 13:
                    case 14:
                        Gsensor.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private Gsensor() {
    }

    public static Gsensor getInstance() {
        return INSTANCE;
    }

    public static void updateNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1)) {
            DATA[i] = iArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(17));
            Gsensor instance = getInstance();
            for (int i = 0; i < 20; i++) {
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
