package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class BoardGamesRecommender implements Recommender {
    private static final String WORD_INDEX_DELIMITER = ": ";
    private static final String INDEX_DELIMITER = ", ";


    private final List<BoardGame> boardGames;
    private final Set<String> stopWords;
    private final Map<String, Set<Integer>> keywordIndexLowerCase;
    private final Map<String, Set<Integer>> keywordIndexOriginal;

    {
        boardGames = new ArrayList<>();
        stopWords = new HashSet<>();
        keywordIndexLowerCase = new HashMap<>();
        keywordIndexOriginal = new HashMap<>();
    }

    private void readFromReader(Reader data, boolean game) {
        try {
            var br = new BufferedReader(data);

            if (game) {
                br.readLine(); // skip header
            }

            for (String line; (line = br.readLine()) != null; ) {
                if (game) {
                    boardGames.add(BoardGame.of(line));
                } else {
                    stopWords.add(line.toLowerCase());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from reader");
        }
    }

    private void extractGames(Path datasetZipFile, String datasetFileName) {
        try (var zf = new ZipFile(String.valueOf(datasetZipFile.toAbsolutePath()))) {
            for (var it = zf.entries().asIterator(); it.hasNext(); ) {
                ZipEntry entry = it.next();

                if (entry.getName().equals(datasetFileName) && !entry.isDirectory()) {
                    readFromReader(new InputStreamReader(zf.getInputStream(entry)), true);
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to read from file %s", datasetFileName));
        }
    }

    private void extractStopWords(Path stopWordsFile) {
        try (BufferedReader bufferedReader = Files.newBufferedReader(stopWordsFile)) {
            for (String word; (word = bufferedReader.readLine()) != null; ) {
                stopWords.add(word.toLowerCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("Failed to read from file %s", stopWordsFile.getFileName()));
        }
    }

    private void createIndex() {
        boardGames.stream()
                .map(BoardGame::index)
                .forEach(s -> s.fillIndex(keywordIndexOriginal, stopWords, false));

        boardGames.stream()
                .map(BoardGame::index)
                .forEach(s -> s.fillIndex(keywordIndexLowerCase, stopWords, true));
    }

    /**
     * Constructs an instance using the provided file names.
     *
     * @param datasetZipFile  ZIP file containing the board games dataset file
     * @param datasetFileName the name of the dataset file (inside the ZIP archive)
     * @param stopwordsFile   the stopwords file
     */
    public BoardGamesRecommender(Path datasetZipFile, String datasetFileName, Path stopwordsFile) {
        extractGames(datasetZipFile, datasetFileName);
        extractStopWords(stopwordsFile);
        createIndex();
    }

    /**
     * Constructs an instance using the provided Reader streams.
     *
     * @param dataset   Reader from which the dataset can be read
     * @param stopwords Reader from which the stopwords list can be read
     */
    public BoardGamesRecommender(Reader dataset, Reader stopwords) {
        readFromReader(dataset, true);
        readFromReader(stopwords, false);
        createIndex();
    }

    @Override
    public Collection<BoardGame> getGames() {
        return List.copyOf(boardGames);
    }

    @Override
    public List<BoardGame> getSimilarTo(BoardGame game, int n) {
        if (game == null || n < 0) {
            throw new IllegalArgumentException();
        }

        if (n == 0) {
            return List.of();
        }

        return boardGames.stream()
                .filter(g -> g.atLeastOneCommonCategory(game) && !g.equals(game))
                .sorted(Comparator.comparingDouble(g -> g.distance(game, g)))
                .limit(n)
                .toList();
    }

    @Override
    public List<BoardGame> getByDescription(String... keywords) {
        if (keywords == null) {
            throw new IllegalArgumentException("Invalid keywords arg");
        }

        Set<String> filteredKeyWords = Arrays.stream(keywords)
                .filter(w -> w != null && !w.isEmpty() && !stopWords.contains(w.toLowerCase()))
                .collect(Collectors.toSet());


        return boardGames.stream()
                .filter(g -> g.keyWordsMatchCount(filteredKeyWords, keywordIndexLowerCase) < 0)
                .sorted(Comparator.comparingInt(g -> g.keyWordsMatchCount(filteredKeyWords, keywordIndexLowerCase)))
                .toList();
    }

    @Override
    public void storeGamesIndex(Writer writer) {
        var bw = new BufferedWriter(writer);
        keywordIndexOriginal.forEach((k, v) -> {
            try {
                bw.write(k + WORD_INDEX_DELIMITER + v.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(INDEX_DELIMITER))
                );
                bw.write(System.lineSeparator());
                bw.flush();
            } catch (IOException e) {
                throw new RuntimeException("Failed to write to writer");
            }
        });
    }
}
