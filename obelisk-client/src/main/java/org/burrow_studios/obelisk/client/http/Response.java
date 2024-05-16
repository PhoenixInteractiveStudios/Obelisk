package org.burrow_studios.obelisk.client.http;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.http.HttpResponse;

import static org.burrow_studios.obelisk.client.http.HTTPClient.GSON;

public class Response {
    private final int code;
    private final String body;
    private final JsonElement bodyJson;
    private final HttpResponse<String> rawResponse;

    public Response(@NotNull HttpResponse<String> rawResponse) {
        this.code = rawResponse.statusCode();
        this.body = rawResponse.body();
        this.rawResponse = rawResponse;

        if (this.body == null) {
            this.bodyJson = null;
        } else {
            this.bodyJson = GSON.fromJson(this.body, JsonElement.class);
        }
    }

    public int getCode() {
        return this.code;
    }

    public boolean isOk() {
        return this.code >= 200 && this.code < 300;
    }

    public String getBody() {
        return this.body;
    }

    public @Nullable JsonElement getBodyJson() {
        return this.bodyJson;
    }
}
