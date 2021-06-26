package com.syu.util;

import android.os.Handler;
import android.os.Looper;

public class UiNotifyEvent {
    public static Handler HANDLER_UI = new Handler(Looper.getMainLooper());
    public IUiNotify uiNotify = null;

    private class NotifyData implements Runnable {
        public float[] flts;
        public int[] ints;
        public String[] strs;
        public int updateCode;

        public NotifyData(int i, int[] iArr, float[] fArr, String[] strArr) {
            this.updateCode = i;
            this.ints = iArr;
            this.flts = fArr;
            this.strs = strArr;
        }

        public void run() {
            if (UiNotifyEvent.this.uiNotify != null) {
                UiNotifyEvent.this.uiNotify.onNotify(this.updateCode, this.ints, this.flts, this.strs);
            }
        }
    }

    public void updateNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        NotifyData notifyData = new NotifyData(i, iArr, fArr, strArr);
        if (Looper.getMainLooper() == Looper.myLooper()) {
            notifyData.run();
        } else {
            HANDLER_UI.post(notifyData);
        }
    }
}
