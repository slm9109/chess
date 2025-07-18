package dataaccess;

import model.AuthData;

import java.util.*;

public class InMemoryAuthDAO {
    private final Map<String, AuthData> auths = new HashMap<>();
    private boolean failOnClear = false;

    public void insertAuth(AuthData auth) {
        auths.put(auth.getAuthToken(), auth);
    }

    public Collection<AuthData> getAllAuths() {
        return auths.values();
    }

    public void setFailOnClear(boolean value) {
        this.failOnClear = value;
    }

    public void clear() {
        if (failOnClear) throw new RuntimeException("Clear failed");
        auths.clear();
    }
}


