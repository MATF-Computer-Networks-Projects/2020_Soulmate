import server.Utils;
import server.core.Server;
import server.form.Gender;
import server.form.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static server.form.UserOffering.offerings;

public class Main {
    private static boolean v = true;
    public static void main(String[] args) {
        try {
            Server.startServer();
        } catch (Exception e) {
            System.out.println("Failed to start server");
            e.printStackTrace();

        }
    }
}
