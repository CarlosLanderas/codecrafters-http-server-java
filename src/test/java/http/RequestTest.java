package http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {
    @Test
    void test() throws IOException {

        var testRequest = "GET /test HTTP/1.1\r\nContent-Type: application/json\r\nContent-Length: 30\r\nUser-Agent: Mozilla\r\n\r\nThese are the request contents";
        var reader = new BufferedReader(new StringReader(testRequest));
        var request = Request.fromReader(reader);


        assertEquals(Method.GET, request.method());
        assertEquals("/test", request.path());
        assertEquals("These are the request contents", new String(request.body()));
        assertEquals("application/json", request.getHeader("Content-Type").get());
        assertEquals("Mozilla", request.getHeader("User-Agent").get());
        assertEquals("30", request.getHeader("Content-Length").get());
    }
}