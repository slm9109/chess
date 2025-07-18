package service;

import dataaccess.InMemoryUserDAO;
import dataaccess.InMemoryGameDAO;
import dataaccess.InMemoryAuthDAO;
import model.UserData;
import model.GameData;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {
    private ClearService clearService;
    private InMemoryUserDAO userDAO;
    private InMemoryGameDAO gameDAO;
    private InMemoryAuthDAO authDAO;

    @BeforeEach
    void setup() {
        userDAO = new InMemoryUserDAO();
        gameDAO = new InMemoryGameDAO();
        authDAO = new InMemoryAuthDAO();

        // pre-populate
        userDAO.insertUser(new UserData("u", "p", "e@example.com"));
        gameDAO.insertGame(new GameData(1, "u", null, "name"));
        authDAO.insertAuth(new AuthData("t", "u"));

        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @Test
    void clear_wipesAllData() {
        clearService.clear();
        assertTrue(userDAO.getAllUsers().isEmpty(), "Users should be empty");
        assertTrue(gameDAO.getAllGames().isEmpty(), "Games should be empty");
        assertTrue(authDAO.getAllAuths().isEmpty(), "Auths should be empty");
    }

    @Test
    void clear_whenDAOThrows_throwsException() {
        userDAO.setFailOnClear(true);
        assertThrows(RuntimeException.class, () -> clearService.clear());
    }
}


