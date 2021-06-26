package com.syu.ipc.module.cb;

import android.os.RemoteException;
import com.syu.ipc.ICallback;

public class Sound786_CB extends ICallback.Stub {
    private static Sound786_CB INSTANCE = new Sound786_CB();

    public static Sound786_CB getInstance() {
        return INSTANCE;
    }

    public void update(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
    }
}
