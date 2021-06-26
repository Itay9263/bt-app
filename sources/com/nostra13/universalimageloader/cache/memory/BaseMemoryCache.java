package com.nostra13.universalimageloader.cache.memory;

import java.lang.ref.Reference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class BaseMemoryCache<K, V> implements MemoryCacheAware<K, V> {
    private final Map<K, Reference<V>> softMap = Collections.synchronizedMap(new HashMap());

    public void clear() {
        this.softMap.clear();
    }

    /* access modifiers changed from: protected */
    public abstract Reference<V> createReference(V v);

    public V get(K k) {
        Reference reference = this.softMap.get(k);
        if (reference != null) {
            return reference.get();
        }
        return null;
    }

    public Collection<K> keys() {
        HashSet hashSet;
        synchronized (this.softMap) {
            hashSet = new HashSet(this.softMap.keySet());
        }
        return hashSet;
    }

    public boolean put(K k, V v) {
        this.softMap.put(k, createReference(v));
        return true;
    }

    public void remove(K k) {
        this.softMap.remove(k);
    }
}
