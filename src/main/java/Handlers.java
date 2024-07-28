import http.Request;
import http.Response;
import http.ResponseWriter;
import http.handler.Handler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Handlers {

    public static Handler fileHander(String contentPath) {
        return (Request request, ResponseWriter writer) -> {
            var filePath = Path.of(contentPath, request.lastSegment());
            if (!Files.exists(filePath)) {
                Response.notFound().writeTo(writer);
                return;
            }

            var content = Files.readAllBytes(filePath);
            Response.ok(content)
                    .setContentType("application/octet-stream")
                    .writeTo(writer);
        };
    }

    public static void userAgentHandler(Request request, ResponseWriter writer) throws IOException {
        var userAgent = request.getHeader("User-Agent");
        if (userAgent.isEmpty()) {
            Response.error("user agent header not found").writeTo(writer);
            return;
        }

        Response.ok(userAgent.get().getBytes())
                .setContentType("text/plain")
                .writeTo(writer);
    }

    public static void echoHandler(Request request, ResponseWriter writer) throws IOException {
        var echoValue = request.lastSegment();

        Response.ok(echoValue.getBytes())
                .setContentType("text/plain")
                .writeTo(writer);
    }

    public static void rootHandler(Request request, ResponseWriter writer) throws IOException {
        Response.ok(null).writeTo(writer);
    }
}
