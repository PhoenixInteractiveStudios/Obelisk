package org.burrow_studios.obelisk.gateway.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.commons.rpc.Endpoint;
import org.burrow_studios.obelisk.commons.rpc.Method;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.authentication.AuthenticationLevel;
import org.burrow_studios.obelisk.commons.rpc.authorization.Authorizer;
import org.burrow_studios.obelisk.commons.rpc.exceptions.ForbiddenException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.InternalServerErrorException;
import org.burrow_studios.obelisk.commons.rpc.exceptions.RequestHandlerException;
import org.burrow_studios.obelisk.commons.turtle.TimeBasedIdGenerator;
import org.burrow_studios.obelisk.commons.yaml.YamlSection;
import org.burrow_studios.obelisk.gateway.ObeliskGateway;
import org.jetbrains.annotations.NotNull;

public class AuthorizationService implements Authorizer {
    private final TimeBasedIdGenerator idGenerator = TimeBasedIdGenerator.get();

    private final ObeliskGateway gateway;
    private final YamlSection config;
    private final Endpoint proxy;

    public AuthorizationService(ObeliskGateway gateway, YamlSection config) {
        this.gateway = gateway;
        this.config = config;

        String endpoint = this.config.getAsPrimitive("endpoint").getAsString();
        this.proxy = Endpoint.build(Method.GET, endpoint, AuthenticationLevel.NONE);
    }

    @Override
    public void authorize(@NotNull String token, @NotNull String[] intents) throws InternalServerErrorException, ForbiddenException {
        if (intents.length == 0) return;

        final DecodedJWT decodedJWT = JWT.decode(token);
        final String application = decodedJWT.getSubject();

        JsonObject body = new JsonObject();
        body.addProperty("application", application);

        JsonArray intentArr = new JsonArray();
        for (String intent : intents)
            intentArr.add(intent);

        body.add("intents", intentArr);

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
