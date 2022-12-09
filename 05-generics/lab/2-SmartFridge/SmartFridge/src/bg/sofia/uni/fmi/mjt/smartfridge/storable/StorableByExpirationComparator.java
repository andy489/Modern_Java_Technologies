package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import java.util.Comparator;

public class StorableByExpirationComparator implements Comparator<Storable> {
    @Override
    public int compare(Storable s1, Storable s2) {
        return s1.getExpiration().compareTo(s2.getExpiration());
    }
}
