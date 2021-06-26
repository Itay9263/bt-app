package com.syu.bt.act;

import android.app.Activity;
import android.os.Bundle;
import com.syu.app.App;

public class ActBtPairDevices extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        App.sPage = 12;
        App.getApp().startAct((Class<?>) ActBt.class);
        finish();
    }
}
