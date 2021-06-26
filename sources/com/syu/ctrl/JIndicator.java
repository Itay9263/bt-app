package com.syu.ctrl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.syu.app.MyUi;
import com.syu.util.FuncUtils;

public class JIndicator extends View {
    private int current = 0;
    Drawable drawable_focus;
    Drawable drawable_normal;
    private ViewGroup group;
    private int height;
    private Bitmap mBitmap = null;
    private Bitmap mBitmap_Focus = null;
    private Bitmap mBitmap_Normal = null;
    private Canvas mCanvas = null;
    private int mHeight;
    private float mLastX;
    private float mLastY;
    private int mWidth;
    private int space = 5;
    private int total = 3;
    private MyUi ui;
    private int width;
    private int xindex = 0;
    private int yindex = 0;

    public JIndicator(Context context, MyUi myUi) {
        super(context);
        this.ui = myUi;
    }

    /* access modifiers changed from: protected */
    public void Drawing(Canvas canvas) {
        if (this.total > 0) {
            this.mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
            int i = this.xindex;
            int i2 = this.width + this.space;
            for (int i3 = 0; i3 < this.total; i3++) {
                if (i3 == this.current) {
                    this.mCanvas.drawBitmap(this.mBitmap_Focus, (float) i, (float) this.yindex, (Paint) null);
                } else {
                    this.mCanvas.drawBitmap(this.mBitmap_Normal, (float) i, (float) this.yindex, (Paint) null);
                }
                i += i2;
            }
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    public void Findindex(float f, float f2) {
        int i = 0;
        while (i < this.total) {
            if (!new Rect(this.xindex + ((this.width + this.space) * i), 0, this.xindex + ((this.width + this.space) * i) + this.width, this.mHeight).contains((int) f, (int) f2)) {
                i++;
            } else if (this.current != i) {
                this.current = i;
                return;
            } else {
                return;
            }
        }
    }

    public void InitIndicator() {
        this.mBitmap_Normal = getBitmapFromDrawable(this.drawable_normal);
        this.mBitmap_Focus = getBitmapFromDrawable(this.drawable_focus);
        this.width = this.mBitmap_Normal.getWidth();
        this.height = this.mBitmap_Normal.getHeight();
    }

    public Bitmap getBitmapFromDrawable(Drawable drawable) {
        return FuncUtils.getBitmapFromDrawable(drawable, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
    }

    public boolean isCilck(float f, float f2, float f3, float f4, long j, long j2, long j3) {
        return Math.abs(f - f3) <= 10.0f && Math.abs(f2 - f4) <= 10.0f && ((float) (j2 - j)) < ((float) j3);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void onClick() {
        Findindex(this.mLastX, this.mLastY);
        if (this.group instanceof JListApp) {
            ((JListApp) this.group).snapToScreen(this.current);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mBitmap == null) {
            this.mWidth = getWidth();
            this.mHeight = getHeight();
            this.xindex = this.mWidth - (this.width * this.total);
            this.yindex = this.mHeight - this.height;
            if (this.xindex < 0 || this.yindex < 0) {
                throw new IllegalStateException("mWidth is not enough! mWidth must be more than " + (this.width * this.total) + "or mHeight is not enough! mHeight must be more than " + this.height);
            }
            this.xindex /= 2;
            this.yindex /= 2;
            this.mBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888);
            this.mCanvas = new Canvas(this.mBitmap);
        }
        Drawing(canvas);
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i > 0 && i2 > 0) {
            super.onSizeChanged(i, i2, i3, i4);
            InitIndicator();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = (float) ((int) motionEvent.getX());
        float y = (float) ((int) motionEvent.getY());
        if (x < ((float) this.xindex) || x > ((float) (this.mWidth - this.xindex)) || y < 0.0f || y > ((float) this.mHeight)) {
            return false;
        }
        switch (motionEvent.getAction()) {
            case 0:
                this.mLastX = x;
                this.mLastY = y;
                break;
            case 1:
                if (isCilck(this.mLastX, this.mLastY, x, y, motionEvent.getDownTime(), motionEvent.getEventTime(), 500)) {
                    onClick();
                    break;
                }
                break;
        }
        return true;
    }

    public void setCurrent(int i) {
        this.current = i;
        invalidate();
    }

    public void setIconName(String[] strArr) {
        this.drawable_normal = this.ui.getDrawableFromPath(strArr[0]);
        this.drawable_focus = this.ui.getDrawableFromPath(strArr[1]);
    }

    public void setTarget(ViewGroup viewGroup) {
        this.group = viewGroup;
    }

    public void setTotal(int i) {
        this.total = i;
        this.current = Math.min(Math.max(0, this.current), this.total);
        invalidate();
    }
}
