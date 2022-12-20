package at.fhtw.sampleapp.service.packages;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;

import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.sampleapp.controller.Controller;
import at.fhtw.sampleapp.model.Users;
import at.fhtw.sampleapp.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import at.fhtw.sampleapp.model.Cards;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.sql.SQLException;
import java.util.List;


public class PackagesController  extends Controller {

    private PackagesDAL packagesDAL;

    public PackagesController(PackagesDAL packagesDAL) {

        this.packagesDAL = packagesDAL;
    }


    // POST /packages
    public Response createPackage(Request request) {
        try {
            Cards[] cards = this.getObjectMapper().readValue(request.getBody(), Cards[].class);
            Token token = new Token();
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            System.out.println(token.getToken_id());
            System.out.println(cards[1].getName());
            Integer responseCode = this.packagesDAL.createPackage(cards, token);       // 201-success 401 authentification fails
            if(responseCode == 201) {                                           // 403 is not admin 409 min. one card already exists
                return new Response(
                        HttpStatus.CREATED,
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
            } else if(responseCode == 409){
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ message: \"conflict\" }"
                );
            }
       }  catch (JsonProcessingException e) {
            e.printStackTrace();
        }  catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
}