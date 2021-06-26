package com.syu.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemProperties;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.syu.camera.MyCamera;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JListViewEx;
import com.syu.ctrl.JPage;
import com.syu.data.FinalChip;
import com.syu.data.FinalType;
import com.syu.ipcself.module.main.Main;
import com.syu.page.IPageNotify;
import com.syu.share.ShareHandler;
import com.syu.util.CrashLog;
import com.syu.util.FuncUtils;
import com.syu.util.InterfaceApp;
import com.syu.util.LogScreen;
import com.syu.util.Markup;
import com.syu.util.MyFolder;
import com.syu.util.ThreadMap;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MyApplication extends Application implements InterfaceApp {
    public static boolean bEnableFocusChange = false;
    public static int hVideoFrameHeight = 0;
    public static int hVideoFrameHeight_PAL = 0;
    public static ActivityManager mActivityManager;
    public static ArrayList<MyActivity> mActs = new ArrayList<>();
    public static AssetManager mAssetManager;
    public static AudioManager mAudioManager;
    public static ContentResolver mContentResolver;
    public static HashMap<String, Integer> mCtrlType = new HashMap<>();
    public static DisplayMetrics mDispayMetrics = new DisplayMetrics();
    public static Handler mHandlerDecodeBmp;
    public static int mIdChip = 3;
    public static int mIdCustomer = 2;
    public static int mIdPlatForm = 4;
    public static boolean mIsOldService = false;
    public static PackageManager mPkgManager;
    public static String mPkgName;
    public static HashMap<String, Integer> mResMap = new HashMap<>();
    public static Resources mResources;
    public static float mScale = 1.0f;
    public static float mScreenMax = 1024.0f;
    public static float mScreenXSet = 1024.0f;
    public static float mScreenYSet = 600.0f;
    public static int mStatusHeight;
    public static HandlerThread mThread_DecodeBmp = new HandlerThread("Bitmap Decoder");
    public static WindowManager mWindowManager;
    public static MyApplication myApp;
    public static MyUi uiApp;
    public static int wVideoFrameWidth = 0;
    public static int wVideoFrameWidth_PAL = 0;

    public MyApplication() {
        myApp = this;
        LogScreen.mContext = myApp;
    }

    public static void DeepCopy_FolderList(List<MyFolder> list, List<MyFolder> list2) {
        if (list != null && list2 != null) {
            list2.clear();
            for (MyFolder next : list) {
                MyFolder myFolder = new MyFolder();
                myFolder.map = next.map;
                myFolder.array.addAll(next.array);
                list2.add(myFolder);
            }
        }
    }

    public static <T> void PutHashMap(HashMap<String, Integer> hashMap, Class<T> cls) {
        if (hashMap != null) {
            for (Field field : cls.getDeclaredFields()) {
                try {
                    hashMap.put(field.getName(), Integer.valueOf(field.getInt(cls)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void add(JGridView jGridView, SparseArray<String> sparseArray) {
        if (jGridView != null) {
            add2GridView(jGridView, sparseArray);
            jGridView.notifyDataSetChanged();
        }
    }

    public static void add2GridView(JGridView jGridView, SparseArray<String> sparseArray) {
        boolean z = false;
        if (jGridView != null) {
            int count = jGridView.getCount();
            if (count > 0 && count <= jGridView.getMaxRowDisp()) {
                Iterator<SparseArray<String>> it = jGridView.getList().iterator();
                int i = 0;
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    } else if (it.next().size() == 0) {
                        jGridView.getList().remove(i);
                        jGridView.getList().add(i, sparseArray);
                        z = true;
                        break;
                    } else {
                        i++;
                    }
                }
            }
            if (!z) {
                jGridView.getList().add(sparseArray);
            }
        }
    }

    public static void addAll(JGridView jGridView, List<SparseArray<String>> list) {
        if (jGridView != null) {
            if (list != null) {
                for (SparseArray<String> add2GridView : list) {
                    add2GridView(jGridView, add2GridView);
                }
            }
            for (int count = jGridView.getCount(); count < jGridView.getMaxRowDisp(); count++) {
                jGridView.getList().add(new SparseArray());
            }
            jGridView.notifyDataSetChanged();
        }
    }

    public static void clear(JGridView jGridView) {
        if (jGridView != null && jGridView.getList() != null) {
            jGridView.getList().clear();
            for (int count = jGridView.getCount(); count < jGridView.getMaxRowDisp(); count++) {
                jGridView.getList().add(new SparseArray());
            }
            jGridView.notifyDataSetChanged();
        }
    }

    public static void getMetrics(Display display) {
        if (display != null) {
            display.getMetrics(mDispayMetrics);
            Point point = new Point();
            try {
                Display.class.getMethod("getRealSize", new Class[]{Point.class}).invoke(display, new Object[]{point});
                mDispayMetrics.widthPixels = point.x;
                mDispayMetrics.heightPixels = point.y;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Drawable myDrawable(int i) {
        if (mResources != null) {
            try {
                return mResources.getDrawable(i);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static int myParseColor(String str) {
        try {
            return Color.parseColor(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int myParseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long myParseLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static long myParseTime(String str) {
        try {
            return Date.parse(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String myString(int i) {
        if (mResources != null) {
            try {
                return mResources.getString(i);
            } catch (Exception e) {
            }
        }
        return FinalChip.BSP_PLATFORM_Null;
    }

    public static String[] myStringArray(int i) {
        if (mResources != null) {
            try {
                return mResources.getStringArray(i);
            } catch (Exception e) {
            }
        }
        return null;
    }

    public static void remove(JGridView jGridView, int i) {
        if (jGridView != null && jGridView.getList() != null) {
            jGridView.getList().remove(i);
            for (int count = jGridView.getCount(); count < jGridView.getMaxRowDisp(); count++) {
                jGridView.getList().add(new SparseArray());
            }
            jGridView.notifyDataSetChanged();
        }
    }

    public static void removeAll(JGridView jGridView, List<SparseArray<String>> list) {
        if (jGridView != null) {
            jGridView.getList().removeAll(list);
            for (int count = jGridView.getCount(); count < jGridView.getMaxRowDisp(); count++) {
                jGridView.getList().add(new SparseArray());
            }
            jGridView.notifyDataSetChanged();
        }
    }

    public static void resetList(JGridView jGridView, List<SparseArray<String>> list) {
        if (jGridView != null) {
            jGridView.getList().clear();
            if (list != null) {
                for (SparseArray<String> add2GridView : list) {
                    add2GridView(jGridView, add2GridView);
                }
            }
            for (int count = jGridView.getCount(); count < jGridView.getMaxRowDisp(); count++) {
                jGridView.getList().add(new SparseArray());
            }
            jGridView.notifyDataSetChanged();
        }
    }

    public static void resetList(JListViewEx jListViewEx, List<MyFolder> list) {
        if (jListViewEx != null) {
            jListViewEx.getList().clear();
            if (list != null) {
                jListViewEx.getList().addAll(list);
            }
            jListViewEx.notifyDataSetChanged();
        }
    }

    public static void resetList_Folder(JGridView jGridView, List<MyFolder> list) {
        if (jGridView != null) {
            jGridView.getList().clear();
            jGridView.getListFolder().clear();
            if (list != null) {
                for (MyFolder next : list) {
                    jGridView.getListFolder().add(next);
                    jGridView.getList().add(next.map);
                }
            }
            jGridView.notifyDataSetChanged();
        }
    }

    public static void startThread(String str, Runnable runnable, boolean z, int i) {
        ThreadMap.startThread(str, runnable, z, i);
    }

    public static void startThread(String str, Runnable runnable, boolean z, int i, long j) {
        ThreadMap.startThread(str, runnable, z, i, j);
    }

    public void InitCtrlId() {
    }

    public void InitCtrlType() {
        PutHashMap(mCtrlType, FinalType.class);
    }

    public void InitMapPage() {
    }

    public void InitPage(JPage jPage) {
    }

    public void InitScreen() {
        String GetAttr;
        int myParseInt;
        boolean z = true;
        getScreenWidthHeight();
        if (!TextUtils.isEmpty(mPkgName)) {
            try {
                int identifier = mResources.getIdentifier("conf_screen", "raw", mPkgName);
                if (identifier > 0) {
                    String readStrFromStream = FuncUtils.readStrFromStream(mResources.openRawResource(identifier));
                    Markup markup = new Markup();
                    markup.ReadXML(readStrFromStream);
                    if (markup.IntoItem()) {
                        do {
                            String GetAttr2 = markup.GetAttr("name");
                            if (GetAttr2 != null) {
                                if (GetAttr2.equals("screen_max")) {
                                    String GetAttr3 = markup.GetAttr("value");
                                    if (GetAttr3 != null && (myParseInt = myParseInt(GetAttr3)) > 0) {
                                        mScreenMax = (float) myParseInt;
                                    }
                                } else if (GetAttr2.equals("screen_set") && (GetAttr = markup.GetAttr("value")) != null) {
                                    String[] split = GetAttr.split(",");
                                    if (split.length >= 2) {
                                        mScreenXSet = (float) myParseInt(split[0]);
                                        mScreenYSet = (float) myParseInt(split[1]);
                                    }
                                    z = false;
                                }
                            }
                        } while (markup.NextItem());
                        if (z) {
                            if (mDispayMetrics.widthPixels > mDispayMetrics.heightPixels) {
                                mScale = ((float) mDispayMetrics.widthPixels) / mScreenMax;
                            } else {
                                mScale = ((float) mDispayMetrics.heightPixels) / mScreenMax;
                            }
                        }
                        markup.ExitItem();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void cmdObdFlagStop(int i) {
    }

    public void createApplication() {
        if (Main.mConf_PlatForm == 5) {
            MyCamera.QUALITY_CVBSP = getIntField("QUALITY_CVBSP", CamcorderProfile.class);
            MyCamera.QUALITY_CVBSN = getIntField("QUALITY_CVBSN", CamcorderProfile.class);
        }
    }

    public View createUiItemOtherWay(JPage jPage, MyUiItem myUiItem) {
        return null;
    }

    public Animation getAnimationFromPath(String str) {
        int identifier = mResources.getIdentifier(str, "anim", mPkgName);
        if (identifier > 0) {
            return AnimationUtils.loadAnimation(this, identifier);
        }
        return null;
    }

    public int getCameraId() {
        return -1;
    }

    public int getIdAnim(String str) {
        return mResources.getIdentifier(str, "anim", mPkgName);
    }

    public int getIdColor(String str) {
        return mResources.getIdentifier(str, "color", mPkgName);
    }

    public int getIdDrawable(String str) {
        return mResources.getIdentifier(str, "drawable", mPkgName);
    }

    public int getIdStyle(String str) {
        return mResources.getIdentifier(str, "style", mPkgName);
    }

    public <T> int getIntField(String str, Class<T> cls) {
        Field[] declaredFields = cls.getDeclaredFields();
        int length = declaredFields.length;
        int i = 0;
        while (i < length) {
            Field field = declaredFields[i];
            try {
                if (field.getName().equals(str)) {
                    return Integer.valueOf(field.getInt(cls)).intValue();
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public int getOrientation() {
        return getResources().getConfiguration().orientation;
    }

    public IPageNotify getPageNotify(int i) {
        JPage jPage;
        if (uiApp == null || (jPage = uiApp.mPages.get(i)) == null) {
            return null;
        }
        return jPage.getNotify();
    }

    public String getPlat() {
        String str = SystemProperties.get("ro.fyt.platform", FinalChip.BSP_PLATFORM_Null);
        return TextUtils.isEmpty(str) ? SystemProperties.get("sys.fyt.platform", FinalChip.BSP_PLATFORM_Null) : str;
    }

    public void getScreenWidthHeight() {
        getMetrics(((WindowManager) getSystemService("window")).getDefaultDisplay());
    }

    public int getStatusBarHeight() {
        try {
            Class<?> cls = Class.forName("com.android.internal.R$dimen");
            return mResources.getDimensionPixelSize(myParseInt(cls.getField("status_bar_height").get(cls.newInstance()).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 60;
        }
    }

    public String getString(String str) {
        int identifier = mResources.getIdentifier(str, "string", mPkgName);
        return identifier == 0 ? str : myString(identifier);
    }

    public String[] getStringArray(String str) {
        int identifier = mResources.getIdentifier(str, "array", mPkgName);
        if (identifier > 0) {
            return myStringArray(identifier);
        }
        return null;
    }

    public MyActivity getTopActivity() {
        Iterator<MyActivity> it = mActs.iterator();
        while (it.hasNext()) {
            MyActivity next = it.next();
            if (next.bTop) {
                return next;
            }
        }
        return null;
    }

    public void goHome() {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setFlags(268435456);
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isAppTop() {
        return getTopActivity() != null;
    }

    public boolean isNetworkAvailable() {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService("connectivity");
        if (connectivityManager == null || (allNetworkInfo = connectivityManager.getAllNetworkInfo()) == null || allNetworkInfo.length <= 0) {
            return false;
        }
        for (NetworkInfo state : allNetworkInfo) {
            if (state.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public void notify_startCamera() {
    }

    public void notify_stopCamera() {
    }

    public void onConnected_Main() {
    }

    public void onConnected_Sound() {
    }

    public void onCreate() {
        mPkgName = getPackageName();
        mPkgManager = getPackageManager();
        CrashLog.getInstance(this);
        uiApp = new MyUi((MyActivity) null);
        mResources = getResources();
        mAssetManager = getAssets();
        mActivityManager = (ActivityManager) getSystemService("activity");
        mWindowManager = (WindowManager) getSystemService("window");
        mAudioManager = (AudioManager) getSystemService("audio");
        mContentResolver = getContentResolver();
        try {
            mIdChip = ShareHandler.getInt(mContentResolver, 13, 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mIdPlatForm = ShareHandler.getInt(mContentResolver, 10, 4);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            mIdCustomer = ShareHandler.getInt(mContentResolver, 9, 2);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            PutHashMap(mResMap, Class.forName(String.valueOf(mPkgName) + ".R$drawable"));
        } catch (Exception e4) {
        }
        InitScreen();
        InitCtrlType();
        InitCtrlId();
        InitMapPage();
        preCreateApplication();
        if (!mIsOldService) {
            Main.DATA[7] = 1;
            Main.sAppIdRequest = 20;
        }
        super.onCreate();
        createApplication();
    }

    public void postRunnable_DecodeBmp(Runnable runnable) {
        if (runnable != null && mHandlerDecodeBmp != null) {
            mHandlerDecodeBmp.post(runnable);
        }
    }

    public void preCreateApplication() {
        mStatusHeight = getStatusBarHeight();
        mThread_DecodeBmp.start();
        mHandlerDecodeBmp = new Handler(mThread_DecodeBmp.getLooper());
    }

    public void requestAppIdRight() {
    }

    public void setCameraCallBack() {
    }

    public void setPreviewFormat() {
    }

    public View setProperty(View view) {
        return null;
    }

    public void startAct(Class<?> cls) {
        try {
            Intent intent = new Intent(myApp, cls);
            intent.addFlags(268435456);
            intent.addFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_END);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAct(String str) {
        try {
            Intent intent = new Intent();
            intent.setAction(str);
            intent.addFlags(268435456);
            intent.addFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_END);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAct(String str, String str2) {
        try {
            Intent intent = new Intent();
            intent.setClassName(str, str2);
            intent.addFlags(268435456);
            intent.addFlags(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_END);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startServiceSafely(Intent intent) {
        try {
            startService(intent);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(intent);
            }
        }
    }

    public String updateOsdInfo_Dvd(int[] iArr) {
        return null;
    }
}
