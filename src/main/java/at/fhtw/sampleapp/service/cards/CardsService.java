package at.fhtw.sampleapp.service.cards;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;
import at.fhtw.sampleapp.service.cards.CardsController;
import at.fhtw.sampleapp.service.packages.PackagesDAL;
import at.fhtw.sampleapp.service.users.UsersController;
import at.fhtw.sampleapp.service.users.UsersDAL;

public class CardsService implements Service {
    private final CardsController cardsController;

    public CardsService() {

        this.cardsController = new CardsController(new CardsDAL());
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET) {
            return this.cardsController.showCards(request);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}