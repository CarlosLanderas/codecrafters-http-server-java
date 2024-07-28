
import http.Method;
import server.Router;
import server.Server;

public class Main {
    private final static int port = 4221;

    public static void main(String[] args) {

        String contentPath = "";
        if(args.length > 1) {
            contentPath = args[1];
        }

        var router = new Router();

        router.register("/files/.*", Method.POST, Handlers.postFileHandler(contentPath));
        router.register("/files/.*", Method.GET, Handlers.fileHandler(contentPath));
        router.register("/user-agent", Method.GET, Handlers::userAgentHandler);
        router.register("/echo/.*", Method.GET, Handlers::echoHandler);
        router.register("/$", Method.GET, Handlers::rootHandler);

        try (var server = new Server(router, port, contentPath)) {
            System.out.println("Server started on port: " + port);
            server.start();
        } catch (Exception ex) {
            System.out.println("server error: " + ex.getMessage());
        }
    }
}
