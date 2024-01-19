package org.burrow_studios.obelisk.core.net;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.net.http.CompiledEndpoint;
import org.burrow_studios.obelisk.core.net.http.Method;
import org.burrow_studios.obelisk.core.net.http.Endpoints;
import org.burrow_studios.obelisk.core.net.socket.NetworkException;
import org.burrow_studios.obelisk.core.net.socket.SocketAdapter;
import org.burrow_studios.obelisk.core.source.DataProvider;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.core.source.Response;
import org.burrow_studios.obelisk.util.TimeBasedIdGenerator;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.*;

public class NetworkHandler implements DataProvider {
    private final ObeliskImpl api;
    private final TimeBasedIdGenerator requestIdGenerator = TimeBasedIdGenerator.get();
    private final ConcurrentHashMap<Long, Request> pendingRequests = new ConcurrentHashMap<>();
    private final SocketAdapter socketAdapter;
    private final HttpClient httpClient;
    private String sessionToken;
    final Gson gson;

    public NetworkHandler(@NotNull ObeliskImpl api) {
        this.api = api;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        this.socketAdapter = new SocketAdapter(this);
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();

        try {
            this.login();
            this.connectSocketAdapter();
        } catch (NetworkException e) {
            throw new RuntimeException(e);
        }
    }

    private CompletableFuture<Response> logout() {
        if (this.sessionToken == null)
            return CompletableFuture.completedFuture(null);

        final long sub = this.api.getSubjectId();
        final long id  = this.requestIdGenerator.newId();
        final TimeoutContext timeout = TimeoutContext.DEFAULT;

        try {
            final String sessionId = JWT.decode(this.sessionToken).getId();

            this.sessionToken = null;

            if (sessionId == null)
                return CompletableFuture.completedFuture(null);

            final CompiledEndpoint endpoint = Endpoints.LOGOUT.builder()
                    .withArg(sub)
                    .withArg(sessionId)
                    .compile();
            final Request request = new Request(this, id, endpoint, null, timeout);

            this.send(request);

            return request.getFuture();
        } catch (JWTDecodeException ignored) {
            this.sessionToken = null;
            return CompletableFuture.completedFuture(null);
        }
    }

    private void login() {
        try {
            this.logout().get();
        } catch (Exception ignored) { }

        final long sub = this.api.getSubjectId();
        final long id  = this.requestIdGenerator.newId();
        final CompiledEndpoint endpoint = Endpoints.LOGIN.builder().withArg(sub).compile();
        final TimeoutContext   timeout  = TimeoutContext.DEFAULT;
        final Request request = new Request(this, id, endpoint, null, timeout);

        this.send(request);

        try {
            Response response = request.getFuture().get(timeout.asTimeout(), TimeUnit.MILLISECONDS);

            if (response.getCode() != 200)
                throw new RuntimeException("Unexpected login response");

            this.sessionToken = Optional.of(response)
                    .map(Response::getContent)
                    .map(JsonElement::getAsJsonObject)
                    .map(json -> json.get("token"))
                    .map(JsonElement::getAsString)
                    .orElseThrow(() -> new RuntimeException("Unexpected login response"));
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

            final DecodedJWT token = JWT.decode(this.sessionToken);
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
        final CompletableFuture<Response> requestFuture = request.getFuture();
        final JsonElement content = request.getContent();
        final long id = request.getId();

        final HttpRequest.Builder builder = HttpRequest.newBuilder();

        builder.uri(request.getEndpoint().asURI());

        builder.timeout(request.getTimeout().asDuration());

        builder.header("Authorization", "Bearer " + api.getToken());

        final Method method = request.getEndpoint().method();
        if (content != null) {
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(gson.toJson(content)));
            builder.header("Content-Type", "application/json");
        } else {
            builder.method(method.name(), HttpRequest.BodyPublishers.noBody());
        }

        HttpRequest httpRequest = builder.build();

        this.httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .handle((httpResponse, throwable) -> {
                    if (throwable == null) {
                        Response response = makeResponse(httpResponse, request);
                        requestFuture.complete(response);
                    } else {
                        requestFuture.completeExceptionally(throwable);
                    }

                    this.pendingRequests.remove(id);
                    return null;
                });
    }

    private @NotNull Response makeResponse(@NotNull HttpResponse<String> httpResponse, @NotNull Request request) {
        if (!request.getProvider().equals(this))
            throw new IllegalArgumentException("Request must be to this provider");

        final String bodyStr = httpResponse.body();
        final JsonElement body = bodyStr == null ? null : gson.fromJson(bodyStr, JsonElement.class);

        return new Response(this, request.getId(), httpResponse.statusCode(), body);
    }
}
