package dataaccess;

// add this import:
import model.UserData;

import dataaccess.DataAccessException;
import java.util.List;

public interface UserDAO {
    void clear();
    void insertUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    List<UserData> getAllUsers();
}

