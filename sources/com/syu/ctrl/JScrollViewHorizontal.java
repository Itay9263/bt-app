package com.syu.ctrl;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class JScrollViewHorizontal extends HorizontalScrollView {
    private LinearLayout layout;
    private GestureDetector mGestureDetector;

    class XScrollDetector extends GestureDetector.SimpleOnGestureListener {
        XScrollDetector() {
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return Math.abs(f) >= Math.abs(f2);
        }
    }

    public JScrollViewHorizontal(Context context) {
        super(context);
        this.mGestureDetector = new GestureDetector(context, new XScrollDetector());
    }

    public LinearLayout getLayout() {
        return this.layout;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mGestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void setLayout(LinearLayout linearLayout) {
        this.layout = linearLayout;
        addView(linearLayout);
    }
}
