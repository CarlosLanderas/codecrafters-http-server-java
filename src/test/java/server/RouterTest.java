package server;

import http.Method;
import http.Request;
import http.ResponseWriter;
import http.handler.Handler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RouterTest {

    @Test
    void test() throws IOException {

        final var invoke = new AtomicBoolean(false);
        final var stream = OutputStream.nullOutputStream();
        final var dummyRequest = Request.create("/test", Method.GET, "HTTP 1.1");
        final var router = new Router();

        router.register("/test", Method.GET, (Request r, ResponseWriter w) -> {
            invoke.set(true);
        });


        router.get("/test", Method.GET)
                .handle(dummyRequest, new ResponseWriter(stream, dummyRequest));

        assertEquals(true, invoke.get());
    }

    @Test
    void test_Unregistered()  {
        final var router = new Router();
        final var handler = router.get("/test", Method.GET);

        Assertions.assertInstanceOf(NotFoundHandler.class, handler);
    }
}

