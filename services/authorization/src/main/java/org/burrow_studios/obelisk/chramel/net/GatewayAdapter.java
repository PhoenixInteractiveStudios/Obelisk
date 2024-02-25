package org.burrow_studios.obelisk.chramel.net;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.chramel.Chramel;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.BadRequestException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.util.validation.Validation;
import org.jetbrains.annotations.NotNull;

public class GatewayAdapter {
    private static final String LEGAL_INTENT_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String LEGAL_INTENT_DELIMITERS = "-._";

    private final Chramel chramel;

    public GatewayAdapter(@NotNull Chramel chramel) {
        this.chramel = chramel;
    }

    public void onAuthorize(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        if (!(request.getBody() instanceof JsonObject requestBody))
            throw new BadRequestException("Missing request body");

        if (!(requestBody.get("application") instanceof JsonPrimitive applicationInfo))
            throw new BadRequestException("Malformed body: Missing application info");
        final long application = applicationInfo.getAsLong();

        if (!(requestBody.get("intent") instanceof JsonPrimitive intentInfo))
            throw new BadRequestException("Malformed body: Missing intent info");
        final String intent = intentInfo.getAsString();

        try {
            Validation.of("Intent", intent)
                    .checkNonNull()
                    .checkNotBlank()
                    .checkCharWhitelist(LEGAL_INTENT_CHARS, LEGAL_INTENT_DELIMITERS);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Illegal intent");
        }

        boolean b = this.chramel.getDatabase().hasIntent(application, intent);

        response.setStatus(b ? Status.NO_CONTENT : Status.FORBIDDEN);
    }
}
