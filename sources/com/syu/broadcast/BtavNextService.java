package com.syu.broadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;

public class BtavNextService extends Service {
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (!IpcObj.isAppIdBtAv()) {
            App.getApp().mIpcObj.LauncherRequestAppIdBtAv();
        }
        if (intent != null) {
            IpcObj.avNext();
        }
        return super.onStartCommand(intent, i, i2);
    }
}
