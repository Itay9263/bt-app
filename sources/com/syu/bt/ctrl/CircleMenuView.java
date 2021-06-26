package com.syu.bt.ctrl;

import android.content.Context;
import android.filterfw.geometry.Point;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class CircleMenuView extends View {
    static double[] degrees = {142.925d, 166.697d, 192.568d, 218.078d, 242.289d, 267.511d, 292.334d, 319.117d, 342.942d, 7.763d, 32.125d, 57.99d, 83.759d, 94.59d};
    int centerX;
    int centerY;
    double degree = 142.925d;
    private int height;
    private PaintFlagsDrawFilter mDrawFilter;
    long mDrawTime = 0;
    Paint paint = new Paint();
    Point point;
    Drawable pointDrawable;
    int progress = 0;
    private Bitmap rotatBitmap;
    double target = 142.925d;
    int toDirection = 0;
    private int width;

    public CircleMenuView(Context context, Drawable drawable) {
        super(context);
        if (drawable != null) {
            this.pointDrawable = drawable;
        }
        initData();
    }

    public CircleMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setPadding(0, 0, 0, 0);
    }

    private void initData() {
        this.rotatBitmap = ((BitmapDrawable) this.pointDrawable).getBitmap();
        this.width = this.rotatBitmap.getWidth();
        this.height = this.rotatBitmap.getHeight();
        this.mDrawFilter = new PaintFlagsDrawFilter(0, 3);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        double abs;
        super.onDraw(canvas);
        double d = this.target;
        double d2 = this.degree;
        long drawingTime = getDrawingTime();
        boolean z = false;
        if (d2 != d) {
            if (this.mDrawTime == 0) {
                this.mDrawTime = drawingTime - 17;
            }
            z = true;
        }
        float max = Math.max(0.0f, Math.min(20.0f, Math.max(6.0f, (float) ((int) (((float) ((drawingTime - this.mDrawTime) * 360)) / 300.0f)))));
        this.mDrawTime = drawingTime;
        if (Math.abs(d2 - d) <= ((double) Math.abs(max))) {
            abs = Math.max(0.0d, Math.min(d, 360.0d));
            this.degree = abs;
            z = false;
        } else {
            if (this.toDirection == 1) {
                abs = d2 > 180.0d ? d2 + ((Math.abs((360.0d - d2) + d) / ((360.0d - d2) + d)) * ((double) max)) : d2 + ((Math.abs(d2 - d) / (d - d2)) * ((double) max));
                if (abs > 360.0d) {
                    abs = Math.min(d, abs % 360.0d);
                }
            } else if (this.toDirection == 2) {
                abs = d2 < 180.0d ? d2 + ((Math.abs((360.0d - d) + d2) / ((d - d2) - 360.0d)) * ((double) max)) : d2 + ((Math.abs(d2 - d) / (d - d2)) * ((double) max));
                if (abs <= 0.0d) {
                    abs = 360.0d;
                }
            } else {
                abs = d2 + ((Math.abs(d2 - d) / (d - d2)) * ((double) max));
            }
            this.degree = abs;
        }
        Matrix matrix = new Matrix();
        matrix.preRotate((float) abs);
        matrix.preTranslate((-((float) this.width)) / 2.0f, (-((float) this.height)) / 2.0f);
        matrix.postTranslate(((float) this.width) / 2.0f, ((float) this.height) / 2.0f);
        canvas.setDrawFilter(this.mDrawFilter);
        canvas.drawBitmap(this.rotatBitmap, matrix, this.paint);
        if (z) {
            invalidate();
            return;
        }
        this.toDirection = 0;
        this.mDrawTime = 0;
    }

    public void reDrawPic(double d) {
        this.degree = d;
        invalidate();
    }

    public void reDrawPicByNum(int i) {
        switch (i) {
            case 103:
            case 104:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case 113:
            case 114:
                this.target = degrees[i - 103];
                break;
            case 120:
                this.target = degrees[degrees.length - 1];
                break;
            case 133:
                this.target = degrees[degrees.length - 2];
                break;
        }
        this.toDirection = 0;
        if (this.degree != this.target) {
            if (this.degree >= 192.568d && this.target <= 57.99d) {
                this.toDirection = 1;
            } else if (this.degree <= 57.99d && this.target >= 192.568d) {
                this.toDirection = 2;
            }
            invalidate();
        }
    }
}
