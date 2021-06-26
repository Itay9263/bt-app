package com.syu.ctrl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.syu.app.MyApplication;
import com.syu.app.MyUi;
import com.syu.app.MyUiItem;
import com.syu.ipcself.module.main.Main;
import com.syu.page.IPageNotify;
import com.syu.util.FuncUtils;
import com.syu.util.UiNotifyEvent;

public class JSwitchButton extends View {
    public static final int ANIMATION_FRAME_DURATION = 16;
    public static final int MAX_ALPHA = 255;
    public static final float VELOCITY = 1000.0f;
    public boolean bFocusAble;
    public boolean bIsCheckBox;
    public boolean bStateClickDown;
    Drawable[] drawable;
    Drawable drawable_Bk;
    Drawable drawable_Bottom;
    Drawable drawable_Frame;
    Drawable drawable_Mask;
    Drawable drawable_Normal;
    int iState = -1;
    private int mAlpha = 255;
    private float mAnimatedVelocity;
    /* access modifiers changed from: private */
    public boolean mAnimating;
    private float mAnimationPosition;
    private Bitmap mBk;
    private Bitmap mBottom;
    private float mBtnInitPos;
    private Bitmap mBtnNormal;
    private float mBtnOffPos;
    private float mBtnOnPos;
    private float mBtnPos;
    private float mBtnWidth;
    /* access modifiers changed from: private */
    public boolean mChecked = false;
    private int mClickTimeout;
    private float mFirstDownX;
    private float mFirstDownY;
    private Bitmap mFrame;
    private boolean mIsConditionSwitch = false;
    private Bitmap mMask;
    private float mMaskWidth;
    private Paint mPaint;
    private PerformClick mPerformClick;
    private float mRealPos;
    private RectF mSaveLayerRectF;
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;
    String mStrDrawable;
    String mStrDrawableFix;
    private int mTouchSlop;
    private boolean mTurningOn;
    private float mVelocity;
    private PorterDuffXfermode mXfermode;
    /* access modifiers changed from: private */
    public JPage page;
    Paint paint;
    private MyUi ui;

    private class PerformClick implements Runnable {
        private PerformClick() {
        }

        /* synthetic */ PerformClick(JSwitchButton jSwitchButton, PerformClick performClick) {
            this();
        }

        public void run() {
            JSwitchButton.this.performClick();
        }
    }

    private class RunnableSetChecked implements Runnable {
        private boolean checked;
        private View v;

        public RunnableSetChecked(View view, boolean z) {
            this.v = view;
            this.checked = z;
        }

        public void run() {
            if (JSwitchButton.this.mChecked != this.checked) {
                JSwitchButton.this.setChecked(this.checked);
                IPageNotify notify = JSwitchButton.this.page.getNotify();
                if (notify != null) {
                    notify.ResponseClick(this.v);
                }
            }
        }
    }

    private class SwitchAnimation implements Runnable {
        private SwitchAnimation() {
        }

        /* synthetic */ SwitchAnimation(JSwitchButton jSwitchButton, SwitchAnimation switchAnimation) {
            this();
        }

        public void run() {
            if (JSwitchButton.this.mAnimating) {
                JSwitchButton.this.doAnimation();
                Main.postRunnable_Ui(true, this, 16);
            }
        }
    }

    public JSwitchButton(Context context, JPage jPage, MyUi myUi, boolean z) {
        super(context);
        this.page = jPage;
        this.ui = myUi;
        this.bIsCheckBox = z;
        initView(context);
        this.paint = new Paint();
        this.paint.setColor(-256);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(4.0f);
    }

    /* access modifiers changed from: private */
    public void doAnimation() {
        this.mAnimationPosition += (this.mAnimatedVelocity * 16.0f) / 1000.0f;
        if (this.mAnimationPosition <= this.mBtnOffPos) {
            stopAnimation();
            this.mAnimationPosition = this.mBtnOffPos;
            setCheckedDelayed(false);
        } else if (this.mAnimationPosition >= this.mBtnOnPos) {
            stopAnimation();
            this.mAnimationPosition = this.mBtnOnPos;
            setCheckedDelayed(true);
        }
        moveView(this.mAnimationPosition);
    }

    private float getRealPos(float f) {
        return f - (this.mBtnWidth / 2.0f);
    }

    private void initView(Context context) {
        this.mPaint = new Paint();
        this.mPaint.setColor(-1);
        this.mClickTimeout = ViewConfiguration.getPressedStateDuration() + ViewConfiguration.getTapTimeout();
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mVelocity = (float) ((int) ((1000.0f * MyApplication.mDispayMetrics.density) + 0.5f));
    }

    private void moveView(float f) {
        this.mBtnPos = f;
        this.mRealPos = getRealPos(this.mBtnPos);
        invalidate();
    }

    private void setCheckedDelayed(boolean z) {
        UiNotifyEvent.HANDLER_UI.postDelayed(new RunnableSetChecked(this, z), 10);
    }

    private void startAnimation(boolean z) {
        this.mAnimating = true;
        this.mAnimatedVelocity = z ? -this.mVelocity : this.mVelocity;
        this.mAnimationPosition = this.mBtnPos;
        new SwitchAnimation(this, (SwitchAnimation) null).run();
    }

    private void stopAnimation() {
        this.mAnimating = false;
    }

    public Bitmap getBitmapFromDrawable(Drawable drawable2) {
        return FuncUtils.getBitmapFromDrawable(drawable2, (int) (((float) drawable2.getIntrinsicWidth()) * this.mScaleX), (int) (((float) drawable2.getIntrinsicHeight()) * this.mScaleY));
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.bIsCheckBox) {
            if (this.drawable != null && this.drawable.length > 0) {
                Drawable drawable2 = this.drawable[this.mChecked ? (char) 1 : 0];
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
                } else {
                    return;
                }
            }
        } else if (this.mXfermode != null) {
            super.onDraw(canvas);
            if (this.mBk != null) {
                canvas.drawBitmap(this.mBk, 0.0f, 0.0f, this.mPaint);
            }
            canvas.saveLayerAlpha(this.mSaveLayerRectF, this.mAlpha, 31);
            canvas.drawBitmap(this.mMask, this.mSaveLayerRectF.left, this.mSaveLayerRectF.top, this.mPaint);
            this.mPaint.setXfermode(this.mXfermode);
            canvas.drawBitmap(this.mBottom, this.mSaveLayerRectF.left + this.mRealPos, this.mSaveLayerRectF.top, this.mPaint);
            this.mPaint.setXfermode((Xfermode) null);
            canvas.drawBitmap(this.mFrame, this.mSaveLayerRectF.left, this.mSaveLayerRectF.top, this.mPaint);
            canvas.drawBitmap(this.mBtnNormal, this.mSaveLayerRectF.left + this.mRealPos, this.mSaveLayerRectF.top, this.mPaint);
            canvas.restore();
        } else {
            return;
        }
        if (isFocused()) {
            canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), this.paint);
        }
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean z, int i, Rect rect) {
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        if (this.bIsCheckBox) {
            super.onSizeChanged(i, i2, i3, i4);
        } else if (i > 0 && i2 > 0 && this.drawable_Mask != null) {
            if (this.drawable_Bk != null) {
                this.mScaleX = (((float) i) * 1.0f) / ((float) this.drawable_Bk.getIntrinsicWidth());
                this.mScaleY = (((float) i2) * 1.0f) / ((float) this.drawable_Bk.getIntrinsicHeight());
            } else {
                this.mScaleX = (((float) i) * 1.0f) / ((float) this.drawable_Mask.getIntrinsicWidth());
                this.mScaleY = (((float) i2) * 1.0f) / ((float) this.drawable_Mask.getIntrinsicHeight());
            }
            if (this.drawable_Bk != null) {
                this.mBk = getBitmapFromDrawable(this.drawable_Bk);
            }
            this.mBottom = getBitmapFromDrawable(this.drawable_Bottom);
            this.mBtnNormal = getBitmapFromDrawable(this.drawable_Normal);
            this.mFrame = getBitmapFromDrawable(this.drawable_Frame);
            this.mMask = getBitmapFromDrawable(this.drawable_Mask);
            this.mBtnWidth = (float) this.mBtnNormal.getWidth();
            this.mMaskWidth = (float) this.mMask.getWidth();
            this.mBtnOffPos = this.mMaskWidth - (this.mBtnWidth / 2.0f);
            this.mBtnOnPos = this.mBtnWidth / 2.0f;
            this.mBtnPos = this.mChecked ? this.mBtnOnPos : this.mBtnOffPos;
            this.mRealPos = getRealPos(this.mBtnPos);
            if (this.mBk != null) {
                int width = ((this.mBk.getWidth() - this.mMask.getWidth()) + 1) / 2;
                i5 = ((this.mBk.getHeight() - this.mMask.getHeight()) + 1) / 2;
                i6 = width;
            } else {
                i5 = 0;
                i6 = 0;
            }
            this.mSaveLayerRectF = new RectF((float) i6, (float) i5, (float) (i6 + this.mMask.getWidth()), (float) (i5 + this.mMask.getHeight()));
            this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean z = true;
        boolean z2 = false;
        if (this.bIsCheckBox) {
            return super.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        switch (action) {
            case 0:
                if (!this.bStateClickDown) {
                    this.bStateClickDown = true;
                    updateBackground();
                    break;
                }
                break;
            case 1:
            case 3:
                if (this.bStateClickDown) {
                    this.bStateClickDown = false;
                    updateBackground();
                    break;
                }
                break;
        }
        if (this.mIsConditionSwitch) {
            switch (action) {
                case 1:
                    IPageNotify notify = this.page.getNotify();
                    if (notify != null) {
                        notify.ResponseClick(this);
                        break;
                    }
                    break;
            }
            return isEnabled();
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float abs = Math.abs(x - this.mFirstDownX);
        float abs2 = Math.abs(y - this.mFirstDownY);
        switch (action) {
            case 0:
                this.mFirstDownX = x;
                this.mFirstDownY = y;
                this.mBtnInitPos = this.mChecked ? this.mBtnOnPos : this.mBtnOffPos;
                break;
            case 1:
            case 3:
                float eventTime = (float) (motionEvent.getEventTime() - motionEvent.getDownTime());
                if (abs2 < ((float) this.mTouchSlop) && abs < ((float) this.mTouchSlop) && eventTime < ((float) this.mClickTimeout)) {
                    if (this.mPerformClick == null) {
                        this.mPerformClick = new PerformClick(this, (PerformClick) null);
                    }
                    if (!post(this.mPerformClick)) {
                        performClick();
                        break;
                    }
                } else {
                    if (!this.mTurningOn) {
                        z2 = true;
                    }
                    startAnimation(z2);
                    break;
                }
                break;
            case 2:
                this.mBtnPos = (this.mBtnInitPos + motionEvent.getX()) - this.mFirstDownX;
                if (this.mBtnPos >= this.mBtnOnPos) {
                    this.mBtnPos = this.mBtnOnPos;
                }
                if (this.mBtnPos <= this.mBtnOffPos) {
                    this.mBtnPos = this.mBtnOffPos;
                }
                if (this.mBtnPos <= ((this.mBtnOnPos - this.mBtnOffPos) / 2.0f) + this.mBtnOffPos) {
                    z = false;
                }
                this.mTurningOn = z;
                this.mRealPos = getRealPos(this.mBtnPos);
                break;
        }
        invalidate();
        return isEnabled();
    }

    public boolean performClick() {
        if (this.bIsCheckBox) {
            return super.performClick();
        }
        startAnimation(this.mChecked);
        return true;
    }

    public void setChecked(boolean z) {
        if (this.mChecked != z) {
            this.mChecked = z;
            if (!this.bIsCheckBox) {
                this.mBtnPos = z ? this.mBtnOnPos : this.mBtnOffPos;
                this.mRealPos = getRealPos(this.mBtnPos);
            }
            invalidate();
        }
    }

    public void setCondition(boolean z) {
        this.mIsConditionSwitch = z;
    }

    public void setEnabled(boolean z) {
        if (z) {
            setFocusable(true);
        } else {
            setFocusable(false);
        }
        this.mAlpha = z ? 255 : 127;
        super.setEnabled(z);
    }

    public void setIconName(String[] strArr) {
        if (this.bIsCheckBox) {
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
            return;
        }
        this.drawable_Bottom = this.ui.getDrawableFromPath(strArr[0]);
        this.drawable_Normal = this.ui.getDrawableFromPath(strArr[1]);
        this.drawable_Frame = this.ui.getDrawableFromPath(strArr[2]);
        this.drawable_Mask = this.ui.getDrawableFromPath(strArr[3]);
        if (strArr.length > 4) {
            this.drawable_Bk = this.ui.getDrawableFromPath(strArr[4]);
        }
    }

    public void setPressed(boolean z) {
        super.setPressed(z);
        if (this.bIsCheckBox) {
            updateBackground();
        }
    }

    public void setStrDrawable(String str, boolean z) {
        this.iState = -1;
        this.mStrDrawable = str;
        if (!z && !isFocused()) {
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
        if (this.bStateClickDown || isPressed() || (this.bFocusAble && isFocused())) {
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
