package com.syu.ipcself;

import com.syu.ipc.IRemoteToolkit;

public interface IConnStateListener {
    void onConnected(IRemoteToolkit iRemoteToolkit);

    void onDisconnected();
}
