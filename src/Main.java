import server.core.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server.startServer();
        } catch (IOException e) {
            System.out.println("Failed to start server");
            e.printStackTrace();
        }
    }
}
