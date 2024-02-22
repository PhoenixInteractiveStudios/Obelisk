package org.burrow_studios.obelisk.commons.rpc.exceptions;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.jetbrains.annotations.NotNull;

public class BadRequestException extends RequestHandlerException {
    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public RPCResponse asResponse(@NotNull RPCRequest request) {
        JsonObject json = new JsonObject();
        json.addProperty("code", 400);
        json.addProperty("description", "Bad Request");
        json.addProperty("message", getMessage());

        return new RPCResponse.Builder(request)
                .setStatus(Status.BAD_REQUEST)
                .setBody(json)
                .build();
    }
}