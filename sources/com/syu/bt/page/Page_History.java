package com.syu.bt.page;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.View;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.ctrl.JView;
import com.syu.page.Page;
import com.syu.util.FuncUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class Page_History extends Page {
    public ActBt actBt;
    private boolean bRoolKey = false;
    boolean bTop = false;
    private String checkPhoneNumber2;
    public int iTypeHistorySelect = -1;
    public boolean mGridClickDial = false;
    JGridView mGridView;
    JGridView mGridViewSearch;
    private int mPosFocusCpy1 = -1;
    private int mPosFocusCpy2 = -1;
    private int mPosMoveTo1 = -1;
    private int mPosMoveTo2 = -1;
    SparseArray<String> mapSelect = new SparseArray<>();
    public JText tvTxtNumber;

    public class MyTextWatcher implements TextWatcher {
        private JText jTxtNumber;

        public MyTextWatcher(JText jText) {
            this.jTxtNumber = jText;
        }

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Page_History.this.queryContacts(this.jTxtNumber.getText().toString());
        }
    }

    public Page_History(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void GridClick(JGridView jGridView) {
        switch (jGridView.getId()) {
            case 157:
                if (!FuncUtils.isFastDoubleClick()) {
                    SparseArray sparseArray = jGridView.getList().get(jGridView.getPosition());
                    if (!TextUtils.isEmpty((CharSequence) sparseArray.get(180))) {
                        IpcObj.itemDial((String) sparseArray.get(180));
                        return;
                    }
                    return;
                }
                return;
            case 196:
                if (this.mGridClickDial && !FuncUtils.isFastDoubleClick()) {
                    if (!App.hideBtnWhenDisconnect) {
                        SparseArray sparseArray2 = jGridView.getList().get(jGridView.getPosition());
                        String str = (String) sparseArray2.get(203);
                        String str2 = (String) sparseArray2.get(200);
                        this.mPosMoveTo1 = -1;
                        if (App.mStrCustomer.equalsIgnoreCase("HAODA")) {
                            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                                if (str.equals(this.mapSelect.get(203)) && str2.equals(this.mapSelect.get(200))) {
                                    IpcObj.itemDial(this.mapSelect.get(203));
                                }
                                this.mapSelect.put(203, str);
                                this.mapSelect.put(200, str2);
                            }
                            if (this.mGridView.adapter != null) {
                                this.mGridView.adapter.notifyDataSetChanged();
                                return;
                            }
                            return;
                        }
                        this.mapSelect.put(203, str);
                        this.mapSelect.put(200, str2);
                        if (!TextUtils.isEmpty(this.mapSelect.get(203))) {
                            IpcObj.itemDial(this.mapSelect.get(203));
                            return;
                        }
                        return;
                    } else if (this.mGridView.adapter != null) {
                        this.mGridView.adapter.notifyDataSetChanged();
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
        SparseArray sparseArray;
        switch (jGridView.getId()) {
            case 196:
                if (!App.hideBtnWhenDisconnect && (sparseArray = jGridView.getList().get(jGridView.getPosition())) != null && sparseArray.size() > 0) {
                    this.actBt.popDeleteContacts(4, App.getApp().getString("bt_is_delete_onerecord"));
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void GridPressed(JGridView jGridView, JPage jPage, boolean z) {
        if (jGridView.mPageInfos != null) {
            switch (jGridView.getId()) {
                case 157:
                    Iterator<WeakReference<JPage>> it = jGridView.mPageInfos.iterator();
                    while (it.hasNext()) {
                        JPage jPage2 = (JPage) it.next().get();
                        if (jPage2 != null) {
                            pressGridItem2(jPage2, jPage, z);
                        }
                    }
                    return;
                case 196:
                    Iterator<WeakReference<JPage>> it2 = jGridView.mPageInfos.iterator();
                    while (it2.hasNext()) {
                        JPage jPage3 = (JPage) it2.next().get();
                        if (jPage3 != null) {
                            pressGridItem1(jPage3, jPage, z);
                        }
                    }
                    if (!this.mGridClickDial) {
                        if (jPage.getChildViewByid(206) == null) {
                            this.mGridClickDial = true;
                        }
                        if (App.mIdCustomer == 63 || bv.i()) {
                            this.mGridClickDial = true;
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:118:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0137  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void InitGridItem(com.syu.ctrl.JGridView r9, com.syu.ctrl.JPage r10, android.util.SparseArray<java.lang.String> r11, int r12) {
        /*
            r8 = this;
            int r0 = r9.getId()
            switch(r0) {
                case 157: goto L_0x0202;
                case 196: goto L_0x0008;
                default: goto L_0x0007;
            }
        L_0x0007:
            return
        L_0x0008:
            r0 = 197(0xc5, float:2.76E-43)
            android.view.View r0 = r10.getChildViewByid(r0)
            if (r0 == 0) goto L_0x0020
            int r1 = r11.size()
            if (r1 != 0) goto L_0x001c
            r1 = 8
            r0.setVisibility(r1)
            goto L_0x0007
        L_0x001c:
            r1 = 0
            r0.setVisibility(r1)
        L_0x0020:
            r0 = 203(0xcb, float:2.84E-43)
            java.lang.Object r0 = r11.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            r1 = 201(0xc9, float:2.82E-43)
            java.lang.Object r1 = r11.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            r2 = 201(0xc9, float:2.82E-43)
            android.view.View r2 = r10.getChildViewByid(r2)
            com.syu.ctrl.JText r2 = (com.syu.ctrl.JText) r2
            r3 = 203(0xcb, float:2.84E-43)
            android.view.View r3 = r10.getChildViewByid(r3)
            com.syu.ctrl.JText r3 = (com.syu.ctrl.JText) r3
            r4 = 202(0xca, float:2.83E-43)
            android.view.View r4 = r10.getChildViewByid(r4)
            com.syu.ctrl.JText r4 = (com.syu.ctrl.JText) r4
            r5 = 204(0xcc, float:2.86E-43)
            android.view.View r5 = r10.getChildViewByid(r5)
            com.syu.ctrl.JText r5 = (com.syu.ctrl.JText) r5
            r6 = 0
            boolean r7 = android.text.TextUtils.isEmpty(r1)
            if (r7 == 0) goto L_0x0058
            r6 = 1
        L_0x0058:
            if (r6 == 0) goto L_0x0146
            if (r2 == 0) goto L_0x0066
            boolean r1 = defpackage.bv.h()
            if (r1 == 0) goto L_0x013f
            r1 = 0
            r2.setVisibility(r1)
        L_0x0066:
            if (r4 == 0) goto L_0x006d
            r1 = 8
            r4.setVisibility(r1)
        L_0x006d:
            if (r3 == 0) goto L_0x0089
            r1 = 0
            r3.setVisibility(r1)
            java.lang.String r1 = com.syu.app.App.mStrCustomer
            java.lang.String r2 = "TZY_NEW"
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x0086
            com.syu.bt.act.ActBt r1 = r8.actBt
            android.graphics.Typeface r1 = defpackage.bt.a((android.content.Context) r1)
            r3.setTypeface(r1)
        L_0x0086:
            r3.setText(r0)
        L_0x0089:
            if (r5 == 0) goto L_0x00a5
            r1 = 0
            r5.setVisibility(r1)
            java.lang.String r1 = com.syu.app.App.mStrCustomer
            java.lang.String r2 = "TZY_NEW"
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x00a2
            com.syu.bt.act.ActBt r1 = r8.actBt
            android.graphics.Typeface r1 = defpackage.bt.a((android.content.Context) r1)
            r5.setTypeface(r1)
        L_0x00a2:
            r5.setText(r0)
        L_0x00a5:
            r1 = 198(0xc6, float:2.77E-43)
            android.view.View r2 = r10.getChildViewByid(r1)
            if (r2 == 0) goto L_0x00db
            java.lang.Object r1 = r2.getTag()
            com.syu.app.MyUiItem r1 = (com.syu.app.MyUiItem) r1
            java.lang.String[] r3 = r1.getParaStr()
            if (r3 == 0) goto L_0x01bd
            boolean r3 = r2 instanceof com.syu.ctrl.JView
            if (r3 == 0) goto L_0x00db
            com.syu.ctrl.JView r2 = (com.syu.ctrl.JView) r2
            r3 = 0
            java.lang.String[] r4 = r1.getParaStr()
            int r5 = com.syu.app.App.sCallsType
            switch(r5) {
                case 1: goto L_0x01ae;
                case 2: goto L_0x01b3;
                case 3: goto L_0x01b8;
                default: goto L_0x00c9;
            }
        L_0x00c9:
            if (r3 != 0) goto L_0x0263
            java.lang.String r1 = r1.getStrDrawable()
        L_0x00cf:
            java.lang.String r3 = r2.getStrDrawable()
            if (r3 == r1) goto L_0x00db
            if (r1 == 0) goto L_0x00db
            r3 = 1
            r2.setStrDrawable(r1, r3)
        L_0x00db:
            r2 = 0
            r1 = 200(0xc8, float:2.8E-43)
            java.lang.Object r1 = r11.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            boolean r3 = android.text.TextUtils.isEmpty(r1)
            if (r3 != 0) goto L_0x0260
            boolean r3 = android.text.TextUtils.isEmpty(r0)
            if (r3 != 0) goto L_0x0260
            int r3 = r8.mPosMoveTo1
            if (r3 < 0) goto L_0x0106
            int r3 = r8.mPosMoveTo1
            if (r3 != r12) goto L_0x0106
            android.util.SparseArray<java.lang.String> r3 = r8.mapSelect
            r4 = 203(0xcb, float:2.84E-43)
            r3.put(r4, r0)
            android.util.SparseArray<java.lang.String> r3 = r8.mapSelect
            r4 = 200(0xc8, float:2.8E-43)
            r3.put(r4, r1)
        L_0x0106:
            android.util.SparseArray<java.lang.String> r3 = r8.mapSelect
            r4 = 203(0xcb, float:2.84E-43)
            java.lang.Object r3 = r3.get(r4)
            boolean r0 = r0.equals(r3)
            if (r0 == 0) goto L_0x0260
            android.util.SparseArray<java.lang.String> r0 = r8.mapSelect
            r3 = 200(0xc8, float:2.8E-43)
            java.lang.Object r0 = r0.get(r3)
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0260
            r0 = 1
        L_0x0123:
            boolean r1 = defpackage.bv.d()
            if (r1 != 0) goto L_0x0137
            java.lang.String r1 = com.syu.app.App.mStrCustomer
            java.lang.String r2 = "HAODA"
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 != 0) goto L_0x0137
            boolean r1 = r8.bRoolKey
            if (r1 == 0) goto L_0x0007
        L_0x0137:
            r10.setFocus(r0)
            r8.pressGridItem1(r10, r10, r0)
            goto L_0x0007
        L_0x013f:
            r1 = 8
            r2.setVisibility(r1)
            goto L_0x0066
        L_0x0146:
            if (r2 == 0) goto L_0x0162
            r2.setText(r1)
            java.lang.String r1 = com.syu.app.App.mStrCustomer
            java.lang.String r6 = "TZY_NEW"
            boolean r1 = r1.equalsIgnoreCase(r6)
            if (r1 == 0) goto L_0x015e
            com.syu.bt.act.ActBt r1 = r8.actBt
            android.graphics.Typeface r1 = defpackage.bt.a((android.content.Context) r1)
            r2.setTypeface(r1)
        L_0x015e:
            r1 = 0
            r2.setVisibility(r1)
        L_0x0162:
            if (r4 == 0) goto L_0x017e
            r4.setText(r0)
            r1 = 0
            r4.setVisibility(r1)
            java.lang.String r1 = com.syu.app.App.mStrCustomer
            java.lang.String r2 = "TZY_NEW"
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x017e
            com.syu.bt.act.ActBt r1 = r8.actBt
            android.graphics.Typeface r1 = defpackage.bt.a((android.content.Context) r1)
            r4.setTypeface(r1)
        L_0x017e:
            if (r3 == 0) goto L_0x018a
            boolean r1 = defpackage.bv.h()
            if (r1 == 0) goto L_0x01a8
            r1 = 0
            r3.setVisibility(r1)
        L_0x018a:
            if (r5 == 0) goto L_0x00a5
            java.lang.String r1 = com.syu.app.App.mStrCustomer
            java.lang.String r2 = "TZY_NEW"
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x019f
            com.syu.bt.act.ActBt r1 = r8.actBt
            android.graphics.Typeface r1 = defpackage.bt.a((android.content.Context) r1)
            r5.setTypeface(r1)
        L_0x019f:
            r1 = 0
            r5.setVisibility(r1)
            r5.setText(r0)
            goto L_0x00a5
        L_0x01a8:
            r1 = 8
            r3.setVisibility(r1)
            goto L_0x018a
        L_0x01ae:
            r3 = 0
            r3 = r4[r3]
            goto L_0x00c9
        L_0x01b3:
            r3 = 1
            r3 = r4[r3]
            goto L_0x00c9
        L_0x01b8:
            r3 = 2
            r3 = r4[r3]
            goto L_0x00c9
        L_0x01bd:
            boolean r1 = com.syu.app.App.bHistoryLogAllFlag
            if (r1 == 0) goto L_0x01e1
            r1 = 199(0xc7, float:2.79E-43)
            java.lang.Object r1 = r11.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            int r1 = java.lang.Integer.parseInt(r1)
        L_0x01cd:
            switch(r1) {
                case 1: goto L_0x01d2;
                case 2: goto L_0x01e4;
                case 3: goto L_0x01f3;
                default: goto L_0x01d0;
            }
        L_0x01d0:
            goto L_0x00db
        L_0x01d2:
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icoincall"
            int r1 = r1.getIdDrawable(r3)
            r2.setBackgroundResource(r1)
            goto L_0x00db
        L_0x01e1:
            int r1 = com.syu.app.App.sCallsType
            goto L_0x01cd
        L_0x01e4:
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icooutcall"
            int r1 = r1.getIdDrawable(r3)
            r2.setBackgroundResource(r1)
            goto L_0x00db
        L_0x01f3:
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icomisscall"
            int r1 = r1.getIdDrawable(r3)
            r2.setBackgroundResource(r1)
            goto L_0x00db
        L_0x0202:
            r0 = 158(0x9e, float:2.21E-43)
            android.view.View r0 = r10.getChildViewByid(r0)
            if (r0 == 0) goto L_0x0215
            int r1 = r11.size()
            if (r1 != 0) goto L_0x025b
            r1 = 8
            r0.setVisibility(r1)
        L_0x0215:
            int r0 = r11.size()
            if (r0 <= 0) goto L_0x0007
            r0 = 160(0xa0, float:2.24E-43)
            android.view.View r0 = r10.getChildViewByid(r0)
            com.syu.ctrl.JText r0 = (com.syu.ctrl.JText) r0
            r1 = 159(0x9f, float:2.23E-43)
            android.view.View r1 = r10.getChildViewByid(r1)
            com.syu.ctrl.JText r1 = (com.syu.ctrl.JText) r1
            r2 = 180(0xb4, float:2.52E-43)
            java.lang.Object r2 = r11.get(r2)
            java.lang.String r2 = (java.lang.String) r2
            r3 = 178(0xb2, float:2.5E-43)
            java.lang.Object r3 = r11.get(r3)
            java.lang.String r3 = (java.lang.String) r3
            if (r0 == 0) goto L_0x0240
            r0.setText(r2)
        L_0x0240:
            if (r1 == 0) goto L_0x0245
            r1.setText(r3)
        L_0x0245:
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 != 0) goto L_0x0007
            int r0 = r8.mPosMoveTo2
            if (r0 < 0) goto L_0x0007
            int r0 = r8.mPosMoveTo2
            if (r0 != r12) goto L_0x0007
            r8.checkPhoneNumber2 = r2
            r0 = 1
            r8.pressGridItem2(r10, r10, r0)
            goto L_0x0007
        L_0x025b:
            r1 = 0
            r0.setVisibility(r1)
            goto L_0x0215
        L_0x0260:
            r0 = r2
            goto L_0x0123
        L_0x0263:
            r1 = r3
            goto L_0x00cf
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.page.Page_History.InitGridItem(com.syu.ctrl.JGridView, com.syu.ctrl.JPage, android.util.SparseArray, int):void");
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void ResponseClick(android.view.View r9) {
        /*
            r8 = this;
            r7 = 35
            r0 = 2
            r1 = 1
            r2 = 3
            r3 = 0
            bt r4 = defpackage.bt.a()
            boolean r4 = r4.d()
            if (r4 == 0) goto L_0x0011
        L_0x0010:
            return
        L_0x0011:
            int r5 = r9.getId()
            int r4 = com.syu.app.App.sCallsType
            switch(r5) {
                case 189: goto L_0x002d;
                case 190: goto L_0x001b;
                case 191: goto L_0x002f;
                case 192: goto L_0x001a;
                case 193: goto L_0x001a;
                case 194: goto L_0x001a;
                case 195: goto L_0x0031;
                default: goto L_0x001a;
            }
        L_0x001a:
            r0 = r4
        L_0x001b:
            int r4 = com.syu.app.App.sCallsType
            if (r0 == r4) goto L_0x007c
            android.util.SparseArray<java.lang.String> r2 = r8.mapSelect
            r2.clear()
            com.syu.app.App.sCallsType = r0
            r8.resetList()
            r8.updateHistorySelected(r0, r1)
            goto L_0x0010
        L_0x002d:
            r0 = r1
            goto L_0x001b
        L_0x002f:
            r0 = r2
            goto L_0x001b
        L_0x0031:
            boolean r6 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r6 != 0) goto L_0x0010
            boolean r6 = com.syu.app.App.bDownloadingRecord
            if (r6 != 0) goto L_0x0010
            boolean r6 = defpackage.bv.h()
            if (r6 == 0) goto L_0x0058
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryAll
            if (r0 == 0) goto L_0x0056
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryAll
            int r0 = r0.size()
            if (r0 > 0) goto L_0x0056
            com.syu.ipcself.ModuleProxy r0 = com.syu.ipcself.module.main.Bt.PROXY
            r0.cmd(r7)
        L_0x0056:
            r0 = r3
            goto L_0x001b
        L_0x0058:
            int r6 = com.syu.app.App.sCallsType
            if (r6 != r2) goto L_0x0065
            com.syu.ipcself.ModuleProxy r0 = com.syu.ipcself.module.main.Bt.PROXY
            r6 = 37
            r0.cmd(r6)
            r0 = r4
            goto L_0x001b
        L_0x0065:
            int r6 = com.syu.app.App.sCallsType
            if (r6 != r0) goto L_0x0070
            com.syu.ipcself.ModuleProxy r0 = com.syu.ipcself.module.main.Bt.PROXY
            r0.cmd(r7)
            r0 = r4
            goto L_0x001b
        L_0x0070:
            int r0 = com.syu.app.App.sCallsType
            if (r0 != r1) goto L_0x001a
            com.syu.ipcself.ModuleProxy r0 = com.syu.ipcself.module.main.Bt.PROXY
            r6 = 36
            r0.cmd(r6)
            goto L_0x001a
        L_0x007c:
            switch(r5) {
                case 97: goto L_0x0123;
                case 101: goto L_0x0155;
                case 103: goto L_0x013d;
                case 104: goto L_0x013d;
                case 105: goto L_0x013d;
                case 106: goto L_0x013d;
                case 107: goto L_0x013d;
                case 108: goto L_0x013d;
                case 109: goto L_0x013d;
                case 110: goto L_0x013d;
                case 111: goto L_0x013d;
                case 112: goto L_0x013d;
                case 113: goto L_0x013d;
                case 114: goto L_0x013d;
                case 120: goto L_0x014a;
                case 192: goto L_0x0088;
                case 193: goto L_0x00be;
                case 194: goto L_0x00f5;
                case 348: goto L_0x0136;
                default: goto L_0x007f;
            }
        L_0x007f:
            com.syu.bt.act.ActBt r0 = r8.actBt
            boolean r0 = r0.MenuClick(r9)
            if (r0 == 0) goto L_0x0010
            goto L_0x0010
        L_0x0088:
            r0 = 0
            boolean r1 = com.syu.app.App.bHistoryLogAllFlag
            if (r1 == 0) goto L_0x00a9
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryAll
        L_0x0091:
            if (r0 == 0) goto L_0x007f
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x007f
            com.syu.bt.act.ActBt r0 = r8.actBt
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_is_delete_allrecords"
            java.lang.String r1 = r1.getString(r3)
            r0.popDeleteContacts(r2, r1)
            goto L_0x007f
        L_0x00a9:
            int r1 = com.syu.app.App.sCallsType
            switch(r1) {
                case 1: goto L_0x00af;
                case 2: goto L_0x00b4;
                case 3: goto L_0x00b9;
                default: goto L_0x00ae;
            }
        L_0x00ae:
            goto L_0x0091
        L_0x00af:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryIn
            goto L_0x0091
        L_0x00b4:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryOut
            goto L_0x0091
        L_0x00b9:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryMiss
            goto L_0x0091
        L_0x00be:
            r0 = 0
            boolean r1 = com.syu.app.App.bHistoryLogAllFlag
            if (r1 == 0) goto L_0x00e0
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryAll
        L_0x00c7:
            if (r0 == 0) goto L_0x007f
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x007f
            com.syu.bt.act.ActBt r0 = r8.actBt
            r1 = 4
            com.syu.app.App r2 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_is_delete_onerecord"
            java.lang.String r2 = r2.getString(r3)
            r0.popDeleteContacts(r1, r2)
            goto L_0x007f
        L_0x00e0:
            int r1 = com.syu.app.App.sCallsType
            switch(r1) {
                case 1: goto L_0x00e6;
                case 2: goto L_0x00eb;
                case 3: goto L_0x00f0;
                default: goto L_0x00e5;
            }
        L_0x00e5:
            goto L_0x00c7
        L_0x00e6:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryIn
            goto L_0x00c7
        L_0x00eb:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryOut
            goto L_0x00c7
        L_0x00f0:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryMiss
            goto L_0x00c7
        L_0x00f5:
            android.util.SparseArray<java.lang.String> r0 = r8.mapSelect
            if (r0 == 0) goto L_0x007f
            android.util.SparseArray<java.lang.String> r0 = r8.mapSelect
            r1 = 203(0xcb, float:2.84E-43)
            java.lang.Object r0 = r0.get(r1)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x007f
            boolean r0 = com.syu.app.ipc.IpcObj.isStateConnect()
            if (r0 == 0) goto L_0x007f
            android.util.SparseArray<java.lang.String> r0 = r8.mapSelect
            r1 = 203(0xcb, float:2.84E-43)
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            com.syu.app.ipc.IpcObj.itemDial(r0)
            com.syu.bt.act.ActBt r0 = r8.actBt
            r0.goPage(r2, r3)
            goto L_0x007f
        L_0x0123:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            boolean r0 = r0.isHalfScreenAble()
            if (r0 == 0) goto L_0x007f
            com.syu.app.App r0 = com.syu.app.App.getApp()
            r0.setFullScreenMode(r3)
            goto L_0x007f
        L_0x0136:
            com.syu.bt.act.ActBt r0 = r8.actBt
            r0.Func_Back()
            goto L_0x007f
        L_0x013d:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            com.syu.app.ipc.IpcObj r0 = r0.mIpcObj
            int r1 = r5 + -103
            r0.cmdNum(r1)
            goto L_0x007f
        L_0x014a:
            boolean r0 = defpackage.bv.a()
            if (r0 == 0) goto L_0x007f
            com.syu.app.ipc.IpcObj.clearKey(r3)
            goto L_0x007f
        L_0x0155:
            boolean r0 = com.syu.util.FuncUtils.isFastDoubleClick()
            if (r0 != 0) goto L_0x0010
            com.syu.app.App r0 = com.syu.app.App.getApp()
            com.syu.app.ipc.IpcObj r0 = r0.mIpcObj
            r0.FuncDial()
            goto L_0x007f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.page.Page_History.ResponseClick(android.view.View):void");
    }

    public boolean ResponseLongClick(View view) {
        switch (view.getId()) {
            case 103:
                IpcObj.LongClick0();
                return false;
            case 120:
                if (!bv.a()) {
                    return false;
                }
                IpcObj.clearKey(true);
                return false;
            default:
                return false;
        }
    }

    public boolean dialByKey() {
        if (this.bTop) {
            if (this.actBt.isFocus(this.mGridView)) {
                if (!TextUtils.isEmpty(this.mapSelect.get(203))) {
                    IpcObj.setNum(this.mapSelect.get(203));
                }
                return true;
            } else if (this.actBt.isFocus(this.mGridViewSearch)) {
                if (!TextUtils.isEmpty(this.checkPhoneNumber2)) {
                    IpcObj.setNum(this.checkPhoneNumber2);
                }
                return true;
            }
        }
        return false;
    }

    public void init() {
        this.tvTxtNumber = (JText) getPage().getChildViewByid(63);
        if (this.tvTxtNumber != null) {
            this.tvTxtNumber.addTextChangedListener(new MyTextWatcher(this.tvTxtNumber));
        }
        this.mGridViewSearch = (JGridView) getPage().getChildViewByid(157);
        this.mGridView = (JGridView) getPage().getChildViewByid(196);
        setButtonColor(App.color);
    }

    public boolean isNumber(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public void pause() {
        this.bTop = false;
        this.mapSelect.clear();
    }

    public void pressGridItem1(JPage jPage, JPage jPage2, boolean z) {
        boolean z2 = z && jPage == jPage2;
        View childViewByid = jPage.getChildViewByid(198);
        if (childViewByid != null && (childViewByid instanceof JView)) {
            ((JView) childViewByid).setFocus(z2);
        }
        View childViewByid2 = jPage.getChildViewByid(206);
        if (childViewByid2 != null && (childViewByid2 instanceof JButton)) {
            ((JButton) childViewByid2).setFocus(z2);
        }
        JText jText = (JText) jPage.getChildViewByid(201);
        if (jText != null) {
            jText.setFocus(z2);
        }
        JText jText2 = (JText) jPage.getChildViewByid(202);
        if (jText2 != null) {
            jText2.setFocus(z2);
        }
        JText jText3 = (JText) jPage.getChildViewByid(203);
        if (jText3 != null) {
            jText3.setFocus(z2);
        }
        JText jText4 = (JText) jPage.getChildViewByid(204);
        if (jText4 != null) {
            jText4.setFocus(z2);
        }
        JText jText5 = (JText) jPage.getChildViewByid(205);
        if (jText5 != null) {
            jText5.setFocus(z2);
        }
    }

    public void pressGridItem2(JPage jPage, JPage jPage2, boolean z) {
        boolean z2 = z && jPage == jPage2;
        JText jText = (JText) jPage.getChildViewByid(160);
        if (jText != null) {
            jText.setFocus(z2);
        }
        JText jText2 = (JText) jPage.getChildViewByid(159);
        if (jText2 != null) {
            jText2.setFocus(z2);
        }
    }

    public void queryContacts(String str) {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(str)) {
            showSearchList(false);
        } else if (isNumber(str)) {
            for (SparseArray next : App.mBtInfo.mListContact) {
                if (((String) next.get(180)).contains(str)) {
                    arrayList.add(next);
                }
            }
            showSearchList(true);
            resetList(this.mGridViewSearch, arrayList);
        }
    }

    public void resetList() {
        boolean z = true;
        if (bv.h() && App.sCallsType > 0) {
            z = false;
        }
        if (!App.bHistoryLogAllFlag || !z) {
            switch (App.sCallsType) {
                case 1:
                    resetList(this.mGridView, App.mBtInfo.mListHistoryIn);
                    return;
                case 2:
                    resetList(this.mGridView, App.mBtInfo.mListHistoryOut);
                    return;
                case 3:
                    resetList(this.mGridView, App.mBtInfo.mListHistoryMiss);
                    return;
                default:
                    return;
            }
        } else {
            resetList(this.mGridView, App.mBtInfo.mListHistoryAll);
        }
    }

    public void resetList(JGridView jGridView, List<SparseArray<String>> list) {
        this.mPosMoveTo1 = -1;
        this.mPosFocusCpy1 = -1;
        if (!IpcObj.isConnect()) {
            App.resetList(jGridView, (List<SparseArray<String>>) null);
        } else {
            App.resetList(jGridView, list);
        }
    }

    public void resume() {
        JPage jPage;
        if (this.actBt.bTop) {
            this.bTop = true;
            this.mapSelect.clear();
            App.getApp().mIpcObj.recoverAppId();
            updateHistorySelected(App.sCallsType, true);
            App.queryCallLog();
            resetList();
            if (App.getApp().bPopBtRingSet) {
                App.getApp().popBtRingSet(false);
            }
            if (App.bDelMenuFromBtAv && (jPage = this.actBt.ui.mPages.get(2)) != null && jPage.getVisibility() != 0) {
                jPage.setVisibility(0);
            }
        }
    }

    public boolean scrollOk() {
        if (this.bTop) {
            this.bRoolKey = true;
            if (this.actBt.isFocus(this.mGridView)) {
                if (!TextUtils.isEmpty(this.mapSelect.get(203))) {
                    IpcObj.itemDial(this.mapSelect.get(203));
                }
                return true;
            } else if (this.actBt.isFocus(this.mGridViewSearch)) {
                if (!TextUtils.isEmpty(this.checkPhoneNumber2)) {
                    IpcObj.itemDial(this.checkPhoneNumber2);
                }
                return true;
            }
        }
        return false;
    }

    public boolean scrollToNext() {
        if (!this.bTop) {
            return false;
        }
        this.bRoolKey = true;
        if (this.actBt.isFocus(this.mGridView)) {
            if (this.mPosFocusCpy1 != this.mGridView.mIndexFocus) {
                this.mPosFocusCpy1 = this.mGridView.mIndexFocus;
                this.mPosMoveTo1 = this.mPosFocusCpy1;
            }
            if (this.mPosMoveTo1 < 0) {
                this.mPosMoveTo1 = 0;
            } else if (this.mPosMoveTo1 + 1 >= this.mGridView.getList().size()) {
                return false;
            } else {
                this.mPosMoveTo1++;
            }
            this.mGridView.scrollToPosition(this.mPosMoveTo1);
            return true;
        } else if (!this.actBt.isFocus(this.mGridViewSearch)) {
            return false;
        } else {
            if (this.mPosFocusCpy2 != this.mGridViewSearch.mIndexFocus) {
                this.mPosFocusCpy2 = this.mGridViewSearch.mIndexFocus;
                this.mPosMoveTo2 = this.mPosFocusCpy2;
            }
            if (this.mPosMoveTo2 < 0) {
                this.mPosMoveTo2 = 0;
            } else if (this.mPosMoveTo2 + 1 >= this.mGridViewSearch.getList().size()) {
                return false;
            } else {
                this.mPosMoveTo2++;
            }
            this.mGridViewSearch.scrollToPosition(this.mPosMoveTo2);
            return true;
        }
    }

    public boolean scrollToPrev() {
        if (!this.bTop) {
            return false;
        }
        this.bRoolKey = true;
        if (this.actBt.isFocus(this.mGridView)) {
            if (this.mPosFocusCpy1 != this.mGridView.mIndexFocus) {
                this.mPosFocusCpy1 = this.mGridView.mIndexFocus;
                this.mPosMoveTo1 = this.mPosFocusCpy1;
            }
            if (this.mPosMoveTo1 < 0) {
                this.mPosMoveTo1 = 0;
            } else if (this.mPosMoveTo1 <= 0) {
                return false;
            } else {
                this.mPosMoveTo1--;
            }
            this.mGridView.scrollToPosition(this.mPosMoveTo1);
            return true;
        } else if (!this.actBt.isFocus(this.mGridViewSearch)) {
            return false;
        } else {
            if (this.mPosFocusCpy2 != this.mGridViewSearch.mIndexFocus) {
                this.mPosFocusCpy2 = this.mGridViewSearch.mIndexFocus;
                this.mPosMoveTo2 = this.mPosFocusCpy2;
            }
            if (this.mPosMoveTo2 < 0) {
                this.mPosMoveTo2 = 0;
            } else if (this.mPosMoveTo2 <= 0) {
                return false;
            } else {
                this.mPosMoveTo2--;
            }
            this.mGridViewSearch.scrollToPosition(this.mPosMoveTo2);
            return true;
        }
    }

    public void setButtonColor(int i) {
        if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
            int[] iArr = {i, i};
            int[][] iArr2 = {new int[]{16842919}, new int[]{16842913}};
            JButton jButton = (JButton) getPage().getChildViewByid(189);
            if (jButton != null) {
                jButton.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(190);
            if (jButton2 != null) {
                jButton2.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton2.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton3 = (JButton) getPage().getChildViewByid(191);
            if (jButton3 != null) {
                jButton3.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton3.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton4 = (JButton) getPage().getChildViewByid(192);
            if (jButton4 != null) {
                jButton4.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton4.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void setPressed(JButton jButton, boolean z) {
        updateSelected(jButton.getId(), !z);
    }

    public void showSearchList(boolean z) {
        int i = 0;
        if (this.mGridView != null) {
            this.mGridView.setVisibility(z ? 8 : 0);
        }
        if (this.mGridViewSearch != null) {
            JGridView jGridView = this.mGridViewSearch;
            if (!z) {
                i = 8;
            }
            jGridView.setVisibility(i);
        }
    }

    public void updateHistorySelected(int i, boolean z) {
        if (!App.bHistoryLogAllFlag || bv.h()) {
            if (z && this.iTypeHistorySelect != i) {
                updateHistorySelected(this.iTypeHistorySelect, false);
                this.iTypeHistorySelect = i;
            }
            JButton jButton = null;
            switch (i) {
                case 0:
                    jButton = (JButton) getPage().getChildViewByid(195);
                    break;
                case 1:
                    jButton = (JButton) getPage().getChildViewByid(189);
                    break;
                case 2:
                    jButton = (JButton) getPage().getChildViewByid(190);
                    break;
                case 3:
                    jButton = (JButton) getPage().getChildViewByid(191);
                    break;
            }
            if (jButton != null) {
                jButton.setFocus(z);
                if (App.mStrCustomer.equalsIgnoreCase("changecolor")) {
                    jButton.setSelected(z);
                }
            }
        }
    }

    public void updateSelected(int i, boolean z) {
        int i2;
        switch (i) {
            case 189:
                i2 = 1;
                break;
            case 190:
                i2 = 2;
                break;
            case 191:
                i2 = 3;
                break;
            default:
                i2 = -1;
                break;
        }
        if (i2 != -1 && this.iTypeHistorySelect != i2 && this.iTypeHistorySelect > 0) {
            updateHistorySelected(this.iTypeHistorySelect, z);
        }
    }
}
