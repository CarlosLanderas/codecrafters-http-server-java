package server;

import http.Request;

import java.io.*;
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

    public void start() throws IOException, InterruptedException {
        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            handleSocket(socket);
        }
    }

    private void handleSocket(Socket socket) throws InterruptedException {
        CompletableFuture.runAsync(() -> {
          try(var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
              var request = Request.fromReader(reader);
              var out = socket.getOutputStream();

              if (request.path().equals("/")) {
                  out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8));
              } else {
                  out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes(StandardCharsets.UTF_8));
              }

              out.flush();
              out.close();

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
