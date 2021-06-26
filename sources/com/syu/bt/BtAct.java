package com.syu.bt;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import com.syu.app.App;
import com.syu.bt.act.ActBt;

public class BtAct extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (App.bIsLauncher_2Ico) {
            App.sPage = 3;
        }
        if (App.bFirstPairPage && !App.getApp().mIpcObj.isCalling()) {
            App.sPage = 12;
        }
        if (App.bFirstBtAvPage) {
            App.sPage = 10;
        }
        if (getIntent() == null || TextUtils.isEmpty(getIntent().getAction()) || !"btav".equals(getIntent().getStringExtra("launcher"))) {
            App.getApp().startAct((Class<?>) ActBt.class);
            finish();
            return;
        }
        App.getApp().startAct("com.syu.btav");
        finish();
    }
}
