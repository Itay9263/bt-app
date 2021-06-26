package com.syu.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import com.syu.data.FinalChip;
import java.util.Random;

public class ConnectionKeepAlive implements ServiceConnection {
    /* access modifiers changed from: private */
    public String mActionName;
    /* access modifiers changed from: private */
    public IBinder mBinder;
    /* access modifiers changed from: private */
    public boolean mConnecting;
    /* access modifiers changed from: private */
    public Context mContext;
    private Runnable mRunnableConnect = new Runnable() {
        public void run() {
            if (ConnectionKeepAlive.this.mBinder == null) {
                if (ConnectionKeepAlive.this.checkApkExist(ConnectionKeepAlive.this.mContext, ConnectionKeepAlive.this.getPackageName(ConnectionKeepAlive.this.mActionName))) {
                    ConnectionKeepAlive.this.mContext.bindService(new Intent(ConnectionKeepAlive.this.mActionName), ConnectionKeepAlive.this, 1);
                }
                MyHandlerThread.getInstance().postDelayed(this, (long) (new Random().nextInt(3000) + 1500));
                return;
            }
            ConnectionKeepAlive.this.mConnecting = false;
        }
    };

    public ConnectionKeepAlive(String str) {
        this.mActionName = str;
    }

    /* access modifiers changed from: private */
    public boolean checkApkExist(Context context, String str) {
        if (str == null || FinalChip.BSP_PLATFORM_Null.equals(str)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(str, 8192);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private synchronized void connect(Context context, long j) {
        if (!this.mConnecting && this.mBinder == null && context != null) {
            this.mContext = context.getApplicationContext();
            this.mConnecting = true;
            MyHandlerThread.getInstance().post(this.mRunnableConnect);
        }
    }

    /* access modifiers changed from: private */
    public String getPackageName(String str) {
        return "com.syu.us.keepAlive".equals(str) ? "com.syu.us" : "com.syu.ss.keepAlive".equals(str) ? "com.syu.ss" : "com.syu.canbus.keepAlive".equals(str) ? "com.syu.canbus" : "com.syu.av.keepAlive".equals(str) ? "com.syu.av" : FinalChip.BSP_PLATFORM_Null;
    }

    public void connect(Context context) {
        connect(context, 0);
    }

    public synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mBinder = iBinder;
    }

    public synchronized void onServiceDisconnected(ComponentName componentName) {
        this.mBinder = null;
        connect(this.mContext, (long) (new Random().nextInt(3000) + 1000));
    }
}
