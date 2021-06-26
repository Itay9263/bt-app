package com.syu.bt.ctrl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class JSwitchButon2 extends View implements View.OnClickListener {
    final int MAX_VELOCITYX;
    final int MIN_FLING;
    boolean isChang;
    boolean isFling;
    float mDisx;
    float mDownx;
    float mDowny;
    GestureDetector.SimpleOnGestureListener mGenericMotionListener;
    GestureDetectorCompat mGestureDetector;
    float mLastX;
    float mLastY;
    OnSwitchStateLisenter mLisenter;
    Rect mSliderRect;
    boolean mSwitch;
    int mSwitchHeight;
    Drawable mSwitchOff;
    Drawable mSwitchOn;
    Drawable mSwitchSlider;
    int mSwitchWidth;
    int maxMoveLen;
    boolean moved;

    public interface OnSwitchStateLisenter {
        void onSwitchOff();

        void onSwitchOn();
    }

    public JSwitchButon2(Context context) {
        this(context, (AttributeSet) null);
    }

    public JSwitchButon2(Context context, Drawable drawable, Drawable drawable2, Drawable drawable3) {
        super(context);
        this.mSwitch = false;
        this.isChang = true;
        this.moved = false;
        this.MIN_FLING = 8;
        this.MAX_VELOCITYX = 2500;
        this.mGenericMotionListener = new GestureDetector.SimpleOnGestureListener() {
            public boolean onDown(MotionEvent motionEvent) {
                JSwitchButon2.this.isFling = false;
                return super.onDown(motionEvent);
            }

            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (Math.abs(motionEvent.getX() - motionEvent2.getX()) > 8.0f && Math.abs(f) < 2500.0f) {
                    JSwitchButon2.this.isFling = true;
                }
                return super.onFling(motionEvent, motionEvent2, f, f2);
            }
        };
        if (drawable != null) {
            this.mSwitchSlider = drawable;
        }
        if (drawable2 != null) {
            this.mSwitchOn = drawable2;
        }
        if (drawable3 != null) {
            this.mSwitchOff = drawable3;
        }
        this.mSwitchWidth = this.mSwitchOff.getIntrinsicWidth();
        this.mSwitchHeight = this.mSwitchOn.getIntrinsicHeight();
        this.maxMoveLen = this.mSwitchWidth - this.mSwitchSlider.getIntrinsicWidth();
    }

    public JSwitchButon2(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public JSwitchButon2(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mSwitch = false;
        this.isChang = true;
        this.moved = false;
        this.MIN_FLING = 8;
        this.MAX_VELOCITYX = 2500;
        this.mGenericMotionListener = new GestureDetector.SimpleOnGestureListener() {
            public boolean onDown(MotionEvent motionEvent) {
                JSwitchButon2.this.isFling = false;
                return super.onDown(motionEvent);
            }

            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (Math.abs(motionEvent.getX() - motionEvent2.getX()) > 8.0f && Math.abs(f) < 2500.0f) {
                    JSwitchButon2.this.isFling = true;
                }
                return super.onFling(motionEvent, motionEvent2, f, f2);
            }
        };
        this.mSwitchWidth = this.mSwitchOff.getIntrinsicWidth();
        this.mSwitchHeight = this.mSwitchOn.getIntrinsicHeight();
        this.maxMoveLen = this.mSwitchWidth - this.mSwitchSlider.getIntrinsicWidth();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setOnClickListener(this);
        this.mGestureDetector = new GestureDetectorCompat(getContext(), this.mGenericMotionListener);
        if (this.mLisenter == null) {
            this.mLisenter = new OnSwitchStateLisenter() {
                public void onSwitchOff() {
                }

                public void onSwitchOn() {
                }
            };
        }
    }

    public void onClick(View view) {
        this.mDisx = (float) (this.mSwitch ? -this.maxMoveLen : this.maxMoveLen);
        this.mSwitch = !this.mSwitch;
        if (this.mLisenter != null) {
            if (this.mSwitch) {
                this.mLisenter.onSwitchOn();
            } else {
                this.mLisenter.onSwitchOff();
            }
        }
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        boolean z = false;
        super.onDraw(canvas);
        if (this.isChang) {
            this.isChang = false;
        }
        int right = getRight() - getLeft();
        int bottom = getBottom() - getTop();
        int i = right / 2;
        int i2 = bottom / 2;
        Drawable drawable = this.mSwitch ? this.mSwitchOn : this.mSwitchOff;
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        float f = this.mDisx;
        if (right < intrinsicWidth || bottom < intrinsicHeight) {
            z = true;
            float min = Math.min(((float) right) / ((float) intrinsicWidth), ((float) bottom) / ((float) intrinsicHeight));
            canvas.save();
            canvas.scale(min, min, (float) i, (float) i2);
            f /= min;
        }
        drawable.setBounds(i - (intrinsicWidth / 2), i2 - (intrinsicHeight / 2), (intrinsicWidth / 2) + i, (intrinsicHeight / 2) + i2);
        drawable.draw(canvas);
        canvas.save();
        Drawable drawable2 = this.mSwitchSlider;
        int intrinsicWidth2 = drawable2.getIntrinsicWidth();
        if (this.mSliderRect == null) {
            this.mSliderRect = new Rect(i - (intrinsicWidth / 2), i2 - (intrinsicHeight / 2), (i - (intrinsicWidth / 2)) + intrinsicWidth2, (intrinsicHeight / 2) + i2);
        }
        if (this.mDisx != 0.0f) {
            int min2 = Math.min(((intrinsicWidth / 2) + i) - intrinsicWidth2, Math.max(i - (intrinsicWidth / 2), (int) (f + ((float) this.mSliderRect.left))));
            this.mSliderRect.set(min2, i2 - (intrinsicHeight / 2), Math.min(intrinsicWidth2 + min2, (intrinsicWidth / 2) + i), i2 + (intrinsicHeight / 2));
        }
        drawable2.setBounds(this.mSliderRect);
        drawable2.draw(canvas);
        canvas.restore();
        if (z) {
            canvas.restore();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        float f = 1.0f;
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size2 = View.MeasureSpec.getSize(i2);
        float f2 = (mode == 0 || size >= this.mSwitchWidth) ? 1.0f : ((float) size) / ((float) this.mSwitchWidth);
        if (mode2 != 0 && size2 < this.mSwitchHeight) {
            f = ((float) size2) / ((float) this.mSwitchHeight);
        }
        float min = Math.min(f2, f);
        setMeasuredDimension(resolveSizeAndState((int) (((float) this.mSwitchWidth) * min), i, 0), resolveSizeAndState((int) (min * ((float) this.mSwitchHeight)), i2, 0));
    }

    public void setOnSwitchStateLisenter(OnSwitchStateLisenter onSwitchStateLisenter) {
        this.mLisenter = onSwitchStateLisenter;
    }

    public void setSwitch(boolean z) {
        if (z != this.mSwitch) {
            this.mSwitch = z;
            this.mDisx = (float) (z ? this.maxMoveLen : -this.maxMoveLen);
            invalidate();
        }
    }
}
