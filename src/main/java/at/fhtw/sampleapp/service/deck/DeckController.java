package at.fhtw.sampleapp.service.deck;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.controller.Controller;
import at.fhtw.sampleapp.model.Users;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;

public class DeckController extends Controller {

    private DeckDAL deckDAL;

    public DeckController(DeckDAL deckDAL) {

        this.deckDAL = deckDAL;
    }


    // GET /deck + param: format=plain
    public Response showDeck(Request request) {
        try {
            Token token = new Token();
            String message = "";
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            System.out.println(token.getToken_id() + " - show Deck");
            System.out.println("Params: " + request.getParams());
            if (request.getParams() != null) {
                message = deckDAL.showDeckBeautiful(token);
            } else {
                message = this.deckDAL.showDeck(token);
            }
            String responseCode[] = message.split("/", 2);
            System.out.println("Return  Code " + responseCode[0]);
            if(responseCode[0].equals("200")) {         // OK and show card_ids
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ message: " + responseCode[1] + " }"
                );
            } else if(responseCode[0].equals("404")){
                //System.out.println("entered DeckController 204 error");
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ message: \"Not Found\" }"
                );
            } else if(responseCode[0].equals("401")){
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ message: \"Unauthorized\" }"
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);


        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
/*
    // GET /deck?format=plain
    public Response showDeckBeautiful(Request request) {
        try {
            Token token = new Token();
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            System.out.println(token.getToken_id() + " - show Deck beautiful");
            String message = this.deckDAL.showDeckBeautiful(token);
            String responseCode[] = message.split("/", 2);
            System.out.println("Return  Code " + responseCode[0]);
            if(responseCode[0].equals("200")) {         // OK and show card_ids
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ message: " + responseCode[1] + " }"
                );
            } else if(responseCode[0].equals("404")){
                //System.out.println("entered DeckController 204 error");
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ message: \"Not Found\" }"
                );
            } else if(responseCode[0].equals("401")){
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ message: \"Unauthorized\" }"
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);


        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }

 */
    public Response configureDeck(Request request) {
        try {
            Token token = new Token();
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            System.out.println(token.getToken_id() + " - configureDeck");
            String cardsString = "";
            cardsString = request.getBody();
            System.out.println("Card string" + cardsString);
            int responseCode = this.deckDAL.configureDeck(cardsString,token);
            if(responseCode == 200) {         // OK and show card_ids
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ message: \"OK\" }"
                );
            } else if(responseCode == 400){     // number of cards != 4
                return new Response(
                        HttpStatus.BAD_REQUEST,
                        ContentType.JSON,
                        "{ message: \"Bad Request\" }"
                );
            } else if(responseCode == 401){     // invalid token
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ message: \"Unauthorized\" }"
                );
            } else if(responseCode == 403){     // card not in users stack
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ message: \"Forbidden\" }"
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
}

/*

return new Response(
                        HttpStatus.NO_CONTENT,
                        ContentType.JSON,
                        "{ message: \"No Content\" }"
                );
 */