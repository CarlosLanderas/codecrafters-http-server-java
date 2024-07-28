package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public record Request(
        String path,
        Method method,
        String protocol,
        byte[] body,
        Map<String, String> headers) {

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

        byte[] body = null;

        if (headers.containsKey("Content-Length")) {
            var length = Integer.parseInt(headers.get("Content-Length"));
            char[] charBuffer = new char[length];
            reader.read(charBuffer, 0, length);
            body = new String(charBuffer).getBytes(StandardCharsets.UTF_8);
        }

        return new Request(path, method, protocol, body, headers);
    }

    public Optional<String> getHeader(String name) {
        if (headers.containsKey(name)) {
            return Optional.of(headers.get(name));
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
}
