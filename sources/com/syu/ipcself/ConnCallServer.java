package com.syu.ipcself;

import android.content.Context;
import com.syu.ipcself.module.main.CallServer;
import com.syu.util.InterfaceApp;

public class ConnCallServer extends Conn_Base {
    public ConnCallServer(InterfaceApp interfaceApp, Context context) {
        super("com.syu.call", "com.syu.app.ToolkitService", interfaceApp, context);
        addObserver(CallServer.getInstance());
    }
}
