package com.syu.ipcself;

import android.content.Context;
import com.syu.ipcself.module.main.TBoxServer;
import com.syu.util.InterfaceApp;

public class ConnTBoxServer extends Conn_Base {
    public ConnTBoxServer(InterfaceApp interfaceApp, Context context) {
        super("com.syu.TBoxClient", "com.syu.app.ToolkitService", interfaceApp, context);
        addObserver(TBoxServer.getInstance());
    }
}
