package server;

import http.Method;
import http.Request;
import http.Response;
import http.ResponseWriter;
import http.handler.Handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

record PathMethod(Pattern path, Method method) {
    public static PathMethod of(Pattern pattern, Method method) {
        return new PathMethod(pattern, method);
    }
}

public class Router {
    private final Map<PathMethod, Handler> handlers = new HashMap<>();

    public void register(String path, Method method, Handler handler) {
        var pattern = Pattern.compile(path);
        handlers.put(PathMethod.of(pattern, method), handler);
    }

    public Handler get(String path, Method method) {
        for (var k : handlers.keySet()) {
            if (k.path().matcher(path).matches() && k.method().equals(method)) {
                return handlers.get(k);
            }
        }

        // If no handler is registered for path we return a default not found impl
        return new NotFoundHandler();
    }
}

class NotFoundHandler implements Handler {
    @Override
    public void handle(Request request, ResponseWriter writer) throws IOException {
        Response.notFound().writeTo(writer);
    }
}
