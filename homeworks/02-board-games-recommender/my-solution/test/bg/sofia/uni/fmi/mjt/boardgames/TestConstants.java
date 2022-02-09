package bg.sofia.uni.fmi.mjt.boardgames;

import java.io.Reader;
import java.io.StringReader;
import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestConstants {
    protected final String FILE_SEP = FileSystems.getDefault().getSeparator();

    protected final String CURRENT_DIR = ".";
    protected final String TEST_DIR = "_testDir";
    protected final String TEST_FILE_ZIP = "_myGames.zip";
    protected final String TEST_FILE = "_myGames.csv";

    protected final String STOP_WORDS_FILE = "_stopWords.txt";
    protected final String INDEX_FILE = "_index.txt";

    protected final String FILE_PATH = CURRENT_DIR + FILE_SEP + TEST_DIR + FILE_SEP + TEST_FILE;
    protected final String ZIP_FILE_PATH = CURRENT_DIR + FILE_SEP + TEST_DIR + FILE_SEP + TEST_FILE_ZIP;

    protected final String STOP_WORDS_FILE_PATH = CURRENT_DIR + FILE_SEP + TEST_DIR + FILE_SEP + STOP_WORDS_FILE;
    protected final String INDEX_FILE_PATH = CURRENT_DIR + FILE_SEP + TEST_DIR + FILE_SEP + INDEX_FILE;

    protected final String[] STOP_WORDS = {"a", "an", "Any", "as", "be", /* "it's", */ "no", "you"};

    protected final String[] GAMES_AS_STRING = {
            "id;maxPlayers;minAge;minPlayers;name;playingTime;category;mechanic;desc",
            "1;5;14;3;Die Matcher;240;Economic,Negotiation,Adventure;Area Control,Dice Rolling;as k0-k1,NO k2 any",
            "2;4;12;3;DragonMaster;30;Adventure,Card Game,Fantasy;Trick-taking;k1-k2 be ANy",
            "31;5;10;2;Dark World;90;Fantasy;Dice Rolling,Grid Movement;You K4..ANY.",
            "33;8;12;1;Arkham Horror;180;Adventure,Horror;Co-operative Play;K0'k2, An.. anY"
    };

    protected final List<BoardGame> MY_GAMES = Stream.of(GAMES_AS_STRING)
            .skip(1)
            .map(BoardGame::of)
            .toList();

    protected final String KEYWORD_INDEX_SEP = ": ";
    protected final String INDEX_SEP = ", ";

    protected final Reader DATASET_READER = new StringReader(
            Arrays.stream(GAMES_AS_STRING).collect(Collectors.joining(System.lineSeparator()))
    );

    protected final Reader STOP_WORDS_READER = new StringReader(
            Arrays.stream(STOP_WORDS).collect(Collectors.joining(System.lineSeparator()))
    );

    protected final int BIG_TEST_NUMBER = 100;

    protected final String INVALID_KEYWORD = null;
    protected final String NON_EXISTING_KEYWORD = "NON-EXISTING KEY";
    protected final String NON_EXISTING_CATEGORY = "NON-EXISTING CATEGORY";

    protected final double AVERAGE_PLAYTIME_ADVENTURE = 150.0;
    protected final double AVERAGE_PLAYTIME_CARD_GAME = 30.0;
    protected final double AVERAGE_PLAYTIME_FANTASY = 60.0;
    protected final double AVERAGE_PLAYTIME_HORROR = 180.0;

    protected final double EPSILON = 0.00001; // delta
}
