package server;

import static spark.Spark.*;

import dataaccess.InMemoryUserDAO;
import dataaccess.InMemoryGameDAO;
import dataaccess.InMemoryAuthDAO;

import service.ClearService;
import service.RegisterService;
import service.LoginService;
import service.GameService;

import server.ClearHandler;
import server.RegisterHandler;
import server.LoginHandler;
import server.LogoutHandler;
import server.CreateGameHandler;
import server.ListGamesHandler;
import server.JoinGameHandler;

public class Server {
    public static void main(String[] args) {
        new Server().run(8080);
    }

    public int run(int portNumber) {
        port(portNumber);
        staticFiles.location("web");

        // DAOs
        InMemoryUserDAO userDAO = new InMemoryUserDAO();
        InMemoryGameDAO gameDAO = new InMemoryGameDAO();
        InMemoryAuthDAO authDAO = new InMemoryAuthDAO();

        // Clear
        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        delete("/db", new ClearHandler(clearService));

        // Register
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        post("/user", new RegisterHandler(registerService));

        // Login
        LoginService loginService = new LoginService(userDAO, authDAO);
        post("/session", new LoginHandler(loginService));

        // Logout
        delete("/session", new LogoutHandler(authDAO));

        // Create Game
        GameService gameService = new GameService(gameDAO);
        post("/game", new CreateGameHandler(gameService));

        // List Games
        get("/game", new ListGamesHandler(gameService));

        // Join Game
        put("/game", new JoinGameHandler(gameService));

        awaitInitialization();
        return port();
    }

    public void stop() {
        // correct Spark shutdown
        spark.Spark.stop();
    }
}



