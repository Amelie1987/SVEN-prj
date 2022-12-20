package at.fhtw.sampleapp.service.transactions;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;


public class TransactionsService implements Service {

    private final TransactionsController transactionsController;

    public TransactionsService(){
        this.transactionsController = new TransactionsController(new TransactionsDAL());
    }
    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST) {
            return this.transactionsController.acquirePackage(request);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
