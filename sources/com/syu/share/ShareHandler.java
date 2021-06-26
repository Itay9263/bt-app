package com.syu.share;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemProperties;
import com.syu.data.FinalChip;

public class ShareHandler {
    public static final int KEY_EXT_DVR_TYPE = 8;
    public static final int KEY_MODULE_ID_AMP = 5;
    public static final int KEY_MODULE_ID_DVD = 4;
    public static final int KEY_MODULE_ID_OBD = 7;
    public static final int KEY_MODULE_ID_SOUND = 3;
    public static final int KEY_MODULE_ID_TPMS = 6;
    public static final int KEY_TV_TYPE = 0;
    public static final int KEY_USE_AMP_EQ = 2;
    public static final int KEY_USE_CAR_EQ = 1;
    public static final Uri URI = Uri.parse("content://com.syu.ms.provider");

    public static int getCustomerID() {
        return SystemProperties.getInt("ro.build.fytmanufacturer", 2);
    }

    public static String getInSimIccid() {
        return SystemProperties.get("ro.sim2.iccid", FinalChip.BSP_PLATFORM_Null);
    }

    public static int getInt(ContentResolver contentResolver, int i, int i2) {
        Cursor query;
        if (!(contentResolver == null || (query = contentResolver.query(URI, (String[]) null, Integer.toString(i), (String[]) null, (String) null)) == null)) {
            try {
                if (query.moveToFirst()) {
                    i2 = query.getInt(0);
                }
                if (query != null) {
                    try {
                        query.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (query != null) {
                    try {
                        query.close();
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                }
                throw th;
            }
        }
        return i2;
    }

    public static int getUIID() {
        return SystemProperties.getInt("ro.fyt.uiid", 2);
    }

    public static int getVerAngle() {
        return SystemProperties.getInt("ro.sf.hwrotation", 0);
    }

    public static boolean is1280Split() {
        return SystemProperties.getInt("ro.fyt.ccbwin", 1) == 0;
    }

    public static boolean is2009(int i) {
        return 9 == i;
    }

    public static boolean is6322() {
        return SystemProperties.get("ro.fyt.platform", FinalChip.BSP_PLATFORM_Null).equals(FinalChip.BSP_PLATFORM_6322);
    }

    public static boolean isCanbusM1A() {
        return SystemProperties.getInt("ro.fyt.canbus_id", 0) == 61444;
    }

    public static boolean isCanbusNianXingChe() {
        return SystemProperties.getInt("ro.fyt.canbus_id", 0) == 61445;
    }

    public static boolean isCanbusT21() {
        return SystemProperties.getInt("ro.fyt.canbus_id", 0) == 61443;
    }

    public static boolean isCanbusX5() {
        return SystemProperties.getInt("ro.fyt.canbus_id", 0) == 61446;
    }

    public static boolean isCanbusYunDu() {
        return SystemProperties.getInt("ro.fyt.canbus_id", 0) == 61447;
    }

    public static boolean isCarScreen(int i) {
        switch (i) {
            case 24:
                return true;
            default:
                return false;
        }
    }

    public static boolean isDebug2825() {
        return SystemProperties.getBoolean("sys.fyt.video.debug", false);
    }

    public static boolean isExistInSim() {
        return SystemProperties.getInt("ril.has_sim1", 0) == 1;
    }

    public static boolean isForeginClient() {
        return SystemProperties.getBoolean("ro.client.foreign", false);
    }

    public static boolean isHiworldObdEnable() {
        return SystemProperties.getBoolean("sys.fyt.hiworld_obd_enable", false);
    }

    public static boolean isInclude8288A(int i) {
        switch (i) {
            case 10:
            case 12:
            case 13:
            case 38:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMcuReverse() {
        return 1 == SystemProperties.getInt("sys.fyt.mcu_reverse", 0);
    }

    public static boolean isModifyDefaultLogo() {
        return SystemProperties.getBoolean("sys.fyt.zyc_default_logo_modify", false);
    }

    public static boolean isNeedStartFrontView() {
        return SystemProperties.getBoolean("persist.fyt.fy_radartofront", false);
    }

    public static boolean isSerialObdEnable() {
        return SystemProperties.getBoolean("persist.fyt.serial_obd_enable", false);
    }

    public static boolean isShowMark() {
        return SystemProperties.getInt("ro.fyt.showmark", 0) == 1;
    }

    public static boolean isShowTrackLine() {
        return SystemProperties.getInt("ro.fyt.showtrack", 0) == 1;
    }

    public static boolean isSofiaNeedActive(int i, int i2) {
        switch (i) {
            case 23:
            case 35:
                switch (i2) {
                    case 62:
                    case 83:
                        return false;
                    default:
                        return true;
                }
            case 28:
            case 29:
            case 36:
            case 37:
                return true;
            default:
                return false;
        }
    }

    public static boolean isThirdVerUI() {
        return SystemProperties.getBoolean("ro.build.fytThirdUI", false);
    }

    public static boolean isTp2825() {
        return SystemProperties.getInt("ro.fyt.m_VideoIC", 0) == 3;
    }

    public static boolean isTp2850() {
        return "TP2850".equals(SystemProperties.get("sys.fyt.video_ic"));
    }

    public static boolean isTpPR2000() {
        return SystemProperties.getInt("ro.fyt.m_VideoIC", 0) == 6;
    }
}
