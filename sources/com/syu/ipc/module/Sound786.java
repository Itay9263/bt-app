package com.syu.ipc.module;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import com.syu.ipc.module.cb.Sound786_CB;
import com.syu.ipcself.IConn;
import com.syu.util.InterfaceApp;

public class Sound786 extends IConn {
    public static final String ACTION_SOUND_CONNECT_SERVER = "com.syu.ms.sound";

    public Sound786(InterfaceApp interfaceApp, Context context) {
        super(interfaceApp, context);
        initAction(ACTION_SOUND_CONNECT_SERVER);
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        super.onServiceConnected(componentName, iBinder);
        registerCallback(Sound786_CB.getInstance(), 3, true);
        registerCallback(Sound786_CB.getInstance(), 4, true);
    }
}
