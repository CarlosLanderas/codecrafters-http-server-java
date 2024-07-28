package http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {

    @Test
    void test() {
        var content = "These are the response contents";
        var response = Response.ok(content.getBytes());
        var expected = "HTTP/1.1 200 OK\r\nContent-Type: \r\nContent-Length: 31\r\n\r\nThese are the response contents";

        assertEquals(expected, new String(response.render()));
    }

    @Test
    void testHeaders() throws IOException {

        var testRequest = "GET /test HTTP/1.1\r\nContent-Type: application/json\r\nContent-Length: 31\r\nUser-Agent: Mozilla\r\n\r\nThese are the response contents";
        var reader = new BufferedReader(new StringReader(testRequest));
        var request = Request.fromReader(reader);

        assertEquals("application/json", request.getHeader("Content-Type").get());
        assertEquals("Mozilla", request.getHeader("User-Agent").get());
        assertEquals("31", request.getHeader("Content-Length").get());
    }
}