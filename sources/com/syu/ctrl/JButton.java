package com.syu.ctrl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import com.syu.app.MyUi;
import com.syu.app.MyUiItem;
import com.syu.page.IPageNotify;
import java.util.List;

public class JButton extends TextView {
    public boolean bPartnerDependent = false;
    public boolean bUpdateBkOnPress = true;
    public boolean focus = false;
    int iState = -1;
    int[] mColor;
    int[] mColorBk;
    public Drawable mIcon;
    public List<View> mPartnerViews;
    public int[] mPosIcon;
    String mStrDrawable;
    String mStrDrawableFix;
    String mStrIcon;
    String mStrIconFix;
    public JPage page;
    public JPage pagePress;
    public MyUi ui;

    public JButton(Context context, JPage jPage, MyUi myUi) {
        super(context);
        this.page = jPage;
        this.ui = myUi;
    }

    public int getState() {
        return this.iState;
    }

    public String getStrDrawable() {
        return this.mStrDrawable;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mIcon != null) {
            this.mIcon.setBounds(this.mPosIcon[0], this.mPosIcon[1], this.mPosIcon[2], this.mPosIcon[3]);
            this.mIcon.draw(canvas);
            this.mIcon.setBounds(0, 0, this.mIcon.getIntrinsicWidth(), this.mIcon.getIntrinsicHeight());
        }
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean z, int i, Rect rect) {
        setFocus(z);
    }

    public void setColor(int... iArr) {
        this.mColor = iArr;
        updateBackground();
    }

    public void setColorBk(int... iArr) {
        this.mColorBk = iArr;
        updateBackground();
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        if (z) {
            setFocusable(true);
        } else {
            setFocusable(false);
        }
        updateBackground();
    }

    public void setFocus(boolean z) {
        this.focus = z;
        updateBackground();
    }

    public void setIcon(int[] iArr, String str) {
        if (iArr != null && iArr.length == 4) {
            if (this.mPosIcon == null) {
                this.mPosIcon = new int[4];
            }
            this.mPosIcon[0] = iArr[0];
            this.mPosIcon[1] = iArr[1];
            this.mPosIcon[2] = iArr[2];
            this.mPosIcon[3] = iArr[3];
        }
        this.mStrIcon = str;
    }

    public void setPartnerFocus(boolean z) {
        if (this.mPartnerViews != null) {
            for (View next : this.mPartnerViews) {
                switch (((MyUiItem) next.getTag()).getType()) {
                    case 6:
                        ((JView) next).setFocus(z);
                        break;
                    case 10:
                        ((JText) next).setFocus(z);
                        break;
                }
            }
        }
    }

    public void setPressed(boolean z) {
        super.setPressed(z);
        updateBackground();
        IPageNotify notify = this.page.getNotify();
        if (notify != null) {
            notify.setPressed(this, z);
        }
        if (this.pagePress != null) {
            this.pagePress.onPress(z);
        }
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

    public void setUpdateBkOnPress(boolean z) {
        this.bUpdateBkOnPress = z;
    }

    public void updateBackground() {
        Drawable drawableFromPath;
        Drawable drawableFromPath2;
        boolean z = true;
        int i = this.iState;
        if (this.focus) {
            this.iState = 1;
        } else if (isFocused()) {
            this.iState = 3;
        } else if (!isEnabled()) {
            this.iState = 2;
        } else if (isPressed()) {
            this.iState = 1;
        } else {
            this.iState = 0;
        }
        if (!this.bPartnerDependent) {
            if (this.iState != 1) {
                z = false;
            }
            setPartnerFocus(z);
        }
        if (i != this.iState) {
            if (this.mColor != null && this.mColor.length > this.iState) {
                setTextColor(this.mColor[this.iState]);
            }
            if (this.bUpdateBkOnPress) {
                if (this.mStrDrawable != null) {
                    this.mStrDrawableFix = this.mStrDrawable;
                    switch (this.iState) {
                        case 0:
                            drawableFromPath2 = null;
                            break;
                        case 1:
                            this.mStrDrawableFix = String.valueOf(this.mStrDrawableFix) + "_p";
                            drawableFromPath2 = null;
                            break;
                        case 2:
                            this.mStrDrawableFix = String.valueOf(this.mStrDrawableFix) + "_u";
                            drawableFromPath2 = null;
                            break;
                        case 3:
                            this.mStrDrawableFix = String.valueOf(this.mStrDrawableFix) + "_f";
                            drawableFromPath2 = this.ui.getDrawableFromPath(this.mStrDrawableFix);
                            if (drawableFromPath2 == null) {
                                this.mStrDrawableFix = String.valueOf(this.mStrDrawable) + "_p";
                                drawableFromPath2 = null;
                                break;
                            }
                            break;
                        default:
                            drawableFromPath2 = null;
                            break;
                    }
                    if (drawableFromPath2 == null) {
                        drawableFromPath2 = this.ui.getDrawableFromPath(this.mStrDrawableFix);
                    }
                    setBackground(drawableFromPath2);
                } else if (this.mColorBk != null && this.mColorBk.length > this.iState) {
                    setBackground(new ColorDrawable(this.mColorBk[this.iState]));
                }
            }
            if (this.mStrIcon != null && this.mPosIcon != null) {
                this.mStrIconFix = this.mStrIcon;
                switch (this.iState) {
                    case 0:
                        drawableFromPath = null;
                        break;
                    case 1:
                        this.mStrIconFix = String.valueOf(this.mStrIconFix) + "_p";
                        drawableFromPath = null;
                        break;
                    case 2:
                        this.mStrIconFix = String.valueOf(this.mStrIconFix) + "_u";
                        drawableFromPath = null;
                        break;
                    case 3:
                        this.mStrIconFix = String.valueOf(this.mStrIconFix) + "_f";
                        drawableFromPath = this.ui.getDrawableFromPath(this.mStrIconFix);
                        if (drawableFromPath == null) {
                            this.mStrIconFix = String.valueOf(this.mStrIcon) + "_p";
                            drawableFromPath = null;
                            break;
                        }
                        break;
                    default:
                        drawableFromPath = null;
                        break;
                }
                if (drawableFromPath == null) {
                    drawableFromPath = this.ui.getDrawableFromPath(this.mStrIconFix);
                }
                this.mIcon = drawableFromPath;
                invalidate();
            }
        }
    }
}
