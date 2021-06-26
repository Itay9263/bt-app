package com.syu.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.syu.app.App;
import com.syu.bt.act.InterfaceBt;
import com.syu.data.FinalAppMainServer;
import com.syu.ipcself.module.main.Main;
import java.util.Iterator;

public class Receiver extends BroadcastReceiver {
    a a = null;

    public class a implements Runnable {
        boolean a = true;
        Bundle b;

        public a(Bundle bundle) {
            this.b = bundle;
        }

        public void a() {
            this.a = false;
        }

        public void run() {
            if (this.a) {
                Receiver.this.a(this.b);
            }
        }
    }

    private void b(Bundle bundle) {
        if (App.getApp().isAppTop() || !App.mPkgName.equals(bundle.getString("pkg"))) {
            return;
        }
        if (!App.bChangeAppIdWhenTalking) {
            App.getApp().mIpcObj.requestAppIdNull();
        } else if (Main.DATA[0] == 3 || Main.DATA[0] == 2) {
            Main.requestAppId(0);
        }
    }

    private void c(Bundle bundle) {
        if (!bundle.containsKey("cmdCode")) {
            String string = bundle.getString("number");
            if (!TextUtils.isEmpty(string)) {
                ca.a(string);
            } else {
                ca.b(bundle.getString("contact"));
            }
        } else {
            switch (bundle.getInt("cmdCode", -1)) {
                case -1:
                    ca.e();
                    return;
                case 20:
                    ca.b(bundle.getString("contact"));
                    return;
                case 21:
                    ca.a(bundle.getString("number"));
                    return;
                case 22:
                    ca.a();
                    return;
                case 23:
                    ca.b();
                    return;
                case 24:
                    ca.d();
                    return;
                case 25:
                    ca.c();
                    return;
                default:
                    return;
            }
        }
    }

    public void a(Bundle bundle) {
        if (bundle.containsKey("CMD_DESC") && bundle.containsKey("CMD_TYPE") && bundle.getInt("CMD_TYPE") == 10000) {
            String string = bundle.getString("CMD_DESC");
            if (string.startsWith("欢迎使用高德语音助手!")) {
                App.getApp().showFloatBtn(true);
            } else if (string.startsWith("打电话") || string.startsWith("呼叫") || string.startsWith("拨号") || string.startsWith("拨打")) {
                App.getApp().popBt(true, true);
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if ("com.syu.bt.local.cmd".equals(action)) {
            Bundle extras = intent.getExtras();
            if (extras != null && !extras.isEmpty()) {
                c(extras);
            } else {
                return;
            }
        } else if ("AMAPASSIST_STANDARD_BROADCAST_CMD".equals(action)) {
            Bundle extras2 = intent.getExtras();
            if (extras2 != null && !extras2.isEmpty()) {
                if (this.a != null) {
                    this.a.a();
                    Main.removeRunnable_Ui(this.a);
                    this.a = null;
                }
                this.a = new a(extras2);
                Main.postRunnable_Ui(false, this.a);
            } else {
                return;
            }
        } else if (FinalAppMainServer.ACTION_SYSTEMUI_REMOVE.equals(action)) {
            Bundle extras3 = intent.getExtras();
            if (extras3 != null && !extras3.isEmpty()) {
                b(extras3);
            } else {
                return;
            }
        } else if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
            bu.a().b();
        } else if ("com.bdxh.bc.send".equals(action)) {
            Bundle extras4 = intent.getExtras();
            if (extras4 != null && !extras4.isEmpty()) {
                bj.a().a(extras4);
            } else {
                return;
            }
        } else if ("android.intent.action.NEW_OUTGOING_CALL".equals(action)) {
            System.out.println("ACTION_NEW_OUTGOING_CALL: " + getResultData());
        } else if (action.equals("launcher3.setcolor")) {
            Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
            while (it.hasNext()) {
                it.next().resetColor();
            }
        }
        if (App.mIsOldService) {
            int i = -1;
            if ("com.syu.bt.PageAv".equals(action)) {
                i = 4;
            } else if ("com.syu.bt.PageAvForce".equals(action)) {
                i = 5;
            } else if ("com.syu.bt.PagePhone".equals(action)) {
                i = 3;
            } else if ("com.syu.bt.PagePhoneByKey".equals(action)) {
                i = 2;
            } else if ("com.syu.bt.ShowPipPhone".equals(action)) {
                i = 1;
            }
            if (i >= 0) {
                MyService.a(i);
            }
        }
    }
}
