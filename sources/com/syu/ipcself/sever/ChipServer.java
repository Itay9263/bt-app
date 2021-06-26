package com.syu.ipcself.sever;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.ModuleObject;
import com.syu.ipcself.IpcToolkit;
import com.syu.ipcself.ModuleCallbackList;

public class ChipServer extends IRemoteModule.Stub {
    private static ChipServer INSTANCE;
    public static final ModuleCallbackList mCallBackList = new ModuleCallbackList();

    private ChipServer() {
    }

    public static ChipServer getInstance() {
        if (INSTANCE == null) {
            synchronized (ChipServer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChipServer();
                }
            }
        }
        return INSTANCE;
    }

    public void cmd(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
    }

    public ModuleObject get(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        return null;
    }

    public void register(IModuleCallback iModuleCallback, int i, int i2) throws RemoteException {
        if (iModuleCallback != null && i >= 0 && i < 10) {
            IpcToolkit.register(mCallBackList, iModuleCallback, i);
        }
    }

    public void unregister(IModuleCallback iModuleCallback, int i) throws RemoteException {
        if (iModuleCallback != null && i >= 0 && i < 10) {
            IpcToolkit.unregister(mCallBackList, iModuleCallback, i);
        }
    }
}
