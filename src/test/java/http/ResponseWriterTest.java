package http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseWriterTest {

    @Test
    void test() throws IOException {
        var content = "These are the magic http contents";
        var str = new ByteArrayOutputStream();
        var rw = new ResponseWriter(str, Request.create("/", Method.GET, "HTTP/1.1"));
        var response = Response.ok(content.getBytes());
        var expected = "HTTP/1.1 200 OK\r\nContent-Length: 33\r\n\r\nThese are the magic http contents";

        rw.write(response);

        assertEquals(expected, str.toString());
    }
}
