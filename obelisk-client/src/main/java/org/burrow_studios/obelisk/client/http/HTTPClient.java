package org.burrow_studios.obelisk.client.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.client.config.AuthConfig;
import org.burrow_studios.obelisk.client.config.HttpConfig;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPClient {
    private static final Logger LOG = LoggerFactory.getLogger(HTTPClient.class);

    static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .create();

    private final AuthConfig authConfig;
    private final HttpConfig httpConfig;
    private final HttpClient client;

    public HTTPClient(@NotNull AuthConfig authConfig, @NotNull HttpConfig httpConfig) {
        this.authConfig = authConfig;
        this.httpConfig = httpConfig;

        this.client = HttpClient.newBuilder()
                .build();
    }

    public <T> void request(Request<T> request) {
        HttpRequest.Builder builder = HttpRequest.newBuilder();

        // auth
        builder.header("Authorization", "Bearer " + authConfig.getToken());

        // request body
        HttpRequest.BodyPublisher bodyPublisher;
        JsonElement bodyJson = request.getBody();
        if (bodyJson == null) {
            bodyPublisher = HttpRequest.BodyPublishers.noBody();
        } else if (bodyJson.isJsonNull()) {
            bodyPublisher = HttpRequest.BodyPublishers.ofString("");
        } else {
            String bodyString = GSON.toJson(bodyJson);
            bodyPublisher = HttpRequest.BodyPublishers.ofString(bodyString);
        }

        // request method
        Route.Compiled route = request.getRoute();
        builder.method(route.getBase().getMethod().name(), bodyPublisher);

        // request URI
        try {
            URI uri = new URI(httpConfig.getBaseUrl() + route.getPath().getPath());
            builder.uri(uri);
        } catch (URISyntaxException e) {
            // TODO: should the raw ex be passed here?
            request.onFailure(e);
            return;
        }

        // request headers
        request.getHeaders().forEach(builder::header);

        HttpResponse<String> rawResponse;
        try {
            rawResponse = this.client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            LOG.warn("Could not send request due to an IOException", e);
            request.onFailure(e);
            return;
        } catch (InterruptedException e) {
            LOG.warn("Interrupted when attempting to send request", e);
            request.onFailure(e);
            return;
        } catch (Throwable t) {
            LOG.error("Encountered an unexpected Throwable when attempting to send request", t);
            // TODO: should the raw Throwable be passed here?
            request.onFailure(t);
            throw t;
        }

        Response response = new Response(rawResponse);
        request.handleResponse(response);
    }
}
