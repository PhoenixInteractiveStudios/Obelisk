package org.burrow_studios.obelisk.monolith.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.burrow_studios.obelisk.core.http.Method;
import org.burrow_studios.obelisk.core.http.Route;
import org.burrow_studios.obelisk.monolith.http.exceptions.IllegalMethodException;
import org.burrow_studios.obelisk.monolith.http.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.monolith.http.exceptions.NotFoundException;
import org.burrow_studios.obelisk.monolith.http.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HTTPServer {
    static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .create();

    private final HttpServer internalServer;
    private final Map<Route, RequestHandler> handlers;

    public HTTPServer(int port) throws IOException {
        this.internalServer = HttpServer.create();
        this.internalServer.bind(new InetSocketAddress(port), -1);
        this.internalServer.createContext("/", this::handle);

        this.handlers = new ConcurrentHashMap<>();
    }

    public void start() {
        this.internalServer.start();
    }

    public void addHandler(@NotNull Route route, @NotNull RequestHandler handler) {
        this.handlers.put(route, handler);
    }

    private void handle(@NotNull HttpExchange exchange) throws IOException {
        // request path (trim leading "/")
        String requestPath = exchange.getRequestURI().getPath().substring(1);

        // request method
        String methodStr = exchange.getRequestMethod();
        Method method = Method.valueOf(methodStr);

        // request body
        JsonElement body = null;
        byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
        if (bodyBytes != null)
            body = GSON.fromJson(new String(bodyBytes), JsonElement.class);

        // request headers
        Map<String, String> headers = new LinkedHashMap<>();
        exchange.getRequestHeaders().forEach((key, values) -> {
            String value = String.join(", ", values);
            headers.put(key, value);
        });

        Route.Compiled route = Route.custom(method, requestPath).compile();

        Request  request  = new Request(exchange, route, body, headers);
        Response response = this.handle(request);

        // response headers
        response.getHeaders().forEach((key, val) -> {
            exchange.getResponseHeaders().add(key, val);
        });

        // response status (& body length)
        exchange.sendResponseHeaders(response.getStatus(), (response.getBody() != null && !response.getBody().isEmpty()) ? response.getBody().length() : -1);

        // response body
        if (response.getBody() != null)
            exchange.getResponseBody().write(response.getBody().getBytes());

        exchange.close();
    }

    private @NotNull Response handle(@NotNull Request request) {
        Route route = null;
        for (Route r : this.handlers.keySet()) {
            if (r.getMethod() != request.getMethod()) continue;
            if (!r.getPath().match(request.getPath())) continue;

            route = r;
            break;
        }

        RequestHandler handler = Optional.ofNullable(route)
                .map(this.handlers::get)
                .orElse(null);

        if (handler == null) {
            // no handler found. Looking for endpoints with the same path but different methods for a possible 405 error

            Method[] methods = this.handlers.keySet().stream()
                    // filter for matching paths
                    .filter(e -> e.getPath().match(request.getPath()))
                    // get other methods of same path
                    .map(Route::getMethod)
                    .toArray(Method[]::new);

            if (methods.length != 0) {
                // there seem to be other methods
                return new IllegalMethodException(request.getMethod(), methods).toResponse();
            }

            // path does not exist on the server at all
            return new NotFoundException().toResponse();
        }


        // TODO: auth


        try {
            return handler.handle(request);
        } catch (RequestHandlerException e) {
            return e.toResponse();
        } catch (Exception e) {
            return new InternalServerErrorException().toResponse();
        }
    }
}
