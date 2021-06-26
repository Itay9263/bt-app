package com.nostra13.universalimageloader.utils;

import android.graphics.Bitmap;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MemoryCacheUtils {
    private static final String URI_AND_SIZE_SEPARATOR = "_";
    private static final String WIDTH_AND_HEIGHT_SEPARATOR = "x";

    private MemoryCacheUtils() {
    }

    public static Comparator<String> createFuzzyKeyComparator() {
        return new Comparator<String>() {
            public int compare(String str, String str2) {
                return str.substring(0, str.lastIndexOf(MemoryCacheUtils.URI_AND_SIZE_SEPARATOR)).compareTo(str2.substring(0, str2.lastIndexOf(MemoryCacheUtils.URI_AND_SIZE_SEPARATOR)));
            }
        };
    }

    public static List<String> findCacheKeysForImageUri(String str, MemoryCacheAware<String, Bitmap> memoryCacheAware) {
        ArrayList arrayList = new ArrayList();
        for (String next : memoryCacheAware.keys()) {
            if (next.startsWith(str)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    public static List<Bitmap> findCachedBitmapsForImageUri(String str, MemoryCacheAware<String, Bitmap> memoryCacheAware) {
        ArrayList arrayList = new ArrayList();
        for (String next : memoryCacheAware.keys()) {
            if (next.startsWith(str)) {
                arrayList.add(memoryCacheAware.get(next));
            }
        }
        return arrayList;
    }

    public static String generateKey(String str, ImageSize imageSize) {
        return str + URI_AND_SIZE_SEPARATOR + imageSize.getWidth() + WIDTH_AND_HEIGHT_SEPARATOR + imageSize.getHeight();
    }

    public static void removeFromCache(String str, MemoryCacheAware<String, Bitmap> memoryCacheAware) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String next : memoryCacheAware.keys()) {
            if (next.startsWith(str)) {
                arrayList.add(next);
            }
        }
        for (String remove : arrayList) {
            memoryCacheAware.remove(remove);
        }
    }
}
