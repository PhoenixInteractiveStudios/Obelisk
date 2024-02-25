package org.burrow_studios.obelisk.shelly.net;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.burrow_studios.obelisk.commons.rpc.authentication.AuthenticationLevel;
import org.burrow_studios.obelisk.commons.rpc.exceptions.BadRequestException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.shelly.Shelly;
import org.jetbrains.annotations.NotNull;

public class GatewayAdapter {
    private final Shelly shelly;

    public GatewayAdapter(@NotNull Shelly shelly) {
        this.shelly = shelly;
    }

    public void onAuthenticate(@NotNull RPCRequest request, @NotNull RPCResponse.Builder response) throws RequestHandlerException {
        if (!(request.getBody() instanceof JsonObject requestBody))
            throw new BadRequestException("Missing request body");

        if (!(requestBody.get("token") instanceof JsonPrimitive tokenInfo))
            throw new BadRequestException("Malformed body: Missing token info");
        final String token = tokenInfo.getAsString();

        if (!(requestBody.get("level") instanceof JsonPrimitive levelInfo))
            throw new BadRequestException("Malformed body: Missing level info");
        final String levelStr = levelInfo.getAsString();
        final AuthenticationLevel level;
        try {
            level = AuthenticationLevel.valueOf(levelStr);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Malformed body: Invalid authentication level");
        }

        if (level == AuthenticationLevel.NONE) {
            response.setStatus(Status.NO_CONTENT);
            return;
        }

        DecodedJWT decodedJWT = null;

        try {
            if (level == AuthenticationLevel.IDENTITY)
                decodedJWT = this.shelly.getTokenManager().decodeIdentityToken(token);
            if (level == AuthenticationLevel.SESSION)
                decodedJWT = this.shelly.getTokenManager().decodeSessionToken(token);
        } catch (JWTVerificationException e) {
            throw new ForbiddenException(e);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }

        if (decodedJWT == null)
            throw new InternalServerErrorException();

        response.setStatus(Status.NO_CONTENT);
    }
}
