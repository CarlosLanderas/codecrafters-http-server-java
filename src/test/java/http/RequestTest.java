package http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestTest {

    @Test
    void test() {
        var content = "These are the response contents";
        var response = Response.ok(content.getBytes());
        var expected = "HTTP/1.1 200 OK\r\nContent-Type: \r\nContent-Length: 31\r\n\r\nThese are the response contents";

        assertEquals(expected, new String(response.render()));
    }
}