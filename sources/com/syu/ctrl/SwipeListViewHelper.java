package com.syu.ctrl;

import android.view.MotionEvent;
import android.view.View;
import com.syu.ipcself.module.main.Main;

public class SwipeListViewHelper {
    JListViewEx listView;
    private View mCurrentItemView;
    private final int mDuration = 100;
    private final int mDurationStep = 10;
    private float mFirstX;
    private float mFirstY;
    private Boolean mIsHorizontal;
    private boolean mIsShown;
    private View mPreItemView;
    private int mRightViewWidth = 0;
    RunnableMove runnableMove;

    public class RunnableMove implements Runnable {
        private boolean bRun = true;
        int fromX;
        int stepX = 0;
        int toX;
        public View view;

        public RunnableMove(View view2, int i, int i2) {
            this.view = view2;
            this.fromX = i;
            this.toX = i2;
            this.stepX = (int) ((((double) ((i2 - i) * 10)) * 1.0d) / 100.0d);
        }

        public void run() {
            if (this.view != null) {
                if (Math.abs(this.toX - this.fromX) < 10) {
                    this.view.scrollTo(this.toX, 0);
                    stopRun(false);
                }
                if (this.bRun && this.view != null) {
                    if (this.view.getScrollX() == this.toX) {
                        stopRun(false);
                        return;
                    }
                    this.fromX += this.stepX;
                    boolean z = (this.stepX > 0 && this.fromX > this.toX) || (this.stepX < 0 && this.fromX < this.toX);
                    if (z) {
                        this.fromX = this.toX;
                    }
                    this.view.scrollTo(this.fromX, 0);
                    SwipeListViewHelper.this.listView.invalidate();
                    if (!z) {
                        Main.postRunnable_Ui(true, this, 10);
                    } else {
                        stopRun(false);
                    }
                }
            }
        }

        public void stopRun(boolean z) {
            this.bRun = false;
            if (z) {
                this.view.setScrollX(0);
            }
        }
    }

    public SwipeListViewHelper(JListViewEx jListViewEx, int i) {
        this.listView = jListViewEx;
        this.mRightViewWidth = i;
    }

    private void clearPressedState() {
        this.mCurrentItemView.setPressed(false);
        this.listView.setPressed(false);
        this.listView.refreshDrawableState();
    }

    private void hiddenRight(View view) {
        if (this.mCurrentItemView != null) {
            if (this.runnableMove != null) {
                this.runnableMove.stopRun(true);
                this.runnableMove = null;
            }
            this.runnableMove = new RunnableMove(view, view.getScrollX(), 0);
            this.runnableMove.run();
            this.mIsShown = false;
        }
    }

    private boolean isHitCurItemLeft(float f) {
        return f < ((float) (this.listView.getWidth() - this.mRightViewWidth));
    }

    private boolean judgeScrollDirection(float f, float f2) {
        if (Math.abs(f) > 30.0f && Math.abs(f) > Math.abs(f2) * 2.0f) {
            this.mIsHorizontal = true;
            return true;
        } else if (Math.abs(f2) <= 30.0f || Math.abs(f2) <= Math.abs(f) * 2.0f) {
            return false;
        } else {
            this.mIsHorizontal = false;
            return true;
        }
    }

    private void showRight(View view) {
        if (this.runnableMove != null) {
            this.runnableMove.stopRun(true);
            this.runnableMove = null;
        }
        if (view.getId() != this.listView.idPageRowGroup) {
            this.runnableMove = new RunnableMove(view, view.getScrollX(), this.mRightViewWidth);
            this.runnableMove.run();
        }
        this.mIsShown = true;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                this.mIsHorizontal = null;
                this.mFirstX = x;
                this.mFirstY = y;
                int pointToPosition = this.listView.pointToPosition((int) this.mFirstX, (int) this.mFirstY);
                if (pointToPosition >= 0) {
                    View childAt = this.listView.getChildAt(pointToPosition - this.listView.getFirstVisiblePosition());
                    this.mPreItemView = this.mCurrentItemView;
                    this.mCurrentItemView = childAt;
                    break;
                }
                break;
            case 1:
            case 3:
                if (this.mIsShown && (this.mPreItemView != this.mCurrentItemView || isHitCurItemLeft(x))) {
                    hiddenRight(this.mPreItemView);
                    break;
                }
            case 2:
                return Math.abs(x - this.mFirstX) >= 5.0f && Math.abs(y - this.mFirstY) >= 5.0f;
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 1:
            case 3:
                clearPressedState();
                if (this.mIsShown) {
                    hiddenRight(this.mPreItemView);
                }
                if (this.mIsHorizontal != null && this.mIsHorizontal.booleanValue()) {
                    if (this.mFirstX - x > ((float) (this.mRightViewWidth / 2))) {
                        showRight(this.mCurrentItemView);
                    } else {
                        hiddenRight(this.mCurrentItemView);
                    }
                    motionEvent.setAction(3);
                    this.listView.superTouchEvent(motionEvent);
                    return true;
                }
            case 2:
                float f = x - this.mFirstX;
                float f2 = y - this.mFirstY;
                if (this.mIsHorizontal != null || judgeScrollDirection(f, f2)) {
                    if (!this.mIsHorizontal.booleanValue()) {
                        if (this.mIsShown) {
                            hiddenRight(this.mPreItemView);
                            break;
                        }
                    } else {
                        if (this.mIsShown && this.mPreItemView != this.mCurrentItemView) {
                            hiddenRight(this.mPreItemView);
                        }
                        if (this.mIsShown && this.mPreItemView == this.mCurrentItemView) {
                            f -= (float) this.mRightViewWidth;
                        }
                        if (this.mCurrentItemView.getId() != this.listView.idPageRowGroup && f < 0.0f && f > ((float) (-this.mRightViewWidth))) {
                            this.mCurrentItemView.scrollTo((int) (-f), 0);
                        }
                        return true;
                    }
                }
                break;
        }
        return false;
    }
}
