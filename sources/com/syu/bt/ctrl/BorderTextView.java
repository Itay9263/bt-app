package com.syu.bt.ctrl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class BorderTextView extends TextView implements Runnable {
    int DEFAULT = 5;
    boolean bIsFocus = false;
    int borderColor;
    float bottom;
    String content;
    float contentWidth;
    boolean isBoard = false;
    float left;
    boolean leftInit = false;
    int moveLeft = 0;
    int moveRight = 1000000;
    float paddingLeft;
    float paddingTop;
    Paint paint;
    float rectLeft;
    float rectTop;
    float right;
    boolean rightInit = false;
    boolean stop = false;
    int textColor;
    int textSize;
    float top;
    float width = 0.0f;

    public BorderTextView(Context context) {
        super(context);
    }

    public BorderTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BorderTextView(Context context, int[] iArr, String str, int i) {
        super(context);
        this.textSize = i;
        this.content = str;
        this.paint = new Paint();
        this.textColor = iArr[0];
        this.borderColor = iArr[1];
        this.paint.setTextSize((float) i);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setFlags(1);
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void onDraw(Canvas canvas) {
        String str = this.content;
        this.paddingLeft = ((float) getWidth()) - this.paint.measureText(this.content);
        this.paddingTop = (float) (getHeight() - this.textSize);
        this.contentWidth = this.paint.measureText(str);
        if (this.paddingLeft <= 10.0f) {
            str = String.valueOf(this.content.substring(0, this.paint.breakText(str, 0, str.length(), true, (float) (getWidth() - 10), (float[]) null) - 1)) + ".";
            this.contentWidth = this.paint.measureText(str);
            this.paddingLeft = ((float) getWidth()) - this.contentWidth;
        }
        this.rectLeft = this.paddingLeft / 2.0f;
        this.rectTop = this.paddingTop / 2.0f;
        Paint.FontMetrics fontMetrics = this.paint.getFontMetrics();
        float height = (((fontMetrics.bottom - fontMetrics.top) / 2.0f) - fontMetrics.bottom) + ((float) (getHeight() / 2));
        this.paint.setTextAlign(Paint.Align.CENTER);
        this.paint.setColor(this.textColor);
        canvas.drawText(str, (float) (getWidth() / 2), height, this.paint);
        if (this.bIsFocus) {
            this.paint.setColor(this.borderColor);
            canvas.drawRoundRect(this.rectLeft - ((float) this.DEFAULT), this.rectTop - ((float) this.DEFAULT), ((float) this.DEFAULT) + this.rectLeft + this.contentWidth, ((float) this.DEFAULT) + this.rectTop + ((float) this.textSize), (float) this.DEFAULT, (float) this.DEFAULT, this.paint);
        }
        super.onDraw(canvas);
        super.onDraw(canvas);
    }

    public void resetText(boolean z) {
        this.bIsFocus = z;
        invalidate();
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            postInvalidate();
        }
    }
}
