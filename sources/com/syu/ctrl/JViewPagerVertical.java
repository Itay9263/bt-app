package com.syu.ctrl;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JViewPagerVertical extends JViewPager {

    public class DefaultTransformer implements ViewPager.PageTransformer {
        public DefaultTransformer() {
        }

        public void transformPage(View view, float f) {
            float f2 = 0.0f;
            if (0.0f <= f && f <= 1.0f) {
                f2 = 1.0f - f;
            } else if (-1.0f < f && f < 0.0f) {
                f2 = f + 1.0f;
            }
            view.setAlpha(f2);
            view.setTranslationX(((float) view.getWidth()) * (-f));
            view.setTranslationY(((float) view.getHeight()) * f);
        }
    }

    public JViewPagerVertical(Context context) {
        super(context);
        setPageTransformer(false, new DefaultTransformer());
    }

    public JViewPagerVertical(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setPageTransformer(false, new DefaultTransformer());
    }

    public JViewPagerVertical(Context context, JPage jPage) {
        super(context, jPage);
        setPageTransformer(false, new DefaultTransformer());
    }

    private MotionEvent swapTouchEvent(MotionEvent motionEvent) {
        float width = (float) getWidth();
        float height = (float) getHeight();
        motionEvent.setLocation((motionEvent.getY() / height) * width, (motionEvent.getX() / width) * height);
        return motionEvent;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(swapTouchEvent(motionEvent));
        swapTouchEvent(motionEvent);
        return onInterceptTouchEvent;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return super.onTouchEvent(swapTouchEvent(motionEvent));
    }
}
