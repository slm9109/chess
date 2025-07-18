package service;

import model.UserData;    // <— IMPORT THIS
import model.AuthData;    // <— AND THIS
import dataaccess.UserDAO;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;

import java.util.UUID;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest req) throws DataAccessException {
        if (req.username() == null || req.password() == null || req.email() == null) {
            throw new DataAccessException("Bad request");
        }
        var user = new UserData(req.username(), req.password(), req.email());
        userDAO.insertUser(user);

        String token = UUID.randomUUID().toString();
        var auth = new AuthData(token, req.username());
        authDAO.insertAuth(auth);

        return new RegisterResult(req.username(), token);
    }
}

