package com.syu.bt.act;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.syu.app.App;
import com.syu.app.MyActivity;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.data.FinalBtCtrl;
import com.syu.bt.data.FinalBtType;
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
import com.syu.bt.page.Page_ContactItem;
import com.syu.bt.page.Page_Dial;
import com.syu.bt.page.Page_DialItem;
import com.syu.bt.page.Page_Dial_HalfScreen;
import com.syu.bt.page.Page_History;
import com.syu.bt.page.Page_HistoryItem;
import com.syu.bt.page.Page_Main;
import com.syu.bt.page.Page_Menu;
import com.syu.bt.page.Page_Pair;
import com.syu.bt.page.Page_PairItem;
import com.syu.bt.page.Page_PairedItem;
import com.syu.bt.page.Page_Set;
import com.syu.bt.page.Page_SetPairItem;
import com.syu.bt.page.pop.Page_PopBtRing;
import com.syu.bt.page.pop.Page_PopBtRingItem;
import com.syu.bt.page.pop.Page_PopDel;
import com.syu.bt.page.pop.Page_PopPinPwd;
import com.syu.bt.page.pop.Page_PopPwd;
import com.syu.bt.page.pop.Page_PopRecSound;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.ctrl.JView;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.page.IPageNotify;
import com.syu.util.FuncUtils;
import com.syu.util.Markup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ActBt extends MyActivity implements InterfaceBt {
    public static final int BACK_FLAG_ACTIVITY_BIT = 2;
    public static final int BACK_FLAG_FULL_APP_BIT = 16;
    public static final int BACK_FLAG_FULL_NAVI2_BIT = 32;
    public static final int BACK_FLAG_FULL_NAVI_BIT = 4;
    public static final int BACK_FLAG_HALF_NAVI_BIT = 8;
    public static final int BACK_FLAG_PAGE_BIT = 1;
    public static final int FLAG_HOMEKEY_DISPATCHED = Integer.MIN_VALUE;
    public static final int FocusBtn_BtAv = 4;
    public static final int FocusBtn_Cnt = 8;
    public static final int FocusBtn_Contact = 6;
    public static final int FocusBtn_Dial = 2;
    public static final int FocusBtn_History = 5;
    public static final int FocusBtn_Main = 1;
    public static final int FocusBtn_Menu = 0;
    public static final int FocusBtn_Pair = 7;
    public static final int FocusBtn_Set = 3;
    public JButton[] mBtnFocusLast = new JButton[8];
    public JPage mPageCurrent;
    public ViewGroup mPageMain;
    public JPage mPageMenu;
    public ProgressDialog mProgressIpPhoneDlg = null;
    public bk mRecSound = new bk();
    public Runnable mRunnableDisLoading = new Runnable() {
        public void run() {
            String b = ActBt.this.uiUtil.b();
            if (TextUtils.isEmpty(b)) {
                ActBt.this.uiUtil.a();
            } else if (!App.bDownloading || !b.equals(App.getApp().getString("bt_download"))) {
                ActBt.this.uiUtil.a();
            }
        }
    };
    public List<Integer> mScrollPageList = new ArrayList();
    public cc uiUtil = new cc(this);

    public class MyAnimatorListener implements Animator.AnimatorListener {
        private int left;
        private JPage page;

        public MyAnimatorListener(JPage jPage, int i) {
            this.page = jPage;
            this.left = i;
        }

        public void onAnimationCancel(Animator animator) {
            if (this.page != null) {
                this.page.setScaleX(1.0f);
                this.page.setScaleY(1.0f);
                this.page.setAlpha(1.0f);
            }
        }

        public void onAnimationEnd(Animator animator) {
            if (this.page != null) {
                this.page.setX((float) this.left);
                this.page.setScaleX(1.0f);
                this.page.setScaleY(1.0f);
                this.page.setAlpha(1.0f);
            }
        }

        public void onAnimationRepeat(Animator animator) {
        }

        public void onAnimationStart(Animator animator) {
        }
    }

    public class RunnableShowLoading implements Runnable {
        private String msg;

        public RunnableShowLoading(String str) {
            this.msg = str;
        }

        public void run() {
            ActBt.this.uiUtil.a(this.msg);
        }
    }

    private void showOrHideScreenButton(String str) {
        Intent intent = new Intent(str);
        intent.setPackage("com.fyt.screenbutton");
        App.getApp().startServiceSafely(intent);
    }

    public void ClearAllContact() {
        JGridView jGridView;
        JPage page = this.ui.getPage(5);
        if (page != null && (jGridView = (JGridView) page.getChildViewByid(171)) != null) {
            if (!App.sContactsSaveFlag) {
                ca.a(App.getApp(), (String) null);
                App.mBtInfo.mListContact.clear();
                App.clear(jGridView);
            } else {
                this.uiUtil.a(App.getApp().getString("bt_deling"));
                if (App.bDoSaveWork && App.bAutoSavePhoneBook) {
                    App.bDoClearWork = true;
                }
                if (!App.mBtInfo.getContentState()) {
                    App.mBtInfo.clearAllContacts();
                }
                App.mBtInfo.mListContact.clear();
                Iterator<InterfaceBt> it = App.mInterfaceBt.iterator();
                while (it.hasNext()) {
                    it.next().resetList();
                }
            }
            App.sContactsSaveFlag = false;
            EditText editText = (EditText) page.getChildViewByid(162);
            if (editText != null) {
                Page_Contact.strSearch = FinalChip.BSP_PLATFORM_Null;
                editText.setText(FinalChip.BSP_PLATFORM_Null);
                editText.clearFocus();
            }
        }
    }

    public void DialByKey() {
        if (this.bTop && this.mPageCurrent != null) {
            IPageNotify notify = this.mPageCurrent.getNotify();
            if (notify instanceof Page_Av) {
                goPage(3, true);
            } else if (notify instanceof Page_Contact) {
                ((Page_Contact) notify).scrollOk(true);
            } else if (notify instanceof Page_Dial) {
                ((Page_Dial) notify).dialByKey();
            } else if (notify instanceof Page_History) {
                ((Page_History) notify).scrollOk();
            } else if (notify instanceof Page_Pair) {
                ((Page_Pair) notify).scrollOk();
            }
        }
    }

    public void DismissPopContacts() {
        Dialog popDlg = this.ui.getPopDlg(16, App.getApp().getIdStyle("pop_add_contacts_anim"), true);
        if (popDlg != null && popDlg.isShowing()) {
            popDlg.dismiss();
        }
    }

    public boolean Func_Back() {
        boolean z = false;
        if (!bt.a().d() && !App.getApp().mIpcObj.isCalling()) {
            int i = -1;
            if (this.mPageCurrent != null) {
                i = this.mPageCurrent.getId();
            }
            if (!(i == 10 || i == 3)) {
                z = true;
            }
            if (z) {
                goPage(3, true);
            } else {
                if (App.bChangeAppIdWhenTalking) {
                    App.getApp().mIpcObj.requestAppIdNull(i);
                } else {
                    App.getApp().mIpcObj.requestAppIdNull();
                }
                moveTaskToBack(true);
            }
        }
        return true;
    }

    public void InitCtrlId() {
        App.PutHashMap(this.ui.mCtrlId, FinalBtCtrl.class);
    }

    public void InitCtrlType() {
        super.InitCtrlType();
        App.PutHashMap(App.mCtrlType, FinalBtType.class);
    }

    public void InitMapPage() {
        this.ui.mMapPage.put(1, "bt_main");
        this.ui.mMapPage.put(2, "bt_menu");
        this.ui.mMapPage.put(3, "bt_dial");
        this.ui.mMapPage.put(4, "bt_dialitem");
        this.ui.mMapPage.put(5, "bt_contact");
        this.ui.mMapPage.put(6, "bt_contactitem");
        this.ui.mMapPage.put(8, "bt_history");
        this.ui.mMapPage.put(9, "bt_historyitem");
        this.ui.mMapPage.put(10, "bt_av");
        this.ui.mMapPage.put(11, "bt_set");
        this.ui.mMapPage.put(12, "bt_pair");
        this.ui.mMapPage.put(13, "bt_pairitem");
        this.ui.mMapPage.put(14, "bt_paireditem");
        this.ui.mMapPage.put(15, "bt_setpairitem");
        this.ui.mMapPage.put(16, "bt_popdel");
        this.ui.mMapPage.put(17, "bt_poppwd");
        this.ui.mMapPage.put(18, "bt_poppinpwd");
        this.ui.mMapPage.put(19, "bt_poprecsound");
        this.ui.mMapPage.put(20, "bt_popbtring");
        this.ui.mMapPage.put(21, "bt_popbtringitem");
        this.ui.mMapPage.put(22, "bt_callin");
        this.ui.mMapPage.put(26, "bt_callin_showdial");
        this.ui.mMapPage.put(27, "bt_callin_showcall");
        this.ui.mMapPage.put(28, "bt_callin_showkey");
        this.ui.mMapPage.put(29, "bt_callin_hidekey");
        this.ui.mMapPage.put(23, "bt_callin_halfscreen");
        this.ui.mMapPage.put(30, "bt_callin_showdial_halfscreen");
        this.ui.mMapPage.put(31, "bt_callin_showcall_halfscreen");
        this.ui.mMapPage.put(32, "bt_callin_showkey_halfscreen");
        this.ui.mMapPage.put(24, "bt_dial_halfscreen");
        this.ui.mMapPage.put(25, "bt_av_halfscreen");
    }

    public void InitPage(JPage jPage) {
        switch (jPage.getId()) {
            case 1:
                jPage.setNotify(new Page_Main(jPage, this));
                return;
            case 2:
                jPage.setNotify(new Page_Menu(jPage, this));
                return;
            case 3:
                jPage.setNotify(new Page_Dial(jPage, this));
                return;
            case 4:
                jPage.setNotify(new Page_DialItem(jPage, this));
                return;
            case 5:
                jPage.setNotify(new Page_Contact(jPage, this));
                return;
            case 6:
                jPage.setNotify(new Page_ContactItem(jPage, this));
                return;
            case 8:
                jPage.setNotify(new Page_History(jPage, this));
                return;
            case 9:
                jPage.setNotify(new Page_HistoryItem(jPage, this));
                return;
            case 10:
                jPage.setNotify(new Page_Av(jPage, this));
                return;
            case 11:
                jPage.setNotify(new Page_Set(jPage, this));
                return;
            case 12:
                jPage.setNotify(new Page_Pair(jPage, this));
                return;
            case 13:
                jPage.setNotify(new Page_PairItem(jPage));
                return;
            case 14:
                jPage.setNotify(new Page_PairedItem(jPage, this));
                return;
            case 15:
                jPage.setNotify(new Page_SetPairItem(jPage, this));
                return;
            case 16:
                jPage.setNotify(new Page_PopDel(jPage, this));
                return;
            case 17:
                jPage.setNotify(new Page_PopPwd(jPage, this));
                return;
            case 18:
                jPage.setNotify(new Page_PopPinPwd(jPage, this));
                return;
            case 19:
                jPage.setNotify(new Page_PopRecSound(jPage, this));
                return;
            case 20:
                jPage.setNotify(new Page_PopBtRing(jPage, this));
                return;
            case 21:
                jPage.setNotify(new Page_PopBtRingItem(jPage));
                return;
            case 22:
                jPage.setNotify(new Page_Callin(jPage, this));
                return;
            case 23:
                jPage.setNotify(new Page_Callin_HalfScreen(jPage, this));
                return;
            case 24:
                jPage.setNotify(new Page_Dial_HalfScreen(jPage, this));
                return;
            case 25:
                jPage.setNotify(new Page_Av_HalfScreen(jPage, this));
                return;
            case 26:
                jPage.setNotify(new Page_Callin_ShowDial(jPage, this));
                return;
            case 27:
                jPage.setNotify(new Page_Callin_ShowCall(jPage, this));
                return;
            case 28:
                jPage.setNotify(new Page_Callin_ShowKey(jPage, this));
                return;
            case 29:
                jPage.setNotify(new Page_Callin_HideKey(jPage, this));
                return;
            case 30:
                jPage.setNotify(new Page_Callin_ShowDial_HalfScreen(jPage, this));
                return;
            case 31:
                jPage.setNotify(new Page_Callin_ShowCall_HalfScreen(jPage, this));
                return;
            case 32:
                jPage.setNotify(new Page_Callin_ShowKey_HalfScreen(jPage, this));
                return;
            default:
                return;
        }
    }

    public boolean MainClick(View view) {
        int fixPageMain = getFixPageMain(view.getId());
        if (fixPageMain == -1) {
            return false;
        }
        goPage(fixPageMain, App.bPageAnimate);
        return true;
    }

    public boolean MenuClick(View view) {
        int fixPage = getFixPage(view.getId());
        if (fixPage == -1) {
            return false;
        }
        goPage(fixPage, App.bPageAnimate);
        return true;
    }

    public void ReadScrollSet() {
        try {
            int identifier = App.mResources.getIdentifier("bt_scroll", "raw", App.mPkgName);
            if (identifier > 0) {
                String readStrFromStream = FuncUtils.readStrFromStream(App.mResources.openRawResource(identifier));
                Markup markup = new Markup();
                markup.ReadXML(readStrFromStream);
                if (markup.IntoItem()) {
                    do {
                        String GetAttr = markup.GetAttr("id");
                        if (!(GetAttr == null || this.ui.mCtrlId == null || !this.ui.mCtrlId.containsKey(GetAttr))) {
                            this.mScrollPageList.add(Integer.valueOf(this.ui.mCtrlId.get(GetAttr).intValue()));
                        }
                    } while (markup.NextItem());
                    markup.ExitItem();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkAppIdIsRight(int i) {
        Main.postRunnable_Ui(true, new Runnable() {
            public void run() {
                if (ActBt.this.mPageCurrent != null && ActBt.this.mPageCurrent.getId() == 10 && !IpcObj.isAppIdBtAv()) {
                    App.getApp().mIpcObj.requestAppIdRight(10);
                }
            }
        }, (long) i);
    }

    @SuppressLint({"NewApi"})
    public void createActivity(Bundle bundle) {
        super.createActivity(bundle);
        if (App.mIsTranslucent == 1 || App.mIsTranslucent == 3) {
            getWindow().setFlags(1024, 1024);
        } else if (App.mIsTranslucent == 2) {
            Window window = getWindow();
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1280);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0035  */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.view.View createUiItemOtherWay(com.syu.ctrl.JPage r12, com.syu.app.MyUiItem r13) {
        /*
            r11 = this;
            r4 = 2
            r3 = 1
            r6 = 0
            r5 = 0
            int r0 = r13.getType()
            switch(r0) {
                case 1000: goto L_0x000c;
                case 1001: goto L_0x0016;
                case 1002: goto L_0x0020;
                case 1003: goto L_0x006b;
                case 1004: goto L_0x00b0;
                case 1005: goto L_0x00bb;
                case 1006: goto L_0x00c6;
                case 1007: goto L_0x0100;
                default: goto L_0x000b;
            }
        L_0x000b:
            return r6
        L_0x000c:
            com.syu.bt.ctrl.JPinBar r6 = new com.syu.bt.ctrl.JPinBar
            com.syu.app.MyUi r0 = r11.ui
            android.content.Context r0 = r0.mContext
            r6.<init>(r0)
            goto L_0x000b
        L_0x0016:
            com.syu.bt.ctrl.JRotView r6 = new com.syu.bt.ctrl.JRotView
            com.syu.app.MyUi r0 = r11.ui
            android.content.Context r0 = r0.mContext
            r6.<init>(r0)
            goto L_0x000b
        L_0x0020:
            android.graphics.Rect[] r7 = r13.getRect()
            r8 = 64
            int[] r0 = r13.getPara()
            if (r0 == 0) goto L_0x0130
            r8 = r0[r5]
            int r1 = r0.length
            if (r1 < r4) goto L_0x0130
            r9 = r0[r3]
        L_0x0033:
            if (r7 == 0) goto L_0x000b
            android.widget.FrameLayout$LayoutParams r0 = r13.getLayoutParams()
            int r0 = r0.width
            float r0 = (float) r0
            r1 = 1065353216(0x3f800000, float:1.0)
            float r0 = r0 * r1
            float r1 = (float) r8
            float r0 = r0 / r1
            r1 = r7[r5]
            int r1 = r1.top
            float r1 = (float) r1
            float r2 = r0 - r1
            com.syu.bt.ctrl.JSpecView r0 = new com.syu.bt.ctrl.JSpecView
            com.syu.app.MyUi r1 = r11.ui
            android.content.Context r1 = r1.mContext
            r3 = r7[r5]
            int r3 = r3.top
            r4 = r7[r5]
            int r4 = r4.right
            r10 = r7[r5]
            int r10 = r10.left
            int r4 = r4 - r10
            r10 = r7[r5]
            int r10 = r10.bottom
            r5 = r7[r5]
            int r5 = r5.top
            int r5 = r10 - r5
            r7 = r6
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            r6 = r0
            goto L_0x000b
        L_0x006b:
            java.lang.String[] r0 = r13.getStrDrawableExtra()
            if (r0 == 0) goto L_0x012b
            com.syu.app.MyUi r0 = r11.ui
            java.lang.String[] r1 = r13.getStrDrawableExtra()
            r1 = r1[r5]
            android.graphics.drawable.Drawable r2 = r0.getDrawableFromPath(r1)
            java.lang.String[] r0 = r13.getStrDrawableExtra()
            int r0 = r0.length
            if (r0 <= r3) goto L_0x0127
            com.syu.app.MyUi r0 = r11.ui
            java.lang.String[] r1 = r13.getStrDrawableExtra()
            r1 = r1[r3]
            java.lang.String r1 = r1.trim()
            android.graphics.drawable.Drawable r1 = r0.getDrawableFromPath(r1)
            com.syu.app.MyUi r0 = r11.ui
            java.lang.String[] r3 = r13.getStrDrawableExtra()
            r3 = r3[r4]
            java.lang.String r3 = r3.trim()
            android.graphics.drawable.Drawable r6 = r0.getDrawableFromPath(r3)
            r0 = r6
        L_0x00a5:
            com.syu.bt.ctrl.JSwitchButon2 r6 = new com.syu.bt.ctrl.JSwitchButon2
            com.syu.app.MyUi r3 = r11.ui
            android.content.Context r3 = r3.mContext
            r6.<init>(r3, r2, r1, r0)
            goto L_0x000b
        L_0x00b0:
            com.syu.bt.ctrl.SlideBarView r6 = new com.syu.bt.ctrl.SlideBarView
            com.syu.app.MyUi r0 = r11.ui
            android.content.Context r0 = r0.mContext
            r6.<init>(r0)
            goto L_0x000b
        L_0x00bb:
            com.syu.bt.ctrl.SwipeCallingView r6 = new com.syu.bt.ctrl.SwipeCallingView
            com.syu.app.MyUi r0 = r11.ui
            android.content.Context r0 = r0.mContext
            r6.<init>(r0)
            goto L_0x000b
        L_0x00c6:
            java.lang.String[] r0 = r13.getStrDrawableExtra()
            if (r0 == 0) goto L_0x0124
            com.syu.app.MyUi r0 = r11.ui
            java.lang.String[] r1 = r13.getStrDrawableExtra()
            r1 = r1[r5]
            android.graphics.drawable.Drawable r1 = r0.getDrawableFromPath(r1)
            java.lang.String[] r0 = r13.getStrDrawableExtra()
            int r0 = r0.length
            if (r0 <= r3) goto L_0x0122
            com.syu.app.MyUi r0 = r11.ui
            java.lang.String[] r2 = r13.getStrDrawableExtra()
            r2 = r2[r3]
            java.lang.String r2 = r2.trim()
            android.graphics.drawable.Drawable r0 = r0.getDrawableFromPath(r2)
        L_0x00ef:
            java.lang.String[] r2 = r13.getParaStr()
            if (r2 == 0) goto L_0x000b
            com.syu.bt.ctrl.CircleMenuLayout r6 = new com.syu.bt.ctrl.CircleMenuLayout
            com.syu.app.MyUi r3 = r11.ui
            android.content.Context r3 = r3.mContext
            r6.<init>(r3, r1, r0, r2)
            goto L_0x000b
        L_0x0100:
            int[] r0 = new int[r4]
            r0 = {16777215, 16777215} // fill-array
            int[] r1 = r13.getColor()
            if (r1 == 0) goto L_0x010f
            int[] r0 = r13.getColor()
        L_0x010f:
            java.lang.String r1 = r13.getText()
            int r2 = r13.getHeight()
            com.syu.bt.ctrl.BorderTextView r6 = new com.syu.bt.ctrl.BorderTextView
            com.syu.app.MyUi r3 = r11.ui
            android.content.Context r3 = r3.mContext
            r6.<init>(r3, r0, r1, r2)
            goto L_0x000b
        L_0x0122:
            r0 = r6
            goto L_0x00ef
        L_0x0124:
            r0 = r6
            r1 = r6
            goto L_0x00ef
        L_0x0127:
            r0 = r6
            r1 = r6
            goto L_0x00a5
        L_0x012b:
            r0 = r6
            r1 = r6
            r2 = r6
            goto L_0x00a5
        L_0x0130:
            r9 = r5
            goto L_0x0033
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.act.ActBt.createUiItemOtherWay(com.syu.ctrl.JPage, com.syu.app.MyUiItem):android.view.View");
    }

    public void disPostLoading(int i) {
        Main.postRunnable_Ui(true, this.mRunnableDisLoading, (long) i);
    }

    public void dismissProgressIpPhoneDlg() {
        if (this.mProgressIpPhoneDlg != null && this.mProgressIpPhoneDlg.isShowing()) {
            this.mProgressIpPhoneDlg.dismiss();
            this.mProgressIpPhoneDlg = null;
        }
    }

    public void dismissRecordDlg() {
        Dialog popDlg = this.ui.getPopDlg(19, App.getApp().getIdStyle("pop_add_contacts_anim"), true);
        if (popDlg != null && popDlg.isShowing()) {
            popDlg.dismiss();
        }
    }

    public JButton getBtnMenuFocus(JPage jPage, int i) {
        if (jPage == null) {
            return null;
        }
        switch (i) {
            case 3:
                if (!bv.c()) {
                    return (JButton) jPage.getChildViewByid(35);
                }
                return null;
            case 5:
                return (JButton) jPage.getChildViewByid(36);
            case 8:
                return (JButton) jPage.getChildViewByid(37);
            case 10:
                return (JButton) jPage.getChildViewByid(38);
            case 11:
                return (JButton) jPage.getChildViewByid(39);
            case 12:
                return (JButton) jPage.getChildViewByid(40);
            default:
                return null;
        }
    }

    public int getFixPage(int i) {
        switch (i) {
            case 35:
                return !bv.c() ? 3 : 8;
            case 36:
                return 5;
            case 37:
                return 8;
            case 38:
                return 10;
            case 39:
                return 11;
            case 40:
                return 12;
            case 41:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                startActivity(intent);
                return -1;
            case 43:
                dispatchKeyEvent(new KeyEvent(0, 4));
                break;
        }
        return -1;
    }

    public int getFixPageMain(int i) {
        switch (i) {
            case 33:
                return App.idPageBeforeAv;
            case 34:
                return 10;
            default:
                return -1;
        }
    }

    public View getFocus(JPage jPage) {
        View view = null;
        if (this.bTop && jPage != null) {
            view = jPage.getFocusedChild();
            while (view instanceof ViewGroup) {
                view = ((ViewGroup) view).getFocusedChild();
            }
        }
        return view;
    }

    public void getMenuViewFocus(JPage jPage, int i) {
        if (jPage != null) {
            JView jView = (JView) jPage.getChildViewByid(371);
            JView jView2 = (JView) jPage.getChildViewByid(372);
            JView jView3 = (JView) jPage.getChildViewByid(373);
            JView jView4 = (JView) jPage.getChildViewByid(374);
            JView jView5 = (JView) jPage.getChildViewByid(375);
            JView jView6 = (JView) jPage.getChildViewByid(376);
            if (jView != null) {
                jView.setVisibility(8);
            }
            if (jView2 != null) {
                jView2.setVisibility(8);
            }
            if (jView3 != null) {
                jView3.setVisibility(8);
            }
            if (jView4 != null) {
                jView4.setVisibility(8);
            }
            if (jView5 != null) {
                jView5.setVisibility(8);
            }
            if (jView6 != null) {
                jView6.setVisibility(8);
            }
            switch (i) {
                case 3:
                    if (jView != null) {
                        jView.setVisibility(0);
                        return;
                    }
                    return;
                case 5:
                    if (jView3 != null) {
                        jView3.setVisibility(0);
                        return;
                    }
                    return;
                case 8:
                    if (jView2 != null) {
                        jView2.setVisibility(0);
                        return;
                    }
                    return;
                case 10:
                    if (jView4 != null) {
                        jView4.setVisibility(0);
                        return;
                    }
                    return;
                case 11:
                    if (jView6 != null) {
                        jView6.setVisibility(0);
                        return;
                    }
                    return;
                case 12:
                    if (jView5 != null) {
                        jView5.setVisibility(0);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public Page_Av_HalfScreen getPageAv_HalfScreen() {
        return (Page_Av_HalfScreen) getPageNotify(25);
    }

    public Page_Callin getPageCallin() {
        return (Page_Callin) getPageNotify(22);
    }

    public Page_Callin_HalfScreen getPageCallin_HalfScreen() {
        return (Page_Callin_HalfScreen) getPageNotify(23);
    }

    public Page_Callin_HideKey getPageCallin_HideKey() {
        return (Page_Callin_HideKey) getPageNotify(29);
    }

    public Page_Callin_ShowCall getPageCallin_ShowCall() {
        return (Page_Callin_ShowCall) getPageNotify(27);
    }

    public Page_Callin_ShowCall_HalfScreen getPageCallin_ShowCall_HalfScreen() {
        return (Page_Callin_ShowCall_HalfScreen) getPageNotify(31);
    }

    public Page_Callin_ShowDial getPageCallin_ShowDial() {
        return (Page_Callin_ShowDial) getPageNotify(26);
    }

    public Page_Callin_ShowDial_HalfScreen getPageCallin_ShowDial_HalfScreen() {
        return (Page_Callin_ShowDial_HalfScreen) getPageNotify(30);
    }

    public Page_Callin_ShowKey getPageCallin_ShowKey() {
        return (Page_Callin_ShowKey) getPageNotify(28);
    }

    public Page_Callin_ShowKey_HalfScreen getPageCallin_ShowKey_HalfScreen() {
        return (Page_Callin_ShowKey_HalfScreen) getPageNotify(32);
    }

    public Page_Contact getPageContact() {
        return (Page_Contact) getPageNotify(5);
    }

    public Page_Dial getPageDial() {
        return (Page_Dial) getPageNotify(3);
    }

    public Page_Dial_HalfScreen getPageDial_HalfScreen() {
        return (Page_Dial_HalfScreen) getPageNotify(24);
    }

    public Page_History getPageHistory() {
        return (Page_History) getPageNotify(8);
    }

    public Page_Pair getPagePair() {
        return (Page_Pair) getPageNotify(12);
    }

    public Page_Set getPageSet() {
        return (Page_Set) getPageNotify(11);
    }

    public void goPage(int i, boolean z) {
        int i2;
        int i3;
        int i4;
        if (Main.mConf_PlatForm == 8) {
            z = false;
        }
        if (i == 3) {
            if (App.getApp().mIpcObj.isCalling()) {
                if (App.mEnableHalfScreen || App.bCallinPageFlag) {
                    i = 22;
                }
            } else if (App.bFirstPairPage) {
                i = 12;
            }
        }
        if (i == 10 && !App.bIsLauncher_2Ico && App.sPage != 10) {
            App.idPageBeforeAv = App.sPage;
        }
        if (i != 5 && App.mSelectContact > 0) {
            App.mSelectContact = -1;
        }
        App.sPage = i;
        JPage jPage = this.mPageCurrent;
        if (jPage != null) {
            i2 = jPage.getId();
            if (i2 == i) {
                updateMainSelected(App.sPage, true);
                updateMenuSelected(2, App.sPage, 0, true);
                return;
            }
        } else {
            i2 = -1;
        }
        JPage loadPage = this.ui.loadPage(true, i);
        if (loadPage != null) {
            if (this.mAnimSet != null && this.mAnimSet.isRunning()) {
                this.mAnimSet.cancel();
            }
            if (this.mPageMain.indexOfChild(loadPage) < 0) {
                this.mPageMain.addView(loadPage);
            }
            this.mPageCurrent = loadPage;
            if (jPage != null) {
                i4 = jPage.getLeft();
                int right = jPage.getRight();
                jPage.pause();
                jPage.setVisibility(8);
                i3 = right;
            } else {
                i3 = -1;
                i4 = -1;
            }
            loadPage.resume();
            loadPage.setVisibility(0);
            if (z && i3 > 0) {
                this.mAnimSet = new AnimatorSet();
                if (i2 > i) {
                    loadPage.setScaleX(1.0f);
                    loadPage.setScaleY(1.0f);
                    loadPage.setAlpha(1.0f);
                    this.mAnimSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(loadPage, "x", new float[]{(float) i3, (float) i4})});
                } else {
                    this.mAnimSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(loadPage, "scaleX", new float[]{0.7f, 1.0f}), ObjectAnimator.ofFloat(loadPage, "scaleY", new float[]{0.7f, 1.0f}), ObjectAnimator.ofFloat(loadPage, "alpha", new float[]{0.2f, 1.0f})});
                }
                this.mAnimSet.addListener(new MyAnimatorListener(loadPage, i4));
                this.mAnimSet.setDuration(300);
                this.mAnimSet.start();
            }
            updateMainSelected(App.sPage, true);
            updateMenuSelected(2, App.sPage, 0, true);
            if (App.hideBtnWhenDisconnect) {
                setMenuBtnEnable(App.sPage, Bt.DATA[9]);
            } else {
                updateMenuSelected(App.sPage, App.sPage, 2, true);
            }
            if (bv.c() && App.mIsTranslucent > 0 && App.mIsTranslucent < 3) {
                if (App.sPage != 3) {
                    hideSystemUI();
                } else {
                    showSystemUI();
                }
            }
        }
    }

    public void hideSystemUI() {
        getWindow().addFlags(1024);
    }

    public boolean isFocus(View view) {
        return view != null && view.isFocused() && view.getVisibility() == 0;
    }

    public boolean isHalf() {
        return !TextUtils.isEmpty(this.ui.mStrHeadXml);
    }

    public void moveTaskToBack() {
        moveTaskToBack(true);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        App.mInterfaceBt.add(this);
        getWindow().getDecorView().setSystemUiVisibility(256);
        super.onCreate(bundle);
        ReadScrollSet();
        if (this.mPageCurrent != null) {
            this.mPageCurrent.resume();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        bt.g();
        App.mInterfaceBt.remove(this);
        super.onDestroy();
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onKeyDown() {
        /*
            r3 = this;
            r1 = 0
            com.syu.ctrl.JPage r0 = r3.mPageCurrent
            if (r0 == 0) goto L_0x0024
            com.syu.ctrl.JPage r0 = r3.mPageCurrent
            com.syu.page.IPageNotify r0 = r0.getNotify()
            boolean r2 = r0 instanceof com.syu.bt.page.Page_Contact
            if (r2 == 0) goto L_0x0024
            boolean r2 = defpackage.bv.d()
            if (r2 == 0) goto L_0x0024
            com.syu.bt.page.Page_Contact r0 = (com.syu.bt.page.Page_Contact) r0
            r1 = 1
            boolean r0 = r0.scrollToNext(r1)
        L_0x001c:
            if (r0 != 0) goto L_0x0023
            r0 = 130(0x82, float:1.82E-43)
            r3.onKeyHandle(r0)
        L_0x0023:
            return
        L_0x0024:
            r0 = r1
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.act.ActBt.onKeyDown():void");
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean z;
        if (bt.a().d()) {
            return true;
        }
        if (App.getApp().mIpcObj.isCalling()) {
            return true;
        }
        if (App.mEnableHalfScreen) {
            return super.onKeyDown(i, keyEvent);
        }
        switch (i) {
            case 4:
                JPage jPage = this.ui.mPages.get(2);
                int i2 = -1;
                if (this.mPageCurrent != null) {
                    i2 = this.mPageCurrent.getId();
                }
                if (App.mIdCustomer == 21) {
                    if (jPage == null && i2 != 3) {
                        z = true;
                    }
                    z = false;
                } else if (bv.c()) {
                    if (i2 != 10 || App.bIsLauncher_2Ico) {
                        if (i2 != 3 && !App.bFirstPairPage && !App.bFirstBtAvPage) {
                            z = true;
                        }
                        z = false;
                    } else {
                        App.sPage = App.idPageBeforeAv;
                        z = false;
                    }
                } else if (bv.e() && i2 == 3 && getPageDial().getSaveViewState()) {
                    getPageDial().disableSaveView(true);
                    return true;
                } else if (App.bIsLauncher_2Ico) {
                    if (i2 != 3 && i2 != 10 && !App.bFirstPairPage && !App.bFirstBtAvPage && jPage == null) {
                        z = true;
                    }
                    z = false;
                } else if (i2 == 10) {
                    App.sPage = App.idPageBeforeAv;
                    z = false;
                } else {
                    if (i2 != 3 && !App.bFirstPairPage && !App.bFirstBtAvPage && jPage == null) {
                        z = true;
                    }
                    z = false;
                }
                if (z) {
                    goPage(3, true);
                } else {
                    if (App.bChangeAppIdWhenTalking) {
                        App.getApp().mIpcObj.requestAppIdNull(i2);
                    } else {
                        App.getApp().mIpcObj.requestAppIdNull();
                    }
                    moveTaskToBack(true);
                }
                return true;
            default:
                return super.onKeyDown(i, keyEvent);
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:132:0x02a0, code lost:
        r7 = false;
     */
    /* JADX WARNING: Removed duplicated region for block: B:139:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onKeyHandle(int r10) {
        /*
            r9 = this;
            r3 = 3
            r5 = -1
            r7 = 1
            r1 = 2
            r4 = 0
            boolean r0 = r9.bTop
            if (r0 == 0) goto L_0x00ae
            com.syu.ctrl.JPage r0 = r9.mPage
            if (r0 == 0) goto L_0x00ae
            com.syu.ctrl.JPage r0 = r9.mPage
            android.view.View r0 = r0.getFocusedChild()
        L_0x0013:
            boolean r2 = r0 instanceof android.view.ViewGroup
            if (r2 != 0) goto L_0x00af
            r6 = 0
            if (r0 == 0) goto L_0x02a3
            boolean r2 = defpackage.bv.j()
            if (r2 == 0) goto L_0x0227
            int r2 = r0.getId()
            switch(r2) {
                case 103: goto L_0x00b7;
                case 104: goto L_0x00d5;
                case 105: goto L_0x00ef;
                case 106: goto L_0x0109;
                case 107: goto L_0x0123;
                case 108: goto L_0x013d;
                case 109: goto L_0x0157;
                case 110: goto L_0x0171;
                case 111: goto L_0x018b;
                case 112: goto L_0x01a5;
                case 113: goto L_0x01bf;
                case 114: goto L_0x01d9;
                case 115: goto L_0x0027;
                case 116: goto L_0x0027;
                case 117: goto L_0x0027;
                case 118: goto L_0x0027;
                case 119: goto L_0x0027;
                case 120: goto L_0x020d;
                case 121: goto L_0x0027;
                case 122: goto L_0x0027;
                case 123: goto L_0x0027;
                case 124: goto L_0x0027;
                case 125: goto L_0x0027;
                case 126: goto L_0x0027;
                case 127: goto L_0x0027;
                case 128: goto L_0x0027;
                case 129: goto L_0x0027;
                case 130: goto L_0x0027;
                case 131: goto L_0x0027;
                case 132: goto L_0x0027;
                case 133: goto L_0x01f3;
                default: goto L_0x0027;
            }
        L_0x0027:
            if (r6 == 0) goto L_0x003b
            r6.requestFocusFromTouch()
            com.syu.page.IPageNotify r2 = r9.getPageNotify(r3)
            com.syu.bt.page.Page_Dial r2 = (com.syu.bt.page.Page_Dial) r2
            if (r2 == 0) goto L_0x003b
            int r8 = r6.getId()
            r2.resetMenuLayoutAngle(r8)
        L_0x003b:
            if (r7 == 0) goto L_0x029d
            android.view.View r2 = r0.focusSearch(r10)
        L_0x0041:
            if (r2 != 0) goto L_0x029a
            com.syu.ctrl.JPage r0 = r9.mPageCurrent
            if (r0 == 0) goto L_0x029a
            com.syu.ctrl.JPage r0 = r9.mPageCurrent
            java.lang.Object r0 = r0.getTag()
            boolean r6 = r0 instanceof com.syu.app.MyUiItem
            if (r6 == 0) goto L_0x029a
            com.syu.app.MyUiItem r0 = (com.syu.app.MyUiItem) r0
            java.lang.String[] r0 = r0.getParaStr()
            if (r0 == 0) goto L_0x029a
            int r6 = r0.length
            if (r6 <= 0) goto L_0x029a
            com.syu.app.MyUi r6 = r9.ui
            java.util.HashMap<java.lang.String, java.lang.Integer> r6 = r6.mCtrlId
            r7 = r0[r4]
            boolean r6 = r6.containsKey(r7)
            if (r6 == 0) goto L_0x029a
            com.syu.app.MyUi r2 = r9.ui
            java.util.HashMap<java.lang.String, java.lang.Integer> r2 = r2.mCtrlId
            r0 = r0[r4]
            java.lang.Object r0 = r2.get(r0)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            android.view.View r0 = r2.findViewById(r0)
            r6 = r0
        L_0x007f:
            if (r6 == 0) goto L_0x009d
            boolean r0 = defpackage.bv.g()
            if (r0 == 0) goto L_0x0295
            int r0 = r6.getId()
            r2 = 41
            if (r0 == r2) goto L_0x0097
            int r0 = r6.getId()
            r2 = 43
            if (r0 != r2) goto L_0x024a
        L_0x0097:
            r6.requestFocusFromTouch()
            r9.onKeyHandle(r10)
        L_0x009d:
            boolean r0 = defpackage.bv.j()
            if (r0 == 0) goto L_0x00ae
            com.syu.page.IPageNotify r0 = r9.getPageNotify(r1)
            com.syu.bt.page.Page_Menu r0 = (com.syu.bt.page.Page_Menu) r0
            if (r0 == 0) goto L_0x00ae
            r0.resetMenuButton()
        L_0x00ae:
            return
        L_0x00af:
            android.view.ViewGroup r0 = (android.view.ViewGroup) r0
            android.view.View r0 = r0.getFocusedChild()
            goto L_0x0013
        L_0x00b7:
            if (r10 != r1) goto L_0x00c4
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 104(0x68, float:1.46E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x00c4:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageMenu
            if (r2 == 0) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageMenu
            r6 = 39
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x00d5:
            if (r10 != r1) goto L_0x00e2
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 105(0x69, float:1.47E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x00e2:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 103(0x67, float:1.44E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x00ef:
            if (r10 != r1) goto L_0x00fc
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 106(0x6a, float:1.49E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x00fc:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 104(0x68, float:1.46E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0109:
            if (r10 != r1) goto L_0x0116
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 107(0x6b, float:1.5E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0116:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 105(0x69, float:1.47E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0123:
            if (r10 != r1) goto L_0x0130
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 108(0x6c, float:1.51E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0130:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 106(0x6a, float:1.49E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x013d:
            if (r10 != r1) goto L_0x014a
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 109(0x6d, float:1.53E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x014a:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 107(0x6b, float:1.5E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0157:
            if (r10 != r1) goto L_0x0164
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 110(0x6e, float:1.54E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0164:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 108(0x6c, float:1.51E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0171:
            if (r10 != r1) goto L_0x017e
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 111(0x6f, float:1.56E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x017e:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 109(0x6d, float:1.53E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x018b:
            if (r10 != r1) goto L_0x0198
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 112(0x70, float:1.57E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0198:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 110(0x6e, float:1.54E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x01a5:
            if (r10 != r1) goto L_0x01b2
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 113(0x71, float:1.58E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x01b2:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 111(0x6f, float:1.56E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x01bf:
            if (r10 != r1) goto L_0x01cc
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 114(0x72, float:1.6E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x01cc:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 112(0x70, float:1.57E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x01d9:
            if (r10 != r1) goto L_0x01e6
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 133(0x85, float:1.86E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x01e6:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 113(0x71, float:1.58E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x01f3:
            if (r10 != r1) goto L_0x0200
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 120(0x78, float:1.68E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0200:
            if (r10 != r7) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 114(0x72, float:1.6E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x020d:
            if (r10 != r7) goto L_0x021a
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 133(0x85, float:1.86E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x021a:
            if (r10 != r1) goto L_0x02a0
            com.syu.ctrl.JPage r2 = r9.mPageCurrent
            r6 = 115(0x73, float:1.61E-43)
            android.view.View r6 = r2.findViewById(r6)
            r7 = r4
            goto L_0x0027
        L_0x0227:
            java.lang.String r2 = com.syu.app.App.mStrCustomer
            java.lang.String r8 = "zhtd_aodi"
            boolean r2 = r2.equalsIgnoreCase(r8)
            if (r2 == 0) goto L_0x003b
            int r2 = r0.getId()
            switch(r2) {
                case 36: goto L_0x023a;
                case 37: goto L_0x023a;
                case 38: goto L_0x023a;
                case 39: goto L_0x0238;
                case 40: goto L_0x0244;
                default: goto L_0x0238;
            }
        L_0x0238:
            goto L_0x003b
        L_0x023a:
            if (r10 != r7) goto L_0x0241
            r2 = 33
        L_0x023e:
            r10 = r2
            goto L_0x003b
        L_0x0241:
            r2 = 130(0x82, float:1.82E-43)
            goto L_0x023e
        L_0x0244:
            if (r10 != r7) goto L_0x003b
            r10 = 33
            goto L_0x003b
        L_0x024a:
            int r0 = r6.getId()
            switch(r0) {
                case 35: goto L_0x025f;
                case 36: goto L_0x026a;
                case 37: goto L_0x026c;
                case 38: goto L_0x026f;
                case 39: goto L_0x0272;
                case 40: goto L_0x0275;
                default: goto L_0x0251;
            }
        L_0x0251:
            r2 = r5
        L_0x0252:
            if (r2 <= r5) goto L_0x0278
            r0 = r9
            r3 = r2
            r5 = r4
            r0.updateMenuSelected_New(r1, r2, r3, r4, r5)
        L_0x025a:
            r6.requestFocusFromTouch()
            goto L_0x009d
        L_0x025f:
            boolean r0 = defpackage.bv.c()
            if (r0 == 0) goto L_0x0268
            r2 = 8
            goto L_0x0252
        L_0x0268:
            r2 = r3
            goto L_0x0252
        L_0x026a:
            r2 = 5
            goto L_0x0252
        L_0x026c:
            r2 = 8
            goto L_0x0252
        L_0x026f:
            r2 = 10
            goto L_0x0252
        L_0x0272:
            r2 = 11
            goto L_0x0252
        L_0x0275:
            r2 = 12
            goto L_0x0252
        L_0x0278:
            com.syu.app.MyUi r0 = r9.ui
            android.util.SparseArray<com.syu.ctrl.JPage> r0 = r0.mPages
            java.lang.Object r0 = r0.get(r1)
            com.syu.ctrl.JPage r0 = (com.syu.ctrl.JPage) r0
            com.syu.ctrl.JPage r3 = r9.mPageCurrent
            int r3 = r3.getId()
            com.syu.ctrl.JButton r0 = r9.getBtnMenuFocus(r0, r3)
            r9.updateBtnFocus(r0, r1, r4)
            com.syu.ctrl.JPage r0 = r9.mPageMenu
            r9.getMenuViewFocus(r0, r2)
            goto L_0x025a
        L_0x0295:
            r6.requestFocusFromTouch()
            goto L_0x009d
        L_0x029a:
            r6 = r2
            goto L_0x007f
        L_0x029d:
            r2 = r6
            goto L_0x0041
        L_0x02a0:
            r7 = r4
            goto L_0x0027
        L_0x02a3:
            r2 = r6
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.act.ActBt.onKeyHandle(int):void");
    }

    public void onKeyLeft() {
        onKeyHandle(17);
    }

    public void onKeyRight() {
        onKeyHandle(66);
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onKeyUp() {
        /*
            r3 = this;
            r1 = 0
            com.syu.ctrl.JPage r0 = r3.mPageCurrent
            if (r0 == 0) goto L_0x0024
            com.syu.ctrl.JPage r0 = r3.mPageCurrent
            com.syu.page.IPageNotify r0 = r0.getNotify()
            boolean r2 = r0 instanceof com.syu.bt.page.Page_Contact
            if (r2 == 0) goto L_0x0024
            boolean r2 = defpackage.bv.d()
            if (r2 == 0) goto L_0x0024
            com.syu.bt.page.Page_Contact r0 = (com.syu.bt.page.Page_Contact) r0
            r1 = 1
            boolean r0 = r0.scrollToPrev(r1)
        L_0x001c:
            if (r0 != 0) goto L_0x0023
            r0 = 33
            r3.onKeyHandle(r0)
        L_0x0023:
            return
        L_0x0024:
            r0 = r1
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.act.ActBt.onKeyUp():void");
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (!bt.a().d() && !App.getApp().mIpcObj.isCalling()) {
            return super.onKeyUp(i, keyEvent);
        }
        return true;
    }

    public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (this.mPageCurrent != null) {
            IPageNotify notify = this.mPageCurrent.getNotify();
            if (notify instanceof Page_Av) {
                ((Page_Av) notify).onNotify(i, iArr, fArr, strArr);
                Page_Av_HalfScreen pageAv_HalfScreen = getPageAv_HalfScreen();
                if (pageAv_HalfScreen != null) {
                    pageAv_HalfScreen.onNotify(i, iArr, fArr, strArr);
                }
            } else if (notify instanceof Page_Dial) {
                ((Page_Dial) notify).onNotify(i, iArr, fArr, strArr);
                Page_Dial_HalfScreen pageDial_HalfScreen = getPageDial_HalfScreen();
                if (pageDial_HalfScreen != null) {
                    pageDial_HalfScreen.onNotify(i, iArr, fArr, strArr);
                }
            } else if (notify instanceof Page_Set) {
                ((Page_Set) notify).onNotify(i, iArr, fArr, strArr);
            } else if (notify instanceof Page_Callin) {
                ((Page_Callin) notify).onNotify(i, iArr, fArr, strArr);
                Page_Callin_HalfScreen pageCallin_HalfScreen = getPageCallin_HalfScreen();
                Page_Callin_ShowDial_HalfScreen pageCallin_ShowDial_HalfScreen = getPageCallin_ShowDial_HalfScreen();
                Page_Callin_ShowCall_HalfScreen pageCallin_ShowCall_HalfScreen = getPageCallin_ShowCall_HalfScreen();
                Page_Callin_ShowKey_HalfScreen pageCallin_ShowKey_HalfScreen = getPageCallin_ShowKey_HalfScreen();
                Page_Callin_ShowDial pageCallin_ShowDial = getPageCallin_ShowDial();
                Page_Callin_ShowCall pageCallin_ShowCall = getPageCallin_ShowCall();
                Page_Callin_ShowKey pageCallin_ShowKey = getPageCallin_ShowKey();
                Page_Callin_HideKey pageCallin_HideKey = getPageCallin_HideKey();
                if (pageCallin_HalfScreen != null) {
                    pageCallin_HalfScreen.onNotify(i, iArr, fArr, strArr);
                }
                if (pageCallin_ShowDial_HalfScreen != null) {
                    pageCallin_ShowDial_HalfScreen.onNotify(i, iArr, fArr, strArr);
                }
                if (pageCallin_ShowCall_HalfScreen != null) {
                    pageCallin_ShowCall_HalfScreen.onNotify(i, iArr, fArr, strArr);
                }
                if (pageCallin_ShowKey_HalfScreen != null) {
                    pageCallin_ShowKey_HalfScreen.onNotify(i, iArr, fArr, strArr);
                }
                if (pageCallin_ShowDial != null) {
                    pageCallin_ShowDial.onNotify(i, iArr, fArr, strArr);
                }
                if (pageCallin_ShowCall != null) {
                    pageCallin_ShowCall.onNotify(i, iArr, fArr, strArr);
                }
                if (pageCallin_ShowKey != null) {
                    pageCallin_ShowKey.onNotify(i, iArr, fArr, strArr);
                }
                if (pageCallin_HideKey != null) {
                    pageCallin_HideKey.onNotify(i, iArr, fArr, strArr);
                }
            }
        }
        App.getApp().mIpcObj.onNotify(getPageContact(), i, iArr, fArr, strArr);
        App.getApp().mIpcObj.onNotify(getPagePair(), i, iArr, fArr, strArr);
        App.getApp().mIpcObj.onNotify(getPageHistory(), i, iArr, fArr, strArr);
        if (App.mIdCustomer == 86 && Main.mConf_PlatForm == 8 && this.mPageMenu != null && this.mPageMenu.getNotify() != null) {
            App.getApp().mIpcObj.onNotify((Page_Menu) this.mPageMenu.getNotify(), i, iArr, fArr, strArr);
        }
    }

    public void onNotify_Sound(int i, int[] iArr, float[] fArr, String[] strArr) {
        if (this.mPageCurrent != null) {
            IPageNotify notify = this.mPageCurrent.getNotify();
            if (notify instanceof Page_Set) {
                ((Page_Set) notify).onNotify_Sound(i, iArr, fArr, strArr);
            }
        }
    }

    public void onNotify_UniCar(int i, int[] iArr, float[] fArr, String[] strArr) {
        Page_Dial pageDial = getPageDial();
        if (pageDial != null) {
            pageDial.onNotify_UniCar(i, iArr, fArr, strArr);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        App.bResumeByDial = false;
        showOrHideScreenButton("android.fyt.action.SHOW");
        if (this.mPageCurrent != null) {
            this.mPageCurrent.pause();
        }
        bi.a().c();
        if (App.bShowFloatBtn) {
            App.getApp().showFloatBtn(true);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        App.getApp().popBt(false, false);
        if (App.bShowFloatBtn || App.bShowBtInNaviFloatBtn) {
            App.getApp().showFloatBtn(false);
        }
        if (App.sPage == 22 && !App.getApp().mIpcObj.isCalling()) {
            App.sPage = 3;
        }
        if (bv.d() && App.bPop3rdPhone_YF) {
            App.getApp().pop3rdPhone(true);
        }
        goPage(App.sPage, false);
        showOrHideScreenButton("android.fyt.action.HIDE");
        if (App.mIdCustomer == 35) {
            Intent intent = new Intent("com.microntek.bootcheck");
            intent.putExtra("class", "com.microntek*");
            App.getApp().sendBroadcast(intent);
        }
        if (this.mPageCurrent != null) {
            this.mPageCurrent.resume();
        }
        if (!App.bResumeByDial) {
            App.getApp().mIpcObj.requestAppIdRight(App.sPage);
        }
        App.bResumeByDial = false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mPageCurrent != null) {
            switch (this.mPageCurrent.getId()) {
                case 11:
                    Page_Set page_Set = (Page_Set) this.mPageCurrent.getNotify();
                    if (page_Set != null) {
                        page_Set.TouchEvent(motionEvent);
                        break;
                    }
                    break;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public void phoneState() {
        App.getApp().mIpcObj.phoneState(this);
    }

    public void popDeleteContacts(int i, String str) {
        JText jText;
        if (str != null) {
            App.mDelType = i;
            Dialog popDlg = this.ui.getPopDlg(16, App.getApp().getIdStyle("pop_add_contacts_anim"), true);
            JPage page = this.ui.getPage(16);
            if (!(page == null || (jText = (JText) page.getChildViewByid(303)) == null)) {
                jText.setText(str);
            }
            if (popDlg != null) {
                popDlg.show();
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void preCreateActivity(Bundle bundle) {
        super.preCreateActivity(bundle);
        jumpPage(1);
        this.mPageMain = new FrameLayout(this);
        this.mPage.addView(this.mPageMain);
        this.mPageMenu = this.ui.loadPage(true, 2);
        if (this.mPageMenu != null) {
            this.mPage.addView(this.mPageMenu);
        }
    }

    public void resetColor() {
        if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
            String[] strArr = new String[3];
            String[] split = bt.a().a("persist.launcher.color", "0,0,123").split(",");
            App.color = Color.rgb(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
            Page_Dial pageDial = getPageDial();
            if (pageDial != null) {
                pageDial.setButtonColor(App.color);
            }
            Page_Contact pageContact = getPageContact();
            if (pageContact != null) {
                pageContact.setButtonColor(App.color);
            }
            Page_Pair pagePair = getPagePair();
            if (pagePair != null) {
                pagePair.setButtonColor(App.color);
            }
            Page_Set pageSet = getPageSet();
            if (pageSet != null) {
                pageSet.setButtonColor(App.color);
            }
            Page_History pageHistory = getPageHistory();
            if (pageHistory != null) {
                pageHistory.setButtonColor(App.color);
            }
            Page_Av page_Av = (Page_Av) getPageNotify(10);
            if (page_Av != null) {
                page_Av.setButtonColor(App.color);
            }
            Page_Menu page_Menu = (Page_Menu) getPageNotify(2);
            if (page_Menu != null) {
                page_Menu.setButtonColor(App.color);
            }
        }
    }

    public void resetList() {
        if (this.mPageCurrent != null) {
            IPageNotify notify = this.mPageCurrent.getNotify();
            if (notify instanceof Page_Contact) {
                ((Page_Contact) notify).resetList();
            } else if (notify instanceof Page_History) {
                ((Page_History) notify).resetList();
            } else if (notify instanceof Page_Pair) {
                ((Page_Pair) notify).refreshList();
                ((Page_Pair) notify).refreshPairedList();
            } else if ((notify instanceof Page_Dial) && bv.e()) {
                ((Page_Dial) notify).resetList();
            }
        }
    }

    public void scanContact() {
        Page_Contact pageContact = getPageContact();
        if (pageContact != null) {
            pageContact.scanContact();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0039  */
    /* JADX WARNING: Removed duplicated region for block: B:50:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scrollOk() {
        /*
            r5 = this;
            r3 = 0
            r4 = 1
            boolean r0 = r5.bTop
            if (r0 == 0) goto L_0x001f
            boolean r0 = defpackage.bv.d()
            if (r0 == 0) goto L_0x0020
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            if (r0 == 0) goto L_0x001f
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            com.syu.page.IPageNotify r0 = r0.getNotify()
            boolean r1 = r0 instanceof com.syu.bt.page.Page_Contact
            if (r1 == 0) goto L_0x001f
            com.syu.bt.page.Page_Contact r0 = (com.syu.bt.page.Page_Contact) r0
            r0.scrollOk(r4)
        L_0x001f:
            return
        L_0x0020:
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            if (r0 == 0) goto L_0x009c
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            com.syu.page.IPageNotify r0 = r0.getNotify()
            boolean r1 = r0 instanceof com.syu.bt.page.Page_Contact
            if (r1 == 0) goto L_0x0060
            com.syu.bt.page.Page_Contact r0 = (com.syu.bt.page.Page_Contact) r0
            boolean r0 = r0.scrollOk(r3)
            if (r0 == 0) goto L_0x005e
            r0 = r3
        L_0x0037:
            if (r0 == 0) goto L_0x001f
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            android.view.View r1 = r5.getFocus(r0)
            boolean r0 = r1 instanceof com.syu.ctrl.JSwitchButton
            if (r0 == 0) goto L_0x0092
            r0 = r1
            com.syu.ctrl.JSwitchButton r0 = (com.syu.ctrl.JSwitchButton) r0
            r2 = r1
            com.syu.ctrl.JSwitchButton r2 = (com.syu.ctrl.JSwitchButton) r2
            boolean r2 = r2.isChecked()
            if (r2 == 0) goto L_0x0090
        L_0x004f:
            r0.setChecked(r3)
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            com.syu.page.IPageNotify r0 = r0.getNotify()
            if (r0 == 0) goto L_0x001f
            r0.ResponseClick(r1)
            goto L_0x001f
        L_0x005e:
            r0 = r4
            goto L_0x0037
        L_0x0060:
            boolean r1 = r0 instanceof com.syu.bt.page.Page_Dial
            if (r1 == 0) goto L_0x0070
            com.syu.bt.page.Page_Dial r0 = (com.syu.bt.page.Page_Dial) r0
            boolean r0 = r0.scrollOk()
            if (r0 == 0) goto L_0x006e
            r0 = r3
            goto L_0x0037
        L_0x006e:
            r0 = r4
            goto L_0x0037
        L_0x0070:
            boolean r1 = r0 instanceof com.syu.bt.page.Page_History
            if (r1 == 0) goto L_0x0080
            com.syu.bt.page.Page_History r0 = (com.syu.bt.page.Page_History) r0
            boolean r0 = r0.scrollOk()
            if (r0 == 0) goto L_0x007e
            r0 = r3
            goto L_0x0037
        L_0x007e:
            r0 = r4
            goto L_0x0037
        L_0x0080:
            boolean r1 = r0 instanceof com.syu.bt.page.Page_Pair
            if (r1 == 0) goto L_0x009c
            com.syu.bt.page.Page_Pair r0 = (com.syu.bt.page.Page_Pair) r0
            boolean r0 = r0.scrollOk()
            if (r0 == 0) goto L_0x008e
            r0 = r3
            goto L_0x0037
        L_0x008e:
            r0 = r4
            goto L_0x0037
        L_0x0090:
            r3 = r4
            goto L_0x004f
        L_0x0092:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            r1 = 66
            r0.simulateKey(r1)
            goto L_0x001f
        L_0x009c:
            r0 = r4
            goto L_0x0037
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.act.ActBt.scrollOk():void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scrollToNext() {
        /*
            r5 = this;
            r1 = 1
            r2 = 0
            boolean r0 = r5.bTop
            if (r0 == 0) goto L_0x003c
            java.util.List<java.lang.Integer> r0 = r5.mScrollPageList
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x004d
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            int r3 = r0.getId()
            java.util.List<java.lang.Integer> r0 = r5.mScrollPageList
            java.util.Iterator r4 = r0.iterator()
            r1 = r2
        L_0x001b:
            boolean r0 = r4.hasNext()
            if (r0 != 0) goto L_0x003d
        L_0x0021:
            int r0 = r1 + 1
            java.util.List<java.lang.Integer> r3 = r5.mScrollPageList
            int r3 = r3.size()
            if (r0 >= r3) goto L_0x003c
            java.util.List<java.lang.Integer> r0 = r5.mScrollPageList
            int r1 = r1 + 1
            java.lang.Object r0 = r0.get(r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r5.goPage(r0, r2)
        L_0x003c:
            return
        L_0x003d:
            java.lang.Object r0 = r4.next()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 == r3) goto L_0x0021
            int r0 = r1 + 1
            r1 = r0
            goto L_0x001b
        L_0x004d:
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            if (r0 == 0) goto L_0x0096
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            com.syu.page.IPageNotify r0 = r0.getNotify()
            boolean r3 = r0 instanceof com.syu.bt.page.Page_Contact
            if (r3 == 0) goto L_0x006c
            com.syu.bt.page.Page_Contact r0 = (com.syu.bt.page.Page_Contact) r0
            boolean r0 = r0.scrollToNext(r2)
            if (r0 == 0) goto L_0x006a
        L_0x0063:
            if (r2 == 0) goto L_0x003c
            r0 = 2
            r5.onKeyHandle(r0)
            goto L_0x003c
        L_0x006a:
            r2 = r1
            goto L_0x0063
        L_0x006c:
            boolean r3 = r0 instanceof com.syu.bt.page.Page_Dial
            if (r3 == 0) goto L_0x007a
            com.syu.bt.page.Page_Dial r0 = (com.syu.bt.page.Page_Dial) r0
            boolean r0 = r0.scrollToNext()
            if (r0 != 0) goto L_0x0063
            r2 = r1
            goto L_0x0063
        L_0x007a:
            boolean r3 = r0 instanceof com.syu.bt.page.Page_History
            if (r3 == 0) goto L_0x0088
            com.syu.bt.page.Page_History r0 = (com.syu.bt.page.Page_History) r0
            boolean r0 = r0.scrollToNext()
            if (r0 != 0) goto L_0x0063
            r2 = r1
            goto L_0x0063
        L_0x0088:
            boolean r3 = r0 instanceof com.syu.bt.page.Page_Pair
            if (r3 == 0) goto L_0x0096
            com.syu.bt.page.Page_Pair r0 = (com.syu.bt.page.Page_Pair) r0
            boolean r0 = r0.scrollToNext()
            if (r0 != 0) goto L_0x0063
            r2 = r1
            goto L_0x0063
        L_0x0096:
            r2 = r1
            goto L_0x0063
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.act.ActBt.scrollToNext():void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0061  */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void scrollToPrev() {
        /*
            r5 = this;
            r1 = 1
            r2 = 0
            boolean r0 = r5.bTop
            if (r0 == 0) goto L_0x0038
            java.util.List<java.lang.Integer> r0 = r5.mScrollPageList
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0049
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            if (r0 == 0) goto L_0x0038
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            int r3 = r0.getId()
            java.util.List<java.lang.Integer> r0 = r5.mScrollPageList
            java.util.Iterator r4 = r0.iterator()
            r1 = r2
        L_0x001f:
            boolean r0 = r4.hasNext()
            if (r0 != 0) goto L_0x0039
        L_0x0025:
            if (r1 <= 0) goto L_0x0038
            java.util.List<java.lang.Integer> r0 = r5.mScrollPageList
            int r1 = r1 + -1
            java.lang.Object r0 = r0.get(r1)
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            r5.goPage(r0, r2)
        L_0x0038:
            return
        L_0x0039:
            java.lang.Object r0 = r4.next()
            java.lang.Integer r0 = (java.lang.Integer) r0
            int r0 = r0.intValue()
            if (r0 == r3) goto L_0x0025
            int r0 = r1 + 1
            r1 = r0
            goto L_0x001f
        L_0x0049:
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            if (r0 == 0) goto L_0x0091
            com.syu.ctrl.JPage r0 = r5.mPageCurrent
            com.syu.page.IPageNotify r0 = r0.getNotify()
            boolean r3 = r0 instanceof com.syu.bt.page.Page_Contact
            if (r3 == 0) goto L_0x0067
            com.syu.bt.page.Page_Contact r0 = (com.syu.bt.page.Page_Contact) r0
            boolean r0 = r0.scrollToPrev(r2)
            if (r0 == 0) goto L_0x0065
        L_0x005f:
            if (r2 == 0) goto L_0x0038
            r5.onKeyHandle(r1)
            goto L_0x0038
        L_0x0065:
            r2 = r1
            goto L_0x005f
        L_0x0067:
            boolean r3 = r0 instanceof com.syu.bt.page.Page_Dial
            if (r3 == 0) goto L_0x0075
            com.syu.bt.page.Page_Dial r0 = (com.syu.bt.page.Page_Dial) r0
            boolean r0 = r0.scrollToPrev()
            if (r0 != 0) goto L_0x005f
            r2 = r1
            goto L_0x005f
        L_0x0075:
            boolean r3 = r0 instanceof com.syu.bt.page.Page_History
            if (r3 == 0) goto L_0x0083
            com.syu.bt.page.Page_History r0 = (com.syu.bt.page.Page_History) r0
            boolean r0 = r0.scrollToPrev()
            if (r0 != 0) goto L_0x005f
            r2 = r1
            goto L_0x005f
        L_0x0083:
            boolean r3 = r0 instanceof com.syu.bt.page.Page_Pair
            if (r3 == 0) goto L_0x0091
            com.syu.bt.page.Page_Pair r0 = (com.syu.bt.page.Page_Pair) r0
            boolean r0 = r0.scrollToPrev()
            if (r0 != 0) goto L_0x005f
            r2 = r1
            goto L_0x005f
        L_0x0091:
            r2 = r1
            goto L_0x005f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.act.ActBt.scrollToPrev():void");
    }

    public void setMenuBtnEnable(int i, int i2) {
        JPage jPage = this.ui.mPages.get(i);
        if (jPage != null) {
            switch (i2) {
                case 0:
                case 1:
                case 6:
                    JButton jButton = (JButton) jPage.getChildViewByid(35);
                    if (jButton != null) {
                        jButton.setEnabled(false);
                    }
                    JButton jButton2 = (JButton) jPage.getChildViewByid(36);
                    if (jButton2 != null) {
                        jButton2.setEnabled(false);
                    }
                    JButton jButton3 = (JButton) jPage.getChildViewByid(37);
                    if (jButton3 != null) {
                        jButton3.setEnabled(false);
                    }
                    updateMenuSelected(App.sPage, App.sPage, 2, false);
                    return;
                default:
                    JButton jButton4 = (JButton) jPage.getChildViewByid(35);
                    if (jButton4 != null) {
                        jButton4.setEnabled(true);
                    }
                    JButton jButton5 = (JButton) jPage.getChildViewByid(36);
                    if (jButton5 != null) {
                        jButton5.setEnabled(true);
                    }
                    JButton jButton6 = (JButton) jPage.getChildViewByid(37);
                    if (jButton6 != null) {
                        jButton6.setEnabled(true);
                    }
                    updateMenuSelected(App.sPage, App.sPage, 2, true);
                    return;
            }
        }
    }

    public void setSwitch(boolean z) {
        Page_Set pageSet = getPageSet();
        if (pageSet != null) {
            pageSet.setSwitch(z);
        }
    }

    public void showPostLoading(String str) {
        Main.postRunnable_Ui(false, new RunnableShowLoading(str));
    }

    public void showProgressIpPhoneDlg() {
        if (this.mProgressIpPhoneDlg == null) {
            this.mProgressIpPhoneDlg = new ProgressDialog(this, 0);
        }
        if (!this.mProgressIpPhoneDlg.isShowing()) {
            this.mProgressIpPhoneDlg.setTitle(FinalChip.BSP_PLATFORM_Null);
            this.mProgressIpPhoneDlg.setCanceledOnTouchOutside(false);
            this.mProgressIpPhoneDlg.setMessage(App.getApp().getString("ipphone_dailing"));
            this.mProgressIpPhoneDlg.show();
        }
    }

    public void showSystemUI() {
        getWindow().clearFlags(1024);
    }

    public void switch2DialWhenHalfScreen() {
        if (App.mEnableHalfScreen && isHalf() && this.mPageCurrent != null) {
            switch (this.mPageCurrent.getId()) {
                case 10:
                    goPage(10, true);
                    return;
                case 22:
                    goPage(22, true);
                    return;
                default:
                    goPage(3, true);
                    return;
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateBorderText(com.syu.ctrl.JButton r4, boolean r5) {
        /*
            r3 = this;
            if (r4 == 0) goto L_0x000c
            java.lang.String r0 = com.syu.app.App.mStrCustomer
            java.lang.String r1 = "zhtd_aodi"
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 != 0) goto L_0x000d
        L_0x000c:
            return
        L_0x000d:
            r1 = 0
            com.syu.app.MyUi r0 = r3.ui
            android.util.SparseArray<com.syu.ctrl.JPage> r0 = r0.mPages
            r2 = 2
            java.lang.Object r0 = r0.get(r2)
            com.syu.ctrl.JPage r0 = (com.syu.ctrl.JPage) r0
            int r2 = r4.getId()
            switch(r2) {
                case 35: goto L_0x0027;
                case 36: goto L_0x0032;
                case 37: goto L_0x003d;
                case 38: goto L_0x0048;
                case 39: goto L_0x0053;
                case 40: goto L_0x005e;
                default: goto L_0x0020;
            }
        L_0x0020:
            r0 = r1
        L_0x0021:
            if (r0 == 0) goto L_0x000c
            r0.resetText(r5)
            goto L_0x000c
        L_0x0027:
            if (r0 == 0) goto L_0x0020
            r1 = 409(0x199, float:5.73E-43)
            android.view.View r0 = r0.getChildViewByid(r1)
            com.syu.bt.ctrl.BorderTextView r0 = (com.syu.bt.ctrl.BorderTextView) r0
            goto L_0x0021
        L_0x0032:
            if (r0 == 0) goto L_0x0020
            r1 = 410(0x19a, float:5.75E-43)
            android.view.View r0 = r0.getChildViewByid(r1)
            com.syu.bt.ctrl.BorderTextView r0 = (com.syu.bt.ctrl.BorderTextView) r0
            goto L_0x0021
        L_0x003d:
            if (r0 == 0) goto L_0x0020
            r1 = 411(0x19b, float:5.76E-43)
            android.view.View r0 = r0.getChildViewByid(r1)
            com.syu.bt.ctrl.BorderTextView r0 = (com.syu.bt.ctrl.BorderTextView) r0
            goto L_0x0021
        L_0x0048:
            if (r0 == 0) goto L_0x0020
            r1 = 412(0x19c, float:5.77E-43)
            android.view.View r0 = r0.getChildViewByid(r1)
            com.syu.bt.ctrl.BorderTextView r0 = (com.syu.bt.ctrl.BorderTextView) r0
            goto L_0x0021
        L_0x0053:
            if (r0 == 0) goto L_0x0020
            r1 = 413(0x19d, float:5.79E-43)
            android.view.View r0 = r0.getChildViewByid(r1)
            com.syu.bt.ctrl.BorderTextView r0 = (com.syu.bt.ctrl.BorderTextView) r0
            goto L_0x0021
        L_0x005e:
            if (r0 == 0) goto L_0x0020
            r1 = 414(0x19e, float:5.8E-43)
            android.view.View r0 = r0.getChildViewByid(r1)
            com.syu.bt.ctrl.BorderTextView r0 = (com.syu.bt.ctrl.BorderTextView) r0
            goto L_0x0021
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.act.ActBt.updateBorderText(com.syu.ctrl.JButton, boolean):void");
    }

    public void updateBtnFocus(JButton jButton, int i, boolean z) {
        if (i >= 0 && i < 8 && jButton != null) {
            if (z) {
                if (!(this.mBtnFocusLast[i] == null || this.mBtnFocusLast[i] == jButton)) {
                    this.mBtnFocusLast[i].setFocus(false);
                    updateBorderText(this.mBtnFocusLast[i], false);
                    if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
                        this.mBtnFocusLast[i].setSelected(false);
                    }
                }
                this.mBtnFocusLast[i] = jButton;
            }
            jButton.setFocus(z);
            updateBorderText(jButton, z);
            if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
                jButton.setSelected(z);
            }
        }
    }

    public void updateMainSelected(int i, boolean z) {
        JPage jPage = this.ui.mPages.get(1);
        if (jPage != null) {
            updateBtnFocus(i != 10 ? (JButton) jPage.getChildViewByid(33) : (JButton) jPage.getChildViewByid(34), 1, z);
        }
        bi.a().b();
    }

    public void updateMenuSelected(int i, int i2, int i3, boolean z) {
        JPage jPage = this.ui.mPages.get(i);
        updateBtnFocus(getBtnMenuFocus(jPage, i2), i3, z);
        getMenuViewFocus(jPage, i2);
    }

    public void updateMenuSelected_New(int i, int i2, int i3, int i4, boolean z) {
        JPage jPage = this.ui.mPages.get(i);
        updateBtnFocus(getBtnMenuFocus(jPage, i2), i4, z);
        getMenuViewFocus(jPage, i3);
    }

    public void updatePhoneBattery(int i) {
        Page_Av page_Av = (Page_Av) getPageNotify(10);
        if (page_Av != null) {
            page_Av.updatePhoneBattery(i);
        }
    }

    public void updatePhoneName() {
        Page_Dial pageDial = getPageDial();
        if (pageDial != null) {
            pageDial.updatePhoneName();
        }
        Page_Callin pageCallin = getPageCallin();
        if (pageCallin != null) {
            pageCallin.updatePhoneName();
        }
        Page_Callin_HalfScreen pageCallin_HalfScreen = getPageCallin_HalfScreen();
        if (pageCallin_HalfScreen != null) {
            pageCallin_HalfScreen.updatePhoneName();
        }
    }

    public void updatePin(String str) {
        Page_Set pageSet = getPageSet();
        if (pageSet != null) {
            pageSet.updatePin(str);
        }
    }

    public void updateShowDial() {
        Page_Dial pageDial = getPageDial();
        if (pageDial != null) {
            pageDial.updateShowDial();
        }
    }

    public void updateSimLayout(int i) {
        Page_Dial pageDial = getPageDial();
        if (pageDial != null) {
            pageDial.updateSimLayout(i);
        }
    }

    public void updateSoundAmp() {
        if (this.mPageCurrent != null) {
            IPageNotify notify = this.mPageCurrent.getNotify();
            if ((notify instanceof Page_Av) && this.bTop) {
                ((Page_Av) notify).updateSoundAmp();
            }
        }
    }

    public void updateSoundLoud() {
        if (this.mPageCurrent != null && this.bTop) {
            IPageNotify notify = this.mPageCurrent.getNotify();
            if (notify instanceof Page_Av) {
                ((Page_Av) notify).updateSoundLoud();
            }
        }
    }

    public void updateSoundMode() {
        if (this.mPageCurrent != null) {
            IPageNotify notify = this.mPageCurrent.getNotify();
            if ((notify instanceof Page_Av) && this.bTop) {
                ((Page_Av) notify).updateSoundMode();
            }
        }
    }

    public void updateSoundVol() {
        Page_Dial pageDial = getPageDial();
        if (pageDial != null) {
            pageDial.updateSoundVol();
        }
    }

    public void updateSystemTime() {
        if (this.mPageCurrent != null) {
            IPageNotify notify = this.mPageCurrent.getNotify();
            if (this.bTop && (notify instanceof Page_Dial)) {
                ((Page_Dial) notify).updateSystemTime();
            }
        }
    }

    public void voiceSearchContact() {
        Page_Contact pageContact = getPageContact();
        if (pageContact != null) {
            pageContact.voiceSearchContact();
        }
    }
}
