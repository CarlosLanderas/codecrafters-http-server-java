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
        var rw = new ResponseWriter(str);

        rw.write(content.getBytes());
        //rw.

        assertEquals(content, str.toString());
    }
}
