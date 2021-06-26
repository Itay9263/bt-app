package com.syu.preloadimg;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PreloadImage extends Service {
    boolean a = false;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }
}
