package com.syu.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.text.format.Time;
import com.syu.data.FinalChip;
import com.syu.ipc.ModuleObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class FuncUtils {
    private static Hashtable<String, String> LOCALE_TO_CHARSET_MAP = new Hashtable<>();
    private static long lastClickTime;
    /* access modifiers changed from: private */
    public static Calendar mCalendar;
    /* access modifiers changed from: private */
    public static SimpleDateFormat mClockFormat;
    private static long mCurMillis;
    private static long mLastMillis;
    public static HashMap<String, Typeface> mTypeFaces = new HashMap<>();
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.TIMEZONE_CHANGED")) {
                FuncUtils.mCalendar = Calendar.getInstance(TimeZone.getTimeZone(intent.getStringExtra("time-zone")));
                if (FuncUtils.mClockFormat != null) {
                    FuncUtils.mClockFormat.setTimeZone(FuncUtils.mCalendar.getTimeZone());
                }
            }
        }
    };

    static {
        LOCALE_TO_CHARSET_MAP.put("ar", "ISO-8859-6");
        LOCALE_TO_CHARSET_MAP.put("be", "ISO-8859-5");
        LOCALE_TO_CHARSET_MAP.put("bg", "ISO-8859-5");
        LOCALE_TO_CHARSET_MAP.put("ca", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("cs", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("da", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("de", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("el", "ISO-8859-7");
        LOCALE_TO_CHARSET_MAP.put("es", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("et", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("fi", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("fr", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("hr", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("hu", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("is", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("it", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("iw", "ISO-8859-8");
        LOCALE_TO_CHARSET_MAP.put("ja", "Shift_JIS");
        LOCALE_TO_CHARSET_MAP.put("ko", "EUC-KR");
        LOCALE_TO_CHARSET_MAP.put("lt", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("lv", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("mk", "ISO-8859-5");
        LOCALE_TO_CHARSET_MAP.put("nl", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("no", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("pl", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("pt", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("ro", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("ru", "ISO-8859-5");
        LOCALE_TO_CHARSET_MAP.put("sh", "ISO-8859-5");
        LOCALE_TO_CHARSET_MAP.put("sk", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("sl", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("sq", "ISO-8859-2");
        LOCALE_TO_CHARSET_MAP.put("sr", "ISO-8859-5");
        LOCALE_TO_CHARSET_MAP.put("sv", "ISO-8859-1");
        LOCALE_TO_CHARSET_MAP.put("tr", "ISO-8859-9");
        LOCALE_TO_CHARSET_MAP.put("uk", "ISO-8859-5");
    }

    public static String FormatTime(long j) {
        return new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(Long.valueOf(j));
    }

    public static int HexToBcd(int i) {
        return (((i % 100) / 10) << 4) | (i % 10);
    }

    public static byte[] String2ByteArray(String str) {
        byte[] bArr = new byte[(str.length() / 2)];
        for (int i = 0; i < str.length(); i += 2) {
            bArr[i / 2] = (byte) (((byte) (char20xByte(str.charAt(i)) << 4)) | char20xByte(str.charAt(i + 1)));
        }
        return bArr;
    }

    public static byte bcd2_hex(byte b) {
        return (byte) (((byte) (b & 15)) + ((byte) (((byte) (((byte) (b >> 4)) & 15)) * 10)));
    }

    public static int byte2int(byte b) {
        return (b & 128) == 0 ? b : b & 255;
    }

    public static String byteArray2String(byte[] bArr) {
        String str = FinalChip.BSP_PLATFORM_Null;
        for (int i = 0; i < bArr.length; i++) {
            str = String.valueOf(str) + intTo0xChar((bArr[i] & 240) / 16) + intTo0xChar(bArr[i] & 15);
        }
        return str;
    }

    public static int bytes2mb(long j) {
        try {
            return new BigDecimal(j).divide(new BigDecimal(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START), 2, 0).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static byte char20xByte(char c) {
        if ('0' <= c && c <= '9') {
            return (byte) (c - '0');
        }
        if ('a' <= c && c <= 'f') {
            return (byte) ((c - 'a') + 10);
        }
        if ('A' > c || c > 'F') {
            return 0;
        }
        return (byte) ((c - 'A') + 10);
    }

    public static boolean check(int[] iArr, int i) {
        return iArr != null && iArr.length > i;
    }

    public static boolean check(Object[] objArr, int i) {
        return objArr != null && objArr.length > i;
    }

    public static boolean checkFilter(String str, String[] strArr) {
        if (TextUtils.isEmpty(str) || strArr == null) {
            return false;
        }
        for (String equals : strArr) {
            if (str.equals(equals)) {
                return true;
            }
        }
        return false;
    }

    public static byte[] convert(int[] iArr) {
        if (iArr == null) {
            return null;
        }
        int length = iArr.length;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            bArr[i] = (byte) iArr[i];
        }
        return bArr;
    }

    public static int[] convert(byte[] bArr) {
        if (bArr == 0) {
            return null;
        }
        int length = bArr.length;
        int[] iArr = new int[length];
        for (int i = 0; i < length; i++) {
            iArr[i] = bArr[i];
        }
        return iArr;
    }

    public static Bitmap createBmp(Bitmap bitmap, int i, int i2) {
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(((float) i) / ((float) bitmap.getWidth()), ((float) i2) / ((float) bitmap.getHeight()));
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Process createSuProcess() throws IOException {
        File file = new File("/system/xbin/ru");
        return file.exists() ? Runtime.getRuntime().exec(file.getAbsolutePath()) : Runtime.getRuntime().exec("su");
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0033 A[SYNTHETIC, Splitter:B:12:0x0033] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.Process createSuProcess(java.lang.String r4) throws java.io.IOException {
        /*
            r2 = 0
            java.lang.Process r0 = createSuProcess()
            java.io.DataOutputStream r1 = new java.io.DataOutputStream     // Catch:{ all -> 0x002f }
            java.io.OutputStream r3 = r0.getOutputStream()     // Catch:{ all -> 0x002f }
            r1.<init>(r3)     // Catch:{ all -> 0x002f }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x003b }
            java.lang.String r3 = java.lang.String.valueOf(r4)     // Catch:{ all -> 0x003b }
            r2.<init>(r3)     // Catch:{ all -> 0x003b }
            java.lang.String r3 = "\n"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ all -> 0x003b }
            java.lang.String r2 = r2.toString()     // Catch:{ all -> 0x003b }
            r1.writeBytes(r2)     // Catch:{ all -> 0x003b }
            java.lang.String r2 = "exit $?\n"
            r1.writeBytes(r2)     // Catch:{ all -> 0x003b }
            if (r1 == 0) goto L_0x002e
            r1.close()     // Catch:{ IOException -> 0x0039 }
        L_0x002e:
            return r0
        L_0x002f:
            r0 = move-exception
            r1 = r2
        L_0x0031:
            if (r1 == 0) goto L_0x0036
            r1.close()     // Catch:{ IOException -> 0x0037 }
        L_0x0036:
            throw r0
        L_0x0037:
            r1 = move-exception
            goto L_0x0036
        L_0x0039:
            r1 = move-exception
            goto L_0x002e
        L_0x003b:
            r0 = move-exception
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.FuncUtils.createSuProcess(java.lang.String):java.lang.Process");
    }

    public static boolean delayIsDone(int i) {
        mCurMillis = System.currentTimeMillis();
        if (Math.abs(mCurMillis - mLastMillis) <= ((long) i)) {
            return false;
        }
        mLastMillis = mCurMillis;
        return true;
    }

    public static boolean equals(int[] iArr, int[] iArr2) {
        if (iArr == iArr2) {
            return true;
        }
        if (iArr == null || iArr2 == null || iArr.length != iArr2.length) {
            return false;
        }
        int length = iArr.length;
        for (int i = 0; i < length; i++) {
            if (iArr[i] != iArr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static int fileSize(String str) {
        int i = -1;
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            i = fileInputStream.available();
            fileInputStream.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return i;
        }
    }

    public static void forgetWIFIpwd(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        wifiManager.setWifiEnabled(false);
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null && configuredNetworks.size() > 0) {
            wifiManager.removeNetwork(configuredNetworks.get(0).networkId);
        }
    }

    public static String formatDuration(long j) {
        long j2 = j / 1000;
        if (j2 <= 0) {
            return "00:00:00";
        }
        return String.format(Locale.US, "%02d:%02d:%02d", new Object[]{Long.valueOf((j2 / 60) / 60), Long.valueOf((j2 / 60) % 60), Long.valueOf(j2 % 60)});
    }

    public static String formatDuration_Sec(long j) {
        if (j <= 0) {
            return "00:00:00";
        }
        return String.format(Locale.US, "%02d:%02d:%02d", new Object[]{Long.valueOf((j / 60) / 60), Long.valueOf((j / 60) % 60), Long.valueOf(j % 60)});
    }

    public static String formatHistoryTimeString(Context context, long j) {
        int i;
        Time time = new Time();
        time.set(j);
        Time time2 = new Time();
        time2.setToNow();
        int i2 = 527104;
        if (time.year != time2.year) {
            i = 527124;
        } else {
            if (time.yearDay != time2.yearDay) {
                i2 = 527120;
            }
            i = i2 | 129;
        }
        return DateUtils.formatDateTime(context, j, i);
    }

    public static int get(ModuleObject moduleObject, int i) {
        return (moduleObject == null || moduleObject.ints == null || moduleObject.ints.length < 1) ? i : moduleObject.ints[0];
    }

    public static String get(ModuleObject moduleObject, String str) {
        return (moduleObject == null || moduleObject.strs == null || moduleObject.strs.length < 1) ? str : moduleObject.strs[0];
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0023 A[SYNTHETIC, Splitter:B:21:0x0023] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap getAssetsBitmap(android.content.res.AssetManager r3, java.lang.String r4) {
        /*
            r0 = 0
            java.io.InputStream r2 = r3.open(r4)     // Catch:{ Exception -> 0x000f, all -> 0x001f }
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeStream(r2)     // Catch:{ Exception -> 0x0034 }
            if (r2 == 0) goto L_0x000e
            r2.close()     // Catch:{ Exception -> 0x002c }
        L_0x000e:
            return r0
        L_0x000f:
            r1 = move-exception
            r2 = r0
        L_0x0011:
            r1.printStackTrace()     // Catch:{ all -> 0x0031 }
            if (r2 == 0) goto L_0x000e
            r2.close()     // Catch:{ Exception -> 0x001a }
            goto L_0x000e
        L_0x001a:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x000e
        L_0x001f:
            r1 = move-exception
            r2 = r0
        L_0x0021:
            if (r2 == 0) goto L_0x0026
            r2.close()     // Catch:{ Exception -> 0x0027 }
        L_0x0026:
            throw r1
        L_0x0027:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0026
        L_0x002c:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x000e
        L_0x0031:
            r0 = move-exception
            r1 = r0
            goto L_0x0021
        L_0x0034:
            r1 = move-exception
            goto L_0x0011
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.FuncUtils.getAssetsBitmap(android.content.res.AssetManager, java.lang.String):android.graphics.Bitmap");
    }

    public static Intent getAudioFileIntent(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(Uri.fromFile(new File(str)), "audio/*");
        return intent;
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable, int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, i, i2);
        drawable.draw(canvas);
        return createBitmap;
    }

    public static byte[] getBytes(char[] cArr) {
        Charset forName = Charset.forName("UTF-8");
        CharBuffer allocate = CharBuffer.allocate(cArr.length);
        allocate.put(cArr);
        allocate.flip();
        return forName.encode(allocate).array();
    }

    public static char[] getChars(byte[] bArr) {
        Charset forName = Charset.forName("UTF-8");
        ByteBuffer allocate = ByteBuffer.allocate(bArr.length);
        allocate.put(bArr);
        allocate.flip();
        return forName.decode(allocate).array();
    }

    public static String getCharset(Locale locale) {
        String str = LOCALE_TO_CHARSET_MAP.get(locale.toString());
        if (str != null) {
            return str;
        }
        String str2 = LOCALE_TO_CHARSET_MAP.get(locale.getLanguage());
        return str2 == null ? "GB18030" : str2;
    }

    public static String getCurrentTime(Context context) {
        return new SimpleDateFormat(DateFormat.is24HourFormat(context) ? "H:mm" : "h:mm").format(Calendar.getInstance().getTime());
    }

    public static String getDateStr() {
        return java.text.DateFormat.getDateInstance(1).format(new Date());
    }

    /* JADX WARNING: Removed duplicated region for block: B:50:0x00b1 A[SYNTHETIC, Splitter:B:50:0x00b1] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00b6 A[SYNTHETIC, Splitter:B:53:0x00b6] */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x00bb A[SYNTHETIC, Splitter:B:56:0x00bb] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00c0 A[SYNTHETIC, Splitter:B:59:0x00c0] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x00e1 A[SYNTHETIC, Splitter:B:72:0x00e1] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00e6 A[SYNTHETIC, Splitter:B:75:0x00e6] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x00eb A[SYNTHETIC, Splitter:B:78:0x00eb] */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x00f0 A[SYNTHETIC, Splitter:B:81:0x00f0] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getExternalStorageDirectory() {
        /*
            r3 = 0
            r8 = 1
            java.lang.String r0 = new java.lang.String
            r0.<init>()
            java.lang.Runtime r1 = java.lang.Runtime.getRuntime()
            java.lang.String r2 = "mount"
            java.lang.Process r6 = r1.exec(r2)     // Catch:{ Exception -> 0x00a7, all -> 0x00da }
            java.io.InputStream r5 = r6.getInputStream()     // Catch:{ Exception -> 0x012e, all -> 0x0120 }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x0134, all -> 0x0125 }
            r4.<init>(r5)     // Catch:{ Exception -> 0x0134, all -> 0x0125 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0139, all -> 0x0129 }
            r2.<init>(r4)     // Catch:{ Exception -> 0x0139, all -> 0x0129 }
        L_0x001f:
            java.lang.String r1 = r2.readLine()     // Catch:{ Exception -> 0x013d }
            if (r1 != 0) goto L_0x003a
            if (r2 == 0) goto L_0x002a
            r2.close()     // Catch:{ Exception -> 0x0108 }
        L_0x002a:
            if (r4 == 0) goto L_0x002f
            r4.close()     // Catch:{ Exception -> 0x010e }
        L_0x002f:
            if (r5 == 0) goto L_0x0034
            r5.close()     // Catch:{ Exception -> 0x0114 }
        L_0x0034:
            if (r6 == 0) goto L_0x0039
            r6.destroy()     // Catch:{ Exception -> 0x011a }
        L_0x0039:
            return r0
        L_0x003a:
            java.lang.String r3 = "secure"
            boolean r3 = r1.contains(r3)     // Catch:{ Exception -> 0x013d }
            if (r3 != 0) goto L_0x001f
            java.lang.String r3 = "asec"
            boolean r3 = r1.contains(r3)     // Catch:{ Exception -> 0x013d }
            if (r3 != 0) goto L_0x001f
            java.lang.String r3 = "fat"
            boolean r3 = r1.contains(r3)     // Catch:{ Exception -> 0x013d }
            if (r3 == 0) goto L_0x0078
            java.lang.String r3 = " "
            java.lang.String[] r1 = r1.split(r3)     // Catch:{ Exception -> 0x013d }
            if (r1 == 0) goto L_0x001f
            int r3 = r1.length     // Catch:{ Exception -> 0x013d }
            if (r3 <= r8) goto L_0x001f
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x013d }
            r7 = 1
            r1 = r1[r7]     // Catch:{ Exception -> 0x013d }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x013d }
            r3.<init>(r1)     // Catch:{ Exception -> 0x013d }
            java.lang.String r1 = "\n"
            java.lang.StringBuilder r1 = r3.append(r1)     // Catch:{ Exception -> 0x013d }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x013d }
            java.lang.String r0 = r0.concat(r1)     // Catch:{ Exception -> 0x013d }
            goto L_0x001f
        L_0x0078:
            java.lang.String r3 = "fuse"
            boolean r3 = r1.contains(r3)     // Catch:{ Exception -> 0x013d }
            if (r3 == 0) goto L_0x001f
            java.lang.String r3 = " "
            java.lang.String[] r1 = r1.split(r3)     // Catch:{ Exception -> 0x013d }
            if (r1 == 0) goto L_0x001f
            int r3 = r1.length     // Catch:{ Exception -> 0x013d }
            if (r3 <= r8) goto L_0x001f
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x013d }
            r7 = 1
            r1 = r1[r7]     // Catch:{ Exception -> 0x013d }
            java.lang.String r1 = java.lang.String.valueOf(r1)     // Catch:{ Exception -> 0x013d }
            r3.<init>(r1)     // Catch:{ Exception -> 0x013d }
            java.lang.String r1 = "\n"
            java.lang.StringBuilder r1 = r3.append(r1)     // Catch:{ Exception -> 0x013d }
            java.lang.String r1 = r1.toString()     // Catch:{ Exception -> 0x013d }
            java.lang.String r0 = r0.concat(r1)     // Catch:{ Exception -> 0x013d }
            goto L_0x001f
        L_0x00a7:
            r1 = move-exception
            r2 = r3
            r4 = r3
            r5 = r3
            r6 = r3
        L_0x00ac:
            r1.printStackTrace()     // Catch:{ all -> 0x012c }
            if (r2 == 0) goto L_0x00b4
            r2.close()     // Catch:{ Exception -> 0x00cb }
        L_0x00b4:
            if (r4 == 0) goto L_0x00b9
            r4.close()     // Catch:{ Exception -> 0x00d0 }
        L_0x00b9:
            if (r5 == 0) goto L_0x00be
            r5.close()     // Catch:{ Exception -> 0x00d5 }
        L_0x00be:
            if (r6 == 0) goto L_0x0039
            r6.destroy()     // Catch:{ Exception -> 0x00c5 }
            goto L_0x0039
        L_0x00c5:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0039
        L_0x00cb:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00b4
        L_0x00d0:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00b9
        L_0x00d5:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00be
        L_0x00da:
            r0 = move-exception
            r2 = r3
            r4 = r3
            r5 = r3
            r6 = r3
        L_0x00df:
            if (r2 == 0) goto L_0x00e4
            r2.close()     // Catch:{ Exception -> 0x00f4 }
        L_0x00e4:
            if (r4 == 0) goto L_0x00e9
            r4.close()     // Catch:{ Exception -> 0x00f9 }
        L_0x00e9:
            if (r5 == 0) goto L_0x00ee
            r5.close()     // Catch:{ Exception -> 0x00fe }
        L_0x00ee:
            if (r6 == 0) goto L_0x00f3
            r6.destroy()     // Catch:{ Exception -> 0x0103 }
        L_0x00f3:
            throw r0
        L_0x00f4:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00e4
        L_0x00f9:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00e9
        L_0x00fe:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00ee
        L_0x0103:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00f3
        L_0x0108:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x002a
        L_0x010e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x002f
        L_0x0114:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0034
        L_0x011a:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0039
        L_0x0120:
            r0 = move-exception
            r2 = r3
            r4 = r3
            r5 = r3
            goto L_0x00df
        L_0x0125:
            r0 = move-exception
            r2 = r3
            r4 = r3
            goto L_0x00df
        L_0x0129:
            r0 = move-exception
            r2 = r3
            goto L_0x00df
        L_0x012c:
            r0 = move-exception
            goto L_0x00df
        L_0x012e:
            r1 = move-exception
            r2 = r3
            r4 = r3
            r5 = r3
            goto L_0x00ac
        L_0x0134:
            r1 = move-exception
            r2 = r3
            r4 = r3
            goto L_0x00ac
        L_0x0139:
            r1 = move-exception
            r2 = r3
            goto L_0x00ac
        L_0x013d:
            r1 = move-exception
            goto L_0x00ac
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.FuncUtils.getExternalStorageDirectory():java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r0 = r3.getName();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getFileExtension(java.io.File r3) {
        /*
            if (r3 == 0) goto L_0x0021
            java.lang.String r0 = r3.getName()
            r1 = 46
            int r1 = r0.lastIndexOf(r1)
            if (r1 <= 0) goto L_0x0021
            int r2 = r0.length()
            int r2 = r2 + -1
            if (r1 >= r2) goto L_0x0021
            int r1 = r1 + 1
            java.lang.String r0 = r0.substring(r1)
            java.lang.String r0 = r0.toLowerCase()
        L_0x0020:
            return r0
        L_0x0021:
            r0 = 0
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.FuncUtils.getFileExtension(java.io.File):java.lang.String");
    }

    public static String getFileNameNoSuffix(String str) {
        if (TextUtils.isEmpty(str)) {
            return " ";
        }
        int lastIndexOf = str.lastIndexOf(".");
        return lastIndexOf >= 0 ? str.substring(0, lastIndexOf) : str;
    }

    public static String getFormatSize(double d) {
        double d2 = d / 1024.0d;
        if (d2 < 1.0d) {
            return String.valueOf(d) + "Byte(s)";
        }
        double d3 = d2 / 1024.0d;
        if (d3 < 1.0d) {
            return String.valueOf(new BigDecimal(Double.toString(d2)).setScale(2, 4).toPlainString()) + "KB";
        }
        double d4 = d3 / 1024.0d;
        if (d4 < 1.0d) {
            return String.valueOf(new BigDecimal(Double.toString(d3)).setScale(2, 4).toPlainString()) + "MB";
        }
        double d5 = d4 / 1024.0d;
        return d5 < 1.0d ? String.valueOf(new BigDecimal(Double.toString(d4)).setScale(2, 4).toPlainString()) + "GB" : String.valueOf(new BigDecimal(d5).setScale(2, 4).toPlainString()) + "TB";
    }

    public static int getNavigationBarHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("navigation_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getNumSlash(String str) {
        String[] split;
        if (TextUtils.isEmpty(str) || (split = str.split("/")) == null) {
            return 0;
        }
        return split.length;
    }

    public static Intent getPhotoFileIntent(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(Uri.fromFile(new File(str)), "image/*");
        return intent;
    }

    public static int getSDcardSpace() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return bytes2mb(((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize()));
    }

    public static int getStatusHeight(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getStatusWidth(Context context) {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return context.getResources().getDimensionPixelSize(Integer.parseInt(cls.getField("status_bar_width").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static CharSequence getTimeUtil(boolean z, Context context) {
        try {
            return !DateFormat.is24HourFormat(context) ? z ? DateUtils.getAMPMString(Calendar.getInstance().get(9)) : Calendar.getInstance().get(9) == 0 ? "AM" : "PM" : FinalChip.BSP_PLATFORM_Null;
        } catch (Exception e) {
            e.printStackTrace();
            return FinalChip.BSP_PLATFORM_Null;
        }
    }

    public static Typeface getTypefaceFromFile(String str) {
        if (mTypeFaces.containsKey(str)) {
            return mTypeFaces.get(str);
        }
        Typeface typeface = null;
        File file = new File(str);
        if (file.exists()) {
            typeface = Typeface.createFromFile(file);
        }
        mTypeFaces.put(str, typeface);
        return typeface;
    }

    public static int getUnsignedByte(byte b) {
        return b & 255;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0006, code lost:
        r0 = r2.lastIndexOf(46);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getUrlExtension(java.lang.String r2) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto L_0x0021
            r0 = 46
            int r0 = r2.lastIndexOf(r0)
            if (r0 <= 0) goto L_0x0021
            int r1 = r2.length()
            int r1 = r1 + -1
            if (r0 >= r1) goto L_0x0021
            int r0 = r0 + 1
            java.lang.String r0 = r2.substring(r0)
            java.lang.String r0 = r0.toLowerCase()
        L_0x0020:
            return r0
        L_0x0021:
            java.lang.String r0 = ""
            goto L_0x0020
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.FuncUtils.getUrlExtension(java.lang.String):java.lang.String");
    }

    public static int getVal(int i, int i2, int i3) {
        return i < i2 ? i2 : i > i3 ? i3 : i;
    }

    public static Intent getVideoFileIntent(String str) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(Uri.fromFile(new File(str)), "video/*");
        return intent;
    }

    public static char intTo0xChar(int i) {
        if (i < 0 || i > 15) {
            return '?';
        }
        return i < 10 ? (char) (i + 48) : (char) ((i - 10) + 97);
    }

    public static boolean isAppInstalled(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(str, 0);
        } catch (Exception e) {
        }
        return packageInfo != null;
    }

    public static boolean isAssetsFileExists(Context context, String str, String str2) {
        try {
            String[] list = context.getAssets().list(str);
            for (String equalsIgnoreCase : list) {
                if (equalsIgnoreCase.equalsIgnoreCase(str2)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isEquals(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        return str.equals(str2);
    }

    public static boolean isFastDoubleClick() {
        long currentTimeMillis = System.currentTimeMillis();
        if (Math.abs(currentTimeMillis - lastClickTime) < 400) {
            return true;
        }
        lastClickTime = currentTimeMillis;
        return false;
    }

    public static boolean isFastDoubleClick(int i) {
        long currentTimeMillis = System.currentTimeMillis();
        if (Math.abs(currentTimeMillis - lastClickTime) < ((long) i)) {
            return true;
        }
        lastClickTime = currentTimeMillis;
        return false;
    }

    public static boolean isFileExist(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return new File(str).exists();
    }

    public static int makeInt(byte b, byte b2) {
        return ((b & 255) << 8) | (b2 & 255);
    }

    public static int makeInt(byte b, byte b2, byte b3) {
        return ((b & 255) << 16) | ((b2 & 255) << 8) | (b3 & 255);
    }

    public static int makeInt(byte b, byte b2, byte b3, byte b4) {
        return ((b & 255) << 24) | ((b2 & 255) << 16) | ((b3 & 255) << 8) | (b4 & 255);
    }

    public static String msFDuration(long j) {
        long j2 = j / 1000;
        if (j2 < 0) {
            return "00:00";
        }
        if (j2 < 60) {
            return String.format(Locale.US, "00:%02d", new Object[]{Long.valueOf(j2)});
        } else if (j2 < 3600) {
            return String.format(Locale.US, "%02d:%02d", new Object[]{Long.valueOf(j2 / 60), Long.valueOf(j2 % 60)});
        } else {
            return String.format(Locale.US, "%d %02d:%02d", new Object[]{Long.valueOf(j2 / 3600), Long.valueOf((j2 % 3600) / 60), Long.valueOf(j2 % 60)});
        }
    }

    public static byte[] readFile(String str) {
        byte[] bArr = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            fileInputStream.close();
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return bArr;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0036 A[SYNTHETIC, Splitter:B:27:0x0036] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x003b A[SYNTHETIC, Splitter:B:30:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0040 A[SYNTHETIC, Splitter:B:33:0x0040] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0058 A[SYNTHETIC, Splitter:B:44:0x0058] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x005d A[SYNTHETIC, Splitter:B:47:0x005d] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0062 A[SYNTHETIC, Splitter:B:50:0x0062] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String readStrFromStream(java.io.InputStream r5) throws java.io.IOException {
        /*
            r0 = 0
            if (r5 == 0) goto L_0x002b
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ Exception -> 0x008a, all -> 0x0053 }
            r3.<init>(r5)     // Catch:{ Exception -> 0x008a, all -> 0x0053 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ Exception -> 0x008e, all -> 0x0084 }
            r2.<init>(r3)     // Catch:{ Exception -> 0x008e, all -> 0x0084 }
            java.lang.StringBuffer r1 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x0030 }
            r1.<init>()     // Catch:{ Exception -> 0x0030 }
        L_0x0012:
            java.lang.String r4 = r2.readLine()     // Catch:{ Exception -> 0x0030 }
            if (r4 != 0) goto L_0x002c
            java.lang.String r0 = r1.toString()     // Catch:{ Exception -> 0x0030 }
            if (r2 == 0) goto L_0x0021
            r2.close()     // Catch:{ Exception -> 0x0075 }
        L_0x0021:
            if (r3 == 0) goto L_0x0026
            r3.close()     // Catch:{ Exception -> 0x007a }
        L_0x0026:
            if (r5 == 0) goto L_0x002b
            r5.close()     // Catch:{ Exception -> 0x007f }
        L_0x002b:
            return r0
        L_0x002c:
            r1.append(r4)     // Catch:{ Exception -> 0x0030 }
            goto L_0x0012
        L_0x0030:
            r1 = move-exception
        L_0x0031:
            r1.printStackTrace()     // Catch:{ all -> 0x0087 }
            if (r2 == 0) goto L_0x0039
            r2.close()     // Catch:{ Exception -> 0x0049 }
        L_0x0039:
            if (r3 == 0) goto L_0x003e
            r3.close()     // Catch:{ Exception -> 0x004e }
        L_0x003e:
            if (r5 == 0) goto L_0x002b
            r5.close()     // Catch:{ Exception -> 0x0044 }
            goto L_0x002b
        L_0x0044:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x002b
        L_0x0049:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0039
        L_0x004e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x003e
        L_0x0053:
            r1 = move-exception
            r2 = r0
            r3 = r0
        L_0x0056:
            if (r2 == 0) goto L_0x005b
            r2.close()     // Catch:{ Exception -> 0x0066 }
        L_0x005b:
            if (r3 == 0) goto L_0x0060
            r3.close()     // Catch:{ Exception -> 0x006b }
        L_0x0060:
            if (r5 == 0) goto L_0x0065
            r5.close()     // Catch:{ Exception -> 0x0070 }
        L_0x0065:
            throw r1
        L_0x0066:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x005b
        L_0x006b:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0060
        L_0x0070:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0065
        L_0x0075:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0021
        L_0x007a:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0026
        L_0x007f:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x002b
        L_0x0084:
            r1 = move-exception
            r2 = r0
            goto L_0x0056
        L_0x0087:
            r0 = move-exception
            r1 = r0
            goto L_0x0056
        L_0x008a:
            r1 = move-exception
            r2 = r0
            r3 = r0
            goto L_0x0031
        L_0x008e:
            r1 = move-exception
            r2 = r0
            goto L_0x0031
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.util.FuncUtils.readStrFromStream(java.io.InputStream):java.lang.String");
    }

    public static void requestPermission() throws InterruptedException, IOException {
        createSuProcess("chmod 666 /dev/alarm").waitFor();
    }

    public static String sFDuration(int i) {
        if (i <= 0) {
            return "00:00";
        }
        StringBuffer stringBuffer = new StringBuffer();
        int i2 = i % 60;
        int i3 = (i / 60) % 60;
        int i4 = ((i / 60) / 60) % 60;
        if (i4 > 0) {
            stringBuffer.append(String.valueOf(i4) + ":");
        }
        if (i3 < 10) {
            stringBuffer.append("0");
        }
        stringBuffer.append(String.valueOf(i3) + ":");
        if (i2 < 10) {
            stringBuffer.append("0");
        }
        stringBuffer.append(i2);
        return stringBuffer.toString();
    }

    public static void setDateTime(int i, int i2) {
        try {
            requestPermission();
            Calendar instance = Calendar.getInstance();
            instance.set(11, i);
            instance.set(12, i2);
            long timeInMillis = instance.getTimeInMillis();
            if (timeInMillis / 1000 < 2147483647L) {
                SystemClock.setCurrentTimeMillis(timeInMillis);
            }
            if (Calendar.getInstance().getTimeInMillis() - timeInMillis > 1000) {
                throw new IOException("failed to set Date.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sleep(int i) {
        try {
            Thread.sleep((long) i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean strEqual(String str, String str2) {
        return str == null ? str2 == null : str.equals(str2);
    }
}
