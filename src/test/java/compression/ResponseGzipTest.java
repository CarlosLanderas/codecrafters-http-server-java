package compression;

import http.Method;
import http.Request;
import http.Response;
import http.ResponseWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.GZIPInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponseGzipTest {

    @Test
    void testGzipResponse() throws IOException {
        var contents = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        var response = Response
                .ok(contents.getBytes())
                .setContentType("text/plain");
        var responseLength = response.contentLength();
        GzipEncoding.encodeResponse(response);
        var encodedLength = response.contentLength();

        assertTrue(responseLength > encodedLength);
        assertEquals(contents, (decodePayload(response.body())));
    }

    String decodePayload(byte[] payload) throws IOException {
        var ba = new ByteArrayInputStream(payload);
        try (var gzip = new GZIPInputStream(ba)) {
            return new String(gzip.readAllBytes());
        }
    }
}
