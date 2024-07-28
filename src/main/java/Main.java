
import http.Method;
import server.Router;
import server.Server;

public class Main {
    private final static int port = 4221;

    public static void main(String[] args) {

        var router = new Router();
        router.register("/echo/.*", Method.GET, Handlers::echoHandler);
        router.register("/$", Method.GET, Handlers::rootHandler);

        try (var server = new Server(router, port)) {

            System.out.println("Server started on port: " + port);
            server.start();
        } catch (Exception ex) {
            System.out.println("server error: " + ex.getMessage());
        }
    }
}
