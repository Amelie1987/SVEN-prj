package at.fhtw.sampleapp.service.transactions;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.service.transactions.TransactionsDAL;
import com.fasterxml.jackson.core.JsonProcessingException;
import at.fhtw.sampleapp.controller.Controller;

import java.sql.SQLException;

public class TransactionsController extends Controller {
    private TransactionsDAL transactionsDAL;

    public TransactionsController(TransactionsDAL transactionsDAL) {

        this.transactionsDAL = transactionsDAL;
    }

    // POST /transactions/packages
    public Response acquirePackage(Request request) {
        try {
            Token token = new Token();
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            System.out.println(token.getToken_id());
            Integer responseCode = this.transactionsDAL.acquirePackage(token);       // 201-success 401 authentification fails
            if(responseCode == 200) {                                           // 403 is not admin 409 min. one card already exists
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ message: \"created\" }"
                );
            } else if(responseCode == 401){
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ message: \"unauthorized\" }"
                );
            } else if(responseCode == 403){
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ message: \"forbidden\" }"
                );
            } else if(responseCode == 404){
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ message: \"Not found\" }"
                );
            }
        }   catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
}
