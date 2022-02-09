package bg.sofia.uni.fmi.mjt.boardgames;

import bg.sofia.uni.fmi.mjt.boardgames.recommender.Stats;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record BoardGame(int id, String name,
                        String description,
                        int maxPlayers,
                        int minAge,
                        int minPlayers,
                        int currPlayingTimeMinutes,
                        Collection<String> categories,
                        Collection<String> mechanics) {

    private static final String ATTRIBUTES_DELIMITER = ";";
    private static final String INNER_DELIMITER = ",";

    private static final int ID_INDEX = 0;
    private static final int MAX_PLAYERS_INDEX = 1;
    private static final int MIN_AGE_INDEX = 2;
    private static final int MIN_PLAYERS_INDEX = 3;
    private static final int NAME_INDEX = 4;
    private static final int PLAYING_TIME_INDEX = 5;
    private static final int CATEGORIES_INDEX = 6;
    private static final int MECHANICS_INDEX = 7;
    private static final int DESCRIPTION_INDEX = 8;

    public static BoardGame of(String line) {
        String[] features = line.split(ATTRIBUTES_DELIMITER);

        int currId = Integer.parseInt(features[ID_INDEX]);
        int currMaxPlayers = (int) Double.parseDouble(features[MAX_PLAYERS_INDEX]);
        int currMinAge = (int) Double.parseDouble(features[MIN_AGE_INDEX]);
        int currMinPlayers = (int) Double.parseDouble(features[MIN_PLAYERS_INDEX]);
        int currPlayingTimeMinutes = (int) Double.parseDouble(features[PLAYING_TIME_INDEX]);

        String currName = features[NAME_INDEX];

        String currDescription = features[DESCRIPTION_INDEX];

        Set<String> currCategories = Arrays.stream(features[CATEGORIES_INDEX].split(INNER_DELIMITER))
                .collect(Collectors.toSet());

        Set<String> currMechanics = Arrays.stream(features[MECHANICS_INDEX].split(INNER_DELIMITER))
                .collect(Collectors.toSet());

        return new BoardGame(
                currId,
                currName,
                currDescription,
                currMaxPlayers,
                currMinAge,
                currMinPlayers,
                currPlayingTimeMinutes,
                currCategories,
                currMechanics
        );
    }

    public static Stats index(BoardGame boardGame) {
        return new Stats(boardGame.id(), boardGame.description());
    }

    public static double euclidDistance(BoardGame left, BoardGame right) {
        return Math.sqrt(Math.pow(left.currPlayingTimeMinutes() - right.currPlayingTimeMinutes(), 2) +
                Math.pow(left.maxPlayers() - right.maxPlayers(), 2) +
                Math.pow(left.minAge() - right.minAge(), 2) +
                Math.pow(left.minPlayers() - right.minPlayers(), 2));
    }

    public static int typeDist(Collection<String> left, Collection<String> right) {
        Set<String> unionSet = new HashSet<>();
        unionSet.addAll(left);
        unionSet.addAll(right);

        int union = unionSet.size();

        int intersection = (int) left.stream()
                .filter(right::contains)
                .count();

        return union - intersection;
    }

    public boolean atLeastOneCommonCategory(BoardGame game) {
        return this.categories().stream().anyMatch(g -> game.categories().contains(g));
    }

    public double distance(BoardGame left, BoardGame right) {
        double euclidMetric = euclidDistance(left, right);
        double categoryMetric = typeDist(left.categories(), right.categories());
        double mechanicMetric = typeDist(left.mechanics(), right.mechanics());

        return euclidMetric + categoryMetric + mechanicMetric;
    }

    public int keyWordsMatchCount(Set<String> filteredKeyWords, Map<String, Set<Integer>> keyWordIndex) {
        int wordMatches = 0;

        for (String word : filteredKeyWords) {
            String wordToLower = word.toLowerCase();
            if (keyWordIndex.containsKey(wordToLower) && keyWordIndex.get(wordToLower).contains(this.id())) {
                wordMatches++;
            }
        }

        return -wordMatches;
    }
}