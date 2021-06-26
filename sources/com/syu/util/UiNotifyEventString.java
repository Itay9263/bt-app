package com.syu.util;

import android.os.RemoteException;
import com.syu.data.FinalChip;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteToolkit;

public class UiNotifyEventString extends UiNotifyEvent {
    private IModuleCallback mCallback = new IModuleCallback.Stub() {
        public void update(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
            if (strArr != null && strArr.length > 0) {
                String str = strArr[0];
                if (str == null) {
                    if (UiNotifyEventString.this.mValue != null) {
                        UiNotifyEventString.this.mValue = str;
                        UiNotifyEventString.this.updateNotify(i, iArr, fArr, strArr);
                    }
                } else if (!str.equals(UiNotifyEventString.this.mValue)) {
                    UiNotifyEventString.this.mValue = str;
                    UiNotifyEventString.this.updateNotify(i, iArr, fArr, strArr);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public String mValue = FinalChip.BSP_PLATFORM_Null;

    public String getValue() {
        return this.mValue;
    }

    public void register(IRemoteToolkit iRemoteToolkit, int i, int i2, int i3) {
        try {
            iRemoteToolkit.getRemoteModule(i).register(this.mCallback, i2, i3);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
