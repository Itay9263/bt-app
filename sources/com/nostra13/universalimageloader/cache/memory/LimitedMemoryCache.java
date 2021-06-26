package com.nostra13.universalimageloader.cache.memory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class LimitedMemoryCache<K, V> extends BaseMemoryCache<K, V> {
    private final AtomicInteger cacheSize;
    private final List<V> hardCache = Collections.synchronizedList(new LinkedList());
    private final int sizeLimit;

    public LimitedMemoryCache(int i) {
        this.sizeLimit = i;
        this.cacheSize = new AtomicInteger();
    }

    public void clear() {
        this.hardCache.clear();
        this.cacheSize.set(0);
        super.clear();
    }

    /* access modifiers changed from: protected */
    public abstract int getSize(V v);

    /* access modifiers changed from: protected */
    public int getSizeLimit() {
        return this.sizeLimit;
    }

    public boolean put(K k, V v) {
        boolean z = false;
        int size = getSize(v);
        int sizeLimit2 = getSizeLimit();
        int i = this.cacheSize.get();
        if (size < sizeLimit2) {
            int i2 = i;
            while (i2 + size > sizeLimit2) {
                Object removeNext = removeNext();
                if (this.hardCache.remove(removeNext)) {
                    i2 = this.cacheSize.addAndGet(-getSize(removeNext));
                }
            }
            this.hardCache.add(v);
            this.cacheSize.addAndGet(size);
            z = true;
        }
        super.put(k, v);
        return z;
    }

    public void remove(K k) {
        Object obj = super.get(k);
        if (obj != null && this.hardCache.remove(obj)) {
            this.cacheSize.addAndGet(-getSize(obj));
        }
        super.remove(k);
    }

    /* access modifiers changed from: protected */
    public abstract V removeNext();
}
