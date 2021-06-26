package com.syu.bt.ctrl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import com.syu.app.App;
import com.syu.data.FinalSound;
import com.syu.data.FinalTpms;

public class CircleMenuLayout extends ViewGroup {
    private static final int FLINGABLE_VALUE = 300;
    private static final int NOCLICK_VALUE = 3;
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 0.2f;
    private static final float RADIO_PADDING_LAYOUT = 0.083333336f;
    private float RADIO_DEFAULT_CENTERITEM_DIMENSION;
    Drawable bkDrawable;
    CircleMenuView circle;
    /* access modifiers changed from: private */
    public boolean isFling;
    private long mDownTime;
    private AutoFlingRunnable mFlingRunnable;
    private int mFlingableValue;
    private String[] mItemImgs;
    private float mLastX;
    private float mLastY;
    private int mMenuItemCount;
    /* access modifiers changed from: private */
    public OnMenuItemClickListener mOnMenuItemClickListener;
    /* access modifiers changed from: private */
    public OnMenuItemLongClickListener mOnMenuItemLongClickListener;
    private float mPadding;
    private int mRadius;
    /* access modifiers changed from: private */
    public double mStartAngle;
    private float mTmpAngle;
    Drawable pointDrawable;

    private class AutoFlingRunnable implements Runnable {
        private float angelPerSecond;

        public AutoFlingRunnable(float f) {
            this.angelPerSecond = f;
        }

        public void run() {
            if (((int) Math.abs(this.angelPerSecond)) < 20) {
                CircleMenuLayout.this.isFling = false;
                return;
            }
            CircleMenuLayout.this.isFling = true;
            CircleMenuLayout circleMenuLayout = CircleMenuLayout.this;
            circleMenuLayout.mStartAngle = circleMenuLayout.mStartAngle + ((double) (this.angelPerSecond / 30.0f));
            this.angelPerSecond /= 1.0666f;
            CircleMenuLayout.this.postDelayed(this, 30);
            if (CircleMenuLayout.this.circle != null) {
                CircleMenuLayout.this.circle.reDrawPic((double) (((float) CircleMenuLayout.this.mStartAngle) % 360.0f));
            }
        }
    }

    public interface OnMenuItemClickListener {
        void itemClick(View view, int i);
    }

    public interface OnMenuItemLongClickListener {
        void itemLongClick(View view, int i);
    }

    public CircleMenuLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public CircleMenuLayout(Context context, Drawable drawable, Drawable drawable2, String[] strArr) {
        super(context);
        this.RADIO_DEFAULT_CENTERITEM_DIMENSION = 0.33333334f;
        this.mFlingableValue = 300;
        this.mStartAngle = 54.0d;
        if (drawable != null) {
            this.bkDrawable = drawable;
        }
        if (drawable2 != null) {
            this.pointDrawable = drawable2;
        }
        setMenuItemIconsAndTexts(strArr);
        this.circle = new CircleMenuView(context, drawable2);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setGravity(17);
        this.circle.setLayoutParams(bt.a().f()[0] == 1920 ? new LinearLayout.LayoutParams(FinalTpms.U_CNT_MAX, FinalTpms.U_CNT_MAX) : new LinearLayout.LayoutParams(400, 400));
        linearLayout.setId(10010);
        addView(linearLayout, layoutParams);
        linearLayout.addView(this.circle);
    }

    public CircleMenuLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.RADIO_DEFAULT_CENTERITEM_DIMENSION = 0.33333334f;
        this.mFlingableValue = 300;
        this.mStartAngle = 54.0d;
        setPadding(0, 0, 0, 0);
    }

    private void addMenuItems() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        for (int i = 0; i < this.mMenuItemCount; i++) {
            Button button = new Button(getContext());
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setGravity(17);
            button.setBackgroundResource(App.getApp().getIdDrawable(this.mItemImgs[i]));
            final int buttonId = getButtonId(this.mItemImgs[i]);
            button.setId(buttonId);
            linearLayout.setId(buttonId);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (CircleMenuLayout.this.circle != null) {
                        CircleMenuLayout.this.circle.reDrawPicByNum(buttonId);
                    }
                    if (CircleMenuLayout.this.mOnMenuItemClickListener != null) {
                        CircleMenuLayout.this.mOnMenuItemClickListener.itemClick(view, buttonId);
                    }
                }
            });
            button.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    if (CircleMenuLayout.this.circle != null) {
                        CircleMenuLayout.this.circle.reDrawPicByNum(buttonId);
                    }
                    if (CircleMenuLayout.this.mOnMenuItemLongClickListener == null) {
                        return false;
                    }
                    CircleMenuLayout.this.mOnMenuItemLongClickListener.itemLongClick(view, buttonId);
                    return true;
                }
            });
            button.setLayoutParams(bt.a().f()[0] == 1920 ? new LinearLayout.LayoutParams(120, 120) : new LinearLayout.LayoutParams(80, 80));
            addView(linearLayout, layoutParams);
            linearLayout.addView(button);
        }
    }

    private float getAngle(float f, float f2) {
        double d = ((double) f) - (((double) this.mRadius) / 2.0d);
        double d2 = ((double) f2) - (((double) this.mRadius) / 2.0d);
        return (float) ((Math.asin(d2 / Math.hypot(d, d2)) * 180.0d) / 3.141592653589793d);
    }

    private int getDefaultWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }

    private int getQuadrant(float f, float f2) {
        int i = (int) (f - ((float) (this.mRadius / 2)));
        int i2 = (int) (f2 - ((float) (this.mRadius / 2)));
        return i >= 0 ? i2 >= 0 ? 4 : 1 : i2 >= 0 ? 3 : 2;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        motionEvent.getX();
        motionEvent.getY();
        switch (motionEvent.getAction()) {
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public int getButtonId(String str) {
        if (str.equalsIgnoreCase("bt_dial0")) {
            return 103;
        }
        if (str.equalsIgnoreCase("bt_dial1")) {
            return 104;
        }
        if (str.equalsIgnoreCase("bt_dial2")) {
            return 105;
        }
        if (str.equalsIgnoreCase("bt_dial3")) {
            return 106;
        }
        if (str.equalsIgnoreCase("bt_dial4")) {
            return 107;
        }
        if (str.equalsIgnoreCase("bt_dial5")) {
            return 108;
        }
        if (str.equalsIgnoreCase("bt_dial6")) {
            return 109;
        }
        if (str.equalsIgnoreCase("bt_dial7")) {
            return 110;
        }
        if (str.equalsIgnoreCase("bt_dial8")) {
            return 111;
        }
        if (str.equalsIgnoreCase("bt_dial9")) {
            return 112;
        }
        if (str.equalsIgnoreCase("bt_dialx")) {
            return 113;
        }
        if (str.equalsIgnoreCase("bt_dialj")) {
            return 114;
        }
        if (str.equalsIgnoreCase("bt_dialdial")) {
            return 133;
        }
        return str.equalsIgnoreCase("bt_dialdel") ? 120 : 103;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5 = this.mRadius;
        int childCount = getChildCount();
        int i6 = (int) (((float) i5) * RADIO_DEFAULT_CHILD_DIMENSION);
        float childCount2 = (float) (360 / (getChildCount() - 1));
        double d = this.mStartAngle % 360.0d;
        for (int i7 = 0; i7 < childCount; i7++) {
            View childAt = getChildAt(i7);
            if (childAt.getId() == 10010) {
                if (bt.a().f()[0] == 1920) {
                    childAt.layout(0, 0, FinalTpms.U_CNT_MAX, FinalTpms.U_CNT_MAX);
                } else {
                    childAt.layout(0, 0, 400, 400);
                }
            } else if (childAt.getId() == 120) {
                double d2 = d - 8.0d;
                float f = (((float) i5) / 2.0f) - ((float) (i6 / 2));
                int round = (i5 / 2) + ((int) Math.round((((double) f) * Math.cos(Math.toRadians(d2))) - ((double) (0.5f * ((float) i6)))));
                int round2 = ((int) Math.round((((double) f) * Math.sin(Math.toRadians(d2))) - ((double) (0.5f * ((float) i6))))) + (i5 / 2);
                childAt.layout(round, round2, round + i6, round2 + i6);
                d = d2 + ((double) childCount2);
            } else {
                float f2 = (((float) i5) / 2.0f) - ((float) (i6 / 2));
                int round3 = (i5 / 2) + ((int) Math.round((((double) f2) * Math.cos(Math.toRadians(d))) - ((double) (0.5f * ((float) i6)))));
                int round4 = ((int) Math.round((((double) f2) * Math.sin(Math.toRadians(d))) - ((double) (0.5f * ((float) i6))))) + (i5 / 2);
                childAt.layout(round3, round4, round3 + i6, round4 + i6);
                d += (double) childCount2;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int size = View.MeasureSpec.getSize(i);
        int mode = View.MeasureSpec.getMode(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int mode2 = View.MeasureSpec.getMode(i2);
        if (mode == 1073741824 && mode2 == 1073741824) {
            int min = Math.min(size, size2);
            i4 = min;
            i3 = min;
        } else {
            int suggestedMinimumWidth = getSuggestedMinimumWidth();
            if (suggestedMinimumWidth == 0) {
                suggestedMinimumWidth = getDefaultWidth();
            }
            i4 = getSuggestedMinimumHeight();
            if (i4 == 0) {
                i4 = getDefaultWidth();
            }
            i3 = suggestedMinimumWidth;
        }
        if (this.bkDrawable != null) {
            i3 = this.bkDrawable.getIntrinsicWidth();
            i4 = this.bkDrawable.getIntrinsicHeight();
        }
        setMeasuredDimension(i3, i4);
        this.mRadius = Math.max(getMeasuredWidth(), getMeasuredHeight());
        int childCount = getChildCount();
        int i5 = (int) (((float) this.mRadius) * RADIO_DEFAULT_CHILD_DIMENSION);
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) (((float) this.mRadius) * this.RADIO_DEFAULT_CENTERITEM_DIMENSION), FinalSound.Face.ALL);
                childAt.measure(makeMeasureSpec, makeMeasureSpec);
            }
        }
        this.mPadding = RADIO_PADDING_LAYOUT * ((float) this.mRadius);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public void setCircleAngle(int i) {
        if (this.circle != null) {
            this.circle.reDrawPicByNum(i);
        }
    }

    public void setFlingableValue(int i) {
        this.mFlingableValue = i;
    }

    public void setMenuItemIconsAndTexts(String[] strArr) {
        this.mItemImgs = strArr;
        if (strArr != null) {
            this.mMenuItemCount = strArr.length;
            addMenuItems();
        }
    }

    public void setMenuItemLayoutId(int i) {
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnMenuItemLongClickListener(OnMenuItemLongClickListener onMenuItemLongClickListener) {
        this.mOnMenuItemLongClickListener = onMenuItemLongClickListener;
    }

    public void setPadding(float f) {
        this.mPadding = f;
    }
}
