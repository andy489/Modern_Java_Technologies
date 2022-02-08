package bg.sofia.uni.fmi.mjt.cocktail.server.server;

import bg.sofia.uni.fmi.mjt.cocktail.client.CocktailClient;
import bg.sofia.uni.fmi.mjt.cocktail.server.CocktailServer;
import bg.sofia.uni.fmi.mjt.cocktail.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.DefaultCocktailStorage;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;

public class CocktailServerTest {

    private static final int PORT = 4444;

    @Test
    public void testNotAUnitTest() throws InterruptedException {
        CocktailServer server = new CocktailServer(PORT, new CommandExecutor(new DefaultCocktailStorage()));

        Thread startThread = new Thread(new StartStopServer(server, true));
        startThread.start();

        Thread.sleep(1000);

        ClientRunnable client = new ClientRunnable(
                new StringReader(
                "create mojito vodka=100ml" + System.lineSeparator() +
                        "get all" + System.lineSeparator() +
                        "disconnect" + System.lineSeparator()
                )
        );

        Thread clientThread = new Thread(client);
        clientThread.start();

        Thread endThread = new Thread(new StartStopServer(server, false));
        endThread.start();
    }
}

record StartStopServer(CocktailServer server, boolean startStopFlag) implements Runnable {
    @Override
    public void run() {
        if (startStopFlag) {
            server.start();
        } else {
            server.stop();
        }
    }
}

record ClientRunnable(Reader abstractReader) implements Runnable {
    @Override
    public void run() {
        CocktailClient client = new CocktailClient(abstractReader);

        client.start();
    }
}


