package com.nostra13.universalimageloader.utils;

import android.opengl.GLES10;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public final class ImageSizeUtils {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType = null;
    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;
    private static ImageSize maxBitmapSize;

    static /* synthetic */ int[] $SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType() {
        int[] iArr = $SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType;
        if (iArr == null) {
            iArr = new int[ViewScaleType.values().length];
            try {
                iArr[ViewScaleType.CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ViewScaleType.FIT_INSIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType = iArr;
        }
        return iArr;
    }

    static {
        int[] iArr = new int[1];
        GLES10.glGetIntegerv(3379, iArr, 0);
        int max = Math.max(iArr[0], 2048);
        maxBitmapSize = new ImageSize(max, max);
    }

    private ImageSizeUtils() {
    }

    public static int computeImageSampleSize(ImageSize imageSize, ImageSize imageSize2, ViewScaleType viewScaleType, boolean z) {
        int min;
        int width = imageSize.getWidth();
        int height = imageSize.getHeight();
        int width2 = imageSize2.getWidth();
        int height2 = imageSize2.getHeight();
        int i = width / width2;
        int i2 = height / height2;
        switch ($SWITCH_TABLE$com$nostra13$universalimageloader$core$assist$ViewScaleType()[viewScaleType.ordinal()]) {
            case 1:
                if (!z) {
                    min = Math.max(i, i2);
                    break;
                } else {
                    min = 1;
                    while (true) {
                        if (width / 2 < width2 && height / 2 < height2) {
                            break;
                        } else {
                            width /= 2;
                            height /= 2;
                            min *= 2;
                        }
                    }
                }
                break;
            case 2:
                if (!z) {
                    min = Math.min(i, i2);
                    break;
                } else {
                    int i3 = 1;
                    while (width / 2 >= width2 && height / 2 >= height2) {
                        width /= 2;
                        height /= 2;
                        i3 = min * 2;
                    }
                }
                break;
            default:
                min = 1;
                break;
        }
        if (min < 1) {
            return 1;
        }
        return min;
    }

    public static float computeImageScale(ImageSize imageSize, ImageSize imageSize2, ViewScaleType viewScaleType, boolean z) {
        int i;
        int width = imageSize.getWidth();
        int height = imageSize.getHeight();
        int width2 = imageSize2.getWidth();
        int height2 = imageSize2.getHeight();
        float f = ((float) width) / ((float) width2);
        float f2 = ((float) height) / ((float) height2);
        if ((viewScaleType != ViewScaleType.FIT_INSIDE || f < f2) && (viewScaleType != ViewScaleType.CROP || f >= f2)) {
            width2 = (int) (((float) width) / f2);
            i = height2;
        } else {
            i = (int) (((float) height) / f);
        }
        if ((z || width2 >= width || i >= height) && (!z || width2 == width || i == height)) {
            return 1.0f;
        }
        return ((float) width2) / ((float) width);
    }

    public static int computeMinImageSampleSize(ImageSize imageSize) {
        int width = imageSize.getWidth();
        int height = imageSize.getHeight();
        return Math.max((int) Math.ceil((double) (((float) width) / ((float) maxBitmapSize.getWidth()))), (int) Math.ceil((double) (((float) height) / ((float) maxBitmapSize.getHeight()))));
    }

    public static ImageSize defineTargetSizeForView(ImageAware imageAware, ImageSize imageSize) {
        int width = imageAware.getWidth();
        if (width <= 0) {
            width = imageSize.getWidth();
        }
        int height = imageAware.getHeight();
        if (height <= 0) {
            height = imageSize.getHeight();
        }
        return new ImageSize(width, height);
    }
}
