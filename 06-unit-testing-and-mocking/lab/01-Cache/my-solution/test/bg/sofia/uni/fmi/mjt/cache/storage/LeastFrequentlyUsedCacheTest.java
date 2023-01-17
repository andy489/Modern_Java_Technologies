package bg.sofia.uni.fmi.mjt.cache.storage;

import bg.sofia.uni.fmi.mjt.cache.storage.exception.ItemNotFound;
import bg.sofia.uni.fmi.mjt.cache.storage.factory.CacheFactory;
import bg.sofia.uni.fmi.mjt.cache.storage.factory.EvictionPolicy;
import bg.sofia.uni.fmi.mjt.cache.storage.storage.Storage;

import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LeastFrequentlyUsedCacheTest extends TestConstants {

    private static Cache<String, Double> LFU;

    @Mock
    private Storage<String, Double> mockStorage;

    @BeforeEach
    public void initCacheLFU() {
        LFU = new LeastFrequentlyUsedCache<>(mockStorage, VALID_CAPACITY);

        lenient().when(mockStorage.retrieve(key1)).thenReturn(value1);
        lenient().when(mockStorage.retrieve(key2)).thenReturn(value2);
        lenient().when(mockStorage.retrieve(key3)).thenReturn(value3);
        lenient().when(mockStorage.retrieve(key4)).thenReturn(value4);
    }

    @Test
    public void testDefaultCapacityFactoryCacheLFU() {
        Cache<String, Double> defaultLFU =
                CacheFactory.getInstance(mockStorage, EvictionPolicy.LEAST_FREQUENTLY_USED);
        assertTrue(defaultLFU instanceof LeastFrequentlyUsedCache);
    }

    @Test
    public void testWithValidPredefinedCapacityFactoryCacheLFU() {
        Cache<String, Double> LFU =
                CacheFactory.getInstance(mockStorage, VALID_CAPACITY, EvictionPolicy.LEAST_FREQUENTLY_USED);
        assertTrue(LFU instanceof LeastFrequentlyUsedCache);
    }

    @Test
    public void testWithInvalidPredefinedCapacityFactoryCacheLFU() {
        assertThrows(IllegalArgumentException.class,
                () -> CacheFactory.getInstance(mockStorage, INVALID_CAPACITY, EvictionPolicy.LEAST_FREQUENTLY_USED));
    }

    @Test
    public void testWithInvalidCapacityCacheLFU() {
        assertThrows(IllegalArgumentException.class,
                () -> new LeastFrequentlyUsedCache<>(mockStorage, INVALID_CAPACITY));
    }

    @Test
    public void testWithNullCacheLFU() {
        assertThrows(IllegalArgumentException.class,
                () -> LFU.get(null));
    }

    @Test
    public void testWithNonExistingItemCacheLFU() {
        assertThrows(ItemNotFound.class,
                () -> LFU.get(NON_EXISTING_ITEM));
    }

    @Test
    public void testHitRateCacheLFU() throws ItemNotFound {
        assertEquals(0.0, LFU.getHitRate(), PRECISION); // starting hit rate

        assertEquals(value1, LFU.get(key1)); // hits: 0, misses: 1
        verify(mockStorage, times(1)).retrieve(key1);

        assertEquals(0.0, LFU.getHitRate(), PRECISION);
        assertEquals(value1, LFU.get(key1)); // hits: 1, misses: 1
        verify(mockStorage, times(1)).retrieve(key1);

        // We must have 1 successful hits here of total 2 hits
        assertEquals(1.0 / 2, LFU.getHitRate(), PRECISION);

        assertEquals(value2, LFU.get(key2)); // hits: 1, misses: 2
        assertEquals(1.0 / 3, LFU.getHitRate(), PRECISION);
        verify(mockStorage, times(1)).retrieve(key2);

        assertEquals(value2, LFU.get(key2)); // hits: 2, misses: 2
        verify(mockStorage, times(1)).retrieve(key2);

        // We must have 2 successful hits here of total 4 hits
        assertEquals(2.0 / 4, LFU.getHitRate(), PRECISION);
    }

    @Test
    public void testSizeCacheLFU() throws ItemNotFound {
        assertEquals(0, LFU.size());
        for (int i = 0; i < VALID_CAPACITY; ++i) {
            LFU.get(key1);
        }
        verify(mockStorage, times(1)).retrieve(key1);

        assertEquals(1, LFU.size());
        LFU.get(key2);
        verify(mockStorage, times(1)).retrieve(key2);

        assertEquals(2, LFU.size());
        LFU.get(key3);
        verify(mockStorage, times(1)).retrieve(key3);

        assertEquals(3, LFU.size());
        LFU.get(key4);
        verify(mockStorage, times(1)).retrieve(key4);

        assertEquals(3, LFU.size());
    }

    @Test
    public void testClearCacheLFU() throws ItemNotFound {
        LFU.get(key1);
        verify(mockStorage, times(1)).retrieve(key1);

        assertEquals(1, LFU.size());
        LFU.clear();
        assertEquals(0, LFU.size());

        LFU.get(key2);
        verify(mockStorage, times(1)).retrieve(key2);
        LFU.get(key3);
        verify(mockStorage, times(1)).retrieve(key3);
        LFU.get(key4);
        verify(mockStorage, times(1)).retrieve(key4);
        assertEquals(3, LFU.size());
        LFU.clear();
        assertEquals(0, LFU.size());

        LFU.get(key4);
        verify(mockStorage, times(2)).retrieve(key4);
    }

    @Test
    public void testHitRateWhenClearLFU() throws ItemNotFound {
        LFU.get(key1);
        assertEquals(1, LFU.size());
        verify(mockStorage, times(1)).retrieve(key1);

        LFU.clear();
        assertEquals(0, LFU.size());

        LFU.get(key2);
        verify(mockStorage, times(1)).retrieve(key2);

        for (int i = 0; i < VALID_CAPACITY; ++i) {
            LFU.get(key3);
        }
        verify(mockStorage, times(1)).retrieve(key3);

        LFU.get(key4);
        verify(mockStorage, times(1)).retrieve(key4);

        assertEquals(3, LFU.size());
        assertEquals(2.0 / 6, LFU.getHitRate(), PRECISION);

        LFU.get(key1);
        verify(mockStorage, times(2)).retrieve(key1);

        LFU.clear();
        assertEquals(0.0, LFU.getHitRate(), PRECISION);
        assertEquals(0, LFU.size());
    }

    @Test
    public void testValuesCacheLFU() throws ItemNotFound {
        assertEquals(0, LFU.values().size());

        LFU.get(key1);
        for (int i = 0; i < BIG_NUMBER_OF_HITS; ++i) {
            LFU.get(key1);
        }

        assertEquals(1, LFU.values().size());
        assertTrue(LFU.values().contains(value1));

        LFU.get(key2); // Least Frequently Used

        for (int i = 0; i < BIG_NUMBER_OF_HITS; ++i) {
            LFU.get(key3);
        }

        LFU.get(key4);

        assertEquals(3, LFU.values().size());
        assertTrue(LFU.values().containsAll(List.of(value1, value3, value4)));
    }

    @Test
    public void testWithNonExistingElementInStorageLFU() {
        assertThrows(ItemNotFound.class, () -> LFU.get(NON_EXISTING_ITEM));
    }
}
