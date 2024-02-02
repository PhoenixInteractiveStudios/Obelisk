package org.burrow_studios.obelisk.commons.http.server.exceptions;

import org.burrow_studios.obelisk.commons.http.Method;
import org.burrow_studios.obelisk.commons.http.server.Response;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class IllegalMethodException extends RequestHandlerException {
    private final @NotNull Method used;
    private final @NotNull Method[] allowed;

    public IllegalMethodException(@NotNull Method used, @NotNull Method... allowed) {
        this.used = used;
        this.allowed = allowed;
    }

    public @NotNull Method getUsed() {
        return used;
    }

    public @NotNull Method[] getAllowed() {
        return allowed;
    }

    public @NotNull String[] getAllowedStr() {
        return Arrays.stream(allowed)
                .map(Enum::name)
                .toArray(String[]::new);
    }

    @Override
    public Response asResponse() {
        return new ResponseBuilder()
                .setCode(405)
                .setHeader("Allow", String.join(", ", getAllowedStr()))
                .build();
    }
}
