package com.syu.bt.ctrl;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.View;
import com.syu.app.MyUi;
import com.syu.app.MyUiItem;
import com.syu.util.IpcUtil;
import java.util.Calendar;
import java.util.TimeZone;

public class JClockView extends View {
    int centerX;
    int centerY;
    BitmapDrawable mDrawableDial;
    BitmapDrawable mDrawableHour;
    BitmapDrawable mDrawableMin;
    BitmapDrawable mDrawableSec;
    int mHeigh;
    Paint mPaint;
    int mTempHeigh;
    int mTempWidth;
    int mWidth;
    private String sTimeZoneString;
    Handler tickHandler;
    /* access modifiers changed from: private */
    public Runnable tickRunnable;
    MyUi ui;

    public JClockView(Context context, MyUi myUi) {
        this(context, "GMT+8：00");
        this.ui = myUi;
    }

    public JClockView(Context context, String str) {
        super(context);
        this.tickRunnable = new Runnable() {
            public void run() {
                JClockView.this.postInvalidate();
                JClockView.this.tickHandler.postDelayed(JClockView.this.tickRunnable, 1000);
            }
        };
        this.sTimeZoneString = str;
        this.mPaint = new Paint();
        this.mPaint.setColor(-16776961);
        run();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawableDial != null) {
            Calendar instance = Calendar.getInstance(TimeZone.getTimeZone(this.sTimeZoneString));
            int i = instance.get(10);
            int i2 = instance.get(12);
            this.mDrawableDial.setBounds(this.centerX - (this.mWidth / 2), this.centerY - (this.mHeigh / 2), this.centerX + (this.mWidth / 2), this.centerY + (this.mHeigh / 2));
            this.mDrawableDial.draw(canvas);
            this.mTempWidth = this.mDrawableHour.getIntrinsicWidth();
            this.mTempHeigh = this.mDrawableHour.getIntrinsicHeight();
            canvas.save();
            canvas.rotate((((float) i) * 30.0f) + ((((float) i2) / 60.0f) * 30.0f), (float) this.centerX, (float) this.centerY);
            this.mDrawableHour.setBounds(this.centerX - (this.mTempWidth / 2), this.centerY - (this.mTempHeigh / 2), this.centerX + (this.mTempWidth / 2), this.centerY + (this.mTempHeigh / 2));
            this.mDrawableHour.draw(canvas);
            canvas.restore();
            this.mTempWidth = this.mDrawableMin.getIntrinsicWidth();
            this.mTempHeigh = this.mDrawableMin.getIntrinsicHeight();
            canvas.save();
            canvas.rotate(((float) i2) * 6.0f, (float) this.centerX, (float) this.centerY);
            this.mDrawableMin.setBounds(this.centerX - (this.mTempWidth / 2), this.centerY - (this.mTempHeigh / 2), this.centerX + (this.mTempWidth / 2), this.centerY + (this.mTempHeigh / 2));
            this.mDrawableMin.draw(canvas);
            canvas.restore();
            this.mTempWidth = this.mDrawableSec.getIntrinsicWidth();
            this.mTempHeigh = this.mDrawableSec.getIntrinsicHeight();
            canvas.rotate(((float) instance.get(13)) * 6.0f, (float) this.centerX, (float) this.centerY);
            this.mDrawableSec.setBounds(this.centerX - (this.mTempWidth / 2), this.centerY - (this.mTempHeigh / 2), this.centerX + (this.mTempWidth / 2), this.centerY + (this.mTempHeigh / 2));
            this.mDrawableSec.draw(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        MyUiItem myUiItem;
        if (this.mDrawableDial == null && (myUiItem = (MyUiItem) getTag()) != null) {
            String[] strDrawableExtra = myUiItem.getStrDrawableExtra();
            if (IpcUtil.strsOk(strDrawableExtra, 4)) {
                this.mDrawableHour = (BitmapDrawable) this.ui.getDrawableFromPath(strDrawableExtra[0]);
                this.mDrawableMin = (BitmapDrawable) this.ui.getDrawableFromPath(strDrawableExtra[1]);
                this.mDrawableSec = (BitmapDrawable) this.ui.getDrawableFromPath(strDrawableExtra[2]);
                this.mDrawableDial = (BitmapDrawable) this.ui.getDrawableFromPath(strDrawableExtra[3]);
                this.mWidth = this.mDrawableDial.getIntrinsicWidth();
                this.mHeigh = this.mDrawableDial.getIntrinsicHeight();
                this.centerX = this.mWidth / 2;
                this.centerY = this.mHeigh / 2;
            }
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void run() {
        this.tickHandler = new Handler();
        this.tickHandler.post(this.tickRunnable);
    }
}
