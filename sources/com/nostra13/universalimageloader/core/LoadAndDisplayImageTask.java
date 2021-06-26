package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.IoUtils;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

final class LoadAndDisplayImageTask implements IoUtils.CopyListener, Runnable {
    /* access modifiers changed from: private */
    public final ImageLoaderConfiguration configuration;
    private final ImageDecoder decoder;
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    final ImageAware imageAware;
    private final ImageLoadingInfo imageLoadingInfo;
    final ImageLoadingListener listener;
    private LoadedFrom loadedFrom = LoadedFrom.NETWORK;
    public int mKeyPath;
    private final String memoryCacheKey;
    private final ImageDownloader networkDeniedDownloader;
    final DisplayImageOptions options;
    final ImageLoadingProgressListener progressListener;
    private final ImageDownloader slowNetworkDownloader;
    private final ImageSize targetSize;
    final String uri;

    class TaskCancelledException extends Exception {
        private static final long serialVersionUID = 1;

        TaskCancelledException() {
        }
    }

    public LoadAndDisplayImageTask(ImageLoaderEngine imageLoaderEngine, ImageLoadingInfo imageLoadingInfo2, Handler handler2, int i) {
        this.engine = imageLoaderEngine;
        this.imageLoadingInfo = imageLoadingInfo2;
        this.handler = handler2;
        this.configuration = imageLoaderEngine.configuration;
        this.downloader = this.configuration.downloader;
        this.networkDeniedDownloader = this.configuration.networkDeniedDownloader;
        this.slowNetworkDownloader = this.configuration.slowNetworkDownloader;
        this.decoder = this.configuration.decoder;
        this.uri = imageLoadingInfo2.uri;
        this.mKeyPath = i;
        this.memoryCacheKey = imageLoadingInfo2.memoryCacheKey;
        this.imageAware = imageLoadingInfo2.imageAware;
        this.targetSize = imageLoadingInfo2.targetSize;
        this.options = imageLoadingInfo2.options;
        this.listener = imageLoadingInfo2.listener;
        this.progressListener = imageLoadingInfo2.progressListener;
    }

    private void checkTaskInterrupted() throws TaskCancelledException {
        if (isTaskInterrupted()) {
            throw new TaskCancelledException();
        }
    }

    private void checkTaskNotActual() throws TaskCancelledException {
        checkViewCollected();
        checkViewReused();
    }

    private void checkViewCollected() throws TaskCancelledException {
        if (isViewCollected()) {
            throw new TaskCancelledException();
        }
    }

    private void checkViewReused() throws TaskCancelledException {
        if (isViewReused()) {
            throw new TaskCancelledException();
        }
    }

    private Bitmap decodeImage(String str) throws IOException {
        String str2 = str;
        return this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, str2, this.targetSize, this.imageAware.getScaleType(), getDownloader(), this.options));
    }

    private boolean delayIfNeed() {
        if (!this.options.shouldDelayBeforeLoading()) {
            return false;
        }
        try {
            Thread.sleep((long) this.options.getDelayBeforeLoading());
            return isTaskNotActual();
        } catch (InterruptedException e) {
            return true;
        }
    }

    private void fireCancelEvent() {
        if (!this.options.isSyncLoading() && !isTaskInterrupted()) {
            runTask(new Runnable() {
                public void run() {
                    LoadAndDisplayImageTask.this.listener.onLoadingCancelled(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView());
                }
            }, false, this.handler, this.engine);
        }
    }

    private void fireFailEvent(final FailReason.FailType failType, final Throwable th) {
        if (!this.options.isSyncLoading() && !isTaskInterrupted() && !isTaskNotActual()) {
            runTask(new Runnable() {
                public void run() {
                    if (("file://" + ((String) LoadAndDisplayImageTask.this.imageAware.getTag(LoadAndDisplayImageTask.this.mKeyPath))).equals(LoadAndDisplayImageTask.this.uri)) {
                        if (LoadAndDisplayImageTask.this.options.shouldShowImageOnFail()) {
                            LoadAndDisplayImageTask.this.imageAware.setImageDrawable(LoadAndDisplayImageTask.this.options.getImageOnFail(LoadAndDisplayImageTask.this.configuration.resources));
                        }
                        LoadAndDisplayImageTask.this.listener.onLoadingFailed(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), new FailReason(failType, th));
                    }
                }
            }, false, this.handler, this.engine);
        }
    }

    private boolean fireProgressEvent(final int i, final int i2) {
        if (this.options.isSyncLoading() || isTaskInterrupted() || isTaskNotActual()) {
            return false;
        }
        runTask(new Runnable() {
            public void run() {
                LoadAndDisplayImageTask.this.progressListener.onProgressUpdate(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), i, i2);
            }
        }, false, this.handler, this.engine);
        return true;
    }

    private ImageDownloader getDownloader() {
        return this.engine.isNetworkDenied() ? this.networkDeniedDownloader : this.engine.isSlowNetwork() ? this.slowNetworkDownloader : this.downloader;
    }

    private boolean isTaskInterrupted() {
        return Thread.interrupted();
    }

    private boolean isTaskNotActual() {
        return isViewCollected() || isViewReused();
    }

    private boolean isViewCollected() {
        return this.imageAware.isCollected();
    }

    private boolean isViewReused() {
        return !this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware));
    }

    static void runTask(Runnable runnable, boolean z, Handler handler2, ImageLoaderEngine imageLoaderEngine) {
        if (z) {
            runnable.run();
        } else if (handler2 == null) {
            imageLoaderEngine.fireCallback(runnable);
        } else {
            handler2.post(runnable);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003c, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003d, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0056, code lost:
        r2 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0057, code lost:
        r0 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0059, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005a, code lost:
        r0 = r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x005c, code lost:
        r2 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x005d, code lost:
        r0 = r1;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003c A[ExcHandler: TaskCancelledException (r0v4 'e' com.nostra13.universalimageloader.core.LoadAndDisplayImageTask$TaskCancelledException A[CUSTOM_DECLARE]), Splitter:B:2:0x0004] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.graphics.Bitmap tryLoadBitmap() throws com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.TaskCancelledException {
        /*
            r4 = this;
            r0 = 0
            r1 = 0
            if (r0 == 0) goto L_0x0010
            int r2 = r1.getWidth()     // Catch:{ IllegalStateException -> 0x0033, TaskCancelledException -> 0x003c, IOException -> 0x003e, OutOfMemoryError -> 0x0046, Throwable -> 0x004e }
            if (r2 <= 0) goto L_0x0010
            int r1 = r1.getHeight()     // Catch:{ IllegalStateException -> 0x0033, TaskCancelledException -> 0x003c, IOException -> 0x003e, OutOfMemoryError -> 0x0046, Throwable -> 0x004e }
            if (r1 > 0) goto L_0x0032
        L_0x0010:
            com.nostra13.universalimageloader.core.assist.LoadedFrom r1 = com.nostra13.universalimageloader.core.assist.LoadedFrom.NETWORK     // Catch:{ IllegalStateException -> 0x0033, TaskCancelledException -> 0x003c, IOException -> 0x003e, OutOfMemoryError -> 0x0046, Throwable -> 0x004e }
            r4.loadedFrom = r1     // Catch:{ IllegalStateException -> 0x0033, TaskCancelledException -> 0x003c, IOException -> 0x003e, OutOfMemoryError -> 0x0046, Throwable -> 0x004e }
            java.lang.String r1 = r4.uri     // Catch:{ IllegalStateException -> 0x0033, TaskCancelledException -> 0x003c, IOException -> 0x003e, OutOfMemoryError -> 0x0046, Throwable -> 0x004e }
            r4.checkTaskNotActual()     // Catch:{ IllegalStateException -> 0x0033, TaskCancelledException -> 0x003c, IOException -> 0x003e, OutOfMemoryError -> 0x0046, Throwable -> 0x004e }
            android.graphics.Bitmap r1 = r4.decodeImage(r1)     // Catch:{ IllegalStateException -> 0x0033, TaskCancelledException -> 0x003c, IOException -> 0x003e, OutOfMemoryError -> 0x0046, Throwable -> 0x004e }
            if (r1 == 0) goto L_0x002b
            int r2 = r1.getWidth()     // Catch:{ IllegalStateException -> 0x005f, TaskCancelledException -> 0x003c, IOException -> 0x005c, OutOfMemoryError -> 0x0059, Throwable -> 0x0056 }
            if (r2 <= 0) goto L_0x002b
            int r2 = r1.getHeight()     // Catch:{ IllegalStateException -> 0x005f, TaskCancelledException -> 0x003c, IOException -> 0x005c, OutOfMemoryError -> 0x0059, Throwable -> 0x0056 }
            if (r2 > 0) goto L_0x0061
        L_0x002b:
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r2 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.DECODING_ERROR     // Catch:{ IllegalStateException -> 0x005f, TaskCancelledException -> 0x003c, IOException -> 0x005c, OutOfMemoryError -> 0x0059, Throwable -> 0x0056 }
            r3 = 0
            r4.fireFailEvent(r2, r3)     // Catch:{ IllegalStateException -> 0x005f, TaskCancelledException -> 0x003c, IOException -> 0x005c, OutOfMemoryError -> 0x0059, Throwable -> 0x0056 }
            r0 = r1
        L_0x0032:
            return r0
        L_0x0033:
            r1 = move-exception
            r1 = r0
        L_0x0035:
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r2 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.NETWORK_DENIED
            r4.fireFailEvent(r2, r0)
            r0 = r1
            goto L_0x0032
        L_0x003c:
            r0 = move-exception
            throw r0
        L_0x003e:
            r1 = move-exception
            r2 = r1
        L_0x0040:
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r1 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.IO_ERROR
            r4.fireFailEvent(r1, r2)
            goto L_0x0032
        L_0x0046:
            r1 = move-exception
            r2 = r1
        L_0x0048:
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r1 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.OUT_OF_MEMORY
            r4.fireFailEvent(r1, r2)
            goto L_0x0032
        L_0x004e:
            r1 = move-exception
            r2 = r1
        L_0x0050:
            com.nostra13.universalimageloader.core.assist.FailReason$FailType r1 = com.nostra13.universalimageloader.core.assist.FailReason.FailType.UNKNOWN
            r4.fireFailEvent(r1, r2)
            goto L_0x0032
        L_0x0056:
            r2 = move-exception
            r0 = r1
            goto L_0x0050
        L_0x0059:
            r2 = move-exception
            r0 = r1
            goto L_0x0048
        L_0x005c:
            r2 = move-exception
            r0 = r1
            goto L_0x0040
        L_0x005f:
            r2 = move-exception
            goto L_0x0035
        L_0x0061:
            r0 = r1
            goto L_0x0032
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.tryLoadBitmap():android.graphics.Bitmap");
    }

    private boolean waitIfPaused() {
        AtomicBoolean pause = this.engine.getPause();
        if (pause.get()) {
            synchronized (this.engine.getPauseLock()) {
                if (pause.get()) {
                    try {
                        this.engine.getPauseLock().wait();
                    } catch (InterruptedException e) {
                        return true;
                    }
                }
            }
        }
        return isTaskNotActual();
    }

    /* access modifiers changed from: package-private */
    public String getLoadingUri() {
        return this.uri;
    }

    public boolean onBytesCopied(int i, int i2) {
        return this.progressListener == null || fireProgressEvent(i, i2);
    }

    public void run() {
        Bitmap bitmap;
        if (!waitIfPaused() && !delayIfNeed()) {
            ReentrantLock reentrantLock = this.imageLoadingInfo.loadFromUriLock;
            reentrantLock.lock();
            try {
                checkTaskNotActual();
                Bitmap bitmap2 = this.configuration.memoryCache.get(this.memoryCacheKey);
                if (bitmap2 == null) {
                    bitmap = tryLoadBitmap();
                    if (bitmap != null) {
                        checkTaskNotActual();
                        checkTaskInterrupted();
                        if (this.options.shouldPreProcess()) {
                            bitmap = this.options.getPreProcessor().process(bitmap);
                        }
                        if (bitmap != null && this.options.isCacheInMemory()) {
                            this.configuration.memoryCache.put(this.memoryCacheKey, bitmap);
                        }
                    } else {
                        return;
                    }
                } else {
                    this.loadedFrom = LoadedFrom.MEMORY_CACHE;
                    bitmap = bitmap2;
                }
                if (bitmap != null && this.options.shouldPostProcess()) {
                    bitmap = this.options.getPostProcessor().process(bitmap);
                }
                checkTaskNotActual();
                checkTaskInterrupted();
                reentrantLock.unlock();
                runTask(new DisplayBitmapTask(bitmap, this.imageLoadingInfo, this.engine, this.loadedFrom, this.mKeyPath), this.options.isSyncLoading(), this.handler, this.engine);
            } catch (TaskCancelledException e) {
                fireCancelEvent();
            } finally {
                reentrantLock.unlock();
            }
        }
    }
}
