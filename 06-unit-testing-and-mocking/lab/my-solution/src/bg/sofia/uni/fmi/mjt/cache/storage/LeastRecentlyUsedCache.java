package bg.sofia.uni.fmi.mjt.cache.storage;

import bg.sofia.uni.fmi.mjt.cache.storage.storage.Storage;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

public class LeastRecentlyUsedCache<K, V> extends CacheBase<K, V> {

    private static final float LOAD_FACTOR = 0.75f;
    private final LinkedHashMap<K, V> cache;

    public LeastRecentlyUsedCache(Storage<K, V> storage, int capacity) {
        super(storage, capacity);
        this.cache = new LinkedHashMap<>(capacity, LOAD_FACTOR, true);
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public void clear() {
        super.resetHitRate();
        cache.clear();
    }

    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(this.cache.values());
    }

    protected V getFromCache(K k) {
        return cache.get(k);
    }

    protected V put(K k, V v) {
        return cache.put(k, v);
    }

    protected boolean containsKey(K k) {
        return cache.containsKey(k);
    }


    protected void evictFromCache() {
        var it = cache.keySet().iterator();
        K first = null;
        if (it.hasNext()) {
            first = it.next(); // BUG_01_LRU: cannot call erase to iterator
        }
        cache.keySet().remove(first);
    }

}