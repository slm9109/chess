package dataaccess;

import model.AuthData;
import dataaccess.DataAccessException;
import java.util.List;

public interface AuthDAO {
    void clear();
    void insertAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String token) throws DataAccessException;
    List<AuthData> getAllAuths();
}
