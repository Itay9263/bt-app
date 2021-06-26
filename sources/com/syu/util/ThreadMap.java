package com.syu.util;

import android.os.Handler;
import android.os.HandlerThread;
import java.util.HashMap;

public class ThreadMap {
    public static HashMap<String, Handler> mHashMapHandler = new HashMap<>();
    public static HashMap<String, HandlerThread> mHashMapThread = new HashMap<>();

    public static void startThread(String str, Runnable runnable, boolean z, int i) {
        if (runnable != null) {
            if (mHashMapThread.get(str) == null) {
                HandlerThread handlerThread = new HandlerThread(str);
                handlerThread.setPriority(i);
                handlerThread.start();
                mHashMapThread.put(str, handlerThread);
                mHashMapHandler.put(str, new Handler(handlerThread.getLooper()));
            }
            Handler handler = mHashMapHandler.get(str);
            if (handler != null) {
                if (z) {
                    handler.removeCallbacks(runnable);
                }
                handler.post(runnable);
            }
        }
    }

    public static void startThread(String str, Runnable runnable, boolean z, int i, long j) {
        if (runnable != null) {
            if (mHashMapThread.get(str) == null) {
                HandlerThread handlerThread = new HandlerThread(str);
                handlerThread.setPriority(i);
                handlerThread.start();
                mHashMapThread.put(str, handlerThread);
                mHashMapHandler.put(str, new Handler(handlerThread.getLooper()));
            }
            Handler handler = mHashMapHandler.get(str);
            if (handler != null) {
                if (z) {
                    handler.removeCallbacks(runnable);
                }
                handler.postDelayed(runnable, j);
            }
        }
    }
}
