package com.syu.util;

import android.os.SystemClock;

public class TickLock {
    private long cur;
    private long last;

    public int pass() {
        this.cur = SystemClock.uptimeMillis();
        return (int) (this.cur - this.last);
    }

    public void reset() {
        this.last = SystemClock.uptimeMillis();
    }

    public void resetOffset(long j) {
        this.last = SystemClock.uptimeMillis() + j;
    }

    public void resetToZero() {
        this.last = 0;
    }

    public boolean unlock(int i) {
        this.cur = SystemClock.uptimeMillis();
        if (this.cur - this.last < ((long) i)) {
            return false;
        }
        this.last = this.cur;
        return true;
    }
}
