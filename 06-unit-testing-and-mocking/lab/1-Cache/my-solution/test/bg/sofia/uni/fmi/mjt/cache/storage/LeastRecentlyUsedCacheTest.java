package bg.sofia.uni.fmi.mjt.cache.storage;

import bg.sofia.uni.fmi.mjt.cache.storage.exception.ItemNotFound;
import bg.sofia.uni.fmi.mjt.cache.storage.factory.CacheFactory;
import bg.sofia.uni.fmi.mjt.cache.storage.factory.EvictionPolicy;
import bg.sofia.uni.fmi.mjt.cache.storage.storage.Storage;

import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LeastRecentlyUsedCacheTest extends TestConstants {

    private static Cache<String, Double> LRU;

    @Mock
    private Storage<String, Double> mockStorage;

    @BeforeEach
    public void initCacheLRU() {
        LRU = new LeastRecentlyUsedCache<>(mockStorage, VALID_CAPACITY);

        lenient().when(mockStorage.retrieve(key1)).thenReturn(value1);
        lenient().when(mockStorage.retrieve(key2)).thenReturn(value2);
        lenient().when(mockStorage.retrieve(key3)).thenReturn(value3);
        lenient().when(mockStorage.retrieve(key4)).thenReturn(value4);
    }

    @Test
    public void testDefaultCapacityFactoryCacheLRU() {
        Cache<String, Double> defaultLRU =
                CacheFactory.getInstance(mockStorage, EvictionPolicy.LEAST_RECENTLY_USED);
        assertTrue(defaultLRU instanceof LeastRecentlyUsedCache);
    }

    @Test
    public void testWithValidPredefinedCapacityFactoryCacheLRU() {
        Cache<String, Double> LRU =
                CacheFactory.getInstance(mockStorage, VALID_CAPACITY,EvictionPolicy.LEAST_RECENTLY_USED);
        assertTrue(LRU instanceof LeastRecentlyUsedCache);
    }

    @Test
    public void testWithInvalidPredefinedCapacityFactoryCacheLRU() {
        assertThrows(IllegalArgumentException.class,
                () -> CacheFactory.getInstance(mockStorage, INVALID_CAPACITY, EvictionPolicy.LEAST_RECENTLY_USED));
    }

    @Test
    public void testWithInvalidCapacityCacheLRU() {
        assertThrows(IllegalArgumentException.class,
                () -> new LeastRecentlyUsedCache<>(mockStorage, INVALID_CAPACITY));
    }

    @Test
    public void testWithNullCacheLRU() {
        assertThrows(IllegalArgumentException.class,
                () -> LRU.get(null));
    }

    @Test
    public void testWithNonExistingItemCacheLRU() {
        assertThrows(ItemNotFound.class,
                () -> LRU.get(NON_EXISTING_ITEM));
    }

    @Test
    public void testHitRateCacheLRU() throws ItemNotFound {
        assertEquals(0.0, LRU.getHitRate(), PRECISION); // starting hit rate

        assertEquals(value1, LRU.get(key1)); // hits: 0, misses: 1
        verify(mockStorage, times(1)).retrieve(key1);

        assertEquals(0.0, LRU.getHitRate(), PRECISION);
        assertEquals(value1, LRU.get(key1)); // hits: 1, misses: 1
        verify(mockStorage, times(1)).retrieve(key1);

        // We must have 1 successful hits here of total 2 hits
        assertEquals(1.0 / 2, LRU.getHitRate(), PRECISION);

        assertEquals(value2, LRU.get(key2)); // hits: 1, misses: 2
        assertEquals(1.0 / 3, LRU.getHitRate(), PRECISION);
        verify(mockStorage, times(1)).retrieve(key2);

        assertEquals(value2, LRU.get(key2)); // hits: 2, misses: 2
        verify(mockStorage, times(1)).retrieve(key2);

        // We must have 2 successful hits here of total 4 hits
        assertEquals(2.0 / 4, LRU.getHitRate(), PRECISION);
    }

    @Test
    public void testSizeCacheLRU() throws ItemNotFound {
        assertEquals(0, LRU.size());
        for (int i = 0; i < VALID_CAPACITY; ++i) {
            LRU.get(key1);
        }
        verify(mockStorage, times(1)).retrieve(key1);

        assertEquals(1, LRU.size());
        LRU.get(key2);
        verify(mockStorage, times(1)).retrieve(key2);

        assertEquals(2, LRU.size());
        LRU.get(key3);
        verify(mockStorage, times(1)).retrieve(key3);

        assertEquals(3, LRU.size());
        LRU.get(key4);
        verify(mockStorage, times(1)).retrieve(key4);

        assertEquals(3, LRU.size());
    }

    @Test
    public void testClearLRU() throws ItemNotFound {
        LRU.get(key1);
        verify(mockStorage, times(1)).retrieve(key1);

        assertEquals(1, LRU.size());
        LRU.clear();
        assertEquals(0, LRU.size());

        LRU.get(key2);
        verify(mockStorage, times(1)).retrieve(key2);
        LRU.get(key3);
        verify(mockStorage, times(1)).retrieve(key3);
        LRU.get(key4);
        verify(mockStorage, times(1)).retrieve(key4);
        assertEquals(3, LRU.size());
        LRU.clear();
        assertEquals(0, LRU.size());

        LRU.get(key4);
        verify(mockStorage, times(2)).retrieve(key4);
    }

    @Test
    public void testHitRateWhenClearLRU() throws ItemNotFound {
        LRU.get(key1);
        assertEquals(1, LRU.size());
        verify(mockStorage, times(1)).retrieve(key1);

        LRU.clear();
        assertEquals(0, LRU.size());

        LRU.get(key2);
        verify(mockStorage, times(1)).retrieve(key2);

        for (int i = 0; i < VALID_CAPACITY; ++i) {
            LRU.get(key3);
        }
        verify(mockStorage, times(1)).retrieve(key3);

        LRU.get(key4);
        verify(mockStorage, times(1)).retrieve(key4);

        assertEquals(3, LRU.size());
        assertEquals(2.0 / 6, LRU.getHitRate(), PRECISION);

        LRU.get(key1);
        verify(mockStorage, times(2)).retrieve(key1);

        LRU.clear();
        assertEquals(0.0, LRU.getHitRate(), PRECISION);
        assertEquals(0, LRU.size());
    }

    @Test
    public void testValuesCacheLRU() throws ItemNotFound {
        assertEquals(0, LRU.values().size());

        LRU.get(key1);
        for (int i = 0; i < BIG_NUMBER_OF_HITS; ++i) {
            LRU.get(key1);
        }

        assertEquals(1, LRU.values().size());
        assertTrue(LRU.values().contains(value1));

        LRU.get(key2); // Least Frequently Used

        for (int i = 0; i < BIG_NUMBER_OF_HITS; ++i) {
            LRU.get(key3);
        }

        LRU.get(key4);

        assertEquals(3, LRU.values().size());
        System.out.println(LRU.values());
        assertTrue(LRU.values().containsAll(List.of(value2, value3, value4)));
    }

    @Test
    public void testValuesWhenTheLeastRecentlyUsedValueIsHitJustBeforeCapacityOverflows() throws ItemNotFound {
        LRU.get(key1);
        LRU.get(key2);
        LRU.get(key3);
        assertTrue(LRU.values().containsAll(List.of(value1, value2, value3)));

        LRU.get(key1);
        LRU.get(key4);
        assertTrue(LRU.values().containsAll(List.of(value1, value3, value4)));
    }

    @Test
    public void testWithNonExistingElementInStorageLRU() {
        assertThrows(ItemNotFound.class, () -> LRU.get(NON_EXISTING_ITEM));
    }

}
