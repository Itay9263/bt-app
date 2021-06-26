package com.syu.util;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;

public class Runnable_AnimDrawable implements Runnable {
    private AnimationDrawable animationDrawable;
    public boolean bRunning = true;
    private int delay = 80;
    private int iCntFrame = 0;
    private int index = 0;
    private View v;

    public Runnable_AnimDrawable(View view, AnimationDrawable animationDrawable2, int i) {
        this.v = view;
        this.animationDrawable = animationDrawable2;
        this.delay = i;
        if (animationDrawable2 != null) {
            this.iCntFrame = animationDrawable2.getNumberOfFrames();
        }
    }

    public void run() {
        if (this.bRunning && this.v != null && this.iCntFrame > 0) {
            this.v.setBackground(this.animationDrawable.getFrame(this.index));
            if (this.index < this.iCntFrame - 1) {
                this.index++;
            } else {
                this.index = 0;
            }
            HandlerUI.getInstance().removeCallbacks(this);
            HandlerUI.getInstance().postDelayed(this, (long) this.delay);
        }
    }

    public void stopRun() {
        this.bRunning = false;
    }
}
