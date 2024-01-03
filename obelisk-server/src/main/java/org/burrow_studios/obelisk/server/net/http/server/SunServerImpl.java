package org.burrow_studios.obelisk.server.net.http.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.*;
import org.burrow_studios.obelisk.server.net.http.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.server.net.http.exceptions.IllegalMethodException;
import org.burrow_studios.obelisk.server.net.http.exceptions.UnauthorizedException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

public class SunServerImpl extends APIHandler implements HttpHandler {
    private final HttpServer server;

    public SunServerImpl(@NotNull NetworkHandler networkHandler) throws IOException {
        super(networkHandler);
        this.server = HttpServer.create();
        this.server.createContext("/", this);
        this.server.start();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            this.handle0(exchange);
        } catch (Exception e) {
            // TODO: log e

            exchange.sendResponseHeaders(500, -1);
        }
        exchange.close();
    }

    private void handle0(HttpExchange exchange) throws IOException {
        final URI uri = exchange.getRequestURI();

        final String methodStr = exchange.getRequestMethod();
        final Method method    = Method.valueOf(methodStr);

        final Endpoint        endpoint = this.getEndpoint(method, uri);
        final EndpointHandler handler  = this.getHandler(endpoint);

        final Map<String, String> headers = exchange.getRequestHeaders().entrySet().stream()
                // combine duplicate header fields into one (separated by comma)
                .map(entry -> Map.entry(entry.getKey(), String.join(", ", entry.getValue())))
                // collect as Map<String, String>
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        final Request request = new Request(
                this,
                endpoint,
                headers
        );

        final ResponseBuilder builder = new ResponseBuilder();

        try {
            handler.handle(request, builder);
        } catch (UnauthorizedException e) {
            builder.clear();
            builder.setCode(401);

            builder.setHeader("WWW-Authenticate", "Bearer");
        } catch (ForbiddenException e) {
            builder.clear();
            builder.setCode(403);
        } catch (IllegalMethodException e) {
            builder.clear();
            builder.setCode(405);

            builder.setHeader("Allow", String.join(", ", e.getAllowedStr()));
        } catch (Exception e) {
            builder.clear();
            builder.setCode(500);
        }

        // send response

        final Response response = builder.build();

        final Headers responseHeaders = exchange.getResponseHeaders();
        response.headers().forEach(responseHeaders::add);

        exchange.sendResponseHeaders(response.code(), response.bodyLength());

        if (response.hasBody())
            exchange.getResponseBody().write(response.bodyBytes());
    }
}
