package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class Server implements AutoCloseable {
    private final ServerSocket serverSocket;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
    }

    public void Start() throws IOException, InterruptedException {
        while(!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            handleSocket(socket);
        }
    }

    private void handleSocket(Socket socket) throws InterruptedException {
        CompletableFuture.runAsync(() -> {
            try {
                socket.getOutputStream().write("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void close() throws Exception {
       if(!serverSocket.isClosed()) {
        serverSocket.close();
       }
    }
}
