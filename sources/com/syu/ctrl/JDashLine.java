package com.syu.ctrl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

public class JDashLine extends View {
    private Paint mPaint;

    public JDashLine(Context context) {
        super(context);
        Init();
    }

    private void Init() {
        this.mPaint = new Paint();
        this.mPaint.setStyle(Paint.Style.STROKE);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Path path = new Path();
        if (getWidth() > getHeight()) {
            path.moveTo(0.0f, 0.0f);
            path.lineTo((float) getWidth(), 0.0f);
        } else {
            path.moveTo(0.0f, 0.0f);
            path.lineTo(0.0f, (float) getHeight());
        }
        this.mPaint.setPathEffect(new DashPathEffect(new float[]{5.0f, 5.0f, 5.0f, 5.0f}, 1.0f));
        canvas.drawPath(path, this.mPaint);
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
