package com.nostra13.universalimageloader.core.decode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.nostra13.universalimageloader.utils.IoUtils;
import java.io.IOException;
import java.io.InputStream;

public class BaseImageDecoder implements ImageDecoder {
    protected static final String ERROR_CANT_DECODE_IMAGE = "Image can't be decoded [%s]";
    protected static final String LOG_FLIP_IMAGE = "Flip image horizontally [%s]";
    protected static final String LOG_ROTATE_IMAGE = "Rotate image on %1$d° [%2$s]";
    protected static final String LOG_SABSAMPLE_IMAGE = "Subsample original image (%1$s) to %2$s (scale = %3$d) [%4$s]";
    protected static final String LOG_SCALE_IMAGE = "Scale subsampled image (%1$s) to %2$s (scale = %3$.5f) [%4$s]";
    protected final boolean loggingEnabled;

    protected static class ExifInfo {
        public final boolean flipHorizontal;
        public final int rotation;

        protected ExifInfo() {
            this.rotation = 0;
            this.flipHorizontal = false;
        }

        protected ExifInfo(int i, boolean z) {
            this.rotation = i;
            this.flipHorizontal = z;
        }
    }

    protected static class ImageFileInfo {
        public final ExifInfo exif;
        public final ImageSize imageSize;

        protected ImageFileInfo(ImageSize imageSize2, ExifInfo exifInfo) {
            this.imageSize = imageSize2;
            this.exif = exifInfo;
        }
    }

    public BaseImageDecoder(boolean z) {
        this.loggingEnabled = z;
    }

    private boolean canDefineExifParams(String str, String str2) {
        return Build.VERSION.SDK_INT >= 5 && "image/jpeg".equalsIgnoreCase(str2) && ImageDownloader.Scheme.ofUri(str) == ImageDownloader.Scheme.FILE;
    }

    /* access modifiers changed from: protected */
    public Bitmap considerExactScaleAndOrientaiton(Bitmap bitmap, ImageDecodingInfo imageDecodingInfo, int i, boolean z) {
        Matrix matrix = new Matrix();
        ImageScaleType imageScaleType = imageDecodingInfo.getImageScaleType();
        if (imageScaleType == ImageScaleType.EXACTLY || imageScaleType == ImageScaleType.EXACTLY_STRETCHED) {
            float computeImageScale = ImageSizeUtils.computeImageScale(new ImageSize(bitmap.getWidth(), bitmap.getHeight(), i), imageDecodingInfo.getTargetSize(), imageDecodingInfo.getViewScaleType(), imageScaleType == ImageScaleType.EXACTLY_STRETCHED);
            if (Float.compare(computeImageScale, 1.0f) != 0) {
                matrix.setScale(computeImageScale, computeImageScale);
            }
        }
        if (z) {
            matrix.postScale(-1.0f, 1.0f);
        }
        if (i != 0) {
            matrix.postRotate((float) i);
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (createBitmap != bitmap) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public Bitmap decode(ImageDecodingInfo imageDecodingInfo) throws IOException {
        InputStream imageStream = getImageStream(imageDecodingInfo);
        try {
            ImageFileInfo defineImageSizeAndRotation = defineImageSizeAndRotation(imageStream, imageDecodingInfo);
            imageStream = resetStream(imageStream, imageDecodingInfo);
            Bitmap decodeStream = BitmapFactory.decodeStream(imageStream, (Rect) null, prepareDecodingOptions(defineImageSizeAndRotation.imageSize, imageDecodingInfo));
            return decodeStream != null ? considerExactScaleAndOrientaiton(decodeStream, imageDecodingInfo, defineImageSizeAndRotation.exif.rotation, defineImageSizeAndRotation.exif.flipHorizontal) : decodeStream;
        } finally {
            IoUtils.closeSilently(imageStream);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0023, code lost:
        r2 = 90;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0028, code lost:
        r2 = 180;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002d, code lost:
        r2 = 270;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0020, code lost:
        r2 = 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.nostra13.universalimageloader.core.decode.BaseImageDecoder.ExifInfo defineExifOrientation(java.lang.String r6) {
        /*
            r5 = this;
            r1 = 0
            r0 = 1
            android.media.ExifInterface r2 = new android.media.ExifInterface     // Catch:{ IOException -> 0x0031 }
            com.nostra13.universalimageloader.core.download.ImageDownloader$Scheme r3 = com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme.FILE     // Catch:{ IOException -> 0x0031 }
            java.lang.String r3 = r3.crop(r6)     // Catch:{ IOException -> 0x0031 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0031 }
            java.lang.String r3 = "Orientation"
            r4 = 1
            int r2 = r2.getAttributeInt(r3, r4)     // Catch:{ IOException -> 0x0031 }
            switch(r2) {
                case 1: goto L_0x001f;
                case 2: goto L_0x0020;
                case 3: goto L_0x0027;
                case 4: goto L_0x0028;
                case 5: goto L_0x002d;
                case 6: goto L_0x0022;
                case 7: goto L_0x0023;
                case 8: goto L_0x002c;
                default: goto L_0x0017;
            }
        L_0x0017:
            r0 = r1
            r2 = r1
        L_0x0019:
            com.nostra13.universalimageloader.core.decode.BaseImageDecoder$ExifInfo r1 = new com.nostra13.universalimageloader.core.decode.BaseImageDecoder$ExifInfo
            r1.<init>(r2, r0)
            return r1
        L_0x001f:
            r0 = r1
        L_0x0020:
            r2 = r1
            goto L_0x0019
        L_0x0022:
            r0 = r1
        L_0x0023:
            r1 = 90
            r2 = r1
            goto L_0x0019
        L_0x0027:
            r0 = r1
        L_0x0028:
            r1 = 180(0xb4, float:2.52E-43)
            r2 = r1
            goto L_0x0019
        L_0x002c:
            r0 = r1
        L_0x002d:
            r1 = 270(0x10e, float:3.78E-43)
            r2 = r1
            goto L_0x0019
        L_0x0031:
            r0 = move-exception
            r0 = r1
            r2 = r1
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.core.decode.BaseImageDecoder.defineExifOrientation(java.lang.String):com.nostra13.universalimageloader.core.decode.BaseImageDecoder$ExifInfo");
    }

    /* access modifiers changed from: protected */
    public ImageFileInfo defineImageSizeAndRotation(InputStream inputStream, ImageDecodingInfo imageDecodingInfo) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, (Rect) null, options);
        String imageUri = imageDecodingInfo.getImageUri();
        ExifInfo exifInfo = (!imageDecodingInfo.shouldConsiderExifParams() || !canDefineExifParams(imageUri, options.outMimeType)) ? new ExifInfo() : defineExifOrientation(imageUri);
        return new ImageFileInfo(new ImageSize(options.outWidth, options.outHeight, exifInfo.rotation), exifInfo);
    }

    /* access modifiers changed from: protected */
    public InputStream getImageStream(ImageDecodingInfo imageDecodingInfo) throws IOException {
        return imageDecodingInfo.getDownloader().getStream(imageDecodingInfo.getImageUri(), imageDecodingInfo.getExtraForDownloader());
    }

    /* access modifiers changed from: protected */
    public BitmapFactory.Options prepareDecodingOptions(ImageSize imageSize, ImageDecodingInfo imageDecodingInfo) {
        int computeImageSampleSize;
        ImageScaleType imageScaleType = imageDecodingInfo.getImageScaleType();
        if (imageScaleType == ImageScaleType.NONE) {
            computeImageSampleSize = ImageSizeUtils.computeMinImageSampleSize(imageSize);
        } else {
            computeImageSampleSize = ImageSizeUtils.computeImageSampleSize(imageSize, imageDecodingInfo.getTargetSize(), imageDecodingInfo.getViewScaleType(), imageScaleType == ImageScaleType.IN_SAMPLE_POWER_OF_2);
        }
        BitmapFactory.Options decodingOptions = imageDecodingInfo.getDecodingOptions();
        decodingOptions.inSampleSize = computeImageSampleSize;
        return decodingOptions;
    }

    /* access modifiers changed from: protected */
    public InputStream resetStream(InputStream inputStream, ImageDecodingInfo imageDecodingInfo) throws IOException {
        try {
            inputStream.reset();
            return inputStream;
        } catch (IOException e) {
            IoUtils.closeSilently(inputStream);
            return getImageStream(imageDecodingInfo);
        }
    }
}
