package com.syu.bt.page.pop;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import com.syu.app.App;
import com.syu.bt.act.ActBt;
import com.syu.bt.page.Page_Set;
import com.syu.ctrl.JGridView;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.ipcself.module.main.Main;
import com.syu.page.Page;
import com.syu.util.MySharePreference;
import java.util.ArrayList;
import java.util.List;

public class Page_PopBtRing extends Page {
    ActBt actBt;
    public List<SparseArray<String>> listFile = new ArrayList();
    JGridView mGridView;
    JText mTip;

    public Page_PopBtRing(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void GridClick(JGridView jGridView) {
        int id = jGridView.getId();
        int position = jGridView.getPosition();
        switch (id) {
            case 323:
                if (this.listFile.size() > position) {
                    final String str = (String) this.listFile.get(position).get(327);
                    final String str2 = (String) this.listFile.get(position).get(326);
                    if (!TextUtils.isEmpty(str)) {
                        App.startThread(App.StrThreadFactoryTest, new Runnable() {
                            public void run() {
                                bw.a().a(str, "/mnt/sdcard/.btring/ring.mp3", false, false);
                                cb.a().a(App.getApp().getString("bt_add_successed"));
                                MySharePreference.saveStringValue("name_ring", str2);
                                final String str = str2;
                                Main.postRunnable_Ui(false, new Runnable() {
                                    public void run() {
                                        Page_Set pageSet = Page_PopBtRing.this.actBt.getPageSet();
                                        if (pageSet != null) {
                                            pageSet.updateRingName(str);
                                        }
                                    }
                                });
                            }
                        }, false, 1);
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void InitGridItem(JGridView jGridView, JPage jPage, SparseArray<String> sparseArray, int i) {
        switch (jGridView.getId()) {
            case 323:
                View childViewByid = jPage.getChildViewByid(325);
                if (childViewByid == null) {
                    return;
                }
                if (sparseArray.size() == 0) {
                    jPage.setFocus(false);
                    childViewByid.setVisibility(8);
                    return;
                }
                childViewByid.setVisibility(0);
                return;
            default:
                return;
        }
    }

    public void init() {
        this.mTip = (JText) getPage().getChildViewByid(324);
        this.mGridView = (JGridView) getPage().getChildViewByid(323);
        App.startThread(App.StrThreadFactoryTest, new Runnable() {
            public void run() {
                bw.a().a("/mnt/sdcard/btring/", Page_PopBtRing.this.listFile);
                Main.postRunnable_Ui(false, new Runnable() {
                    public void run() {
                        if (Page_PopBtRing.this.listFile.size() > 0) {
                            App.resetList(Page_PopBtRing.this.mGridView, Page_PopBtRing.this.listFile);
                        } else if (Page_PopBtRing.this.mTip != null) {
                            Page_PopBtRing.this.mTip.setVisibility(0);
                        }
                    }
                });
            }
        }, false, 1);
    }

    public void onDismiss() {
        super.onDismiss();
    }

    public void resume() {
    }
}
