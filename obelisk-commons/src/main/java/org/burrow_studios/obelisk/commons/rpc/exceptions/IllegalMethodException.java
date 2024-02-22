package org.burrow_studios.obelisk.commons.rpc.exceptions;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.Method;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
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
    public RPCResponse asResponse(@NotNull RPCRequest request) {
        JsonObject json = new JsonObject();
        json.addProperty("code", 405);
        json.addProperty("description", "Method Not Allowed");
        json.addProperty("message", getMessage());

        return new RPCResponse.Builder(request)
                .setStatus(Status.METHOD_NOT_ALLOWED)
                .setBody(json)
                .build();
    }
}
