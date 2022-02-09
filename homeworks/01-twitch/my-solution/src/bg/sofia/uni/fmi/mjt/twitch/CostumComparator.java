package bg.sofia.uni.fmi.mjt.twitch;

import java.util.Comparator;

public class CostumComparator implements Comparator<CategoryCount> {
    @Override
    public int compare(CategoryCount o1, CategoryCount o2) {
        return Long.compare(o1.count(), o2.count());
    }
}
