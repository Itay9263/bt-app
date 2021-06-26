package com.syu.util;

import android.os.SystemClock;

public class TimeLock {
    private long cur;
    private long last;

    public void reset() {
        this.last = SystemClock.uptimeMillis();
    }

    public void resetToZero() {
        this.last = 0;
    }

    public boolean unlock(int i) {
        this.cur = SystemClock.uptimeMillis();
        return this.cur - this.last >= ((long) i);
    }
}
