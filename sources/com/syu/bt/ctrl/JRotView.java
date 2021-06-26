package com.syu.bt.ctrl;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import com.syu.ipcself.module.main.Main;

public class JRotView extends View {
    private float mAngle;
    public long mDelay;
    private boolean mRun;
    public Runnable mRunnableInvalidate = new Runnable() {
        public void run() {
            JRotView.this.invalidate();
        }
    };
    public float mSpeed = 15.0f;
    private float mStep;

    public JRotView(Context context) {
        super(context);
        if (Main.mConf_PlatForm == 5 || Main.mConf_PlatForm == 6) {
            this.mSpeed = 15.0f;
            this.mDelay = 100;
        }
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(this.mAngle, (float) (getWidth() >> 1), (float) (getHeight() >> 1));
        super.draw(canvas);
        canvas.restore();
        if (this.mRun) {
            if (this.mStep < this.mSpeed) {
                this.mStep += 0.5f;
            }
        } else if (this.mStep > 0.0f) {
            this.mStep -= 0.5f;
            if (((double) Math.abs(this.mStep)) <= 0.05d) {
                this.mStep = 0.0f;
            }
        }
        if (this.mStep != 0.0f) {
            this.mAngle += this.mStep;
            Main.postRunnable_Ui(true, this.mRunnableInvalidate, this.mDelay);
        }
    }

    public void resetStep() {
        this.mStep = 0.0f;
    }

    public void setRun(boolean z) {
        if (this.mRun != z) {
            this.mRun = z;
            if (z) {
                invalidate();
            }
        }
    }

    public void setSpeed(float f) {
        this.mSpeed = f;
    }
}
