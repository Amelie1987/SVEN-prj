package at.fhtw.sampleapp.service.users;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

import java.sql.SQLException;

public class UsersService implements Service{
    private final UsersController usersController;

    public UsersService() {
        this.usersController = new UsersController(new UsersDAL());
    }

    @Override
    public Response handleRequest(Request request) throws SQLException {
        if (request.getMethod() == Method.GET && request.getPathParts().get(0).equals("stats")) {               // get all user data
            return this.usersController.getUsersStats(request); // get user data stats (elo, wins, losses)
        } else if (request.getMethod() == Method.GET && request.getPathParts().get(0).equals("score")) {               // get all user data
            return this.usersController.getUsersScore(request); // get scoreboard
        }else if (request.getMethod() == Method.GET) {
                return this.usersController.getUsers(request); // get user data
        } else if (request.getMethod() == Method.PUT) {       // update user for a given username
            return this.usersController.updateUsers(request);
        } else if (request.getMethod() == Method.POST) {
            return this.usersController.addUsers(request);    // (1) Create Users - Registration
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
