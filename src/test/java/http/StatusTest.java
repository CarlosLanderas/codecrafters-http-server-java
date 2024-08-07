package http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StatusTest {

    @Test
    public void test() {
        var s1 = Status.fromCode(200);
        var s2 = Status.fromCode(404);

        Assertions.assertEquals(Status.OK, s1);
        Assertions.assertEquals(Status.NOTFOUND, s2);
        Assertions.assertThrows(IllegalArgumentException.class, () -> Status.fromCode(429));
    }
}
