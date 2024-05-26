package org.burrow_studios.obelisk.monolith.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.burrow_studios.obelisk.core.http.Method;
import org.burrow_studios.obelisk.core.http.Route;
import org.burrow_studios.obelisk.monolith.auth.ApplicationContext;
import org.burrow_studios.obelisk.monolith.auth.AuthManager;
import org.burrow_studios.obelisk.monolith.exceptions.AuthenticationException;
import org.burrow_studios.obelisk.monolith.http.exceptions.*;
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
    private final AuthManager authManager;
    private final Map<Route, RequestHandler> handlers;
    private final Map<Route, String[]> intents;
    private final Map<Route, Boolean> reqAuth;

    public HTTPServer(int port, @NotNull AuthManager authManager) throws IOException {
        this.internalServer = HttpServer.create();
        this.internalServer.bind(new InetSocketAddress(port), -1);
        this.internalServer.createContext("/", this::handle);

        this.authManager = authManager;

        this.handlers = new ConcurrentHashMap<>();
        this.intents  = new ConcurrentHashMap<>();
        this.reqAuth  = new ConcurrentHashMap<>();
    }

    public void start() {
        this.internalServer.start();
    }

    public void stop() {
        this.internalServer.stop(4);
    }

    public void addHandler(@NotNull Route route, @NotNull RequestHandler handler, boolean requiredAuth, @NotNull String... intents) {
        this.handlers.put(route, handler);
        this.intents.put(route, intents);
        this.reqAuth.put(route, requiredAuth);
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


        Boolean requiresAuth = this.reqAuth.get(route);
        if (requiresAuth == null)
            return new InternalServerErrorException().toResponse();
        if (requiresAuth) {
            String authHeader = request.getHeaders().get("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer "))
                return new UnauthorizedException("Bearer authorization required").toResponse();

            String token = authHeader.substring("Bearer ".length());

            ApplicationContext appCtx;
            try {
                appCtx = this.authManager.authenticate(token);
            } catch (AuthenticationException e) {
                return new UnauthorizedException("Valid bearer authorization required").toResponse();
            }

            String[] intents = this.intents.get(route);
            if (intents == null)
                return new InternalServerErrorException().toResponse();
            for (String intent : intents) {
                if (appCtx.hasIntent(intent)) continue;
                return new ForbiddenException("This endpoints requires the '" + intent + "' intent.").toResponse();
            }
        }


        try {
            return handler.handle(request);
        } catch (RequestHandlerException e) {
            return e.toResponse();
        } catch (Exception e) {
            return new InternalServerErrorException().toResponse();
        }
    }
}
