package com.syu.ipcself.module.unicar;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class UniCar extends IModuleCallback.Stub implements IConnStateListener {
    public static int[] DATA = new int[10];
    private static UniCar INSTANCE = new UniCar();
    public static ModuleProxy PROXY = new ModuleProxy();
    public static UiNotifyEvent mUiNotifyEvent = new UiNotifyEvent();

    private UniCar() {
    }

    public static UniCar getInstance() {
        return INSTANCE;
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(0));
            UniCar instance = getInstance();
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
        if (i >= 0 && i < 10) {
            if (IpcUtil.intsOk(iArr, 1)) {
                DATA[i] = iArr[0];
            }
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }
}
