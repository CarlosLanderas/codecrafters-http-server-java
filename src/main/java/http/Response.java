package http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static http.HeaderNames.*;

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

    public int contentLength() {
        return this.headers.containsKey(CONTENT_LENGTH) ?
                Integer.parseInt(this.headers.get(CONTENT_LENGTH)) :
                0;
    }

    public Response setContentType(String contentType) {
        this.headers.put(CONTENT_TYPE, contentType);
        return this;
    }

    public Response setContentEncoding(Optional<String> encoding) {
        encoding.ifPresent(e -> this.headers.put(CONTENT_ENCODING, e));
        return this;
    }

    public Response setContentLength(int length) {
        this.headers.put(CONTENT_LENGTH, Integer.toString(length));
        return this;
    }

    public Response setBody(byte[] body) {
        this.body = body;
        this.setContentLength(body.length);
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
                "HTTP/1.1 %d %s\r\n%s\r\n",
                statusCode,
                statusName(),
                headerBuilder).getBytes();

        var responseLength = response.length + (body == null ? 0 : body.length);
        var responseBytes = new byte[responseLength];

        System.arraycopy(response, 0, responseBytes, 0, response.length);

        if (body != null) {
            System.arraycopy(body, 0, responseBytes, response.length, body.length);
        }

        return responseBytes;
    }

    public void writeTo(ResponseWriter writer) throws IOException {
        writer.write(this);
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
