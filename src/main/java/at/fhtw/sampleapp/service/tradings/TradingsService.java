package at.fhtw.sampleapp.service.tradings;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;


import java.sql.SQLException;

public class TradingsService implements Service {
    private final TradingsController tradingsController;

    public TradingsService() {
        this.tradingsController = new TradingsController(new TradingsDAL());
    }

    @Override
    public Response handleRequest(Request request) throws SQLException { // request.getPathParts().get(1) != null
       if (request.getMethod() == Method.POST && request.getPathParts().size() == 2){  // Carry out a trade for the deal with the provided card
            return this.tradingsController.executeDeals(request);
        } else if (request.getMethod() == Method.GET) {               // get all available deals
            return this.tradingsController.getDeals(request);
        }else if (request.getMethod() == Method.POST) {               // create new deal
            return this.tradingsController.createDeals(request);
        } else if (request.getMethod() == Method.DELETE) {       // deletes an existing trading deal
            return this.tradingsController.deleteDeals(request);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
