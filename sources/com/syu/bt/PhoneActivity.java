package com.syu.bt;

import android.app.Activity;
import android.os.Bundle;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;

public class PhoneActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        String schemeSpecificPart = getIntent().getData().getSchemeSpecificPart();
        if (IpcObj.isConnect()) {
            App.getApp().mIpcObj.dialOut(schemeSpecificPart);
        }
        finish();
        super.onCreate(bundle);
    }
}
