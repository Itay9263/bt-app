package com.syu.ctrl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class JLine extends View {
    private Paint mPaint;

    public JLine(Context context) {
        super(context);
        Init();
    }

    private void Init() {
        this.mPaint = new Paint();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getWidth() > getHeight()) {
            canvas.drawLine(0.0f, 0.0f, (float) getWidth(), 0.0f, this.mPaint);
            return;
        }
        canvas.drawLine(0.0f, 0.0f, 0.0f, (float) getHeight(), this.mPaint);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i > 0 && i2 > 0) {
            if (i > i2) {
                this.mPaint.setStrokeWidth((float) i2);
            } else {
                this.mPaint.setStrokeWidth((float) i);
            }
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void setColor(int i) {
        this.mPaint.setColor(i);
    }
}
