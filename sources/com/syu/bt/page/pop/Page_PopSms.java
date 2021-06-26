package com.syu.bt.page.pop;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import com.syu.app.App;
import com.syu.ctrl.JPage;
import com.syu.ctrl.JText;
import com.syu.page.Page;

public class Page_PopSms extends Page {
    JText vInfo;
    JText vNumber;
    JText vTime;
    ValueAnimator value;

    public Page_PopSms(JPage jPage) {
        super(jPage);
    }

    public void ResponseClick(View view) {
        if (this.value != null) {
            this.value.cancel();
        }
        switch (view.getId()) {
        }
    }

    public void init() {
        super.init();
        this.vTime = (JText) getPage().getChildViewByid(73);
        this.vNumber = (JText) getPage().getChildViewByid(72);
        this.vInfo = (JText) getPage().getChildViewByid(74);
        if (this.vTime != null) {
            this.vTime.setTypeface(bt.a((Context) App.getApp()));
        }
        if (this.vNumber != null) {
            this.vNumber.setTypeface(bt.a((Context) App.getApp()));
        }
        if (this.vInfo != null) {
            this.vInfo.setTypeface(bt.a((Context) App.getApp()));
        }
    }

    public void updateSms(String str, String str2) {
        if (this.vTime != null) {
            if (this.value == null) {
                this.value = ValueAnimator.ofInt(new int[]{1050, 0});
                this.value.setInterpolator(new TimeInterpolator() {
                    public float getInterpolation(float f) {
                        return f;
                    }
                });
                this.value.setDuration(10000);
                this.value.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                        Page_PopSms.this.vTime.setText(String.valueOf(intValue / 100) + "s");
                        if (intValue < 70) {
                            App.getApp().popPhoneSMS(false);
                        }
                    }
                });
            }
            this.value.cancel();
            this.value.start();
        }
        if (this.vNumber != null) {
            this.vNumber.setText(str2);
        }
        if (this.vInfo != null) {
            this.vInfo.setText(str2);
        }
    }
}
