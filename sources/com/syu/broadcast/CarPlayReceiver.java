package com.syu.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.syu.app.ipc.IpcObj;
import com.syu.ipcself.module.main.Main;

public class CarPlayReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("status");
        String stringExtra2 = intent.getStringExtra("phoneType");
        if (stringExtra != null && stringExtra2 != null) {
            if (Main.mConf_PlatForm != 7 && Main.mConf_PlatForm != 8) {
                return;
            }
            if (!stringExtra.equalsIgnoreCase("CONNECTED") || !stringExtra2.equalsIgnoreCase("ios_carplay")) {
                if (stringExtra.equalsIgnoreCase("DISCONNECTED")) {
                    stringExtra2.equalsIgnoreCase("ios_carplay");
                }
            } else if (IpcObj.isConnect()) {
                IpcObj.cut();
            }
        }
    }
}
