package com.syu.ipcself;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import com.syu.ipc.IRemoteToolkit;
import com.syu.util.InterfaceApp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Conn_Base implements ServiceConnection {
    public static Handler handler = new Handler(looper);
    static Looper looper;
    public static InterfaceApp mInterfaceApp = null;
    String clsName;
    /* access modifiers changed from: private */
    public boolean mConnecting;
    private ArrayList<IConnStateListener> mConnectionObservers = new ArrayList<>();
    /* access modifiers changed from: private */
    public Context mContext;
    public Conn_Base mInstance = null;
    /* access modifiers changed from: private */
    public IRemoteToolkit mRemoteToolkit;
    private Runnable mRunnableConnect = new Runnable() {
        public void run() {
            if (Conn_Base.this.mRemoteToolkit == null) {
                Intent intent = new Intent();
                intent.setClassName(Conn_Base.this.pkgName, Conn_Base.this.clsName);
                Conn_Base.this.mContext.bindService(intent, Conn_Base.this.mInstance, 1);
                Conn_Base.handler.postDelayed(this, (long) (new Random().nextInt(3000) + 1000));
                return;
            }
            Conn_Base.this.mConnecting = false;
        }
    };
    String pkgName;

    private class OnServiceConnected implements Runnable {
        private IConnStateListener observer;

        private OnServiceConnected(IConnStateListener iConnStateListener) {
            this.observer = iConnStateListener;
        }

        /* synthetic */ OnServiceConnected(Conn_Base conn_Base, IConnStateListener iConnStateListener, OnServiceConnected onServiceConnected) {
            this(iConnStateListener);
        }

        public void run() {
            IRemoteToolkit access$0 = Conn_Base.this.mRemoteToolkit;
            if (access$0 != null && this.observer != null) {
                this.observer.onConnected(access$0);
            }
        }
    }

    private class OnServiceDisconnected implements Runnable {
        private IConnStateListener observer;

        private OnServiceDisconnected(IConnStateListener iConnStateListener) {
            this.observer = iConnStateListener;
        }

        /* synthetic */ OnServiceDisconnected(Conn_Base conn_Base, IConnStateListener iConnStateListener, OnServiceDisconnected onServiceDisconnected) {
            this(iConnStateListener);
        }

        public void run() {
            if (this.observer != null) {
                this.observer.onDisconnected();
            }
        }
    }

    static {
        HandlerThread handlerThread = new HandlerThread("ConnectionThread");
        handlerThread.start();
        looper = handlerThread.getLooper();
    }

    public Conn_Base(String str, String str2, InterfaceApp interfaceApp, Context context) {
        this.pkgName = str;
        this.clsName = str2;
        this.mInstance = this;
        if (mInterfaceApp == null) {
            mInterfaceApp = interfaceApp;
        }
        connect(context);
    }

    private void connect(Context context, long j) {
        if (!this.mConnecting && this.mRemoteToolkit == null && context != null) {
            this.mContext = context.getApplicationContext();
            this.mConnecting = true;
            handler.post(this.mRunnableConnect);
        }
    }

    public void addObserver(IConnStateListener iConnStateListener) {
        if (iConnStateListener != null && !this.mConnectionObservers.contains(iConnStateListener)) {
            this.mConnectionObservers.add(iConnStateListener);
            if (this.mRemoteToolkit != null) {
                handler.post(new OnServiceConnected(this, iConnStateListener, (OnServiceConnected) null));
            }
        }
    }

    public void clearObservers() {
        if (this.mRemoteToolkit != null) {
            Iterator<IConnStateListener> it = this.mConnectionObservers.iterator();
            while (it.hasNext()) {
                handler.post(new OnServiceDisconnected(this, it.next(), (OnServiceDisconnected) null));
            }
        }
        this.mConnectionObservers.clear();
    }

    public void connect(Context context) {
        connect(context, 0);
    }

    public IRemoteToolkit getRemoteToolkit() {
        return this.mRemoteToolkit;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mRemoteToolkit = IRemoteToolkit.Stub.asInterface(iBinder);
        Iterator<IConnStateListener> it = this.mConnectionObservers.iterator();
        while (it.hasNext()) {
            handler.post(new OnServiceConnected(this, it.next(), (OnServiceConnected) null));
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        this.mRemoteToolkit = null;
        Iterator<IConnStateListener> it = this.mConnectionObservers.iterator();
        while (it.hasNext()) {
            handler.post(new OnServiceDisconnected(this, it.next(), (OnServiceDisconnected) null));
        }
        connect(this.mContext, (long) (new Random().nextInt(3000) + 1000));
    }

    public void removeObserver(IConnStateListener iConnStateListener) {
        if (iConnStateListener != null) {
            this.mConnectionObservers.remove(iConnStateListener);
        }
        if (this.mRemoteToolkit != null) {
            handler.post(new OnServiceDisconnected(this, iConnStateListener, (OnServiceDisconnected) null));
        }
    }
}
