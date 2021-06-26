package com.syu.ipcself.module.main;

import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.syu.data.FinalChip;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.Conn;
import com.syu.ipcself.Conn_Base;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.share.ShareHandler;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;
import com.syu.util.UiNotifyEventString;
import java.util.ArrayList;

public class Main extends IModuleCallback.Stub implements IConnStateListener {
    public static final int BACK_CAMERA_NOT_FOUND = 4;
    public static final int BACK_CVBS = 1;
    public static final int BACK_END = 3;
    public static final int BACK_NULL = 0;
    public static final int BACK_USB = 2;
    public static int[] DATA = new int[256];
    public static int[] DATA_RADAR = new int[16];
    private static Main INSTANCE = new Main();
    public static ModuleProxy PROXY = new ModuleProxy();
    public static final int PlatForm_3188 = 2;
    public static final int PlatForm_6025 = 6;
    public static final int PlatForm_786 = 4;
    public static final int PlatForm_8700 = 3;
    public static final int PlatForm_9853 = 8;
    public static final int PlatForm_E7 = 1;
    public static final int PlatForm_PX5 = 7;
    public static final int PlatForm_Sophia = 5;
    private static final int RADAR_CNT = 16;
    public static final int RADAR_MAX_NORMAL = 20;
    private static Runnable_REQEUST REQEUST_APP_ID = null;
    public static final UiNotifyEventString VER_BT = new UiNotifyEventString();
    public static final UiNotifyEventString VER_DVD = new UiNotifyEventString();
    public static final UiNotifyEventString VER_MCU = new UiNotifyEventString();
    public static boolean bCanInitCamera = false;
    public static boolean bIsHiworldObdEnable = SystemProperties.getBoolean("sys.fyt.hiworld_obd_enable", false);
    public static String factorySn = FinalChip.BSP_PLATFORM_Null;
    public static int mConf_PlatForm = 2;
    public static UiNotifyEvent mUiNotifyEvent = new UiNotifyEvent();
    public static int mVideoChannel;
    public static int mVideoChannelCurrent;
    public static boolean mcuMatchError = false;
    public static String naviPackageName = FinalChip.BSP_PLATFORM_Null;
    public static int sAppIdRequest;
    public static ArrayList<String> sArrayDebugBackCar;

    public static class Runnable_REQEUST implements Runnable {
        public boolean bRunning = true;

        public void run() {
            if ((Conn.mInterfaceApp == null || Conn.mInterfaceApp.isAppTop()) && Main.PROXY.getI(0, -100) != Main.sAppIdRequest) {
                Main.requestAppId(Main.sAppIdRequest);
                if (this.bRunning) {
                    Conn_Base.handler.postDelayed(this, 1000);
                }
            }
        }

        public void stop() {
            this.bRunning = false;
        }
    }

    private class Runnable_Update implements Runnable {
        public float[] flts;
        public int[] ints;
        public String[] strs;
        public int updateCode;

        public Runnable_Update(int i, int[] iArr, float[] fArr, String[] strArr) {
            this.updateCode = i;
            this.ints = iArr;
            this.flts = fArr;
            this.strs = strArr;
        }

        public void run() {
            if (this.updateCode >= 0 && this.updateCode < 256) {
                switch (this.updateCode) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 12:
                    case 13:
                    case 22:
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 30:
                    case 36:
                    case 38:
                    case 50:
                    case 65:
                    case 81:
                    case 83:
                    case 88:
                    case 89:
                    case 99:
                    case 100:
                        Main.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 90:
                    case 91:
                    case 92:
                    case 93:
                    case 94:
                    case 95:
                    case 96:
                    case 97:
                        if (IpcUtil.intsOk(this.ints, 1) && !Main.bIsHiworldObdEnable) {
                            Main.updateRadar(this.updateCode, this.ints, this.flts, this.strs);
                            return;
                        }
                        return;
                    case 28:
                        Main.updateNaviName(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 35:
                        Main.updateMcuSerial(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 41:
                        if (!Main.bIsHiworldObdEnable) {
                            Main.updateSteerAngle(this.updateCode, this.ints, this.flts, this.strs);
                            return;
                        }
                        return;
                    case 45:
                        if (IpcUtil.intsOk(this.ints, 1)) {
                            switch (this.ints[0]) {
                                case 4:
                                    if (IpcUtil.strsOk(this.strs, 1) && !TextUtils.isEmpty(this.strs[0])) {
                                        if (Main.sArrayDebugBackCar == null) {
                                            Main.sArrayDebugBackCar = new ArrayList<>();
                                        }
                                        Main.sArrayDebugBackCar.add(this.strs[0]);
                                        Main.updateDirect(this.updateCode, this.ints, this.flts, this.strs);
                                        return;
                                    }
                                    return;
                                case 5:
                                    Main.updateDirect(this.updateCode, this.ints, this.flts, this.strs);
                                    return;
                                default:
                                    Main.updateDirect(this.updateCode, this.ints, this.flts, this.strs);
                                    return;
                            }
                        } else {
                            return;
                        }
                    case 68:
                        if (IpcUtil.intsOk(this.ints, 2)) {
                            switch (this.ints[1]) {
                                case 0:
                                    Main.bCanInitCamera = false;
                                    break;
                                case 1:
                                    Main.mVideoChannelCurrent = this.ints[0];
                                    if (Main.mVideoChannelCurrent != Main.mVideoChannel) {
                                        Main.bCanInitCamera = false;
                                        break;
                                    } else {
                                        Main.bCanInitCamera = true;
                                        break;
                                    }
                            }
                            Main.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                            return;
                        }
                        return;
                    default:
                        if (IpcUtil.intsOk(this.ints, 1)) {
                            Main.DATA[this.updateCode] = this.ints[0];
                        }
                        Main.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                }
            }
        }
    }

    static {
        DATA[1] = 1;
        for (int i = 0; i < 16; i++) {
            DATA_RADAR[i] = 10;
        }
    }

    private Main() {
    }

    public static int getBackCameraId(int i) {
        return ShareHandler.is2009(i) ? 1 : 0;
    }

    public static int getBackCameraType() {
        return SystemProperties.get("ro.fyt.usbenable").equals("1") ? 2 : 1;
    }

    public static Main getInstance() {
        return INSTANCE;
    }

    public static boolean hasCamera() {
        switch (mConf_PlatForm) {
            case 5:
            case 6:
            case 7:
            case 8:
                return true;
            default:
                return false;
        }
    }

    public static void initPlatForm(String str) {
        if ("E7".equals(str)) {
            mConf_PlatForm = 1;
        } else if ("3188".equals(str)) {
            mConf_PlatForm = 2;
        } else if ("8700".equals(str)) {
            mConf_PlatForm = 3;
        } else if ("786".equals(str)) {
            mConf_PlatForm = 4;
        } else if ("Sophia".equals(str)) {
            mConf_PlatForm = 5;
        } else if (FinalChip.BSP_PLATFORM_6025.equals(str)) {
            mConf_PlatForm = 6;
        } else if ("PX5".equals(str)) {
            mConf_PlatForm = 7;
        } else if ("9853".equals(str)) {
            mConf_PlatForm = 8;
        }
    }

    public static boolean isAccOn() {
        return DATA[50] != 0;
    }

    public static void postRunnable_Ui(boolean z, Runnable runnable) {
        if (runnable != null) {
            if (z) {
                removeRunnable_Ui(runnable);
            }
            UiNotifyEvent.HANDLER_UI.post(runnable);
        }
    }

    public static void postRunnable_Ui(boolean z, Runnable runnable, long j) {
        if (runnable != null) {
            if (z) {
                removeRunnable_Ui(runnable);
            }
            if (j == 0) {
                UiNotifyEvent.HANDLER_UI.post(runnable);
            } else {
                UiNotifyEvent.HANDLER_UI.postDelayed(runnable, j);
            }
        }
    }

    public static void removeRunnable_Ui(Runnable runnable) {
        if (runnable != null) {
            UiNotifyEvent.HANDLER_UI.removeCallbacks(runnable);
        }
    }

    public static void requestAppId(int i) {
        sAppIdRequest = i;
        PROXY.cmd(0, i);
    }

    public static void requestAppIdByOnTop(int i) {
        if (Conn.mInterfaceApp == null || Conn.mInterfaceApp.isAppTop()) {
            requestAppId(i);
            if (REQEUST_APP_ID != null) {
                REQEUST_APP_ID.stop();
                REQEUST_APP_ID = null;
            }
            REQEUST_APP_ID = new Runnable_REQEUST();
            Conn_Base.handler.postDelayed(REQEUST_APP_ID, 300);
        }
    }

    public static void resetRadar() {
        updateRadar(14, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(15, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(16, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(17, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(18, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(19, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(20, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(21, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(97, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(96, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(95, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(94, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(93, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(92, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(91, new int[]{20}, (float[]) null, (String[]) null);
        updateRadar(90, new int[]{20}, (float[]) null, (String[]) null);
    }

    public static void updateDirect(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1)) {
            DATA[i] = iArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void updateMcuSerial(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.strsOk(strArr, 1)) {
            factorySn = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void updateNaviName(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.strsOk(strArr, 1)) {
            naviPackageName = strArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void updateNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        int i2 = 2;
        if (IpcUtil.intsOk(iArr, 1)) {
            switch (i) {
                case 89:
                    if (IpcUtil.intsOk(iArr, 2)) {
                        if (iArr[0] > 0) {
                            i2 = iArr[1];
                        }
                        if (DATA[i] != i2) {
                            DATA[i] = i2;
                            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    if (DATA[i] != iArr[0]) {
                        DATA[i] = iArr[0];
                        mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
                        return;
                    }
                    return;
            }
        }
    }

    public static void updateRadar(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1)) {
            DATA[i] = iArr[0];
            if (i < 90) {
                if (DATA_RADAR[i - 14] != iArr[0]) {
                    DATA_RADAR[i - 14] = iArr[0];
                    mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
                }
            } else if (i <= 93) {
                if (DATA_RADAR[(i + 12) - 90] != iArr[0]) {
                    DATA_RADAR[(i + 12) - 90] = iArr[0];
                    mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
                }
            } else if (i <= 97 && DATA_RADAR[(i + 4) - 90] != iArr[0]) {
                DATA_RADAR[(i + 4) - 90] = iArr[0];
                mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
            }
        }
    }

    public static void updateSteerAngle(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1) && DATA[i] != iArr[0]) {
            DATA[i] = iArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            VER_MCU.register(iRemoteToolkit, 0, 34, 1);
            VER_DVD.register(iRemoteToolkit, 3, 30, 1);
            VER_BT.register(iRemoteToolkit, 2, 17, 1);
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(0));
            Main instance = getInstance();
            for (int i = 0; i < 256; i++) {
                PROXY.register(instance, i, 1);
            }
            if (Conn.mInterfaceApp != null) {
                Conn.mInterfaceApp.onConnected_Main();
                if (Conn.mInterfaceApp.isAppTop()) {
                    Conn.mInterfaceApp.requestAppIdRight();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDisconnected() {
        PROXY.setRemoteModule((IRemoteModule) null);
    }

    public void update(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        postRunnable_Ui(false, new Runnable_Update(i, iArr, fArr, strArr));
    }
}
