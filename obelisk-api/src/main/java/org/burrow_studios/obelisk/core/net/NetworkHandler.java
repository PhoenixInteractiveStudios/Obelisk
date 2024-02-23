package org.burrow_studios.obelisk.core.net;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.*;
import org.burrow_studios.obelisk.commons.rpc.http.HTTPClient;
import org.burrow_studios.obelisk.commons.turtle.TimeBasedIdGenerator;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.net.socket.NetworkException;
import org.burrow_studios.obelisk.core.net.socket.SocketAdapter;
import org.burrow_studios.obelisk.core.source.DataProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.BiFunction;

public class NetworkHandler implements DataProvider {
    private final ObeliskImpl api;
    private final TimeBasedIdGenerator requestIdGenerator = TimeBasedIdGenerator.get();
    private final Set<RPCRequest> pendingRequests = ConcurrentHashMap.newKeySet();
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
        this.httpClient = new HTTPClient("https://api.burrow-studios.org/v1", identityToken, null);

        try {
            this.login();
            this.connectSocketAdapter();
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<RPCResponse> logout() {
        if (this.httpClient.getSessionToken() == null)
            return CompletableFuture.completedFuture(null);

        final long id = this.requestIdGenerator.newId();

        try {
            final String sessionId = JWT.decode(this.httpClient.getSessionToken()).getId();

            this.httpClient.setSessionToken(null);

            if (sessionId == null)
                return CompletableFuture.completedFuture(null);

            final RPCRequest request = Endpoints.LOGOUT.builder(this.subjectId, sessionId).build(id);

            return this.send(request);
        } catch (JWTDecodeException ignored) {
            this.httpClient.setSessionToken(null);
            return CompletableFuture.completedFuture(null);
        }
    }

    private void login() {
        try {
            this.logout().get();
        } catch (Exception ignored) { }

        final long id = this.requestIdGenerator.newId();
        final TimeoutContext timeout = TimeoutContext.DEFAULT;
        final RPCRequest request = Endpoints.LOGIN.builder(this.subjectId).build(id);

        try {
            RPCResponse response = this.send(request).get(timeout.asTimeout(), TimeUnit.MILLISECONDS);

            if (response.getStatus() != Status.OK)
                throw new RuntimeException("Unexpected login response");

            String sessionToken = Optional.of(response)
                    .map(RPCResponse::getBody)
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
        final TimeoutContext timeout = TimeoutContext.DEFAULT;
        final RPCRequest request = Endpoints.GET_SOCKET.builder().build(id);

        try {
            RPCResponse response = this.send(request).get();

            if (response.getStatus() != Status.OK)
                throw new RuntimeException("Unexpected response to socket-request");

            JsonObject json = Optional.of(response)
                    .map(RPCResponse::getBody)
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

    @Override
    public <T> CompletableFuture<T> submitRequest(@NotNull ActionImpl<?> action, @NotNull BiFunction<RPCRequest, RPCResponse, T> mapper) {
        final long id = this.requestIdGenerator.newId();

        RPCRequest.Builder builder = action.requestBuilder();
        // TODO: auth?
        RPCRequest request = builder.build(id);

        CompletableFuture<T> future = new CompletableFuture<>();

        this.send(request).whenComplete((response, throwable) -> {
            if (response != null)
                future.complete(mapper.apply(request, response));
            if (throwable != null)
                future.completeExceptionally(throwable);
        });

        return future;
    }

    public @NotNull CompletableFuture<RPCResponse> send(@NotNull RPCRequest request) {
        this.pendingRequests.add(request);

        try {
            return this.httpClient.send(request).whenComplete(
                    // remove from pending requests when complete (regardless of outcome)
                    (response, throwable) -> this.pendingRequests.remove(request)
            );
        } catch (Exception e) {
            this.pendingRequests.remove(request);
            return CompletableFuture.failedFuture(e);
        }
    }
}
