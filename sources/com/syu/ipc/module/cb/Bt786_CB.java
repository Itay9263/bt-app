package com.syu.ipc.module.cb;

import android.os.RemoteException;
import com.syu.ipc.ICallback;
import com.syu.ipc.module.Bt786;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.util.IpcUtil;

public class Bt786_CB extends ICallback.Stub {
    private static Bt786_CB INSTANCE = new Bt786_CB();

    private class Runnable_Update implements Runnable {
        public float[] flts;
        public int[] ints;
        public String[] strs;
        public int updateCode;

        public Runnable_Update(int i, int[] iArr, float[] fArr, String[] strArr) {
            this.updateCode = i;
            this.ints = iArr;
            this.flts = fArr;
            this.strs = strArr;
        }

        public void run() {
            switch (this.updateCode) {
                case 4:
                case 9:
                case 11:
                case 12:
                case 23:
                    Bt786.update(this.updateCode, this.ints, this.flts, this.strs);
                    return;
                case 5:
                    Bt786.phoneNumber(this.updateCode, this.ints, this.flts, this.strs);
                    return;
                case 6:
                    Bt786.devName(this.updateCode, this.ints, this.flts, this.strs);
                    return;
                case 8:
                    Bt786.devPin(this.updateCode, this.ints, this.flts, this.strs);
                    return;
                case 13:
                    if (IpcUtil.strsOk(this.strs, 2)) {
                        Bt.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    }
                    return;
                case 14:
                    Bt786.phoneName(this.updateCode, this.ints, this.flts, this.strs);
                    return;
                case 15:
                    Bt786.localAddr(this.updateCode, this.ints, this.flts, this.strs);
                    return;
                case 17:
                    Bt786.phoneAddr(this.updateCode, this.ints, this.flts, this.strs);
                    return;
                default:
                    if (IpcUtil.intsOk(this.ints, 1) && this.updateCode < 50 && this.ints[0] != Bt.DATA[this.updateCode]) {
                        if (this.updateCode == 1) {
                            Bt.sLastPhoneState = Bt.DATA[this.updateCode];
                        }
                        Bt.DATA[this.updateCode] = this.ints[0];
                    }
                    Bt.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                    return;
            }
        }
    }

    public static Bt786_CB getInstance() {
        return INSTANCE;
    }

    public void update(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        Main.postRunnable_Ui(false, new Runnable_Update(i, iArr, fArr, strArr));
    }
}
