package com.syu.ipcself;

import android.content.Context;
import com.syu.ipcself.module.main.MediaServer;
import com.syu.util.InterfaceApp;

public class ConnMediaServer extends Conn_Base {
    public ConnMediaServer(InterfaceApp interfaceApp, Context context) {
        super("com.syu.video", "com.syu.app.ToolkitService", interfaceApp, context);
        addObserver(MediaServer.getInstance());
    }
}
