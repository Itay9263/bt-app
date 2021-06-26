package com.syu.ctrl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import com.syu.app.MyApplication;
import com.syu.app.MyUi;
import com.syu.app.MyUiItem;

public class JCheckBox extends TextView {
    Drawable[] drawable;
    int flag;
    int iState = -1;
    String[] iconNames;
    boolean isCheck = false;
    String mStrDrawable;
    String mStrDrawableFix;
    private JPage page;
    Paint paint;
    String text;
    float textSize = 18.0f;
    private MyUi ui;

    public JCheckBox(Context context, JPage jPage, MyUi myUi) {
        super(context);
        this.page = jPage;
        this.ui = myUi;
        this.paint = new Paint();
        this.paint.setColor(-256);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(4.0f);
    }

    public JPage getPage() {
        return this.page;
    }

    public boolean isChecked() {
        return this.isCheck;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.drawable != null && this.drawable.length > 0) {
            Drawable drawable2 = this.drawable[this.isCheck ? (char) 1 : 0];
            if (drawable2 != null) {
                int intrinsicWidth = drawable2.getIntrinsicWidth();
                int intrinsicHeight = drawable2.getIntrinsicHeight();
                drawable2.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
                Rect[] rect = ((MyUiItem) getTag()).getRect();
                if (rect == null || rect.length < 1) {
                    canvas.save();
                    canvas.translate((float) MyUi.getFixValue(intrinsicWidth + 5, true), 0.0f);
                    super.onDraw(canvas);
                    canvas.restore();
                    canvas.save();
                    canvas.translate(0.0f, (float) ((getHeight() - MyUi.getFixValue(intrinsicHeight, true)) / 2));
                    canvas.scale(MyApplication.mScale, MyApplication.mScale);
                    drawable2.draw(canvas);
                    canvas.restore();
                } else {
                    super.onDraw(canvas);
                    canvas.save();
                    canvas.translate((float) rect[0].left, (float) ((getHeight() - MyUi.getFixValue(intrinsicHeight, true)) / 2));
                    canvas.scale(MyApplication.mScale, MyApplication.mScale);
                    drawable2.draw(canvas);
                    canvas.restore();
                }
                if (isFocused()) {
                    canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.paint);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean z, int i, Rect rect) {
        invalidate();
    }

    public void setChecked(boolean z) {
        this.isCheck = z;
        invalidate();
    }

    public void setIconName(String[] strArr) {
        this.iconNames = strArr;
        if (strArr != null) {
            this.drawable = new Drawable[2];
            int length = strArr.length;
            int i = 0;
            int i2 = 0;
            while (i < length) {
                String str = strArr[i];
                if (i2 > 1) {
                    break;
                }
                this.drawable[i2] = this.ui.getDrawableFromPath(str);
                i++;
                i2++;
            }
            invalidate();
        }
    }

    public void setPressed(boolean z) {
        super.setPressed(z);
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

    public void setText(String str) {
        if (str != null) {
            super.setText(str);
        }
    }

    public void setTextSize(float f) {
        this.textSize = f;
        super.setTextSize(f);
    }

    public void updateBackground() {
        int i = this.iState;
        if (isPressed()) {
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
