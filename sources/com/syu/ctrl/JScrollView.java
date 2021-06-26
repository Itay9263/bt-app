package com.syu.ctrl;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class JScrollView extends ScrollView {
    private LinearLayout layout;
    private GestureDetector mGestureDetector;

    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        YScrollDetector() {
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return Math.abs(f2) >= Math.abs(f);
        }
    }

    public JScrollView(Context context) {
        super(context);
        this.mGestureDetector = new GestureDetector(context, new YScrollDetector());
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
