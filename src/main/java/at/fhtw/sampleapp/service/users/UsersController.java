package at.fhtw.sampleapp.service.users;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.sampleapp.controller.Controller;
import at.fhtw.sampleapp.model.Users;
import at.fhtw.sampleapp.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import at.fhtw.sampleapp.model.Users;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.sql.SQLException;
import java.util.List;

public class UsersController extends Controller {
    private UsersDAL usersDAL;

    public UsersController(UsersDAL usersDAL) {

        this.usersDAL = usersDAL;
    }

    //GET /score
    public Response getUsersScore(Request request) throws SQLException {
        try {
            Token token = new Token();
            String path = request.getPathParts().get(0);
            System.out.println("getUsersStats Path::" + path + "::");
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            String message = this.usersDAL.getUsersScore(token);
            String responseCode[] = message.split("/", 2);
            System.out.println("Return  Code " + responseCode[0]);

            if (responseCode[0].equals("200")) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        responseCode[1]
                );
            } else if (responseCode[0].equals("401")) {
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ \"message\" : \"Unauthorized\" }"
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
    //GET /stats
    public Response getUsersStats(Request request) throws SQLException {
        try {
            Token token = new Token();
            String path = request.getPathParts().get(0);
            System.out.println("getUsersStats Path::" + path + "::");
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            String message = this.usersDAL.getUsersStats(token);
            String responseCode[] = message.split("/", 2);
            System.out.println("Return  Code " + responseCode[0]);

            if (responseCode[0].equals("200")) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        responseCode[1]
                );
            } else if (responseCode[0].equals("401")) {
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ \"message\" : \"Unauthorized\" }"
                );
            } else if (responseCode[0].equals("404")) {
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\" : \"Not Found\" }"
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
    // PUT /users data
    public Response updateUsers(Request request) throws SQLException {
        //Users usersData = this.usersDAL.getUsers(Integer.parseInt(id));
        try {
            Token token = new Token();
            Users user = new Users();
            String username = request.getPathParts().get(1);
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            user = this.getObjectMapper().readValue(request.getBody(), Users.class);
            Integer responseCode = this.usersDAL.updateUsers(username, token, user);
            System.out.println("Return  Code " + responseCode);

            if (responseCode == 200) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        "{ \"message\" : \"OK\" }"
                );
            } else if (responseCode == 401) {
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ \"message\" : \"Unauthorized\" }"
                );
            } else if (responseCode == 404) {
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\" : \"Not Found\" }"
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
    // GET /users data
    public Response getUsers(Request request) throws SQLException {
        //Users usersData = this.usersDAL.getUsers(Integer.parseInt(id));
        try {
            Token token = new Token();
            String path = request.getPathParts().get(0);
            System.out.println("getUsers Path::" + path + "::");
            String username = request.getPathParts().get(1);
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            String message = this.usersDAL.getUsers(username, token);
            // String usersDataJSON = this.getObjectMapper().writeValueAsString(usersData);
            String responseCode[] = message.split("/", 2);
            System.out.println("Return  Code " + responseCode[0]);

            if (responseCode[0].equals("200")) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        responseCode[1]
                );
            } else if (responseCode[0].equals("401")) {
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ \"message\" : \"Unauthorized\" }"
                );
            } else if (responseCode[0].equals("404")) {
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\" : \"Not Found\" }"
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
    // POST /users
    public Response addUsers(Request request) {
        try {

            Users users = this.getObjectMapper().readValue(request.getBody(), Users.class);
            System.out.println(users);
            System.out.println(users.getUsername());
            System.out.println(users.getPassword());
            boolean userAdded = this.usersDAL.addUsers(users);
            if(userAdded == true) {
                return new Response(
                        HttpStatus.CREATED,
                        ContentType.JSON,
                        "{ message: \"Success\" }"
                );
            } else {
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ message: \"failed\" }"
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
