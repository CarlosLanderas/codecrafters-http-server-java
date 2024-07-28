
import server.Server;

public class Main {
  private final static int port = 4221;
  public static void main(String[] args) {
    try (var server = new Server(port)) {

      System.out.println("Server started on port: " + port);
      server.start();
    } catch (Exception ex) {
      System.out.println("server error: " + ex.getMessage());
    }
  }
}
