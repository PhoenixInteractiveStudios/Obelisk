package org.burrow_studios.obelisk.commons.http.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.commons.http.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class HTTPClient {
    static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final HttpClient httpClient;

    private @Nullable String identityToken;
    private @Nullable String sessionToken;

    public HTTPClient(@Nullable String identityToken, @Nullable String sessionToken) {
        this.identityToken = identityToken;
        this.sessionToken  = sessionToken;

        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();
    }

    public @NotNull CompletableFuture<HTTPResponse> send(@NotNull CompiledEndpoint endpoint, @Nullable JsonElement content, @NotNull TimeoutContext timeout) {
        final HttpRequest.Builder builder = HttpRequest.newBuilder();

        builder.uri(endpoint.asURI());

        builder.timeout(timeout.asDuration());

        final AuthLevel privilege = endpoint.endpoint().getPrivilege();
        if (privilege == AuthLevel.IDENTITY)
            builder.header("Authorization", "Bearer " + identityToken);
        if (privilege == AuthLevel.SESSION)
            builder.header("Authorization", "Bearer " + sessionToken);

        final Method method = endpoint.method();
        if (content != null) {
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(GSON.toJson(content)));
            builder.header("Content-Type", "application/json");
        } else {
            builder.method(method.name(), HttpRequest.BodyPublishers.noBody());
        }

        HttpRequest httpRequest = builder.build();

        CompletableFuture<HTTPResponse> callback = new CompletableFuture<>();

        this.httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .handle((httpResponse, throwable) -> {
                    try {
                        handleResponse(callback, httpResponse, throwable);
                    } catch (Exception e) {
                        callback.completeExceptionally(e);
                    }
                    return null;
                });

        return callback;
    }

    private static void handleResponse(@NotNull CompletableFuture<HTTPResponse> callback, HttpResponse<String> httpResponse, Throwable throwable) {
        if (throwable == null) {
            // re-map headers to single strings per header value (HttpResponse only provides lists)
            Map<String, String> headers = new LinkedHashMap<>();
            httpResponse.headers().map().forEach((key, val) -> {
                headers.put(key, String.join(", ", val));
            });

            // build response
            HTTPResponse response = new HTTPResponse(httpResponse.statusCode(), headers, httpResponse.body());

            // answer callback
            callback.complete(response);
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
}
