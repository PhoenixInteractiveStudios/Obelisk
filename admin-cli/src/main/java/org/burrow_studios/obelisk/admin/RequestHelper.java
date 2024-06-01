package org.burrow_studios.obelisk.admin;

import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

public class RequestHelper {
    public static @NotNull HttpRequest.Builder get(@NotNull Route.Compiled route) throws IOException, URISyntaxException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();

        Config config = Config.get();

        String suffix = route.getPath().getPath();

        URI uri = new URI(config.getUrl() + suffix);

        builder.uri(uri);
        builder.method(route.getBase().getMethod().name(), HttpRequest.BodyPublishers.noBody());
        builder.setHeader("Authorization", "Bearer " + config.getToken());

        return builder;
    }
}
