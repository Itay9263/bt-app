package com.syu.bt.page;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.ContentObserver;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.Bt_Info;
import com.syu.bt.act.ActBt;
import com.syu.bt.ctrl.SlideBarView;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.ctrl.JView;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import com.syu.util.FuncUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Page_Contact extends Page {
    public static SparseArray<String> mClick = new SparseArray<>();
    public static String strSearch;
    public ActBt actBt;
    private boolean bRoolKey = false;
    boolean bTop = false;
    boolean editFocus = false;
    ContentObserver mContactsObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean z) {
            super.onChange(z);
            if (!App.mBtInfo.bSaving && !App.mBtInfo.bClearing_Contact) {
                Page_Contact.this.scanContact();
            }
        }
    };
    public boolean mDownloading = false;
    public EditText mEditSearch;
    public boolean mGridClickDial = false;
    public JGridView mGridView;
    public int mInputType;
    private int mPosFocusCpy = -1;
    private int mPosMoveTo = -1;
    public Runnable mRunnableQueryContact = new Runnable() {
        public void run() {
            App.mBtInfo.queryContacts();
            if (!App.bDownloading) {
                Page_Contact.this.actBt.disPostLoading(0);
            }
            Main.postRunnable_Ui(true, Page_Contact.this.mRunnable_resetList);
        }
    };
    Runnable mRunnableSearchRunnable = new Runnable() {
        public void run() {
            for (int i = 0; i < App.mBtInfo.mPinYin.length; i++) {
                char c = App.mBtInfo.mPinYin[i];
                JButton jButton = (JButton) Page_Contact.this.getPage().getChildViewByid(i + 377);
                if ((c < 'A' || c > 'Z') && c != '#') {
                    if (jButton != null) {
                        jButton.setEnabled(false);
                    }
                } else if (jButton != null) {
                    jButton.setEnabled(true);
                }
            }
        }
    };
    Runnable mRunnable_resetList = new Runnable() {
        public void run() {
            Page_Contact.this.actBt.resetList();
        }
    };
    public SlideBarView mSlideBar;
    public TextView tvLetterTitle;
    public JText tvTxtSum;
    public JText tvTxtSumStr;

    public class MyTextWatcher implements TextWatcher {
        private EditText editSearch;

        public MyTextWatcher(EditText editText) {
            this.editSearch = editText;
        }

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Page_Contact.strSearch = this.editSearch.getText().toString();
            this.editSearch.setSelection(Page_Contact.strSearch.length());
            Page_Contact.this.queryContacts(Page_Contact.strSearch);
        }
    }

    public Page_Contact(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    private void downloadPhoneBook() {
        if (IpcObj.isDisConnect()) {
            cb.a().a(App.getApp().getString("bt_state_disconnected"));
        } else if (IpcObj.isPairing()) {
        } else {
            if (App.bDownloading && bv.d()) {
                cb.a().a(App.getApp().getString("bt_downloading"));
            } else if (App.mBtInfo.mListContact != null && App.mBtInfo.mListContact.size() > 0) {
                this.actBt.popDeleteContacts(1, App.getApp().getString("bt_clear_and_reload"));
            } else if (App.bAutoDownPhoneBook) {
                IpcObj.downloadBook();
            } else {
                App.bShowLoading = false;
                IpcObj.downloadBook();
                if (bv.k() && !App.bDownloading && App.bShowLoading) {
                    this.actBt.uiUtil.a(App.getApp().getString("bt_download"));
                }
            }
        }
    }

    public void GridClick(JGridView jGridView) {
        switch (jGridView.getId()) {
            case 171:
                if (!FuncUtils.isFastDoubleClick()) {
                    int position = jGridView.getPosition();
                    if (App.mSelectContact > 0) {
                        bz.a("contact_save" + App.mSelectContact, jGridView.getList().get(position));
                        App.mSelectContact = -1;
                        this.actBt.goPage(3, false);
                        return;
                    } else if (this.mGridClickDial) {
                        SparseArray sparseArray = jGridView.getList().get(position);
                        String str = (String) sparseArray.get(180);
                        String str2 = (String) sparseArray.get(178);
                        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                            this.mPosMoveTo = -1;
                            if (!str.equals(mClick.get(180)) || !str2.equals(mClick.get(178))) {
                                mClick.put(180, str);
                                mClick.put(178, str2);
                                if (!bv.d() && !App.mStrCustomer.equalsIgnoreCase("HAODA") && IpcObj.isStateConnect()) {
                                    IpcObj.itemDial(str);
                                    this.actBt.goPage(3, false);
                                }
                            } else if (IpcObj.isStateConnect()) {
                                IpcObj.itemDial(str);
                                this.actBt.goPage(3, false);
                            }
                            if ((bv.d() || App.mStrCustomer.equalsIgnoreCase("HAODA")) && this.mGridView.adapter != null) {
                                this.mGridView.adapter.notifyDataSetChanged();
                                return;
                            }
                            return;
                        }
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

    public void GridLongClick(JGridView jGridView) {
        switch (jGridView.getId()) {
            case 171:
                mClick.clear();
                SparseArray sparseArray = jGridView.getList().get(jGridView.getPosition());
                if (sparseArray != null && sparseArray.size() > 0) {
                    this.actBt.popDeleteContacts(2, App.getApp().getString("bt_is_delete_one"));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void GridPressed(JGridView jGridView, JPage jPage, boolean z) {
        if (jGridView.mPageInfos != null) {
            Iterator<WeakReference<JPage>> it = jGridView.mPageInfos.iterator();
            while (it.hasNext()) {
                JPage jPage2 = (JPage) it.next().get();
                if (jPage2 != null) {
                    pressGridItem(jPage2, jPage, z);
                }
            }
            if (!this.mGridClickDial && App.mSelectContact < 0) {
                if (jPage.getChildViewByid(187) == null) {
                    this.mGridClickDial = true;
                }
                if (App.mIdCustomer == 63 || bv.d() || bv.i() || App.mStrCustomer.equalsIgnoreCase("HAODA")) {
                    this.mGridClickDial = true;
                }
            }
        }
    }

    public void InitGridItem(JGridView jGridView, JPage jPage, SparseArray<String> sparseArray, int i) {
        int indexOf;
        int indexOf2;
        switch (jGridView.getId()) {
            case 171:
                View childViewByid = jPage.getChildViewByid(174);
                if (childViewByid != null) {
                    if (sparseArray.size() == 0) {
                        childViewByid.setVisibility(8);
                    } else {
                        childViewByid.setVisibility(0);
                    }
                }
                if (sparseArray.size() > 0) {
                    JText jText = (JText) jPage.getChildViewByid(182);
                    JText jText2 = (JText) jPage.getChildViewByid(181);
                    String str = sparseArray.get(180);
                    String str2 = sparseArray.get(178);
                    boolean z = false;
                    boolean z2 = false;
                    if (!TextUtils.isEmpty(strSearch)) {
                        if (isNumber(strSearch) && !TextUtils.isEmpty(str) && (indexOf2 = str.toUpperCase().indexOf(strSearch.toUpperCase())) >= 0) {
                            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(str);
                            spannableStringBuilder.setSpan(new ForegroundColorSpan(-16711936), indexOf2, strSearch.length() + indexOf2, 33);
                            jText.setText(spannableStringBuilder);
                            z = true;
                        }
                        if (!TextUtils.isEmpty(str2) && (indexOf = str2.toUpperCase().indexOf(strSearch.toUpperCase())) >= 0) {
                            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(str2);
                            spannableStringBuilder2.setSpan(new ForegroundColorSpan(-16711936), indexOf, strSearch.length() + indexOf, 33);
                            jText2.setText(spannableStringBuilder2);
                            z2 = true;
                        }
                    }
                    if (!z) {
                        if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
                            jText.setTypeface(bt.a((Context) this.actBt));
                        }
                        jText.setText(str);
                    }
                    if (!z2) {
                        if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
                            jText2.setTypeface(bt.a((Context) this.actBt));
                        }
                        jText2.setText(str2);
                    }
                    if (bv.h() || bv.e()) {
                        JButton jButton = (JButton) jPage.getChildViewByid(188);
                        if (jButton != null) {
                            String str3 = sparseArray.get(183);
                            if (!TextUtils.isEmpty(str3)) {
                                if (str3.equals(App.bFavorite ? "1" : "collect")) {
                                    jButton.setFocus(true);
                                } else {
                                    jButton.setFocus(false);
                                }
                            } else {
                                jButton.setFocus(false);
                            }
                        }
                        JView jView = (JView) jPage.getChildViewByid(175);
                        if (jView != null) {
                            String str4 = sparseArray.get(186);
                            if (!TextUtils.isEmpty(str4)) {
                                jView.setBackgroundResource(App.getApp().getIdDrawable("bt_zimu_" + str4));
                            } else {
                                jView.setBackgroundResource(App.getApp().getIdDrawable("bt_zimu_1"));
                            }
                        }
                    }
                    boolean z3 = false;
                    if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                        if (this.mPosMoveTo >= 0 && this.mPosMoveTo == i) {
                            mClick.put(180, str);
                            mClick.put(178, str2);
                        }
                        if (str.equals(mClick.get(180)) && str2.equals(mClick.get(178))) {
                            z3 = true;
                        }
                    }
                    if (bv.d() || App.mStrCustomer.equalsIgnoreCase("HAODA") || this.bRoolKey) {
                        jPage.setFocus(z3);
                        pressGridItem(jPage, jPage, z3);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void ResponseClick(View view) {
        if (!bt.a().d()) {
            clearPageFocus();
            int id = view.getId();
            switch (id) {
                case 97:
                    if (App.getApp().isHalfScreenAble()) {
                        App.getApp().setFullScreenMode(0);
                        break;
                    }
                    break;
                case 163:
                    clearContacts();
                    break;
                case 164:
                case 165:
                    voiceSearchContact();
                    break;
                case 166:
                    downloadPhoneBook();
                    break;
                case 167:
                    App.mBtInfo.savePhoneBook();
                    break;
                case 168:
                    if (this.mEditSearch != null) {
                        strSearch = this.mEditSearch.getText().toString();
                        if (!TextUtils.isEmpty(strSearch)) {
                            strSearch = strSearch.subSequence(0, strSearch.length() - 1).toString();
                            this.mEditSearch.setText(strSearch.length() == 0 ? FinalChip.BSP_PLATFORM_Null : strSearch);
                            break;
                        }
                    }
                    break;
                case 169:
                    App.bIsFavList = true;
                    resetList();
                    break;
                case 170:
                    App.bIsFavList = false;
                    resetList();
                    break;
                case 348:
                    this.actBt.Func_Back();
                    break;
                case 377:
                case 378:
                case 379:
                case 380:
                case 381:
                case 382:
                case 383:
                case 384:
                case 385:
                case 386:
                case 387:
                case 388:
                case 389:
                case 390:
                case 391:
                case 392:
                case 393:
                case 394:
                case 395:
                case 396:
                case 397:
                case 398:
                case 399:
                case 400:
                case 401:
                case 402:
                case 403:
                    String str = Bt_Info.b[id - 377];
                    int positionForSection = getPositionForSection(str.charAt(0));
                    if (!(this.mGridView == null || this.mGridView.adapter == null)) {
                        if (!"#".equals(str)) {
                            if (positionForSection != -1) {
                                this.mGridView.notifyDataSetChanged();
                                if (this.mGridView.adapter != null) {
                                    this.mGridView.scrollToPosition(positionForSection);
                                    break;
                                }
                            }
                        } else {
                            this.mGridView.scrollToPosition(0);
                            break;
                        }
                    }
                    break;
            }
            if (!App.getApp().mIpcObj.isCalling() || view.getId() == 35) {
                if (this.actBt.MenuClick(view)) {
                }
            } else {
                cb.a().a(App.getApp().getString("bt_state_using"));
            }
        }
    }

    public boolean ResponseLongClick(View view) {
        int id = view.getId();
        clearPageFocus();
        switch (id) {
            case 168:
                if (this.mEditSearch == null) {
                    return false;
                }
                EditText editText = this.mEditSearch;
                strSearch = FinalChip.BSP_PLATFORM_Null;
                editText.setText(FinalChip.BSP_PLATFORM_Null);
                return false;
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public void clearContacts() {
        if (App.mBtInfo.mListContact != null && App.mBtInfo.mListContact.size() > 0) {
            this.actBt.popDeleteContacts(1, App.getApp().getString("bt_is_delete"));
        }
    }

    public void clearEditFocus() {
        if (this.mEditSearch != null && this.editFocus) {
            this.mEditSearch.clearFocus();
            App.getApp().hideInput(this.mEditSearch);
        }
    }

    public void clearPageFocus() {
        this.mPosMoveTo = -1;
        this.mPosFocusCpy = -1;
        mClick.clear();
        if ((bv.d() || App.mStrCustomer.equalsIgnoreCase("HAODA")) && this.mGridView.adapter != null) {
            this.mGridView.adapter.notifyDataSetChanged();
        }
    }

    public boolean dialByKey() {
        if (!this.bTop || !this.actBt.isFocus(this.mGridView)) {
            return false;
        }
        if (!TextUtils.isEmpty(mClick.get(180))) {
            IpcObj.setNum(mClick.get(180));
        }
        return true;
    }

    public void endDownload() {
        this.actBt.uiUtil.a();
        if (!App.bAutoDownPhoneBook && !App.bAutoSavePhoneBook && App.mBtInfo.mListContact != null && App.mBtInfo.mListContact.size() > 0) {
            this.actBt.popDeleteContacts(5, App.getApp().getString("bt_need_save"));
        }
        if (App.mIdCustomer == 21) {
            cb.a().a(String.valueOf(App.getApp().getString("bt_load_completed")) + String.valueOf(App.mBtInfo.mListContact.size()));
        }
    }

    public int getPositionForSection(int i) {
        int i2 = 0;
        for (SparseArray<String> sparseArray : App.mBtInfo.mListContact) {
            String b = bx.b((String) sparseArray.get(178));
            if (!TextUtils.isEmpty(b) && b.toUpperCase().charAt(0) == i) {
                return i2;
            }
            i2++;
        }
        return -1;
    }

    public void init() {
        LinearLayout linearLayout;
        this.mEditSearch = (EditText) getPage().getChildViewByid(162);
        if (this.mEditSearch != null) {
            this.mInputType = this.mEditSearch.getInputType();
            this.mEditSearch.setSingleLine();
            this.mEditSearch.setFocusableInTouchMode(true);
            this.mEditSearch.setFocusable(true);
            this.mEditSearch.setEllipsize(TextUtils.TruncateAt.START);
            this.mEditSearch.setImeOptions(6);
            this.mEditSearch.setHint(App.getApp().getString("search"));
            this.mEditSearch.addTextChangedListener(new MyTextWatcher(this.mEditSearch));
            this.mEditSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View view, boolean z) {
                    Page_Contact.this.editFocus = z;
                }
            });
            this.mEditSearch.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (Page_Contact.this.mEditSearch == null) {
                        return false;
                    }
                    Page_Contact.this.clearPageFocus();
                    Page_Contact.this.mEditSearch.clearFocus();
                    Page_Contact.this.mEditSearch.requestFocus();
                    App.getApp().showInput(Page_Contact.this.mEditSearch);
                    return false;
                }
            });
            if (App.mStrCustomer.equalsIgnoreCase("TZY_NEW")) {
                this.mEditSearch.setTypeface(bt.a((Context) this.actBt));
            }
        }
        this.mGridView = (JGridView) getPage().getChildViewByid(171);
        this.mGridView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        if (Page_Contact.this.mEditSearch == null) {
                            return false;
                        }
                        Page_Contact.this.mEditSearch.clearFocus();
                        App.getApp().hideInput(Page_Contact.this.mEditSearch);
                        return false;
                    default:
                        return false;
                }
            }
        });
        this.mSlideBar = (SlideBarView) getPage().getChildViewByid(342);
        this.tvLetterTitle = (TextView) getPage().getChildViewByid(343);
        this.tvTxtSum = (JText) getPage().getChildViewByid(172);
        this.tvTxtSumStr = (JText) getPage().getChildViewByid(173);
        if (this.mSlideBar != null) {
            this.mSlideBar.setTextView(this.tvLetterTitle);
            this.mSlideBar.setOnTouchLettersChangedListener(new SlideBarView.OnTouchLettersChangedListener() {
                public void onTouchLettersChanged(String str) {
                    int positionForSection = Page_Contact.this.getPositionForSection(str.charAt(0));
                    if (Page_Contact.this.mGridView != null && Page_Contact.this.mGridView.adapter != null) {
                        if ("#".equals(str)) {
                            Page_Contact.this.mGridView.scrollToPosition(0);
                        } else if (positionForSection != -1) {
                            Page_Contact.this.mGridView.notifyDataSetChanged();
                            if (Page_Contact.this.mGridView.adapter != null) {
                                Page_Contact.this.mGridView.scrollToPosition(positionForSection);
                            }
                        }
                    }
                }
            });
        }
        if (bv.h() && (linearLayout = (LinearLayout) getPage().getChildViewByid(274)) != null) {
            linearLayout.setFocusable(true);
            linearLayout.setFocusableInTouchMode(true);
        }
        setButtonColor(App.color);
    }

    public boolean isNumber(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public void notifyDataSetChanged() {
        if (this.mGridView != null) {
            this.mGridView.notifyDataSetChanged();
        }
    }

    public void pause() {
        this.bTop = false;
        App.mContentResolver.unregisterContentObserver(this.mContactsObserver);
        if (this.mEditSearch != null) {
            EditText editText = this.mEditSearch;
            strSearch = FinalChip.BSP_PLATFORM_Null;
            editText.setText(FinalChip.BSP_PLATFORM_Null);
            this.mEditSearch.clearFocus();
            App.getApp().hideInput(this.mEditSearch);
        }
        if (!App.bAutoDownPhoneBook && App.bDownloading) {
            this.actBt.uiUtil.a();
        }
        if (App.mBtInfo.bClearing_Contact) {
            this.actBt.uiUtil.a();
        }
    }

    public void pressGridItem(JPage jPage, JPage jPage2, boolean z) {
        boolean z2 = z && jPage == jPage2;
        View childViewByid = jPage.getChildViewByid(175);
        if (childViewByid != null && (childViewByid instanceof JView)) {
            ((JView) childViewByid).setFocus(z2);
        }
        View childViewByid2 = jPage.getChildViewByid(187);
        if (childViewByid2 != null && (childViewByid2 instanceof JButton)) {
            ((JButton) childViewByid2).setFocus(z2);
        }
        JText jText = (JText) jPage.getChildViewByid(181);
        if (jText != null) {
            jText.setFocus(z2);
        }
        JText jText2 = (JText) jPage.getChildViewByid(182);
        if (jText2 != null) {
            jText2.setFocus(z2);
        }
    }

    public void queryContacts(String str) {
        if (TextUtils.isEmpty(str)) {
            resetList(App.mBtInfo.mListContact);
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (isNumber(str)) {
            for (SparseArray next : App.mBtInfo.mListContact) {
                String str2 = (String) next.get(180);
                if (bv.d()) {
                    if (str2.contains(str) || str2.contains(str.trim())) {
                        arrayList.add(next);
                    }
                } else if (str2.contains(str)) {
                    arrayList.add(next);
                }
            }
            resetList(arrayList);
            return;
        }
        for (SparseArray next2 : App.mBtInfo.mListContact) {
            String str3 = (String) next2.get(178);
            if (bv.d()) {
                String trim = str.trim();
                if (str3.toUpperCase().contains(str.toUpperCase()) || str3.toUpperCase().contains(trim.toUpperCase()) || bx.b(str3).startsWith(str.toUpperCase()) || bx.b(str3).startsWith(trim.toUpperCase())) {
                    arrayList.add(next2);
                }
            } else if (str3.toUpperCase().contains(str.toUpperCase()) || bx.b(str3).startsWith(str.toUpperCase())) {
                arrayList.add(next2);
            }
        }
        resetList(arrayList);
    }

    public void removePinYinSearch() {
        Main.removeRunnable_Ui(this.mRunnableSearchRunnable);
    }

    public void resetButtonState(List<SparseArray<String>> list) {
        if (App.bIsFavList) {
            JButton jButton = (JButton) getPage().getChildViewByid(169);
            if (jButton != null) {
                jButton.setFocus(true);
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(170);
            if (jButton2 != null) {
                jButton2.setFocus(false);
            }
        } else {
            JButton jButton3 = (JButton) getPage().getChildViewByid(169);
            if (jButton3 != null) {
                jButton3.setFocus(false);
            }
            JButton jButton4 = (JButton) getPage().getChildViewByid(170);
            if (jButton4 != null) {
                jButton4.setFocus(true);
            }
        }
        removePinYinSearch();
        String[] strArr = new String[27];
        for (int i = 0; i < App.mBtInfo.mPinYin.length; i++) {
            App.mBtInfo.mPinYin[i] = ' ';
            strArr[i] = Bt_Info.b[i];
        }
        if (IpcObj.isConnect()) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                list.get(i2).put(186, FinalChip.BSP_PLATFORM_Null);
                char charAt = ((String) list.get(i2).get(179)).charAt(0);
                if (charAt < 'A' || charAt > 'Z') {
                    App.mBtInfo.mPinYin[0] = '#';
                    if (!TextUtils.isEmpty(strArr[0])) {
                        list.get(i2).put(186, "0");
                        strArr[0] = FinalChip.BSP_PLATFORM_Null;
                    }
                } else {
                    App.mBtInfo.mPinYin[charAt - '@'] = charAt;
                    if (!TextUtils.isEmpty(strArr[charAt - '@'])) {
                        list.get(i2).put(186, strArr[charAt - '@'].toLowerCase());
                        strArr[charAt - '@'] = FinalChip.BSP_PLATFORM_Null;
                    }
                }
            }
        }
        showPinYinSearch();
    }

    public void resetList() {
        mClick.clear();
        this.mPosMoveTo = -1;
        this.mPosFocusCpy = -1;
        if (App.bIsFavList) {
            App.mBtInfo.sortContact_Fav();
            resetList(App.mBtInfo.mListFavContact);
            return;
        }
        App.mBtInfo.sortContact();
        resetList(App.mBtInfo.mListContact);
    }

    public void resetList(List<SparseArray<String>> list) {
        if (bv.h()) {
            resetButtonState(list);
        }
        if (!IpcObj.isConnect()) {
            App.resetList(this.mGridView, (List<SparseArray<String>>) null);
        } else {
            App.resetList(this.mGridView, list);
        }
        if (list.size() <= 0 || !IpcObj.isConnect()) {
            if (this.tvTxtSum != null) {
                this.tvTxtSum.setVisibility(8);
            }
            if (this.tvTxtSumStr != null) {
                this.tvTxtSumStr.setVisibility(8);
                return;
            }
            return;
        }
        if (this.tvTxtSum != null) {
            this.tvTxtSum.setText(Integer.toString(list.size()));
            this.tvTxtSum.setVisibility(0);
        }
        if (this.tvTxtSumStr != null) {
            this.tvTxtSumStr.setVisibility(0);
        }
    }

    public void resume() {
        JPage jPage;
        if (this.actBt.bTop) {
            App.bIsFavList = false;
            App.bPageInitForDownLoadContact = true;
            this.bTop = true;
            App.getApp().mIpcObj.recoverAppId();
            App.mContentResolver.registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, this.mContactsObserver);
            resetList();
            if (App.getApp().bPopBtRingSet) {
                App.getApp().popBtRingSet(false);
            }
            if (!(!App.bDelMenuFromBtAv || (jPage = this.actBt.ui.mPages.get(2)) == null || jPage.getVisibility() == 0)) {
                jPage.setVisibility(0);
            }
            if (!App.bAutoDownPhoneBook && App.bDownloading && bv.d()) {
                this.actBt.uiUtil.a(App.getApp().getString("bt_download"));
            } else if (IpcObj.isConnect() && !App.bHasDownBook && App.bAutoDownPhoneBook && App.mBtInfo.mListContact.size() <= 0) {
                IpcObj.downloadBook();
            }
            if (App.mBtInfo.bClearing_Contact && App.mBtInfo.mListContact.size() > 0) {
                this.actBt.uiUtil.a(App.getApp().getString("bt_deling"));
            }
        }
    }

    public void scanContact() {
        App.startThread(App.StrThreadDBContact, this.mRunnableQueryContact, true, 1);
        App.queryCallLog();
    }

    public boolean scrollOk(boolean z) {
        if (this.bTop) {
            this.bRoolKey = true;
            if (this.actBt.isFocus(this.mGridView) || z) {
                if (!TextUtils.isEmpty(mClick.get(180))) {
                    IpcObj.itemDial(mClick.get(180));
                }
                return true;
            }
        }
        return false;
    }

    public boolean scrollToNext(boolean z) {
        if (!this.bTop) {
            return false;
        }
        clearEditFocus();
        this.bRoolKey = true;
        if (!this.actBt.isFocus(this.mGridView) && !z) {
            return false;
        }
        if (this.mPosFocusCpy != this.mGridView.mIndexFocus) {
            this.mPosFocusCpy = this.mGridView.mIndexFocus;
            this.mPosMoveTo = this.mPosFocusCpy;
        }
        if (this.mPosMoveTo < 0) {
            this.mPosMoveTo = 0;
        } else if (this.mPosMoveTo + 1 >= this.mGridView.getList().size()) {
            return false;
        } else {
            this.mPosMoveTo++;
        }
        this.mGridView.scrollToPosition(this.mPosMoveTo);
        return true;
    }

    public boolean scrollToPrev(boolean z) {
        if (!this.bTop) {
            return false;
        }
        this.bRoolKey = true;
        clearEditFocus();
        if (!this.actBt.isFocus(this.mGridView) && !z) {
            return false;
        }
        if (this.mPosFocusCpy != this.mGridView.mIndexFocus) {
            this.mPosFocusCpy = this.mGridView.mIndexFocus;
            this.mPosMoveTo = this.mPosFocusCpy;
        }
        if (this.mPosMoveTo < 0) {
            this.mPosMoveTo = 0;
        } else if (this.mPosMoveTo <= 0) {
            return false;
        } else {
            this.mPosMoveTo--;
        }
        this.mGridView.scrollToPosition(this.mPosMoveTo);
        return true;
    }

    public void setButtonColor(int i) {
        if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
            int[] iArr = {i};
            int[][] iArr2 = {new int[]{16842919}};
            JButton jButton = (JButton) getPage().getChildViewByid(168);
            if (jButton != null) {
                jButton.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(164);
            if (jButton2 != null) {
                jButton2.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton2.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton3 = (JButton) getPage().getChildViewByid(166);
            if (jButton3 != null) {
                jButton3.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton3.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton4 = (JButton) getPage().getChildViewByid(167);
            if (jButton4 != null) {
                jButton4.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton4.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton5 = (JButton) getPage().getChildViewByid(163);
            if (jButton5 != null) {
                jButton5.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton5.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void setContactNum(String str) {
        if (this.tvTxtSum != null) {
            if (this.tvTxtSum.getVisibility() != 0) {
                this.tvTxtSum.setVisibility(0);
            }
            this.tvTxtSum.setText(str);
        }
        if (this.tvTxtSumStr != null && this.tvTxtSumStr.getVisibility() != 0) {
            this.tvTxtSumStr.setVisibility(0);
        }
    }

    public void showPinYinSearch() {
        Main.postRunnable_Ui(false, this.mRunnableSearchRunnable, 1000);
    }

    public void voiceSearchContact() {
        clearPageFocus();
        if (this.mEditSearch != null) {
            this.mEditSearch.clearFocus();
            this.mEditSearch.requestFocus();
            App.getApp().showInput(this.mEditSearch);
        }
        if (bv.d()) {
            this.actBt.sendBroadcast(new Intent("speak_click"));
        }
    }
}
