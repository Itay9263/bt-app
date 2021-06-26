package com.syu.util;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.syu.data.FinalChip;

public class LogScreen {
    public static final String LOG_DIR_PATH = "/sdcard/log";
    static LogScreen instance = null;
    public static int mColor = -2147418113;
    public static Context mContext = null;
    public static int mGravity = 85;
    public static int mTypeLayoutParams = 2003;
    public static final String strThreadCheck = "LogPreview_Work";
    Runnable checkLogPreview = new Runnable() {
        public void run() {
            HandlerUI.getInstance().post(new Runnable() {
                public void run() {
                    if (LogScreen.this.rootView.getParent() == null) {
                        LogScreen.this.wm.addView(LogScreen.this.rootView, LogScreen.this.mParams);
                    }
                }
            });
            Handler handler = ThreadMap.mHashMapHandler.get(LogScreen.strThreadCheck);
            if (handler != null) {
                handler.removeCallbacks(this);
                handler.postDelayed(this, 25000);
            }
        }
    };
    TextView logPreview;
    StringBuffer logs = new StringBuffer();
    WindowManager.LayoutParams mParams;
    FrameLayout rootView;
    WindowManager wm;

    LogScreen(Context context) {
        mContext = context.getApplicationContext();
        this.wm = (WindowManager) mContext.getSystemService("window");
        init();
        ThreadMap.startThread(strThreadCheck, this.checkLogPreview, true, 1);
    }

    public static void show(String str) {
        if (mContext != null) {
            if (instance == null) {
                instance = new LogScreen(mContext);
            }
            if (instance.logs.length() >= 20480) {
                instance.logs.replace(0, 4096, FinalChip.BSP_PLATFORM_Null);
            }
            instance.logs.append(String.valueOf(mContext.getPackageName()) + "-" + SystemClock.uptimeMillis() + ":" + str + "\n");
            HandlerUI.getInstance().post(new Runnable() {
                public void run() {
                    LogScreen.instance.logPreview.setText(LogScreen.instance.logs.toString());
                }
            });
        }
    }

    public void init() {
        if (Build.VERSION.SDK_INT >= 26 && mTypeLayoutParams == 2003) {
            mTypeLayoutParams = 2038;
        }
        this.mParams = new WindowManager.LayoutParams(-1, -1, 0, 0, mTypeLayoutParams, 56, 1);
        this.rootView = new FrameLayout(mContext);
        this.logPreview = new TextView(mContext);
        this.logPreview.setGravity(mGravity);
        this.logPreview.setTextColor(mColor);
        this.logPreview.setTextSize(16.0f);
        this.logPreview.setPadding(0, 0, 60, 0);
        this.rootView.addView(this.logPreview, new FrameLayout.LayoutParams(-1, -2));
    }
}
