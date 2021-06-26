package com.nostra13.universalimageloader.core.display;

import android.graphics.Bitmap;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class RoundedVignetteBitmapDisplayer extends RoundedBitmapDisplayer {

    protected static class RoundedVignetteDrawable extends RoundedBitmapDisplayer.RoundedDrawable {
        RoundedVignetteDrawable(Bitmap bitmap, int i, int i2) {
            super(bitmap, i, i2);
        }

        /* access modifiers changed from: protected */
        public void onBoundsChange(Rect rect) {
            super.onBoundsChange(rect);
            int[] iArr = new int[3];
            iArr[2] = 2130706432;
            RadialGradient radialGradient = new RadialGradient(this.mRect.centerX(), (this.mRect.centerY() * 1.0f) / 0.7f, this.mRect.centerX() * 1.3f, iArr, new float[]{0.0f, 0.7f, 1.0f}, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();
            matrix.setScale(1.0f, 0.7f);
            radialGradient.setLocalMatrix(matrix);
            this.paint.setShader(new ComposeShader(this.bitmapShader, radialGradient, PorterDuff.Mode.SRC_OVER));
        }
    }

    public RoundedVignetteBitmapDisplayer(int i, int i2) {
        super(i, i2);
    }

    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom, String str, int i) {
        if (!("file://" + ((String) imageAware.getTag(i))).equals(str)) {
            return;
        }
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }
        imageAware.setImageDrawable(new RoundedVignetteDrawable(bitmap, this.cornerRadius, this.margin));
    }
}
