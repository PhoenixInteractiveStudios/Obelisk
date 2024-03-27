package org.burrow_studios.obelisk.commons.rpc.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.burrow_studios.obelisk.commons.rpc.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HTTPClient extends RPCClient {
    static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final HttpClient httpClient;

    private final @NotNull String host;

    private @Nullable String identityToken;
    private @Nullable String sessionToken;

    public HTTPClient(@NotNull String host, @Nullable String identityToken, @Nullable String sessionToken) {
        this.host = host;

        this.identityToken = identityToken;
        this.sessionToken  = sessionToken;

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();
    }

    @Override
    public @NotNull CompletableFuture<RPCResponse> send(@NotNull RPCRequest request) throws IOException {
        final HttpRequest.Builder builder = HttpRequest.newBuilder();

        builder.uri(URI.create(host + request.getPath()));

        // TODO: timeout

        /*
        final AuthLevel privilege = endpoint.endpoint().getPrivilege();
        if (privilege == AuthLevel.IDENTITY)
            builder.header("Authorization", "Bearer " + identityToken);
        if (privilege == AuthLevel.SESSION)
            builder.header("Authorization", "Bearer " + sessionToken);
        */

        final Method method = request.getMethod();
        if (request.getBody() != null) {
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(GSON.toJson(request.getBody())));
            builder.header("Content-Type", "application/json");
        } else {
            builder.method(method.name(), HttpRequest.BodyPublishers.noBody());
        }

        HttpRequest httpRequest = builder.build();

        CompletableFuture<RPCResponse> callback = request.getFuture();

        this.httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .handle((httpResponse, throwable) -> {
                    try {
                        handleResponse(callback, request, httpResponse, throwable);
                    } catch (Exception e) {
                        callback.completeExceptionally(e);
                    }
                    return null;
                });


        // schedule timeout
        scheduler.schedule(() -> {
            if (callback.isDone()) return;

            callback.completeExceptionally(new TimeoutException());
        }, request.getTimeout().asTimeout(), TimeUnit.MILLISECONDS);

        return callback;
    }

    private static void handleResponse(@NotNull CompletableFuture<RPCResponse> callback, @NotNull RPCRequest request, HttpResponse<String> httpResponse, Throwable throwable) {
        if (throwable == null) {
            RPCResponse.Builder builder = new RPCResponse.Builder(request);

            // headers
            httpResponse.headers().map().forEach((key, val) -> builder.setHeaders(key, val));

            // status
            Status status = null;
            for (Status value : Status.values()) {
                if (value.getCode() != httpResponse.statusCode()) continue;
                status = value;
                break;
            }
            if (status == null)
                throw new IllegalArgumentException("Unknown status code: " + httpResponse.statusCode());
            builder.setStatus(status);

            // body
            builder.setBody(httpResponse.body());

            // answer callback
            callback.complete(builder.build());
        } else {
            callback.completeExceptionally(throwable);
        }
    }

    public void setIdentityToken(@Nullable String identityToken) {
        this.identityToken = identityToken;
    }

    public void setSessionToken(@Nullable String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public @Nullable String getSessionToken() {
        return sessionToken;
    }

    @Override
    public void close() {
        // do nothing
    }
}
