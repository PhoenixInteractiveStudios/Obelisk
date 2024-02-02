package org.burrow_studios.obelisk.core.net;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.http.CompiledEndpoint;
import org.burrow_studios.obelisk.commons.http.Endpoints;
import org.burrow_studios.obelisk.commons.http.HTTPResponse;
import org.burrow_studios.obelisk.commons.http.TimeoutContext;
import org.burrow_studios.obelisk.commons.http.client.HTTPClient;
import org.burrow_studios.obelisk.commons.turtle.TimeBasedIdGenerator;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.net.socket.NetworkException;
import org.burrow_studios.obelisk.core.net.socket.SocketAdapter;
import org.burrow_studios.obelisk.core.source.DataProvider;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.*;

public class NetworkHandler implements DataProvider {
    private final ObeliskImpl api;
    private final TimeBasedIdGenerator requestIdGenerator = TimeBasedIdGenerator.get();
    private final ConcurrentHashMap<Long, Request> pendingRequests = new ConcurrentHashMap<>();
    private final SocketAdapter socketAdapter;
    private final HTTPClient httpClient;
    private final long subjectId;
    final Gson gson;

    public NetworkHandler(@NotNull ObeliskImpl api, @NotNull String identityToken) {
        this.api = api;

        try {
            this.subjectId = Long.parseLong(JWT.decode(identityToken).getSubject());
        } catch (JWTDecodeException | NullPointerException | NumberFormatException e) {
            throw new IllegalArgumentException("Invalid token");
        }

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        this.socketAdapter = new SocketAdapter(this);
        this.httpClient = new HTTPClient(identityToken, null);

        try {
            this.login();
            this.connectSocketAdapter();
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<Response> logout() {
        if (this.httpClient.getSessionToken() == null)
            return CompletableFuture.completedFuture(null);

        final long id  = this.requestIdGenerator.newId();
        final TimeoutContext timeout = TimeoutContext.DEFAULT;

        try {
            final String sessionId = JWT.decode(this.httpClient.getSessionToken()).getId();

            this.httpClient.setSessionToken(null);

            if (sessionId == null)
                return CompletableFuture.completedFuture(null);

            final CompiledEndpoint endpoint = Endpoints.LOGOUT.builder()
                    .withArg(this.subjectId)
                    .withArg(sessionId)
                    .compile();
            final Request request = new Request(this, id, endpoint, null, timeout);

            this.send(request);

            return request.getFuture();
        } catch (JWTDecodeException ignored) {
            this.httpClient.setSessionToken(null);
            return CompletableFuture.completedFuture(null);
        }
    }

    private void login() {
        try {
            this.logout().get();
        } catch (Exception ignored) { }

        final long id  = this.requestIdGenerator.newId();
        final CompiledEndpoint endpoint = Endpoints.LOGIN.builder()
                .withArg(this.subjectId)
                .compile();
        final TimeoutContext   timeout  = TimeoutContext.DEFAULT;
        final Request request = new Request(this, id, endpoint, null, timeout);

        this.send(request);

        try {
            Response response = request.getFuture().get(timeout.asTimeout(), TimeUnit.MILLISECONDS);

            if (response.getCode() != 200)
                throw new RuntimeException("Unexpected login response");

            String sessionToken = Optional.of(response)
                    .map(Response::getContent)
                    .map(JsonElement::getAsJsonObject)
                    .map(json -> json.get("token"))
                    .map(JsonElement::getAsString)
                    .orElseThrow(() -> new RuntimeException("Unexpected login response"));

            this.httpClient.setSessionToken(sessionToken);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectSocketAdapter() throws NetworkException {
        final long id = this.requestIdGenerator.newId();
        final CompiledEndpoint endpoint = Endpoints.GET_SOCKET.builder().compile();
        final TimeoutContext timeout = TimeoutContext.DEFAULT;
        final Request request = new Request(this, id, endpoint, null, timeout);

        this.send(request);

        try {
            Response response = request.getFuture().get();

            if (response.getCode() != 200)
                throw new RuntimeException("Unexpected response to socket-request");

            JsonObject json = Optional.of(response)
                    .map(Response::getContent)
                    .map(JsonElement::getAsJsonObject)
                    .orElseThrow(() -> new RuntimeException("Unexpected response to socket-request"));

            final String host = json.get("host").getAsString();
            final int    port = json.get("port").getAsInt();

            final DecodedJWT token = JWT.decode(this.httpClient.getSessionToken());
            final String sub = token.getSubject();
            final String sid = token.getId();
            final String sok = token.getClaim("sok").asString();

            this.socketAdapter.connect(host, port, sub, sid, sok);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } catch (JWTDecodeException | NullPointerException e) {
            throw new RuntimeException("Unable to decode session token", e);
        }
    }

    @Override
    public @NotNull ObeliskImpl getAPI() {
        return this.api;
    }

    public @NotNull Request submitRequest(@NotNull ActionImpl<?> action) {
        final long id = this.requestIdGenerator.newId();

        final Request request = new Request(
                this,
                id,
                action.getEndpoint(),
                action.getContent(),
                TimeoutContext.DEFAULT
        );

        this.pendingRequests.put(id, request);
        this.send(request);
        return request;
    }

    private void send(@NotNull Request request) {
        CompletableFuture<HTTPResponse> callback = this.httpClient.send(request.getEndpoint(), request.getContent(), request.getTimeout());
        callback.handle((httpResponse, throwable) -> {
            if (httpResponse != null)
                request.getFuture().complete(makeResponse(httpResponse, request));
            if (throwable != null)
                request.getFuture().completeExceptionally(throwable);
            return null;
        });
    }

    private @NotNull Response makeResponse(@NotNull HTTPResponse httpResponse, @NotNull Request request) {
        if (!request.getProvider().equals(this))
            throw new IllegalArgumentException("Request must be to this provider");

        final String bodyStr = httpResponse.body();
        final JsonElement body = bodyStr == null ? null : gson.fromJson(bodyStr, JsonElement.class);

        return new Response(this, request.getId(), httpResponse.code(), body);
    }
}
