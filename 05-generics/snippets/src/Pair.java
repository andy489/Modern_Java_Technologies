import java.util.List;
import java.util.Set;

public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K key() {
        return key;
    }

    public V value() {
        return value;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Pair{key=%s, value=%s}", key, value);
    }

    public static void main(String... args) {
        Pair<String, Integer> pair1 = new Pair<>("Stoyo", 1);
        Pair<String, Double> pair2 = new Pair<>("Pesho", 2.7182);
        Pair<Double, Double> pair3 = new Pair<>(1.1, 2.2);
        Pair<List<String>, Set<Integer>> pair4 = new Pair<>(List.of("FMI", "MJT"), Set.of(2021, 2022, 2023));

        System.out.println(pair4);
    }
}
