package com.syu.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.syu.app.App;
import com.syu.ipcself.module.main.Bt;

public class DiDiHuReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equalsIgnoreCase("com.bt.ACTION_BT_CONNECTION_REQUEST")) {
            Intent intent2 = new Intent();
            if (Bt.DATA[9] == 2 || Bt.DATA[9] == 3 || Bt.DATA[9] == 4 || Bt.DATA[9] == 5) {
                intent2.putExtra("extra_bt_connection_event", 1);
            } else {
                intent2.putExtra("extra_bt_connection_event", 0);
            }
            intent2.setAction("com.bt.ACTION_BT_CONNECTION_CHANGE");
            context.sendBroadcast(intent2);
        } else if (action.equalsIgnoreCase("com.bt.ACTION_BT_NAME_AND_PINCODE_REQUEST")) {
            Intent intent3 = new Intent();
            intent3.putExtra("extra_bt_name", Bt.sDevName);
            intent3.putExtra("extra_bt_pincode", Bt.sDevPin);
            intent3.setAction("com.bt.ACTION_BT_NAME_AND_PINCODE");
            context.sendBroadcast(intent3);
        } else if (action.equalsIgnoreCase("com.bt.ACTION_BT_SYNC_CONTACT_REQUEST")) {
            Intent intent4 = new Intent();
            if (App.mBtInfo.mListContact == null) {
                intent4.putExtra("extra_bt_ddhu_sync_contact_state", 0);
            } else if (App.mBtInfo.mListContact.size() > 0) {
                intent4.putExtra("extra_bt_ddhu_sync_contact_state", 1);
            } else {
                intent4.putExtra("extra_bt_ddhu_sync_contact_state", 0);
            }
            intent4.putExtra("extra_bt_connected_mac", Bt.sDevAddr);
            intent4.setAction("com.nwd.bt.ddhu.ACTION_DDHU_SYNC_CONTACT_FINISH");
            context.sendBroadcast(intent4);
        }
    }
}
