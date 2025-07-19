package service;

import model.LoginRequest;
import model.LoginResult;
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import java.util.UUID;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public LoginResult login(LoginRequest req) throws DataAccessException {
        if (req.username == null || req.password == null) throw new DataAccessException("Bad request");
        var user = userDAO.getUser(req.username);
        if (!user.getPassword().equals(req.password)) throw new DataAccessException("Unauthorized");
        String token = UUID.randomUUID().toString();
        authDAO.insertAuth(new AuthData(token, req.username));
        return new LoginResult(req.username, token);
    }
}

