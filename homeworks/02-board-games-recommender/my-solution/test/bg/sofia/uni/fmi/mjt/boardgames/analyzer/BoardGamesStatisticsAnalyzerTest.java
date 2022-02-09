package bg.sofia.uni.fmi.mjt.boardgames.analyzer;

import bg.sofia.uni.fmi.mjt.boardgames.TestConstants;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardGamesStatisticsAnalyzerTest extends TestConstants {

    @Test
    public void testGetNMostPopularCategories() {
        var analyzer = new BoardGamesStatisticsAnalyzer(MY_GAMES);

        List<String> mostPopular = analyzer.getNMostPopularCategories(0);
        assertTrue(mostPopular.isEmpty());

        mostPopular = analyzer.getNMostPopularCategories(1);
        assertEquals(1, mostPopular.size());
        assertTrue(mostPopular.contains("Adventure"));

        mostPopular = analyzer.getNMostPopularCategories(2);
        assertEquals(2, mostPopular.size());
        assertTrue(mostPopular.containsAll(List.of("Adventure", "Fantasy")));

        mostPopular = analyzer.getNMostPopularCategories(3);
        assertEquals(3, mostPopular.size());
        assertFalse(mostPopular.containsAll(List.of("Horror", "Economic"))); // does not contain at least one of them

        mostPopular = analyzer.getNMostPopularCategories(100);
        assertEquals(6, mostPopular.size());
        assertTrue(mostPopular.containsAll(List.of(
                "Adventure", "Fantasy", "Negotiation", "Economic", "Horror", "Card Game"))
        );
    }

    @Test
    public void testGetAverageMinAgeNoGames() {
        var analyzer = new BoardGamesStatisticsAnalyzer(List.of());
        assertEquals(0.0, analyzer.getAverageMinAge());
    }

    @Test
    public void testGetAverageMinAge() {
        var analyzer = new BoardGamesStatisticsAnalyzer(MY_GAMES);
        assertEquals(12.0, analyzer.getAverageMinAge(), EPSILON);
    }

    @Test
    public void testGetAveragePlayingTimeByCategoryNoGames() {
        var analyzer = new BoardGamesStatisticsAnalyzer(List.of());
        assertEquals(0.0, analyzer.getAverageMinAge());
    }

    @Test
    public void testGetAveragePlayingTimeByCategoryWithArgument() {
        var analyzer = new BoardGamesStatisticsAnalyzer(MY_GAMES);

        assertEquals(AVERAGE_PLAYTIME_ADVENTURE, analyzer.getAveragePlayingTimeByCategory("Adventure"), EPSILON);
        assertEquals(AVERAGE_PLAYTIME_CARD_GAME, analyzer.getAveragePlayingTimeByCategory("Card Game"), EPSILON);
        assertEquals(AVERAGE_PLAYTIME_FANTASY, analyzer.getAveragePlayingTimeByCategory("Fantasy"), EPSILON);
        assertEquals(0.0, analyzer.getAveragePlayingTimeByCategory(NON_EXISTING_CATEGORY));
    }

    @Test
    public void testGetAveragePlayingTimeByCategoryNoArgument() {
        var analyzer = new BoardGamesStatisticsAnalyzer(MY_GAMES);

        Map<String, Double> categoryAverage = analyzer.getAveragePlayingTimeByCategory();

        assertEquals(AVERAGE_PLAYTIME_ADVENTURE, categoryAverage.get("Adventure"));
        assertEquals(AVERAGE_PLAYTIME_CARD_GAME, categoryAverage.get("Card Game"));
        assertEquals(AVERAGE_PLAYTIME_FANTASY, categoryAverage.get("Fantasy"));
        assertEquals(AVERAGE_PLAYTIME_HORROR, categoryAverage.get("Horror"));
    }

    @Test
    public void testGetAveragePlayingTimeByCategory() {
        var analyzer = new BoardGamesStatisticsAnalyzer(MY_GAMES);

        assertThrows(IllegalArgumentException.class, () -> analyzer.getAveragePlayingTimeByCategory(null));
    }

    @Test
    public void testGetAveragePlayingTimeByCategoryEmptyCategory() {
        var analyzer = new BoardGamesStatisticsAnalyzer(MY_GAMES);

        assertThrows(IllegalArgumentException.class, () -> analyzer.getAveragePlayingTimeByCategory(""));
    }

    @Test
    public void testGetNMostPopularCategoriesNegativeN() {
        var analyzer = new BoardGamesStatisticsAnalyzer(MY_GAMES);

        assertThrows(IllegalArgumentException.class, () -> analyzer.getNMostPopularCategories(-1));
    }
}
