package com.syu.ipcself;

import android.os.IBinder;
import com.syu.ipc.IModuleCallback;
import java.util.BitSet;

public class IpcToolkit {
    private static BitSet getBitSet(ModuleCallbackList moduleCallbackList, IModuleCallback iModuleCallback) {
        BitSet bitSet;
        IBinder asBinder = iModuleCallback.asBinder();
        int beginBroadcast = moduleCallbackList.beginBroadcast();
        int i = 0;
        while (true) {
            if (i >= beginBroadcast) {
                bitSet = null;
                break;
            } else if (asBinder.equals(((IModuleCallback) moduleCallbackList.getBroadcastItem(i)).asBinder())) {
                bitSet = (BitSet) moduleCallbackList.getBroadcastCookie(i);
                break;
            } else {
                i++;
            }
        }
        moduleCallbackList.finishBroadcast();
        return bitSet;
    }

    public static void register(ModuleCallbackList moduleCallbackList, IModuleCallback iModuleCallback, int i) {
        synchronized (moduleCallbackList) {
            BitSet bitSet = getBitSet(moduleCallbackList, iModuleCallback);
            if (bitSet == null) {
                BitSet bitSet2 = new BitSet(128);
                bitSet2.set(i);
                moduleCallbackList.register(iModuleCallback, bitSet2);
            } else {
                bitSet.set(i);
            }
        }
    }

    public static void unregister(ModuleCallbackList moduleCallbackList, IModuleCallback iModuleCallback, int i) {
        synchronized (moduleCallbackList) {
            BitSet bitSet = getBitSet(moduleCallbackList, iModuleCallback);
            if (bitSet != null) {
                bitSet.clear(i);
                if (bitSet.isEmpty()) {
                    moduleCallbackList.unregister(iModuleCallback);
                }
            }
        }
    }
}
