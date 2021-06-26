package defpackage;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.os.Environment;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.WindowManager;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.data.FinalChip;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/* renamed from: bt  reason: default package */
public class bt {
    public static final String[] d = {"data1", "display_name", "starred", "contact_id", "lookup"};
    static SoundPool e;
    static int f = -1000000;
    private static bt k;
    private static long l;
    private static Typeface m;
    public String a = "TZY";
    public int[] b;
    final long c = 86400000;
    private String g = FinalChip.BSP_PLATFORM_Null;
    private String h = FinalChip.BSP_PLATFORM_Null;
    private String[] i = {"/storage/USB1/", "/storage/USB2/", "/storage/USB3/", "/storage/USB4/", "/storage/sd1/", "/storage/sd2/"};
    private String j = "num";

    public static Typeface a(Context context) {
        if (m == null) {
            m = Typeface.createFromAsset(context.getAssets(), "fonts/ystextregular.otf");
        }
        return m;
    }

    public static bt a() {
        if (k == null) {
            k = new bt();
        }
        return k;
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String a(Long l2) {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(l2.longValue()));
    }

    /* JADX WARNING: Removed duplicated region for block: B:14:0x0050  */
    /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void a(android.content.Context r8, int r9, final int r10) {
        /*
            r7 = 100
            r1 = 0
            r6 = 2
            r4 = 3
            r5 = 1
            java.lang.String r0 = com.syu.app.App.mStrCustomer
            java.lang.String r2 = "TZY_NEW"
            boolean r0 = r0.equalsIgnoreCase(r2)
            if (r0 != 0) goto L_0x0011
        L_0x0010:
            return
        L_0x0011:
            java.lang.String r0 = "ringtype"
            int r0 = com.syu.util.MySharePreference.getIntValue(r0, r4)
            if (r0 == r4) goto L_0x0010
            g()
            int r2 = android.os.Build.VERSION.SDK_INT
            r3 = 21
            if (r2 < r3) goto L_0x0061
            android.media.SoundPool$Builder r2 = new android.media.SoundPool$Builder
            r2.<init>()
            r2.setMaxStreams(r5)
            android.media.AudioAttributes$Builder r3 = new android.media.AudioAttributes$Builder
            r3.<init>()
            r3.setLegacyStreamType(r4)
            android.media.AudioAttributes r3 = r3.build()
            r2.setAudioAttributes(r3)
            android.media.SoundPool r2 = r2.build()
            e = r2
        L_0x003f:
            android.content.res.AssetManager r3 = r8.getAssets()
            r2 = 0
            if (r0 != r5) goto L_0x0072
            if (r9 != r6) goto L_0x0069
            java.lang.String r0 = "ring_connect.mp3"
            android.content.res.AssetFileDescriptor r0 = r3.openFd(r0)     // Catch:{ IOException -> 0x00a6 }
        L_0x004e:
            if (r0 == 0) goto L_0x0010
            android.media.SoundPool r1 = e
            int r0 = r1.load(r0, r5)
            android.media.SoundPool r1 = e
            bt$1 r2 = new bt$1
            r2.<init>(r0, r10)
            r1.setOnLoadCompleteListener(r2)
            goto L_0x0010
        L_0x0061:
            android.media.SoundPool r2 = new android.media.SoundPool
            r2.<init>(r5, r4, r1)
            e = r2
            goto L_0x003f
        L_0x0069:
            if (r9 != r7) goto L_0x00aa
            java.lang.String r0 = "ring_search.mp3"
            android.content.res.AssetFileDescriptor r0 = r3.openFd(r0)     // Catch:{ IOException -> 0x00a6 }
            goto L_0x004e
        L_0x0072:
            java.util.Locale r0 = java.util.Locale.getDefault()     // Catch:{ IOException -> 0x00a6 }
            java.lang.String r0 = r0.getLanguage()     // Catch:{ IOException -> 0x00a6 }
            java.lang.String r4 = "ru"
            boolean r0 = r0.contains(r4)     // Catch:{ IOException -> 0x00a6 }
            if (r0 == 0) goto L_0x0096
            if (r9 != r6) goto L_0x008c
            java.lang.String r0 = "ru_connect_tone.wav"
            android.content.res.AssetFileDescriptor r0 = r3.openFd(r0)     // Catch:{ IOException -> 0x00ac }
            r10 = r1
            goto L_0x004e
        L_0x008c:
            if (r9 != r7) goto L_0x00af
            java.lang.String r0 = "ru_search_tone.wav"
            android.content.res.AssetFileDescriptor r0 = r3.openFd(r0)     // Catch:{ IOException -> 0x00ac }
            r10 = r1
            goto L_0x004e
        L_0x0096:
            if (r9 != r6) goto L_0x009f
            java.lang.String r0 = "connect_tone.mp3"
            android.content.res.AssetFileDescriptor r0 = r3.openFd(r0)     // Catch:{ IOException -> 0x00a6 }
            goto L_0x004e
        L_0x009f:
            java.lang.String r0 = "search_tone.mp3"
            android.content.res.AssetFileDescriptor r0 = r3.openFd(r0)     // Catch:{ IOException -> 0x00a6 }
            goto L_0x004e
        L_0x00a6:
            r0 = move-exception
        L_0x00a7:
            r0.printStackTrace()
        L_0x00aa:
            r0 = r2
            goto L_0x004e
        L_0x00ac:
            r0 = move-exception
            r10 = r1
            goto L_0x00a7
        L_0x00af:
            r0 = r2
            r10 = r1
            goto L_0x004e
        */
        throw new UnsupportedOperationException("Method not decompiled: defpackage.bt.a(android.content.Context, int, int):void");
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String b(Long l2) {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date(l2.longValue()));
    }

    public static void g() {
        if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW") && e != null) {
            e.stop(f);
            e.release();
        }
    }

    public static void h() {
        if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW") && e != null) {
            e.pause(f);
        }
    }

    public static void i() {
        if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW") && e != null) {
            e.resume(f);
        }
    }

    public int a(String str, int i2) {
        return SystemProperties.getInt(str, i2);
    }

    @SuppressLint({"SimpleDateFormat"})
    public String a(long j2) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(j2));
    }

    public String a(String str, String str2) {
        return SystemProperties.get(str, str2);
    }

    public void a(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter != null) {
            for (BluetoothDevice removeBond : bluetoothAdapter.getBondedDevices()) {
                removeBond.removeBond();
            }
        }
    }

    public void a(String str) {
        this.g = str;
    }

    public void a(List<SparseArray<String>> list, String str) {
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/zhangmyinfo");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(String.valueOf(file.getPath()) + "/" + str);
        if (file2.exists()) {
            file2.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            for (SparseArray next : list) {
                fileOutputStream.write((String.valueOf((String) next.get(178)) + "num").getBytes("utf-8"));
                fileOutputStream.write(((String) next.get(180)).getBytes("utf-8"));
                fileOutputStream.write("\r\n".getBytes());
            }
            fileOutputStream.flush();
            fileOutputStream.getFD().sync();
            fileOutputStream.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void a(byte[] bArr) {
        try {
            BluetoothAdapter adapter = ((BluetoothManager) App.getApp().getSystemService("bluetooth")).getAdapter();
            a(adapter);
            String str = FinalChip.BSP_PLATFORM_Null;
            String choiceAddr = IpcObj.getChoiceAddr();
            if (!choiceAddr.contains(":")) {
                for (int i2 = 0; i2 < choiceAddr.length() - 2; i2 += 2) {
                    str = String.valueOf(str) + choiceAddr.substring(i2, i2 + 2);
                    if (i2 < choiceAddr.length() - 2) {
                        str = String.valueOf(str) + ":";
                    }
                }
            } else {
                str = choiceAddr;
            }
            BluetoothDevice remoteDevice = adapter.getRemoteDevice(str);
            if (bArr != null) {
                remoteDevice.setPin(bArr);
            }
            remoteDevice.createBond();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public boolean a(int i2) {
        boolean z = false;
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - l >= ((long) i2)) {
            z = true;
        }
        l = currentTimeMillis;
        return z;
    }

    public void b(String str) {
        this.h = str;
    }

    public void b(List<SparseArray<String>> list, String str) {
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/favmyinfo");
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!TextUtils.isEmpty(str)) {
            String str2 = String.valueOf(file.getPath()) + "/" + str;
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(str2));
                for (SparseArray next : list) {
                    fileOutputStream.write((String.valueOf((String) next.get(178)) + "first" + ((String) next.get(180)) + "second" + ((String) next.get(179)) + "third" + ((String) next.get(183))).getBytes("utf-8"));
                    fileOutputStream.write("\r\n".getBytes());
                }
                fileOutputStream.flush();
                fileOutputStream.getFD().sync();
                fileOutputStream.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            b(str2, String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/favmyinfo/info");
        }
    }

    public boolean b() {
        String[] strArr = this.i;
        int length = strArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            this.h = String.valueOf(strArr[i2]) + this.g;
            if (new File(this.h).exists()) {
                return true;
            }
        }
        return false;
    }

    public boolean b(String str, String str2) {
        try {
            File file = new File(str);
            if (!file.exists() || !file.isFile() || !file.canRead()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (-1 == read) {
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return true;
                }
                fileOutputStream.write(bArr, 0, read);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public String c() {
        return this.h;
    }

    public void c(String str) {
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/zhangmyinfo");
        if (file.exists()) {
            File file2 = new File(String.valueOf(file.getPath()) + "/" + str);
            if (file2.exists()) {
                file2.delete();
            }
        }
    }

    public List<SparseArray<String>> d(String str) {
        ArrayList arrayList = null;
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/zhangmyinfo");
        if (file.exists()) {
            File file2 = new File(String.valueOf(file.getPath()) + "/" + str);
            if (file2.exists()) {
                arrayList = new ArrayList();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file2);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        } else if (readLine.contains(this.j)) {
                            String substring = readLine.substring(0, readLine.lastIndexOf(this.j));
                            arrayList.add(App.getNewMapContact(substring, readLine.replace(String.valueOf(substring) + this.j, FinalChip.BSP_PLATFORM_Null)));
                        }
                    }
                    fileInputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return arrayList;
    }

    public boolean d() {
        return false;
    }

    public List<SparseArray<String>> e(String str) {
        ArrayList arrayList = null;
        File file = new File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/favmyinfo");
        if (file.exists()) {
            String str2 = String.valueOf(file.getPath()) + "/" + str;
            File file2 = new File(str2);
            if (file2.exists()) {
                arrayList = new ArrayList();
                try {
                    FileInputStream fileInputStream = new FileInputStream(file2);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        int lastIndexOf = readLine.lastIndexOf("first");
                        int lastIndexOf2 = readLine.lastIndexOf("second");
                        int lastIndexOf3 = readLine.lastIndexOf("third");
                        SparseArray sparseArray = new SparseArray();
                        if (lastIndexOf > 0) {
                            sparseArray.put(178, readLine.substring(0, lastIndexOf));
                        }
                        if (lastIndexOf2 > 0) {
                            sparseArray.put(180, readLine.substring(lastIndexOf + 5, lastIndexOf2));
                        }
                        if (lastIndexOf3 > 0) {
                            sparseArray.put(179, readLine.substring(lastIndexOf2 + 6, lastIndexOf3));
                        }
                        sparseArray.put(183, readLine.substring(lastIndexOf3 + 5));
                        arrayList.add(sparseArray);
                    }
                    fileInputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                b(str2, String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/favmyinfo/info");
            }
        }
        return arrayList;
    }

    public boolean e() {
        return true;
    }

    @SuppressLint({"SimpleDateFormat"})
    public long f(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        try {
            Calendar instance = Calendar.getInstance();
            instance.setTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(str.replaceAll("/", FinalChip.BSP_PLATFORM_Null).replace(" ", FinalChip.BSP_PLATFORM_Null).replace(":", FinalChip.BSP_PLATFORM_Null)));
            return instance.getTimeInMillis();
        } catch (ParseException e2) {
            e2.printStackTrace();
            return currentTimeMillis;
        }
    }

    public int[] f() {
        if (this.b == null) {
            int[] iArr = new int[2];
            WindowManager windowManager = (WindowManager) App.getApp().getSystemService("window");
            DisplayMetrics displayMetrics = new DisplayMetrics();
            if (windowManager != null) {
                windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                iArr[0] = displayMetrics.widthPixels;
                iArr[1] = displayMetrics.heightPixels;
            }
            this.b = iArr;
        }
        return this.b;
    }

    public String g(String str) {
        return (!App.needSplice || TextUtils.isEmpty(str) || str.startsWith("+")) ? str : "+" + str;
    }
}
