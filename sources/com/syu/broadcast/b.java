package com.syu.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.syu.app.App;
import com.syu.bt.act.InterfaceBt;
import com.syu.ipcself.module.main.Main;
import java.util.Iterator;

public class b extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        new Thread(new Runnable() {
            public void run() {
                Main.postRunnable_Ui(false, new Runnable() {
                    public void run() {
                        if (App.getApp().isAppTop()) {
                            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                            while (it.hasNext()) {
                                it.next().updateSystemTime();
                            }
                        }
                    }
                });
            }
        }).start();
    }
}
