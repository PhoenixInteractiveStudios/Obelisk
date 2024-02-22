package org.burrow_studios.obelisk.commons.rpc.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.jetbrains.annotations.NotNull;

public class ForbiddenException extends RequestHandlerException {
    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(JWTVerificationException cause) {
        super(cause);
    }

    @Override
    public RPCResponse asResponse(@NotNull RPCRequest request) {
        JsonObject json = new JsonObject();
        json.addProperty("code", 403);
        json.addProperty("description", "Forbidden");
        json.addProperty("message", getMessage());

        return new RPCResponse.Builder(request)
                .setStatus(Status.FORBIDDEN)
                .setBody(json)
                .build();
    }
}
