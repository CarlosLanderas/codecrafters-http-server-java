package http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTest {

    @Test
    void test_Response_Ok() {
        var content = "Dummy response content";
        var response = Response.ok(content.getBytes());
        response.setContentType("text/plain");

        var rString =  new String(response.render());
        var expected = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: 22\r\n\r\nDummy response content";

        assertEquals(expected, rString);
    }

    @Test
    void test_Response_NotFound() {
        var rString = new String(Response.notFound().render());
        var expected = "HTTP/1.1 404 Not Found\r\nContent-Type: \r\nContent-Length: 0\r\n\r\n";
    }
}
