package com.syu.bt.ctrl;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class JPinBar extends View {
    private float mAngle = 0.0f;
    private float mAngleTarget;
    private long mDrawingTime;

    public JPinBar(Context context) {
        super(context);
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(this.mAngle, (float) ((getWidth() * 253) / 300), (float) ((getHeight() * 28) / 179));
        super.draw(canvas);
        canvas.restore();
        if (this.mAngle != this.mAngleTarget) {
            long drawingTime = getDrawingTime();
            if (this.mDrawingTime == 0) {
                this.mDrawingTime = drawingTime - 17;
            }
            float f = (((float) (drawingTime - this.mDrawingTime)) * 15.0f) / 200.0f;
            this.mDrawingTime = drawingTime;
            if (Math.abs(this.mAngle - this.mAngleTarget) < f) {
                this.mAngle = this.mAngleTarget;
            } else if (this.mAngle > this.mAngleTarget) {
                this.mAngle -= f;
            } else {
                this.mAngle += f;
            }
            invalidate();
            return;
        }
        this.mDrawingTime = 0;
    }

    public void setRun(boolean z) {
        if (z) {
            if (this.mAngleTarget == 0.0f) {
                this.mAngleTarget = 15.0f;
                invalidate();
            }
        } else if (this.mAngleTarget != 0.0f) {
            this.mAngleTarget = 0.0f;
            invalidate();
        }
    }

    public void skipAnim() {
        if (this.mAngle != this.mAngleTarget) {
            this.mAngle = this.mAngleTarget;
            invalidate();
        }
    }
}
