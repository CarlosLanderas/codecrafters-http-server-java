package http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

public class MethodTest {

    @Test
    void test() {

        var m1 = Method.fromString("get");
        var m2 = Method.fromString("GET");
        var m3 = Method.fromString("POST");

        Assertions.assertEquals(m1, Method.GET);
        Assertions.assertEquals(m2, Method.GET);
        Assertions.assertEquals(m3, Method.POST);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Method.fromString("options"));
    }
}
