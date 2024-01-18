package org.burrow_studios.obelisk.server.net.http;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.core.net.http.Method;
import org.burrow_studios.obelisk.server.auth.crypto.TokenManager;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.exceptions.APIException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class APIHandler {
    protected final @NotNull NetworkHandler networkHandler;

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final ConcurrentHashMap<Endpoint, EndpointHandler> handlers;

    public APIHandler(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
        this.handlers = new ConcurrentHashMap<>();
    }

    public final @NotNull APIHandler addEndpoint(@NotNull Method method, @NotNull String path, @NotNull AuthLevel privilege, @NotNull EndpointHandler handler) {
        return this.addEndpoint(new Endpoint(method, path, privilege), handler);
    }

    public final @NotNull APIHandler addEndpoint(@NotNull Endpoint endpoint, @NotNull EndpointHandler handler) {
        this.handlers.put(endpoint, handler);
        return this;
    }

    public final @NotNull APIHandler removeEndpoint(@NotNull Endpoint endpoint) {
        this.handlers.remove(endpoint);
        return this;
    }

    public final @NotNull NetworkHandler getNetworkHandler() {
        return this.networkHandler;
    }

    protected final @NotNull Response handle(@NotNull Method method, @NotNull String path, @NotNull Map<String, String> headers, @Nullable String bodyStr) {
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

            if (authHeader == null || !authHeader.startsWith("Bearer"))
                return new ResponseBuilder()
                        .setCode(401)
                        .setHeader("WWW-Authenticate", "Bearer")
                        .build();

            final String tokenStr = authHeader.substring("Bearer ".length());

            final TokenManager tokenManager = this.getNetworkHandler().getServer().getAuthenticator().getTokenManager();

            try {
                switch (endpoint.getPrivilege()) {
                    case SESSION  -> token = tokenManager.decodeSessionToken(tokenStr);
                    case IDENTITY -> token = tokenManager.decodeIdentityToken(tokenStr);
                }
            } catch (JWTVerificationException e) {
                return new ResponseBuilder()
                        .setCode(403)
                        .build();
            }
        }

        JsonElement body = null;
        if (bodyStr != null && !bodyStr.isEmpty()) {
            // for now only JSON bodies are supported
            final String contentType = headers.get("Content-Type");
            if (!Objects.equals(contentType, "application/json"))
                return new ResponseBuilder()
                        .setCode(400)
                        .build();

            body = GSON.fromJson(bodyStr, JsonElement.class);
        }

        try {
            final Request request = new Request(this, endpoint, path, segments, headers, token, body);
            final ResponseBuilder builder = new ResponseBuilder();

            handler.handle(request, builder);

            return builder.build();
        } catch (APIException e) {
            return e.asResponse();
        } catch (Exception e) {
            return new ResponseBuilder()
                    .setCode(500)
                    .build();
        }
    }
}
