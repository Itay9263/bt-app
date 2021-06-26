package com.syu.bt.page;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.syu.app.App;
import com.syu.app.MyUiItem;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.bt.ctrl.JSwitchButon2;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JSwitchButton;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import com.syu.util.FuncUtils;
import com.syu.util.MySharePreference;
import com.syu.util.Runnable_AnimDrawable;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

public class Page_Set extends Page {
    public static boolean isSetStartDiscovey = false;
    public ActBt actBt;
    public boolean bDevEditFlag = false;
    public boolean bPinEditFlag = false;
    private int idAutoSelect = -1;
    public boolean isAutoAnswer = false;
    public boolean isClickReSet = false;
    public boolean isSetSelfLink = false;
    AnimationDrawable mAnimationDrawable;
    public JSwitchButon2 mBTOnOff;
    public JButton mBtRing;
    JButton mBtnPairSearch;
    public EditText mEditName;
    public EditText mEditPin;
    private View.OnFocusChangeListener mFocus = new View.OnFocusChangeListener() {
        public void onFocusChange(View view, boolean z) {
            if (z) {
                switch (view.getId()) {
                    case 256:
                        Page_Set.this.bDevEditFlag = true;
                        Page_Set.this.bPinEditFlag = false;
                        return;
                    case 257:
                        Page_Set.this.bDevEditFlag = false;
                        Page_Set.this.bPinEditFlag = true;
                        return;
                    default:
                        return;
                }
            } else {
                switch (view.getId()) {
                    case 256:
                        EditText editText = (EditText) view;
                        if (!editText.getText().toString().equals(IpcObj.getDevName()) && TextUtils.isEmpty(IpcObj.getDevName())) {
                            editText.setText(IpcObj.getDevName());
                            Page_Set.this.setBtName(IpcObj.getDevName());
                            return;
                        }
                        return;
                    case 257:
                        EditText editText2 = (EditText) view;
                        if (!editText2.getText().toString().equals(IpcObj.getDevPin())) {
                            editText2.setText(IpcObj.getDevPin());
                            Page_Set.this.setBtPin(IpcObj.getDevPin());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    };
    View mProgLoad;
    Runnable_AnimDrawable mRunnable_AnimDrawable = null;
    public JSwitchButon2 mSB2;
    public JText mTvPin;
    public int[] sb_set = new int[1];
    String strBtAvMicSet;
    String strPhoneMicSet;
    String strRingLevel;
    public JText tvBtAvMicSet;
    public JText tvDevName;
    public JText tvLinkName;
    public JText tvPhoneMicSet;
    public JText tvPhoneState;
    public JText tvPin;
    public JText tvRingName;
    public JText tvRingSet;

    public class RunnableHideText implements Runnable {
        private JText text;

        public RunnableHideText(JText jText) {
            this.text = jText;
        }

        public void run() {
            this.text.setVisibility(8);
        }
    }

    public Page_Set(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void GridClick(JGridView jGridView) {
        int id = jGridView.getId();
        int position = jGridView.getPosition();
        switch (id) {
            case 295:
                String str = null;
                if (App.mBtInfo.mListSetPair != null && App.mBtInfo.mListSetPair.size() > position) {
                    str = (String) App.mBtInfo.mListSetPair.get(position).get(298);
                }
                if (!TextUtils.isEmpty(str) && !IpcObj.getChoiceAddr().equals(str)) {
                    IpcObj.setChoiceAddr(str);
                    App.getApp().mIpcObj.FuncPairLink(this);
                    if (jGridView.adapter != null) {
                        jGridView.adapter.notifyDataSetChanged();
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void GridPressed(JGridView jGridView, JPage jPage, boolean z) {
        if (z) {
            Iterator<WeakReference<JPage>> it = jGridView.mPageInfos.iterator();
            while (it.hasNext()) {
                JPage jPage2 = (JPage) it.next().get();
                if (jPage2 != null) {
                    pressGridItem(jPage2, jPage, z);
                }
            }
        }
        super.GridPressed(jGridView, jPage, z);
    }

    public void InitGridItem(JGridView jGridView, JPage jPage, SparseArray<String> sparseArray, int i) {
        switch (jGridView.getId()) {
            case 295:
                View childViewByid = jPage.getChildViewByid(296);
                if (childViewByid != null) {
                    if (sparseArray.size() == 0) {
                        jPage.setFocus(false);
                        childViewByid.setVisibility(8);
                        return;
                    }
                    childViewByid.setVisibility(0);
                }
                String str = sparseArray.get(298);
                JText jText = (JText) jPage.getChildViewByid(298);
                JText jText2 = (JText) jPage.getChildViewByid(299);
                View childViewByid2 = jPage.getChildViewByid(300);
                if (childViewByid2 == null) {
                    return;
                }
                if (!IpcObj.getPhoneAddr().equals(str) || IpcObj.isDisConnect()) {
                    childViewByid2.setBackgroundResource(App.getApp().getIdDrawable("bt_icocut"));
                    if (jText != null) {
                        jText.setTextColor(-1);
                        jText.setEllipsize(TextUtils.TruncateAt.END);
                        jText.setFocus(false);
                    }
                    if (jText2 != null) {
                        jText2.setTextColor(-1);
                        jText2.setEllipsize(TextUtils.TruncateAt.END);
                        jText2.setFocus(false);
                        return;
                    }
                    return;
                }
                childViewByid2.setBackgroundResource(App.getApp().getIdDrawable("bt_icolink"));
                if (jText != null) {
                    jText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    jText.setTextColor(-16711936);
                    jText.setFocus(true);
                }
                if (jText2 != null) {
                    jText2.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    jText2.setTextColor(-16711936);
                    jText2.setFocus(true);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void ResponseClick(View view) {
        if (!bt.a().d() && !this.actBt.MenuClick(view)) {
            int id = view.getId();
            switch (id) {
                case 116:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.link();
                        return;
                    }
                    return;
                case 117:
                    if (!FuncUtils.isFastDoubleClick()) {
                        IpcObj.cut();
                        return;
                    }
                    return;
                case 239:
                    setAutoAnswer(true);
                    return;
                case 240:
                    setAutoAnswer(false);
                    return;
                case 241:
                    if (this.isAutoAnswer) {
                        IpcObj.setAutoAnswer(false);
                        return;
                    } else {
                        IpcObj.setAutoAnswer(true);
                        return;
                    }
                case 242:
                    EditText editText = (EditText) getPage().getChildViewByid(256);
                    if (editText != null) {
                        String editable = editText.getText().toString();
                        if (TextUtils.isEmpty(editable)) {
                            cb.a().a(App.getApp().getString("bt_device_name_not_null"));
                            return;
                        }
                        cb.a().a(App.getApp().getString("bt_update_successed"));
                        IpcObj.setDevName(editable);
                        switch (App.mIdCustomer) {
                            case 53:
                                if (this.bDevEditFlag) {
                                    this.bDevEditFlag = false;
                                    if (this.mSB2 != null) {
                                        this.mSB2.setFocusableInTouchMode(true);
                                    }
                                    Main.postRunnable_Ui(false, new Runnable() {
                                        public void run() {
                                            ((EditText) Page_Set.this.getPage().getChildViewByid(256)).clearFocus();
                                        }
                                    }, 1000);
                                    if (this.mSB2 != null) {
                                        App.getApp().hideInput(this.mSB2);
                                        break;
                                    }
                                }
                                break;
                        }
                        App.getApp().SaveConfig(editable, App.getDiyPin());
                        return;
                    }
                    return;
                case 244:
                    EditText editText2 = (EditText) getPage().getChildViewByid(257);
                    String editable2 = editText2 != null ? editText2.getText().toString() : this.mTvPin != null ? this.mTvPin.getText().toString() : null;
                    if (TextUtils.isEmpty(editable2) || (!TextUtils.isEmpty(editable2) && editable2.length() < 4)) {
                        cb.a().a(App.getApp().getString("bt_pin_prompt"));
                        return;
                    }
                    cb.a().a(App.getApp().getString("bt_update_successed"));
                    IpcObj.setDevPin(editable2);
                    switch (App.mIdCustomer) {
                        case 53:
                            if (this.bPinEditFlag) {
                                this.bPinEditFlag = false;
                                App.getApp().hideInput(editText2);
                                ((EditText) getPage().getChildViewByid(256)).setFocusableInTouchMode(true);
                                editText2.clearFocus();
                                break;
                            }
                            break;
                    }
                    App.getApp().SaveConfig(App.getDiyName(), editable2);
                    return;
                case 246:
                    this.actBt.uiUtil.a(App.getApp().getString("bt_closing"));
                    if (!this.isClickReSet && App.bBtPowerOnOff) {
                        MySharePreference.saveStringValue("name_diy", App.strDefaultName);
                        MySharePreference.saveStringValue("pin_diy", App.strDefaultPin);
                        App.getApp().SaveConfig(App.strDefaultName, App.strDefaultPin);
                        IpcObj.Reset();
                        this.isClickReSet = true;
                        this.actBt.uiUtil.a(App.getApp().getString("bt_resetting"));
                        return;
                    }
                    return;
                case 248:
                    if (!this.isClickReSet) {
                        popBtRing();
                        return;
                    }
                    return;
                case 250:
                    if (this.isClickReSet) {
                        return;
                    }
                    if (bt.a().b()) {
                        this.actBt.popDeleteContacts(7, App.getApp().getString("bt_update_config"));
                        return;
                    } else {
                        cb.a().a(App.getApp().getString("bt_update_nofile"));
                        return;
                    }
                case 252:
                    MySharePreference.saveIntValue("ringtype", 1);
                    updateRingSelect(1);
                    return;
                case 253:
                    MySharePreference.saveIntValue("ringtype", 2);
                    updateRingSelect(2);
                    return;
                case 254:
                    MySharePreference.saveIntValue("ringtype", 3);
                    updateRingSelect(3);
                    return;
                case 255:
                    if (((JSwitchButton) view).isChecked()) {
                        IpcObj.setAutoAnswer(true);
                        return;
                    } else {
                        IpcObj.setAutoAnswer(false);
                        return;
                    }
                case 259:
                    if (((JSwitchButton) view).isChecked()) {
                        IpcObj.setBtSwitch(true);
                        this.actBt.uiUtil.a(App.getApp().getString("bt_opening"));
                        return;
                    }
                    IpcObj.setBtSwitch(false);
                    this.actBt.uiUtil.a(App.getApp().getString("bt_closing"));
                    return;
                case 260:
                    App.getApp().popBtRingSet(true);
                    return;
                case 267:
                    App.getApp().popPhoneMicSet(true);
                    return;
                case 268:
                    App.getApp().popBtAvMicSet(true);
                    return;
                case 276:
                    if (!App.bBtPowerOnOff) {
                        cb.a().a(App.getApp().getString("bt_opendevice"));
                        return;
                    } else if (App.bBtInitFlag) {
                        if (Main.mConf_PlatForm != 5) {
                            IpcObj.cut();
                        }
                        App.mBtInfo.mListSetPair.clear();
                        resetList_BtSet(App.mBtInfo.mListSetPair);
                        IpcObj.startDiscover();
                        showSetDiscover(true);
                        return;
                    } else {
                        cb.a().a(App.getApp().getString("bt_btinit"));
                        Main.postRunnable_Ui(false, new Runnable() {
                            public void run() {
                                App.bBtInitFlag = true;
                            }
                        }, 3000);
                        return;
                    }
                case 328:
                case 329:
                case 330:
                case 331:
                case 332:
                case 333:
                case 334:
                case 335:
                case 336:
                case 337:
                    if (this.mTvPin != null) {
                        String charSequence = this.mTvPin.getText().toString();
                        this.mTvPin.setText(charSequence.length() > 3 ? String.valueOf(charSequence.substring(1, charSequence.length())) + (id - 328) : String.valueOf(charSequence) + (id - 328));
                        return;
                    }
                    return;
                case 338:
                    if (this.mTvPin != null) {
                        String charSequence2 = this.mTvPin.getText().toString();
                        try {
                            if (!TextUtils.isEmpty(charSequence2)) {
                                this.mTvPin.setText(charSequence2.substring(0, charSequence2.length() - 1));
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
                case 348:
                    this.actBt.Func_Back();
                    return;
                default:
                    return;
            }
        }
    }

    public boolean ResponseLongClick(View view) {
        switch (view.getId()) {
            case 338:
                if (this.mTvPin == null) {
                    return false;
                }
                this.mTvPin.setText(FinalChip.BSP_PLATFORM_Null);
                return false;
            default:
                return false;
        }
    }

    public void TouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 1) {
            if (App.getApp().bPopBtRingSet) {
                App.getApp().popBtRingSet(false);
            }
            if (App.getApp().bPopPhoneMicSet) {
                App.getApp().popPhoneMicSet(false);
            }
            if (App.getApp().bPopBtAvMicSet) {
                App.getApp().popBtAvMicSet(false);
            }
        }
    }

    public void dismissPopBtRing() {
        Dialog popDlg = this.actBt.ui.getPopDlg(20, App.getApp().getIdStyle("pop_add_contacts_anim"), true);
        if (popDlg != null && popDlg.isShowing()) {
            popDlg.dismiss();
        }
    }

    public SparseArray<String> getChoiceName(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (App.mBtInfo.mListSetPair != null) {
            for (SparseArray<String> next : App.mBtInfo.mListSetPair) {
                if (!TextUtils.isEmpty(next.get(298)) && next.get(298).equals(str)) {
                    return next;
                }
            }
        }
        return null;
    }

    public void hideLoadAnim() {
        if (this.mRunnable_AnimDrawable != null) {
            Main.removeRunnable_Ui(this.mRunnable_AnimDrawable);
            this.mRunnable_AnimDrawable.stopRun();
            this.mRunnable_AnimDrawable = null;
        }
        if (this.mBtnPairSearch != null) {
            this.mBtnPairSearch.setVisibility(0);
        }
        if (this.mProgLoad != null) {
            this.mProgLoad.setVisibility(8);
        }
    }

    public void init() {
        LinearLayout linearLayout;
        String[] strDrawableExtra;
        this.mEditName = (EditText) getPage().getChildViewByid(256);
        if (this.mEditName != null) {
            this.mEditName.setText(IpcObj.getDevName());
            this.mEditName.setSingleLine();
            this.mEditName.setEms(16);
            this.mEditName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
            this.mEditName.setEllipsize(TextUtils.TruncateAt.END);
            this.mEditName.setOnFocusChangeListener(this.mFocus);
            this.mEditName.setInputType(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
        }
        this.mEditPin = (EditText) getPage().getChildViewByid(257);
        if (this.mEditPin != null) {
            String diyPin = App.getDiyPin();
            if (!diyPin.equals(Bt.sDevPin)) {
                IpcObj.setDevPin(diyPin);
            }
            this.mEditPin.setText(diyPin);
            this.mEditPin.setEms(4);
            this.mEditPin.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
            this.mEditPin.setSingleLine();
            this.mEditPin.setInputType(2);
            this.mEditPin.setOnFocusChangeListener(this.mFocus);
        }
        this.mTvPin = (JText) getPage().getChildViewByid(339);
        if (this.mTvPin != null) {
            this.mTvPin.setText(IpcObj.getDevPin());
        }
        this.tvDevName = (JText) getPage().getChildViewByid(263);
        if (this.tvDevName != null) {
            this.tvDevName.setText(IpcObj.getDevName());
        }
        this.tvPin = (JText) getPage().getChildViewByid(265);
        if (this.tvPin != null) {
            this.tvPin.setText(IpcObj.getDevPin());
        }
        this.tvPhoneState = (JText) getPage().getChildViewByid(61);
        int i = Bt.DATA[9];
        if (this.tvPhoneState != null && i >= 0 && i <= 6) {
            this.tvPhoneState.setText(App.getApp().getString(App.mStrStates[i]));
        }
        this.tvLinkName = (JText) getPage().getChildViewByid(69);
        if (this.tvLinkName != null) {
            this.tvLinkName.setText(Bt.sPhoneName);
        }
        this.mSB2 = (JSwitchButon2) getPage().getChildViewByid(340);
        if (this.mSB2 != null) {
            this.mSB2.setOnSwitchStateLisenter(new JSwitchButon2.OnSwitchStateLisenter() {
                public void onSwitchOff() {
                    Page_Set.this.setAutoAnswer(false);
                }

                public void onSwitchOn() {
                    Page_Set.this.setAutoAnswer(true);
                }
            });
        }
        this.mBTOnOff = (JSwitchButon2) getPage().getChildViewByid(341);
        if (this.mBTOnOff != null) {
            this.mBTOnOff.setOnSwitchStateLisenter(new JSwitchButon2.OnSwitchStateLisenter() {
                public void onSwitchOff() {
                    IpcObj.setBtSwitch(false);
                    Page_Set.this.actBt.uiUtil.a(App.getApp().getString("bt_closing"));
                }

                public void onSwitchOn() {
                    IpcObj.setBtSwitch(true);
                    Page_Set.this.actBt.uiUtil.a(App.getApp().getString("bt_opening"));
                }
            });
        }
        this.tvRingSet = (JText) getPage().getChildViewByid(262);
        this.tvPhoneMicSet = (JText) getPage().getChildViewByid(269);
        this.tvBtAvMicSet = (JText) getPage().getChildViewByid(270);
        this.mBtnPairSearch = (JButton) getPage().getChildViewByid(276);
        this.tvRingName = (JText) getPage().getChildViewByid(261);
        View childViewByid = getPage().getChildViewByid(271);
        if (App.mBtType == 4 || App.mBtType == 6) {
            if (!bv.d() || App.mBtType != 4) {
                bt.a().a("BT.bin");
            } else {
                bt.a().a("bc5update.dfu");
            }
        } else if (childViewByid != null) {
            childViewByid.setVisibility(8);
        }
        this.mProgLoad = getPage().getChildViewByid(272);
        if (!(this.mProgLoad == null || (strDrawableExtra = ((MyUiItem) this.mProgLoad.getTag()).getStrDrawableExtra()) == null || strDrawableExtra.length <= 0)) {
            this.mAnimationDrawable = (AnimationDrawable) this.actBt.ui.getDrawableFromPath(strDrawableExtra[0]);
        }
        View childViewByid2 = getPage().getChildViewByid(273);
        if (childViewByid2 != null) {
            childViewByid2.setVisibility(bv.k() ? 8 : 0);
        }
        if (bv.h() && (linearLayout = (LinearLayout) getPage().getChildViewByid(274)) != null) {
            linearLayout.setFocusable(true);
            linearLayout.setFocusableInTouchMode(true);
        }
        setButtonColor(App.color);
        initTypeface();
    }

    public void initTypeface() {
        if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
            if (this.mEditName != null) {
                this.mEditName.setTypeface(bt.a((Context) this.actBt));
            }
            if (this.mEditPin != null) {
                this.mEditPin.setTypeface(bt.a((Context) this.actBt));
            }
            JText jText = (JText) getPage().getChildViewByid(258);
            if (jText != null) {
                jText.setTypeface(bt.a((Context) this.actBt));
            }
            if (this.tvRingName != null) {
                this.tvRingName.setTypeface(bt.a((Context) this.actBt));
            }
            JButton jButton = (JButton) getPage().getChildViewByid(242);
            if (jButton != null) {
                jButton.setTypeface(bt.a((Context) this.actBt));
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(244);
            if (jButton2 != null) {
                jButton2.setTypeface(bt.a((Context) this.actBt));
            }
            JButton jButton3 = (JButton) getPage().getChildViewByid(248);
            if (jButton3 != null) {
                jButton3.setTypeface(bt.a((Context) this.actBt));
            }
            JButton jButton4 = (JButton) getPage().getChildViewByid(246);
            if (jButton4 != null) {
                jButton4.setTypeface(bt.a((Context) this.actBt));
            }
            JText jText2 = (JText) getPage().getChildViewByid(243);
            if (jText2 != null) {
                jText2.setTypeface(bt.a((Context) this.actBt));
            }
            JText jText3 = (JText) getPage().getChildViewByid(245);
            if (jText3 != null) {
                jText3.setTypeface(bt.a((Context) this.actBt));
            }
            JText jText4 = (JText) getPage().getChildViewByid(247);
            if (jText4 != null) {
                jText4.setTypeface(bt.a((Context) this.actBt));
            }
            JText jText5 = (JText) getPage().getChildViewByid(249);
            if (jText5 != null) {
                jText5.setTypeface(bt.a((Context) this.actBt));
            }
            JText jText6 = (JText) getPage().getChildViewByid(251);
            if (jText6 != null) {
                jText6.setTypeface(bt.a((Context) this.actBt));
            }
        }
    }

    public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        App.getApp().mIpcObj.onNotify(this, i, iArr, fArr, strArr);
    }

    public void onNotify_Sound(int i, int[] iArr, float[] fArr, String[] strArr) {
        App.getApp().mIpcObj.onNotify_Sound(this, i, iArr, fArr, strArr);
    }

    public void pause() {
        if (this.mEditName != null) {
            this.mEditName.clearFocus();
            App.getApp().hideInput(this.mEditName);
        }
        if (this.mEditPin != null) {
            this.mEditPin.clearFocus();
            App.getApp().hideInput(this.mEditPin);
        }
        dismissPopBtRing();
    }

    public void popBtRing() {
        Dialog popDlg = this.actBt.ui.getPopDlg(20, App.getApp().getIdStyle("pop_add_contacts_anim"), true);
        if (popDlg != null) {
            popDlg.show();
        }
    }

    public void popPinPwd() {
        Dialog popDlg = this.actBt.ui.getPopDlg(18, App.getApp().getIdStyle("pop_add_contacts_anim"), true);
        if (popDlg != null) {
            popDlg.show();
        }
    }

    public void popPwd() {
        Dialog popDlg = this.actBt.ui.getPopDlg(18, App.getApp().getIdStyle("pop_add_contacts_anim"), true);
        if (popDlg != null) {
            popDlg.show();
        }
    }

    public void pressGridItem(JPage jPage, JPage jPage2, boolean z) {
    }

    public void resetList_BtSet(List<SparseArray<String>> list) {
        JGridView jGridView = (JGridView) getPage().getChildViewByid(295);
        if (jGridView != null) {
            App.resetList(jGridView, list);
        }
    }

    public void resume() {
        JPage jPage;
        int i;
        if (this.actBt.bTop) {
            App.getApp().mIpcObj.updateNotify_Set();
            App.getApp().mIpcObj.recoverAppId();
            if (this.tvPhoneMicSet != null) {
                this.tvPhoneMicSet.setFocusable(true);
                this.tvPhoneMicSet.setFocusableInTouchMode(true);
                this.tvPhoneMicSet.requestFocus();
            }
            if (this.mEditName != null) {
                this.mEditName.setText(IpcObj.getDevName());
                this.mEditName.clearFocus();
            }
            if (this.mEditPin != null) {
                if (!TextUtils.isEmpty(Bt.sDevPin)) {
                    this.mEditPin.setText(IpcObj.getDevPin());
                } else {
                    String diyPin = App.getDiyPin();
                    if (!TextUtils.isEmpty(diyPin)) {
                        IpcObj.setDevPin(diyPin);
                        this.mEditPin.setText(IpcObj.getDevPin());
                    }
                }
            }
            if (this.mTvPin != null) {
                this.mTvPin.setText(IpcObj.getDevPin());
            }
            if (this.tvDevName != null) {
                this.tvDevName.setText(IpcObj.getDevName());
            }
            if (this.tvPin != null) {
                this.tvPin.setText(IpcObj.getDevPin());
            }
            if (this.tvPhoneState != null && (i = Bt.DATA[9]) >= 0 && i <= 6) {
                this.tvPhoneState.setText(App.getApp().getString(App.mStrStates[i]));
            }
            if (this.tvLinkName != null) {
                this.tvLinkName.setText(Bt.sPhoneName);
            }
            if (this.tvRingSet != null) {
                this.strRingLevel = String.valueOf(App.iRingLevel);
                this.tvRingSet.setText(this.strRingLevel);
            }
            if (this.mBTOnOff != null) {
                this.mBTOnOff.setSwitch(App.bBtPowerOnOff);
            }
            if (this.tvPhoneMicSet != null) {
                App.SB = App.getApp().mIpcObj.getSB();
                App.iPhoneMicSet = App.SB[2];
                this.strPhoneMicSet = String.valueOf(App.iPhoneMicSet);
                this.tvPhoneMicSet.setText(this.strPhoneMicSet);
            }
            if (this.tvBtAvMicSet != null) {
                App.SB = App.getApp().mIpcObj.getSB();
                App.iBtAvMicSet = App.SB[3];
                this.strBtAvMicSet = String.valueOf(App.iBtAvMicSet);
                this.tvBtAvMicSet.setText(this.strBtAvMicSet);
            }
            if (this.tvRingName != null) {
                String stringValue = MySharePreference.getStringValue("name_ring");
                if (!TextUtils.isEmpty(stringValue)) {
                    this.tvRingName.setText(stringValue);
                } else {
                    this.tvRingName.setText("Unknow");
                }
            }
            if (!(!App.bDelMenuFromBtAv || (jPage = this.actBt.ui.mPages.get(2)) == null || jPage.getVisibility() == 0)) {
                jPage.setVisibility(0);
            }
            resetList_BtSet(App.mBtInfo.mListSetPair);
            updateRingSelect(MySharePreference.getIntValue("ringtype", 3));
        }
    }

    public void setAutoAnswer(boolean z) {
        updateAutoSelected(z ? 239 : 240, true);
        IpcObj.setAutoAnswer(z);
    }

    public void setBtName(String str) {
        JText jText = (JText) getPage().getChildViewByid(263);
        if (jText != null) {
            jText.setText(str);
        }
        JText jText2 = (JText) getPage().getChildViewByid(264);
        if (jText2 != null) {
            jText2.setText(str);
        }
    }

    public void setBtPin(String str) {
        JText jText = (JText) getPage().getChildViewByid(265);
        if (jText != null) {
            jText.setText(str);
        }
        JText jText2 = (JText) getPage().getChildViewByid(266);
        if (jText2 != null) {
            jText2.setText(str);
        }
    }

    public void setButtonColor(int i) {
        if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
            int[] iArr = {i};
            int[][] iArr2 = {new int[]{16842919}};
            JButton jButton = (JButton) getPage().getChildViewByid(242);
            if (jButton != null) {
                jButton.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(244);
            if (jButton2 != null) {
                jButton2.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton2.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton3 = (JButton) getPage().getChildViewByid(246);
            if (jButton3 != null) {
                jButton3.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton3.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void setPressed(JButton jButton, boolean z) {
        updateSelected(jButton.getId(), !z);
    }

    public void setSwitch(boolean z) {
        if (this.mBTOnOff != null) {
            this.mBTOnOff.setSwitch(z);
        }
    }

    public void showLoadAnim() {
        if (this.mBtnPairSearch != null) {
            this.mBtnPairSearch.setVisibility(8);
        }
        if (this.mProgLoad != null) {
            this.mProgLoad.setVisibility(0);
        }
        if (this.mRunnable_AnimDrawable != null) {
            Main.removeRunnable_Ui(this.mRunnable_AnimDrawable);
            this.mRunnable_AnimDrawable.stopRun();
            this.mRunnable_AnimDrawable = null;
        }
        this.mRunnable_AnimDrawable = new Runnable_AnimDrawable(this.mProgLoad, this.mAnimationDrawable, 80);
        Main.postRunnable_Ui(false, this.mRunnable_AnimDrawable);
    }

    public void showSetDiscover(boolean z) {
        if (z) {
            showLoadAnim();
            isSetStartDiscovey = true;
        } else if (isSetStartDiscovey) {
            isSetStartDiscovey = false;
            hideLoadAnim();
        }
    }

    public void updateAutoSelected(int i, boolean z) {
        if (z && this.idAutoSelect != i) {
            updateAutoSelected(this.idAutoSelect, false);
            this.idAutoSelect = i;
        }
        JButton jButton = (JButton) getPage().getChildViewByid(i);
        if (jButton != null) {
            jButton.setFocus(z);
        }
    }

    public void updatePin(String str) {
        if (this.mEditName != null) {
            this.mEditName.setText(str);
        }
    }

    public void updateRingName(String str) {
        if (this.tvRingName != null) {
            this.tvRingName.setText(str);
        }
    }

    public void updateRingSelect(int i) {
        boolean z = true;
        JButton jButton = (JButton) getPage().getChildViewByid(252);
        JButton jButton2 = (JButton) getPage().getChildViewByid(253);
        JButton jButton3 = (JButton) getPage().getChildViewByid(254);
        if (jButton != null) {
            jButton.setFocus(i == 1);
        }
        if (jButton2 != null) {
            jButton2.setFocus(i == 2);
        }
        if (jButton3 != null) {
            if (i != 3) {
                z = false;
            }
            jButton3.setFocus(z);
        }
    }

    public void updateSelected(int i, boolean z) {
        switch (i) {
            case 239:
            case 240:
                break;
            default:
                i = -1;
                break;
        }
        if (i != -1 && this.idAutoSelect != i && this.idAutoSelect > 0) {
            updateAutoSelected(this.idAutoSelect, z);
        }
    }
}
