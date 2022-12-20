package at.fhtw.sampleapp.service.tradings;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.sampleapp.controller.Controller;
import at.fhtw.sampleapp.model.Token;
import at.fhtw.sampleapp.model.Trade;
import at.fhtw.sampleapp.model.Cards;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.sql.SQLException;

public class TradingsController extends Controller {
    private TradingsDAL tradingsDAL;

    public TradingsController(TradingsDAL tradingsDAL) {

        this.tradingsDAL = tradingsDAL;
    }

    //GET /tradings  shows available tradings
    public Response getDeals(Request request) throws SQLException {
        try {
            Token token = new Token();
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            String message = this.tradingsDAL.getDeals(token);
            String responseCode[] = message.split("/", 2);
            System.out.println("Return  Code " + responseCode[0]);

            if (responseCode[0].equals("200")) {
                return new Response(
                        HttpStatus.OK,
                        ContentType.JSON,
                        responseCode[1]
                );
            } else if (responseCode[0].equals("404")) {  // no deals available
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\" : \"No Content\" }"
                );
            }else if (responseCode[0].equals("401")) {
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

    // POST /tradings  insert a new trading deal into DB
    public Response createDeals(Request request) throws SQLException {
        //Users usersData = this.usersDAL.getUsers(Integer.parseInt(id));
        System.out.println("tradings create Deals active");
        try {
            Token token = new Token();
            Trade trade = new Trade();
            System.out.println("tradings create Deals active");
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            trade = this.getObjectMapper().readValue(request.getBody(), Trade.class);
            Integer responseCode = this.tradingsDAL.createDeals(token, trade);
            System.out.println("Return  Code " + responseCode);

            if (responseCode == 201) {
                return new Response(
                        HttpStatus.CREATED,
                        ContentType.JSON,
                        "{ \"message\" : \"created\" }"
                );
            } else if (responseCode == 401) {
                return new Response(
                        HttpStatus.UNAUTHORIZED,
                        ContentType.JSON,
                        "{ \"message\" : \"Unauthorized\" }"
                );
            } else if (responseCode == 403) {
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ \"message\" : \"Forbidden\" }"
                );
            } else if (responseCode == 409) {
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ \"message\" : \"Conflict\" }\n"
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

    // DELETE /tradings/trade_id
    public Response deleteDeals(Request request) throws SQLException {
        //Users usersData = this.usersDAL.getUsers(Integer.parseInt(id));
        System.out.println("tradings - delete Deals active");
        try {
            Token token = new Token();
            Trade trade = new Trade();
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            trade.setTrade_id(request.getPathParts().get(1));
            System.out.println("tradings - delete Deals active" + trade.getTrade_id());
            Integer responseCode = this.tradingsDAL.deleteDeals(token, trade);
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
            } else if (responseCode == 403) {
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ \"message\" : \"Forbidden\" }"
                );
            } else if (responseCode == 404) {
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\" : \"Not Found\" }\n"
                );
            } else if (responseCode == 409) {
                return new Response(
                        HttpStatus.CONFLICT,
                        ContentType.JSON,
                        "{ \"message\" : \"Conflict\" }\n"
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

    // POST /tradings/trade_id
    public Response executeDeals(Request request) throws SQLException {
        //Users usersData = this.usersDAL.getUsers(Integer.parseInt(id));
        System.out.println("tradings - EXECUTE Deals active");
        try {
            Token token = new Token();
            Trade trade = new Trade();
            Cards card = new Cards();
            token.setToken_id(request.getHeaderMap().getHeader("Authorization"));
            trade.setTrade_id(request.getPathParts().get(1));
            String cardString = request.getBody();
            System.out.println("tradings - execute Deals active" + trade.getTrade_id());
            Integer responseCode = this.tradingsDAL.executeDeals(token, trade, cardString);
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
            } else if (responseCode == 403) {
                return new Response(
                        HttpStatus.FORBIDDEN,
                        ContentType.JSON,
                        "{ \"message\" : \"Forbidden\" }"
                );
            } else if (responseCode == 404) {
                return new Response(
                        HttpStatus.NOT_FOUND,
                        ContentType.JSON,
                        "{ \"message\" : \"Not Found\" }\n"
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
