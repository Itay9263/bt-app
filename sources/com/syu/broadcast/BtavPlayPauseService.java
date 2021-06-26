package com.syu.broadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;

public class BtavPlayPauseService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (!IpcObj.isAppIdBtAv()) {
            App.getApp().mIpcObj.LauncherRequestAppIdBtAv();
        }
        if (intent != null) {
            String stringExtra = intent.getStringExtra("status");
            if (TextUtils.isEmpty(stringExtra)) {
                IpcObj.avPlayPause();
            } else if (stringExtra.equalsIgnoreCase("play")) {
                IpcObj.avPlay();
            } else if (stringExtra.equalsIgnoreCase("pause")) {
                IpcObj.avPause();
            } else {
                IpcObj.avPlayPause();
            }
        }
        return super.onStartCommand(intent, i, i2);
    }
}
