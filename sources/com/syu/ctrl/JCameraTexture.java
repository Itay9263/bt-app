package com.syu.ctrl;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class JCameraTexture extends SurfaceView implements SurfaceHolder.Callback {
    public SurfaceHolder mHolder;

    public JCameraTexture(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public JCameraTexture(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().addCallback(this);
    }

    public JCameraTexture(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        getHolder().addCallback(this);
    }

    public SurfaceHolder getSurfaceHolder() {
        return this.mHolder;
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        this.mHolder = surfaceHolder;
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mHolder = surfaceHolder;
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mHolder = null;
    }
}
