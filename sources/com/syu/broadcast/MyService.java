package com.syu.broadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.bt.act.InterfaceBt;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import java.util.Iterator;

public class MyService extends Service {
    public static Runnable a = new Runnable() {
        public void run() {
            int i = (!App.getApp().mIpcObj.isCalling() || !App.bCallinPageFlag) ? 3 : 22;
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().goPage(i, true);
            }
        }
    };
    public static Runnable b = new Runnable() {
        public void run() {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().goPage(5, true);
            }
        }
    };
    public static Runnable c = new Runnable() {
        public void run() {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                InterfaceBt next = it.next();
                next.goPage(5, true);
                next.voiceSearchContact();
            }
        }
    };
    public static Runnable d = new Runnable() {
        public void run() {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().goPage(10, true);
            }
        }
    };
    public static Runnable e = new Runnable() {
        public void run() {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().goPage(11, true);
            }
        }
    };
    public static Runnable f = new Runnable() {
        public void run() {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().goPage(12, true);
            }
        }
    };
    public static Runnable g = new Runnable() {
        public void run() {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().goPage(8, true);
            }
        }
    };

    public static void a(int i) {
        switch (i) {
            case 1:
                if (App.mEnableHalfScreen) {
                    if (App.mInterfaceBt.size() != 0 && App.getApp().isAppTop()) {
                        return;
                    }
                    if (App.bBackCarFlag) {
                        switch (Bt.DATA[9]) {
                            case 3:
                            case 4:
                            case 5:
                                IpcObj.hang();
                                return;
                            default:
                                return;
                        }
                    } else {
                        if (App.sPage != 22) {
                            App.sPageBak = App.sPage;
                            App.sBackFlag |= 1;
                            App.sPage = 22;
                        }
                        App.bResumeByDial = true;
                        int fullScreenMode = App.getApp().getFullScreenMode() & 15;
                        if (fullScreenMode == 0) {
                            App.sBackFlag |= 8;
                        } else if (fullScreenMode == 1) {
                            App.sBackFlag |= 16;
                        } else if (fullScreenMode == 2) {
                            App.sBackFlag |= 4;
                        }
                        if (a()) {
                            App.sBackFlag |= 2;
                            return;
                        }
                        return;
                    }
                } else if (App.bCallinPageFlag) {
                    if ((App.mInterfaceBt.size() == 0 || !App.getApp().isAppTop()) && App.sPage != 22) {
                        App.sPageBak = App.sPage;
                        App.sBackFlag |= 1;
                        App.sPage = 22;
                    }
                    Main.removeRunnable_Ui(d);
                    if (App.getApp().isAppTop()) {
                        Main.postRunnable_Ui(true, a);
                        return;
                    }
                    App.getApp().bPopBtShowBak_Num = false;
                    App.getApp().popBt(true, false);
                    return;
                } else {
                    if ((App.mInterfaceBt.size() == 0 || !App.getApp().isAppTop()) && App.sPage != 3) {
                        App.sPageBak = App.sPage;
                        App.sBackFlag |= 1;
                        App.sPage = 3;
                    }
                    Main.removeRunnable_Ui(d);
                    if (App.getApp().isAppTop()) {
                        Main.postRunnable_Ui(true, a);
                        return;
                    } else if (App.bHidePopBt) {
                        App.sBackFlag |= 32;
                        App.bResumeByDial = true;
                        if (a()) {
                            App.sBackFlag |= 2;
                            return;
                        }
                        return;
                    } else if (App.bHidePopBt2) {
                        App.bResumeByDial = true;
                        return;
                    } else {
                        App.getApp().bPopBtShowBak_Num = false;
                        App.getApp().popBt(true, false);
                        return;
                    }
                }
            case 2:
                if (App.mInterfaceBt.size() == 0 || !App.getApp().isAppTop()) {
                    App.sPage = 3;
                }
                Main.removeRunnable_Ui(d);
                Main.postRunnable_Ui(true, a);
                if (App.getApp().bPopBtShow) {
                    App.getApp().popBt(false, false);
                }
                a();
                return;
            case 3:
                if (App.mInterfaceBt.size() == 0 || !App.getApp().isAppTop()) {
                    if (App.mEnableHalfScreen) {
                        if (App.bBackCarFlag) {
                            switch (Bt.DATA[9]) {
                                case 3:
                                case 4:
                                case 5:
                                    IpcObj.hang();
                                    return;
                                default:
                                    return;
                            }
                        } else if (App.sPage != 22) {
                            App.sPageBak = App.sPage;
                            App.sBackFlag |= 1;
                            App.sPage = 22;
                            int fullScreenMode2 = App.getApp().getFullScreenMode() & 15;
                            if (fullScreenMode2 == 0) {
                                App.sBackFlag |= 8;
                            } else if (fullScreenMode2 == 1) {
                                App.sBackFlag |= 16;
                            } else if (fullScreenMode2 == 2) {
                                App.sBackFlag |= 4;
                            }
                        }
                    } else if (App.bCallinPageFlag) {
                        if (App.sPage != 22) {
                            App.sPageBak = App.sPage;
                            App.sBackFlag |= 1;
                            App.sPage = 22;
                        }
                    } else if (App.sPage != 3) {
                        App.sPageBak = App.sPage;
                        App.sBackFlag |= 1;
                        App.sPage = 3;
                    }
                } else if (App.mEnableHalfScreen) {
                    int fullScreenMode3 = App.getApp().getFullScreenMode() & 15;
                    if (fullScreenMode3 == 0) {
                        App.sBackFlag |= 8;
                    } else if (fullScreenMode3 == 1) {
                        App.sBackFlag |= 16;
                    } else if (fullScreenMode3 == 2) {
                        App.sBackFlag |= 4;
                    }
                }
                Main.removeRunnable_Ui(d);
                App.getApp().popBt(false, false);
                App.bResumeByDial = true;
                if (a()) {
                    App.sBackFlag |= 2;
                    return;
                }
                return;
            case 4:
                if (bv.b()) {
                    if (App.mInterfaceBt.size() == 0 || !App.getApp().isAppTop()) {
                        App.sPage = 10;
                    }
                    Main.removeRunnable_Ui(a);
                    Main.postRunnable_Ui(true, d);
                    return;
                }
                return;
            case 5:
                if (bv.b()) {
                    if (App.mInterfaceBt.size() == 0 || !App.getApp().isAppTop()) {
                        App.sPage = 10;
                    }
                    Main.removeRunnable_Ui(a);
                    Main.postRunnable_Ui(true, d);
                    a();
                    return;
                }
                return;
            case 6:
                if (App.getApp().bPopPhoneFirstValue == 1) {
                    App.getApp().bPopPhoneFirstValue = 0;
                }
                App.getApp().popPhoneVoice(true);
                return;
            default:
                return;
        }
    }

    public static boolean a() {
        if (App.getApp().isAppTop()) {
            return false;
        }
        if (App.bShowFloatBtn) {
            App.getApp().popBt(true, false);
            return false;
        }
        App.getApp().startAct((Class<?>) ActBt.class);
        return true;
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (intent != null && !App.bFirstBtAvPage) {
            String action = intent.getAction();
            if ("com.syu.bt.pip".equals(action)) {
                a(1);
            } else if ("com.syu.bt.bywork".equals(action)) {
                a(3);
            } else if ("com.syu.bt.bykey".equals(action)) {
                a(2);
            } else if ("com.syu.bt.byav".equals(action)) {
                a(4);
            } else if ("com.syu.bt.byav.force".equals(action)) {
                a(5);
            } else if ("com.syu.bt.phone.voice".equals(action)) {
                a(6);
            }
        }
        return super.onStartCommand(intent, i, i2);
    }
}
