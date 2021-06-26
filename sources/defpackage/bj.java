package defpackage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.ipcself.module.main.Bt;

/* renamed from: bj  reason: default package */
public class bj extends Handler {
    static bj a;

    bj(Looper looper) {
        super(looper);
    }

    public static bj a() {
        if (a == null) {
            a = new bj(Looper.getMainLooper());
        }
        return a;
    }

    public static void a(String str) {
        if (TextUtils.isEmpty(str)) {
            cb.a().a(App.getApp().getString("bt_match_number_fail"));
        } else {
            App.getApp().mIpcObj.dialOut(str);
        }
    }

    public void a(Bundle bundle) {
        switch (bundle.getInt("BT_SEND_CMD")) {
            case 2:
                a(bundle.getString("CALL_CENTER_NUMBER"));
                return;
            default:
                return;
        }
    }

    public void dispatchMessage(Message message) {
        switch (message.what) {
            case 1:
                Intent intent = new Intent("com.bdxh.bc.rec");
                int i = IpcObj.isConnect() ? 0 : 1;
                switch (Bt.DATA[9]) {
                    case 3:
                        i = 3;
                        break;
                    case 4:
                        i = 2;
                        break;
                    case 5:
                        i = 5;
                        break;
                }
                intent.putExtra("BT_RECEIVE_CMD", i);
                App.getApp().sendBroadcast(intent);
                break;
        }
        super.dispatchMessage(message);
    }
}
