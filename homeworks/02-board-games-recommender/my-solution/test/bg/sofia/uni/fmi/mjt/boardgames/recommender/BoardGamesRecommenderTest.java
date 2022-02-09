package bg.sofia.uni.fmi.mjt.boardgames.recommender;

import bg.sofia.uni.fmi.mjt.boardgames.BoardGame;
import bg.sofia.uni.fmi.mjt.boardgames.TestConstants;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BoardGamesRecommenderTest extends TestConstants {

    protected static void writeToFile(Path filePath, String game) {
        try (var bufferedWriter = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
            bufferedWriter.write(game);
            bufferedWriter.write(System.lineSeparator());
            bufferedWriter.flush();
        } catch (IOException e) {
            String msg = String.format("Could not write to file %s", filePath.getFileName());
            throw new IllegalStateException(msg, e);
        }
    }

    protected static void zipAFile(String filePath, String zipFilePath) {
        var fileToBeZipped = Path.of(filePath);

        try (ZipOutputStream zippedOutput = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            zippedOutput.putNextEntry(new ZipEntry(fileToBeZipped.getFileName().toString()));

            byte[] bytes = Files.readAllBytes(fileToBeZipped);
            zippedOutput.write(bytes, 0, bytes.length);
            zippedOutput.closeEntry();
        } catch (FileNotFoundException e) {
            System.err.format("The file %s does not exist", filePath);
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("I/O error: " + e);
            throw new RuntimeException(e);
        }
    }

    private void deleteTestDirRecursively(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (final File f : files) {
                deleteTestDirRecursively(f);
            }
        }

        dir.delete();
    }

    @BeforeEach
    public void setUp() {
        File testDir, myFile, myStopWords;

        deleteTestDirRecursively((Paths.get(TEST_DIR).toAbsolutePath().toFile()));

        try {
            testDir = new File(TEST_DIR);
            myFile = new File(FILE_PATH);
            myStopWords = new File(STOP_WORDS_FILE_PATH);

            assertTrue(testDir.mkdirs());
            assertTrue(myFile.createNewFile());
            assertTrue(myStopWords.createNewFile());
        } catch (IOException e) {
            throw new RuntimeException("~Failed to create dir or file");
        }

        for (String game : GAMES_AS_STRING) {
            writeToFile(Path.of(myFile.getPath()), game);
        }

        for (String stopWord : STOP_WORDS) {
            writeToFile(Path.of(myStopWords.getPath()), stopWord.toLowerCase());
        }

        zipAFile(FILE_PATH, ZIP_FILE_PATH);
        assertTrue(myFile.delete());
    }

    @Test
    public void testBoardGameRecommenderConstructorWithPathsGetGames() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        assertTrue(recommender.getGames().containsAll(MY_GAMES));
    }

    @Test
    public void testBoardGameRecommenderConstructorWithReadersGetGames() {
        var recommender = new BoardGamesRecommender(DATASET_READER, STOP_WORDS_READER);

        assertTrue(recommender.getGames().containsAll(MY_GAMES));
    }

    @Test
    public void testBoardGameRecommenderInvalidReader() {
        assertThrows(RuntimeException.class, () -> new BoardGamesRecommender(null, STOP_WORDS_READER));
    }

    @Test
    public void testGetGamesUnmodifiableList() {
        var recommender = new BoardGamesRecommender(DATASET_READER, STOP_WORDS_READER);

        assertThrows(UnsupportedOperationException.class, () -> recommender.getGames().clear());
    }

    @Test
    public void testBoardGameGetSimilarToNullGame() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> recommender.getSimilarTo(null, 1)
        );
    }

    @Test
    public void testGetSimilarToValidN() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        BoardGame someGame = MY_GAMES.stream().findAny().orElse(null);

        List<BoardGame> similarGamesTo = recommender.getSimilarTo(someGame, 0);
        assertTrue(similarGamesTo.isEmpty());

        similarGamesTo = recommender.getSimilarTo(someGame, BIG_TEST_NUMBER);
        // one game does not share common category with the matched game
        assertTrue(similarGamesTo.size() < MY_GAMES.size());
    }

    @Test
    public void testGetSimilarTo() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        BoardGame firstGame = MY_GAMES.stream().findFirst().orElse(null);

        List<BoardGame> similarGamesTo = recommender.getSimilarTo(firstGame, 1);
        assertEquals(1, similarGamesTo.size());
        assertFalse(similarGamesTo.contains(firstGame));

        similarGamesTo = recommender.getSimilarTo(firstGame, 2);
        assertEquals(2, similarGamesTo.size());
        assertEquals(similarGamesTo.get(0), MY_GAMES.get(3));
        assertEquals(similarGamesTo.get(1), MY_GAMES.get(1));

        similarGamesTo = recommender.getSimilarTo(firstGame, 3);
        assertEquals(2, similarGamesTo.size());
        assertEquals(similarGamesTo.get(0), MY_GAMES.get(3));
        assertEquals(similarGamesTo.get(1), MY_GAMES.get(1));

        similarGamesTo = recommender.getSimilarTo(firstGame, BIG_TEST_NUMBER);

        // Again same size 2, because one of the four games DOES NOT
        // share common category with the first game we match,
        // and we do not include the searched game
        assertEquals(2, similarGamesTo.size());
    }

    @Test
    public void testGetByDescriptionNoKeywords() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        List<BoardGame> descriptionSimGames = recommender.getByDescription();
        assertTrue(descriptionSimGames.isEmpty());
    }

    @Test
    public void testGetByDescriptionNullKeywordAndEmptyKeywords() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        assertTrue(recommender.getByDescription("", INVALID_KEYWORD).isEmpty());
    }

    @Test
    public void testGetByDescriptionNoMatchKeywords() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        assertTrue(recommender.getByDescription(NON_EXISTING_KEYWORD).isEmpty());
    }

    @Test
    public void testGetByDescriptionVariousKeywords() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        List<BoardGame> descriptionSimGames = recommender.getByDescription("AN", "K0"/*, "it's"*/);

        assertEquals(2, descriptionSimGames.size());

        assertTrue(descriptionSimGames.containsAll(
                MY_GAMES.stream()
                        .filter(g -> g.id() == 1 || g.id() == 33) // games with id 1 and 33 have keyword k0 (or KO)
                        .toList()
        ));

        assertThrows(UnsupportedOperationException.class, descriptionSimGames::clear); // unmodifiable list

        descriptionSimGames = recommender.getByDescription("as", "k4", "YoU");

        assertEquals(1, descriptionSimGames.size());
        assertTrue(descriptionSimGames.contains(MY_GAMES.get(2)));

        descriptionSimGames = recommender.getByDescription("A", "k2", "k0");

        assertEquals(3, descriptionSimGames.size());
        assertTrue(descriptionSimGames.containsAll(
                        MY_GAMES.stream()
                                .filter(g -> g.id() == 1 || g.id() == 33 || g.id() == 2)
                                .toList()
                )
        );

        assertEquals(MY_GAMES.get(1), descriptionSimGames.get(2));
    }

    @Test
    public void testStoreGamesIndex() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        try {
            recommender.storeGamesIndex(Files.newBufferedWriter(Path.of(INDEX_FILE_PATH)));
        } catch (IOException e) {
            String msg = String.format("~Failed to write to %s", INDEX_FILE_PATH);
            throw new RuntimeException(msg, e);
        }

        int iterator = 0;

        List<List<String>> indexesCheck = new ArrayList<>();

        for (int i = 0; i < 5; ++i) {
            indexesCheck.add(new ArrayList<>());
        }

        indexesCheck.get(0).addAll(List.of("1"));
        indexesCheck.get(1).addAll(List.of("33"));
        indexesCheck.get(2).addAll(List.of("1", "2"));
        indexesCheck.get(3).addAll(List.of("1", "33", "2"));
        indexesCheck.get(4).add("31");

        try (var br = Files.newBufferedReader(Paths.get(INDEX_FILE_PATH))) {
            for (String line; (line = br.readLine()) != null; ) {
                List<String> indexes = List.of(
                        line.split(KEYWORD_INDEX_SEP)[1]
                                .split(INDEX_SEP)
                );

                assertTrue(indexes.containsAll(indexesCheck.get(iterator)));

                iterator++;
            }
        } catch (IOException e) {
            throw new RuntimeException(String.format("~Failed to read from to %s", INDEX_FILE_PATH));
        }
    }

    @Test
    public void testStoreGameIndexInvalidWriter() {
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        assertThrows(RuntimeException.class, () -> recommender.storeGamesIndex(null));
    }

    @Test
    public void testStoreGamesIndexSize(){
        var recommender = new BoardGamesRecommender(
                Paths.get(ZIP_FILE_PATH),
                TEST_FILE,
                Paths.get(STOP_WORDS_FILE_PATH)
        );

        Writer writer = new StringWriter(100);

        recommender.storeGamesIndex(writer);

        assertEquals(42, writer.toString().length(), "index size should be 42");
    }

    @AfterEach
    public void cleanCreatedFilesAndFolders() {
        deleteTestDirRecursively(Paths.get(TEST_DIR).toAbsolutePath().toFile());
    }
}
