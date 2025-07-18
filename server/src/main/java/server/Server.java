package server;



import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
// Initialize in-memory DAOs
        var userDAO = new dataaccess.InMemoryUserDAO();
        var gameDAO = new dataaccess.InMemoryGameDAO();
        var authDAO = new dataaccess.InMemoryAuthDAO();

// Set up the clear service and handler
        var clearService = new service.ClearService(userDAO, gameDAO, authDAO);
        var clearHandler = new server.ClearHandler(clearService);

// Register the HTTP DELETE /db endpoint
        Spark.delete("/db", clearHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
