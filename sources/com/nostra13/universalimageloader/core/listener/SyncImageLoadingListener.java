package com.nostra13.universalimageloader.core.listener;

import android.graphics.Bitmap;
import android.view.View;

public class SyncImageLoadingListener extends SimpleImageLoadingListener {
    private Bitmap loadedImage;

    public Bitmap getLoadedBitmap() {
        return this.loadedImage;
    }

    public void onLoadingComplete(String str, View view, Bitmap bitmap) {
        this.loadedImage = bitmap;
    }
}
