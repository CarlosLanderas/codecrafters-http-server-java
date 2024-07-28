package http;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {
    private int statusCode;
    private byte[] body;
    private String contentType = "";
    private int contentLength;

    public static Response ok(byte[] body) {
        return response(200, body);
    }

    public static Response notFound() {
        return response(404, null);
    }

    public String bodyContent() {
        return body == null ? "" : new String(body, StandardCharsets.UTF_8);
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] render() {
        var response = String.format(
                "HTTP/1.1 %d %s\r\nContent-Type: %s\r\nContent-Length: %d\r\n\r\n%s",
                statusCode,
                statusName(),
                contentType,
                contentLength,
                bodyContent());

        return response.getBytes(StandardCharsets.UTF_8);
    }

    public void writeTo(ResponseWriter writer) throws IOException {
        writer.write(render());
    }

    private static Response response(int statusCode, byte[] body) {

        var response = new Response();
        response.statusCode = statusCode;
        response.body = body;

        if (body != null) {
            response.contentLength = body.length;
        }

        return response;
    }

    private String statusName() {
        return Status.fromCode(statusCode).getName();
    }
}
