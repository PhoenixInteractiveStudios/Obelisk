package org.burrow_studios.obelisk.shelly.net;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.shelly.Shelly;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.Base64;

public class PubKeyHandler {
    private final @NotNull Shelly shelly;

    public PubKeyHandler(@NotNull Shelly shelly) {
        this.shelly = shelly;
    }

    public void onGetPublicIdentityKey(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final long subject = request.getPathSegmentAsLong(1);

        final JsonObject responseJson = new JsonObject();

        try {
            final PublicKey key      = shelly.getTokenManager().getPublicIdentityKey(subject);
            final byte[]    keyBytes = Base64.getEncoder().encode(key.getEncoded());
            final String    keyStr   = new String(keyBytes);

            responseJson.addProperty("key", keyStr);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setBody(responseJson);
        response.setStatus(Status.OK);
    }

    public void onGetPublicSessionKey(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        final JsonObject responseJson = new JsonObject();

        try {
            final PublicKey key      = shelly.getTokenManager().getCurrentPublicSessionKey();
            final byte[]    keyBytes = Base64.getEncoder().encode(key.getEncoded());
            final String    keyStr   = new String(keyBytes);

            responseJson.addProperty("key", keyStr);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        response.setBody(responseJson);
        response.setStatus(Status.OK);
    }
}
