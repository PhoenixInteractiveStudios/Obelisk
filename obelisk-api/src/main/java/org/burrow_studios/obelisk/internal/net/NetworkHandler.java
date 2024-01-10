package org.burrow_studios.obelisk.internal.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.util.TimeBasedIdGenerator;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.action.ActionImpl;
import org.burrow_studios.obelisk.internal.net.http.Method;
import org.jetbrains.annotations.NotNull;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkHandler {
    private final ObeliskImpl api;
    private final TimeBasedIdGenerator requestIdGenerator = TimeBasedIdGenerator.get();
    private final ConcurrentHashMap<Long, Request> pendingRequests = new ConcurrentHashMap<>();
    private final HttpClient httpClient;
    final Gson gson;

    public NetworkHandler(@NotNull ObeliskImpl api) {
        this.api = api;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();
    }

    public @NotNull Request submitRequest(@NotNull ActionImpl<?> action) {
        final long id = this.requestIdGenerator.newId();

        final Request request = new Request(
                this,
                id,
                action.getRoute(),
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

        builder.uri(request.getRoute().asURI());

        builder.timeout(request.getTimeout().asDuration());

        builder.header("Authorization", "Bearer " + api.getToken());

        final Method method = request.getRoute().method();
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
                        Response response = Response.ofHttpResponse(httpResponse, request);
                        requestFuture.complete(response);
                    } else {
                        requestFuture.completeExceptionally(throwable);
                    }

                    this.pendingRequests.remove(id);
                    return null;
                });
    }
}
