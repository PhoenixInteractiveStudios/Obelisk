package org.burrow_studios.obelisk.commons.http.server;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.commons.http.Endpoint;
import org.burrow_studios.obelisk.commons.http.Method;
import org.burrow_studios.obelisk.commons.http.HTTPResponse;
import org.burrow_studios.obelisk.commons.http.server.exceptions.RequestHandlerException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/** A simple HTTP server equipped for everything any Obelisk component or service might commonly need. */
public abstract class HTTPServer {
    static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    /** Maps expected endpoints to their respective handlers. This serves as lookup during request processing. */
    private final ConcurrentHashMap<Endpoint, EndpointHandler> handlers = new ConcurrentHashMap<>();
    private final Authorizer authorizer;

    public HTTPServer(@NotNull Authorizer authorizer) {
        this.authorizer = authorizer;
    }

    /**
     * Registers an {@link EndpointHandler} as the responsible handler for a specific {@link Endpoint}.
     * @return This HTTPServer instance for chaining purposes.
     */
    public final @NotNull HTTPServer addEndpoint(@NotNull Endpoint endpoint, @NotNull EndpointHandler handler) {
        this.handlers.put(endpoint, handler);
        return this;
    }

    /**
     * Removes and {@link Endpoint} from the internal registry, causing this server to "forget" any mapped
     * {@link EndpointHandler}.
     * @return This HTTPServer instance for chaining purposes.
     */
    public final @NotNull HTTPServer removeEndpoint(@NotNull Endpoint endpoint) {
        this.handlers.remove(endpoint);
        return this;
    }

    /** Interpret &amp; authorize an incoming request and relay it to the responsible {@link EndpointHandler}. */
    protected final @NotNull HTTPResponse handle(@NotNull Method method, @NotNull String path, @NotNull Map<String, String> headers, @Nullable String bodyStr) {
        final String[] segments = path.substring(1).split("/");

        Endpoint endpoint = null;

        for (Endpoint e : this.handlers.keySet()) {
            if (!Objects.equals(e.getMethod(), method)) continue;
            if (!e.matchPath(segments))                 continue;

            endpoint = e;
            break;
        }

        final EndpointHandler handler = Optional.ofNullable(endpoint)
                .map(this.handlers::get)
                .orElse(null);

        if (handler == null) {
            // no handler found. Looking for endpoints with the same path but different methods

            final List<String> methods = this.handlers.keySet().stream()
                    // filter for matching paths
                    .filter(e -> e.matchPath(segments))
                    // get other methods of same path
                    .map(Endpoint::getMethod)
                    .map(Enum::name)
                    .toList();

            if (!methods.isEmpty()) {
                // there seem to be other methods, this calls for a 405
                return new ResponseBuilder()
                        .setCode(405)
                        .setHeader("Allow", String.join(", ", methods))
                        .build();
            }

            // path does not exist on the server at all -> 404
            return new ResponseBuilder()
                    .setCode(404)
                    .build();
        }

        DecodedJWT token = null;

        // auth
        if (endpoint.isPrivileged()) {
            final String authHeader = headers.get("Authorization");

            // fail if no token has been sent with the request
            if (authHeader == null || !authHeader.startsWith("Bearer"))
                return new ResponseBuilder()
                        .setCode(401)
                        .setHeader("WWW-Authenticate", "Bearer")
                        .build();

            final String tokenStr = authHeader.substring("Bearer ".length());

            try {
                switch (endpoint.getPrivilege()) {
                    case IDENTITY -> token = authorizer.authorizeIdentity(tokenStr);
                    case SESSION  -> token = authorizer.authorizeSession(tokenStr);
                }
            } catch (JWTVerificationException e) {
                return new ResponseBuilder()
                        .setCode(403)
                        .build();
            }
        }

        JsonElement body = null;
        if (bodyStr != null && !bodyStr.isEmpty()) {
            // FIXME: for now only JSON bodies are supported
            final String contentType = headers.get("Content-Type");
            if (!Objects.equals(contentType, "application/json"))
                return new ResponseBuilder()
                        .setCode(400)
                        .build();

            body = GSON.fromJson(bodyStr, JsonElement.class);
        }

        try {
            final HTTPRequest request = new HTTPRequest(this, endpoint, path, segments, headers, token, body);
            final ResponseBuilder builder = new ResponseBuilder();

            handler.handle(request, builder);

            return builder.build();
        } catch (RequestHandlerException e) {
            return e.asResponse();
        } catch (Exception e) {
            return new ResponseBuilder()
                    .setCode(500)
                    .build();
        }
    }
}
