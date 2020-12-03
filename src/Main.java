import server.Utils;
import server.core.Server;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static boolean v = true;
    public static void main(String[] args) {
        try {
            Server.startServer();
        } catch (IOException e) {
            System.out.println("Failed to start server");
            e.printStackTrace();

        }
    }
}
