package org.burrow_studios.obelisk.gateway.authentication;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.*;
import org.burrow_studios.obelisk.commons.rpc.authentication.AuthenticationLevel;
import org.burrow_studios.obelisk.commons.rpc.authentication.Authenticator;
import org.burrow_studios.obelisk.commons.rpc.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TimeBasedIdGenerator;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.gateway.ObeliskGateway;
import org.jetbrains.annotations.NotNull;

public class AuthenticationService implements Authenticator {
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
            // TODO: log
            throw new InternalServerErrorException();
        }

        throw new ForbiddenException();
    }
}
