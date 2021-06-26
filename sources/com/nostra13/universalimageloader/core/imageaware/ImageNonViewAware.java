package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;

public class ImageNonViewAware implements ImageAware {
    protected final ImageSize imageSize;
    protected final String imageUri;
    protected final ViewScaleType scaleType;

    public ImageNonViewAware(ImageSize imageSize2, ViewScaleType viewScaleType) {
        this((String) null, imageSize2, viewScaleType);
    }

    public ImageNonViewAware(String str, ImageSize imageSize2, ViewScaleType viewScaleType) {
        this.imageUri = str;
        this.imageSize = imageSize2;
        this.scaleType = viewScaleType;
    }

    public int getHeight() {
        return this.imageSize.getHeight();
    }

    public int getId() {
        return TextUtils.isEmpty(this.imageUri) ? super.hashCode() : this.imageUri.hashCode();
    }

    public ViewScaleType getScaleType() {
        return this.scaleType;
    }

    public Object getTag() {
        return null;
    }

    public Object getTag(int i) {
        return null;
    }

    public int getWidth() {
        return this.imageSize.getWidth();
    }

    public View getWrappedView() {
        return null;
    }

    public boolean isCollected() {
        return false;
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        return true;
    }

    public boolean setImageDrawable(Drawable drawable) {
        return true;
    }
}
