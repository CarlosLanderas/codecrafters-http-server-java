
import server.Server;

public class Main {
  public static void main(String[] args) {
    try (var server = new Server(4221)) {
      server.Start();
    } catch (Exception ex) {
      System.out.println("server error: " + ex.getMessage());
    }
  }
}
