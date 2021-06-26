package com.syu.ipcself.module.main;

import android.os.RemoteException;
import com.syu.data.FinalCanbus;
import com.syu.data.FinalChip;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.IRemoteToolkit;
import com.syu.ipcself.IConnStateListener;
import com.syu.ipcself.ModuleProxy;
import com.syu.util.IpcUtil;
import com.syu.util.UiNotifyEvent;

public class Canbus extends IModuleCallback.Stub implements IConnStateListener {
    public static String Album = null;
    public static String Artist = null;
    public static final int CHANNEL_CNT_MAX = 6;
    public static int[] CHANNEL_FREQ_PRESET = new int[7];
    public static String CarCdText = FinalChip.BSP_PLATFORM_Null;
    public static String CarFrameNum = FinalChip.BSP_PLATFORM_Null;
    public static String CarRadioText = FinalChip.BSP_PLATFORM_Null;
    public static String CdTextInfo = null;
    public static int CdTextType = 0;
    public static String CdUsbList = null;
    public static int CdUsbListType = 0;
    public static int[] DATA = new int[1200];
    public static final boolean DEBUG_AIR = true;
    public static final boolean DEBUG_DOOR = true;
    private static Canbus INSTANCE = new Canbus();
    public static int[] MCU_CANBUS_SUPPORT = new int[1000];
    public static final int MINUTEOILEXPENDMAX = 15;
    public static String MenuList = null;
    public static int MenuListType = 0;
    public static ModuleProxy PROXY = new ModuleProxy();
    public static String Songname = null;
    public static String Str1 = null;
    public static String Str2 = null;
    public static String Str3 = null;
    public static String Str4 = null;
    public static String Str5 = null;
    public static String Str6 = null;
    public static final int TRIPOILEXPENDMAX = 5;
    public static String TelTextInfo;
    public static int TelTextType;
    public static String Title;
    public static String WifiName;
    public static String WifiPsword;
    public static String anjixingNumber;
    public static String btNumber;
    public static String btPassWord;
    public static int canbusId;
    public static int carId;
    public static String cdId3Album;
    public static String cdId3Artist;
    public static String cdId3Track;
    public static int cdid3charset;
    public static int cdid3type;
    public static String current_playtime;
    public static boolean delay = false;
    public static int[] freq = new int[10];
    public static int id3infoflag;
    public static int infoIndex;
    public static int infoType;
    public static boolean jump = false;
    public static int[] mBlower_Time = new int[5];
    public static String mCarId = null;
    public static String mId3DiscName;
    public static String mId3Name;
    public static int mId3Type;
    public static String mIdArtist3Name;
    public static int[][] mMinuteoilexpend = new int[15][];
    public static int[] mTRACK_INFO = new int[2];
    public static int[] mTRACK_TIME_INFO = new int[2];
    public static int[][] mTripoilexpend = new int[5][];
    public static UiNotifyEvent mUiNotifyEvent = new UiNotifyEvent();
    public static String onStarPhoneNumber;
    public static String onStarTTS;
    public static int sCanbusId;
    public static int sMcuCanbusSupportCnt;

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
            if (this.updateCode >= 0 && this.updateCode < 1200) {
                switch (this.updateCode) {
                    case 67:
                    case 1000:
                    case 1001:
                    case 1002:
                    case 1003:
                    case 1009:
                    case 1013:
                    case FinalCanbus.U_RIGHT_CAMERA_ON_OFF:
                    case FinalCanbus.U_RIGHT_CAMERA_STATE:
                    case FinalCanbus.U_CANBUS_SLAVECAR_TOUCH_CALI:
                    case FinalCanbus.U_CANBUS_SLAVECAR_BACKLIGHT:
                        Canbus.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    case 1010:
                        Canbus.mcuCanbusSupportCnt(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    default:
                        if (IpcUtil.intsOk(this.ints, 1)) {
                            Canbus.DATA[this.updateCode] = this.ints[0];
                        }
                        Canbus.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                }
            }
        }
    }

    static {
        DATA[1001] = 1;
        DATA[1002] = 1;
    }

    private Canbus() {
    }

    public static Canbus getInstance() {
        return INSTANCE;
    }

    public static void mcuCanbusSupportCnt(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1) && sMcuCanbusSupportCnt != iArr[0]) {
            if (iArr[0] == 0) {
                sMcuCanbusSupportCnt = 0;
            }
            while (sMcuCanbusSupportCnt < iArr[0]) {
                MCU_CANBUS_SUPPORT[sMcuCanbusSupportCnt] = PROXY.getI(1000, sMcuCanbusSupportCnt, -1);
                sMcuCanbusSupportCnt++;
                if (sMcuCanbusSupportCnt >= 1000) {
                    break;
                }
            }
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public static void updateNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (IpcUtil.intsOk(iArr, 1) && DATA[i] != iArr[0]) {
            DATA[i] = iArr[0];
            mUiNotifyEvent.updateNotify(i, iArr, fArr, strArr);
        }
    }

    public void onConnected(IRemoteToolkit iRemoteToolkit) {
        try {
            PROXY.setRemoteModule(iRemoteToolkit.getRemoteModule(7));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Canbus instance = getInstance();
        for (int i = 1000; i < 1200; i++) {
            PROXY.register(instance, i, 1);
        }
    }

    public void onDisconnected() {
        PROXY.setRemoteModule((IRemoteModule) null);
        DATA[1000] = 0;
        mcuCanbusSupportCnt(1010, new int[1], (float[]) null, (String[]) null);
    }

    public void update(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        Main.postRunnable_Ui(false, new Runnable_Update(i, iArr, fArr, strArr));
    }
}
