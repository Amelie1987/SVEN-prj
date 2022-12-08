package at.fhtw.sampleapp.service.users;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.sampleapp.controller.Controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import at.fhtw.sampleapp.model.Users;

import java.util.List;

public class UsersController extends Controller {
    private UsersDAL usersDAL;

    public UsersController(UsersDAL usersDAL) {

        this.usersDAL = usersDAL;
    }

    // GET /users(:id
    public Response getUsers(String id)
    {
        try {           //Users usersData = this.usersDAL.getUsers(Integer.parseInt(id));
            Users usersData = this.usersDAL.getUsers(id);
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String usersDataJSON = this.getObjectMapper().writeValueAsString(usersData);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    usersDataJSON
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }
    // GET /weather
    public Response getUsers() {
        try {
            List usersData = this.usersDAL.getUsers();
            // "[ { \"id\": 1, \"city\": \"Vienna\", \"temperature\": 9.0 }, { ... }, { ... } ]"
            String usersDataJSON = this.getObjectMapper().writeValueAsString(usersData);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    usersDataJSON
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        }
    }

    // POST /weather
    public Response addUsers(Request request) {
        try {

            // request.getBody() => "{ \"id\": 4, \"city\": \"Graz\", ... }
            Users users = this.getObjectMapper().readValue(request.getBody(), Users.class);
            this.usersDAL.addUsers(users);

            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{ message: \"Success\" }"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
}
