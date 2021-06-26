package com.syu.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.app.ipc.Ipc_New;
import com.syu.bt.act.InterfaceBt;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import java.util.Iterator;

public class AutoTestReceiver extends BroadcastReceiver {

    public class a implements Runnable {
        String a;

        public a(String str) {
            this.a = str;
        }

        public void run() {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().updatePin(this.a);
            }
        }
    }

    public class b implements Runnable {
        int a;

        public b(int i) {
            this.a = i;
        }

        public void run() {
            Intent intent = new Intent("com.syu.autotest.bt_recv");
            if (!IpcObj.isConnect() || IpcObj.isPairing()) {
                intent.putExtra("data1", 1);
            } else if (App.mBtInfo.mListContact == null || App.mBtInfo.mListContact.size() <= 0) {
                intent.putExtra("data1", 0);
            } else if (App.mInterfaceBt.size() > 0) {
                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                while (it.hasNext()) {
                    it.next().ClearAllContact();
                }
                intent.putExtra("data1", 0);
            } else {
                intent.putExtra("data1", 1);
            }
            intent.putExtra("data0", this.a);
            App.getApp().sendBroadcast(intent);
        }
    }

    private boolean a(String str) {
        int length = str.length();
        do {
            length--;
            if (length < 0) {
                return true;
            }
        } while (Character.isDigit(str.charAt(length)));
        return false;
    }

    public void onReceive(Context context, Intent intent) {
        Runnable runnable = null;
        int intExtra = intent.getIntExtra("data0", -1);
        int intExtra2 = intent.getIntExtra("data1", -1);
        String stringExtra = intent.getStringExtra("str0");
        Intent intent2 = new Intent("com.syu.autotest.bt_recv");
        switch (intExtra) {
            case 1:
                switch (intExtra2) {
                    case 1:
                        IpcObj.setBtSwitch(true);
                        Main.postRunnable_Ui(false, new Runnable() {
                            public void run() {
                                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                                while (it.hasNext()) {
                                    it.next().setSwitch(true);
                                }
                            }
                        });
                        break;
                    case 2:
                        IpcObj.setBtSwitch(false);
                        Main.postRunnable_Ui(false, new Runnable() {
                            public void run() {
                                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                                while (it.hasNext()) {
                                    it.next().setSwitch(false);
                                }
                            }
                        });
                        break;
                }
                if (intExtra2 == 1 || intExtra2 == 2) {
                    intent2.putExtra("data0", intExtra);
                    intent2.putExtra("data1", intExtra2);
                    App.getApp().sendBroadcast(intent2);
                    return;
                }
                return;
            case 2:
                switch (intExtra2) {
                    case 0:
                        runnable = MyService.b;
                        break;
                    case 1:
                        runnable = MyService.e;
                        break;
                    case 3:
                        runnable = MyService.a;
                        break;
                    case 4:
                        runnable = MyService.e;
                        break;
                    case 5:
                        runnable = MyService.g;
                        break;
                }
                if (runnable != null) {
                    Main.postRunnable_Ui(true, runnable);
                    intent2.putExtra("data0", intExtra);
                    intent2.putExtra("data1", intExtra2);
                    App.getApp().sendBroadcast(intent2);
                    return;
                }
                return;
            case 3:
                String str = Bt.sDevAddr;
                if (!TextUtils.isEmpty(str)) {
                    String replace = str.replace(":", FinalChip.BSP_PLATFORM_Null);
                    if (replace.length() == 12) {
                        intent2.putExtra("str0", replace);
                    } else {
                        intent2.putExtra("str0", (String) null);
                    }
                } else {
                    intent2.putExtra("str0", (String) null);
                }
                intent2.putExtra("data0", intExtra);
                intent2.putExtra("data1", intExtra2);
                App.getApp().sendBroadcast(intent2);
                return;
            case 4:
                if (stringExtra == null || stringExtra.length() != 4 || !a(stringExtra)) {
                    intent2.putExtra("data1", 1);
                    intent2.putExtra("str0", App.getDiyPin());
                } else {
                    IpcObj.setDevPin(stringExtra);
                    Main.postRunnable_Ui(false, new a(stringExtra));
                    App.getApp().SaveConfig(App.getDiyName(), stringExtra);
                    intent2.putExtra("data1", 0);
                    intent2.putExtra("str0", stringExtra);
                }
                intent2.putExtra("data0", intExtra);
                App.getApp().sendBroadcast(intent2);
                return;
            case 5:
                switch (intExtra2) {
                    case 1:
                        if (Bt.DATA[9] != 4) {
                            intent2.putExtra("data1", 0);
                            break;
                        } else {
                            Ipc_New.dial();
                            intent2.putExtra("data1", 2);
                            break;
                        }
                    case 2:
                        if (!IpcObj.isInCall()) {
                            intent2.putExtra("data1", 0);
                            break;
                        } else {
                            IpcObj.hang();
                            intent2.putExtra("data1", 3);
                            break;
                        }
                }
                intent2.putExtra("data0", intExtra);
                App.getApp().sendBroadcast(intent2);
                return;
            case 7:
                if (intExtra2 < 0 || intExtra2 > 10) {
                    intent2.putExtra("data1", 1);
                    intent2.putExtra("str0", String.valueOf(App.iPhoneMicSet));
                } else {
                    App.getApp().mIpcObj.sendCmdVolBal(2, intExtra2);
                    intent2.putExtra("data1", 0);
                    intent2.putExtra("str0", String.valueOf(intExtra2));
                }
                intent2.putExtra("data0", intExtra);
                App.getApp().sendBroadcast(intent2);
                return;
            case 8:
                if (Ipc_New.isPhoneConnect()) {
                    intent2.putExtra("data1", 0);
                } else {
                    intent2.putExtra("data1", 1);
                }
                intent2.putExtra("data0", intExtra);
                App.getApp().sendBroadcast(intent2);
                return;
            case 10:
                if (stringExtra != null) {
                    IpcObj.setDevName(stringExtra);
                    Main.postRunnable_Ui(false, new a(stringExtra));
                    App.getApp().SaveConfig(stringExtra, App.getDiyPin());
                    intent2.putExtra("data1", 0);
                    intent2.putExtra("str0", stringExtra);
                } else {
                    intent2.putExtra("data1", 1);
                    intent2.putExtra("str0", App.getDiyName());
                }
                intent2.putExtra("data0", intExtra);
                App.getApp().sendBroadcast(intent2);
                return;
            case 11:
                if (!IpcObj.isConnect() || IpcObj.isPairing()) {
                    intent2.putExtra("data1", 0);
                } else {
                    IpcObj.downloadBook();
                    intent2.putExtra("data1", 1);
                }
                intent2.putExtra("data0", intExtra);
                App.getApp().sendBroadcast(intent2);
                return;
            case 13:
                Main.postRunnable_Ui(false, new b(intExtra));
                return;
            case 14:
                if (Ipc_New.isTalk()) {
                    intent2.putExtra("data1", 0);
                } else {
                    intent2.putExtra("data1", 1);
                }
                intent2.putExtra("data0", intExtra);
                App.getApp().sendBroadcast(intent2);
                return;
            default:
                return;
        }
    }
}
