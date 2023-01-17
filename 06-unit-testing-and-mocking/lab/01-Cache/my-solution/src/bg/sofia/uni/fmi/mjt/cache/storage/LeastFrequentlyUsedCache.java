package bg.sofia.uni.fmi.mjt.cache.storage;

import bg.sofia.uni.fmi.mjt.cache.storage.storage.Storage;

import java.util.*;

public class LeastFrequentlyUsedCache<K, V> extends CacheBase<K, V> {
    private final HashMap<K, V> cache;
    private final Map<K, Integer> keyUses;

    public LeastFrequentlyUsedCache(Storage<K, V> storage, int capacity) {
        super(storage, capacity);
        cache = new HashMap<>(capacity);
        keyUses = new HashMap<>(capacity);
    }

    @Override
    public int size() {
        return this.cache.size();
    }

    @Override
    public Collection<V> values() {
        return Collections.unmodifiableCollection(this.cache.values());
    }

    @Override
    public void clear() {
        super.resetHitRate();
        cache.clear();
        keyUses.clear();
    }

    protected V getFromCache(K key) {
        var val = cache.get(key);
        if (val == null) {
            return null;
        }

        keyUses.merge(key, 1, Integer::sum); // BUG_03_LFU: increment hit when getFromCache

//        keyUses.put(key, keyUses.get(key) + 1);

        return val;
    }

    protected V put(K key, V value) {
        keyUses.merge(key, 1, Integer::sum);
/*
          if (keyUses.containsKey(key)) {
              keyUses.put(key, keyUses.get(key) + 1);
          } else {
              keyUses.put(key, 1);
          }
*/
        return cache.put(key, value);
    }

    protected boolean containsKey(K k) {
        return this.cache.containsKey(k);
    }

    protected void evictFromCache() {
        K evictionKey = getEvictionKey();
        cache.remove(evictionKey);
        keyUses.remove(evictionKey);
    }

    private K getEvictionKey() {
        int minUsageCount = Integer.MAX_VALUE; // BUG_01_LFU: minUsageCount not set properly
        K toBeRemoved = null;

        for (K key : keyUses.keySet()) {
            int usageCount = keyUses.get(key);

            if (usageCount <= minUsageCount) { // BUG_02_LFU: fix scope {}
                toBeRemoved = key;
                minUsageCount = usageCount;
            }
        }

        return toBeRemoved;
    }

}