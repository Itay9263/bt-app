package com.syu.ipcself;

import android.content.Context;
import com.syu.ipcself.module.main.Main;
import com.syu.util.InterfaceApp;

public class Conn extends Conn_Base {
    public Conn(InterfaceApp interfaceApp, Context context) {
        super("com.syu.ms", "app.ToolkitService", interfaceApp, context);
        addObserver(Main.getInstance());
    }
}
