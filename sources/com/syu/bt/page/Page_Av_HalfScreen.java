package com.syu.bt.page;

import android.view.View;
import android.widget.TextView;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.act.ActBt;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.ipcself.module.main.Bt;
import com.syu.page.Page;
import com.syu.util.FuncUtils;

public class Page_Av_HalfScreen extends Page {
    public ActBt actBt;
    public boolean misAvStop = false;
    public TextView tvTitle_Half;

    public Page_Av_HalfScreen(JPage jPage, ActBt actBt2) {
        super(jPage);
        this.actBt = actBt2;
    }

    public void ResponseClick(View view) {
        switch (view.getId()) {
            case 98:
                App.getApp().setFullScreenMode(1);
                break;
            case 209:
                if (!FuncUtils.isFastDoubleClick()) {
                    IpcObj.avPrev();
                    this.misAvStop = false;
                    break;
                }
                break;
            case 210:
                if (!FuncUtils.isFastDoubleClick()) {
                    IpcObj.avPlayPause();
                    this.misAvStop = false;
                    break;
                }
                break;
            case 212:
                if (!FuncUtils.isFastDoubleClick()) {
                    IpcObj.avPlay();
                    this.misAvStop = false;
                    break;
                }
                break;
            case 213:
                if (!FuncUtils.isFastDoubleClick()) {
                    IpcObj.avPause();
                    this.misAvStop = false;
                    break;
                }
                break;
            case 214:
                if (!FuncUtils.isFastDoubleClick()) {
                    IpcObj.avStop();
                    this.misAvStop = true;
                    break;
                }
                break;
            case 215:
                if (!FuncUtils.isFastDoubleClick()) {
                    IpcObj.avNext();
                    this.misAvStop = false;
                    break;
                }
                break;
        }
        if (this.actBt.MenuClick(view)) {
        }
    }

    public void init() {
    }

    public void onNotify(int i, int[] iArr, float[] fArr, String[] strArr) {
        App.getApp().mIpcObj.onNotify(this, i, iArr, fArr, strArr);
    }

    public void pause() {
        JPage jPage;
        App.getApp().DisableGainSpec();
        App.bResumeByDial = false;
        if (App.bDelMenuFromBtAv && (jPage = this.actBt.ui.mPages.get(2)) != null) {
            jPage.setVisibility(0);
        }
    }

    public void resume() {
        JPage jPage;
        if (this.actBt.bTop) {
            App.getApp().mIpcObj.updateNotify_BtAv_HalfScreen();
            App.getApp().mIpcObj.requestAppIdRight(10);
            App.getApp().EnableGainSpec();
            if (App.bDelMenuFromBtAv && (jPage = this.actBt.ui.mPages.get(2)) != null) {
                jPage.setVisibility(8);
            }
            this.tvTitle_Half = (JText) getPage().getChildViewByid(223);
            if (IpcObj.isDisConnect()) {
                if (this.tvTitle_Half != null) {
                    this.tvTitle_Half.setText(App.getApp().getString("bt_disconnected"));
                }
            } else if (this.tvTitle_Half != null) {
                this.tvTitle_Half.setText(Bt.sId3Title);
            }
            if (App.getApp().bPopBtRingSet) {
                App.getApp().popBtRingSet(false);
            }
        }
    }
}
