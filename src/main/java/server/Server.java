package server;

import http.Request;
import http.ResponseWriter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class Server implements AutoCloseable {
    private final Router router;
    private final ServerSocket serverSocket;
    private final String contentPath;

    public Server(Router router, int port, String contentPath) throws IOException {
        this.router = router;
        this.serverSocket = new ServerSocket(port);
        this.contentPath = contentPath;
        this.serverSocket.setReuseAddress(true);
    }

    public void start() throws IOException, InterruptedException {
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            handleSocket(socket);
        }
    }

    private void handleSocket(Socket socket) {
        CompletableFuture.runAsync(() -> {
            try (var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                var request = Request.fromReader(reader);
                var handler = router.get(request.path(), request.method());
                var rw = new ResponseWriter(socket.getOutputStream());

                handler.handle(request, rw);
                rw.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void close() throws Exception {
        if (!serverSocket.isClosed()) {
            serverSocket.close();
        }
    }
}
