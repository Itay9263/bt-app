package com.syu.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class MyHandlerThread extends Handler {
    private static final MyHandlerThread INSTANCE;

    static {
        HandlerThread handlerThread = new HandlerThread("HandlerMyThread");
        handlerThread.start();
        INSTANCE = new MyHandlerThread(handlerThread.getLooper());
    }

    private MyHandlerThread(Looper looper) {
        super(looper);
    }

    public static MyHandlerThread getInstance() {
        return INSTANCE;
    }
}
