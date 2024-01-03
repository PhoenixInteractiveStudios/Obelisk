package org.burrow_studios.obelisk.server.net.http.exceptions;

import org.burrow_studios.obelisk.server.net.http.Method;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class IllegalMethodException extends APIException {
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
}
