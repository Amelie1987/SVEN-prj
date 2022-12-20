package at.fhtw.sampleapp.service.session;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.sampleapp.controller.Controller;
import at.fhtw.sampleapp.model.Users;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;

public class SessionController extends Controller {

    private SessionDAL sessionDAL;

    public SessionController(SessionDAL sessionDAL) {

        this.sessionDAL = sessionDAL;
    }


    // POST /session
    public Response login(Request request) {
        try {
            Users users = this.getObjectMapper().readValue(request.getBody(), Users.class);
            System.out.println(users.getUsername());
            System.out.println(users.getPassword());
            boolean userLogin = this.sessionDAL.login(users);
            if(userLogin) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ message: \"OK\" }"
                );
            } else {
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ message: \"unauthorized\" }"
                );
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
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