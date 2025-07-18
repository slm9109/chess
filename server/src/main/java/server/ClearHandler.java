package server;

import com.google.gson.Gson;
import service.ClearService;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    private final ClearService clearService;
    private final Gson gson = new Gson();

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        try {
            clearService.clear();
            res.status(200);
            return "{}";  // success: empty JSON object
        } catch (DataAccessException e) {
            res.status(500);
            return gson.toJson(new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private record ErrorMessage(String message) {}
}
