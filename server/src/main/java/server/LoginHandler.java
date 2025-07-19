package server;

import spark.Request;
import spark.Response;
import spark.Route;
import com.google.gson.Gson;
import service.LoginService;
import model.LoginRequest;
import model.LoginResult;
import dataaccess.DataAccessException;

// import Gson, service.LoginService, model.LoginRequest, model.LoginResult, DataAccessException, spark.Route
public class LoginHandler implements Route {
    private final LoginService loginService;
    private final Gson gson = new Gson();

    public LoginHandler(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        LoginRequest body = gson.fromJson(req.body(), LoginRequest.class);
        try {
            LoginResult result = loginService.login(body);
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private record ErrorMessage(String message) {}
}



