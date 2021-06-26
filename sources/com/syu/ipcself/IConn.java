package com.syu.ipcself;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import com.syu.ipc.ICallback;
import com.syu.ipc.IRemote;
import com.syu.util.InterfaceApp;

public class IConn implements ServiceConnection {
    private static final int MSG_CONNECT = 0;
    public static Handler mHandlerCmd;
    public static HandlerThread mHandlerThreadCmd;
    public static InterfaceApp mInterfaceApp;
    /* access modifiers changed from: private */
    public IConn mConn = this;
    /* access modifiers changed from: private */
    public Context mContext;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    removeMessages(0);
                    if (IConn.this.mRemoteModule == null) {
                        IConn.this.mContext.bindService(IConn.this.mIntent, IConn.this.mConn, 1);
                        sendEmptyMessageDelayed(0, 1000);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public Intent mIntent;
    protected IRemote mRemoteModule;

    public class Runnable_Cmd implements Runnable {
        int cmdCode;
        float[] flts;
        int[] ints;
        String[] strs;

        public Runnable_Cmd(int i, int[] iArr, float[] fArr, String[] strArr) {
            this.cmdCode = i;
            this.ints = iArr;
            this.flts = fArr;
            this.strs = strArr;
        }

        public void run() {
            try {
                if (IConn.this.mRemoteModule != null) {
                    IConn.this.mRemoteModule.cmd(this.cmdCode, this.ints, this.flts, this.strs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static {
        mHandlerThreadCmd = null;
        mHandlerCmd = null;
        mHandlerThreadCmd = new HandlerThread("IConn-CMD-Thread");
        mHandlerThreadCmd.start();
        mHandlerCmd = new Handler(mHandlerThreadCmd.getLooper());
    }

    public IConn(InterfaceApp interfaceApp, Context context) {
        if (mInterfaceApp == null) {
            mInterfaceApp = interfaceApp;
        }
        connect(context);
    }

    public void cmd(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (mHandlerCmd != null) {
            mHandlerCmd.post(new Runnable_Cmd(i, iArr, fArr, strArr));
        }
    }

    public void connect(Context context) {
        this.mHandler.sendEmptyMessage(0);
        this.mContext = context.getApplicationContext();
    }

    public float[] getAF(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (this.mRemoteModule != null) {
            try {
                return this.mRemoteModule.getAF(i, iArr, fArr, strArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int[] getAI(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (this.mRemoteModule != null) {
            try {
                return this.mRemoteModule.getAI(i, iArr, fArr, strArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String[] getAS(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (this.mRemoteModule != null) {
            try {
                return this.mRemoteModule.getAS(i, iArr, fArr, strArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public float getF(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (this.mRemoteModule != null) {
            try {
                return this.mRemoteModule.getF(i, iArr, fArr, strArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0.0f;
    }

    public int getI(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (this.mRemoteModule != null) {
            try {
                return this.mRemoteModule.getI(i, iArr, fArr, strArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String getS(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (this.mRemoteModule != null) {
            try {
                return this.mRemoteModule.getS(i, iArr, fArr, strArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void initAction(String str) {
        this.mIntent = new Intent(str);
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mRemoteModule = IRemote.Stub.asInterface(iBinder);
    }

    public void onServiceDisconnected(ComponentName componentName) {
        this.mRemoteModule = null;
        connect(this.mContext);
    }

    public void registerCallback(ICallback iCallback, int i, boolean z) {
        if (this.mRemoteModule != null) {
            try {
                this.mRemoteModule.registerCallback(iCallback, i, z);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterCallback(ICallback iCallback, int i) {
        if (this.mRemoteModule != null) {
            try {
                this.mRemoteModule.unregisterCallback(iCallback, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
