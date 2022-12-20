package at.fhtw.httpserver.server;

import java.sql.SQLException;

public interface Service {
    Response handleRequest(Request request) throws SQLException;
}
