package com.nostra13.universalimageloader.cache.memory.impl;

import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class FuzzyKeyMemoryCache<K, V> implements MemoryCacheAware<K, V> {
    private final MemoryCacheAware<K, V> cache;
    private final Comparator<K> keyComparator;

    public FuzzyKeyMemoryCache(MemoryCacheAware<K, V> memoryCacheAware, Comparator<K> comparator) {
        this.cache = memoryCacheAware;
        this.keyComparator = comparator;
    }

    public void clear() {
        this.cache.clear();
    }

    public V get(K k) {
        return this.cache.get(k);
    }

    public Collection<K> keys() {
        return this.cache.keys();
    }

    public boolean put(K k, V v) {
        Object obj;
        synchronized (this.cache) {
            Iterator<K> it = this.cache.keys().iterator();
            while (true) {
                if (it.hasNext()) {
                    obj = it.next();
                    if (this.keyComparator.compare(k, obj) == 0) {
                        break;
                    }
                } else {
                    obj = null;
                    break;
                }
            }
            if (obj != null) {
                this.cache.remove(obj);
            }
        }
        return this.cache.put(k, v);
    }

    public void remove(K k) {
        this.cache.remove(k);
    }
}
