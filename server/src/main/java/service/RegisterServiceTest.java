package service;


import model.UserData;    // <— IMPORT
import model.AuthData;    // <— IMPORT
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import dataaccess.InMemoryUserDAO;
import dataaccess.InMemoryAuthDAO;
import dataaccess.DataAccessException;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {
    private RegisterService registerService;
    private InMemoryUserDAO userDAO;
    private InMemoryAuthDAO authDAO;

    @BeforeEach
    void setup() {
        userDAO = new InMemoryUserDAO();
        authDAO = new InMemoryAuthDAO();
        registerService = new RegisterService(userDAO, authDAO);
    }

    @Test
    void register_successful() throws DataAccessException {
        var req = new RegisterRequest("u","p","e");
        var res = registerService.register(req);
        assertEquals("u", res.username());
        assertNotNull(res.authToken());
        // underlying DAOs should have the entries
        assertEquals(new UserData("u","p","e"), userDAO.getUser("u"));
        assertEquals(1, authDAO.getAllAuths().size());
    }

    @Test
    void register_withNull_throws() {
        var req = new RegisterRequest(null, "p", "e");
        assertThrows(DataAccessException.class, () -> registerService.register(req));
    }
}

