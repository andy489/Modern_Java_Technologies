package bg.sofia.uni.fmi.mjt.cocktail.server;

import bg.sofia.uni.fmi.mjt.cocktail.server.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.cocktail.server.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.DefaultCocktailStorage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class CocktailServer {
    private static final String HOST = "localhost";
    private static final int PORT = 4444;
    private static final int BUFFER_SIZE = 1024;

    private final CommandExecutor commandExecutor;

    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    public CocktailServer(int port, CommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
    }

//    public static void main(String... args) {
//        new CocktailServer(PORT, new CommandExecutor(new DefaultCocktailStorage())).start();
//    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()

        ) {
            selector = Selector.open();

            configureServerSocketChannel(serverSocketChannel, selector);

            buffer = ByteBuffer.allocate(BUFFER_SIZE);

            isServerWorking = true;

            System.out.println("Cocktail server started!");

            while (isServerWorking) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        accept(selector, key);
                    } else if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        String clientInput = getClientInput(clientChannel);

                        if (clientInput == null) {
                            continue;
                        }

                        String output = commandExecutor.execute(CommandCreator.newCommand(clientInput.toLowerCase()));
                        writeClientOutput(clientChannel, output);
                    }

                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the server socket", e);
        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];

        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put((output + System.lineSeparator()).getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }
}