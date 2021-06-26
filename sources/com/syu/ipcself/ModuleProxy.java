package com.syu.ipcself;

import android.os.Handler;
import android.os.HandlerThread;
import com.syu.ipc.IModuleCallback;
import com.syu.ipc.IRemoteModule;
import com.syu.ipc.ModuleObject;

public class ModuleProxy extends IRemoteModule.Stub {
    public static Handler mHandlerCmd;
    public static HandlerThread mHandlerThreadCmd;
    /* access modifiers changed from: private */
    public IRemoteModule mRemoteModule;

    public class Runnable_Cmd implements Runnable {
        int cmdCode;
        float[] flts;
        int[] ints;
        String[] strs;

        public Runnable_Cmd(int i, int[] iArr, float[] fArr, String[] strArr) {
            this.cmdCode = i;
            this.ints = iArr;
            this.flts = fArr;
            this.strs = strArr;
        }

        public void run() {
            try {
                if (ModuleProxy.this.mRemoteModule != null) {
                    ModuleProxy.this.mRemoteModule.cmd(this.cmdCode, this.ints, this.flts, this.strs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static {
        mHandlerThreadCmd = null;
        mHandlerCmd = null;
        mHandlerThreadCmd = new HandlerThread("ModuleProxy-CMD-Thread");
        mHandlerThreadCmd.start();
        mHandlerCmd = new Handler(mHandlerThreadCmd.getLooper());
    }

    public void cmd(int i) {
        cmd(i, (int[]) null, (float[]) null, (String[]) null);
    }

    public void cmd(int i, int i2) {
        cmd(i, new int[]{i2}, (float[]) null, (String[]) null);
    }

    public void cmd(int i, int i2, int i3) {
        cmd(i, new int[]{i2, i3}, (float[]) null, (String[]) null);
    }

    public void cmd(int i, int i2, int i3, int i4) {
        cmd(i, new int[]{i2, i3, i4}, (float[]) null, (String[]) null);
    }

    public void cmd(int i, String str) {
        cmd(i, (int[]) null, (float[]) null, new String[]{str});
    }

    public void cmd(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (mHandlerCmd != null) {
            mHandlerCmd.post(new Runnable_Cmd(i, iArr, fArr, strArr));
        }
    }

    public ModuleObject get(int i, int i2) {
        IRemoteModule iRemoteModule = this.mRemoteModule;
        if (iRemoteModule == null) {
            return null;
        }
        try {
            return iRemoteModule.get(i, new int[]{i2}, (float[]) null, (String[]) null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ModuleObject get(int i, int[] iArr, float[] fArr, String[] strArr) {
        IRemoteModule iRemoteModule = this.mRemoteModule;
        if (iRemoteModule != null) {
            try {
                return iRemoteModule.get(i, iArr, fArr, strArr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getI(int i, int i2) {
        IRemoteModule iRemoteModule = this.mRemoteModule;
        if (iRemoteModule == null) {
            return i2;
        }
        try {
            ModuleObject moduleObject = iRemoteModule.get(i, (int[]) null, (float[]) null, (String[]) null);
            return (moduleObject == null || moduleObject.ints == null || moduleObject.ints.length < 1) ? i2 : moduleObject.ints[0];
        } catch (Exception e) {
            e.printStackTrace();
            return i2;
        }
    }

    public int getI(int i, int i2, int i3) {
        IRemoteModule iRemoteModule = this.mRemoteModule;
        if (iRemoteModule == null) {
            return i3;
        }
        try {
            ModuleObject moduleObject = iRemoteModule.get(i, new int[]{i2}, (float[]) null, (String[]) null);
            return (moduleObject == null || moduleObject.ints == null || moduleObject.ints.length < 1) ? i3 : moduleObject.ints[0];
        } catch (Exception e) {
            e.printStackTrace();
            return i3;
        }
    }

    public IRemoteModule getRemoteModule() {
        return this.mRemoteModule;
    }

    public String getS(int i, int i2) {
        IRemoteModule iRemoteModule = this.mRemoteModule;
        if (iRemoteModule == null) {
            return null;
        }
        try {
            ModuleObject moduleObject = iRemoteModule.get(i, new int[]{i2}, (float[]) null, (String[]) null);
            if (moduleObject == null || moduleObject.strs == null || moduleObject.strs.length < 1) {
                return null;
            }
            return moduleObject.strs[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getS(int i, int i2, int i3) {
        IRemoteModule iRemoteModule = this.mRemoteModule;
        if (iRemoteModule == null) {
            return null;
        }
        try {
            ModuleObject moduleObject = iRemoteModule.get(i, new int[]{i2, i3}, (float[]) null, (String[]) null);
            if (moduleObject == null || moduleObject.strs == null || moduleObject.strs.length < 1) {
                return null;
            }
            return moduleObject.strs[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void register(IModuleCallback iModuleCallback, int i, int i2) {
        IRemoteModule iRemoteModule = this.mRemoteModule;
        if (iRemoteModule != null) {
            try {
                iRemoteModule.register(iModuleCallback, i, i2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setRemoteModule(IRemoteModule iRemoteModule) {
        this.mRemoteModule = iRemoteModule;
    }

    public void unregister(IModuleCallback iModuleCallback, int i) {
        IRemoteModule iRemoteModule = this.mRemoteModule;
        if (iRemoteModule != null) {
            try {
                iRemoteModule.unregister(iModuleCallback, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
