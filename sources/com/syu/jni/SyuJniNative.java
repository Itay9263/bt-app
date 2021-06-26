package com.syu.jni;

public class SyuJniNative {
    public static SyuJniNative INSTANCE = null;
    public static final int JNI_EXE_CMD_0_TEST = 0;
    public static final int JNI_EXE_CMD_101_PARAMA_READ = 101;
    public static final int JNI_EXE_CMD_104_WRITE_GAMMA = 104;
    public static final int JNI_EXE_CMD_105_SET_BL_ADJ = 105;
    public static final int JNI_EXE_CMD_106_SET_BL_LIMIT = 106;
    public static final int JNI_EXE_CMD_107_GET_BL_PARAM = 107;
    public static final int JNI_EXE_CMD_109_GET_BL_DUTY = 109;
    public static final int JNI_EXE_CMD_10_LITTLE_HOM = 10;
    public static final int JNI_EXE_CMD_110_SET_BL_DUTY = 110;
    public static final int JNI_EXE_CMD_112_IS_AREAACTIVED = 112;
    public static final int JNI_EXE_CMD_113_ACTIVE_AREA = 113;
    public static final int JNI_EXE_CMD_114_GET_ACTIVEKEY = 114;
    public static final int JNI_EXE_CMD_118_GET_TOUCHLEVEL = 118;
    public static final int JNI_EXE_CMD_119_SET_TOUCHLEVEL = 119;
    public static final int JNI_EXE_CMD_11_VIDEO_NTSC_PAL = 11;
    public static final int JNI_EXE_CMD_12_GET_USB_SPEED = 12;
    public static final int JNI_EXE_CMD_13_SET_USB_SPEED = 13;
    public static final int JNI_EXE_CMD_148_READ_APPDATE = 148;
    public static final int JNI_EXE_CMD_149_WRITE_APPDATE = 149;
    public static final int JNI_EXE_CMD_14_SET_BACKCAR_TYPE = 14;
    public static final int JNI_EXE_CMD_150_GET_WRITE_FLASH_PROCESS_PID = 150;
    public static final int JNI_EXE_CMD_151_SET_WRITE_FLASH_PROCESS_PID_FLAG = 151;
    public static final int JNI_EXE_CMD_153_GSENSOR_POWER_ONOFF = 153;
    public static final int JNI_EXE_CMD_15_GET_BACKCAR_TYPE = 15;
    public static final int JNI_EXE_CMD_16_SET_LED_COLORS = 16;
    public static final int JNI_EXE_CMD_17_GET_LED_COLORS = 17;
    public static final int JNI_EXE_CMD_19_SET_AIRPLANE_MODE = 19;
    public static final int JNI_EXE_CMD_1_BACKCAR_MIRROR = 1;
    public static final int JNI_EXE_CMD_22_SET_VIDEO_SYS_MODE = 22;
    public static final int JNI_EXE_CMD_23_GET_VIDEO_SYS_MODE = 23;
    public static final int JNI_EXE_CMD_24_RESET_8288A = 24;
    public static final int JNI_EXE_CMD_25_GET_VIDEO_MODE = 25;
    public static final int JNI_EXE_CMD_26_GET_VIDEO_SIGNAL_ON = 26;
    public static final int JNI_EXE_CMD_27_SET_BL_PARAM = 27;
    public static final int JNI_EXE_CMD_28_GET_BL_PARAM = 28;
    public static final int JNI_EXE_CMD_29_ACC_STATE_TO_BSP = 29;
    public static final int JNI_EXE_CMD_2_SOUND_MIX = 2;
    public static final int JNI_EXE_CMD_31_FAN_EN = 31;
    public static final int JNI_EXE_CMD_32_GET_BOOT_REVERSE_STATUS = 32;
    public static final int JNI_EXE_CMD_33_RESET_VIDEOIC = 33;
    public static final int JNI_EXE_CMD_34_CONTROL_SET_VIDEO_POSITION = 34;
    public static final int JNI_EXE_CMD_35_SET_AUDIO_SAMPLE_RATE = 35;
    public static final int JNI_EXE_CMD_3_ENCARPTION_RESULT = 3;
    public static final int JNI_EXE_CMD_4_AUDIO_STATE = 4;
    public static final int JNI_EXE_CMD_50_FM_ANT_EN = 50;
    public static final int JNI_EXE_CMD_5_TURNOFF_LCDC = 5;
    public static final int JNI_EXE_CMD_6_MUTE_AMP = 6;
    public static final int JNI_EXE_CMD_7_AMP_MUTE_STATE = 7;
    public static final int JNI_EXE_CMD_8_RESET_GPS = 8;
    public static final int JNI_EXE_CMD_9_POWERON_SCREEN = 9;
    public static boolean bLoadLibOnce = false;

    private SyuJniNative() {
        System.loadLibrary("syu_jni");
    }

    public static SyuJniNative getInstance() {
        if (!bLoadLibOnce) {
            bLoadLibOnce = true;
            try {
                if (INSTANCE == null) {
                    INSTANCE = new SyuJniNative();
                }
            } catch (Throwable th) {
                INSTANCE = null;
            }
        }
        return INSTANCE;
    }

    public native int syu_jni_command(int i, Object obj, Object obj2);
}
