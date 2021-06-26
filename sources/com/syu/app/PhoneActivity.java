package com.syu.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.syu.app.ipc.IpcObj;

public class PhoneActivity extends Activity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Uri data = getIntent().getData();
        Log.i("Phone", data + ", " + data.getSchemeSpecificPart());
        String schemeSpecificPart = data.getSchemeSpecificPart();
        if (IpcObj.isConnect()) {
            AppBluetooth.getApp().mIpcObj.dialOut(schemeSpecificPart);
        }
        finish();
        super.onCreate(bundle);
    }
}
