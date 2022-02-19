package bg.sofia.uni.fmi.mjt.battleships.online.server.storage;

import bg.sofia.uni.fmi.mjt.battleships.online.server.game.SavedGame;

import java.util.function.Consumer;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.BLANKS_AFTER_GAME_PLAYERS;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.CELLS_GAME_STATUS;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.CELLS_PER_DIM;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED_SEP;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.TEN_DASH;

public class ListGamesViewHelper {
    public static StringBuilder getHeader() {
        return new StringBuilder(RED_SEP).append(" NAME     ")
                .append(RED_SEP).append(" CREATOR  ")
                .append(RED_SEP).append(" STATUS       ")
                .append(RED_SEP).append(" PLAYERS  ")
                .append(RED_SEP).append(System.lineSeparator()).append(RED + "> |" + DEFAULT)
                .append(TEN_DASH).append("+").append(TEN_DASH)
                .append("+").append(TEN_DASH + "----").append("+").append(TEN_DASH)
                .append(RED_SEP).append(System.lineSeparator());
    }

    public static Consumer<UserIn> getCurrentGamesListConsumer(StringBuilder sb) {
        return u -> sb.append(RED + "> |" + DEFAULT).append(" ")
                .append(u.getCurrGame().getGameName())
                .append(blanks(CELLS_PER_DIM - u.getCurrGame().getGameName().length() - 1))
                .append(RED_SEP).append(" ")
                .append(u.getCurrGame().getHostName())
                .append(blanks(CELLS_PER_DIM - u.getCurrGame().getHostName().length() - 1))
                .append(RED_SEP).append(" ")
                .append(u.getCurrGame().getStatus())
                .append(blanks(CELLS_GAME_STATUS - u.getCurrGame().getStatus().len() - 1))
                .append(RED_SEP).append(" ").append(u.getCurrGame().numPlayers()).append("/2")
                .append(blanks(BLANKS_AFTER_GAME_PLAYERS)).append(RED_SEP).append(System.lineSeparator());
    }

    public static Consumer<SavedGame> getMySavedGamesListConsumer(StringBuilder sb) {
        return sg -> sb.append(RED + "> |" + DEFAULT).append(" ")
                .append(sg.getName())
                .append(blanks(CELLS_PER_DIM - sg.getName().length() - 1))
                .append(RED_SEP).append(" ")
                .append(sg.getWasThisPlayerHost() ? "host " : "guest")
                .append(blanks(CELLS_PER_DIM - "guest".length() - 1))
                .append(RED_SEP).append(" ")
                .append("saved")
                .append(blanks(CELLS_GAME_STATUS - "saved".length() - 1))
                .append(RED_SEP).append(" ").append("0").append("/2")
                .append(blanks(BLANKS_AFTER_GAME_PLAYERS)).append(RED_SEP).append(System.lineSeparator());
    }

    private static String blanks(int n) {
        n = Math.abs(n);

        String b = "";

        for (int i = 0; i < n; i++) {
            b = b.concat(" ");
        }

        return b;
    }
}
