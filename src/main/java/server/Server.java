package server;

import http.Request;
import http.ResponseWriter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Server implements AutoCloseable {
    private final Router router;
    private final ServerSocket serverSocket;

    public static final List<String> supportedEncodings = Collections.singletonList("gzip");

    public Server(Router router, int port) throws IOException {
        this.router = router;
        this.serverSocket = new ServerSocket(port);
        this.serverSocket.setReuseAddress(true);
    }

    public void start() throws IOException {
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
                var rw = new ResponseWriter(socket.getOutputStream(), request);

                handler.handle(request, rw);

                rw.close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
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
