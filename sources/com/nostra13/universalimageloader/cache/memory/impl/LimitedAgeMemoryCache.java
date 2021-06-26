package com.nostra13.universalimageloader.cache.memory.impl;

import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LimitedAgeMemoryCache<K, V> implements MemoryCacheAware<K, V> {
    private final MemoryCacheAware<K, V> cache;
    private final Map<K, Long> loadingDates = Collections.synchronizedMap(new HashMap());
    private final long maxAge;

    public LimitedAgeMemoryCache(MemoryCacheAware<K, V> memoryCacheAware, long j) {
        this.cache = memoryCacheAware;
        this.maxAge = 1000 * j;
    }

    public void clear() {
        this.cache.clear();
        this.loadingDates.clear();
    }

    public V get(K k) {
        Long l = this.loadingDates.get(k);
        if (l != null && System.currentTimeMillis() - l.longValue() > this.maxAge) {
            this.cache.remove(k);
            this.loadingDates.remove(k);
        }
        return this.cache.get(k);
    }

    public Collection<K> keys() {
        return this.cache.keys();
    }

    public boolean put(K k, V v) {
        boolean put = this.cache.put(k, v);
        if (put) {
            this.loadingDates.put(k, Long.valueOf(System.currentTimeMillis()));
        }
        return put;
    }

    public void remove(K k) {
        this.cache.remove(k);
        this.loadingDates.remove(k);
    }
}
