package org.burrow_studios.obelisk.client.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.burrow_studios.obelisk.client.config.AuthConfig;
import org.burrow_studios.obelisk.client.config.HttpConfig;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.http.HttpClient;

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
        // TODO
    }
}
