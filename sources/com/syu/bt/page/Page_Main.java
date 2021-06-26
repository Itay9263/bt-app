package com.syu.bt.page;

import android.view.View;
import com.syu.app.App;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JButton;
import com.syu.ctrl.JPage;
import com.syu.page.Page;

public class Page_Main extends Page {
    ActBt actBt;

    public Page_Main(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    private boolean btnClick(View view) {
        switch (view.getId()) {
            case 47:
                App.getApp().mIpcObj.setStandBy();
                return true;
            default:
                return false;
        }
    }

    public void ResponseClick(View view) {
        if (bt.a().d() || btnClick(view)) {
            return;
        }
        if (App.getApp().mIpcObj.isCalling()) {
            cb.a().a(App.getApp().getString("bt_state_using"));
        } else {
            this.actBt.MainClick(view);
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        JButton jButton;
        this.actBt.handleSizeChanged();
        if (App.mEnableHalfScreen) {
            if (this.actBt.isHalf()) {
                this.actBt.switch2DialWhenHalfScreen();
            }
            if (this.actBt.mPageCurrent != null) {
                int id = this.actBt.mPageCurrent.getId();
                JPage jPage = this.actBt.ui.mPages.get(2);
                if (!(jPage == null || (jButton = (JButton) jPage.getChildViewByid(36)) == null)) {
                    if (this.actBt.isHalf()) {
                        jButton.setEnabled(false);
                    } else {
                        jButton.setEnabled(true);
                    }
                }
                switch (id) {
                    case 3:
                        Page_Dial page_Dial = (Page_Dial) this.actBt.mPageCurrent.getNotify();
                        if (page_Dial != null) {
                            page_Dial.switchFull();
                            if (page_Dial.mViewShowGps == null) {
                                return;
                            }
                            if (App.getApp().isHalfScreenAble()) {
                                page_Dial.mViewShowGps.setVisibility(0);
                                return;
                            } else {
                                page_Dial.mViewShowGps.setVisibility(8);
                                return;
                            }
                        } else {
                            return;
                        }
                    case 10:
                        Page_Av page_Av = (Page_Av) this.actBt.mPageCurrent.getNotify();
                        if (page_Av != null) {
                            page_Av.switchFull();
                            if (page_Av.mViewShowGps == null) {
                                return;
                            }
                            if (App.getApp().isHalfScreenAble()) {
                                page_Av.mViewShowGps.setVisibility(0);
                                return;
                            } else {
                                page_Av.mViewShowGps.setVisibility(8);
                                return;
                            }
                        } else {
                            return;
                        }
                    case 22:
                        Page_Callin page_Callin = (Page_Callin) this.actBt.mPageCurrent.getNotify();
                        if (page_Callin != null) {
                            page_Callin.switchFull();
                            if (page_Callin.mViewShowGps == null) {
                                return;
                            }
                            if (App.getApp().isHalfScreenAble()) {
                                page_Callin.mViewShowGps.setVisibility(0);
                                return;
                            } else {
                                page_Callin.mViewShowGps.setVisibility(8);
                                return;
                            }
                        } else {
                            return;
                        }
                    default:
                        return;
                }
            }
        }
    }

    public void setPressed(JButton jButton, boolean z) {
        updateSelected(jButton.getId(), !z);
    }

    public boolean updateSelected(int i, boolean z) {
        int id;
        int fixPageMain = this.actBt.getFixPageMain(i);
        if (fixPageMain == -1) {
            return false;
        }
        if (!(this.actBt.mPageCurrent == null || (id = this.actBt.mPageCurrent.getId()) == fixPageMain)) {
            this.actBt.updateMainSelected(id, z);
        }
        return true;
    }
}
