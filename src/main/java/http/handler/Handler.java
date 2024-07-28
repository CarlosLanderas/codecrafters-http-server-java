package http.handler;

import http.Request;
import http.ResponseWriter;

import java.io.IOException;

public interface Handler {
    void handle(Request request, ResponseWriter writer) throws IOException;
}