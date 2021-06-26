package com.syu.ipcself.sever;

import android.os.RemoteException;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.ModuleObject;
import com.syu.ipcself.Conn;
import com.syu.ipcself.IpcToolkit;
import com.syu.ipcself.ModuleCallbackList;
import com.syu.ipcself.module.sys.Sys;
import com.syu.util.IpcUtil;

public class SysServer extends IRemoteModule.Stub {
    private static final SysServer INSTANCE = new SysServer();
    public static final ModuleCallbackList mCallBackList = new ModuleCallbackList();
    long time = 0;

    private SysServer() {
    }

    public static SysServer getInstance() {
        return INSTANCE;
    }

    public void cmd(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        switch (i) {
            case 0:
                mCallBackList.update(0, iArr, fArr, strArr);
                return;
            case 1:
                if (IpcUtil.intsOk(iArr, 1) && Conn.mInterfaceApp != null) {
                    Conn.mInterfaceApp.cmdObdFlagStop(iArr[0]);
                    return;
                }
                return;
            case 2:
                mCallBackList.update(2, iArr, fArr, strArr);
                return;
            case 3:
                if (IpcUtil.intsOk(iArr, 1)) {
                    mCallBackList.update(4, iArr, fArr, strArr);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public ModuleObject get(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        return null;
    }

    public void register(IModuleCallback iModuleCallback, int i, int i2) throws RemoteException {
        if (iModuleCallback != null) {
            if (i >= 0 && i < 10) {
                IpcToolkit.register(mCallBackList, iModuleCallback, i);
            }
            if (i2 != 0) {
                switch (i) {
                    case 3:
                        ModuleCallbackList.update(iModuleCallback, i, Sys.simCardState);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void unregister(IModuleCallback iModuleCallback, int i) throws RemoteException {
        if (iModuleCallback != null && i >= 0 && i < 10) {
            IpcToolkit.unregister(mCallBackList, iModuleCallback, i);
        }
    }
}
