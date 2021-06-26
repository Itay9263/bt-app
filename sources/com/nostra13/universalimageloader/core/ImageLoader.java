package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageNonViewAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SyncImageLoadingListener;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import java.io.File;

public class ImageLoader {
    private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader configuration can not be initialized with null";
    private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
    private static final String ERROR_WRONG_ARGUMENTS = "Wrong arguments were passed to displayImage() method (ImageView reference must not be null)";
    static final String LOG_DESTROY = "Destroy ImageLoader";
    static final String LOG_INIT_CONFIG = "Initialize ImageLoader with configuration";
    static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";
    public static final String TAG = ImageLoader.class.getSimpleName();
    private static volatile ImageLoader instance;
    private ImageLoaderConfiguration configuration;
    private final ImageLoadingListener emptyListener = new SimpleImageLoadingListener();
    private ImageLoaderEngine engine;

    protected ImageLoader() {
    }

    private void checkConfiguration() {
        if (this.configuration == null) {
            throw new IllegalStateException(ERROR_NOT_INIT);
        }
    }

    private static Handler defineHandler(DisplayImageOptions displayImageOptions) {
        Handler handler = displayImageOptions.getHandler();
        if (displayImageOptions.isSyncLoading()) {
            return null;
        }
        return (handler == null && Looper.myLooper() == Looper.getMainLooper()) ? new Handler() : handler;
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    public void cancelDisplayTask(ImageView imageView) {
        this.engine.cancelDisplayTaskFor(new ImageViewAware(imageView));
    }

    public void cancelDisplayTask(ImageAware imageAware) {
        this.engine.cancelDisplayTaskFor(imageAware);
    }

    public void clearMemoryCache() {
        checkConfiguration();
        this.configuration.memoryCache.clear();
    }

    public void denyNetworkDownloads(boolean z) {
        this.engine.denyNetworkDownloads(z);
    }

    public void destroy() {
        stop();
        this.engine = null;
        this.configuration = null;
    }

    public void displayImage(String str, ImageView imageView, int i) {
        displayImage(str, (ImageAware) new ImageViewAware(imageView), (DisplayImageOptions) null, (ImageLoadingListener) null, (ImageLoadingProgressListener) null, i);
    }

    public void displayImage(String str, ImageView imageView, DisplayImageOptions displayImageOptions, int i) {
        displayImage(str, (ImageAware) new ImageViewAware(imageView), displayImageOptions, (ImageLoadingListener) null, (ImageLoadingProgressListener) null, i);
    }

    public void displayImage(String str, ImageView imageView, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener, int i) {
        displayImage(str, imageView, displayImageOptions, imageLoadingListener, (ImageLoadingProgressListener) null, i);
    }

    public void displayImage(String str, ImageView imageView, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener, ImageLoadingProgressListener imageLoadingProgressListener, int i) {
        displayImage(str, (ImageAware) new ImageViewAware(imageView), displayImageOptions, imageLoadingListener, imageLoadingProgressListener, i);
    }

    public void displayImage(String str, ImageView imageView, ImageLoadingListener imageLoadingListener, int i) {
        displayImage(str, (ImageAware) new ImageViewAware(imageView), (DisplayImageOptions) null, imageLoadingListener, (ImageLoadingProgressListener) null, i);
    }

    public void displayImage(String str, ImageAware imageAware, int i) {
        displayImage(str, imageAware, (DisplayImageOptions) null, (ImageLoadingListener) null, (ImageLoadingProgressListener) null, i);
    }

    public void displayImage(String str, ImageAware imageAware, DisplayImageOptions displayImageOptions, int i) {
        displayImage(str, imageAware, displayImageOptions, (ImageLoadingListener) null, (ImageLoadingProgressListener) null, i);
    }

    public void displayImage(String str, ImageAware imageAware, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener, int i) {
        displayImage(str, imageAware, displayImageOptions, imageLoadingListener, (ImageLoadingProgressListener) null, i);
    }

    public void displayImage(String str, ImageAware imageAware, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener, ImageLoadingProgressListener imageLoadingProgressListener, int i) {
        checkConfiguration();
        if (imageAware == null) {
            throw new IllegalArgumentException(ERROR_WRONG_ARGUMENTS);
        }
        ImageLoadingListener imageLoadingListener2 = imageLoadingListener == null ? this.emptyListener : imageLoadingListener;
        DisplayImageOptions displayImageOptions2 = displayImageOptions == null ? this.configuration.defaultDisplayImageOptions : displayImageOptions;
        if (TextUtils.isEmpty(str)) {
            this.engine.cancelDisplayTaskFor(imageAware);
            imageLoadingListener2.onLoadingStarted(str, imageAware.getWrappedView());
            if (("file://" + ((String) imageAware.getTag(i))).equals(str)) {
                if (displayImageOptions2.shouldShowImageForEmptyUri()) {
                    imageAware.setImageDrawable(displayImageOptions2.getImageForEmptyUri(this.configuration.resources));
                } else {
                    imageAware.setImageDrawable((Drawable) null);
                }
                imageLoadingListener2.onLoadingComplete(str, imageAware.getWrappedView(), (Bitmap) null);
                return;
            }
            return;
        }
        File file = new File((String) imageAware.getTag(i));
        if (file.exists()) {
            long lastModified = file.lastModified();
            ImageSize defineTargetSizeForView = ImageSizeUtils.defineTargetSizeForView(imageAware, this.configuration.getMaxImageSize());
            String generateKey = MemoryCacheUtils.generateKey(String.valueOf(str) + lastModified, defineTargetSizeForView);
            this.engine.prepareDisplayTaskFor(imageAware, generateKey);
            imageLoadingListener2.onLoadingStarted(str, imageAware.getWrappedView());
            Bitmap bitmap = this.configuration.memoryCache.get(generateKey);
            if (bitmap == null || bitmap.isRecycled()) {
                if (("file://" + ((String) imageAware.getTag(i))).equals(str)) {
                    if (displayImageOptions2.shouldShowImageOnLoading()) {
                        imageAware.setImageDrawable(displayImageOptions2.getImageOnLoading(this.configuration.resources));
                    } else if (displayImageOptions2.isResetViewBeforeLoading()) {
                        imageAware.setImageDrawable((Drawable) null);
                    }
                    LoadAndDisplayImageTask loadAndDisplayImageTask = new LoadAndDisplayImageTask(this.engine, new ImageLoadingInfo(str, imageAware, defineTargetSizeForView, generateKey, displayImageOptions2, imageLoadingListener2, imageLoadingProgressListener, this.engine.getLockForUri(str)), defineHandler(displayImageOptions2), i);
                    if (displayImageOptions2.isSyncLoading()) {
                        loadAndDisplayImageTask.run();
                    } else {
                        this.engine.submit(loadAndDisplayImageTask);
                    }
                }
            } else if (displayImageOptions2.shouldPostProcess()) {
                ProcessAndDisplayImageTask processAndDisplayImageTask = new ProcessAndDisplayImageTask(this.engine, bitmap, new ImageLoadingInfo(str, imageAware, defineTargetSizeForView, generateKey, displayImageOptions2, imageLoadingListener2, imageLoadingProgressListener, this.engine.getLockForUri(str)), defineHandler(displayImageOptions2), i);
                if (displayImageOptions2.isSyncLoading()) {
                    processAndDisplayImageTask.run();
                } else {
                    this.engine.submit(processAndDisplayImageTask);
                }
            } else {
                displayImageOptions2.getDisplayer().display(bitmap, imageAware, LoadedFrom.MEMORY_CACHE, str, i);
                imageLoadingListener2.onLoadingComplete(str, imageAware.getWrappedView(), bitmap);
            }
        }
    }

    public void displayImage(String str, ImageAware imageAware, ImageLoadingListener imageLoadingListener, int i) {
        displayImage(str, imageAware, (DisplayImageOptions) null, imageLoadingListener, (ImageLoadingProgressListener) null, i);
    }

    public String getLoadingUriForView(ImageView imageView) {
        return this.engine.getLoadingUriForView(new ImageViewAware(imageView));
    }

    public String getLoadingUriForView(ImageAware imageAware) {
        return this.engine.getLoadingUriForView(imageAware);
    }

    public MemoryCacheAware<String, Bitmap> getMemoryCache() {
        checkConfiguration();
        return this.configuration.memoryCache;
    }

    public void handleSlowNetwork(boolean z) {
        this.engine.handleSlowNetwork(z);
    }

    public synchronized void init(ImageLoaderConfiguration imageLoaderConfiguration) {
        if (imageLoaderConfiguration == null) {
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        } else if (this.configuration == null) {
            this.engine = new ImageLoaderEngine(imageLoaderConfiguration);
            this.configuration = imageLoaderConfiguration;
        }
    }

    public boolean isInited() {
        return this.configuration != null;
    }

    public void loadImage(String str, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener, int i) {
        loadImage(str, (ImageSize) null, displayImageOptions, imageLoadingListener, (ImageLoadingProgressListener) null, i);
    }

    public void loadImage(String str, ImageSize imageSize, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener, int i) {
        loadImage(str, imageSize, displayImageOptions, imageLoadingListener, (ImageLoadingProgressListener) null, i);
    }

    public void loadImage(String str, ImageSize imageSize, DisplayImageOptions displayImageOptions, ImageLoadingListener imageLoadingListener, ImageLoadingProgressListener imageLoadingProgressListener, int i) {
        checkConfiguration();
        if (imageSize == null) {
            imageSize = this.configuration.getMaxImageSize();
        }
        displayImage(str, (ImageAware) new ImageNonViewAware(str, imageSize, ViewScaleType.CROP), displayImageOptions == null ? this.configuration.defaultDisplayImageOptions : displayImageOptions, imageLoadingListener, imageLoadingProgressListener, i);
    }

    public void loadImage(String str, ImageSize imageSize, ImageLoadingListener imageLoadingListener, int i) {
        loadImage(str, imageSize, (DisplayImageOptions) null, imageLoadingListener, (ImageLoadingProgressListener) null, i);
    }

    public void loadImage(String str, ImageLoadingListener imageLoadingListener, int i) {
        loadImage(str, (ImageSize) null, (DisplayImageOptions) null, imageLoadingListener, (ImageLoadingProgressListener) null, i);
    }

    public Bitmap loadImageSync(String str, int i) {
        return loadImageSync(str, (ImageSize) null, (DisplayImageOptions) null, i);
    }

    public Bitmap loadImageSync(String str, DisplayImageOptions displayImageOptions, int i) {
        return loadImageSync(str, (ImageSize) null, displayImageOptions, i);
    }

    public Bitmap loadImageSync(String str, ImageSize imageSize, int i) {
        return loadImageSync(str, imageSize, (DisplayImageOptions) null, i);
    }

    public Bitmap loadImageSync(String str, ImageSize imageSize, DisplayImageOptions displayImageOptions, int i) {
        if (displayImageOptions == null) {
            displayImageOptions = this.configuration.defaultDisplayImageOptions;
        }
        DisplayImageOptions build = new DisplayImageOptions.Builder().cloneFrom(displayImageOptions).syncLoading(true).build();
        SyncImageLoadingListener syncImageLoadingListener = new SyncImageLoadingListener();
        loadImage(str, imageSize, build, syncImageLoadingListener, i);
        return syncImageLoadingListener.getLoadedBitmap();
    }

    public void pause() {
        this.engine.pause();
    }

    public void removeFromCache(String str) {
        checkConfiguration();
        MemoryCacheUtils.removeFromCache(str, this.configuration.memoryCache);
    }

    public void resume() {
        this.engine.resume();
    }

    public void stop() {
        this.engine.stop();
    }
}
