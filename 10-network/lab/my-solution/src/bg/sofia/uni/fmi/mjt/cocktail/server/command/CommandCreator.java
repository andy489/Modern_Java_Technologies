package bg.sofia.uni.fmi.mjt.cocktail.server.command;

import java.util.ArrayList;
import java.util.List;

public class CommandCreator {
    private static List<String> getCommandArguments(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == ' ') {
                tokens.add(sb.toString());
                sb.delete(0, sb.length());
            } else if (c != '\n' && c != '\r') {
                sb.append(c);
            }
        }
        tokens.add(sb.toString());

        return tokens;
    }

    public static Command newCommand(String clientInput) {
        List<String> tokens = CommandCreator.getCommandArguments(clientInput);
        String[] args = tokens.subList(1, tokens.size()).toArray(new String[0]);

        return new Command(tokens.get(0), args);
    }
}
