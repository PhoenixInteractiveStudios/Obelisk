package org.burrow_studios.obelisk.gateway.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.Endpoint;
import org.burrow_studios.obelisk.commons.rpc.Method;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.authentication.AuthenticationLevel;
import org.burrow_studios.obelisk.commons.rpc.authentication.Authenticator;
import org.burrow_studios.obelisk.commons.rpc.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TimeBasedIdGenerator;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.gateway.ObeliskGateway;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationService implements Authenticator {
    private static final Logger LOG = Logger.getLogger("authentication");

    private final TimeBasedIdGenerator idGenerator = TimeBasedIdGenerator.get();

    private final ObeliskGateway gateway;
    private final YamlSection config;
    private final Endpoint proxy;

    public AuthenticationService(ObeliskGateway gateway, YamlSection config) {
        this.gateway = gateway;
        this.config = config;

        String endpoint = this.config.getAsPrimitive("endpoint").getAsString();
        this.proxy = Endpoint.build(Method.GET, endpoint, AuthenticationLevel.NONE);
    }

    @Override
    public void authenticate(@NotNull String token, @NotNull AuthenticationLevel level) throws InternalServerErrorException, ForbiddenException {
        if (level == AuthenticationLevel.NONE) return;

        JsonObject body = new JsonObject();
        body.addProperty("token", token);
        body.addProperty("level", level.name());

        RPCRequest request = proxy.builder()
                .setBody(body)
                .build(idGenerator.newId());

        try {
            RPCResponse response = this.gateway.handleInternal(proxy, request);

            if (!response.getStatus().isSuccess()) return;
        } catch (RequestHandlerException e) {
            LOG.log(Level.WARNING, "Encountered a RequestHandlerException when attempting to pass an authentication request to the backend service", e);
            throw new InternalServerErrorException();
        }

        try {
            String id = JWT.decode(token).getId();

            LOG.log(Level.FINE, "Request failed authentication for level " + level.name() + " from token " + id + " (not validated)");
        } catch (JWTDecodeException e) {
            LOG.log(Level.FINE, "Request failed authentication for level " + level.name() + " from invalid token");
        }

        throw new ForbiddenException();
    }
}
