import http.Request;
import http.Response;
import http.ResponseWriter;

import java.io.IOException;
import java.io.OutputStream;

public class Handlers {

    public static void rootHandler(Request request, ResponseWriter writer) throws IOException {
        Response.ok(null).writeTo(writer);
    }

    public static void echoHandler(Request request, ResponseWriter writer) throws IOException {
        var echoValue = request.lastSegment();
        var response = Response.ok(echoValue.getBytes());
        response.setContentType("text/plain");

        writer.write(response.render());
    }
}
