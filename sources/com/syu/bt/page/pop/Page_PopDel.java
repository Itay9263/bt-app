package com.syu.bt.page.pop;

import com.syu.bt.act.ActBt;
import com.syu.ctrl.JPage;
import com.syu.page.Page;

public class Page_PopDel extends Page {
    public static final int DEL_CONTACT_ALL = 1;
    public static final int DEL_CONTACT_ONE = 2;
    public static final int DEL_HISTORY_ALL = 3;
    public static final int DEL_HISTORY_ONE = 4;
    public static final int DEL_LOVE_NUMBER = 10;
    public static final int DEL_PAIRED_ONE = 9;
    public static final int DEL_PAIR_ONE = 8;
    public static final int SAVE_CONTACT = 5;
    public static final int SWITCH_TO_HFP = 6;
    public static final int UPDATE_BT = 7;
    ActBt actBt;

    public Page_PopDel(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0065, code lost:
        r3 = r0.getPosition();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void ResponseClick(android.view.View r7) {
        /*
            r6 = this;
            r5 = 180(0xb4, float:2.52E-43)
            r4 = 178(0xb2, float:2.5E-43)
            int r0 = r7.getId()
            switch(r0) {
                case 304: goto L_0x000c;
                case 305: goto L_0x0254;
                default: goto L_0x000b;
            }
        L_0x000b:
            return
        L_0x000c:
            int r0 = com.syu.app.App.mDelType
            switch(r0) {
                case 1: goto L_0x001d;
                case 2: goto L_0x0050;
                case 3: goto L_0x0146;
                case 4: goto L_0x00ac;
                case 5: goto L_0x016b;
                case 6: goto L_0x0185;
                case 7: goto L_0x0038;
                case 8: goto L_0x018a;
                case 9: goto L_0x01fe;
                case 10: goto L_0x01d4;
                default: goto L_0x0011;
            }
        L_0x0011:
            com.syu.ctrl.JPage r0 = r6.getPage()
            android.app.Dialog r0 = r0.getDialog()
            r0.dismiss()
            goto L_0x000b
        L_0x001d:
            com.syu.bt.act.ActBt r0 = r6.actBt
            r0.ClearAllContact()
            boolean r0 = com.syu.app.App.bAutoSavePhoneBook
            if (r0 == 0) goto L_0x0011
            bt r0 = defpackage.bt.a()
            java.lang.String r1 = com.syu.ipcself.module.main.Bt.sPhoneAddr
            java.lang.String r2 = ":"
            java.lang.String r3 = ""
            java.lang.String r1 = r1.replace(r2, r3)
            r0.c(r1)
            goto L_0x0011
        L_0x0038:
            com.syu.bt.act.ActBt r0 = r6.actBt
            com.syu.app.MyUi r0 = r0.ui
            r1 = 11
            com.syu.ctrl.JPage r0 = r0.getPage(r1)
            if (r0 == 0) goto L_0x0011
            bt r0 = defpackage.bt.a()
            java.lang.String r0 = r0.c()
            com.syu.app.ipc.IpcObj.sendPathToUpdateBt(r0)
            goto L_0x0011
        L_0x0050:
            com.syu.bt.act.ActBt r0 = r6.actBt
            com.syu.app.MyUi r0 = r0.ui
            r1 = 5
            com.syu.ctrl.JPage r0 = r0.getPage(r1)
            if (r0 == 0) goto L_0x0011
            r1 = 171(0xab, float:2.4E-43)
            android.view.View r0 = r0.getChildViewByid(r1)
            com.syu.ctrl.JGridView r0 = (com.syu.ctrl.JGridView) r0
            if (r0 == 0) goto L_0x0011
            int r3 = r0.getPosition()
            java.util.List r1 = r0.getList()
            java.lang.Object r1 = r1.get(r3)
            android.util.SparseArray r1 = (android.util.SparseArray) r1
            if (r1 == 0) goto L_0x0011
            boolean r2 = com.syu.app.App.sContactsSaveFlag
            if (r2 != 0) goto L_0x0099
            java.lang.Object r2 = r1.get(r4)
            java.lang.String r2 = (java.lang.String) r2
            java.lang.Object r1 = r1.get(r5)
            java.lang.String r1 = (java.lang.String) r1
            com.syu.bt.Bt_Info r4 = com.syu.app.App.mBtInfo
            android.util.SparseArray r1 = r4.getMapContact(r2, r1)
            if (r1 == 0) goto L_0x0094
            com.syu.bt.Bt_Info r2 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r2 = r2.mListContact
            r2.remove(r1)
        L_0x0094:
            com.syu.app.App.remove(r0, r3)
            goto L_0x0011
        L_0x0099:
            com.syu.bt.Bt_Info r2 = com.syu.app.App.mBtInfo
            java.lang.Object r0 = r1.get(r4)
            java.lang.String r0 = (java.lang.String) r0
            java.lang.Object r1 = r1.get(r5)
            java.lang.String r1 = (java.lang.String) r1
            r2.delContact(r0, r1)
            goto L_0x0011
        L_0x00ac:
            com.syu.bt.act.ActBt r0 = r6.actBt
            com.syu.app.MyUi r0 = r0.ui
            r1 = 8
            com.syu.ctrl.JPage r0 = r0.getPage(r1)
            if (r0 == 0) goto L_0x0011
            r1 = 196(0xc4, float:2.75E-43)
            android.view.View r0 = r0.getChildViewByid(r1)
            com.syu.ctrl.JGridView r0 = (com.syu.ctrl.JGridView) r0
            if (r0 == 0) goto L_0x0011
            int r2 = r0.getPosition()
            com.syu.ctrl.JGridView$GridAdapter r1 = r0.adapter
            if (r1 == 0) goto L_0x0011
            java.util.List r1 = r0.getList()
            java.lang.Object r1 = r1.get(r2)
            android.util.SparseArray r1 = (android.util.SparseArray) r1
            if (r1 == 0) goto L_0x0011
            com.syu.app.App.remove(r0, r2)
            boolean r0 = com.syu.app.App.bHistoryLogAllFlag
            if (r0 == 0) goto L_0x00ec
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryAll
            if (r0 == 0) goto L_0x00ec
            int r3 = r0.size()
            if (r3 <= r2) goto L_0x00ec
            r0.remove(r2)
        L_0x00ec:
            boolean r0 = com.syu.app.App.bHistoryLogAllFlag
            if (r0 == 0) goto L_0x00f6
            boolean r0 = defpackage.bv.h()
            if (r0 == 0) goto L_0x00fb
        L_0x00f6:
            int r0 = com.syu.app.App.sCallsType
            switch(r0) {
                case 1: goto L_0x0116;
                case 2: goto L_0x0126;
                case 3: goto L_0x0136;
                default: goto L_0x00fb;
            }
        L_0x00fb:
            com.syu.bt.Bt_Info r2 = com.syu.app.App.mBtInfo
            r0 = 203(0xcb, float:2.84E-43)
            java.lang.Object r0 = r1.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            r3 = 200(0xc8, float:2.8E-43)
            java.lang.Object r1 = r1.get(r3)
            java.lang.String r1 = (java.lang.String) r1
            long r4 = com.syu.app.App.myParseLong(r1)
            r2.delCallLog(r0, r4)
            goto L_0x0011
        L_0x0116:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryIn
            if (r0 == 0) goto L_0x00fb
            int r3 = r0.size()
            if (r3 <= r2) goto L_0x00fb
            r0.remove(r2)
            goto L_0x00fb
        L_0x0126:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryOut
            if (r0 == 0) goto L_0x00fb
            int r3 = r0.size()
            if (r3 <= r2) goto L_0x00fb
            r0.remove(r2)
            goto L_0x00fb
        L_0x0136:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListHistoryMiss
            if (r0 == 0) goto L_0x00fb
            int r3 = r0.size()
            if (r3 <= r2) goto L_0x00fb
            r0.remove(r2)
            goto L_0x00fb
        L_0x0146:
            boolean r0 = com.syu.app.App.bHistoryLogAllFlag
            if (r0 == 0) goto L_0x0151
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            r0.clearAllCallLog()
            goto L_0x0011
        L_0x0151:
            com.syu.bt.act.ActBt r0 = r6.actBt
            cc r0 = r0.uiUtil
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r2 = "bt_deling"
            java.lang.String r1 = r1.getString(r2)
            r0.a(r1)
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            int r1 = com.syu.app.App.sCallsType
            r0.clearCallLog(r1)
            goto L_0x0011
        L_0x016b:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListContact
            if (r0 == 0) goto L_0x0011
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListContact
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0011
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            r0.saveDownloadContacts()
            r0 = 1
            com.syu.app.App.sContactsSaveFlag = r0
            goto L_0x0011
        L_0x0185:
            com.syu.app.ipc.IpcObj.VoiceSwitch()
            goto L_0x0011
        L_0x018a:
            com.syu.bt.act.ActBt r0 = r6.actBt
            com.syu.bt.page.Page_Pair r2 = r0.getPagePair()
            if (r2 == 0) goto L_0x0011
            java.lang.String r0 = com.syu.app.App.mDeletePairAddr
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 != 0) goto L_0x0011
            r0 = 0
            r1 = r0
        L_0x019c:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListPair
            int r0 = r0.size()
            if (r1 < r0) goto L_0x01ae
        L_0x01a6:
            r2.refreshList()
            r0 = 0
            com.syu.app.App.mDeletePairAddr = r0
            goto L_0x0011
        L_0x01ae:
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListPair
            java.lang.Object r0 = r0.get(r1)
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            r3 = 283(0x11b, float:3.97E-43)
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r3 = com.syu.app.App.mDeletePairAddr
            boolean r0 = r0.equalsIgnoreCase(r3)
            if (r0 == 0) goto L_0x01d0
            com.syu.bt.Bt_Info r0 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r0 = r0.mListPair
            r0.remove(r1)
            goto L_0x01a6
        L_0x01d0:
            int r0 = r1 + 1
            r1 = r0
            goto L_0x019c
        L_0x01d4:
            com.syu.bt.act.ActBt r0 = r6.actBt
            com.syu.bt.page.Page_Dial r0 = r0.getPageDial()
            if (r0 == 0) goto L_0x0011
            int r1 = com.syu.app.App.mSelectContact
            if (r1 <= 0) goto L_0x0011
            android.util.SparseArray r1 = new android.util.SparseArray
            r1.<init>()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "contact_save"
            r2.<init>(r3)
            int r3 = com.syu.app.App.mSelectContact
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            defpackage.bz.a((java.lang.String) r2, (android.util.SparseArray<java.lang.String>) r1)
            r0.showLoveNumber()
            goto L_0x0011
        L_0x01fe:
            com.syu.bt.act.ActBt r0 = r6.actBt
            com.syu.app.MyUi r0 = r0.ui
            r1 = 12
            com.syu.ctrl.JPage r0 = r0.getPage(r1)
            if (r0 == 0) goto L_0x0011
            r1 = 287(0x11f, float:4.02E-43)
            android.view.View r0 = r0.getChildViewByid(r1)
            com.syu.ctrl.JGridView r0 = (com.syu.ctrl.JGridView) r0
            if (r0 == 0) goto L_0x0011
            int r1 = r0.getPosition()
            java.util.List r0 = r0.getList()
            java.lang.Object r0 = r0.get(r1)
            android.util.SparseArray r0 = (android.util.SparseArray) r0
            if (r0 == 0) goto L_0x0011
            r1 = 290(0x122, float:4.06E-43)
            java.lang.Object r1 = r0.get(r1)
            java.lang.String r1 = (java.lang.String) r1
            com.syu.app.ipc.IpcObj.deleteConnectedDevice(r1)
            com.syu.bt.Bt_Info r2 = com.syu.app.App.mBtInfo
            java.util.List<android.util.SparseArray<java.lang.String>> r2 = r2.mListHasPaired
            r2.remove(r0)
            boolean r0 = com.syu.app.ipc.IpcObj.isConnect()
            if (r0 == 0) goto L_0x0247
            java.lang.String r0 = com.syu.ipcself.module.main.Bt.sPhoneAddr
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0247
            com.syu.app.ipc.IpcObj.cut()
        L_0x0247:
            com.syu.bt.act.ActBt r0 = r6.actBt
            com.syu.bt.page.Page_Pair r0 = r0.getPagePair()
            if (r0 == 0) goto L_0x0011
            r0.refreshPairedList()
            goto L_0x0011
        L_0x0254:
            r0 = -1
            com.syu.app.App.mSelectContact = r0
            com.syu.ctrl.JPage r0 = r6.getPage()
            android.app.Dialog r0 = r0.getDialog()
            r0.dismiss()
            goto L_0x000b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.page.pop.Page_PopDel.ResponseClick(android.view.View):void");
    }
}
