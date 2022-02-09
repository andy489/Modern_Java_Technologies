package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record Stats(int id, String description) {

    public void fillIndex(Map<String, Set<Integer>> index, Set<String> stopWords, boolean toLower) {
        String[] words = description.split("[\\p{IsPunctuation}\\p{IsWhite_Space}]+");

        for (String word : words) {
            if (toLower) {
                word = word.toLowerCase();
            }

            if (!stopWords.contains(word.toLowerCase())) {
                if (!index.containsKey(word)) {
                    index.put(word, new HashSet<>());
                }
                index.get(word).add(id);
            }
        }
    }
}
