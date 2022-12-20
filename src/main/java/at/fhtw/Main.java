package at.fhtw;

import at.fhtw.httpserver.utils.Router;
import at.fhtw.httpserver.server.Server;
import at.fhtw.sampleapp.service.echo.EchoService;
import at.fhtw.sampleapp.service.users.UsersService;
import at.fhtw.sampleapp.service.session.SessionService;
import at.fhtw.sampleapp.service.packages.PackagesService;
import at.fhtw.sampleapp.service.cards.CardsService;
import at.fhtw.sampleapp.service.deck.DeckService;
import at.fhtw.sampleapp.service.tradings.TradingsService;
import at.fhtw.sampleapp.service.transactions.TransactionsService;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Router configureRouter()
    {
        Router router = new Router();
        router.addService("/echo", new EchoService());
        router.addService("/users", new UsersService());
        router.addService("/sessions", new SessionService());
        router.addService("/packages", new PackagesService());
        router.addService("/transactions", new TransactionsService());
        router.addService("/cards", new CardsService());
        router.addService("/deck", new DeckService());
        router.addService("/stats", new UsersService());        // ! - requestHandle from /stats in UsersService!
        router.addService("/score", new UsersService());        // ! - requestHandle from /score in UsersService!
        router.addService("/tradings", new TradingsService());
        return router;
    }
}
