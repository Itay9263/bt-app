package com.syu.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.syu.app.App;
import com.syu.bt.act.InterfaceBt;
import java.util.Iterator;

public class a extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        int min;
        int intExtra = intent.getIntExtra("android.bluetooth.headsetclient.extra.BATTERY_LEVEL", -1);
        if (intExtra > -1 && App.mPhoneBatterty != (min = Math.min(5, Math.max(1, intExtra)))) {
            App.mPhoneBatterty = min;
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().updatePhoneBattery(App.mPhoneBatterty);
            }
        }
    }
}
