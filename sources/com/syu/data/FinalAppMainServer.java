package com.syu.data;

import com.syu.util.ConnectionKeepAlive;

public class FinalAppMainServer {
    public static final String ACTION_EASYCONN_APP_QUIT = "net.easyconn.app.quit";
    public static final String ACTION_EASYCONN_BT_A2DP_ACQUIRE = "net.easyconn.a2dp.acquire";
    public static final String ACTION_EASYCONN_BT_A2DP_RELEASE = "net.easyconn.a2dp.release";
    public static final String ACTION_EASYCONN_BT_CHECKSTATUS = "net.easyconn.bt.checkstatus";
    public static final String ACTION_ECAR = "com.android.ecar.send";
    public static final String ACTION_GD_KEY = "android.intent.action.ACTION_KEY_VALUE";
    public static final String ACTION_GD_KEY_VALUE = "extra_key_value";
    public static final String ACTION_GD_MODE_CHANGE = "com.autonavi.xm.action.CAR_MODE_CHANGE";
    public static final String ACTION_GD_MODE_CHANGE_VALUE = "car_mode";
    public static final String ACTION_GD_NAVI_ENTER = "com.autonavi.action.NAVIGATION_STATUP_FINISH";
    public static final String ACTION_GD_NAVI_QUIT = "com.autonavi.action.NAVIGATION_QUIT";
    public static final String ACTION_GD_POWER_OFF = "android.intent.action.POWER_OFF";
    public static final String ACTION_GD_TELE_NAVIGATION = "com.autonavi.action.TELE_NAVIGATION";
    public static final String ACTION_GLSX_NAVI_VOIP = "com.glsx.navi.voip";
    public static final String ACTION_OBD_VALUE = "com.android.SYSOBD";
    public static final String ACTION_SYSTEMUI_REMOVE = "com.fyt.systemui.remove";
    public static final String ACTION_ZLINK = "com.zjinnova.zlink";
    public static final String APP_EASYCONN = "net.easyconn";
    public static final ConnectionKeepAlive CONNECTION_AV = new ConnectionKeepAlive("com.syu.av.keepAlive");
    public static final ConnectionKeepAlive CONNECTION_CANBUS = new ConnectionKeepAlive("com.syu.canbus.keepAlive");
    public static final ConnectionKeepAlive CONNECTION_SUB_SERVER = new ConnectionKeepAlive("com.syu.ss.keepAlive");
    public static final ConnectionKeepAlive CONNECTION_UI_SERVER = new ConnectionKeepAlive("com.syu.us.keepAlive");
}
