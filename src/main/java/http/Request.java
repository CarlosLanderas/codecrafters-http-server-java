package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public record Request(
        String path,
        Method method,
        String protocol,
        Map<String, Object> headers) {

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

        return new Request(path, method, protocol, Map.of());
    }

    public String[] pathSegments() {
        return path.split("/");
    }

    public String lastSegment() {
        var segments = pathSegments();
        return segments[segments.length - 1];
    }
}
