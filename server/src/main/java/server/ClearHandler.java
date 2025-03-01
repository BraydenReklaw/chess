package server;

import dataaccess.DataAccessException;
import spark.*;
import service.ClearService;

public class ClearHandler {
    private ClearService clearService;

    public ClearHandler (ClearService clearService) {
        this.clearService = clearService;
    }

    public Object clear(Request request, Response response) throws DataAccessException {
        clearService.clear();
        response.status(200);
        return "{}";
    }
}
