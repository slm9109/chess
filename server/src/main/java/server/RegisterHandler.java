package server;

import com.google.gson.Gson;
import service.RegisterService;
import service.RegisterRequest;
import service.RegisterResult;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {
    private final RegisterService registerService;
    private final Gson gson = new Gson();

    public RegisterHandler(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        try {
            RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
            RegisterResult result = registerService.register(request);
            res.status(200);
            return gson.toJson(result);
        } catch (DataAccessException e) {
            res.status(400);
            return gson.toJson(new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private record ErrorMessage(String message) {}
}
