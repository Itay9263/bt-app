package com.syu.ipcself;

import android.content.Context;
import com.syu.ipcself.module.sys.Locate;
import com.syu.ipcself.module.sys.Sys;
import com.syu.util.InterfaceApp;

public class ConnSys extends Conn_Base {
    public ConnSys(InterfaceApp interfaceApp, Context context) {
        super("com.syu.ss", "app.ToolkitService", interfaceApp, context);
        addObserver(Sys.getInstance());
        addObserver(Locate.getInstance());
    }

    public ConnSys(String str, String str2, InterfaceApp interfaceApp, Context context) {
        super(str, str2, interfaceApp, context);
        addObserver(Sys.getInstance());
        addObserver(Locate.getInstance());
    }
}
