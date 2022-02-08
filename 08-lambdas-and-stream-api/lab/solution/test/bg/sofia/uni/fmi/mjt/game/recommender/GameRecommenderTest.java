package bg.sofia.uni.fmi.mjt.game.recommender;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameRecommenderTest {
    private static GameRecommender recommender;
    public static ArrayList<Game> games;

    @BeforeAll
    public static void init() {
        final String[] GAMES_STR = new String[]{
                "header",
                "name0,platform1,01-Jan-2010,keyword1 keyword2 keyword3,90,90.0",
                "name1,platform1,02-Feb-2012,keyword5 keyword2 keyword4,91,91.1",
                "name2,platform2,03-Mar-2012,keyword1 keyword5 keyword6,92,92.2",
                "name3,platform2,04-Apr-2013,keyword3 keyword2 keyword7,93,93.3",
                "name4,platform9,05-May-2014,keyword4 keyword2 keyword3,94,94.4",
                "name5,platform4,06-Jun-2015,keyword1 keyword3 keyword4,95,95.5",
                "name6,platform4,07-Jul-2016,keyword1 keyword2 keyword5,96,96.6",
                "name7,platform3,08-Aug-2017,keyword6 keyword2 keyword3,97,97.7",
                "name8,platform3,09-Sep-2018,keyword1 keyword2 keyword9,98,98.8",
                "name9,platform3,10-Oct-2019,keyword1 keyword4 keyword7,99,99.9"
        };

        games = new ArrayList<>();

        for (int i = 1; i < GAMES_STR.length; ++i) {
            games.add(Game.of(GAMES_STR[i]));
        }

        Reader testReader = new StringReader(
                Stream.of(
                        GAMES_STR[0],
                        GAMES_STR[1], GAMES_STR[2], GAMES_STR[3], GAMES_STR[4], GAMES_STR[5],
                        GAMES_STR[6], GAMES_STR[7], GAMES_STR[8], GAMES_STR[9], GAMES_STR[10]
                ).collect(Collectors.joining(System.lineSeparator()))
        );

        Reader emptyTestReader = new StringReader("");

        recommender = new GameRecommender(testReader);
    }

    @Test
    public void testTopNUserRatedGames_HighN() {
        assertEquals(10, recommender.getTopNUserRatedGames(20).size(), "Games should be 10");
        assertTrue(recommender.getTopNUserRatedGames(20).containsAll(games), "Should contain all games");
    }

    @Test
    public void testTopScoringYears() {
        assertEquals(3, recommender.getYearsWithTopScoringGames(97).size(),
                "Games should be 3"
        );

        assertTrue(recommender.getYearsWithTopScoringGames(97).containsAll(List.of(2017, 2018, 2019)),
                "Should contain last three games with score >= 97"
        );
    }

    @Test
    public void testGameOf() {
        Game game = Game.of("name9,platform3,10-Oct-2019,keyword1 keyword4 keyword7,99,99.9");
        assertEquals(game, games.get(9), "Games should be equal");
    }

    @Test
    public void testGetNamesOfReleased_NoGames() {
        assertEquals(0, recommender.getAllNamesOfGamesReleasedIn(2009).length(),
                "Expected no games released in year 2009"
        );
    }

    @Test
    void testGetGamesSimilarTo() {
        assertEquals(2, recommender.getGamesSimilarTo("keyword5", "keyword2").size(),
                "Expected 2 games with keywords \"keyword5\" and \"keyword2\""
        );

        assertTrue(recommender.getGamesSimilarTo("keyword5", "keyword2")
                        .containsAll(List.of(games.get(1), games.get(6))),
                "Expected 2 games with keywords \"keyword5\" and \"keyword2\""
        );
    }

    @Test
    public void testGetYearsActive_NullPlatform() {
        assertEquals(0, recommender.getYearsActive(null),
                "Expected no active years for null platform"
        );
    }

    @Test
    public void testHighestUserRatedGame_NullPlatform() {
        assertThrows(NoSuchElementException.class, () -> recommender.getHighestUserRatedGameByPlatform(null),
                "Expected to throw NoSuchElementException when platform is null"
        );
    }

    @Test
    public void testGetGamesReleasedAfter() {
        assertEquals(2,
                recommender.getGamesReleasedAfter(LocalDate.of(2017, 12, 10)).size(),
                "Expected two games released after 2017-12-10"
        );

        assertTrue(
                recommender.getGamesReleasedAfter(LocalDate.of(2017, 12, 10))
                        .containsAll(List.of(games.get(8), games.get(9))),
                "Expected games 8 & 9"
        );
    }

    @Test
    public void testTopNUserRatedGames_ZeroN() {
        assertTrue(recommender.getTopNUserRatedGames(0).isEmpty(), "Expected empty list");
    }

    @Test
    public void testHighestUserRatedGameByPlatform() {
        assertEquals(games.get(6), recommender.getHighestUserRatedGameByPlatform("platform4"),
                "Expected game 6 to be the highest user rated game from platform 4"
        );
    }

    @Test
    public void testTopNUserRatedGames_NegativeN() {
        assertThrows(IllegalArgumentException.class, () -> recommender.getTopNUserRatedGames(-1),
                "Expected IllegalArgumentException when getTopNUsersRatedGames is called with -1"
        );
    }

    @Test
    public void testGetYearsActive_InvalidPlatform() {
        assertEquals(0, recommender.getYearsActive("no-platform"),
                "Expected 0 years active for invalid platform"
        );
    }

    @Test
    public void testGetYearsActive() {
        assertEquals(2, recommender.getYearsActive("platform4"),
                "Expected 3 active years for platform 4"
        );
    }

    @Test
    public void testGetGames_NoSimilarGames() {
        assertEquals(0, recommender.getGamesSimilarTo("no-key").size(),
                "Expected no similar games with keyword \"no-key\"");
    }

    @Test
    public void testGetTopNUserRatedGames() {
        assertEquals(1, recommender.getTopNUserRatedGames(1).size(),
                "Expected only one game"
        );

        assertTrue(recommender.getTopNUserRatedGames(1).contains(games.get(9)),
                "Expected game 9"
        );
    }

    @Test
    public void testGetAllGamesByPlatform() {
        Map<String, Set<Game>> gamesPlatform = recommender.getAllGamesByPlatform();

        assertEquals(5, gamesPlatform.size(), "Expected 4 platforms");

        assertEquals(gamesPlatform.keySet(), Set.of("platform1", "platform2", "platform3", "platform4", "platform9"),
                "Expected platforms 1-4 & 9");
    }

    @Test
    public void testGetGamesReleasedAfter_FutureDate() {
        assertTrue(recommender.getGamesReleasedAfter(LocalDate.now().plusDays(1)).isEmpty(),
                "Expected no games to be releasef with future date");
    }

    @Test
    public void testTopScoringYears_NegativeScore() {
        assertEquals(9, recommender.getYearsWithTopScoringGames(-1).size(),
                "Expected all years to be returned"
        );

        assertTrue(recommender.getYearsWithTopScoringGames(-1).contains(2010),
                "Expected 2010 to be returned");

        assertTrue(recommender.getYearsWithTopScoringGames(-1)
                        .containsAll(IntStream.range(2012, 2019).boxed().toList()),
                "Expected all years from 2012 to 2019 to be returned"
        );
    }

    @Test
    public void testHighestUserRatedGame_InvalidPlatform() {
        assertThrows(NoSuchElementException.class, () -> recommender.getHighestUserRatedGameByPlatform("no-platform"),
                "Expected NoSuchElementException when platform does not exists");
    }

    @Test
    public void testGetNamesOfReleased() {
        assertEquals(5,
                recommender.getGamesReleasedAfter(LocalDate.of(2014, 12, 10)).size(),
                "Expected 5 games released after 2014-12-10"
        );
    }

    @Test
    public void testGetYearsActive_OneGame() {
        assertEquals(1, recommender.getYearsActive("platform9"),
                "Expected only one year of activity for platform 9"
        );
    }

    @Test
    public void testTopScoringYears_HighScore() {
        assertTrue(recommender.getYearsWithTopScoringGames(94)
                        .containsAll(IntStream.range(2014, 2020).boxed().toList()),
                "Expected all years from 2014 to 2019");
    }

    @Test
    public void testGetGames_DuplicateKeywords() {
        assertEquals(3, recommender.getGamesSimilarTo("keyword1", "keyword2", "keyword1").size(),
                "Expected 3 games with \"keyword1\" and \"keyword2\"");

        assertTrue(recommender.getGamesSimilarTo("keyword1", "keyword2", "keyword1")
                        .containsAll(List.of(games.get(0), games.get(6), games.get(8))),
                "Expected games 0, 6 and 8 with keywords \"keyword1\" and \"keyword2\"");
    }
}