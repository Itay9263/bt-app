package com.syu.app.ipc;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.syu.app.App;
import com.syu.app.MyUiItem;
import com.syu.bt.act.InterfaceBt;
import com.syu.bt.ctrl.JPinBar;
import com.syu.bt.ctrl.JRotView;
import com.syu.bt.ctrl.JSwitchButon2;
import com.syu.bt.page.Page_Av;
import com.syu.bt.page.Page_Av_HalfScreen;
import com.syu.bt.page.Page_Callin;
import com.syu.bt.page.Page_Callin_HalfScreen;
import com.syu.bt.page.Page_Callin_HideKey;
import com.syu.bt.page.Page_Callin_ShowCall;
import com.syu.bt.page.Page_Callin_ShowCall_HalfScreen;
import com.syu.bt.page.Page_Callin_ShowDial;
import com.syu.bt.page.Page_Callin_ShowDial_HalfScreen;
import com.syu.bt.page.Page_Callin_ShowKey;
import com.syu.bt.page.Page_Callin_ShowKey_HalfScreen;
import com.syu.bt.page.Page_Contact;
import com.syu.bt.page.Page_Dial;
import com.syu.bt.page.Page_Dial_HalfScreen;
import com.syu.bt.page.Page_History;
import com.syu.bt.page.Page_Menu;
import com.syu.bt.page.Page_Pair;
import com.syu.bt.page.Page_Set;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JSwitchButton;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.ipcself.module.unicar.UniCar;
import com.syu.util.IpcUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Ipc_NewNotifyPage {
    List<SparseArray<String>> listContact = new ArrayList();
    List<SparseArray<String>> listContactDownload = new ArrayList();
    boolean zzz = true;

    private String getLauncherPackageName(Context context) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        ResolveInfo resolveActivity = context.getPackageManager().resolveActivity(intent, 0);
        if (resolveActivity == null || resolveActivity.activityInfo == null || resolveActivity.activityInfo.packageName.equals("android")) {
            return null;
        }
        return resolveActivity.activityInfo.packageName;
    }

    private boolean isExitMusicAndTimeWidget() {
        for (PackageInfo packageInfo : App.getApp().getPackageManager().getInstalledPackages(0)) {
            if ("com.syu.widget".equals(packageInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    private void sendService(Bundle bundle) {
        if (isExitMusicAndTimeWidget()) {
            Intent intent = new Intent();
            intent.putExtras(bundle);
            intent.setAction("com.syu.widget.BtavService");
            intent.setPackage("com.syu.widget");
            App.getApp().startServiceSafely(intent);
        }
        String launcherPackageName = getLauncherPackageName(App.getApp());
        if (launcherPackageName != null) {
            Intent intent2 = new Intent();
            intent2.putExtras(bundle);
            intent2.setAction("com.syu.widget.BtavService");
            intent2.setPackage(launcherPackageName);
            App.getApp().startServiceSafely(intent2);
        }
        if (App.mStrCustomer.equalsIgnoreCase("TZY_UI5") && isExitTzyApplication()) {
            Intent intent3 = new Intent();
            intent3.putExtras(bundle);
            intent3.setAction("com.syu.widget.BtavService");
            intent3.setPackage("cn.teyes.online");
            App.getApp().startServiceSafely(intent3);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isExitTzyApplication() {
        for (PackageInfo packageInfo : App.getApp().getPackageManager().getInstalledPackages(0)) {
            if ("cn.teyes.online".equals(packageInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isStateValid() {
        switch (Bt.DATA[9]) {
            case 5:
                switch (Bt.sLastPhoneState) {
                    case 3:
                    case 4:
                        return true;
                    default:
                        return false;
                }
            default:
                return true;
        }
    }

    public void onNotify(Page_Av page_Av, int i, int[] iArr, float[] fArr, String[] strArr) {
        boolean z;
        boolean z2 = true;
        switch (i) {
            case 0:
                String str = Bt.sId3Title;
                JText jText = (JText) page_Av.getPage().getChildViewByid(218);
                if (!IpcObj.isDisConnect()) {
                    if (jText != null) {
                        jText.setText(str);
                        JText jText2 = (JText) page_Av.getPage().getChildViewByid(223);
                        if (jText2 != null) {
                            jText2.setText(jText.getText());
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("btav", str);
                    sendService(bundle);
                    return;
                } else if (jText != null) {
                    jText.setText(App.getApp().getString("bt_disconnected"));
                    return;
                } else {
                    return;
                }
            case 1:
                String str2 = Bt.sId3Artist;
                JText jText3 = (JText) page_Av.getPage().getChildViewByid(219);
                if (jText3 != null) {
                    jText3.setText(str2);
                }
                Bundle bundle2 = new Bundle();
                bundle2.putString("btav_art", str2);
                sendService(bundle2);
                return;
            case 2:
                App.iBtAv_TotalTime = Bt.DATA[i];
                String valueOf = String.valueOf(App.iBtAv_TotalTime);
                Bundle bundle3 = new Bundle();
                bundle3.putString("btav_totaltime", valueOf);
                sendService(bundle3);
                if (page_Av != null) {
                    page_Av.updateSeekbar(App.iBtAv_TotalTime, true);
                    return;
                }
                return;
            case 9:
                if (IpcObj.isDisConnect()) {
                    JText jText4 = (JText) page_Av.getPage().getChildViewByid(218);
                    if (jText4 != null) {
                        jText4.setText(FinalChip.BSP_PLATFORM_Null);
                    }
                    JText jText5 = (JText) page_Av.getPage().getChildViewByid(219);
                    if (jText5 != null) {
                        jText5.setText(FinalChip.BSP_PLATFORM_Null);
                    }
                    View childViewByid = page_Av.getPage().getChildViewByid(225);
                    if (childViewByid != null) {
                        childViewByid.setVisibility(8);
                    }
                    page_Av.misAvStop = false;
                }
                if (page_Av != null) {
                    page_Av.updateBtConnectTip();
                    return;
                }
                return;
            case 13:
                int i2 = Bt.DATA[i];
                if (IpcObj.isDisConnect() || i2 != 1) {
                    App.bBtAvPlayState = false;
                    z = false;
                } else {
                    App.bBtAvPlayState = true;
                    z = true;
                }
                if (page_Av.mBtnPlayPause != null) {
                    page_Av.updateBtnPlayPause();
                }
                if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
                    if (z) {
                        page_Av.startAnim();
                    } else {
                        page_Av.stopAnim();
                    }
                }
                switch (App.mIdCustomer) {
                    case 66:
                        Bundle bundle4 = new Bundle();
                        bundle4.putString("btav_playpause", String.valueOf(i2));
                        Intent intent = new Intent();
                        intent.putExtras(bundle4);
                        intent.setAction("com.syu.widget.BtavService");
                        intent.setPackage("com.android.launcher3");
                        App.getApp().startService(intent);
                        break;
                }
                JRotView jRotView = (JRotView) page_Av.getPage().getChildViewByid(207);
                JPinBar jPinBar = (JPinBar) page_Av.getPage().getChildViewByid(208);
                if (jRotView != null) {
                    jRotView.setRun(z);
                }
                if (jPinBar != null) {
                    jPinBar.setRun(z);
                }
                if (i2 != 0 || !page_Av.misAvStop) {
                    z2 = false;
                }
                View childViewByid2 = page_Av.getPage().getChildViewByid(225);
                if (childViewByid2 == null) {
                    return;
                }
                if (z2) {
                    childViewByid2.setVisibility(8);
                    return;
                } else {
                    childViewByid2.setVisibility(0);
                    return;
                }
            case 26:
                String str3 = Bt.sID3Album;
                JText jText6 = (JText) page_Av.getPage().getChildViewByid(220);
                if (jText6 != null) {
                    jText6.setText(str3);
                    return;
                }
                return;
            case 28:
                App.iBtAv_CurTime = Bt.DATA[i];
                String valueOf2 = String.valueOf(App.iBtAv_CurTime);
                Bundle bundle5 = new Bundle();
                bundle5.putString("btav_curtime", valueOf2);
                sendService(bundle5);
                if (page_Av != null && App.iBtAv_CurTime <= App.iBtAv_TotalTime) {
                    page_Av.handleSeekPos(App.iBtAv_CurTime);
                    return;
                }
                return;
            case 34:
                JText jText7 = (JText) page_Av.getPage().getChildViewByid(222);
                switch (Bt.DATA[i]) {
                    case 0:
                        if (jText7 != null) {
                            jText7.setText(App.getApp().getString(App.mStrStates[0]));
                            return;
                        }
                        return;
                    case 1:
                        if (jText7 != null) {
                            jText7.setText(App.getApp().getString(App.mStrStates[1]));
                            return;
                        }
                        return;
                    case 2:
                        if (jText7 != null) {
                            jText7.setText(App.getApp().getString(App.mStrStates[2]));
                            return;
                        }
                        return;
                    case 3:
                        if (jText7 != null) {
                            jText7.setText(App.getApp().getString("bt_cuting"));
                            return;
                        }
                        return;
                    default:
                        return;
                }
            case 44:
                if (page_Av != null) {
                    page_Av.updatePhoneBattery(App.mPhoneBatterty);
                    return;
                }
                return;
            case 47:
                if (page_Av != null) {
                    page_Av.updatePhoneAptx(App.mPhoneCoding);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onNotify(Page_Av_HalfScreen page_Av_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 0:
                JText jText = (JText) page_Av_HalfScreen.getPage().getChildViewByid(223);
                if (jText == null) {
                    return;
                }
                if (IpcObj.isDisConnect()) {
                    jText.setText(App.getApp().getString("bt_disconnected"));
                    return;
                } else {
                    jText.setText(Bt.sId3Title);
                    return;
                }
            case 1:
                JText jText2 = (JText) page_Av_HalfScreen.getPage().getChildViewByid(224);
                if (jText2 != null) {
                    jText2.setText(Bt.sId3Artist);
                    return;
                }
                return;
            case 9:
                if (IpcObj.isDisConnect()) {
                    JText jText3 = (JText) page_Av_HalfScreen.getPage().getChildViewByid(223);
                    if (jText3 != null) {
                        jText3.setText(FinalChip.BSP_PLATFORM_Null);
                    }
                    JText jText4 = (JText) page_Av_HalfScreen.getPage().getChildViewByid(224);
                    if (jText4 != null) {
                        jText4.setText(FinalChip.BSP_PLATFORM_Null);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onNotify(Page_Callin page_Callin, int i, int[] iArr, float[] fArr, String[] strArr) {
        JText jText;
        switch (i) {
            case 8:
                JText jText2 = (JText) page_Callin.getPage().getChildViewByid(78);
                if (jText2 != null) {
                    if (App.bCallinHideKeyTextFlag) {
                        jText2.setVisibility(8);
                    } else {
                        jText2.setVisibility(0);
                        jText2.setText(Bt.sPhoneNumber);
                    }
                }
                JText jText3 = (JText) page_Callin.getPage().getChildViewByid(79);
                if (jText3 != null) {
                    if (App.bCallinHideKeyTextFlag) {
                        jText3.setVisibility(8);
                    } else {
                        jText3.setVisibility(0);
                    }
                    if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                        jText3.setText(FinalChip.BSP_PLATFORM_Null);
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    arrayList.addAll(App.mBtInfo.mListContact);
                    App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText3, FinalChip.BSP_PLATFORM_Null, true), false, 5);
                    return;
                }
                return;
            case 9:
                JText jText4 = (JText) page_Callin.getPage().getChildViewByid(81);
                int i2 = Bt.DATA[9];
                if (jText4 != null) {
                    if (App.bCallinHideKeyTextFlag) {
                        jText4.setVisibility(8);
                    } else {
                        jText4.setVisibility(0);
                    }
                    if (i2 >= 0 && i2 <= 6) {
                        jText4.setText(App.getApp().getString(App.mStrStates[i2]));
                    }
                }
                if (!App.bShowKeyFlag) {
                    switch (Bt.DATA[9]) {
                        case 3:
                            page_Callin.Switch2Keyboard(1);
                            break;
                        case 4:
                            page_Callin.Switch2Keyboard(2);
                            break;
                        case 5:
                            page_Callin.Switch2Keyboard(3);
                            break;
                    }
                }
                JText jText5 = (JText) page_Callin.getPage().getChildViewByid(80);
                if (jText5 == null) {
                    return;
                }
                if (i2 == 5 || (i2 == 4 && Bt.DATA[20] > 0)) {
                    jText5.setVisibility(0);
                    return;
                } else {
                    jText5.setVisibility(4);
                    return;
                }
            case 12:
                JText jText6 = (JText) page_Callin.getPage().getChildViewByid(80);
                if (jText6 != null) {
                    int i3 = Bt.DATA[9];
                    if (i3 == 5 || (i3 == 4 && Bt.DATA[20] > 0)) {
                        if (App.bCallinHideKeyTextFlag) {
                            jText6.setVisibility(8);
                        } else {
                            jText6.setVisibility(0);
                        }
                    }
                    int i4 = (Bt.DATA[i] + 100) / 1000;
                    jText6.setText(String.format(Locale.US, "%02d:%02d", new Object[]{Integer.valueOf(i4 / 60), Integer.valueOf(i4 % 60)}));
                    return;
                }
                return;
            case 18:
                if (IpcUtil.intsOk(iArr, 1)) {
                    JButton jButton = (JButton) page_Callin.getPage().getChildViewByid(118);
                    if (App.mIdCustomer != 41 || Bt.DATA[9] != 5 || jButton == null) {
                        return;
                    }
                    if (Bt.DATA[i] == 0) {
                        jButton.setFocus(false);
                        return;
                    } else {
                        jButton.setFocus(true);
                        return;
                    }
                } else {
                    return;
                }
            case 21:
                if (Bt.DATA[21] > 0 && Bt.DATA[9] == 4 && (jText = (JText) page_Callin.getPage().getChildViewByid(80)) != null) {
                    int i5 = (Bt.DATA[21] + 200) / 1000;
                    if (i5 > 0) {
                        jText.setText(String.valueOf(App.getApp().getString("bt_auto_suspend")) + " " + i5);
                        return;
                    } else {
                        jText.setText(FinalChip.BSP_PLATFORM_Null);
                        return;
                    }
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Callin_HalfScreen page_Callin_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        JText jText;
        switch (i) {
            case 8:
                JText jText2 = (JText) page_Callin_HalfScreen.getPage().getChildViewByid(82);
                if (jText2 != null) {
                    jText2.setText(Bt.sPhoneNumber);
                }
                JText jText3 = (JText) page_Callin_HalfScreen.getPage().getChildViewByid(83);
                if (jText3 == null) {
                    return;
                }
                if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                    jText3.setText(FinalChip.BSP_PLATFORM_Null);
                    return;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(App.mBtInfo.mListContact);
                App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText3, FinalChip.BSP_PLATFORM_Null, true), false, 5);
                return;
            case 9:
                JText jText4 = (JText) page_Callin_HalfScreen.getPage().getChildViewByid(85);
                int i2 = Bt.DATA[9];
                if (jText4 != null && i2 >= 0 && i2 <= 6) {
                    jText4.setText(App.getApp().getString(App.mStrStates[i2]));
                }
                switch (Bt.DATA[9]) {
                    case 3:
                        page_Callin_HalfScreen.Switch2Key(1);
                        break;
                    case 4:
                        page_Callin_HalfScreen.Switch2Key(2);
                        break;
                    case 5:
                        page_Callin_HalfScreen.Switch2Key(3);
                        break;
                }
                JText jText5 = (JText) page_Callin_HalfScreen.getPage().getChildViewByid(84);
                if (jText5 == null) {
                    return;
                }
                if (i2 == 5 || (i2 == 4 && Bt.DATA[20] > 0)) {
                    jText5.setVisibility(0);
                    return;
                } else {
                    jText5.setVisibility(4);
                    return;
                }
            case 12:
                JText jText6 = (JText) page_Callin_HalfScreen.getPage().getChildViewByid(84);
                if (jText6 != null) {
                    int i3 = (Bt.DATA[i] + 100) / 1000;
                    jText6.setText(String.format(Locale.US, "%02d:%02d", new Object[]{Integer.valueOf(i3 / 60), Integer.valueOf(i3 % 60)}));
                    return;
                }
                return;
            case 18:
                JButton jButton = (JButton) page_Callin_HalfScreen.getPage().getChildViewByid(118);
                if (App.mIdCustomer != 41 || Bt.DATA[9] != 5 || jButton == null) {
                    return;
                }
                if (Bt.DATA[i] == 0) {
                    jButton.setFocus(false);
                    return;
                } else {
                    jButton.setFocus(true);
                    return;
                }
            case 21:
                if (Bt.DATA[21] > 0 && Bt.DATA[9] == 4 && (jText = (JText) page_Callin_HalfScreen.getPage().getChildViewByid(84)) != null) {
                    int i4 = (Bt.DATA[21] + 200) / 1000;
                    if (i4 > 0) {
                        jText.setText(String.valueOf(App.getApp().getString("bt_auto_suspend")) + " " + i4);
                        return;
                    } else {
                        jText.setText(FinalChip.BSP_PLATFORM_Null);
                        return;
                    }
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Callin_HideKey page_Callin_HideKey, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 18:
                JButton jButton = (JButton) page_Callin_HideKey.getPage().getChildViewByid(118);
                if (App.mIdCustomer != 41 || Bt.DATA[9] != 5 || jButton == null) {
                    return;
                }
                if (Bt.DATA[i] == 0) {
                    jButton.setFocus(false);
                    return;
                } else {
                    jButton.setFocus(true);
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Callin_ShowCall page_Callin_ShowCall, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 9:
                if (App.mIdCustomer == 55) {
                    page_Callin_ShowCall.updateSwipeCallingBtns();
                    return;
                }
                return;
            case 18:
                JButton jButton = (JButton) page_Callin_ShowCall.getPage().getChildViewByid(118);
                if (App.mIdCustomer != 41 || Bt.DATA[9] != 5 || jButton == null) {
                    return;
                }
                if (Bt.DATA[i] == 0) {
                    jButton.setFocus(false);
                    return;
                } else {
                    jButton.setFocus(true);
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Callin_ShowCall_HalfScreen page_Callin_ShowCall_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 18:
                JButton jButton = (JButton) page_Callin_ShowCall_HalfScreen.getPage().getChildViewByid(118);
                if (App.mIdCustomer != 41 || Bt.DATA[9] != 5 || jButton == null) {
                    return;
                }
                if (Bt.DATA[i] == 0) {
                    jButton.setFocus(false);
                    return;
                } else {
                    jButton.setFocus(true);
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Callin_ShowDial page_Callin_ShowDial, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 18:
                JButton jButton = (JButton) page_Callin_ShowDial.getPage().getChildViewByid(118);
                if (App.mIdCustomer != 41 || Bt.DATA[9] != 5 || jButton == null) {
                    return;
                }
                if (Bt.DATA[i] == 0) {
                    jButton.setFocus(false);
                    return;
                } else {
                    jButton.setFocus(true);
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Callin_ShowDial_HalfScreen page_Callin_ShowDial_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 18:
                JButton jButton = (JButton) page_Callin_ShowDial_HalfScreen.getPage().getChildViewByid(118);
                if (App.mIdCustomer != 41 || Bt.DATA[9] != 5 || jButton == null) {
                    return;
                }
                if (Bt.DATA[i] == 0) {
                    jButton.setFocus(false);
                    return;
                } else {
                    jButton.setFocus(true);
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Callin_ShowKey page_Callin_ShowKey, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 18:
                JButton jButton = (JButton) page_Callin_ShowKey.getPage().getChildViewByid(118);
                if (App.mIdCustomer != 41 || Bt.DATA[9] != 5 || jButton == null) {
                    return;
                }
                if (Bt.DATA[i] == 0) {
                    jButton.setFocus(false);
                    return;
                } else {
                    jButton.setFocus(true);
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Callin_ShowKey_HalfScreen page_Callin_ShowKey_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 18:
                JButton jButton = (JButton) page_Callin_ShowKey_HalfScreen.getPage().getChildViewByid(118);
                if (App.mIdCustomer != 41 || Bt.DATA[9] != 5 || jButton == null) {
                    return;
                }
                if (Bt.DATA[i] == 0) {
                    jButton.setFocus(false);
                    return;
                } else {
                    jButton.setFocus(true);
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Contact page_Contact, int i, int[] iArr, float[] fArr, String[] strArr) {
        boolean z = true;
        switch (i) {
            case 22:
                if (IpcObj.isConnect() && IpcUtil.strsOk(strArr, 2) && App.bDownloading) {
                    App.bHasDownBook = true;
                    String str = strArr[0];
                    String str2 = strArr[1];
                    if (str2.indexOf("+") == 0) {
                        str2 = String.valueOf(str2.substring(0, 1)) + str2.substring(1, str2.length()).replaceAll("\\D", FinalChip.BSP_PLATFORM_Null);
                    } else {
                        z = false;
                    }
                    if (!z) {
                        str2 = str2.replaceAll("[\\D]", FinalChip.BSP_PLATFORM_Null);
                    }
                    if (str.endsWith("/H")) {
                        str = str.substring(0, str.indexOf("/"));
                    } else if (str.endsWith("/M")) {
                        str = str.substring(0, str.indexOf("/"));
                    }
                    App.startThread(App.StrThreadDownContact, new bp(TextUtils.isEmpty(str) ? str2 : str, str2, this.listContact, this.listContactDownload, page_Contact), false, 10);
                    return;
                }
                return;
            case 29:
                if (Bt.DATA[i] == 2) {
                    if (!App.bDownloading) {
                        this.listContactDownload.clear();
                        if (App.mBtInfo.mListContactDownload.size() > 0) {
                            this.listContactDownload.addAll(App.mBtInfo.mListContactDownload);
                        }
                        this.listContact.clear();
                        if (App.mBtInfo.mListContact.size() > 0) {
                            this.listContact.addAll(App.mBtInfo.mListContact);
                        }
                    }
                    App.iDownloadCnt = 0;
                    App.bDownloading = true;
                    App.sContactsSaveFlag = false;
                    if (!App.bAutoDownPhoneBook && page_Contact != null) {
                        page_Contact.actBt.uiUtil.a(App.getApp().getString("bt_download"));
                        return;
                    }
                    return;
                } else if (App.bDownloading) {
                    App.bDownloading = false;
                    App.startThread(App.StrThreadDownContact, new bo(page_Contact), false, 10);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Dial page_Dial, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 7:
                JText jText = (JText) page_Dial.getPage().getChildViewByid(59);
                JText jText2 = (JText) page_Dial.getPage().getChildViewByid(69);
                if (jText != null) {
                    String str = Bt.sPhoneName;
                    jText.setText(str);
                    if (jText2 != null) {
                        jText2.setText(str);
                        return;
                    }
                    return;
                }
                return;
            case 8:
                if (bv.h()) {
                    boolean z = false;
                    if (IpcUtil.strsOk(strArr, 2)) {
                        z = true;
                    }
                    if (App.bPop3rdPhone != z) {
                        App.bPop3rdPhone = z;
                        Main.removeRunnable_Ui(App.getApp().mRunnable_updatePhoneView);
                        Main.postRunnable_Ui(false, App.getApp().mRunnable_updatePhoneView, 100);
                    }
                }
                page_Dial.updateThirdPhoneName();
                Bt.sPhoneNumber = bt.a().g(Bt.sPhoneNumber);
                JText jText3 = (JText) page_Dial.getPage().getChildViewByid(56);
                JText jText4 = (JText) page_Dial.getPage().getChildViewByid(62);
                if (jText3 != null) {
                    jText3.setText(Bt.sPhoneNumber);
                }
                if (jText4 != null) {
                    if (bv.h()) {
                        JText jText5 = (JText) page_Dial.getPage().getChildViewByid(63);
                        if (jText5 != null) {
                            jText5.setText(App.sDialInput);
                        }
                        if (!TextUtils.isEmpty(App.sDialInput)) {
                            int lastIndexOf = Bt.sPhoneNumber.lastIndexOf(App.sDialInput);
                            if (lastIndexOf > 0) {
                                jText4.setText(Bt.sPhoneNumber.subSequence(0, lastIndexOf));
                            }
                        } else {
                            jText4.setText(Bt.sPhoneNumber);
                        }
                    } else {
                        jText4.setText(Bt.sPhoneNumber);
                    }
                }
                if (App.bHistoryLogAllFlag) {
                    page_Dial.HideOrShowNumberPad();
                }
                JText jText6 = (JText) page_Dial.getPage().getChildViewByid(57);
                JText jText7 = (JText) page_Dial.getPage().getChildViewByid(64);
                JText jText8 = (JText) page_Dial.getPage().getChildViewByid(65);
                if (jText6 != null || jText7 != null || jText8 != null) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.addAll(App.mBtInfo.mListContact);
                    if (App.mStrCustomer.equalsIgnoreCase("XINGKE")) {
                        App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText6, jText7, jText8, Bt.sPhoneNumber, true), false, 5);
                        return;
                    } else {
                        App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText6, jText7, jText8, FinalChip.BSP_PLATFORM_Null, true), false, 5);
                        return;
                    }
                } else {
                    return;
                }
            case 9:
                if (bv.c()) {
                    page_Dial.controlDialInfo(Bt.DATA[9]);
                }
                phoneState(page_Dial);
                return;
            case 12:
                JText jText9 = (JText) page_Dial.getPage().getChildViewByid(58);
                JText jText10 = (JText) page_Dial.getPage().getChildViewByid(68);
                if (jText9 != null) {
                    int i2 = (Bt.DATA[i] + 100) / 1000;
                    jText9.setText(String.format(Locale.US, "%02d:%02d", new Object[]{Integer.valueOf(i2 / 60), Integer.valueOf(i2 % 60)}));
                    if (jText10 != null) {
                        jText10.setText(jText9.getText());
                        return;
                    }
                    return;
                }
                return;
            case 14:
                JText jText11 = (JText) page_Dial.getPage().getChildViewByid(60);
                JText jText12 = (JText) page_Dial.getPage().getChildViewByid(70);
                if (jText11 != null) {
                    String str2 = Bt.sDevAddr;
                    if (!TextUtils.isEmpty(Bt.sDevAddr)) {
                        if (Bt.sDevAddr.contains(":") || Bt.sDevAddr.length() != 12) {
                            jText11.setText(str2);
                        } else {
                            jText11.setText(String.valueOf(str2.substring(0, 2)) + ":" + str2.substring(2, 4) + ":" + str2.substring(4, 6) + ":" + str2.substring(6, 8) + ":" + str2.substring(8, 10) + ":" + str2.substring(10, 12));
                        }
                    }
                    if (Bt.DATA[9] == 2 || App.mIdCustomer == 111) {
                        jText11.setVisibility(4);
                    } else if (Bt.DATA[9] == 0) {
                        jText11.setVisibility(0);
                    }
                    if (jText12 != null) {
                        jText12.setText(jText11.getText());
                        jText12.setVisibility(jText11.getVisibility());
                        return;
                    }
                    return;
                }
                return;
            case 15:
                JText jText13 = (JText) page_Dial.getPage().getChildViewByid(264);
                if (jText13 != null) {
                    jText13.setText(Bt.sDevName);
                    return;
                }
                return;
            case 18:
                App.bHFP = Bt.DATA[i] == 1;
                if ((bv.h() || bv.d()) && page_Dial != null) {
                    page_Dial.updateBtnVoiceSwitch(App.bHFP);
                    return;
                }
                return;
            case 21:
                if ((Bt.DATA[21] > 0 || Bt.DATA[9] != 4) && Bt.DATA[21] > 0 && Bt.DATA[9] == 4) {
                    JText jText14 = (JText) page_Dial.getPage().getChildViewByid(58);
                    JText jText15 = (JText) page_Dial.getPage().getChildViewByid(68);
                    if (jText14 != null) {
                        int i3 = (Bt.DATA[21] + 200) / 1000;
                        if (i3 > 0) {
                            jText14.setText(String.valueOf(App.getApp().getString("bt_auto_suspend")) + " " + i3);
                        } else {
                            jText14.setText(FinalChip.BSP_PLATFORM_Null);
                        }
                        if (jText15 != null) {
                            jText15.setText(jText14.getText());
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            case 42:
                App.bPhoneMIC = Bt.DATA[i] == 1;
                JButton jButton = (JButton) page_Dial.getPage().getChildViewByid(119);
                if (jButton != null) {
                    MyUiItem myUiItem = (MyUiItem) jButton.getTag();
                    if (myUiItem.getParaStr() != null) {
                        String strDrawable = App.bPhoneMIC ? myUiItem.getStrDrawable() : myUiItem.getParaStr() != null ? myUiItem.getParaStr()[0] : null;
                        if (jButton.getStrDrawable() != strDrawable && strDrawable != null) {
                            jButton.setStrDrawable(strDrawable, true);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            case 149:
                if (App.mEnableHalfScreen) {
                    page_Dial.HideOrShowNumberPad();
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onNotify(Page_Dial_HalfScreen page_Dial_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        JText jText;
        switch (i) {
            case 7:
                JText jText2 = (JText) page_Dial_HalfScreen.getPage().getChildViewByid(74);
                if (jText2 != null) {
                    jText2.setText(Bt.sPhoneName);
                    return;
                }
                return;
            case 9:
                int i2 = Bt.DATA[9];
                JText jText3 = (JText) page_Dial_HalfScreen.getPage().getChildViewByid(73);
                if (jText3 != null && i2 >= 0 && i2 <= 6) {
                    jText3.setText(App.getApp().getString(App.mStrStates[i2]));
                }
                JText jText4 = (JText) page_Dial_HalfScreen.getPage().getChildViewByid(58);
                if (jText4 != null) {
                    if (i2 == 5 || (i2 == 4 && Bt.DATA[20] > 0)) {
                        jText4.setVisibility(0);
                    } else {
                        jText4.setVisibility(4);
                    }
                }
                JText jText5 = (JText) page_Dial_HalfScreen.getPage().getChildViewByid(74);
                if (jText5 == null) {
                    return;
                }
                if (i2 == 2) {
                    jText5.setVisibility(0);
                    return;
                } else if (i2 == 0) {
                    jText5.setVisibility(8);
                    return;
                } else {
                    return;
                }
            case 14:
                JText jText6 = (JText) page_Dial_HalfScreen.getPage().getChildViewByid(75);
                if (jText6 != null && !TextUtils.isEmpty(Bt.sDevAddr)) {
                    if (Bt.sDevAddr.contains(":") || Bt.sDevAddr.length() != 12) {
                        jText6.setText(Bt.sDevAddr);
                        return;
                    } else {
                        jText6.setText(String.valueOf(Bt.sDevAddr.substring(0, 2)) + ":" + Bt.sDevAddr.substring(2, 4) + ":" + Bt.sDevAddr.substring(4, 6) + ":" + Bt.sDevAddr.substring(6, 8) + ":" + Bt.sDevAddr.substring(8, 10) + ":" + Bt.sDevAddr.substring(10, 12));
                        return;
                    }
                } else {
                    return;
                }
            case 21:
                if (Bt.DATA[21] > 0 && Bt.DATA[9] == 4 && (jText = (JText) page_Dial_HalfScreen.getPage().getChildViewByid(58)) != null) {
                    int i3 = (Bt.DATA[21] + 200) / 1000;
                    if (i3 > 0) {
                        jText.setText(String.valueOf(App.getApp().getString("bt_auto_suspend")) + " " + i3);
                        return;
                    } else {
                        jText.setText(FinalChip.BSP_PLATFORM_Null);
                        return;
                    }
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_History page_History, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 8:
                if (bv.c() && page_History != null) {
                    if (App.needSplice && !TextUtils.isEmpty(Bt.sPhoneNumber) && !Bt.sPhoneNumber.startsWith("+")) {
                        Bt.sPhoneNumber = "+" + Bt.sPhoneNumber;
                    }
                    JText jText = (JText) page_History.getPage().getChildViewByid(63);
                    if (jText != null) {
                        jText.setText(Bt.sPhoneNumber);
                        return;
                    }
                    return;
                }
                return;
            case 29:
                if (Bt.DATA[i] == 4) {
                    App.bDownloadingRecord = true;
                    switch (App.iDownRecordState) {
                        case 2:
                            App.mBtInfo.clearCallLog(1);
                            return;
                        case 3:
                            App.mBtInfo.clearCallLog(3);
                            return;
                        case 255:
                            if (page_History != null) {
                                page_History.actBt.uiUtil.a(App.getApp().getString("bt_download"));
                            }
                            App.mBtInfo.clearCallLog(2);
                            return;
                        default:
                            return;
                    }
                } else if (App.bDownloadingRecord) {
                    App.queryCallLog();
                    switch (App.iDownRecordState) {
                        case 2:
                            App.iDownRecordState = 3;
                            Bt.PROXY.cmd(37);
                            return;
                        case 3:
                            if (page_History != null) {
                                page_History.actBt.uiUtil.a();
                            }
                            App.iDownRecordState = 255;
                            App.bDownloadingRecord = false;
                            return;
                        case 255:
                            App.iDownRecordState = 2;
                            Bt.PROXY.cmd(36);
                            return;
                        default:
                            return;
                    }
                } else {
                    return;
                }
            case 35:
                if (IpcUtil.intsOk(iArr, 1) && IpcUtil.strsOk(strArr, 3)) {
                    ContentValues contentValues = null;
                    switch (iArr[0]) {
                        case 4:
                            contentValues = new ContentValues();
                            contentValues.put("type", 2);
                            contentValues.put("date", Long.valueOf(bt.a().f(strArr[2])));
                            contentValues.put("number", strArr[1]);
                            break;
                        case 5:
                            contentValues = new ContentValues();
                            contentValues.put("type", 1);
                            contentValues.put("date", Long.valueOf(bt.a().f(strArr[2])));
                            contentValues.put("number", strArr[1]);
                            break;
                        case 6:
                            contentValues = new ContentValues();
                            contentValues.put("type", 3);
                            contentValues.put("date", Long.valueOf(bt.a().f(strArr[2])));
                            contentValues.put("number", strArr[1]);
                            break;
                    }
                    if (contentValues != null) {
                        App.mBtInfo.insertCallLog(contentValues, false);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onNotify(Page_Menu page_Menu, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 8:
                JText jText = (JText) page_Menu.getPage().getChildViewByid(62);
                if (App.needSplice && !TextUtils.isEmpty(Bt.sPhoneNumber) && !Bt.sPhoneNumber.startsWith("+")) {
                    Bt.sPhoneNumber = "+" + Bt.sPhoneNumber;
                }
                if (jText != null) {
                    jText.setText(Bt.sPhoneNumber);
                }
                JText jText2 = (JText) page_Menu.getPage().getChildViewByid(64);
                if (jText2 == null) {
                    return;
                }
                if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                    jText2.setText(FinalChip.BSP_PLATFORM_Null);
                    return;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(App.mBtInfo.mListContact);
                App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText2, FinalChip.BSP_PLATFORM_Null, true), false, 5);
                return;
            default:
                return;
        }
    }

    public void onNotify(Page_Pair page_Pair, int i, int[] iArr, float[] fArr, String[] strArr) {
        SparseArray<String> choiceName;
        String str;
        switch (i) {
            case 5:
                if (!App.bShowPairedBt) {
                    return;
                }
                if ((iArr == null || iArr.length >= 1 || strArr == null || strArr.length >= 2) && !TextUtils.isEmpty(strArr[1])) {
                    if (App.mBtInfo.mListHasPaired.size() > 0) {
                        int i2 = 0;
                        while (true) {
                            if (i2 < App.mBtInfo.mListHasPaired.size()) {
                                SparseArray sparseArray = App.mBtInfo.mListHasPaired.get(i2);
                                if (TextUtils.isEmpty((CharSequence) sparseArray.get(290)) || !((String) sparseArray.get(290)).equalsIgnoreCase(strArr[0])) {
                                    i2++;
                                } else {
                                    App.mBtInfo.mListHasPaired.remove(i2);
                                }
                            }
                        }
                    }
                    if (App.mBtInfo.mListPair.size() > 0) {
                        int i3 = 0;
                        while (true) {
                            if (i3 < App.mBtInfo.mListPair.size()) {
                                SparseArray sparseArray2 = App.mBtInfo.mListPair.get(i3);
                                if (TextUtils.isEmpty((CharSequence) sparseArray2.get(283)) || !((String) sparseArray2.get(283)).equalsIgnoreCase(strArr[0])) {
                                    i3++;
                                } else {
                                    App.mBtInfo.mListPair.remove(i3);
                                }
                            }
                        }
                    }
                    if (!bv.h() || !strArr[1].startsWith("OBD")) {
                        SparseArray sparseArray3 = new SparseArray();
                        sparseArray3.put(290, strArr[0]);
                        sparseArray3.put(291, strArr[1]);
                        sparseArray3.put(289, Integer.toString(iArr[0]));
                        App.mBtInfo.mListHasPaired.add(sparseArray3);
                        if (page_Pair != null) {
                            page_Pair.refreshPairedList();
                            page_Pair.refreshList();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            case 9:
                if (page_Pair != null) {
                    if (Ipc_New.isPhoneConnect()) {
                        page_Pair.showConnectTip(false, App.getApp().getString("bt_connected"));
                        page_Pair.resetBtnEnable();
                    }
                    JGridView jGridView = (JGridView) page_Pair.getPage().getChildViewByid(280);
                    if (jGridView != null) {
                        jGridView.notifyDataSetChanged();
                        return;
                    }
                    return;
                }
                return;
            case 24:
                if (strArr == null) {
                    if (page_Pair != null && page_Pair.isStartDiscovey) {
                        page_Pair.showDiscover(false, App.getApp().getString("bt_discover_end"));
                        return;
                    }
                    return;
                } else if (iArr == null || iArr.length >= 1 || strArr == null || strArr.length >= 2) {
                    try {
                        boolean z = false;
                        for (SparseArray<String> sparseArray4 : App.mBtInfo.mListPair) {
                            if (((String) sparseArray4.get(283)).equalsIgnoreCase(strArr[0])) {
                                z = true;
                            }
                        }
                        if (App.mBtInfo.mListHasPaired.size() > 0) {
                            for (SparseArray<String> sparseArray5 : App.mBtInfo.mListHasPaired) {
                                String str2 = (String) sparseArray5.get(290);
                                if (!TextUtils.isEmpty(str2) && str2.equalsIgnoreCase(strArr[0])) {
                                    z = true;
                                }
                            }
                        }
                        if (!z && !TextUtils.isEmpty(strArr[1])) {
                            if (!bv.h() || !strArr[1].startsWith("OBD")) {
                                SparseArray sparseArray6 = new SparseArray();
                                sparseArray6.put(283, strArr[0]);
                                sparseArray6.put(284, strArr[1]);
                                sparseArray6.put(282, Integer.toString(iArr[0]));
                                App.mBtInfo.mListPair.add(sparseArray6);
                                if (page_Pair != null) {
                                    page_Pair.refreshList();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
            case 25:
                if (IpcUtil.intsOk(iArr, 1) && page_Pair != null && (choiceName = page_Pair.getChoiceName(IpcObj.getChoiceAddr())) != null && (str = choiceName.get(282)) != null) {
                    int myParseInt = App.myParseInt(str);
                    if (page_Pair.isSelfLink && myParseInt != 2) {
                        page_Pair.isSelfLink = false;
                        if (Bt.DATA[i] == 1) {
                            cb.a().a(App.getApp().getString("bt_obd_connected"));
                            return;
                        } else if (Bt.DATA[i] == 0) {
                            cb.a().a(App.getApp().getString("bt_obd_disconnected"));
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void onNotify(Page_Set page_Set, int i, int[] iArr, float[] fArr, String[] strArr) {
        SparseArray<String> choiceName;
        String str;
        String string;
        boolean z;
        boolean z2 = true;
        boolean z3 = false;
        switch (i) {
            case 9:
                int i2 = Bt.DATA[9];
                if (page_Set.tvPhoneState != null && i2 >= 0 && i2 <= 6) {
                    page_Set.tvPhoneState.setText(App.getApp().getString(App.mStrStates[i2]));
                }
                if (page_Set.tvLinkName != null) {
                    page_Set.tvLinkName.setText(Bt.sPhoneName);
                }
                JGridView jGridView = (JGridView) page_Set.getPage().getChildViewByid(295);
                if (jGridView != null) {
                    jGridView.notifyDataSetChanged();
                    return;
                }
                return;
            case 15:
                EditText editText = (EditText) page_Set.getPage().getChildViewByid(256);
                if (editText != null) {
                    editText.setText(Bt.sDevName);
                }
                if (page_Set != null) {
                    page_Set.setBtName(Bt.sDevName);
                    return;
                }
                return;
            case 16:
                if (!TextUtils.isEmpty(Bt.sDevPin)) {
                    EditText editText2 = (EditText) page_Set.getPage().getChildViewByid(257);
                    if (editText2 != null) {
                        editText2.setText(Bt.sDevPin);
                    }
                    if (page_Set.mTvPin != null) {
                        page_Set.mTvPin.setText(Bt.sDevPin);
                    }
                    if (page_Set != null) {
                        page_Set.setBtPin(Bt.sDevPin);
                        return;
                    }
                    return;
                }
                return;
            case 19:
                if (page_Set.isClickReSet) {
                    page_Set.actBt.uiUtil.a();
                    cb.a().a(App.getApp().getString("bt_reset_success"));
                    if (!TextUtils.isEmpty(App.strDefaultName) && !App.strDefaultName.equals(Bt.sDevName)) {
                        IpcObj.setDevName(App.strDefaultName);
                    }
                    if (!TextUtils.isEmpty(App.strDefaultPin) && !App.strDefaultPin.equals(Bt.sDevPin)) {
                        IpcObj.setDevPin(App.strDefaultPin);
                    }
                    App.getApp().SaveConfig(App.strDefaultName, App.strDefaultPin);
                }
                page_Set.isClickReSet = false;
                return;
            case 20:
                if (Bt.DATA[i] > 0) {
                    string = App.getApp().getString("bt_opened");
                    z = true;
                } else {
                    string = App.getApp().getString("bt_closed");
                    z = false;
                }
                page_Set.isAutoAnswer = z;
                JText jText = (JText) page_Set.getPage().getChildViewByid(258);
                if (jText != null) {
                    jText.setText(string);
                }
                JSwitchButton jSwitchButton = (JSwitchButton) page_Set.getPage().getChildViewByid(255);
                if (jSwitchButton != null) {
                    jSwitchButton.setChecked(z);
                }
                page_Set.updateAutoSelected(239, z);
                if (!z) {
                    z3 = true;
                }
                page_Set.updateAutoSelected(240, z3);
                page_Set.updateAutoSelected(241, z);
                JSwitchButon2 jSwitchButon2 = (JSwitchButon2) page_Set.getPage().getChildViewByid(340);
                if (jSwitchButon2 != null) {
                    jSwitchButon2.setSwitch(z);
                    return;
                }
                return;
            case 24:
                if (strArr == null) {
                    page_Set.showSetDiscover(false);
                    return;
                } else if (iArr == null || iArr.length >= 1 || strArr == null || strArr.length >= 2) {
                    try {
                        for (SparseArray<String> sparseArray : App.mBtInfo.mListSetPair) {
                            if (((String) sparseArray.get(298)).equalsIgnoreCase(strArr[0])) {
                                z3 = true;
                            }
                        }
                        if (!z3 && !TextUtils.isEmpty(strArr[1])) {
                            SparseArray sparseArray2 = new SparseArray();
                            sparseArray2.put(298, strArr[0]);
                            sparseArray2.put(299, strArr[1]);
                            sparseArray2.put(297, Integer.toString(iArr[0]));
                            App.mBtInfo.mListSetPair.add(sparseArray2);
                            page_Set.resetList_BtSet(App.mBtInfo.mListSetPair);
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }
            case 25:
                if (IpcUtil.intsOk(iArr, 1) && (choiceName = page_Set.getChoiceName(IpcObj.getChoiceAddr())) != null && (str = choiceName.get(297)) != null) {
                    int myParseInt = App.myParseInt(str);
                    if (App.bSelfLink && myParseInt != 2) {
                        App.bSelfLink = false;
                        if (Bt.DATA[i] == 1) {
                            cb.a().a(App.getApp().getString("bt_obd_connected"));
                            return;
                        } else if (Bt.DATA[i] == 0) {
                            cb.a().a(App.getApp().getString("bt_obd_disconnected"));
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case 31:
                JGridView jGridView2 = (JGridView) page_Set.getPage().getChildViewByid(295);
                if (Bt.DATA[i] > 0) {
                    App.bBtPowerOnOff = true;
                    App.bBtInitFlag = true;
                    if (jGridView2 != null) {
                        jGridView2.setVisibility(0);
                    }
                } else {
                    App.bBtPowerOnOff = false;
                    if (jGridView2 != null) {
                        jGridView2.setVisibility(4);
                    }
                }
                if (Bt.DATA[i] <= 0) {
                    z2 = false;
                }
                JSwitchButton jSwitchButton2 = (JSwitchButton) page_Set.getPage().getChildViewByid(259);
                if (jSwitchButton2 != null) {
                    jSwitchButton2.setChecked(z2);
                }
                if (page_Set.mBTOnOff != null) {
                    page_Set.mBTOnOff.setSwitch(App.bBtPowerOnOff);
                }
                if (Main.mConf_PlatForm == 5) {
                    Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                    while (it.hasNext()) {
                        it.next().disPostLoading(3000);
                    }
                    return;
                }
                page_Set.actBt.uiUtil.a();
                return;
            case 33:
                if (iArr != null && iArr.length > 0) {
                    int i3 = iArr[0];
                    App.iRingLevel = iArr[0];
                    String valueOf = String.valueOf(i3);
                    if (page_Set.tvRingSet != null) {
                        page_Set.tvRingSet.setText(valueOf);
                        return;
                    }
                    return;
                }
                return;
            case 36:
                if (iArr != null && iArr.length > 0) {
                    int i4 = iArr[0] + 1;
                    if (i4 > 100 || i4 < 0) {
                        App.mBtUpdating = false;
                        if (page_Set != null) {
                            page_Set.actBt.uiUtil.a();
                            return;
                        }
                        return;
                    }
                    App.mBtUpdating = true;
                    if (page_Set != null) {
                        page_Set.actBt.uiUtil.a(String.valueOf(i4) + "%");
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onNotify_Sound(Page_Set page_Set, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 12:
                if (iArr != null && iArr.length > 0) {
                    App.SB = App.getApp().mIpcObj.getSB();
                    App.iPhoneMicSet = App.SB[2];
                    App.iBtAvMicSet = App.SB[3];
                    String valueOf = String.valueOf(App.iPhoneMicSet);
                    String valueOf2 = String.valueOf(App.iBtAvMicSet);
                    if (page_Set.tvPhoneMicSet != null) {
                        page_Set.tvPhoneMicSet.setText(valueOf);
                    }
                    if (page_Set.tvBtAvMicSet != null) {
                        page_Set.tvBtAvMicSet.setText(valueOf2);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onNotify_UniCar(Page_Dial page_Dial, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 0:
                switch (UniCar.DATA[i]) {
                    case 1:
                        if (Ipc_New.isConnect()) {
                            App.bCutBtByIpPhone = true;
                            Ipc_New.cut();
                            return;
                        }
                        return;
                    case 2:
                        if (App.bCutBtByIpPhone) {
                            App.bCutBtByIpPhone = false;
                            Ipc_New.link();
                        }
                        Toast.makeText(App.getApp(), App.getApp().getString("ipphone_dailwait"), 1).show();
                        page_Dial.actBt.dismissProgressIpPhoneDlg();
                        return;
                    case 3:
                        if (App.bCutBtByIpPhone) {
                            App.bCutBtByIpPhone = false;
                            Ipc_New.link();
                        }
                        Toast.makeText(App.getApp(), App.getApp().getString("ipphone_simstatus"), 1).show();
                        page_Dial.actBt.dismissProgressIpPhoneDlg();
                        return;
                    default:
                        return;
                }
            case 1:
                switch (UniCar.DATA[i]) {
                    case 1:
                        if (App.bCutBtByIpPhone) {
                            App.bCutBtByIpPhone = false;
                            Ipc_New.link();
                        }
                        page_Dial.stopIpPhoneTimer();
                        return;
                    case 2:
                        page_Dial.actBt.dismissProgressIpPhoneDlg();
                        page_Dial.startIpPhoneTimer();
                        return;
                    default:
                        return;
                }
            case 2:
                App.getApp().initIpPhoneTip();
                return;
            case 3:
                phoneState(page_Dial);
                if (IpcUtil.intsOk(iArr, 2)) {
                    Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                    while (it.hasNext()) {
                        it.next().updateSimLayout(iArr[1]);
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void phoneState(Page_Dial page_Dial) {
        boolean z;
        String str = null;
        int i = Bt.DATA[9];
        if (i == 2 && Main.mConf_PlatForm == 5 && App.bFactoryTest) {
            App.bFactoryTest = false;
            App.iOverTime = 0;
            page_Dial.popTestDlg();
        }
        page_Dial.resetThridDial(i);
        page_Dial.resetBtnEnable();
        JText jText = (JText) page_Dial.getPage().getChildViewByid(60);
        JText jText2 = (JText) page_Dial.getPage().getChildViewByid(70);
        if (jText != null) {
            if (Bt.DATA[9] != 0 || App.mIdCustomer == 111) {
                jText.setVisibility(4);
            } else {
                jText.setVisibility(0);
            }
            if (jText2 != null) {
                jText2.setVisibility(jText.getVisibility());
            }
        }
        JText jText3 = (JText) page_Dial.getPage().getChildViewByid(55);
        JText jText4 = (JText) page_Dial.getPage().getChildViewByid(61);
        if (jText3 != null) {
            if (i != 0 || !Ipc_New.isIpPhoneConnect()) {
                z = false;
            } else {
                jText3.setText(App.getApp().getString("ipphone_connected"));
                z = true;
            }
            if (!z && i >= 0 && i <= 6) {
                jText3.setText(App.getApp().getString(App.mStrStates[i]));
            }
            if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
                jText3.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, !IpcObj.isDisConnect() ? App.getApp().getDrawable(App.getApp().getIdDrawable("connect")) : null, (Drawable) null);
            }
            if (jText4 != null) {
                jText4.setText(jText3.getText());
            }
        }
        JText jText5 = (JText) page_Dial.getPage().getChildViewByid(58);
        JText jText6 = (JText) page_Dial.getPage().getChildViewByid(68);
        if (jText5 != null) {
            if (i == 5 || (i == 4 && Bt.DATA[20] > 0)) {
                jText5.setVisibility(0);
            } else {
                jText5.setVisibility(4);
            }
            if (jText6 != null) {
                jText6.setVisibility(jText5.getVisibility());
            }
        }
        JText jText7 = (JText) page_Dial.getPage().getChildViewByid(59);
        JText jText8 = (JText) page_Dial.getPage().getChildViewByid(69);
        if (jText7 != null) {
            if (i == 2) {
                jText7.setVisibility(0);
            } else if (i == 0) {
                jText7.setVisibility(8);
            }
            if (jText8 != null) {
                jText8.setVisibility(jText7.getVisibility());
            }
        }
        Main.removeRunnable_Ui(App.getApp().mRunnable_updatePhoneView);
        Main.postRunnable_Ui(false, App.getApp().mRunnable_updatePhoneView, 100);
        if (Ipc_New.isInCall()) {
            page_Dial.showInCallAnim();
        } else {
            page_Dial.hideInCallAnim();
        }
        if (bv.h()) {
            switch (Bt.DATA[9]) {
                case 0:
                case 2:
                case 3:
                case 4:
                    App.sDialInput = FinalChip.BSP_PLATFORM_Null;
                    break;
            }
            JButton jButton = (JButton) page_Dial.getPage().getChildViewByid(119);
            if (jButton != null) {
                MyUiItem myUiItem = (MyUiItem) jButton.getTag();
                if (myUiItem.getParaStr() != null) {
                    if (App.bPhoneMIC) {
                        str = myUiItem.getStrDrawable();
                    } else if (myUiItem.getParaStr() != null) {
                        str = myUiItem.getParaStr()[0];
                    }
                    if (jButton.getStrDrawable() != str && str != null) {
                        jButton.setStrDrawable(str, true);
                    }
                }
            }
        }
    }
}
