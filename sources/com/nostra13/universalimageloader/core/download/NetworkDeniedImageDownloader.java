package com.nostra13.universalimageloader.core.download;

import com.nostra13.universalimageloader.core.download.ImageDownloader;
import java.io.IOException;
import java.io.InputStream;

public class NetworkDeniedImageDownloader implements ImageDownloader {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme;
    private final ImageDownloader wrappedDownloader;

    static /* synthetic */ int[] $SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme() {
        int[] iArr = $SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme;
        if (iArr == null) {
            iArr = new int[ImageDownloader.Scheme.values().length];
            try {
                iArr[ImageDownloader.Scheme.ASSETS.ordinal()] = 5;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ImageDownloader.Scheme.CONTENT.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ImageDownloader.Scheme.DRAWABLE.ordinal()] = 6;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ImageDownloader.Scheme.FILE.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ImageDownloader.Scheme.HTTP.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ImageDownloader.Scheme.HTTPS.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[ImageDownloader.Scheme.UNKNOWN.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            $SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme = iArr;
        }
        return iArr;
    }

    public NetworkDeniedImageDownloader(ImageDownloader imageDownloader) {
        this.wrappedDownloader = imageDownloader;
    }

    public InputStream getStream(String str, Object obj) throws IOException {
        switch ($SWITCH_TABLE$com$nostra13$universalimageloader$core$download$ImageDownloader$Scheme()[ImageDownloader.Scheme.ofUri(str).ordinal()]) {
            case 1:
            case 2:
                throw new IllegalStateException();
            default:
                return this.wrappedDownloader.getStream(str, obj);
        }
    }
}
