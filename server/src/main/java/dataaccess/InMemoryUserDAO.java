package dataaccess;

import model.UserData;

import java.util.*;

public class InMemoryUserDAO {
    private final Map<String, UserData> users = new HashMap<>();
    private boolean failOnClear = false;

    public void insertUser(UserData user) {
        users.put(user.getUsername(), user);
    }

    public Collection<UserData> getAllUsers() {
        return users.values();
    }

    public void setFailOnClear(boolean value) {
        this.failOnClear = value;
    }

    public void clear() {
        if (failOnClear) throw new RuntimeException("Clear failed");
        users.clear();
    }
}

