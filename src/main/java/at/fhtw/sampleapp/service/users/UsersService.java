package at.fhtw.sampleapp.service.users;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class UsersService implements Service{
    private final UsersController usersController;

    public UsersService() {
        this.usersController = new UsersController(new UsersDAL());
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET &&
                request.getPathParts().size() > 1) {
            return this.usersController.getUsers(request.getPathParts().get(1));
        } else if (request.getMethod() == Method.GET) {
            return this.usersController.getUsers();
        } else if (request.getMethod() == Method.POST) {
            return this.usersController.addUsers(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
