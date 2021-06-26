package com.syu.ctrl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.syu.app.MyUi;

public class JView extends View {
    public boolean focus = false;
    int iState = -1;
    String mStrDrawable;
    String mStrDrawableFix;
    private MyUi ui;

    public JView(Context context, MyUi myUi) {
        super(context);
        this.ui = myUi;
    }

    public int getState() {
        return this.iState;
    }

    public String getStrDrawable() {
        return this.mStrDrawable;
    }

    public void setFocus(boolean z) {
        this.focus = z;
        updateBackground();
    }

    public void setStrDrawable(String str, boolean z) {
        this.iState = -1;
        this.mStrDrawable = str;
        if (!z) {
            return;
        }
        if (str == null) {
            setBackground((Drawable) null);
        } else {
            updateBackground();
        }
    }

    public void updateBackground() {
        int i = this.iState;
        if (this.focus) {
            this.iState = 1;
        } else {
            this.iState = 0;
        }
        if (i != this.iState && this.mStrDrawable != null) {
            this.mStrDrawableFix = this.mStrDrawable;
            switch (this.iState) {
                case 1:
                    this.mStrDrawableFix = String.valueOf(this.mStrDrawableFix) + "_p";
                    break;
            }
            setBackground(this.ui.getDrawableFromPath(this.mStrDrawableFix));
        }
    }
}
