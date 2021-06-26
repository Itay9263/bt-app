package com.syu.ipc.module;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import com.syu.data.FinalChip;
import com.syu.ipc.module.cb.Bt786_CB;
import com.syu.ipcself.IConn;
import com.syu.ipcself.module.main.Bt;
import com.syu.util.InterfaceApp;
import com.syu.util.IpcUtil;

public class Bt786 extends IConn {
    public static String ACTION_BT_CONNECT_SERVER = "com.syu.ms.bt";
    public static Bt786 hInstance = null;

    public Bt786(InterfaceApp interfaceApp, Context context) {
        super(interfaceApp, context);
        initAction(ACTION_BT_CONNECT_SERVER);
        hInstance = this;
    }

    public static void cmdBt(int i, int i2, String str) {
        if (str != null) {
            cmdStatic(i, (int[]) null, (float[]) null, new String[]{str});
        } else if (str == null && i2 == -2) {
            cmdStatic(i, (int[]) null, (float[]) null, (String[]) null);
        } else if (str == null && i2 != -2) {
            cmdStatic(i, new int[]{i2}, (float[]) null, (String[]) null);
        }
    }

    public static void cmdStatic(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (hInstance != null) {
            hInstance.cmd(i, iArr, fArr, strArr);
        }
    }

    public static void devName(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            Bt.sDevName = FinalChip.BSP_PLATFORM_Null;
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(Bt.sDevName)) {
            Bt.sDevName = strArr[0];
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void devPin(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            Bt.sDevPin = FinalChip.BSP_PLATFORM_Null;
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(Bt.sDevPin)) {
            Bt.sDevPin = strArr[0];
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void localAddr(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            Bt.sDevAddr = FinalChip.BSP_PLATFORM_Null;
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(Bt.sDevAddr)) {
            Bt.sDevAddr = strArr[0];
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void phoneAddr(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            Bt.sPhoneAddr = FinalChip.BSP_PLATFORM_Null;
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(Bt.sPhoneAddr)) {
            Bt.sPhoneAddr = strArr[0];
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void phoneName(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            Bt.sPhoneName = FinalChip.BSP_PLATFORM_Null;
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(Bt.sPhoneName)) {
            Bt.sPhoneName = strArr[0];
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void phoneNumber(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!IpcUtil.strsOk(strArr, 1)) {
            return;
        }
        if (strArr[0] == null) {
            Bt.sPhoneNumber = FinalChip.BSP_PLATFORM_Null;
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        } else if (!strArr[0].equals(Bt.sPhoneNumber)) {
            Bt.sPhoneNumber = strArr[0];
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void update(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1) && Bt.DATA[i] != iArr[0]) {
            Bt.DATA[i] = iArr[0];
            Bt.mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        super.onServiceConnected(componentName, iBinder);
        for (int i = 0; i < 50; i++) {
            if (i == 29) {
                registerCallback(Bt786_CB.getInstance(), i, false);
            } else {
                registerCallback(Bt786_CB.getInstance(), i, true);
            }
        }
    }
}
