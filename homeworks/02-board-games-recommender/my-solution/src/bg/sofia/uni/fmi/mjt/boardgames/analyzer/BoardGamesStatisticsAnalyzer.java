package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardGamesStatisticsAnalyzer implements StatisticsAnalyzer {

    private final Collection<BoardGame> games;

    public BoardGamesStatisticsAnalyzer(Collection<BoardGame> games) {
        this.games = games;
    }

    @Override
    public List<String> getNMostPopularCategories(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid arg");
        }

        if (n == 0) {
            return List.of();
        }

        Map<String, Long> map = games.stream()
                .map(BoardGame::categories)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public double getAverageMinAge() {
        return games.stream()
                .mapToInt(BoardGame::minAge)
                .average()
                .orElse(0.0);
    }

    @Override
    public double getAveragePlayingTimeByCategory(String category) {
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Invalid arg");
        }

        return games.stream()
                .filter(g -> g.categories().contains(category))
                .mapToInt(BoardGame::currPlayingTimeMinutes)
                .average()
                .orElse(0.0);
    }

    @Override
    public Map<String, Double> getAveragePlayingTimeByCategory() {
        Map<String, List<Double>> categoryTime = new HashMap<>();

        for (BoardGame g : games) {
            for (String c : g.categories()) {
                if (!categoryTime.containsKey(c)) {
                    categoryTime.put(c, new ArrayList<>());
                }
                categoryTime.get(c).add((double) g.currPlayingTimeMinutes());
            }
        }

        Map<String, Double> categoryAverage = new HashMap<>();

        for (var entry : categoryTime.entrySet()) {
            categoryAverage.put(entry.getKey(),
                    entry.getValue().stream()
                            .mapToDouble(i -> i)
                            .average()
                            .orElse(0.0));
        }

        return categoryAverage;
    }
}
