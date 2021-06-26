package com.syu.bt.page;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.data.FinalChip;
import com.syu.ipcself.module.main.Bt;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

public class Page_Pair extends Page {
    public ActBt actBt;
    private boolean bRegisterReceiver;
    boolean bTop = false;
    JButton btnPairCut = null;
    JButton btnPairDel = null;
    JButton btnPairLink = null;
    JButton btnPairSearch = null;
    public boolean isBtavStop = false;
    public boolean isConnectBt = false;
    public boolean isSelfLink = false;
    public boolean isStartDiscovey = false;
    JGridView mGridView;
    HideText mHideText = new HideText();
    JGridView mPairedView;
    private int mPosFocusCpy = -1;
    private int mPosMoveTo = -1;
    RunnableHideText mRunnableHideText = new RunnableHideText();
    RunnableHideText2 mRunnableHideText2 = new RunnableHideText2();
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (!"android.bluetooth.device.action.FOUND".equals(action)) {
                if ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(action)) {
                    switch (((BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE")).getBondState()) {
                        case 10:
                            cb.a().a(App.getApp().getString("bt_unpaired_dev"));
                            break;
                        case 11:
                            cb.a().a(App.getApp().getString("bt_paring"));
                            break;
                        case 12:
                            cb.a().a(App.getApp().getString("bt_paired_dev"));
                            break;
                    }
                }
            } else {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
            }
            JGridView jGridView = (JGridView) Page_Pair.this.getPage().getChildViewByid(280);
            if (jGridView != null) {
                jGridView.notifyDataSetChanged();
            }
        }
    };
    JText tipText;

    public class HideText implements Runnable {
        public HideText() {
        }

        public void run() {
            if (Page_Pair.this.tipText != null) {
                Page_Pair.this.tipText.setVisibility(8);
            }
        }
    }

    public class RunnableHideText implements Runnable {
        public RunnableHideText() {
        }

        public void run() {
            if (Page_Pair.this.tipText != null) {
                Page_Pair.this.tipText.setVisibility(8);
            }
            if (Page_Pair.this.isConnectBt) {
                Page_Pair.this.showConnectTip(true, App.getApp().getString("bt_linking"));
            }
            if (Page_Pair.this.isStartDiscovey) {
                Page_Pair.this.showDiscover(true, App.getApp().getString("bt_discovering"));
            }
        }
    }

    public class RunnableHideText2 implements Runnable {
        public RunnableHideText2() {
        }

        public void run() {
            if (Page_Pair.this.isConnectBt) {
                Page_Pair.this.isConnectBt = false;
                if (Page_Pair.this.tipText != null) {
                    Page_Pair.this.tipText.setText(App.getApp().getString("bt_obd_disconnected"));
                    Page_Pair.this.tipText.postDelayed(new Runnable() {
                        public void run() {
                            Page_Pair.this.tipText.setVisibility(8);
                        }
                    }, 1000);
                }
            }
        }
    }

    public Page_Pair(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    @SuppressLint({"NewApi"})
    private Set<BluetoothDevice> getBindObdDevice() {
        try {
            if (Main.mConf_PlatForm == 5) {
                return ((BluetoothManager) App.getApp().getSystemService("bluetooth")).getAdapter().getBondedDevices();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isAudioVideoType(BluetoothDevice bluetoothDevice) {
        return bluetoothDevice.getBluetoothClass().getMajorDeviceClass() == 512;
    }

    public static boolean isPhoneType(BluetoothDevice bluetoothDevice) {
        return bluetoothDevice.getBluetoothClass().getMajorDeviceClass() == 512;
    }

    private void registerSearchDevicesReceiver() {
        if (Main.mConf_PlatForm != 8) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.bluetooth.device.action.FOUND");
            intentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
            intentFilter.addAction("android.bluetooth.adapter.action.SCAN_MODE_CHANGED");
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            this.actBt.registerReceiver(this.searchDevices, intentFilter);
            this.bRegisterReceiver = true;
        }
    }

    public static boolean removeBond(BluetoothDevice bluetoothDevice) {
        try {
            return ((Boolean) bluetoothDevice.getClass().getMethod("removeBond", new Class[0]).invoke(bluetoothDevice, new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void unregisterSearchDevicesReceiver() {
        if (this.bRegisterReceiver) {
            this.actBt.unregisterReceiver(this.searchDevices);
        }
        this.bRegisterReceiver = false;
    }

    public void GridClick(JGridView jGridView) {
        int id = jGridView.getId();
        int position = jGridView.getPosition();
        switch (id) {
            case 280:
                if (App.mBtInfo.mListPair.size() > position) {
                    String str = (String) App.mBtInfo.mListPair.get(position).get(283);
                    if (!TextUtils.isEmpty(str)) {
                        this.mPosMoveTo = -1;
                        if (bv.h() || App.mIdCustomer == 66) {
                            if (!Bt.sPhoneAddr.equals(str) || !IpcObj.isConnect()) {
                                IpcObj.setChoiceAddr(str);
                                App.getApp().mIpcObj.FuncPairLink(this);
                                if (!(this.mGridView == null || this.mGridView.adapter == null)) {
                                    this.mGridView.adapter.notifyDataSetChanged();
                                }
                                if (!(this.mPairedView == null || this.mPairedView.adapter == null)) {
                                    this.mPairedView.adapter.notifyDataSetChanged();
                                }
                            } else {
                                IpcObj.cut();
                            }
                        }
                        if (!IpcObj.getChoiceAddr().equals(str)) {
                            IpcObj.setChoiceAddr(str);
                            if (!(this.mGridView == null || this.mGridView.adapter == null)) {
                                this.mGridView.adapter.notifyDataSetChanged();
                            }
                            if (!(this.mPairedView == null || this.mPairedView.adapter == null)) {
                                this.mPairedView.adapter.notifyDataSetChanged();
                            }
                        }
                        resetBtnEnable();
                        return;
                    }
                    return;
                }
                return;
            case 287:
                if (App.mBtInfo.mListHasPaired.size() > position) {
                    String str2 = (String) App.mBtInfo.mListHasPaired.get(position).get(290);
                    if (!TextUtils.isEmpty(str2)) {
                        this.mPosMoveTo = -1;
                        if (bv.h()) {
                            if (!Bt.sPhoneAddr.equals(str2) || !IpcObj.isConnect()) {
                                IpcObj.setChoiceAddr(str2);
                                App.getApp().mIpcObj.FuncPairLink(this);
                                if (!(this.mGridView == null || this.mGridView.adapter == null)) {
                                    this.mGridView.adapter.notifyDataSetChanged();
                                }
                                if (!(this.mPairedView == null || this.mPairedView.adapter == null)) {
                                    this.mPairedView.adapter.notifyDataSetChanged();
                                }
                            } else {
                                IpcObj.cut();
                            }
                        }
                        if (!IpcObj.getChoiceAddr().equals(str2)) {
                            IpcObj.setChoiceAddr(str2);
                            if (!(this.mGridView == null || this.mGridView.adapter == null)) {
                                this.mGridView.adapter.notifyDataSetChanged();
                            }
                            if (!(this.mPairedView == null || this.mPairedView.adapter == null)) {
                                this.mPairedView.adapter.notifyDataSetChanged();
                            }
                        }
                        resetBtnEnable();
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void GridLongClick(JGridView jGridView) {
        switch (jGridView.getId()) {
            case 287:
                SparseArray sparseArray = jGridView.getList().get(jGridView.getPosition());
                if (sparseArray != null && sparseArray.size() > 0) {
                    this.actBt.popDeleteContacts(9, App.getApp().getString("bt_is_delete_onepaired"));
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005f, code lost:
        if (com.syu.app.ipc.IpcObj.getChoiceAddr().equalsIgnoreCase(r0) != false) goto L_0x0061;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void InitGridItem(com.syu.ctrl.JGridView r8, com.syu.ctrl.JPage r9, android.util.SparseArray<java.lang.String> r10, int r11) {
        /*
            r7 = this;
            r6 = 290(0x122, float:4.06E-43)
            r5 = 283(0x11b, float:3.97E-43)
            r2 = 8
            r3 = 1
            r4 = 0
            int r0 = r8.getId()
            switch(r0) {
                case 280: goto L_0x0010;
                case 287: goto L_0x0157;
                default: goto L_0x000f;
            }
        L_0x000f:
            return
        L_0x0010:
            r0 = 281(0x119, float:3.94E-43)
            android.view.View r0 = r9.getChildViewByid(r0)
            if (r0 == 0) goto L_0x0028
            int r1 = r10.size()
            if (r1 != 0) goto L_0x0025
            r9.setFocus(r4)
            r0.setVisibility(r2)
            goto L_0x000f
        L_0x0025:
            r0.setVisibility(r4)
        L_0x0028:
            java.lang.Object r0 = r10.get(r5)
            java.lang.String r0 = (java.lang.String) r0
            android.view.View r1 = r9.getChildViewByid(r5)
            com.syu.ctrl.JText r1 = (com.syu.ctrl.JText) r1
            r2 = 284(0x11c, float:3.98E-43)
            android.view.View r2 = r9.getChildViewByid(r2)
            com.syu.ctrl.JText r2 = (com.syu.ctrl.JText) r2
            boolean r5 = android.text.TextUtils.isEmpty(r0)
            if (r5 != 0) goto L_0x0236
            int r5 = r7.mPosMoveTo
            if (r5 < 0) goto L_0x004d
            int r5 = r7.mPosMoveTo
            if (r5 != r11) goto L_0x004d
            com.syu.app.ipc.IpcObj.setChoiceAddr(r0)
        L_0x004d:
            java.lang.String r5 = com.syu.app.ipc.IpcObj.getChoiceAddr()
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x0236
            java.lang.String r5 = com.syu.app.ipc.IpcObj.getChoiceAddr()
            boolean r5 = r5.equalsIgnoreCase(r0)
            if (r5 == 0) goto L_0x0236
        L_0x0061:
            r9.setFocus(r3)
            if (r2 == 0) goto L_0x0083
            if (r3 == 0) goto L_0x010a
            android.text.TextUtils$TruncateAt r4 = android.text.TextUtils.TruncateAt.MARQUEE
        L_0x006a:
            r2.setEllipsize(r4)
            r2.setFocus(r3)
            java.lang.String r4 = com.syu.app.App.mStrCustomer
            java.lang.String r5 = "TZY_NEW"
            boolean r4 = r4.equalsIgnoreCase(r5)
            if (r4 == 0) goto L_0x0083
            com.syu.bt.act.ActBt r4 = r7.actBt
            android.graphics.Typeface r4 = defpackage.bt.a((android.content.Context) r4)
            r2.setTypeface(r4)
        L_0x0083:
            if (r1 == 0) goto L_0x00a2
            if (r3 == 0) goto L_0x010e
            android.text.TextUtils$TruncateAt r4 = android.text.TextUtils.TruncateAt.MARQUEE
        L_0x0089:
            r1.setEllipsize(r4)
            r1.setFocus(r3)
            java.lang.String r1 = com.syu.app.App.mStrCustomer
            java.lang.String r3 = "TZY_NEW"
            boolean r1 = r1.equalsIgnoreCase(r3)
            if (r1 == 0) goto L_0x00a2
            com.syu.bt.act.ActBt r1 = r7.actBt
            android.graphics.Typeface r1 = defpackage.bt.a((android.content.Context) r1)
            r2.setTypeface(r1)
        L_0x00a2:
            r1 = 285(0x11d, float:4.0E-43)
            android.view.View r2 = r9.getChildViewByid(r1)
            if (r2 == 0) goto L_0x00e1
            java.lang.String r1 = com.syu.app.ipc.IpcObj.getPhoneAddr()
            boolean r1 = r1.equals(r0)
            if (r1 == 0) goto L_0x00ba
            boolean r1 = com.syu.app.ipc.IpcObj.isDisConnect()
            if (r1 == 0) goto L_0x0112
        L_0x00ba:
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icocut"
            int r1 = r1.getIdDrawable(r3)
            r2.setBackgroundResource(r1)
        L_0x00c7:
            boolean r1 = com.syu.app.App.bPopObdFlag
            if (r1 == 0) goto L_0x00e1
            java.util.Set r1 = r7.getBindObdDevice()
            if (r1 == 0) goto L_0x00e1
            int r3 = r1.size()
            if (r3 == 0) goto L_0x00e1
            java.util.Iterator r3 = r1.iterator()
        L_0x00db:
            boolean r1 = r3.hasNext()
            if (r1 != 0) goto L_0x0120
        L_0x00e1:
            r1 = 286(0x11e, float:4.01E-43)
            android.view.View r1 = r9.getChildViewByid(r1)
            android.widget.TextView r1 = (android.widget.TextView) r1
            if (r1 == 0) goto L_0x000f
            java.lang.String r2 = com.syu.app.ipc.IpcObj.getPhoneAddr()
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x00fb
            boolean r0 = com.syu.app.ipc.IpcObj.isDisConnect()
            if (r0 == 0) goto L_0x0148
        L_0x00fb:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            java.lang.String r2 = "bt_disconnected"
            java.lang.String r0 = r0.getString(r2)
            r1.setText(r0)
            goto L_0x000f
        L_0x010a:
            android.text.TextUtils$TruncateAt r4 = android.text.TextUtils.TruncateAt.END
            goto L_0x006a
        L_0x010e:
            android.text.TextUtils$TruncateAt r4 = android.text.TextUtils.TruncateAt.END
            goto L_0x0089
        L_0x0112:
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icolink"
            int r1 = r1.getIdDrawable(r3)
            r2.setBackgroundResource(r1)
            goto L_0x00c7
        L_0x0120:
            java.lang.Object r1 = r3.next()
            android.bluetooth.BluetoothDevice r1 = (android.bluetooth.BluetoothDevice) r1
            java.lang.String r1 = r1.getAddress()
            boolean r1 = r1.equalsIgnoreCase(r0)
            if (r1 == 0) goto L_0x00db
            java.lang.String r1 = com.syu.app.ipc.IpcObj.getPhoneAddr()
            boolean r1 = r1.equals(r0)
            if (r1 != 0) goto L_0x00db
            com.syu.app.App r1 = com.syu.app.App.getApp()
            java.lang.String r4 = "bt_icolinkobd"
            int r1 = r1.getIdDrawable(r4)
            r2.setBackgroundResource(r1)
            goto L_0x00db
        L_0x0148:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            java.lang.String r2 = "bt_connected"
            java.lang.String r0 = r0.getString(r2)
            r1.setText(r0)
            goto L_0x000f
        L_0x0157:
            r0 = 288(0x120, float:4.04E-43)
            android.view.View r0 = r9.getChildViewByid(r0)
            if (r0 == 0) goto L_0x0170
            int r1 = r10.size()
            if (r1 != 0) goto L_0x016d
            r9.setFocus(r4)
            r0.setVisibility(r2)
            goto L_0x000f
        L_0x016d:
            r0.setVisibility(r4)
        L_0x0170:
            java.lang.Object r0 = r10.get(r6)
            java.lang.String r0 = (java.lang.String) r0
            android.view.View r1 = r9.getChildViewByid(r6)
            com.syu.ctrl.JText r1 = (com.syu.ctrl.JText) r1
            r2 = 291(0x123, float:4.08E-43)
            android.view.View r2 = r9.getChildViewByid(r2)
            com.syu.ctrl.JText r2 = (com.syu.ctrl.JText) r2
            boolean r5 = android.text.TextUtils.isEmpty(r0)
            if (r5 != 0) goto L_0x01aa
            int r5 = r7.mPosMoveTo
            if (r5 < 0) goto L_0x0195
            int r5 = r7.mPosMoveTo
            if (r5 != r11) goto L_0x0195
            com.syu.app.ipc.IpcObj.setChoiceAddr(r0)
        L_0x0195:
            java.lang.String r5 = com.syu.app.ipc.IpcObj.getChoiceAddr()
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x01aa
            java.lang.String r5 = com.syu.app.ipc.IpcObj.getChoiceAddr()
            boolean r5 = r5.equalsIgnoreCase(r0)
            if (r5 == 0) goto L_0x01aa
            r4 = r3
        L_0x01aa:
            r9.setFocus(r4)
            if (r2 == 0) goto L_0x01b9
            if (r4 == 0) goto L_0x0213
            android.text.TextUtils$TruncateAt r3 = android.text.TextUtils.TruncateAt.MARQUEE
        L_0x01b3:
            r2.setEllipsize(r3)
            r2.setFocus(r4)
        L_0x01b9:
            if (r1 == 0) goto L_0x01c5
            if (r4 == 0) goto L_0x0216
            android.text.TextUtils$TruncateAt r2 = android.text.TextUtils.TruncateAt.MARQUEE
        L_0x01bf:
            r1.setEllipsize(r2)
            r1.setFocus(r4)
        L_0x01c5:
            r1 = 292(0x124, float:4.09E-43)
            android.view.View r1 = r9.getChildViewByid(r1)
            if (r1 == 0) goto L_0x01ea
            java.lang.String r2 = com.syu.app.ipc.IpcObj.getPhoneAddr()
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x01dd
            boolean r2 = com.syu.app.ipc.IpcObj.isDisConnect()
            if (r2 == 0) goto L_0x0219
        L_0x01dd:
            com.syu.app.App r2 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icocut"
            int r2 = r2.getIdDrawable(r3)
            r1.setBackgroundResource(r2)
        L_0x01ea:
            r1 = 293(0x125, float:4.1E-43)
            android.view.View r1 = r9.getChildViewByid(r1)
            android.widget.TextView r1 = (android.widget.TextView) r1
            if (r1 == 0) goto L_0x000f
            java.lang.String r2 = com.syu.app.ipc.IpcObj.getPhoneAddr()
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L_0x0204
            boolean r0 = com.syu.app.ipc.IpcObj.isDisConnect()
            if (r0 == 0) goto L_0x0227
        L_0x0204:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            java.lang.String r2 = "bt_disconnected"
            java.lang.String r0 = r0.getString(r2)
            r1.setText(r0)
            goto L_0x000f
        L_0x0213:
            android.text.TextUtils$TruncateAt r3 = android.text.TextUtils.TruncateAt.END
            goto L_0x01b3
        L_0x0216:
            android.text.TextUtils$TruncateAt r2 = android.text.TextUtils.TruncateAt.END
            goto L_0x01bf
        L_0x0219:
            com.syu.app.App r2 = com.syu.app.App.getApp()
            java.lang.String r3 = "bt_icolink"
            int r2 = r2.getIdDrawable(r3)
            r1.setBackgroundResource(r2)
            goto L_0x01ea
        L_0x0227:
            com.syu.app.App r0 = com.syu.app.App.getApp()
            java.lang.String r2 = "bt_connected"
            java.lang.String r0 = r0.getString(r2)
            r1.setText(r0)
            goto L_0x000f
        L_0x0236:
            r3 = r4
            goto L_0x0061
        */
        throw new UnsupportedOperationException("Method not decompiled: com.syu.bt.page.Page_Pair.InitGridItem(com.syu.ctrl.JGridView, com.syu.ctrl.JPage, android.util.SparseArray, int):void");
    }

    public void ResponseClick(View view) {
        boolean z;
        boolean z2 = true;
        int i = 0;
        if (!bt.a().d()) {
            switch (view.getId()) {
                case 97:
                    if (App.getApp().isHalfScreenAble()) {
                        App.getApp().setFullScreenMode(0);
                        break;
                    }
                    break;
                case 276:
                    if (Main.mConf_PlatForm != 2) {
                        if (Main.mConf_PlatForm == 7) {
                            if (!this.isStartDiscovey) {
                                IpcObj.startDiscover();
                                showDiscover(true, App.getApp().getString("bt_discovering"));
                                break;
                            } else {
                                IpcObj.stopDiscover();
                                showDiscover(false, App.getApp().getString("bt_discover_end"));
                                break;
                            }
                        } else {
                            boolean z3 = !this.isStartDiscovey;
                            if (bv.h() && this.tipText != null && this.isStartDiscovey && this.tipText.getVisibility() == 8) {
                                showConnectTip(false, App.getApp().getString("bt_linking"));
                                showDiscover(true, App.getApp().getString("bt_discovering"));
                            }
                            if (!z3) {
                                if (this.isStartDiscovey && App.mIdCustomer == 43) {
                                    IpcObj.stopDiscover();
                                    break;
                                }
                            } else {
                                App.mBtInfo.mListPair.clear();
                                refreshList();
                                IpcObj.startDiscover();
                                showConnectTip(false, App.getApp().getString("bt_linking"));
                                showDiscover(true, App.getApp().getString("bt_discovering"));
                                break;
                            }
                        }
                    } else {
                        IpcObj.cut();
                        break;
                    }
                case 277:
                    App.getApp().mIpcObj.FuncPairLink(this);
                    break;
                case 278:
                    if (!IpcObj.isConnect()) {
                        removeBond();
                        break;
                    } else {
                        IpcObj.cut();
                        break;
                    }
                case 279:
                    if (bv.d()) {
                        if (App.mBtInfo.mListPair == null) {
                            App.mBtInfo.mListPair.clear();
                            refreshList();
                            break;
                        } else {
                            int i2 = 0;
                            while (true) {
                                if (i2 >= App.mBtInfo.mListPair.size()) {
                                    z = false;
                                } else if (IpcObj.getChoiceAddr().equalsIgnoreCase((String) App.mBtInfo.mListPair.get(i2).get(283))) {
                                    App.mBtInfo.mListPair.remove(i2);
                                    IpcObj.setChoiceAddr(FinalChip.BSP_PLATFORM_Null);
                                    z = true;
                                } else {
                                    i2++;
                                }
                            }
                            if (!z) {
                                while (true) {
                                    if (i >= App.mBtInfo.mListHasPaired.size()) {
                                        z2 = z;
                                    } else if (IpcObj.getChoiceAddr().equalsIgnoreCase((String) App.mBtInfo.mListHasPaired.get(i).get(290))) {
                                        IpcObj.deleteConnectedDevice(IpcObj.getChoiceAddr());
                                        App.mBtInfo.mListHasPaired.remove(i);
                                        IpcObj.setChoiceAddr(FinalChip.BSP_PLATFORM_Null);
                                    } else {
                                        i++;
                                    }
                                }
                                if (z2) {
                                    App.getApp().mIpcObj.queryPairList();
                                    refreshPairedList();
                                    break;
                                }
                            } else {
                                refreshList();
                                break;
                            }
                        }
                    } else {
                        App.mBtInfo.mListPair.clear();
                        refreshList();
                        break;
                    }
                    break;
                case 348:
                    this.actBt.Func_Back();
                    break;
            }
            if (this.actBt.MenuClick(view)) {
            }
        }
    }

    public void clickItem(JGridView jGridView, int i) {
        if (i >= 0 && App.mBtInfo.mListPair.size() > i) {
            String str = (String) App.mBtInfo.mListPair.get(i).get(283);
            if (!TextUtils.isEmpty(str)) {
                if (!IpcObj.getChoiceAddr().equals(str)) {
                    IpcObj.setChoiceAddr(str);
                    if (jGridView.adapter != null) {
                        jGridView.adapter.notifyDataSetChanged();
                    }
                }
                resetBtnEnable();
            }
        }
    }

    public SparseArray<String> getChoiceName(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (App.mBtInfo.mListPair != null) {
            for (SparseArray<String> next : App.mBtInfo.mListPair) {
                if (!TextUtils.isEmpty(next.get(283)) && next.get(283).equals(str)) {
                    return next;
                }
            }
        }
        if (App.mBtInfo.mListHasPaired != null) {
            for (SparseArray<String> next2 : App.mBtInfo.mListHasPaired) {
                if (!TextUtils.isEmpty(next2.get(290)) && next2.get(290).equals(str)) {
                    return next2;
                }
            }
        }
        return null;
    }

    public void init() {
        this.mGridView = (JGridView) getPage().getChildViewByid(280);
        this.mPairedView = (JGridView) getPage().getChildViewByid(287);
        this.tipText = (JText) getPage().getChildViewByid(275);
        this.btnPairSearch = (JButton) getPage().getChildViewByid(276);
        this.btnPairLink = (JButton) getPage().getChildViewByid(277);
        this.btnPairCut = (JButton) getPage().getChildViewByid(278);
        this.btnPairDel = (JButton) getPage().getChildViewByid(279);
        refreshList();
        refreshPairedList();
        setButtonColor(App.color);
        if (bv.h()) {
            if (this.mGridView != null) {
                try {
                    Drawable drawableFromPath = this.actBt.ui.getDrawableFromPath("grid_verthumb");
                    if (drawableFromPath != null) {
                        Field declaredField = View.class.getDeclaredField("mScrollCache");
                        declaredField.setAccessible(true);
                        Object obj = declaredField.get(this.mGridView);
                        Field declaredField2 = declaredField.getType().getDeclaredField("scrollBar");
                        declaredField2.setAccessible(true);
                        Object obj2 = declaredField2.get(obj);
                        Field declaredField3 = declaredField2.getType().getDeclaredField("mVerticalThumb");
                        declaredField3.setAccessible(true);
                        declaredField3.set(obj2, drawableFromPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (this.mPairedView != null) {
                try {
                    Drawable drawableFromPath2 = this.actBt.ui.getDrawableFromPath("grid_verthumb");
                    if (drawableFromPath2 != null) {
                        Field declaredField4 = View.class.getDeclaredField("mScrollCache");
                        declaredField4.setAccessible(true);
                        Object obj3 = declaredField4.get(this.mPairedView);
                        Field declaredField5 = declaredField4.getType().getDeclaredField("scrollBar");
                        declaredField5.setAccessible(true);
                        Object obj4 = declaredField5.get(obj3);
                        Field declaredField6 = declaredField5.getType().getDeclaredField("mVerticalThumb");
                        declaredField6.setAccessible(true);
                        declaredField6.set(obj4, drawableFromPath2);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        App.getApp().mIpcObj.onNotify(this, i, iArr, fArr, strArr);
    }

    public void pause() {
        this.bTop = false;
        super.pause();
        unregisterSearchDevicesReceiver();
        bt.h();
    }

    public void popPwd() {
        Dialog popDlg = this.actBt.ui.getPopDlg(17, App.getApp().getIdStyle("pop_add_contacts_anim"), true);
        if (popDlg != null) {
            popDlg.show();
        }
    }

    public void pressGridItem(JPage jPage, JPage jPage2, boolean z) {
        boolean z2 = z && jPage == jPage2;
        JText jText = (JText) jPage.getChildViewByid(283);
        if (jText != null) {
            jText.setFocus(z2);
        }
        JText jText2 = (JText) jPage.getChildViewByid(284);
        if (jText2 != null) {
            jText2.setFocus(z2);
        }
        JText jText3 = (JText) jPage.getChildViewByid(290);
        if (jText3 != null) {
            jText3.setFocus(z2);
        }
        JText jText4 = (JText) jPage.getChildViewByid(291);
        if (jText4 != null) {
            jText4.setFocus(z2);
        }
    }

    public void refreshList() {
        if (this.mGridView != null) {
            resetList(this.mGridView, App.mBtInfo.mListPair);
        }
    }

    public void refreshPairedList() {
        if (this.mPairedView != null) {
            if (App.mBtInfo.mListHasPaired.size() > 0) {
                int i = 0;
                while (true) {
                    if (i < App.mBtInfo.mListHasPaired.size()) {
                        SparseArray sparseArray = App.mBtInfo.mListHasPaired.get(i);
                        if (!TextUtils.isEmpty((CharSequence) sparseArray.get(290)) && ((String) sparseArray.get(290)).equalsIgnoreCase(Bt.sPhoneAddr)) {
                            App.mBtInfo.mListHasPaired.remove(i);
                            App.mBtInfo.mListHasPaired.add(0, sparseArray);
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
            }
            resetList(this.mPairedView, App.mBtInfo.mListHasPaired);
        }
    }

    @SuppressLint({"NewApi"})
    public void removeBond() {
        if (Main.mConf_PlatForm == 5) {
            try {
                BluetoothDevice remoteDevice = ((BluetoothManager) App.getApp().getSystemService("bluetooth")).getAdapter().getRemoteDevice(IpcObj.getChoiceAddr());
                if (!TextUtils.isEmpty(IpcObj.getChoiceAddr())) {
                    removeBond(remoteDevice);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void resetBtnEnable() {
        if (App.hideBtnWhenDisconnect) {
            if (this.btnPairSearch != null) {
                this.btnPairSearch.setEnabled(true);
            }
            if (this.btnPairLink != null) {
                this.btnPairLink.setEnabled(true);
            }
            if (this.btnPairCut != null) {
                this.btnPairCut.setEnabled(true);
            }
            if (this.btnPairDel != null) {
                this.btnPairDel.setEnabled(true);
            }
            if (!TextUtils.isEmpty(IpcObj.getChoiceAddr())) {
                if (Bt.sPhoneAddr.equals(IpcObj.getChoiceAddr())) {
                    if (!IpcObj.isDisConnect()) {
                        if (this.btnPairSearch != null) {
                            this.btnPairSearch.setEnabled(false);
                        }
                        if (this.btnPairLink != null) {
                            this.btnPairLink.setEnabled(false);
                        }
                    } else if (this.btnPairCut != null) {
                        this.btnPairCut.setEnabled(false);
                    }
                } else if (!IpcObj.isDisConnect()) {
                    if (this.btnPairSearch != null) {
                        this.btnPairSearch.setEnabled(false);
                    }
                    if (this.btnPairCut != null) {
                        this.btnPairCut.setEnabled(false);
                    }
                } else if (this.btnPairCut != null) {
                    this.btnPairCut.setEnabled(false);
                }
            } else if (TextUtils.isEmpty(IpcObj.getChoiceAddr())) {
                if (!IpcObj.isDisConnect() && ((App.mBtInfo.mListPair.size() > 0 || App.mBtInfo.mListHasPaired.size() > 0) && this.btnPairSearch != null)) {
                    this.btnPairSearch.setEnabled(false);
                }
                if (this.btnPairLink != null) {
                    this.btnPairLink.setEnabled(false);
                }
                if (this.btnPairCut != null) {
                    this.btnPairCut.setEnabled(false);
                }
                if (this.btnPairDel != null) {
                    this.btnPairDel.setEnabled(false);
                }
            }
        }
    }

    public void resetList(JGridView jGridView, List<SparseArray<String>> list) {
        resetBtnEnable();
        if (jGridView != null) {
            App.resetList(jGridView, list);
        }
    }

    public void resume() {
        JPage jPage;
        if (this.actBt.bTop) {
            this.bTop = true;
            App.getApp().mIpcObj.recoverAppId();
            if (this.mPairedView != null && App.bShowPairedBt) {
                App.mBtInfo.mListHasPaired.clear();
                App.getApp().mIpcObj.queryPairList();
            }
            refreshList();
            refreshPairedList();
            registerSearchDevicesReceiver();
            resetBtnEnable();
            if (App.getApp().bPopBtRingSet) {
                App.getApp().popBtRingSet(false);
            }
            if (!(!App.bDelMenuFromBtAv || (jPage = this.actBt.ui.mPages.get(2)) == null || jPage.getVisibility() == 0)) {
                jPage.setVisibility(0);
            }
            bt.i();
        }
    }

    public boolean scrollOk() {
        if (!this.bTop || !this.actBt.isFocus(this.mGridView)) {
            return false;
        }
        clickItem(this.mGridView, this.mPosMoveTo);
        return true;
    }

    public boolean scrollToNext() {
        if (!this.bTop || !this.actBt.isFocus(this.mGridView)) {
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

    public boolean scrollToPrev() {
        if (!this.bTop || !this.actBt.isFocus(this.mGridView)) {
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
            JButton jButton = (JButton) getPage().getChildViewByid(276);
            if (jButton != null) {
                jButton.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton2 = (JButton) getPage().getChildViewByid(277);
            if (jButton2 != null) {
                jButton2.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton2.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton3 = (JButton) getPage().getChildViewByid(278);
            if (jButton3 != null) {
                jButton3.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton3.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
            JButton jButton4 = (JButton) getPage().getChildViewByid(279);
            if (jButton4 != null) {
                jButton4.setBackgroundTintList(new ColorStateList(iArr2, iArr));
                jButton4.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public void showConnectTip() {
        if (this.isConnectBt) {
            showConnectTip(true, App.getApp().getString("bt_linking"));
        } else {
            showConnectTip(false, App.getApp().getString("bt_connected"));
        }
    }

    public void showConnectTip(boolean z, String str) {
        if (this.tipText != null) {
            this.isConnectBt = z;
            if (this.isConnectBt && this.tipText.getVisibility() == 8) {
                this.tipText.setText(str);
                this.tipText.setVisibility(0);
                this.tipText.removeCallbacks(this.mHideText);
                this.tipText.removeCallbacks(this.mRunnableHideText2);
                this.tipText.postDelayed(this.mRunnableHideText2, 30000);
            } else if (!this.isConnectBt && !this.isStartDiscovey && this.tipText.getVisibility() == 0) {
                this.tipText.setText(str);
                this.tipText.removeCallbacks(this.mHideText);
                this.tipText.removeCallbacks(this.mRunnableHideText2);
                this.tipText.postDelayed(this.mRunnableHideText, 1000);
            }
        }
    }

    public void showDiscover() {
        if (this.isStartDiscovey) {
            showDiscover(true, App.getApp().getString("bt_discovering"));
        } else {
            showDiscover(false, App.getApp().getString("bt_discover_end"));
        }
    }

    public void showDiscover(boolean z, String str) {
        if (this.tipText != null) {
            this.isStartDiscovey = z;
            if (this.isStartDiscovey && this.tipText.getVisibility() == 8) {
                this.tipText.setText(str);
                this.tipText.removeCallbacks(this.mHideText);
                this.tipText.removeCallbacks(this.mRunnableHideText2);
                this.tipText.setVisibility(0);
            } else if (!this.isStartDiscovey && this.tipText.getVisibility() == 0) {
                this.tipText.setText(str);
                this.tipText.removeCallbacks(this.mHideText);
                this.tipText.removeCallbacks(this.mRunnableHideText2);
                this.tipText.postDelayed(this.mRunnableHideText, 1000);
            }
            if (!z && str.equals(App.getApp().getString("bt_discover_end"))) {
                bt.g();
            } else if (z && str.equals(App.getApp().getString("bt_discovering"))) {
                bt.a(App.getApp(), 100, 0);
            }
            if (bv.h() && this.isStartDiscovey && this.tipText.getVisibility() == 0) {
                this.tipText.postDelayed(this.mHideText, 10000);
            }
        }
    }
}
