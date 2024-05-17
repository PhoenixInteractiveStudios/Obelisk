package org.burrow_studios.obelisk.monolith.http.exceptions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.http.Method;
import org.burrow_studios.obelisk.monolith.http.Response;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

public class IllegalMethodException extends RequestHandlerException {
    private final @NotNull Method used;
    private final @NotNull Method[] allowed;

    public IllegalMethodException(@NotNull Method used, @NotNull Method[] allowed) {
        super();
        this.used = used;
        this.allowed = allowed;
    }

    public @NotNull Method getUsed() {
        return this.used;
    }

    public @NotNull Method[] getAllowed() {
        return this.allowed;
    }

    @Override
    public @NotNull Response toResponse() {
        JsonObject details = new JsonObject();
        JsonArray allowedMethods = new JsonArray(this.allowed.length);
        for (Method method : this.allowed)
            allowedMethods.add(method.name());
        details.add("allow", allowedMethods);

        String[] allowed = Arrays.stream(this.allowed)
                .map(Enum::name)
                .toArray(String[]::new);
        String allowedStr = String.join(", ", allowed);

        return Response.error(405, "Method Not Allowed", details, Map.of("Allow", allowedStr));
    }
}
