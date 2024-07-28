package http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

        var requestParts = parts.get(0).split(" ");
        var method = Method.fromString(requestParts[0]);
        var path = requestParts[1];
        var protocol = requestParts[2];

        return new Request(path, method, protocol, Map.of());
    }
}
