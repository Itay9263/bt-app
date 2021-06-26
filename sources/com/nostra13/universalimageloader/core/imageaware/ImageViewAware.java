package com.nostra13.universalimageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class ImageViewAware implements ImageAware {
    public static final String WARN_CANT_SET_BITMAP = "Can't set a bitmap into view. You should call ImageLoader on UI thread for it.";
    public static final String WARN_CANT_SET_DRAWABLE = "Can't set a drawable into view. You should call ImageLoader on UI thread for it.";
    protected boolean checkActualViewSize;
    protected Reference<ImageView> imageViewRef;

    public ImageViewAware(ImageView imageView) {
        this(imageView, true);
    }

    public ImageViewAware(ImageView imageView, boolean z) {
        this.imageViewRef = new WeakReference(imageView);
        this.checkActualViewSize = z;
    }

    private static int getImageViewFieldValue(Object obj, String str) {
        try {
            Field declaredField = ImageView.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            int intValue = ((Integer) declaredField.get(obj)).intValue();
            if (intValue <= 0 || intValue >= Integer.MAX_VALUE) {
                return 0;
            }
            return intValue;
        } catch (Exception e) {
            return 0;
        }
    }

    public int getHeight() {
        int i = 0;
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (!(!this.checkActualViewSize || layoutParams == null || layoutParams.height == -2)) {
            i = imageView.getHeight();
        }
        if (i <= 0 && layoutParams != null) {
            i = layoutParams.height;
        }
        return i <= 0 ? getImageViewFieldValue(imageView, "mMaxHeight") : i;
    }

    public int getId() {
        ImageView imageView = this.imageViewRef.get();
        return imageView == null ? super.hashCode() : imageView.hashCode();
    }

    public ViewScaleType getScaleType() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return null;
    }

    public Object getTag() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView != null) {
            return imageView.getTag();
        }
        return null;
    }

    public Object getTag(int i) {
        ImageView imageView = this.imageViewRef.get();
        if (imageView != null) {
            return imageView.getTag(i);
        }
        return null;
    }

    public int getWidth() {
        int i = 0;
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (!(!this.checkActualViewSize || layoutParams == null || layoutParams.width == -2)) {
            i = imageView.getWidth();
        }
        if (i <= 0 && layoutParams != null) {
            i = layoutParams.width;
        }
        return i <= 0 ? getImageViewFieldValue(imageView, "mMaxWidth") : i;
    }

    public ImageView getWrappedView() {
        return this.imageViewRef.get();
    }

    public boolean isCollected() {
        return this.imageViewRef.get() == null;
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        ImageView imageView;
        if (Looper.myLooper() != Looper.getMainLooper() || (imageView = this.imageViewRef.get()) == null) {
            return false;
        }
        imageView.setImageBitmap(bitmap);
        return true;
    }

    public boolean setImageDrawable(Drawable drawable) {
        ImageView imageView;
        if (Looper.myLooper() != Looper.getMainLooper() || (imageView = this.imageViewRef.get()) == null) {
            return false;
        }
        imageView.setImageDrawable(drawable);
        return true;
    }
}
