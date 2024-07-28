package http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Response {
    private int statusCode;
    private byte[] body;
    private final Map<String, String> headers = new HashMap<>();

    public static Response ok(byte[] body) {
        return response(200, body);
    }

    public static Response created() {
        return response(201, null);
    }

    public static Response notFound() {
        return response(404, null);
    }

    public static Response error(String message) {
        return response(500, message.getBytes());
    }

    public byte[] body() {
        return body;
    }

    public String bodyString() {
        return body == null ? "" : new String(body, StandardCharsets.UTF_8);
    }

    public Response setContentType(String contentType) {
        this.headers.put("Content-Type", contentType);
        return this;
    }

    public Response setContentEncoding(Optional<String> encoding) {
        encoding.ifPresent(e -> this.headers.put("Content-Encoding", e));
        return this;
    }

    public Response setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public byte[] render() {

        StringBuilder headerBuilder = new StringBuilder();

        for (var header : headers.entrySet()) {
            headerBuilder
                    .append(header.getKey())
                    .append(": ").append(header.getValue()).append("\r\n");
        }

        var response = String.format(
                "HTTP/1.1 %d %s\r\n%s\r\n%s",
                statusCode,
                statusName(),
                headerBuilder,
                bodyString());

        return response.getBytes(StandardCharsets.UTF_8);
    }

    public void writeTo(ResponseWriter writer) throws IOException {
        writer.write(this);
    }

    private Response setContentLength(int length) {
        this.headers.put("Content-Length", Integer.toString(length));
        return this;
    }

    private static Response response(int statusCode, byte[] body) {

        var response = new Response();
        response.statusCode = statusCode;
        response.body = body;

        if (body != null) {
            response.setContentLength(body.length);
        }

        return response;
    }

    private String statusName() {
        return Status.fromCode(statusCode).getName();
    }
}
