package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;

class ProcessAndDisplayImageTask implements Runnable {
    private final Bitmap bitmap;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    private final ImageLoadingInfo imageLoadingInfo;
    private int mKeyPath;

    public ProcessAndDisplayImageTask(ImageLoaderEngine imageLoaderEngine, Bitmap bitmap2, ImageLoadingInfo imageLoadingInfo2, Handler handler2, int i) {
        this.engine = imageLoaderEngine;
        this.bitmap = bitmap2;
        this.imageLoadingInfo = imageLoadingInfo2;
        this.handler = handler2;
        this.mKeyPath = i;
    }

    public void run() {
        LoadAndDisplayImageTask.runTask(new DisplayBitmapTask(this.imageLoadingInfo.options.getPostProcessor().process(this.bitmap), this.imageLoadingInfo, this.engine, LoadedFrom.MEMORY_CACHE, this.mKeyPath), this.imageLoadingInfo.options.isSyncLoading(), this.handler, this.engine);
    }
}
