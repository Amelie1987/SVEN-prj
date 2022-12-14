package at.fhtw.sampleapp.service.packages;

import at.fhtw.httpserver.http.ContentType;
import at.fhtw.httpserver.http.HttpStatus;
import at.fhtw.httpserver.http.Method;
import at.fhtw.httpserver.server.Request;
import at.fhtw.httpserver.server.Response;
import at.fhtw.httpserver.server.Service;

public class PackagesService  implements Service {
    private final PackagesController packagesController;

    public PackagesService() {

        this.packagesController = new PackagesController(new PackagesDAL());
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.POST) {
            return this.packagesController.createPackage(request);
        }
        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}