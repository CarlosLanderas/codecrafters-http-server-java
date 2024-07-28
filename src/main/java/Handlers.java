import http.Request;
import http.Response;
import http.ResponseWriter;
import jdk.jfr.Frequency;

import java.io.IOException;

public class Handlers {

    public static void userAgentHandler(Request request, ResponseWriter writer) throws IOException {
        var userAgent = request.getHeader("User-Agent");
        if (userAgent.isEmpty()) {
            Response.error("user agent header not found").writeTo(writer);
            return;
        }

        var response = Response.ok(userAgent.get().getBytes());
        response.setContentType("text/plain");
        response.writeTo(writer);
    }

    public static void echoHandler(Request request, ResponseWriter writer) throws IOException {
        var echoValue = request.lastSegment();
        var response = Response.ok(echoValue.getBytes());
        response.setContentType("text/plain");

        writer.write(response.render());
    }

    public static void rootHandler(Request request, ResponseWriter writer) throws IOException {
        Response.ok(null).writeTo(writer);
    }
}
