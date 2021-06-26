package com.syu.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class HandlerTickAlways extends Handler {
    private static final HandlerTickAlways INSTANCE;

    static {
        HandlerThread handlerThread = new HandlerThread("HandlerTickAlways");
        handlerThread.start();
        INSTANCE = new HandlerTickAlways(handlerThread.getLooper());
    }

    private HandlerTickAlways(Looper looper) {
        super(looper);
    }

    public static HandlerTickAlways getInstance() {
        return INSTANCE;
    }
}
