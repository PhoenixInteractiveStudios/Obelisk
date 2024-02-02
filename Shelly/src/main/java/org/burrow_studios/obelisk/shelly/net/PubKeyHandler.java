package org.burrow_studios.obelisk.shelly.net;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.http.server.HTTPRequest;
import org.burrow_studios.obelisk.commons.http.server.ResponseBuilder;
import org.burrow_studios.obelisk.commons.http.server.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.http.server.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.shelly.Shelly;
import org.jetbrains.annotations.NotNull;

import java.security.PublicKey;
import java.util.Base64;

public class PubKeyHandler {
    private final @NotNull Shelly shelly;

    public PubKeyHandler(@NotNull Shelly shelly) {
        this.shelly = shelly;
    }

    public void onGetPublicIdentityKey(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
        final long subject = request.getSegmentLong(1);

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
        response.setCode(200);
    }

    public void onGetPublicSessionKey(@NotNull HTTPRequest request, @NotNull ResponseBuilder response) throws RequestHandlerException {
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
        response.setCode(200);
    }
}
