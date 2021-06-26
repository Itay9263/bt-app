package com.syu.ipcself;

import android.content.Context;
import com.syu.ipcself.module.unicar.UniCar;
import com.syu.util.InterfaceApp;

public class ConnUniCar extends Conn_Base {
    public ConnUniCar(InterfaceApp interfaceApp, Context context) {
        super("com.syu.unicar", "com.syu.unicar.ToolkitService", interfaceApp, context);
        addObserver(UniCar.getInstance());
    }
}
