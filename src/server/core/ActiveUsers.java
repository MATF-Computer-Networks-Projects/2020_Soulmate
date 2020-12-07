package server.core;


import javafx.util.Pair;
import server.form.User;

import java.util.HashMap;


public class ActiveUsers {
    private final HashMap<User, String> activeUsers;

    public ActiveUsers() {
        this.activeUsers = new HashMap<User, String>();
    }

    public void addUser(User user, String address) {
        this.activeUsers.put(user, address);

    }
}
