package com.syu.bt.ctrl;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.GravityCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.syu.app.App;
import com.syu.data.FinalTpms;

@SuppressLint({"ClickableViewAccessibility"})
public class SwipeCallingView extends RelativeLayout {
    static boolean isInCycle = false;
    private ImageView DialView;
    private ImageView DialViewMirror;
    View GreenView = null;
    final int ID_DIAL = 2002;
    final int ID_GREEN = 2001;
    final int ID_RED = 2000;
    int MAXHEIGHT = 0;
    int MAXWITH = 0;
    View RedView = null;
    int X = 0;
    int Y = 0;
    TranslateAnimation anim;
    AnimationSet animSet;
    AnimationSet animSet2;
    Context context;
    int duration = FinalTpms.U_CNT_MAX;
    boolean isActiveCall = false;
    boolean isCalling = false;
    private ImageView ivHangup;
    private ImageView ivPickup;
    int lastX = 0;
    int lastY = 0;
    int leftwitdh = 0;
    setEvent mEventListner = null;
    Paint mPaint;
    int offsetDialIcon = 20;
    int posRed = 0;
    int red_h;
    int red_w;
    int rightwitdh = 0;
    WindowManager.LayoutParams wmLp;
    WindowManager wmManager;

    public interface setEvent {
        void setEventLeft();

        void setEventRight();
    }

    public SwipeCallingView(Context context2) {
        super(context2);
        init(context2);
        this.context = context2;
    }

    public SwipeCallingView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        init(context2);
    }

    public SwipeCallingView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        init(context2);
    }

    @TargetApi(16)
    private void init(Context context2) {
        this.wmManager = (WindowManager) context2.getSystemService("window");
        this.wmLp = new WindowManager.LayoutParams();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.RedView = new ImageView(context2);
        this.RedView.setBackgroundResource(App.getApp().getIdDrawable("red_buble"));
        this.RedView.setVisibility(4);
        layoutParams.addRule(15, -1);
        layoutParams.addRule(7, 2002);
        this.RedView.setId(2000);
        addView(this.RedView, layoutParams);
        this.GreenView = new ImageView(context2);
        this.GreenView.setVisibility(4);
        this.GreenView.setBackgroundResource(App.getApp().getIdDrawable("green_buble"));
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams2.addRule(15, -1);
        layoutParams2.addRule(5, 2002);
        this.GreenView.setId(2001);
        addView(this.GreenView, layoutParams2);
        this.DialView = new ImageView(context2);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams3.addRule(13, -1);
        this.DialView.setBackgroundResource(App.getApp().getIdDrawable("bt_calling_dial"));
        this.DialView.setId(2002);
        addView(this.DialView, layoutParams3);
        this.ivPickup = new ImageView(context2);
        this.ivPickup.setBackgroundResource(App.getApp().getIdDrawable("bt_calling_pickup"));
        addView(this.ivPickup);
        this.ivHangup = new ImageView(context2);
        this.ivHangup.setBackgroundResource(App.getApp().getIdDrawable("bt_calling_hang"));
        addView(this.ivHangup);
        this.ivPickup.setVisibility(4);
        this.ivHangup.setVisibility(4);
        this.anim = new TranslateAnimation(0.0f, -350.0f, 0.0f, 0.0f);
        this.anim.setDuration((long) this.duration);
        this.anim.setRepeatMode(1);
        this.anim.setRepeatCount(-1);
        this.anim.setInterpolator(new AccelerateDecelerateInterpolator());
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 350.0f, 0.0f, 0.0f);
        translateAnimation.setDuration((long) this.duration);
        translateAnimation.setRepeatMode(1);
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 0.8f);
        alphaAnimation.setDuration((long) (this.duration / 2));
        alphaAnimation.setRepeatMode(2);
        alphaAnimation.setRepeatCount(-1);
        this.animSet = new AnimationSet(false);
        this.animSet.addAnimation(this.anim);
        this.animSet.addAnimation(alphaAnimation);
        this.animSet2 = new AnimationSet(false);
        this.animSet2.addAnimation(translateAnimation);
        this.animSet2.addAnimation(alphaAnimation);
        if (this.mEventListner == null) {
            this.mEventListner = new setEvent() {
                public void setEventLeft() {
                }

                public void setEventRight() {
                }
            };
        }
    }

    private void updateDialView() {
        this.wmLp.x = (this.X - (this.DialView.getWidth() / 2)) + ((int) getX());
        this.wmLp.y = this.Y + this.offsetDialIcon;
        if (this.DialViewMirror != null) {
            this.wmManager.updateViewLayout(this.DialViewMirror, this.wmLp);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        this.MAXHEIGHT = getMeasuredHeight();
        this.MAXWITH = getMeasuredWidth();
        this.ivHangup.setX(50.0f);
        this.ivPickup.setX((float) ((this.MAXWITH - this.ivPickup.getWidth()) - 50));
        this.leftwitdh = (int) (this.ivHangup.getX() - this.DialView.getX());
        this.rightwitdh = (int) (this.ivPickup.getX() - this.DialView.getX());
        super.onMeasure(i, i2);
    }

    @SuppressLint({"NewApi"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.X = (int) motionEvent.getX();
        this.Y = (int) motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                this.lastX = (int) motionEvent.getX();
                this.lastY = (int) motionEvent.getY();
                if (this.lastX <= 462 || this.lastX >= 562 || this.lastY <= 42 || this.lastY >= 142) {
                    return true;
                }
                isInCycle = true;
                this.DialView.setVisibility(8);
                this.wmLp.width = -2;
                this.wmLp.height = -2;
                this.wmLp.format = 1;
                this.wmLp.gravity = GravityCompat.START;
                this.DialViewMirror = new ImageView(this.context);
                this.DialViewMirror.setBackgroundResource(App.getApp().getIdDrawable("bt_calling_dial"));
                this.wmManager.addView(this.DialViewMirror, this.wmLp);
                updateDialView();
                startCalling(false);
                invalidate();
                return true;
            case 1:
                if (this.isActiveCall) {
                    this.isCalling = false;
                } else {
                    this.isCalling = true;
                    this.ivPickup.setVisibility(4);
                    this.ivHangup.setVisibility(4);
                }
                startCalling(this.isCalling);
                isInCycle = false;
                this.DialView.setVisibility(0);
                removeDialView();
                return true;
            case 2:
                if (isInCycle) {
                    int i = this.X - this.lastX;
                    this.ivPickup.setVisibility(4);
                    this.ivHangup.setVisibility(4);
                    if (this.DialViewMirror != null) {
                        if (i > 0) {
                            float f = (((float) i) * 1.0f) / ((float) this.rightwitdh);
                            if (((double) f) > 0.2d) {
                                this.DialViewMirror.setAlpha(f);
                                this.DialViewMirror.setBackgroundResource(App.getApp().getIdDrawable("bt_calling_pickup"));
                                if (((double) f) > 0.5d) {
                                    this.ivPickup.setVisibility(0);
                                    if (((double) f) > 0.7d) {
                                        this.mEventListner.setEventRight();
                                        this.isActiveCall = true;
                                        removeDialView();
                                    }
                                }
                            }
                        } else {
                            float f2 = (((float) i) * 1.0f) / ((float) this.leftwitdh);
                            if (((double) f2) > 0.2d) {
                                this.DialViewMirror.setBackgroundResource(App.getApp().getIdDrawable("bt_calling_hang"));
                                this.DialViewMirror.setAlpha(f2);
                                if (((double) f2) > 0.5d) {
                                    this.ivHangup.setVisibility(0);
                                    if (((double) f2) > 0.7d) {
                                        this.isActiveCall = true;
                                        this.mEventListner.setEventLeft();
                                        removeDialView();
                                    }
                                }
                            }
                        }
                    }
                    updateDialView();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void removeDialView() {
        if (this.DialViewMirror != null) {
            this.wmManager.removeView(this.DialViewMirror);
            this.DialViewMirror = null;
        }
    }

    public void setOnSwipeEventListner(setEvent setevent) {
        this.mEventListner = setevent;
    }

    public void startCalling(boolean z) {
        this.isCalling = z;
        if (this.isCalling) {
            this.isActiveCall = false;
            this.RedView.setVisibility(0);
            this.GreenView.setVisibility(0);
            if (this.animSet != null) {
                this.RedView.startAnimation(this.animSet);
            }
            if (this.animSet2 != null) {
                this.GreenView.startAnimation(this.animSet2);
                return;
            }
            return;
        }
        if (this.animSet != null) {
            this.RedView.clearAnimation();
        }
        if (this.animSet2 != null) {
            this.GreenView.clearAnimation();
        }
        this.RedView.setVisibility(4);
        this.GreenView.setVisibility(4);
    }
}
