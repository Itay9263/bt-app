package com.syu.jni;

import android.os.Bundle;

public class ToolsJni {
    private static int cmdCpu = -1;

    public static byte[] cmd_101_getT132Parama() {
        byte[] bArr = new byte[16384];
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance != null) {
            Bundle bundle = new Bundle();
            bundle.putByteArray("param1", bArr);
            instance.syu_jni_command(101, (Object) null, bundle);
        }
        return bArr;
    }

    public static int cmd_104_write_gamma(byte[] bArr) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putByteArray("param0", bArr);
        return instance.syu_jni_command(104, bundle, (Object) null);
    }

    public static int cmd_105_set_bl_adj(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(105, bundle, (Object) null);
    }

    public static int[] cmd_109_get_bl_limit() {
        int[] iArr = new int[3];
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance != null) {
            Bundle bundle = new Bundle();
            instance.syu_jni_command(109, (Object) null, bundle);
            if (bundle != null) {
                iArr[0] = bundle.getInt("param0", 0);
                iArr[1] = bundle.getInt("param1", 0);
                iArr[2] = bundle.getInt("param2", 0);
            }
        }
        return iArr;
    }

    public static int cmd_10_little_hom(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(10, bundle, (Object) null);
    }

    public static int cmd_110_set_bl_limit(int i, int i2) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        bundle.putInt("param1", i2);
        return instance.syu_jni_command(110, bundle, (Object) null);
    }

    public static int cmd_118_get_Touchscreen_Sensitivity(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle.putInt("touchlevelcnt", 5);
        bundle.putByteArray("paramalevel0", bArr);
        bundle.putByteArray("paramalevel1", bArr2);
        bundle.putByteArray("paramalevel2", bArr3);
        bundle.putByteArray("paramalevel3", bArr4);
        bundle.putByteArray("paramalevel4", bArr5);
        instance.syu_jni_command(118, bundle, bundle2);
        if (bundle2 != null) {
            return bundle2.getInt("touchlevel", -1);
        }
        return 0;
    }

    public static int cmd_119_set_Touchscreen_Sensitivity(int i, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, byte[] bArr5) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle.putInt("touchlevelcnt", 5);
        bundle.putInt("setlevel", i);
        bundle.putByteArray("paramalevel0", bArr);
        bundle.putByteArray("paramalevel1", bArr2);
        bundle.putByteArray("paramalevel2", bArr3);
        bundle.putByteArray("paramalevel3", bArr4);
        bundle.putByteArray("paramalevel4", bArr5);
        instance.syu_jni_command(119, bundle, bundle2);
        if (bundle2 != null) {
            return bundle2.getInt("touchlevel", -1);
        }
        return 0;
    }

    public static int cmd_11_video_ntsc_pal(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(11, bundle, (Object) null);
    }

    public static int cmd_12_get_usb_speed() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        instance.syu_jni_command(12, (Object) null, bundle);
        if (bundle != null) {
            return bundle.getInt("param0", 0);
        }
        return 0;
    }

    public static int cmd_13_write_usb_speed(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(13, bundle, (Object) null);
    }

    public static byte[] cmd_148_read_data(int i, int i2) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance != null) {
            Bundle bundle = new Bundle();
            Bundle bundle2 = new Bundle();
            bundle.putInt("offset", i2);
            bundle.putInt("readsize", i);
            if (instance.syu_jni_command(148, bundle, bundle2) == 0) {
                return bundle2.getByteArray("appdata");
            }
        }
        return null;
    }

    public static int cmd_149_write_data(byte[] bArr, int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putByteArray("writedata", bArr);
        bundle.putInt("offset", i);
        return instance.syu_jni_command(149, bundle, (Object) null);
    }

    public static int cmd_14_set_backcar_type(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 1;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(14, bundle, (Object) null);
    }

    public static int cmd_153_gsensor_power_onoff(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(153, bundle, (Object) null);
    }

    public static int cmd_15_get_backcar_type() {
        try {
            SyuJniNative instance = SyuJniNative.getInstance();
            if (instance == null) {
                return 0;
            }
            Bundle bundle = new Bundle();
            instance.syu_jni_command(15, (Object) null, bundle);
            if (bundle != null) {
                return bundle.getInt("param0", 0);
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int cmd_160_write_rotation_angle(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(160, bundle, bundle2);
    }

    public static int cmd_16_set_led_colors(int i, int i2) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        bundle.putInt("param1", i2);
        return instance.syu_jni_command(16, bundle, (Object) null);
    }

    public static int cmd_17_get_led_colors() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        instance.syu_jni_command(17, (Object) null, bundle);
        if (bundle != null) {
            return bundle.getInt("param0", 0);
        }
        return 0;
    }

    public static int cmd_19_airplane_mode(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(19, bundle, (Object) null);
    }

    public static int cmd_1_backcarMirror(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(1, bundle, (Object) null);
    }

    public static int cmd_22_set_video_mode(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(22, bundle, (Object) null);
    }

    public static void cmd_24_reset_8288a() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance != null) {
            instance.syu_jni_command(24, (Object) null, (Object) null);
        }
    }

    public static int cmd_25_get_video_mode() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        instance.syu_jni_command(25, (Object) null, bundle);
        if (bundle != null) {
            return bundle.getInt("param0", 0);
        }
        return 0;
    }

    public static int cmd_26_get_video_signal_on() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 1;
        }
        Bundle bundle = new Bundle();
        instance.syu_jni_command(26, (Object) null, bundle);
        if (bundle != null) {
            return bundle.getInt("param0", 1);
        }
        return 1;
    }

    public static int cmd_27_set_bl_param(int i, int i2, int i3) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param3", i);
        bundle.putInt("param4", i2);
        bundle.putInt("param5", i3);
        return instance.syu_jni_command(27, bundle, (Object) null);
    }

    public static int[] cmd_28_get_bl_param() {
        int[] iArr = new int[6];
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance != null) {
            Bundle bundle = new Bundle();
            instance.syu_jni_command(28, (Object) null, bundle);
            if (bundle != null) {
                iArr[0] = bundle.getInt("param0");
                iArr[1] = bundle.getInt("param1");
                iArr[2] = bundle.getInt("param2");
                iArr[3] = bundle.getInt("param3");
                iArr[4] = bundle.getInt("param4");
                iArr[5] = bundle.getInt("param5");
            }
        }
        return iArr;
    }

    public static int cmd_29_acc_state_to_bsp(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(29, bundle, (Object) null);
    }

    public static int cmd_2_soundMix(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(2, bundle, (Object) null);
    }

    public static void cmd_31_fan_en(int i) {
        if (cmdCpu != i) {
            cmdCpu = i;
            SyuJniNative instance = SyuJniNative.getInstance();
            if (instance != null) {
                Bundle bundle = new Bundle();
                bundle.putInt("param0", cmdCpu);
                instance.syu_jni_command(31, bundle, (Object) null);
            }
        }
    }

    public static int cmd_32_get_boot_reverse_status() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        instance.syu_jni_command(32, (Object) null, bundle);
        if (bundle != null) {
            return bundle.getInt("param0", 0);
        }
        return 0;
    }

    public static void cmd_33_reset_videoIc(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("param0", 1);
            instance.syu_jni_command(33, bundle, (Object) null);
        }
    }

    public static int cmd_3_encarption_result() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        instance.syu_jni_command(3, (Object) null, bundle);
        if (bundle != null) {
            return bundle.getInt("param0", 1);
        }
        return 0;
    }

    public static int cmd_4_audio_state() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 1;
        }
        Bundle bundle = new Bundle();
        instance.syu_jni_command(4, (Object) null, bundle);
        if (bundle != null) {
            return bundle.getInt("param0", 0);
        }
        return 1;
    }

    public static int cmd_50_fm_ant_en(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(50, bundle, (Object) null);
    }

    public static int cmd_5_turnoff_lcdc(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(5, bundle, (Object) null);
    }

    public static int cmd_6_mute_amp(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(6, bundle, (Object) null);
    }

    public static int cmd_7_get_amp_state() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        instance.syu_jni_command(7, (Object) null, bundle);
        if (bundle != null) {
            return bundle.getInt("param0", 0);
        }
        return 0;
    }

    public static int cmd_8_reset_gps() {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance != null) {
            return instance.syu_jni_command(8, (Object) null, (Object) null);
        }
        return 0;
    }

    public static int cmd_9_poweron_screen(int i) {
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("param0", i);
        return instance.syu_jni_command(9, bundle, (Object) null);
    }

    public static int setVideoPos_Px5(int[] iArr) {
        SyuJniNative instance;
        if (iArr == null || iArr.length != 4 || (instance = SyuJniNative.getInstance()) == null) {
            return 0;
        }
        Bundle bundle = new Bundle();
        Bundle bundle2 = new Bundle();
        bundle.putIntArray("param0", iArr);
        return instance.syu_jni_command(34, bundle, bundle2);
    }

    public static int testJni() {
        int i = 0;
        SyuJniNative instance = SyuJniNative.getInstance();
        if (instance != null) {
            Bundle bundle = new Bundle();
            Bundle bundle2 = new Bundle();
            bundle.putInt("test_param", 100);
            i = instance.syu_jni_command(0, bundle, bundle2);
            if (bundle2 != null) {
                bundle2.getDouble("test_param");
            }
        }
        return i;
    }
}
