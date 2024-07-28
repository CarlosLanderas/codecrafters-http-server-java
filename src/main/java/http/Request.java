package http;

import server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Request {
    private String protocol;
    private String path;
    private Method method;
    private byte[] body;
    private Map<String, String> headers = new HashMap<>();

    public static Request create(String path, Method method, String protocol) {
        return new Request()
                .withProtocol(protocol)
                .withPath(path)
                .withMethod(method);
    }

    public static Request fromReader(BufferedReader reader) throws IOException {
        var parts = new ArrayList<String>();
        String line;
        while (!(line = reader.readLine()).isEmpty()) {
            parts.add(line);
        }

        var requestParts = parts.getFirst().split(" ");
        var method = Method.fromString(requestParts[0]);
        var path = requestParts[1];
        var protocol = requestParts[2];
        var headers = parseHeaders(parts);

        return Request.create(path, method, protocol)
                .withHeaders(headers)
                .withBody(reader);
    }

    protected Request withBody(BufferedReader reader) throws IOException {
        var length = contentLength();
        if (length > 0) {
            this.body = parseBody(reader, length);
        }

        return this;
    }

    public Request withMethod(Method method) {
        this.method = method;
        return this;
    }

    public Request withPath(String path) {
        this.path = path;
        return this;
    }

    public Request withProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public Request withHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public String path() {
        return path;
    }

    public Method method() {
        return this.method;
    }

    public byte[] body() {
        return this.body;
    }

    public Optional<String> getHeader(String name) {
        if (headers.containsKey(name)) {
            return Optional.of(headers.get(name));
        }

        return Optional.empty();
    }

    public boolean hasHeader(String name) {
        return headers.containsKey(name);
    }

    public int contentLength() {
        if (hasHeader("Content-Length")) {
            return Integer.parseInt(headers.get("Content-Length"));
        }

        return 0;
    }

    public Optional<String> acceptEncoding() {
        if (hasHeader("Accept-Encoding")) {
            var headerEncodings = Arrays.stream(headers.get("Accept-Encoding")
                            .split(","))
                    .map(String::trim)
                    .toList();

            for (var encoding : headerEncodings) {
                if (Server.supportedEncodings.contains(encoding)) { // We return the first supported encoding on the list
                    return Optional.of(encoding);
                }
            }
        }

        return Optional.empty();
    }

    public String[] pathSegments() {
        return path.split("/");
    }

    public String lastSegment() {
        var segments = pathSegments();
        return segments[segments.length - 1];
    }

    private static Map<String, String> parseHeaders(ArrayList<String> parts) {
        var headers = parts.subList(1, parts.size()).stream();
        return headers
                .map(h -> h.split(":"))
                .collect(Collectors.toMap(h -> h[0], h -> h[1].trim()));
    }

    private static byte[] parseBody(BufferedReader reader, int contentLength) throws IOException {
        char[] charBuffer = new char[contentLength];
        reader.read(charBuffer, 0, contentLength);
        return new String(charBuffer).getBytes(StandardCharsets.UTF_8);
    }
}
