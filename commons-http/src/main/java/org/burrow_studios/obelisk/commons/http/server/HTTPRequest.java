package org.burrow_studios.obelisk.commons.http.server;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.commons.http.Endpoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public record HTTPRequest(
        @NotNull HTTPServer handler,
        @NotNull Endpoint endpoint,
        @NotNull String path,
        @NotNull String[] segments,
        @NotNull Map<String, String> headers,
        @Nullable DecodedJWT token,
        @Nullable JsonElement body
) {
    public <T> T getSegment(int index, @NotNull Function<String, T> fun) {
        return fun.apply(segments[index]);
    }

    public long getSegmentLong(int index) {
        return this.getSegment(index, Long::parseLong);
    }

    public @NotNull Optional<JsonElement> optionalBody() {
        return Optional.ofNullable(body());
    }
}
