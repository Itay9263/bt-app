package com.syu.app.ipc;

import android.text.TextUtils;
import android.util.SparseArray;
import com.syu.app.App;
import com.syu.bt.act.ActBt;
import com.syu.bt.act.InterfaceBt;
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
import com.syu.bt.page.Page_SetPairItem;
import com.syu.bt.page.pop.Page_PopBt_Book;
import com.syu.ctrl.JPage;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.util.MySharePreference;
import java.util.Iterator;
import java.util.List;

public class IpcObj {
    public static int bAppIdBefore = -1;
    public static boolean bIsAppIdBtAv = false;
    public static String sPhoneAddrScan = FinalChip.BSP_PLATFORM_Null;
    Ipc_786 mIpc_786 = new Ipc_786();
    Ipc_New mIpc_New = new Ipc_New();
    public Runnable mRunnableQueryContact = new Runnable() {
        public void run() {
            App.mBtInfo.queryContacts();
            Main.postRunnable_Ui(true, App.getApp().mRunnable_updatePhoneName);
        }
    };
    public Runnable mRunnableStartPlayAv = new Runnable() {
        public void run() {
            if (IpcObj.isAppIdBtAv() && IpcObj.isConnect() && !App.bBtAvPlayState) {
                IpcObj.avPlay();
            } else if (!App.bBtAvPlayState) {
                Main.removeRunnable_Ui(App.getApp().mIpcObj.mRunnableStartPlayAv);
                Main.postRunnable_Ui(true, App.getApp().mIpcObj.mRunnableStartPlayAv, 4000);
            }
        }
    };
    Runnable mRunnable_ClearContactAndHistory_Disp = new Runnable() {
        public void run() {
            JPage jPage;
            Page_PopBt_Book page_PopBt_Book;
            if (IpcObj.isDisConnect()) {
                App.mBtInfo.mListContact.clear();
                App.mBtInfo.mListHistoryIn.clear();
                App.mBtInfo.mListHistoryOut.clear();
                App.mBtInfo.mListHistoryMiss.clear();
                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                while (it.hasNext()) {
                    it.next().resetList();
                }
                if (App.getApp().bPopBtBook && (jPage = App.uiApp.mPages.get(4)) != null && (page_PopBt_Book = (Page_PopBt_Book) jPage.getNotify()) != null) {
                    page_PopBt_Book.queryContacts(Bt.sPhoneNumber);
                }
            }
        }
    };
    public Runnable mRunnable_scanDB = new Runnable() {
        public void run() {
            boolean z;
            boolean z2 = true;
            if (!IpcObj.isDisConnect() && !TextUtils.isEmpty(Bt.sPhoneAddr)) {
                App.bHaveScanDB = true;
                if (!MySharePreference.getStringValue("bt_phone_addr").equalsIgnoreCase(Bt.sPhoneAddr)) {
                    App.bHasDownBook = false;
                    MySharePreference.saveStringValue("bt_phone_addr", Bt.sPhoneAddr);
                    bz.b();
                    if (!App.mBtInfo.getContentState()) {
                        App.mBtInfo.clearAllContacts();
                        z = true;
                    } else {
                        z = false;
                    }
                    App.mBtInfo.clearAllCallLog();
                    App.mBtInfo.mListHistoryIn.clear();
                    App.mBtInfo.mListHistoryOut.clear();
                    App.mBtInfo.mListHistoryMiss.clear();
                    App.mBtInfo.mListContact.clear();
                    if (App.mBtInfo.getContentState()) {
                        z2 = false;
                    }
                    MySharePreference.saveBooleanValue("save_content", z2);
                } else {
                    z = true;
                }
                if (App.bAutoSavePhoneBook && App.mBtInfo.mListContact.size() <= 0) {
                    List<SparseArray<String>> d = bt.a().d(Bt.sPhoneAddr.replace(":", FinalChip.BSP_PLATFORM_Null));
                    if (d != null) {
                        App.mBtInfo.mListContact.addAll(d);
                    }
                } else if (bv.h() || bv.e()) {
                    App.mBtInfo.mListFavContact.clear();
                    List<SparseArray<String>> e = bt.a().e(Bt.sPhoneAddr.replace(":", FinalChip.BSP_PLATFORM_Null));
                    if (e != null) {
                        App.mBtInfo.mListFavContact.addAll(e);
                    }
                }
                IpcObj.sPhoneAddrScan = Bt.sPhoneAddr;
                if (z || !App.mBtInfo.getContentState()) {
                    IpcObj.this.scanDB();
                }
                if (App.bShowPairedBt) {
                    Main.postRunnable_Ui(false, new Runnable() {
                        public void run() {
                            App.getApp().mIpcObj.queryPairList();
                        }
                    }, 1500);
                }
                App.mBtInfo.handlerPairedBt(Bt.sPhoneAddr, Bt.sPhoneName);
                if (App.bAutoDownPhoneBook && !App.bHasDownBook && App.bPageInitForDownLoadContact) {
                    IpcObj.downloadBook();
                }
                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                while (it.hasNext()) {
                    it.next().resetList();
                }
            }
        }
    };

    public static void LongClick0() {
        if (App.mIsOldService) {
            Ipc_786.LongClick0();
        } else {
            Ipc_New.LongClick0();
        }
    }

    public static void MicSwitch() {
        if (isTalk()) {
            Ipc_New.micSwitch();
        }
    }

    public static void Reset() {
        if (App.mIsOldService) {
            Ipc_786.Reset();
        } else {
            Ipc_New.Reset();
        }
    }

    public static void SetJumpNavi() {
        if (Main.mConf_PlatForm == 5 || Main.mConf_PlatForm == 8) {
            Ipc_New.SetJumpNavi();
        }
    }

    public static void SetRingLevel(int i) {
        if (Main.mConf_PlatForm == 5) {
            Ipc_New.SetRingLevel(i);
        }
    }

    public static void VoiceSwitch() {
        if (isTalk()) {
            hfp();
        }
    }

    public static void avCut() {
        Ipc_New.avCut();
    }

    public static void avLink() {
        Ipc_New.avLink();
    }

    public static void avNext() {
        if (App.mIsOldService) {
            Ipc_786.avNext();
        } else {
            Ipc_New.avNext();
        }
    }

    public static void avPause() {
        if (App.mIsOldService) {
            Ipc_786.avPause();
        } else {
            Ipc_New.avPause();
        }
    }

    public static void avPlay() {
        if (App.mIsOldService) {
            Ipc_786.avPlay();
        } else {
            Ipc_New.avPlay();
        }
    }

    public static void avPlayPause() {
        if (App.mIsOldService) {
            Ipc_786.avPlayPause();
        } else {
            Ipc_New.avPlayPause();
        }
    }

    public static void avPrev() {
        if (App.mIsOldService) {
            Ipc_786.avPrev();
        } else {
            Ipc_New.avPrev();
        }
    }

    public static void avStop() {
        if (App.mIsOldService) {
            Ipc_786.avStop();
        } else {
            Ipc_New.avStop();
        }
    }

    public static void clearKey(boolean z) {
        if (App.mIsOldService) {
            Ipc_786.clearKey(z);
        } else {
            Ipc_New.clearKey(z);
        }
    }

    public static void connectDevice(String str) {
        if (App.mIsOldService) {
            Ipc_786.connectDevice(str);
        } else {
            Ipc_New.connectDevice(str);
        }
    }

    public static void connectOBD(String str) {
        if (App.mIsOldService) {
            Ipc_786.connectOBD(str);
        } else {
            Ipc_New.connectOBD(str);
        }
    }

    public static void cut() {
        if (App.mIsOldService) {
            Ipc_786.cut();
        } else {
            Ipc_New.cut();
        }
    }

    public static void deleteConnectedDevice(String str) {
        Ipc_New.deleteConnectedDevice(str);
    }

    public static void dial() {
        if (App.mIsOldService) {
            Ipc_786.dial();
        } else {
            Ipc_New.dial();
        }
    }

    public static void dial(String str) {
        if (!App.mIsOldService) {
            Ipc_New.dial(str);
        }
    }

    public static void downloadBook() {
        if (App.bPageInitForDownLoadContact && bt.a().a(2500) && !App.bDownloading) {
            App.bShowLoading = true;
            App.mBtInfo.mListContactDownload.clear();
            if (App.mIsOldService) {
                Ipc_786.downloadBook();
            } else {
                Ipc_New.downloadBook();
            }
        }
    }

    public static String filterNumber(String str) {
        return str.replace("(", FinalChip.BSP_PLATFORM_Null).replace(")", FinalChip.BSP_PLATFORM_Null);
    }

    public static String getChoiceAddr() {
        return Bt.sChoiceAddr;
    }

    public static String getDevName() {
        return Bt.sDevName;
    }

    public static String getDevPin() {
        return Bt.sDevPin;
    }

    public static String getPhoneAddr() {
        return Bt.sPhoneAddr;
    }

    public static void hang() {
        if (App.mIsOldService) {
            Ipc_786.hang();
        } else {
            Ipc_New.hang();
        }
    }

    public static void hangIpPhone() {
        if (!App.mIsOldService) {
            Ipc_New.hangIpPhone();
        }
    }

    public static void hfp() {
        if (App.mIsOldService) {
            Ipc_786.hfp();
        } else {
            Ipc_New.hfp();
        }
    }

    public static boolean isAppIdBtAv() {
        return App.mIsOldService ? Ipc_786.isAppIdBtAv() : Ipc_New.isAppIdBtAv();
    }

    public static boolean isAppIdBtPhone() {
        return App.mIsOldService ? Ipc_786.isAppIdBtPhone() : Ipc_New.isAppIdBtPhone();
    }

    public static boolean isConnect() {
        return App.mIsOldService ? Ipc_786.isConnect() : Ipc_New.isConnect();
    }

    public static boolean isDisConnect() {
        return App.mIsOldService ? Ipc_786.isDisConnect() : Ipc_New.isDisConnect();
    }

    public static boolean isInCall() {
        return App.mIsOldService ? Ipc_786.isInCall() : Ipc_New.isInCall();
    }

    public static boolean isPairing() {
        return App.mIsOldService ? Ipc_786.isPairing() : Ipc_New.isPairing();
    }

    public static boolean isRightAppId() {
        return Main.mConf_PlatForm == 1 || isAppIdBtAv() || isAppIdBtPhone();
    }

    public static boolean isRing() {
        return App.mIsOldService ? Ipc_786.isRing() : Ipc_New.isRing();
    }

    public static boolean isStateConnect() {
        return App.mIsOldService ? Ipc_786.isStateConnect() : Ipc_New.isStateConnect();
    }

    public static boolean isStateDialAble() {
        return App.mIsOldService ? Ipc_786.isStateDialAble() : Ipc_New.isStateDialAble();
    }

    public static boolean isStateDisconnect() {
        return App.mIsOldService ? Ipc_786.isStateDisconnect() : Ipc_New.isStateDisconnect();
    }

    public static boolean isTalk() {
        return App.mIsOldService ? Ipc_786.isTalk() : Ipc_New.isTalk();
    }

    public static void itemDial(String str) {
        if (!isStateConnect()) {
            return;
        }
        if (App.mIsOldService) {
            setNum(str);
            dial();
            return;
        }
        dial(str);
    }

    public static void link() {
        if (App.mIsOldService) {
            Ipc_786.link();
        } else {
            Ipc_New.link();
        }
    }

    public static void linkCut() {
        if (isStateDisconnect()) {
            link();
        } else {
            cut();
        }
    }

    public static void mcuOn(int i, int i2) {
        if (i2 == 0) {
            if (!isStateDisconnect()) {
                cut();
            }
        } else if (i2 == 1) {
            link();
        }
    }

    public static void redial() {
        if (App.mIsOldService) {
            Ipc_786.redial();
        } else {
            Ipc_New.redial();
        }
    }

    /* access modifiers changed from: private */
    public void scanDB() {
        App.bDoSaveWork = true;
        App.startThread(App.StrThreadDBContact, this.mRunnableQueryContact, true, 1);
        App.bDoSaveWork = false;
        App.queryCallLog();
    }

    public static void sendPathToUpdateBt(String str) {
        if (!TextUtils.isEmpty(str)) {
            updateBt(str);
        }
    }

    public static void setAutoAnswer(boolean z) {
        if (App.mIsOldService) {
            Ipc_786.setAutoAnswer(z);
        } else {
            Ipc_New.setAutoAnswer(z);
        }
    }

    public static void setBtSwitch(boolean z) {
        if (!App.mIsOldService) {
            Ipc_New.setBtSwitch(z);
        }
    }

    public static void setChoiceAddr(String str) {
        Bt.sChoiceAddr = str;
    }

    public static void setDevName(String str) {
        MySharePreference.saveStringValue("name_diy", str);
        if (App.mIsOldService) {
            Ipc_786.setDevName(str);
        } else {
            Ipc_New.setDevName(str);
        }
    }

    public static void setDevPin(String str) {
        MySharePreference.saveStringValue("pin_diy", str);
        if (App.mIsOldService) {
            Ipc_786.setDevPin(str);
        } else {
            Ipc_New.setDevPin(str);
        }
    }

    public static void setNum(String str) {
        if (App.mIsOldService) {
            Ipc_786.setNum(str);
        } else {
            Ipc_New.setNum(str);
        }
    }

    public static void setNumKey(int i) {
        if (App.mIsOldService) {
            Ipc_786.setNumKey(i);
        } else {
            Ipc_New.setNumKey(i);
        }
    }

    public static void startDiscover() {
        if (App.mIsOldService) {
            Ipc_786.startDiscover();
        } else {
            Ipc_New.startDiscover();
        }
    }

    public static void stopDiscover() {
        if (App.mIsOldService) {
            Ipc_786.stopDiscover();
        } else {
            Ipc_New.stopDiscover();
        }
    }

    public static void test(String str) {
        if (App.mIsOldService) {
            Ipc_786.test(str);
        } else {
            Ipc_New.test(str);
        }
    }

    public static void updateBt(String str) {
        if (!App.mIsOldService) {
            Ipc_New.updateBt(str);
        }
    }

    public void DisableGainSpec() {
        if (!App.mIsOldService) {
            this.mIpc_New.DisableGainSpec();
        }
    }

    public void EnableGainSpec() {
        if (!App.mIsOldService) {
            this.mIpc_New.EnableGainSpec();
        }
    }

    public void FuncDial() {
        if (App.mIsOldService) {
            this.mIpc_786.FuncDial();
        } else {
            this.mIpc_New.FuncDial();
        }
    }

    public boolean FuncDialIpPhone() {
        if (!App.mIsOldService) {
            return this.mIpc_New.FuncDialIpPhone();
        }
        return false;
    }

    public void FuncPairLink(final Page_Pair page_Pair) {
        SparseArray<String> choiceName;
        int i;
        if (!TextUtils.isEmpty(getChoiceAddr())) {
            if (page_Pair.isStartDiscovey) {
                stopDiscover();
            }
            if ((!Bt.sPhoneAddr.equals(getChoiceAddr()) || (Bt.sPhoneAddr.equals(getChoiceAddr()) && !isConnect())) && (choiceName = page_Pair.getChoiceName(getChoiceAddr())) != null) {
                String str = TextUtils.isEmpty(choiceName.get(283)) ? choiceName.get(291) : choiceName.get(284);
                boolean contains = !TextUtils.isEmpty(str) ? Main.mConf_PlatForm == 5 ? str.startsWith("OBD") || str.startsWith("gooddriver") : str.contains("OBD") : false;
                App.bPopObdFlag = contains;
                if (contains) {
                    page_Pair.isSelfLink = true;
                    if (!bv.k() || App.mBtType != 0) {
                        page_Pair.popPwd();
                    } else {
                        bt.a().a((byte[]) null);
                    }
                } else {
                    if (isConnect()) {
                        i = 2000;
                        cut();
                    } else {
                        i = 0;
                    }
                    Main.postRunnable_Ui(false, new Runnable() {
                        public void run() {
                            IpcObj.connectDevice(IpcObj.getChoiceAddr());
                            page_Pair.showConnectTip(true, App.getApp().getString("bt_linking"));
                        }
                    }, (long) i);
                }
            }
        }
    }

    public void FuncPairLink(Page_Set page_Set) {
        SparseArray<String> choiceName;
        if (!TextUtils.isEmpty(getChoiceAddr())) {
            App.bSelfLink = true;
            if (Page_Set.isSetStartDiscovey) {
                stopDiscover();
            }
            if (isConnect()) {
                cut();
            }
            if ((!Bt.sPhoneAddr.equals(getChoiceAddr()) || !isConnect()) && (choiceName = page_Set.getChoiceName(getChoiceAddr())) != null) {
                String str = choiceName.get(299);
                if (Main.mConf_PlatForm == 5 ? str.startsWith("OBD") || str.startsWith("gooddriver") : str.startsWith("OBD")) {
                    page_Set.popPwd();
                } else {
                    connectDevice(getChoiceAddr());
                }
            }
        }
    }

    public void FuncPairLink(Page_SetPairItem page_SetPairItem) {
        SparseArray<String> choiceName;
        if (!TextUtils.isEmpty(getChoiceAddr())) {
            App.bSelfLink = true;
            if (Page_Set.isSetStartDiscovey) {
                stopDiscover();
            }
            if (isConnect()) {
                cut();
            }
            if ((!Bt.sPhoneAddr.equals(getChoiceAddr()) || !isConnect()) && (choiceName = page_SetPairItem.getChoiceName(getChoiceAddr())) != null) {
                String str = choiceName.get(299);
                if (Main.mConf_PlatForm == 5 ? str.startsWith("OBD") || str.startsWith("gooddriver") : str.startsWith("OBD")) {
                    page_SetPairItem.popPwd();
                } else {
                    connectDevice(getChoiceAddr());
                }
            }
        }
    }

    public void LauncherRequestAppIdBtAv() {
        this.mIpc_New.LauncherRequestAppIdBtAv();
    }

    public void addNotify_PopBt() {
        if (App.mIsOldService) {
            this.mIpc_786.addNotify_PopBt();
        } else {
            this.mIpc_New.addNotify_PopBt();
        }
    }

    public void cmdNum(int i) {
        if (App.mIsOldService) {
            this.mIpc_786.cmdNum(i);
        } else {
            this.mIpc_New.cmdNum(i);
        }
    }

    public void dialOut(String str) {
        if (isStateConnect()) {
            if (App.mIsOldService) {
                setNum(str);
                dial();
                return;
            }
            dial(str);
        } else if (!isStateDialAble()) {
            cb.a().a(App.getApp().getString("bt_state_cannot_dial"));
        }
    }

    public float getElectricValue() {
        if (!App.mIsOldService) {
            return this.mIpc_New.getElectricValue();
        }
        return 0.0f;
    }

    public int[] getSB() {
        return this.mIpc_New.getSB();
    }

    public void initIpc() {
        if (App.mIsOldService) {
            this.mIpc_786.initIpc();
        } else {
            this.mIpc_New.initIpc();
        }
    }

    public boolean isCalling() {
        return App.mIsOldService ? this.mIpc_786.isCalling() : this.mIpc_New.isCalling();
    }

    public boolean isMute() {
        return App.mIsOldService ? this.mIpc_786.isMute() : this.mIpc_New.isMute();
    }

    public boolean isPBAPStateLoad() {
        return App.mIsOldService ? this.mIpc_786.isPBAPStateLoad() : this.mIpc_New.isPBAPStateLoad();
    }

    public boolean isPauseDraw() {
        return App.mIsOldService ? this.mIpc_786.isPauseDraw() : this.mIpc_New.isPauseDraw();
    }

    public void onNotify(Page_Av page_Av, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (App.mIsOldService) {
            this.mIpc_786.mNotify.onNotify(page_Av, i, iArr, fArr, strArr);
        } else {
            this.mIpc_New.mNotify.onNotify(page_Av, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Av_HalfScreen page_Av_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Av_HalfScreen, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Callin page_Callin, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Callin, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Callin_HalfScreen page_Callin_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Callin_HalfScreen, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Callin_HideKey page_Callin_HideKey, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Callin_HideKey, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Callin_ShowCall page_Callin_ShowCall, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Callin_ShowCall, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Callin_ShowCall_HalfScreen page_Callin_ShowCall_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Callin_ShowCall_HalfScreen, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Callin_ShowDial page_Callin_ShowDial, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Callin_ShowDial, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Callin_ShowDial_HalfScreen page_Callin_ShowDial_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Callin_ShowDial_HalfScreen, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Callin_ShowKey page_Callin_ShowKey, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Callin_ShowKey, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Callin_ShowKey_HalfScreen page_Callin_ShowKey_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Callin_ShowKey_HalfScreen, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Contact page_Contact, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (App.mIsOldService) {
            this.mIpc_786.mNotify.onNotify(page_Contact, i, iArr, fArr, strArr);
        } else {
            this.mIpc_New.mNotify.onNotify(page_Contact, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Dial page_Dial, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (App.mIsOldService) {
            this.mIpc_786.mNotify.onNotify(page_Dial, i, iArr, fArr, strArr);
        } else {
            this.mIpc_New.mNotify.onNotify(page_Dial, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Dial_HalfScreen page_Dial_HalfScreen, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_Dial_HalfScreen, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_History page_History, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify(page_History, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Menu page_Menu, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (App.mIsOldService) {
            this.mIpc_786.mNotify.onNotify(page_Menu, i, iArr, fArr, strArr);
        } else {
            this.mIpc_New.mNotify.onNotify(page_Menu, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Pair page_Pair, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (App.mIsOldService) {
            this.mIpc_786.mNotify.onNotify(page_Pair, i, iArr, fArr, strArr);
        } else {
            this.mIpc_New.mNotify.onNotify(page_Pair, i, iArr, fArr, strArr);
        }
    }

    public void onNotify(Page_Set page_Set, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (App.mIsOldService) {
            this.mIpc_786.mNotify.onNotify(page_Set, i, iArr, fArr, strArr);
        } else {
            this.mIpc_New.mNotify.onNotify(page_Set, i, iArr, fArr, strArr);
        }
    }

    public void onNotify_Sound(Page_Set page_Set, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (App.mIsOldService) {
            this.mIpc_786.mNotify.onNotify_Sound(page_Set, i, iArr, fArr, strArr);
        } else {
            this.mIpc_New.mNotify.onNotify_Sound(page_Set, i, iArr, fArr, strArr);
        }
    }

    public void onNotify_UniCar(Page_Dial page_Dial, int i, int[] iArr, float[] fArr, String[] strArr) {
        if (!App.mIsOldService) {
            this.mIpc_New.mNotify.onNotify_UniCar(page_Dial, i, iArr, fArr, strArr);
        }
    }

    public void phoneState(ActBt actBt) {
        if (App.mIsOldService) {
            this.mIpc_786.phoneState(actBt);
        } else {
            this.mIpc_New.phoneState(actBt);
        }
    }

    public void queryPairList() {
        if (App.mIsOldService) {
            Ipc_786.queryPairList();
        } else {
            Ipc_New.queryPairList();
        }
    }

    public void recoverAppId() {
        if (App.mIsOldService) {
            this.mIpc_786.recoverAppId();
        } else {
            this.mIpc_New.recoverAppId();
        }
    }

    public void removeNotify_PopBt() {
        if (App.mIsOldService) {
            this.mIpc_786.removeNotify_PopBt();
        } else {
            this.mIpc_New.removeNotify_PopBt();
        }
    }

    public void requestAppIdBtAv() {
        if (App.mIsOldService) {
            this.mIpc_786.requestAppIdBtAv();
        } else {
            this.mIpc_New.requestAppIdBtAv();
        }
    }

    public void requestAppIdBtPhone() {
        if (App.mIsOldService) {
            this.mIpc_786.requestAppIdBtPhone();
        } else {
            this.mIpc_New.requestAppIdBtPhone();
        }
    }

    public void requestAppIdNull() {
        if (App.mIsOldService) {
            this.mIpc_786.requestAppIdNull();
        } else {
            this.mIpc_New.requestAppIdNull();
        }
    }

    public void requestAppIdNull(int i) {
        if (!App.mIsOldService) {
            this.mIpc_New.requestAppIdNull(i);
        }
    }

    public void requestAppIdRight(int i) {
        if (bt.a().e()) {
            switch (i) {
                case 3:
                    requestAppIdBtPhone();
                    return;
                case 10:
                    requestAppIdBtAv();
                    return;
                default:
                    if (App.bIsLauncher_2Ico) {
                        requestAppIdBtPhone();
                        return;
                    } else {
                        recoverAppId();
                        return;
                    }
            }
        }
    }

    public void sendCmdAmp(int i) {
        if (!App.mIsOldService) {
            this.mIpc_New.sendCmdAmp(i);
        }
    }

    public void sendCmdLoud(int i) {
        if (!App.mIsOldService) {
            this.mIpc_New.sendCmdLoud(i);
        }
    }

    public void sendCmdVolBal(int i, int i2) {
        if (!App.mIsOldService) {
            this.mIpc_New.sendCmdVolBal(i, i2);
        }
    }

    public void setBlackScreen() {
        if (!App.mIsOldService) {
            Ipc_New.SetBlackScreen();
        }
    }

    public void setMcuOff() {
        if (!App.mIsOldService) {
            Ipc_New.SetMcuOff();
        }
    }

    public void setStandBy() {
        if (!App.mIsOldService) {
            Ipc_New.SetStandBy();
        }
    }

    public void updateNotify_BtAv() {
        if (App.mIsOldService) {
            this.mIpc_786.updateNotify_BtAv();
        } else {
            this.mIpc_New.updateNotify_BtAv();
        }
    }

    public void updateNotify_BtAv_HalfScreen() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_BtAv_HalfScreen();
        }
    }

    public void updateNotify_Callin() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Callin();
        }
    }

    public void updateNotify_Callin_HalfScreen() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Callin_HalfScreen();
        }
    }

    public void updateNotify_Callin_HideKey() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Callin();
        }
    }

    public void updateNotify_Callin_ShowCall() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Callin();
        }
    }

    public void updateNotify_Callin_ShowCall_HalfScreen() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Callin_HalfScreen();
        }
    }

    public void updateNotify_Callin_ShowDial() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Callin();
        }
    }

    public void updateNotify_Callin_ShowDial_HalfScreen() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Callin_HalfScreen();
        }
    }

    public void updateNotify_Callin_ShowHide_HalfScreen() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Callin_HalfScreen();
        }
    }

    public void updateNotify_Callin_ShowKey() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Callin();
        }
    }

    public void updateNotify_Dial() {
        if (App.mIsOldService) {
            this.mIpc_786.updateNotify_Dial();
        } else {
            this.mIpc_New.updateNotify_Dial();
        }
    }

    public void updateNotify_Dial_HalfScreen() {
        if (!App.mIsOldService) {
            this.mIpc_New.updateNotify_Dial_HalfScreen();
        }
    }

    public void updateNotify_Set() {
        if (App.mIsOldService) {
            this.mIpc_786.updateNotify_Set();
        } else {
            this.mIpc_New.updateNotify_Set();
        }
    }
}
