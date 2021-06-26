package com.syu.app;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Looper;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.syu.app.data.FinalAppCtrl;
import com.syu.app.ipc.IpcObj;
import com.syu.app.ipc.Ipc_New;
import com.syu.broadcast.Receiver;
import com.syu.broadcast.a;
import com.syu.broadcast.b;
import com.syu.bt.Bt_Info;
import com.syu.bt.act.InterfaceBt;
import com.syu.bt.page.pop.Page_Pop3rdPhone;
import com.syu.bt.page.pop.Page_PopBt;
import com.syu.bt.page.pop.Page_PopBtAvMicSet;
import com.syu.bt.page.pop.Page_PopBtRingSet;
import com.syu.bt.page.pop.Page_PopBt_Book;
import com.syu.bt.page.pop.Page_PopBt_BookItem;
import com.syu.bt.page.pop.Page_PopBt_HistoryItem;
import com.syu.bt.page.pop.Page_PopPhoneMicSet;
import com.syu.bt.page.pop.Page_PopPhoneVoice;
import com.syu.bt.page.pop.Page_PopSms;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.data.FinalAppMainServer;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.util.FuncUtils;
import com.syu.util.Markup;
import com.syu.util.MySharePreference;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class App extends MyApplication {
    public static final String AMAPASSIST_PKG_NAME = "com.iflytek.amap.assist";
    public static int[] SB = new int[2];
    public static final String StrThreadDBContact = "db-operation-contact";
    public static final String StrThreadDBHistory = "db-operation-history";
    public static final String StrThreadDownContact = "Thread-DownContact";
    public static final String StrThreadFactoryTest = "factoryTest";
    public static final String StrThreadGetNameByNumber = "Thread-GetNameByNumber";
    public static final String StrThreadGetNameByNumberHistory = "Thread-GetNameByNumberHistory";
    public static final String StrThreadGetNameByNumberPopBtBook = "Thread-GetNameByNumberPopBtBook";
    public static final String StrThreadSimulateKey = "ThreadSimulateKey";
    public static boolean bAutoDownPhoneBook = false;
    public static boolean bAutoSavePhoneBook = false;
    public static boolean bBackCarFlag = false;
    public static boolean bBtAcc_On = false;
    public static boolean bBtAvPlayState = false;
    public static boolean bBtInitFlag = false;
    public static boolean bBtPowerOnOff = true;
    public static boolean bCallinHideKeyTextFlag = false;
    public static boolean bCallinPageFlag = false;
    public static boolean bChangeAppIdWhenTalking = false;
    public static boolean bCutBtByIpPhone = false;
    public static boolean bDelMenuFromBtAv = false;
    public static boolean bDoClearWork = false;
    public static boolean bDoSaveWork = false;
    public static boolean bDownloading = false;
    public static boolean bDownloadingRecord = false;
    public static boolean bEnableSpec = false;
    public static boolean bFactoryTest = false;
    public static boolean bFavorite = false;
    public static boolean bFirstBtAvPage = false;
    public static boolean bFirstPairPage = false;
    public static String bForegin;
    public static boolean bHFP = false;
    public static boolean bHasDownBook = false;
    public static boolean bHaveScanDB = false;
    public static boolean bHidePopBt = false;
    public static boolean bHidePopBt2 = false;
    public static boolean bHistoryLogAllFlag = false;
    public static boolean bIsAppAmapAssistInstall = false;
    public static boolean bIsFavList = false;
    public static boolean bIsLauncher_2Ico = false;
    public static boolean bPageAnimate = true;
    public static boolean bPageInitForDownLoadContact = false;
    public static boolean bPhoneMIC = false;
    public static boolean bPop3rdPhone = false;
    public static boolean bPop3rdPhone_YF = false;
    public static boolean bPopObdFlag = false;
    public static boolean bResumeByDial = false;
    public static boolean bSelfLink = false;
    public static boolean bShowBtInNaviFloatBtn = false;
    public static boolean bShowFloatBtn = false;
    public static boolean bShowKeyBoradWhenPopBt = false;
    public static boolean bShowKeyFlag = false;
    public static boolean bShowLoading = false;
    public static boolean bShowPairedBt = false;
    public static boolean bShowTime = false;
    public static int bSoundAMP = 0;
    public static int bSoundLoud = 0;
    public static int bSoundMode = 0;
    public static int bSoundVol = 0;
    public static boolean bSpeValue = false;
    public static boolean bSyncHistory = false;
    public static int color = 0;
    public static boolean hideBtnWhenDisconnect = false;
    public static int iBtAvMicSet = 0;
    public static int iBtAv_CurTime = 0;
    public static int iBtAv_TotalTime = 0;
    public static int iDownRecordState = 255;
    public static int iDownloadCnt = 0;
    public static int iOverTime = 0;
    public static int iPhoneMicSet = 0;
    public static int iRingLevel = 8;
    public static int iTestTime = 0;
    public static int iTimerCnt = 5;
    public static int idPageBeforeAv = 3;
    public static Bt_Info mBtInfo;
    public static int mBtType = -1;
    public static boolean mBtUpdating = false;
    public static int mColorType = -1;
    public static int mCustomUiId = 0;
    public static int mDelType = 0;
    public static String mDeletePairAddr;
    public static boolean mEnableHalfScreen = false;
    public static ArrayList<InterfaceBt> mInterfaceBt = new ArrayList<>();
    public static int mIsTranslucent = 0;
    public static String mLocale;
    public static int mPhoneBatterty = 0;
    public static int mPhoneCoding = 0;
    public static Runnable mRunnableResetList = new Runnable() {
        public void run() {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().resetList();
            }
        }
    };
    public static int mSelectContact = -1;
    public static String mStrCustomer = "SYU";
    public static final String[] mStrStates = {"bt_disconnected", "bt_linking", "bt_connected", "bt_dialing", "bt_ring", "bt_talking", "bt_paring", "bt_loading"};
    public static TelephonyManager mTelephonyManager;
    public static boolean needSplice = false;
    public static int sBackFlag;
    public static int sCallsType = 1;
    public static boolean sContactsSaveFlag = false;
    public static String sDialInput = FinalChip.BSP_PLATFORM_Null;
    public static int sPage = 3;
    public static int sPageBak;
    public static String select_pair_addr = FinalChip.BSP_PLATFORM_Null;
    public static String strDefaultName = "BC8-Android";
    public static String strDefaultPin = "0000";
    public static String strNumberBak = FinalChip.BSP_PLATFORM_Null;
    public static int testid = 0;
    public int[] ToastUtil_XY = new int[2];
    public int[] UIUtil_XY = new int[2];
    public boolean bFloatViewShow = false;
    public boolean bPopBtAvMicSet = false;
    public boolean bPopBtBook = false;
    public boolean bPopBtRingSet = false;
    public boolean bPopBtShow = false;
    public boolean bPopBtShowBak_Num = false;
    public boolean bPopBtShow_Num = false;
    public int bPopPhoneFirstValue = 1;
    public boolean bPopPhoneMicSet = false;
    public boolean bPopPhoneSms = false;
    public boolean bPopPhoneVoice = false;
    public Method fytGetState;
    public Method fytSetState;
    /* access modifiers changed from: private */
    public WindowManager.LayoutParams lpFloat = null;
    private a mBatteryReceiver;
    public IpcObj mIpcObj = new IpcObj();
    private Receiver mReceiver = new Receiver();
    private b mReceiver_TimeUpdate = new b();
    public Runnable mRunnableKillSelf = new Runnable() {
        public void run() {
            Process.killProcess(Process.myPid());
        }
    };
    public Runnable mRunnable_HideFloatBtn = new Runnable() {
        public void run() {
            if (App.this.bFloatViewShow && App.this.mTxtFloat != null) {
                App.this.bFloatViewShow = false;
                if (App.bShowBtInNaviFloatBtn) {
                    App.this.mIpcObj.removeNotify_PopBt();
                }
                App.mWindowManager.removeView(App.this.mTxtFloat);
            }
        }
    };
    public Runnable mRunnable_PopBt = new Runnable() {
        public void run() {
            Page_PopBt page_PopBt;
            if (App.bShowFloatBtn && !App.this.mIpcObj.isCalling()) {
                Bt.sLastPhoneState = Bt.DATA[9];
            }
            if (App.this.bPopBtShow) {
                JPage jPage = App.uiApp.mPages.get(1);
                if (jPage == null) {
                    jPage = App.uiApp.mPages.get(3);
                }
                if (jPage != null && (page_PopBt = (Page_PopBt) jPage.getNotify()) != null) {
                    page_PopBt.showKeyBorad(App.bShowKeyBoradWhenPopBt);
                    return;
                }
                return;
            }
            JPage loadPage = App.uiApp.loadPage(true, 1);
            JPage loadPage2 = loadPage == null ? App.uiApp.loadPage(true, 3) : loadPage;
            if (loadPage2 != null) {
                loadPage2.resume();
                MyUiItem myUiItem = (MyUiItem) loadPage2.getTag();
                int[] padding = myUiItem.getPadding();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = 40;
                layoutParams.type = 2002;
                layoutParams.format = 1;
                layoutParams.width = myUiItem.getWidth();
                layoutParams.height = myUiItem.getHeight();
                if (padding == null || padding.length < 2) {
                    layoutParams.x = 0;
                    layoutParams.y = 0;
                } else {
                    layoutParams.x = padding[0];
                    layoutParams.y = padding[1];
                }
                layoutParams.gravity = 83;
                App.this.bPopBtShow = true;
                App.this.bPopBtShowBak_Num = false;
                App.this.windowManager.addView(loadPage2, layoutParams);
                App.this.mIpcObj.addNotify_PopBt();
                Page_PopBt page_PopBt2 = (Page_PopBt) loadPage2.getNotify();
                if (page_PopBt2 != null) {
                    page_PopBt2.showKeyBorad(App.bShowKeyBoradWhenPopBt);
                }
            }
        }
    };
    public Runnable mRunnable_PopBtNum = new Runnable() {
        public void run() {
            if (!App.this.bPopBtShow_Num) {
                JPage loadPage = App.uiApp.loadPage(true, 2);
                JPage loadPage2 = App.uiApp.loadPage(true, 3);
                if (loadPage != null && loadPage2 != null) {
                    loadPage.resume();
                    MyUiItem myUiItem = (MyUiItem) loadPage.getTag();
                    MyUiItem myUiItem2 = (MyUiItem) loadPage2.getTag();
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.flags = 40;
                    layoutParams.type = 2002;
                    layoutParams.format = 1;
                    layoutParams.width = myUiItem.getWidth();
                    layoutParams.height = myUiItem.getHeight();
                    int i = App.mStrCustomer.equalsIgnoreCase("TZY_NEW") ? 15 : 0;
                    int[] padding = myUiItem.getPadding();
                    if (padding == null || padding.length < 2) {
                        layoutParams.x = 0;
                        layoutParams.y = myUiItem2.getHeight() - i;
                    } else {
                        layoutParams.x = padding[0];
                        layoutParams.y = padding[1];
                    }
                    layoutParams.gravity = 83;
                    App.this.bPopBtShow_Num = true;
                    App.this.bPopBtShowBak_Num = true;
                    App.this.windowManager.addView(loadPage, layoutParams);
                }
            }
        }
    };
    public Runnable mRunnable_ShowFloatButton = new Runnable() {
        public void run() {
            if (!App.this.isAppTop() && !App.this.bFloatViewShow && App.mWindowManager != null) {
                if (App.this.mTxtFloat == null) {
                    App.this.mTxtFloat = new TextView(App.getApp());
                    if (App.bShowBtInNaviFloatBtn) {
                        App.this.mTxtFloat.setBackgroundResource(App.getApp().getIdDrawable("bt_icon4"));
                    } else {
                        App.this.mTxtFloat.setBackgroundResource(App.getApp().getIdDrawable("bt_icon"));
                    }
                    App.this.lpFloat = new WindowManager.LayoutParams();
                    App.this.lpFloat.type = 2002;
                    App.this.lpFloat.flags = 40;
                    App.this.lpFloat.format = 1;
                    App.this.lpFloat.alpha = 0.8f;
                    if (App.bShowBtInNaviFloatBtn) {
                        App.this.lpFloat.width = App.this.dp2px(60.0f);
                        App.this.lpFloat.height = App.this.dp2px(60.0f);
                        App.this.lpFloat.x = 10;
                        App.this.lpFloat.y = 10;
                        App.this.lpFloat.gravity = 85;
                    } else {
                        App.this.lpFloat.width = App.this.dp2px(80.0f);
                        App.this.lpFloat.height = App.this.dp2px(80.0f);
                        App.this.lpFloat.x = 0;
                        App.this.lpFloat.y = 0;
                        App.this.lpFloat.gravity = 83;
                    }
                }
                if (App.this.mTxtFloat != null) {
                    App.this.mTxtFloat.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (App.this.bPopBtShow) {
                                App.this.popBt(false, false);
                            } else if (bv.d()) {
                                App.this.popBt(true, false);
                            } else {
                                App.this.popBt(true, true);
                            }
                        }
                    });
                    App.this.mTxtFloat.setOnTouchListener(new View.OnTouchListener() {
                        int lastDownTime;
                        int lastX;
                        int lastY;
                        WindowManager.LayoutParams params2;

                        {
                            this.params2 = App.this.lpFloat;
                        }

                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            switch (motionEvent.getAction()) {
                                case 0:
                                    this.lastDownTime = (int) System.currentTimeMillis();
                                    this.lastX = (int) motionEvent.getRawX();
                                    this.lastY = (int) motionEvent.getRawY();
                                    return true;
                                case 1:
                                    if (((int) System.currentTimeMillis()) - this.lastDownTime >= 300) {
                                        return false;
                                    }
                                    App.this.mTxtFloat.performClick();
                                    return false;
                                case 2:
                                    float rawX = motionEvent.getRawX();
                                    float rawY = motionEvent.getRawY();
                                    WindowManager.LayoutParams layoutParams = this.params2;
                                    layoutParams.x = ((int) (rawX - ((float) this.lastX))) + layoutParams.x;
                                    this.params2.y -= (int) (rawY - ((float) this.lastY));
                                    this.lastX = (int) rawX;
                                    this.lastY = (int) rawY;
                                    App.mWindowManager.updateViewLayout(App.this.mTxtFloat, this.params2);
                                    return false;
                                default:
                                    return false;
                            }
                        }
                    });
                    App.this.bFloatViewShow = true;
                    if (App.bShowBtInNaviFloatBtn) {
                        App.this.mIpcObj.addNotify_PopBt();
                    }
                    App.mWindowManager.addView(App.this.mTxtFloat, App.this.lpFloat);
                }
            }
        }
    };
    public Runnable mRunnable_hide3rdPhone = new Runnable() {
        public void run() {
            if (App.bPop3rdPhone) {
                JPage jPage = App.uiApp.mPages.get(10);
                if (jPage != null) {
                    App.this.windowManager.removeView(jPage);
                }
                App.bPop3rdPhone = false;
                App.uiApp.mPages.remove(10);
            }
        }
    };
    public Runnable mRunnable_hideBt = new Runnable() {
        public void run() {
            if (App.this.bPopBtShow) {
                App.this.mIpcObj.removeNotify_PopBt();
                JPage jPage = App.uiApp.mPages.get(1);
                if (jPage == null) {
                    jPage = App.uiApp.mPages.get(3);
                }
                App.this.windowManager.removeView(jPage);
                App.this.bPopBtShow = false;
            }
            if (App.this.bPopBtShow_Num) {
                JPage jPage2 = App.uiApp.mPages.get(2);
                if (jPage2 != null) {
                    App.this.windowManager.removeView(jPage2);
                }
                App.this.bPopBtShow_Num = false;
            }
            App.uiApp.mPages.remove(1);
            App.uiApp.mPages.remove(3);
            App.uiApp.mPages.remove(2);
        }
    };
    public Runnable mRunnable_hideBtAvMicSet = new Runnable() {
        public void run() {
            if (App.this.bPopBtAvMicSet) {
                JPage jPage = App.uiApp.mPages.get(8);
                if (jPage != null) {
                    App.this.windowManager.removeView(jPage);
                }
                App.this.bPopBtAvMicSet = false;
                App.this.timer.cancel();
                App.this.mytask.cancel();
                App.uiApp.mPages.remove(8);
            }
        }
    };
    public Runnable mRunnable_hideBtBook = new Runnable() {
        public void run() {
            if (App.this.bPopBtBook) {
                JPage jPage = App.uiApp.mPages.get(4);
                if (jPage != null) {
                    App.this.windowManager.removeView(jPage);
                }
                App.this.bPopBtBook = false;
            }
        }
    };
    public Runnable mRunnable_hideBtNum = new Runnable() {
        public void run() {
            if (App.this.bPopBtShow_Num) {
                JPage jPage = App.uiApp.mPages.get(2);
                if (jPage != null) {
                    App.this.windowManager.removeView(jPage);
                }
                App.this.bPopBtShow_Num = false;
                App.this.bPopBtShowBak_Num = false;
                App.uiApp.mPages.remove(2);
            }
        }
    };
    public Runnable mRunnable_hideBtRingSet = new Runnable() {
        public void run() {
            if (App.this.bPopBtRingSet) {
                JPage jPage = App.uiApp.mPages.get(9);
                if (jPage != null) {
                    App.this.windowManager.removeView(jPage);
                }
                App.this.bPopBtRingSet = false;
                App.this.timer.cancel();
                App.this.mytask.cancel();
                App.uiApp.mPages.remove(9);
            }
        }
    };
    public Runnable mRunnable_hidePhoneMicSet = new Runnable() {
        public void run() {
            if (App.this.bPopPhoneMicSet) {
                JPage jPage = App.uiApp.mPages.get(7);
                if (jPage != null) {
                    App.this.windowManager.removeView(jPage);
                }
                App.this.bPopPhoneMicSet = false;
                App.this.timer.cancel();
                App.this.mytask.cancel();
                App.uiApp.mPages.remove(7);
            }
        }
    };
    public Runnable mRunnable_hidePhoneSMS = new Runnable() {
        public void run() {
            if (App.this.bPopPhoneSms) {
                JPage jPage = App.uiApp.mPages.get(12);
                if (jPage != null) {
                    App.this.windowManager.removeView(jPage);
                }
                App.this.bPopPhoneSms = false;
                App.uiApp.mPages.remove(12);
            }
        }
    };
    public Runnable mRunnable_hidePhoneVoice = new Runnable() {
        public void run() {
            if (App.this.bPopPhoneVoice) {
                JPage jPage = App.uiApp.mPages.get(11);
                if (jPage != null) {
                    App.this.windowManager.removeView(jPage);
                }
                App.this.bPopPhoneVoice = false;
                App.uiApp.mPages.remove(11);
            }
        }
    };
    public Runnable mRunnable_pop3rdPhone = new Runnable() {
        public void run() {
            JPage loadPage = App.uiApp.loadPage(true, 10);
            if ((loadPage == null || !App.bPop3rdPhone) && loadPage != null) {
                loadPage.resume();
                MyUiItem myUiItem = (MyUiItem) loadPage.getTag();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = 40;
                layoutParams.type = 2002;
                layoutParams.gravity = 16;
                layoutParams.format = 1;
                layoutParams.width = myUiItem.getWidth();
                layoutParams.height = myUiItem.getHeight();
                App.bPop3rdPhone = true;
                App.this.windowManager.addView(loadPage, layoutParams);
                App.this.mIpcObj.addNotify_PopBt();
            }
        }
    };
    public Runnable mRunnable_popBtAvMicSet = new Runnable() {
        public void run() {
            JPage loadPage = App.uiApp.loadPage(true, 8);
            if ((loadPage == null || !App.this.bPopBtAvMicSet) && loadPage != null) {
                loadPage.resume();
                MyUiItem myUiItem = (MyUiItem) loadPage.getTag();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = 40;
                layoutParams.type = 2002;
                layoutParams.gravity = 16;
                layoutParams.format = 1;
                layoutParams.width = myUiItem.getWidth();
                layoutParams.height = myUiItem.getHeight();
                if (loadPage.getId() == 8) {
                    App.this.bPopBtAvMicSet = true;
                }
                App.this.windowManager.addView(loadPage, layoutParams);
                App.iTimerCnt = 5;
                App.this.timer = new Timer();
                App.this.mytask = new MyTask();
                App.this.timer.schedule(App.this.mytask, 1000, 1000);
            }
        }
    };
    public Runnable mRunnable_popBtBook = new Runnable() {
        public void run() {
            JPage loadPage;
            if (!App.this.bPopBtBook && (loadPage = App.uiApp.loadPage(true, 4)) != null) {
                loadPage.resume();
                MyUiItem myUiItem = (MyUiItem) loadPage.getTag();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = 40;
                layoutParams.type = 2002;
                layoutParams.format = 1;
                layoutParams.width = myUiItem.getWidth();
                layoutParams.height = myUiItem.getHeight();
                int[] padding = myUiItem.getPadding();
                if (padding == null || padding.length < 2) {
                    layoutParams.x = 0;
                    layoutParams.y = 280;
                } else {
                    layoutParams.x = padding[0];
                    layoutParams.y = padding[1];
                }
                layoutParams.gravity = 83;
                App.this.bPopBtBook = true;
                App.this.windowManager.addView(loadPage, layoutParams);
            }
        }
    };
    public Runnable mRunnable_popBtRingSet = new Runnable() {
        public void run() {
            JPage loadPage = App.uiApp.loadPage(true, 9);
            if ((loadPage == null || !App.this.bPopBtRingSet) && loadPage != null) {
                loadPage.resume();
                MyUiItem myUiItem = (MyUiItem) loadPage.getTag();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = 40;
                layoutParams.type = 2002;
                layoutParams.gravity = 16;
                layoutParams.format = 1;
                layoutParams.width = myUiItem.getWidth();
                layoutParams.height = myUiItem.getHeight();
                if (loadPage.getId() == 9) {
                    App.this.bPopBtRingSet = true;
                }
                App.this.windowManager.addView(loadPage, layoutParams);
                App.iTimerCnt = 5;
                App.this.timer = new Timer();
                App.this.mytask = new MyTask();
                App.this.timer.schedule(App.this.mytask, 1000, 1000);
            }
        }
    };
    public Runnable mRunnable_popPhoneMicSet = new Runnable() {
        public void run() {
            JPage loadPage = App.uiApp.loadPage(true, 7);
            if ((loadPage == null || !App.this.bPopPhoneMicSet) && loadPage != null) {
                loadPage.resume();
                MyUiItem myUiItem = (MyUiItem) loadPage.getTag();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = 40;
                layoutParams.type = 2002;
                layoutParams.gravity = 16;
                layoutParams.format = 1;
                layoutParams.width = myUiItem.getWidth();
                layoutParams.height = myUiItem.getHeight();
                if (loadPage.getId() == 7) {
                    App.this.bPopPhoneMicSet = true;
                }
                App.this.windowManager.addView(loadPage, layoutParams);
                App.iTimerCnt = 5;
                App.this.timer = new Timer();
                App.this.mytask = new MyTask();
                App.this.timer.schedule(App.this.mytask, 1000, 1000);
            }
        }
    };
    public Runnable mRunnable_popPhoneSMS = new Runnable() {
        public void run() {
            JPage loadPage = App.uiApp.loadPage(true, 12);
            if ((loadPage == null || !App.this.bPopPhoneSms) && loadPage != null) {
                loadPage.resume();
                MyUiItem myUiItem = (MyUiItem) loadPage.getTag();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = 40;
                layoutParams.type = 2002;
                layoutParams.gravity = 16;
                layoutParams.format = 1;
                layoutParams.width = myUiItem.getWidth();
                layoutParams.height = myUiItem.getHeight();
                if (loadPage.getId() == 12) {
                    App.this.bPopPhoneSms = true;
                }
                App.this.windowManager.addView(loadPage, layoutParams);
                App.this.mIpcObj.addNotify_PopBt();
            }
        }
    };
    public Runnable mRunnable_popPhoneVoice = new Runnable() {
        public void run() {
            JPage loadPage = App.uiApp.loadPage(true, 11);
            if ((loadPage == null || !App.this.bPopPhoneVoice) && loadPage != null) {
                loadPage.resume();
                MyUiItem myUiItem = (MyUiItem) loadPage.getTag();
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.flags = 40;
                layoutParams.type = 2002;
                layoutParams.gravity = 16;
                layoutParams.format = 1;
                layoutParams.width = myUiItem.getWidth();
                layoutParams.height = myUiItem.getHeight();
                if (loadPage.getId() == 11) {
                    App.this.bPopPhoneVoice = true;
                }
                App.this.windowManager.addView(loadPage, layoutParams);
            }
        }
    };
    public Runnable mRunnable_updatePhoneName = new Runnable() {
        public void run() {
            App.this.updatePhoneName();
        }
    };
    public Runnable mRunnable_updatePhoneView = new Runnable() {
        public void run() {
            App.this.updatePhoneView();
        }
    };
    /* access modifiers changed from: private */
    public TextView mTxtFloat = null;
    MyTask mytask;
    Timer timer;
    public WindowManager windowManager;

    class MyTask extends TimerTask {
        MyTask() {
        }

        public void run() {
            if (App.iTimerCnt > 0) {
                App.iTimerCnt--;
            }
            if (App.iTimerCnt <= 1) {
                if (App.this.bPopBtRingSet) {
                    App.this.popBtRingSet(false);
                }
                if (App.this.bPopPhoneMicSet) {
                    App.this.popPhoneMicSet(false);
                }
                if (App.this.bPopBtAvMicSet) {
                    App.this.popBtAvMicSet(false);
                }
            }
        }
    }

    public static class Runnable_queryCallLog implements Runnable {
        public synchronized void run() {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(App.mBtInfo.mListContact);
            App.mBtInfo.queryHistory(arrayList);
            Main.postRunnable_Ui(true, App.mRunnableResetList);
        }
    }

    public static String ReadNameFromFile() {
        File file = new File("/mnt/sdcard/AppCfg/config.txt");
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String str = null;
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return str;
                }
                String[] split = readLine.split("#");
                if (split.length > 2 && "bt".equals(split[0])) {
                    str = split[1];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String ReadPinFromFile() {
        File file = new File("/mnt/sdcard/AppCfg/config.txt");
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String str = null;
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return str;
                }
                String[] split = readLine.split("#");
                if (split.length > 2 && "bt".equals(split[0])) {
                    str = split[2];
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static App getApp() {
        return (App) myApp;
    }

    public static String getDiyName() {
        String stringValue = MySharePreference.getStringValue("name_diy");
        if (!TextUtils.isEmpty(stringValue)) {
            return stringValue;
        }
        String ReadNameFromFile = ReadNameFromFile();
        return TextUtils.isEmpty(ReadNameFromFile) ? strDefaultName : ReadNameFromFile;
    }

    public static String getDiyPin() {
        String stringValue = MySharePreference.getStringValue("pin_diy");
        if (!TextUtils.isEmpty(stringValue)) {
            return stringValue;
        }
        String ReadPinFromFile = ReadPinFromFile();
        return TextUtils.isEmpty(ReadPinFromFile) ? strDefaultPin : ReadPinFromFile;
    }

    public static SparseArray<String> getNewMapContact(String str, String str2) {
        Object obj;
        if (str != null) {
            str = str.trim();
        }
        if (str2 != null) {
            str2 = str2.trim();
        }
        SparseArray<String> sparseArray = new SparseArray<>();
        String a = bv.h() ? b.a(str, FinalChip.BSP_PLATFORM_Null) : bx.a(str.trim(), false, true);
        sparseArray.put(178, str);
        sparseArray.put(179, a);
        if (needSplice && !str2.startsWith("+")) {
            str2 = "+" + str2;
        }
        sparseArray.put(180, str2);
        if (bv.h() || bv.e()) {
            int size = mBtInfo.mListFavContact.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    obj = FinalChip.BSP_PLATFORM_Null;
                    break;
                }
                SparseArray sparseArray2 = mBtInfo.mListFavContact.get(i);
                if (sparseArray2 != null) {
                    String str3 = (String) sparseArray2.get(180);
                    String str4 = (String) sparseArray2.get(178);
                    if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4) && str3.equals(str2) && str4.equals(str)) {
                        sparseArray2.put(184, "exist");
                        mBtInfo.mListFavContact.set(i, sparseArray2);
                        obj = "collect";
                        break;
                    }
                }
                i++;
            }
            sparseArray.put(183, obj);
        }
        return sparseArray;
    }

    public static void queryCallLog() {
        Main.postRunnable_Ui(true, new Runnable() {
            public void run() {
                App.startThread(App.StrThreadDBHistory, new Runnable_queryCallLog(), false, 10);
            }
        });
    }

    public void CustomerConfig() {
        mBtType = bt.a().a("sys.fyt.bluetooth_type", -1);
        mLocale = Locale.getDefault().getLanguage();
        if (!bv.c()) {
            if (mStrCustomer.equalsIgnoreCase("TZY_UI5")) {
                mColorType = bt.a().a("persist.fyt.selectcolor", -1);
                switch (mColorType) {
                    case 0:
                        MyUi.mStrHeadDrawable = "yellow_";
                        break;
                    case 1:
                        MyUi.mStrHeadDrawable = "purple_";
                        break;
                    default:
                        MyUi.mStrHeadDrawable = FinalChip.BSP_PLATFORM_Null;
                        break;
                }
            }
        } else {
            mColorType = bt.a().a("persist.fyt.selectcolor", -1);
            switch (mColorType) {
                case 0:
                    MyUi.mStrHeadDrawable = "pink_";
                    break;
                case 1:
                    MyUi.mStrHeadDrawable = "red_";
                    break;
                case 3:
                    MyUi.mStrHeadDrawable = "blue_";
                    break;
                case 4:
                    MyUi.mStrHeadDrawable = "purple_";
                    break;
                default:
                    MyUi.mStrHeadDrawable = FinalChip.BSP_PLATFORM_Null;
                    break;
            }
        }
        if (mStrCustomer.equalsIgnoreCase("changecolor")) {
            String[] strArr = new String[3];
            String[] split = bt.a().a("persist.launcher.color", "0,0,123").split(",");
            color = Color.rgb(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
        }
        if (bv.h()) {
            sCallsType = 0;
        }
    }

    public void DisableGainSpec() {
        bEnableSpec = false;
        this.mIpcObj.DisableGainSpec();
    }

    public void EnableGainSpec() {
        bEnableSpec = true;
        this.mIpcObj.EnableGainSpec();
    }

    public void InitBroadCast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(FinalAppMainServer.ACTION_SYSTEMUI_REMOVE);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(this.mReceiver, intentFilter);
        if (bShowTime) {
            IntentFilter intentFilter2 = new IntentFilter();
            intentFilter2.addAction("android.intent.action.TIME_SET");
            intentFilter2.addAction("android.intent.action.TIME_TICK");
            intentFilter2.addAction("android.intent.action.TIMEZONE_CHANGED");
            intentFilter2.setPriority(Integer.MAX_VALUE);
            registerReceiver(this.mReceiver_TimeUpdate, intentFilter2);
        }
        if (mStrCustomer.equalsIgnoreCase("changecolor")) {
            IntentFilter intentFilter3 = new IntentFilter();
            intentFilter3.addAction("launcher3.setcolor");
            intentFilter3.setPriority(Integer.MAX_VALUE);
            registerReceiver(this.mReceiver, intentFilter3);
        }
        if (bv.k()) {
            if (this.mBatteryReceiver == null) {
                this.mBatteryReceiver = new a();
            }
            IntentFilter intentFilter4 = new IntentFilter();
            intentFilter4.addAction("android.bluetooth.headsetclient.profile.action.AG_EVENT");
            intentFilter4.setPriority(Integer.MAX_VALUE);
            registerReceiver(this.mBatteryReceiver, intentFilter4);
        }
    }

    public void InitCtrlId() {
        PutHashMap(uiApp.mCtrlId, FinalAppCtrl.class);
    }

    public void InitMapPage() {
        uiApp.mMapPage.put(1, "bt_popbt");
        uiApp.mMapPage.put(2, "bt_popbt_num");
        uiApp.mMapPage.put(3, "bt_popbt_func");
        uiApp.mMapPage.put(4, "bt_popbt_book");
        uiApp.mMapPage.put(5, "bt_popbt_bookitem");
        uiApp.mMapPage.put(6, "bt_popbt_historyitem");
        uiApp.mMapPage.put(9, "bt_popringset");
        uiApp.mMapPage.put(7, "bt_popphonemicset");
        uiApp.mMapPage.put(8, "bt_popbtavmicset");
        uiApp.mMapPage.put(10, "bt_pop3rdphone");
        uiApp.mMapPage.put(11, "bt_popphone_voice");
        uiApp.mMapPage.put(12, "bt_popphone_sms");
    }

    public void InitPage(JPage jPage) {
        switch (jPage.getId()) {
            case 1:
            case 2:
            case 3:
                jPage.setNotify(new Page_PopBt(jPage));
                return;
            case 4:
                jPage.setNotify(new Page_PopBt_Book(jPage));
                return;
            case 5:
                jPage.setNotify(new Page_PopBt_BookItem(jPage));
                return;
            case 6:
                jPage.setNotify(new Page_PopBt_HistoryItem(jPage));
                return;
            case 7:
                jPage.setNotify(new Page_PopPhoneMicSet(jPage));
                return;
            case 8:
                jPage.setNotify(new Page_PopBtAvMicSet(jPage));
                return;
            case 9:
                jPage.setNotify(new Page_PopBtRingSet(jPage));
                return;
            case 10:
                jPage.setNotify(new Page_Pop3rdPhone(jPage));
                return;
            case 11:
                jPage.setNotify(new Page_PopPhoneVoice(jPage));
                return;
            case 12:
                jPage.setNotify(new Page_PopSms(jPage));
                return;
            default:
                return;
        }
    }

    public void ReadConf() {
        String GetAttr;
        try {
            int identifier = mResources.getIdentifier("conf_platform", "raw", mPkgName);
            if (identifier > 0) {
                String readStrFromStream = FuncUtils.readStrFromStream(mResources.openRawResource(identifier));
                Markup markup = new Markup();
                markup.ReadXML(readStrFromStream);
                if (markup.IntoItem()) {
                    do {
                        String GetAttr2 = markup.GetAttr("name");
                        if (GetAttr2 != null) {
                            if (GetAttr2.equals("platform")) {
                                String GetAttr3 = markup.GetAttr("value");
                                if (GetAttr3 != null) {
                                    Main.initPlatForm(GetAttr3);
                                }
                            } else if (GetAttr2.equals("old_service")) {
                                String GetAttr4 = markup.GetAttr("value");
                                if (GetAttr4 != null && GetAttr4.equals("1")) {
                                    mIsOldService = true;
                                }
                            } else if (GetAttr2.equals("EnableFocusChange")) {
                                String GetAttr5 = markup.GetAttr("value");
                                if (GetAttr5 != null && GetAttr5.equals("1")) {
                                    bEnableFocusChange = true;
                                }
                            } else if (GetAttr2.equals("EnableHalfScreen")) {
                                String GetAttr6 = markup.GetAttr("value");
                                if (GetAttr6 != null && GetAttr6.equals("1")) {
                                    mEnableHalfScreen = true;
                                }
                            } else if (GetAttr2.equals("name_diy")) {
                                String GetAttr7 = markup.GetAttr("value");
                                if (GetAttr7 != null) {
                                    strDefaultName = GetAttr7;
                                }
                            } else if (GetAttr2.equals("pin_diy")) {
                                String GetAttr8 = markup.GetAttr("value");
                                if (GetAttr8 != null) {
                                    strDefaultPin = GetAttr8;
                                }
                            } else if (GetAttr2.equals("launcher_2ico")) {
                                String GetAttr9 = markup.GetAttr("value");
                                if (GetAttr9 != null && GetAttr9.equals("1")) {
                                    bIsLauncher_2Ico = true;
                                }
                            } else if (GetAttr2.equals("hide_btav_menu")) {
                                String GetAttr10 = markup.GetAttr("value");
                                if (GetAttr10 != null && GetAttr10.equals("1")) {
                                    bDelMenuFromBtAv = true;
                                }
                            } else if (GetAttr2.equals("spevalue")) {
                                String GetAttr11 = markup.GetAttr("value");
                                if (GetAttr11 != null && GetAttr11.equals("1")) {
                                    bSpeValue = true;
                                }
                            } else if (GetAttr2.equals("auto_downPhoneBook")) {
                                String GetAttr12 = markup.GetAttr("value");
                                if (GetAttr12 != null && GetAttr12.equals("1")) {
                                    bAutoDownPhoneBook = true;
                                }
                            } else if (GetAttr2.equals("auto_savePhoneBook")) {
                                String GetAttr13 = markup.GetAttr("value");
                                if (GetAttr13 != null && GetAttr13.equals("1")) {
                                    bAutoSavePhoneBook = true;
                                }
                            } else if (GetAttr2.equals("changeappidwhentalking")) {
                                String GetAttr14 = markup.GetAttr("value");
                                if (GetAttr14 != null && GetAttr14.equals("1")) {
                                    bChangeAppIdWhenTalking = true;
                                }
                            } else if (GetAttr2.equals("show_pairedbt")) {
                                String GetAttr15 = markup.GetAttr("value");
                                if (GetAttr15 != null && GetAttr15.equals("1")) {
                                    bShowPairedBt = true;
                                }
                            } else if (GetAttr2.equals("ShowFloatBtn")) {
                                String GetAttr16 = markup.GetAttr("value");
                                if (GetAttr16 != null && GetAttr16.equals("1")) {
                                    bShowFloatBtn = true;
                                }
                            } else if (GetAttr2.equals("ShowBtInNaviFloatBtn")) {
                                String GetAttr17 = markup.GetAttr("value");
                                if (GetAttr17 != null && GetAttr17.equals("1")) {
                                    bShowBtInNaviFloatBtn = true;
                                }
                            } else if (GetAttr2.equals("HideBtnWhenDisconnect")) {
                                String GetAttr18 = markup.GetAttr("value");
                                if (GetAttr18 != null && GetAttr18.equals("1")) {
                                    hideBtnWhenDisconnect = true;
                                }
                            } else if (GetAttr2.equals("SyncHistory")) {
                                String GetAttr19 = markup.GetAttr("value");
                                if (GetAttr19 != null && GetAttr19.equals("1")) {
                                    bSyncHistory = true;
                                }
                            } else if (GetAttr2.equals("ToastUtil_XY")) {
                                String GetAttr20 = markup.GetAttr("value");
                                if (GetAttr20 != null) {
                                    this.ToastUtil_XY = uiApp.getIntArray(GetAttr20);
                                }
                            } else if (GetAttr2.equals("UIUtil_XY")) {
                                String GetAttr21 = markup.GetAttr("value");
                                if (GetAttr21 != null) {
                                    this.UIUtil_XY = uiApp.getIntArray(GetAttr21);
                                }
                            } else if (GetAttr2.equals("HidePopBt")) {
                                String GetAttr22 = markup.GetAttr("value");
                                if (GetAttr22 != null && GetAttr22.equals("1")) {
                                    bHidePopBt = true;
                                }
                            } else if (GetAttr2.equals("HidePopBt2")) {
                                String GetAttr23 = markup.GetAttr("value");
                                if (GetAttr23 != null && GetAttr23.equals("1")) {
                                    bHidePopBt2 = true;
                                }
                            } else if (GetAttr2.equals("CallinPageFlag")) {
                                String GetAttr24 = markup.GetAttr("value");
                                if (GetAttr24 != null && GetAttr24.equals("1")) {
                                    bCallinPageFlag = true;
                                }
                            } else if (GetAttr2.equals("HistoryLogAllFlag")) {
                                String GetAttr25 = markup.GetAttr("value");
                                if (GetAttr25 != null && GetAttr25.equals("1")) {
                                    bHistoryLogAllFlag = true;
                                }
                            } else if (GetAttr2.equals("NoPageAnimate")) {
                                String GetAttr26 = markup.GetAttr("value");
                                if (GetAttr26 != null && GetAttr26.equals("1")) {
                                    bPageAnimate = false;
                                }
                            } else if (GetAttr2.equals("Translucent")) {
                                String GetAttr27 = markup.GetAttr("value");
                                if (GetAttr27 != null) {
                                    if (GetAttr27.equals("1")) {
                                        mIsTranslucent = 1;
                                    } else if (GetAttr27.equals("2")) {
                                        mIsTranslucent = 2;
                                    } else if (GetAttr27.equals("3")) {
                                        mIsTranslucent = 3;
                                    }
                                }
                            } else if (GetAttr2.equals("FirstPairPage")) {
                                String GetAttr28 = markup.GetAttr("value");
                                if (GetAttr28 != null && GetAttr28.equals("1")) {
                                    bFirstPairPage = true;
                                }
                            } else if (GetAttr2.equals("FirstBtAvPage")) {
                                String GetAttr29 = markup.GetAttr("value");
                                if (GetAttr29 != null && GetAttr29.equals("1")) {
                                    bFirstBtAvPage = true;
                                }
                            } else if (GetAttr2.equals("ShowTime")) {
                                String GetAttr30 = markup.GetAttr("value");
                                if (GetAttr30 != null && GetAttr30.equals("1")) {
                                    bShowTime = true;
                                }
                            } else if (GetAttr2.equals("customer") && (GetAttr = markup.GetAttr("value")) != null) {
                                mStrCustomer = GetAttr;
                            }
                        }
                    } while (markup.NextItem());
                    markup.ExitItem();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ReadConfig() {
        File exceptionFile = new File("/oem/app/exceptions.txt");
        PrintWriter pw = new PrintWriter(exceptionFile);
        File file = new File("/system/app/config.txt");
        if (!file.exists()) {
            file = new File("/oem/app/config.txt");
            if (!file.exists()) {
                return;
            }
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    bufferedReader.close();
                    return;
                }
                String[] split = readLine.split("#");
                if (split.length > 2 && "bt".equals(split[0])) {
                    strDefaultName = split[1];
                    strDefaultPin = split[2];
                }
            }
        } catch (Exception e) {
            e.printStackTrace(pw);
        }
    }

    public void SaveConfig(String str, String str2) {
        String readLine;
        boolean z;
        File file = new File("/mnt/sdcard");
        File file2 = new File("/mnt/sdcard/AppCfg");
        File file3 = new File("/mnt/sdcard/AppCfg/config.txt");
        if (file.exists()) {
            if (!file2.exists() && !file2.isDirectory()) {
                file2.mkdir();
            }
            if (!file3.exists()) {
                try {
                    file3.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file3));
                ArrayList arrayList = new ArrayList();
                String str3 = "bt#" + str + "#" + str2;
                while (true) {
                    readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    arrayList.add(readLine);
                }
                FileWriter fileWriter = new FileWriter(file3);
                if (arrayList.size() > 0) {
                    int i = 0;
                    boolean z2 = false;
                    while (i < arrayList.size()) {
                        if ("bt".equals(((String) arrayList.get(i)).split("#")[0])) {
                            arrayList.set(i, str3);
                            z = true;
                        } else {
                            z = z2;
                        }
                        i++;
                        z2 = z;
                    }
                    if (!z2) {
                        arrayList.add(str3);
                    }
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        fileWriter.write(((String) arrayList.get(i2)).concat("\r\n"));
                    }
                    fileWriter.close();
                    bufferedReader.close();
                    return;
                }
                if (readLine == null) {
                    fileWriter.write(str3);
                }
                fileWriter.close();
                bufferedReader.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void cmdNext(View view, ViewGroup viewGroup) {
        if (view instanceof JButton) {
            MyUiItem myUiItem = (MyUiItem) view.getTag();
            switch (myUiItem.mIntPrevNext[1]) {
                case -4:
                    simulateKey(20);
                    return;
                case -3:
                    simulateKey(22);
                    return;
                case -2:
                    simulateKey(19);
                    return;
                case -1:
                    simulateKey(21);
                    return;
                default:
                    View findViewById = viewGroup.findViewById(myUiItem.mIntPrevNext[1]);
                    if (findViewById != null) {
                        findViewById.requestFocusFromTouch();
                        return;
                    }
                    return;
            }
        }
    }

    public void cmdPrev(View view, ViewGroup viewGroup) {
        if (view instanceof JButton) {
            MyUiItem myUiItem = (MyUiItem) view.getTag();
            switch (myUiItem.mIntPrevNext[0]) {
                case -4:
                    simulateKey(20);
                    return;
                case -3:
                    simulateKey(22);
                    return;
                case -2:
                    simulateKey(19);
                    return;
                case -1:
                    simulateKey(21);
                    return;
                default:
                    View findViewById = viewGroup.findViewById(myUiItem.mIntPrevNext[0]);
                    if (findViewById != null) {
                        findViewById.requestFocusFromTouch();
                        return;
                    }
                    return;
            }
        }
    }

    public void createApplication() {
        super.createApplication();
        bIsAppAmapAssistInstall = FuncUtils.isAppInstalled(this, AMAPASSIST_PKG_NAME);
        InitBroadCast();
        this.mIpcObj.initIpc();
        cd.a().b();
    }

    public int dp2px(float f) {
        return (int) ((getResources().getDisplayMetrics().density * f) + 0.5f);
    }

    public int getFullScreenMode() {
        int i;
        ActivityManager activityManager;
        if (!(this.fytGetState == null || (activityManager = (ActivityManager) getApp().getSystemService("activity")) == null)) {
            try {
                i = ((Integer) this.fytGetState.invoke(activityManager, new Object[0])).intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return i & 15;
        }
        i = -1;
        return i & 15;
    }

    public void getMethod() {
        for (Method method : ActivityManager.class.getDeclaredMethods()) {
            if (method.getName().equals("fytgetState")) {
                this.fytGetState = method;
            } else if (method.getName().equals("fytsetState")) {
                this.fytSetState = method;
            }
        }
    }

    public void hideInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getApp().getSystemService("input_method");
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void initIpPhoneTip() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002e  */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0026 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isHalfScreenAble() {
        /*
            r4 = this;
            r1 = 0
            java.lang.reflect.Method r0 = r4.fytGetState
            if (r0 == 0) goto L_0x002c
            com.syu.app.App r0 = getApp()
            java.lang.String r2 = "activity"
            java.lang.Object r0 = r0.getSystemService(r2)
            android.app.ActivityManager r0 = (android.app.ActivityManager) r0
            if (r0 == 0) goto L_0x002c
            java.lang.reflect.Method r2 = r4.fytGetState     // Catch:{ Exception -> 0x0028 }
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x0028 }
            java.lang.Object r0 = r2.invoke(r0, r3)     // Catch:{ Exception -> 0x0028 }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Exception -> 0x0028 }
            int r0 = r0.intValue()     // Catch:{ Exception -> 0x0028 }
        L_0x0022:
            r0 = r0 & 32
            if (r0 == 0) goto L_0x002e
            r0 = 1
        L_0x0027:
            return r0
        L_0x0028:
            r0 = move-exception
            r0.printStackTrace()
        L_0x002c:
            r0 = r1
            goto L_0x0022
        L_0x002e:
            r0 = r1
            goto L_0x0027
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.app.App.isHalfScreenAble():boolean");
    }

    public boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public void pop3rdPhone(boolean z) {
        Main.removeRunnable_Ui(this.mRunnable_hide3rdPhone);
        Main.removeRunnable_Ui(this.mRunnable_pop3rdPhone);
        Runnable runnable = z ? this.mRunnable_pop3rdPhone : this.mRunnable_hide3rdPhone;
        if (runnable == null) {
            return;
        }
        if (isMainThread()) {
            runnable.run();
        } else {
            Main.postRunnable_Ui(true, runnable);
        }
    }

    public void popBt(boolean z, boolean z2) {
        bShowKeyBoradWhenPopBt = z2;
        Main.removeRunnable_Ui(this.mRunnable_PopBt);
        Main.removeRunnable_Ui(this.mRunnable_hideBt);
        if (z) {
            if (bv.d() && bPop3rdPhone_YF) {
                getApp().pop3rdPhone(true);
            }
            showFloatBtn(false);
            if (!z2) {
                popBtNum(false);
                popBtBook(false);
            }
        } else {
            popBtBook(false);
            if (bShowFloatBtn && !isAppTop()) {
                showFloatBtn(true);
            }
        }
        Runnable runnable = z ? this.mRunnable_PopBt : this.mRunnable_hideBt;
        if (runnable != null) {
            if (isMainThread()) {
                runnable.run();
            } else {
                Main.postRunnable_Ui(true, runnable);
            }
        }
        if (!bHasDownBook && bAutoDownPhoneBook) {
            IpcObj.downloadBook();
        }
    }

    public void popBtAvMicSet(boolean z) {
        Main.removeRunnable_Ui(this.mRunnable_popBtAvMicSet);
        Main.removeRunnable_Ui(this.mRunnable_hideBtAvMicSet);
        Runnable runnable = z ? this.mRunnable_popBtAvMicSet : this.mRunnable_hideBtAvMicSet;
        if (runnable == null) {
            return;
        }
        if (isMainThread()) {
            runnable.run();
        } else {
            Main.postRunnable_Ui(true, runnable);
        }
    }

    public void popBtBook(boolean z) {
        Main.removeRunnable_Ui(this.mRunnable_popBtBook);
        Main.removeRunnable_Ui(this.mRunnable_hideBtBook);
        Runnable runnable = z ? this.mRunnable_popBtBook : this.mRunnable_hideBtBook;
        if (runnable == null) {
            return;
        }
        if (isMainThread()) {
            runnable.run();
        } else {
            Main.postRunnable_Ui(true, runnable);
        }
    }

    public void popBtNum(boolean z) {
        Main.removeRunnable_Ui(this.mRunnable_PopBtNum);
        Main.removeRunnable_Ui(this.mRunnable_hideBtNum);
        Runnable runnable = z ? this.mRunnable_PopBtNum : this.mRunnable_hideBtNum;
        if (runnable == null) {
            return;
        }
        if (isMainThread()) {
            runnable.run();
        } else {
            Main.postRunnable_Ui(true, runnable);
        }
    }

    public void popBtRingSet(boolean z) {
        Main.removeRunnable_Ui(this.mRunnable_hideBtRingSet);
        Main.removeRunnable_Ui(this.mRunnable_popBtRingSet);
        Runnable runnable = z ? this.mRunnable_popBtRingSet : this.mRunnable_hideBtRingSet;
        if (runnable == null) {
            return;
        }
        if (isMainThread()) {
            runnable.run();
        } else {
            Main.postRunnable_Ui(true, runnable);
        }
    }

    public void popPhoneMicSet(boolean z) {
        Main.removeRunnable_Ui(this.mRunnable_hidePhoneMicSet);
        Main.removeRunnable_Ui(this.mRunnable_popPhoneMicSet);
        Runnable runnable = z ? this.mRunnable_popPhoneMicSet : this.mRunnable_hidePhoneMicSet;
        if (runnable == null) {
            return;
        }
        if (isMainThread()) {
            runnable.run();
        } else {
            Main.postRunnable_Ui(true, runnable);
        }
    }

    public void popPhoneSMS(boolean z) {
        if (this.bPopPhoneSms || z) {
            Main.removeRunnable_Ui(this.mRunnable_hidePhoneSMS);
            Main.removeRunnable_Ui(this.mRunnable_popPhoneSMS);
            Runnable runnable = z ? this.mRunnable_popPhoneSMS : this.mRunnable_hidePhoneSMS;
            if (runnable == null) {
                return;
            }
            if (isMainThread()) {
                runnable.run();
            } else {
                Main.postRunnable_Ui(true, runnable);
            }
        }
    }

    public void popPhoneVoice(boolean z) {
        Main.removeRunnable_Ui(this.mRunnable_hidePhoneVoice);
        Main.removeRunnable_Ui(this.mRunnable_popPhoneVoice);
        Runnable runnable = z ? this.mRunnable_popPhoneVoice : this.mRunnable_hidePhoneVoice;
        if (runnable == null) {
            return;
        }
        if (isMainThread()) {
            runnable.run();
        } else {
            Main.postRunnable_Ui(true, runnable);
        }
    }

    public void preCreateApplication() {
        MySharePreference.init(this, "bt_data");
        ReadConf();
        ReadConfig();
        Main.postRunnable_Ui(true, this.mIpcObj.mRunnable_scanDB, 3000);
        if (mEnableHalfScreen) {
            getMethod();
        }
        super.preCreateApplication();
        mBtInfo = new Bt_Info();
        this.windowManager = (WindowManager) getSystemService("window");
        CustomerConfig();
    }

    public void requestAppIdRight() {
        this.mIpcObj.requestAppIdRight(sPage);
    }

    public void sendCmd2AmapAssist(String str) {
        Intent intent = new Intent();
        intent.setAction("BT_STANDARD_BROADCAST_CMD");
        intent.putExtra("CMD_TYPE", 10000);
        intent.putExtra("CMD_DESC", str);
        intent.setFlags(32);
        sendBroadcast(intent);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x002e A[SYNTHETIC, Splitter:B:12:0x002e] */
    /* JADX WARNING: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setFullScreenMode(int r6) {
        /*
            r5 = this;
            r3 = 1
            r2 = 0
            java.lang.reflect.Method r0 = r5.fytSetState
            if (r0 == 0) goto L_0x003d
            com.syu.app.App r0 = getApp()
            java.lang.String r1 = "activity"
            java.lang.Object r0 = r0.getSystemService(r1)
            android.app.ActivityManager r0 = (android.app.ActivityManager) r0
            if (r0 == 0) goto L_0x003d
            r1 = 16
            if (r6 != r1) goto L_0x0042
            java.lang.reflect.Method r1 = r5.fytGetState     // Catch:{ Exception -> 0x003e }
            r4 = 0
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ Exception -> 0x003e }
            java.lang.Object r1 = r1.invoke(r0, r4)     // Catch:{ Exception -> 0x003e }
            java.lang.Integer r1 = (java.lang.Integer) r1     // Catch:{ Exception -> 0x003e }
            int r1 = r1.intValue()     // Catch:{ Exception -> 0x003e }
            r1 = r1 & 16
            if (r1 == 0) goto L_0x0042
            r1 = r2
        L_0x002c:
            if (r1 == 0) goto L_0x003d
            java.lang.reflect.Method r1 = r5.fytSetState     // Catch:{ Exception -> 0x0044 }
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x0044 }
            r3 = 0
            java.lang.Integer r4 = java.lang.Integer.valueOf(r6)     // Catch:{ Exception -> 0x0044 }
            r2[r3] = r4     // Catch:{ Exception -> 0x0044 }
            r1.invoke(r0, r2)     // Catch:{ Exception -> 0x0044 }
        L_0x003d:
            return
        L_0x003e:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0042:
            r1 = r3
            goto L_0x002c
        L_0x0044:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x003d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.app.App.setFullScreenMode(int):void");
    }

    public void showFloatBtn(boolean z) {
        if (bIsAppAmapAssistInstall) {
            if (IpcObj.isDisConnect()) {
                getApp().sendCmd2AmapAssist("bt_disconnect");
            } else {
                getApp().sendCmd2AmapAssist("bt_connect");
            }
            if (z) {
                sendCmd2AmapAssist("ShowFloatBt");
            } else {
                sendCmd2AmapAssist("hideFloatBt");
            }
            if (!bShowBtInNaviFloatBtn) {
                return;
            }
        }
        Main.removeRunnable_Ui(this.mRunnable_HideFloatBtn);
        Main.removeRunnable_Ui(this.mRunnable_ShowFloatButton);
        Runnable runnable = z ? this.mRunnable_ShowFloatButton : this.mRunnable_HideFloatBtn;
        if (runnable == null) {
            return;
        }
        if (isMainThread()) {
            runnable.run();
        } else {
            Main.postRunnable_Ui(true, runnable);
        }
    }

    public void showInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getApp().getSystemService("input_method");
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    public void simulateKey(final int i) {
        startThread(StrThreadSimulateKey, new Runnable() {
            public void run() {
                try {
                    new Instrumentation().sendKeyDownUpSync(i);
                } catch (Exception e) {
                }
            }
        }, false, 5);
    }

    public void updatePhoneName() {
        Iterator<InterfaceBt> it = mInterfaceBt.iterator();
        while (it.hasNext()) {
            it.next().updatePhoneName();
        }
        if (this.bPopBtShow) {
            JPage jPage = uiApp.mPages.get(1);
            if (jPage == null) {
                jPage = uiApp.mPages.get(3);
            }
            if (jPage != null) {
                JText jText = (JText) jPage.getChildViewByid(17);
                JText jText2 = (JText) jPage.getChildViewByid(18);
                if (jText == null) {
                    return;
                }
                if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                    jText.setText(FinalChip.BSP_PLATFORM_Null);
                    if (jText2 != null) {
                        jText2.setText(FinalChip.BSP_PLATFORM_Null);
                        return;
                    }
                    return;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(mBtInfo.mListContact);
                if (jText2 == null || !bv.h()) {
                    startThread(StrThreadGetNameByNumber, new br(arrayList, jText, FinalChip.BSP_PLATFORM_Null, false), false, 5);
                    return;
                }
                String str = Bt.sPhoneNumber;
                if (Ipc_New.isRing()) {
                    str = Bt.sPhoneNumberHoldOn;
                }
                startThread(StrThreadGetNameByNumber, new bs(arrayList, jText, jText2, str, str, true), false, 5);
            }
        }
    }

    public void updatePhoneView() {
        Iterator<InterfaceBt> it = mInterfaceBt.iterator();
        while (it.hasNext()) {
            it.next().updateShowDial();
        }
    }
}
