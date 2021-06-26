package com.syu.ipc.module.cb;

import android.os.RemoteException;
import com.syu.ipc.ICallback;
import com.syu.ipc.module.Main786;
import com.syu.ipcself.module.main.Main;
import com.syu.util.IpcUtil;

public class Main786_CB extends ICallback.Stub {
    private static Main786_CB INSTANCE = new Main786_CB();

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
            if (this.updateCode >= 0 && this.updateCode < 100) {
                switch (this.updateCode) {
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                    case 11:
                    case 16:
                    case 25:
                        Main786.update(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                    default:
                        if (IpcUtil.intsOk(this.ints, 1)) {
                            Main.DATA[this.updateCode] = this.ints[0];
                        }
                        Main.mUiNotifyEvent.updateNotify(this.updateCode, this.ints, this.flts, this.strs);
                        return;
                }
            }
        }
    }

    public static Main786_CB getInstance() {
        return INSTANCE;
    }

    public void update(int i, int[] iArr, float[] fArr, String[] strArr) throws RemoteException {
        Main.postRunnable_Ui(false, new Runnable_Update(i, iArr, fArr, strArr));
    }
}
