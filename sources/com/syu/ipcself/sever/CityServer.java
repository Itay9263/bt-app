package com.syu.ipcself.sever;

import android.os.RemoteException;
import com.syu.data.FinalChip;
import com.syu.geometry.GetCity;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.ModuleObject;
import com.syu.ipcself.IpcToolkit;
import com.syu.ipcself.ModuleCallbackList;

public class CityServer extends IRemoteModule.Stub {
    private static final CityServer INSTANCE = new CityServer();
    public static final ModuleCallbackList mCallBackList = new ModuleCallbackList();
    public static final String[] mStr = new String[20];

    static {
        for (int i = 0; i < 20; i++) {
            mStr[i] = FinalChip.BSP_PLATFORM_Null;
        }
    }

    private CityServer() {
    }

    public static CityServer getInstance() {
        return INSTANCE;
    }

    public void cmd(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        switch (i) {
            case 1:
                mCallBackList.update(0, iArr, fArr, strArr);
                return;
            case 2:
                try {
                    String city = GetCity.getCity((double) fArr[0], (double) fArr[1]);
                    if (city != null && !city.isEmpty()) {
                        cmdCity(1, city);
                        return;
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    public void cmdCity(int i, String str) {
        try {
            mStr[0] = str;
            cmd(i, (int[]) null, (float[]) null, new String[]{str});
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ModuleObject get(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        return null;
    }

    public void register(IModuleCallback iModuleCallback, int i, int i2) throws RemoteException {
        if (iModuleCallback != null) {
            if (i >= 0 && i < 20) {
                IpcToolkit.register(mCallBackList, iModuleCallback, i);
            }
            if (i2 != 0) {
                switch (i) {
                    case 0:
                        if (mStr[i] != null && !mStr[i].isEmpty()) {
                            mCallBackList.update(0, (int[]) null, (float[]) null, new String[]{mStr[0]});
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void unregister(IModuleCallback iModuleCallback, int i) throws RemoteException {
        if (iModuleCallback != null && i >= 0 && i < 20) {
            IpcToolkit.unregister(mCallBackList, iModuleCallback, i);
        }
    }
}
