package com.syu.bt.page.pop;

import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;
import com.syu.app.App;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.page.Page;

public class Page_PopPhoneVoice extends Page {
    public JText Txt;
    AnimationDrawable animDrawable;
    public ImageView vAni;

    public Page_PopPhoneVoice(JPage jPage) {
        super(jPage);
    }

    public void init() {
        this.vAni = (ImageView) getPage().getChildViewByid(62);
        this.Txt = (JText) getPage().getChildViewByid(63);
        if (this.vAni != null) {
            this.vAni.setBackgroundResource(App.getApp().getIdAnim("voice_anim"));
            this.animDrawable = (AnimationDrawable) this.vAni.getBackground();
        }
    }

    public void pause() {
        super.pause();
        if (this.animDrawable != null) {
            this.animDrawable.stop();
        }
    }

    public void resume() {
        super.resume();
        if (this.animDrawable != null) {
            this.animDrawable.start();
        }
    }
}
