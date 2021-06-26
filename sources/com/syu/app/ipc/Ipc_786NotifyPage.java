package com.syu.app.ipc;

import android.support.v4.os.EnvironmentCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;
import com.syu.app.App;
import com.syu.bt.ctrl.JPinBar;
import com.syu.bt.ctrl.JRotView;
import com.syu.bt.page.Page_Av;
import com.syu.bt.page.Page_Contact;
import com.syu.bt.page.Page_Dial;
import com.syu.bt.page.Page_Menu;
import com.syu.bt.page.Page_Pair;
import com.syu.bt.page.Page_Set;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JSwitchButton;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.util.IpcUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Ipc_786NotifyPage {
    List<SparseArray<String>> listContact = new ArrayList();
    List<SparseArray<String>> listContactDownload = new ArrayList();

    public void onNotify(Page_Av page_Av, int i, int[] iArr, float[] fArr, String[] strArr) {
        boolean z = true;
        switch (i) {
            case 1:
                if (IpcObj.isDisConnect()) {
                    JText jText = (JText) page_Av.getPage().getChildViewByid(218);
                    if (jText != null) {
                        jText.setText(FinalChip.BSP_PLATFORM_Null);
                    }
                    JText jText2 = (JText) page_Av.getPage().getChildViewByid(219);
                    if (jText2 != null) {
                        jText2.setText(FinalChip.BSP_PLATFORM_Null);
                    }
                    View childViewByid = page_Av.getPage().getChildViewByid(225);
                    if (childViewByid != null) {
                        childViewByid.setVisibility(8);
                    }
                    page_Av.misAvStop = false;
                    return;
                }
                return;
            case 4:
                int i2 = Bt.DATA[i];
                boolean z2 = !IpcObj.isDisConnect() && i2 == 1;
                JRotView jRotView = (JRotView) page_Av.getPage().getChildViewByid(207);
                JPinBar jPinBar = (JPinBar) page_Av.getPage().getChildViewByid(208);
                if (jRotView != null) {
                    jRotView.setRun(z2);
                }
                if (jPinBar != null) {
                    jPinBar.setRun(z2);
                }
                if (i2 != 0 || !page_Av.misAvStop) {
                    z = false;
                }
                View childViewByid2 = page_Av.getPage().getChildViewByid(225);
                if (childViewByid2 == null) {
                    return;
                }
                if (z) {
                    childViewByid2.setVisibility(8);
                    return;
                } else {
                    childViewByid2.setVisibility(0);
                    return;
                }
            case 18:
                String str = Bt.sId3Title;
                JText jText3 = (JText) page_Av.getPage().getChildViewByid(218);
                if (jText3 != null) {
                    jText3.setText(str);
                    return;
                }
                return;
            case 19:
                String str2 = Bt.sId3Artist;
                JText jText4 = (JText) page_Av.getPage().getChildViewByid(219);
                if (jText4 != null) {
                    jText4.setText(str2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onNotify(Page_Contact page_Contact, int i, int[] iArr, float[] fArr, String[] strArr) {
        boolean z = true;
        switch (i) {
            case 1:
                if (Bt.DATA[i] == 8) {
                    if (!App.bDownloading) {
                        this.listContactDownload.clear();
                        this.listContactDownload.addAll(App.mBtInfo.mListContactDownload);
                        this.listContact.clear();
                        this.listContact.addAll(App.mBtInfo.mListContact);
                    }
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
            case 13:
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
            default:
                return;
        }
    }

    public void onNotify(Page_Dial page_Dial, int i, int[] iArr, float[] fArr, String[] strArr) {
        JText jText;
        switch (i) {
            case 1:
                phoneState(page_Dial);
                return;
            case 5:
                JText jText2 = (JText) page_Dial.getPage().getChildViewByid(56);
                if (jText2 != null) {
                    jText2.setText(Bt.sPhoneNumber);
                }
                JText jText3 = (JText) page_Dial.getPage().getChildViewByid(57);
                if (jText3 == null) {
                    return;
                }
                if (TextUtils.isEmpty(Bt.sPhoneNumber)) {
                    jText3.setText(FinalChip.BSP_PLATFORM_Null);
                    return;
                }
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(App.mBtInfo.mListContact);
                App.startThread(App.StrThreadGetNameByNumber, new br(arrayList, jText3, App.getApp().getString(EnvironmentCompat.MEDIA_UNKNOWN), false), false, 5);
                return;
            case 9:
                JText jText4 = (JText) page_Dial.getPage().getChildViewByid(58);
                if (jText4 != null) {
                    int i2 = (Bt.DATA[i] + 100) / 1000;
                    jText4.setText(String.format(Locale.US, "%02d:%02d", new Object[]{Integer.valueOf(i2 / 60), Integer.valueOf(i2 % 60)}));
                    return;
                }
                return;
            case 12:
                if ((Bt.DATA[12] > 0 || Bt.DATA[1] != 4) && Bt.DATA[12] > 0 && Bt.DATA[1] == 4 && (jText = (JText) page_Dial.getPage().getChildViewByid(58)) != null) {
                    int i3 = (Bt.DATA[12] + 200) / 1000;
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
            case 14:
                JText jText5 = (JText) page_Dial.getPage().getChildViewByid(59);
                if (jText5 != null) {
                    jText5.setText(Bt.sPhoneName);
                    return;
                }
                return;
            case 15:
                JText jText6 = (JText) page_Dial.getPage().getChildViewByid(60);
                if (jText6 != null) {
                    String str = Bt.sDevAddr;
                    if (!TextUtils.isEmpty(Bt.sDevAddr)) {
                        if (Bt.sDevAddr.contains(":") || Bt.sDevAddr.length() != 12) {
                            jText6.setText(str);
                        } else {
                            jText6.setText(String.valueOf(str.substring(0, 2)) + ":" + str.substring(2, 4) + ":" + str.substring(4, 6) + ":" + str.substring(6, 8) + ":" + str.substring(8, 10) + ":" + str.substring(10, 12));
                        }
                    }
                    if (Bt.DATA[1] == 1) {
                        jText6.setVisibility(4);
                        return;
                    } else if (Bt.DATA[1] == 0) {
                        jText6.setVisibility(0);
                        return;
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

    public void onNotify(Page_Menu page_Menu, int i, int[] iArr, float[] fArr, String[] strArr) {
        switch (i) {
            case 8:
                JText jText = (JText) page_Menu.getPage().getChildViewByid(56);
                if (App.needSplice && !TextUtils.isEmpty(Bt.sPhoneNumber) && !Bt.sPhoneNumber.startsWith("+")) {
                    Bt.sPhoneNumber = "+" + Bt.sPhoneNumber;
                }
                if (jText != null) {
                    jText.setText(Bt.sPhoneNumber);
                }
                JText jText2 = (JText) page_Menu.getPage().getChildViewByid(57);
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
        String str;
        boolean z = false;
        switch (i) {
            case 1:
                JGridView jGridView = (JGridView) page_Pair.getPage().getChildViewByid(280);
                if (jGridView != null) {
                    jGridView.notifyDataSetChanged();
                    return;
                }
                return;
            case 21:
                if (strArr == null) {
                    page_Pair.showDiscover(false, App.getApp().getString("bt_discover_end"));
                    return;
                } else if (strArr.length >= 3) {
                    try {
                        for (SparseArray<String> sparseArray : App.mBtInfo.mListPair) {
                            if (((String) sparseArray.get(283)).equalsIgnoreCase(strArr[1])) {
                                z = true;
                            }
                        }
                        if (!z && !TextUtils.isEmpty(strArr[2])) {
                            SparseArray sparseArray2 = new SparseArray();
                            sparseArray2.put(283, strArr[1]);
                            sparseArray2.put(284, strArr[2]);
                            sparseArray2.put(282, strArr[0]);
                            App.mBtInfo.mListPair.add(sparseArray2);
                            page_Pair.refreshList();
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
            case 23:
                SparseArray<String> choiceName = page_Pair.getChoiceName(IpcObj.getChoiceAddr());
                if (choiceName != null && (str = choiceName.get(282)) != null) {
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
        String string;
        boolean z;
        boolean z2 = false;
        switch (i) {
            case 6:
                EditText editText = (EditText) page_Set.getPage().getChildViewByid(256);
                if (editText != null) {
                    editText.setText(Bt.sDevName);
                    return;
                }
                return;
            case 8:
                EditText editText2 = (EditText) page_Set.getPage().getChildViewByid(257);
                if (editText2 != null) {
                    editText2.setText(Bt.sDevPin);
                    return;
                }
                return;
            case 11:
                if (Bt.DATA[i] > 0) {
                    string = App.getApp().getString("bt_opened");
                    z = true;
                } else {
                    string = App.getApp().getString("bt_closed");
                    z = false;
                }
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
                    z2 = true;
                }
                page_Set.updateAutoSelected(240, z2);
                return;
            case 22:
                if (page_Set.isClickReSet) {
                    page_Set.actBt.uiUtil.a();
                    cb.a().a(App.getApp().getString("bt_reset_success"));
                    if (!TextUtils.isEmpty(App.strDefaultName) && !App.strDefaultName.equals(Bt.sDevName)) {
                        IpcObj.setDevName(App.strDefaultName);
                    }
                    if (!TextUtils.isEmpty(App.strDefaultPin) && !App.strDefaultPin.equals(Bt.sDevPin)) {
                        IpcObj.setDevPin(App.strDefaultPin);
                    }
                }
                page_Set.isClickReSet = false;
                return;
            default:
                return;
        }
    }

    public void onNotify_Sound(Page_Set page_Set, int i, int[] iArr, float[] fArr, String[] strArr) {
    }

    public void phoneState(Page_Dial page_Dial) {
        int i = Bt.DATA[1];
        JText jText = (JText) page_Dial.getPage().getChildViewByid(55);
        if (jText != null) {
            char c = 65535;
            switch (i) {
                case 0:
                    c = 0;
                    break;
                case 1:
                    c = 2;
                    break;
                case 2:
                    c = 6;
                    break;
                case 3:
                    c = 1;
                    break;
                case 4:
                    c = 4;
                    break;
                case 5:
                    c = 5;
                    break;
                case 6:
                    c = 3;
                    break;
            }
            if (c >= 0) {
                jText.setText(App.getApp().getString(App.mStrStates[c]));
            }
        }
        JText jText2 = (JText) page_Dial.getPage().getChildViewByid(58);
        if (jText2 != null) {
            if (i == 5 || (i == 4 && Bt.DATA[11] > 0)) {
                jText2.setVisibility(0);
            } else {
                jText2.setVisibility(4);
            }
        }
        JText jText3 = (JText) page_Dial.getPage().getChildViewByid(59);
        if (jText3 == null) {
            return;
        }
        if (i == 1) {
            jText3.setVisibility(0);
        } else if (i == 0) {
            jText3.setVisibility(8);
        }
    }
}
