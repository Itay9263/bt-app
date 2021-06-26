package com.syu.ctrl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.TextView;
import com.syu.app.MyUi;

public class JText extends TextView {
    private boolean bMarQuee = false;
    public boolean focus = false;
    int iState = -1;
    int[] mColor;
    String mStrDrawable;
    String mStrDrawableFix;
    private MyUi ui;

    public JText(Context context, MyUi myUi) {
        super(context);
        this.ui = myUi;
        init(context);
    }

    public int getState() {
        return this.iState;
    }

    /* access modifiers changed from: protected */
    public void init(Context context) {
    }

    public boolean isFocused() {
        return this.bMarQuee;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (this.bMarQuee && i > 0 && i2 > 0) {
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
            setMarqueeRepeatLimit(-1);
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void setColor(int... iArr) {
        this.mColor = iArr;
        updateBackground();
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        updateBackground();
    }

    public void setFocus(boolean z) {
        this.focus = z;
        updateBackground();
    }

    public void setMarQuee(boolean z) {
        this.bMarQuee = z;
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
        } else if (!isEnabled()) {
            this.iState = 2;
        } else {
            this.iState = 0;
        }
        if (i != this.iState) {
            if (this.mColor != null && this.mColor.length > this.iState) {
                setTextColor(this.mColor[this.iState]);
            }
            if (this.mStrDrawable != null) {
                this.mStrDrawableFix = this.mStrDrawable;
                switch (this.iState) {
                    case 1:
                        this.mStrDrawableFix = String.valueOf(this.mStrDrawableFix) + "_p";
                        break;
                    case 2:
                        this.mStrDrawableFix = String.valueOf(this.mStrDrawableFix) + "_u";
                        break;
                }
                setBackground(this.ui.getDrawableFromPath(this.mStrDrawableFix));
            }
        }
    }
}
