package com.syu.ipcself;

import android.os.RemoteCallbackList;
import com.syu.ipc.IModuleCallback;
import java.util.BitSet;

public class ModuleCallbackList extends RemoteCallbackList<IModuleCallback> {
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void update(com.syu.ipc.IModuleCallback r3, int r4, int r5) {
        /*
            if (r3 == 0) goto L_0x000f
            monitor-enter(r3)
            r0 = 1
            int[] r0 = new int[r0]     // Catch:{ RemoteException -> 0x0010 }
            r1 = 0
            r0[r1] = r5     // Catch:{ RemoteException -> 0x0010 }
            r1 = 0
            r2 = 0
            r3.update(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0010 }
        L_0x000e:
            monitor-exit(r3)     // Catch:{ all -> 0x0015 }
        L_0x000f:
            return
        L_0x0010:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0015 }
            goto L_0x000e
        L_0x0015:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0015 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.ipcself.ModuleCallbackList.update(com.syu.ipc.IModuleCallback, int, int):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void update(com.syu.ipc.IModuleCallback r3, int r4, int r5, int r6) {
        /*
            if (r3 == 0) goto L_0x0012
            monitor-enter(r3)
            r0 = 2
            int[] r0 = new int[r0]     // Catch:{ RemoteException -> 0x0013 }
            r1 = 0
            r0[r1] = r5     // Catch:{ RemoteException -> 0x0013 }
            r1 = 1
            r0[r1] = r6     // Catch:{ RemoteException -> 0x0013 }
            r1 = 0
            r2 = 0
            r3.update(r4, r0, r1, r2)     // Catch:{ RemoteException -> 0x0013 }
        L_0x0011:
            monitor-exit(r3)     // Catch:{ all -> 0x0018 }
        L_0x0012:
            return
        L_0x0013:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0018 }
            goto L_0x0011
        L_0x0018:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0018 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.ipcself.ModuleCallbackList.update(com.syu.ipc.IModuleCallback, int, int, int):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void update(com.syu.ipc.IModuleCallback r4, int r5, int r6, java.lang.String r7) {
        /*
            if (r4 == 0) goto L_0x0014
            monitor-enter(r4)
            r0 = 1
            int[] r0 = new int[r0]     // Catch:{ RemoteException -> 0x0015 }
            r1 = 0
            r0[r1] = r6     // Catch:{ RemoteException -> 0x0015 }
            r1 = 0
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ RemoteException -> 0x0015 }
            r3 = 0
            r2[r3] = r7     // Catch:{ RemoteException -> 0x0015 }
            r4.update(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0015 }
        L_0x0013:
            monitor-exit(r4)     // Catch:{ all -> 0x001a }
        L_0x0014:
            return
        L_0x0015:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x001a }
            goto L_0x0013
        L_0x001a:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x001a }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.ipcself.ModuleCallbackList.update(com.syu.ipc.IModuleCallback, int, int, java.lang.String):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void update(com.syu.ipc.IModuleCallback r4, int r5, java.lang.String r6) {
        /*
            if (r4 == 0) goto L_0x000f
            monitor-enter(r4)
            r0 = 0
            r1 = 0
            r2 = 1
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ RemoteException -> 0x0010 }
            r3 = 0
            r2[r3] = r6     // Catch:{ RemoteException -> 0x0010 }
            r4.update(r5, r0, r1, r2)     // Catch:{ RemoteException -> 0x0010 }
        L_0x000e:
            monitor-exit(r4)     // Catch:{ all -> 0x0015 }
        L_0x000f:
            return
        L_0x0010:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x0015 }
            goto L_0x000e
        L_0x0015:
            r0 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0015 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.ipcself.ModuleCallbackList.update(com.syu.ipc.IModuleCallback, int, java.lang.String):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void update(com.syu.ipc.IModuleCallback r2, int r3, int[] r4) {
        /*
            if (r2 == 0) goto L_0x0009
            monitor-enter(r2)
            r0 = 0
            r1 = 0
            r2.update(r3, r4, r0, r1)     // Catch:{ RemoteException -> 0x000a }
        L_0x0008:
            monitor-exit(r2)     // Catch:{ all -> 0x000f }
        L_0x0009:
            return
        L_0x000a:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x000f }
            goto L_0x0008
        L_0x000f:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x000f }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.ipcself.ModuleCallbackList.update(com.syu.ipc.IModuleCallback, int, int[]):void");
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void update(com.syu.ipc.IModuleCallback r1, int r2, int[] r3, float[] r4, java.lang.String[] r5) {
        /*
            if (r1 == 0) goto L_0x0007
            monitor-enter(r1)
            r1.update(r2, r3, r4, r5)     // Catch:{ RemoteException -> 0x0008 }
        L_0x0006:
            monitor-exit(r1)     // Catch:{ all -> 0x000d }
        L_0x0007:
            return
        L_0x0008:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x000d }
            goto L_0x0006
        L_0x000d:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x000d }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.ipcself.ModuleCallbackList.update(com.syu.ipc.IModuleCallback, int, int[], float[], java.lang.String[]):void");
    }

    public synchronized boolean register(IModuleCallback iModuleCallback) {
        return super.register(iModuleCallback);
    }

    public synchronized boolean unregister(IModuleCallback iModuleCallback) {
        return super.unregister(iModuleCallback);
    }

    public void update(int i, int i2) {
        update(i, new int[]{i2}, (float[]) null, (String[]) null);
    }

    public synchronized void update(int i, int[] iArr, float[] fArr, String[] strArr) {
        int beginBroadcast = beginBroadcast();
        for (int i2 = 0; i2 < beginBroadcast; i2++) {
            if (((BitSet) getBroadcastCookie(i2)).get(i)) {
                IModuleCallback iModuleCallback = (IModuleCallback) getBroadcastItem(i2);
                synchronized (iModuleCallback) {
                    try {
                        iModuleCallback.update(i, iArr, fArr, strArr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        finishBroadcast();
    }
}
