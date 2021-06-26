package com.syu.ipc.module;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import com.syu.ipc.module.cb.Main786_CB;
import com.syu.ipcself.Conn_Base;
import com.syu.ipcself.IConn;
import com.syu.ipcself.module.main.Main;
import com.syu.util.InterfaceApp;
import com.syu.util.IpcUtil;

public class Main786 extends IConn {
    public static final String ACTION_MAIN_CONNECT_SERVER = "com.syu.ms.main";
    private static Runnable_REQEUST REQEUST_APP_ID = null;
    public static Main786 hInstance = null;

    public static class Runnable_REQEUST implements Runnable {
        public boolean bRunning = true;

        public void run() {
            if ((IConn.mInterfaceApp == null || IConn.mInterfaceApp.isAppTop()) && Main.DATA[3] != Main.sAppIdRequest) {
                Main786.requestAppId(Main.sAppIdRequest);
                if (this.bRunning) {
                    Conn_Base.handler.postDelayed(this, 1000);
                }
            }
        }

        public void stop() {
            this.bRunning = false;
        }
    }

    public Main786(InterfaceApp interfaceApp, Context context) {
        super(interfaceApp, context);
        initAction(ACTION_MAIN_CONNECT_SERVER);
        hInstance = this;
    }

    public static void cmdStatic(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (hInstance != null) {
            hInstance.cmd(i, iArr, fArr, strArr);
        }
    }

    public static void requestAppId(int i) {
        Main.sAppIdRequest = i;
        cmdStatic(1, new int[]{i}, (float[]) null, (String[]) null);
    }

    public static void requestAppIdByOnTop(int i) {
        if (IConn.mInterfaceApp == null || IConn.mInterfaceApp.isAppTop()) {
            requestAppId(i);
            if (REQEUST_APP_ID != null) {
                REQEUST_APP_ID.stop();
            }
            REQEUST_APP_ID = new Runnable_REQEUST();
            Conn_Base.handler.postDelayed(REQEUST_APP_ID, 300);
        }
    }

    public static void update(int i, int[] iArr, float[] fArr, String[] strArr) {
        boolean z = true;
        if (IpcUtil.intsOk(iArr, 1)) {
            switch (i) {
                case 0:
                case 41:
                    break;
                default:
                    z = false;
                    break;
            }
            if (z || Main.DATA[i] != iArr[0]) {
                Main.DATA[i] = iArr[0];
                Main.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
            }
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        super.onServiceConnected(componentName, iBinder);
        for (int i = 0; i < 100; i++) {
            switch (i) {
                case 27:
                case 41:
                case 70:
                    registerCallback(Main786_CB.getInstance(), i, false);
                    break;
                default:
                    registerCallback(Main786_CB.getInstance(), i, true);
                    break;
            }
        }
        if (IConn.mInterfaceApp != null) {
            IConn.mInterfaceApp.onConnected_Main();
            if (IConn.mInterfaceApp.isAppTop()) {
                IConn.mInterfaceApp.requestAppIdRight();
            }
        }
    }
}
