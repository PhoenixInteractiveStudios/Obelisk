package org.burrow_studios.obelisk.commons.rpc.exceptions;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.jetbrains.annotations.NotNull;

public class UnauthorizedException extends RequestHandlerException {
    @Override
    public RPCResponse asResponse(@NotNull RPCRequest request) {
        JsonObject json = new JsonObject();
        json.addProperty("code", 401);
        json.addProperty("description", "Unauthorized");
        json.addProperty("message", getMessage());

        return new RPCResponse.Builder(request)
                .setStatus(Status.UNAUTHORIZED)
                .setBody(json)
                .build();
    }
}
