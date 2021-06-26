package com.syu.util;

public interface InterfaceApp {
    void cmdObdFlagStop(int i);

    int getCameraId();

    boolean isAppTop();

    void notify_startCamera();

    void notify_stopCamera();

    void onConnected_Main();

    void onConnected_Sound();

    void requestAppIdRight();

    void setCameraCallBack();

    void setPreviewFormat();

    String updateOsdInfo_Dvd(int[] iArr);
}
