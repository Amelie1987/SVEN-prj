package at.fhtw.sampleapp.service.cards;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.controller.Controller;


import java.sql.SQLException;

public class CardsController extends Controller {

    private CardsDAL cardsDAL;

    public CardsController(CardsDAL cardsDAL) {

        this.cardsDAL = cardsDAL;
    }


    // GET /cards
    public Response showCards(Request request) {
        try {
            Token token = new Token();
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            System.out.println(token.getToken_id());
            String message = this.cardsDAL.showCards(token);       // 201-success 401 authentification fails
            String responseCode[] = message.split("/", 2);
            System.out.println("Return error Code " + responseCode[0]);
            if(responseCode[0].equals("200")) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ message: " + responseCode[1] + " }"
                );
            } else if(responseCode[0].equals("204")){
                return new Response(
                        HttpStatus.NO_CONTENT,
                        ContentType.JSON,
                        "{ message: \"No Content\" }"
                );
            } else if(responseCode[0].equals("401")){
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ message: \"Unauthorized\" }"
                );
            }
        }    catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
}
