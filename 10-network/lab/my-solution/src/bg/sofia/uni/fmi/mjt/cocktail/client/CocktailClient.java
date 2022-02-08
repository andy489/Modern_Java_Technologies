package bg.sofia.uni.fmi.mjt.cocktail.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class CocktailClient {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 4444;

    private final Reader abstractReader;

    public CocktailClient(Reader abstractReader) {
        this.abstractReader = abstractReader;
    }

    public void start() {
        try (var socketChannel = SocketChannel.open();
             var reader = new BufferedReader(Channels.newReader(socketChannel, StandardCharsets.UTF_8));
             var writer = new PrintWriter(Channels.newWriter(socketChannel, StandardCharsets.UTF_8), true);
             BufferedReader br = new BufferedReader(abstractReader)
        ) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the server.");

            String line;
            while ((line = br.readLine()) != null && !line.equals("disconnect")) {
                writer.println(line);

                String reply = reader.readLine();
                System.out.println(reply);
            }

            System.out.println("~Disconnected");
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}
