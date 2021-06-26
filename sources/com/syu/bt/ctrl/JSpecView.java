package com.syu.bt.ctrl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.syu.app.App;
import java.util.Timer;
import java.util.TimerTask;

public class JSpecView extends View {
    /* access modifiers changed from: private */
    public float data;
    private int iStyle = 0;
    private Bitmap mBitmap = null;
    private Canvas mCanvas = null;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 0) {
                JSpecView.this.invalidate();
            }
            super.handleMessage(message);
        }
    };
    private Rect mRect = null;
    private BarRenderer mRenderer = null;
    private Timer mTimer = null;
    private TimerTask readElec = null;

    class ElectricReader extends TimerTask {
        ElectricReader() {
        }

        public void run() {
            JSpecView.this.data = App.getApp().mIpcObj.getElectricValue() / 36.0f;
            JSpecView jSpecView = JSpecView.this;
            jSpecView.data = jSpecView.data - ((float) ((int) JSpecView.this.data));
            JSpecView.this.mHandler.sendEmptyMessage(0);
        }
    }

    public JSpecView(Context context, float f, int i, int i2, int i3, Bitmap bitmap, Bitmap bitmap2, int i4, int i5) {
        super(context);
        init(f, i, i2, i3, bitmap, bitmap2, i4);
        this.iStyle = i5;
    }

    private void init(float f, int i, int i2, int i3, Bitmap bitmap, Bitmap bitmap2, int i4) {
        setElecRenderer(new BarRenderer(f, i, i2, i3, true, false, true, bitmap, bitmap2, i4));
        enable();
    }

    public void disEnable() {
        if (this.mTimer != null && this.readElec != null) {
            this.mTimer.cancel();
            this.readElec.cancel();
            this.readElec = null;
            this.mTimer = null;
        }
    }

    public void enable() {
        if (this.mTimer == null) {
            this.mTimer = new Timer();
        }
        if (this.readElec == null) {
            this.readElec = new ElectricReader();
            this.mTimer.schedule(this.readElec, 10, 100);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mBitmap != null) {
            if (this.mRenderer != null) {
                this.mRenderer.render(this.mCanvas, this.data, this.mRect, this.iStyle);
            }
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i > 0 && i2 > 0) {
            super.onSizeChanged(i, i2, i3, i4);
            this.mRect = new Rect(0, 0, getWidth(), getHeight());
            this.mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            this.mCanvas = new Canvas(this.mBitmap);
        }
    }

    public void release() {
        disEnable();
    }

    public void setElecRenderer(BarRenderer barRenderer) {
        this.mRenderer = barRenderer;
    }
}
