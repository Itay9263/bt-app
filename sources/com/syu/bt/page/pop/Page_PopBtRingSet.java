package com.syu.bt.page.pop;

import android.view.View;
import com.syu.app.App;
import com.syu.app.ipc.IpcObj;
import com.syu.bt.page.Page_Set;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JSeekBar;
import com.syu.ctrl.JText;
import com.syu.page.Page;

public class Page_PopBtRingSet extends Page {
    public Page_Set BtSet;
    public JSeekBar mSeekBar;
    public String strRingSet;
    public JText tvRingLevel;

    public Page_PopBtRingSet(JPage jPage) {
        super(jPage);
    }

    public void ResponseClick(View view) {
        App.iTimerCnt = 5;
        App.iRingLevel = this.mSeekBar.getValue();
        IpcObj.SetRingLevel(App.iRingLevel);
        this.strRingSet = String.valueOf(App.iRingLevel);
        if (this.tvRingLevel != null) {
            this.tvRingLevel.setText(this.strRingSet);
        }
    }

    public void init() {
        this.mSeekBar = (JSeekBar) getPage().getChildViewByid(45);
        if (this.mSeekBar != null) {
            this.mSeekBar.setProgressMax(10);
        }
        this.tvRingLevel = (JText) getPage().getChildViewByid(46);
    }

    public void pause() {
        super.pause();
    }

    public void resume() {
        super.resume();
        this.mSeekBar.setProgress(App.iRingLevel);
        this.strRingSet = String.valueOf(App.iRingLevel);
        if (this.tvRingLevel != null) {
            this.tvRingLevel.setText(this.strRingSet);
        }
    }
}
