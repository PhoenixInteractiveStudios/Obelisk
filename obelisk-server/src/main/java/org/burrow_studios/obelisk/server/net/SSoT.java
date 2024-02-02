package org.burrow_studios.obelisk.server.net;

import org.burrow_studios.obelisk.commons.http.CompiledEndpoint;
import org.burrow_studios.obelisk.commons.http.Endpoint;
import org.burrow_studios.obelisk.commons.http.Endpoints;
import org.burrow_studios.obelisk.commons.util.ArrayUtil;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.action.ActionImpl;
import org.burrow_studios.obelisk.core.source.DataProvider;
import org.burrow_studios.obelisk.core.source.EventProvider;
import org.burrow_studios.obelisk.core.source.Request;
import org.burrow_studios.obelisk.server.ObeliskServer;
import org.jetbrains.annotations.NotNull;

public class SSoT implements DataProvider, EventProvider {
    private static final Endpoint[]   AUTH_ENDPOINTS = Endpoints.getAuthEndpoints();
    private static final Endpoint[] ENTITY_ENDPOINTS = Endpoints.getEntityEndpoints();

    private final ObeliskServer server;

    public SSoT(@NotNull ObeliskServer server) {
        this.server = server;
    }

    @Override
    public @NotNull ObeliskImpl getAPI() {
        return this.server.getAPI();
    }

    @Override
    public @NotNull Request submitRequest(@NotNull ActionImpl<?> action) {
        final CompiledEndpoint compiledEndpoint = action.getEndpoint();
        final         Endpoint         endpoint = compiledEndpoint.endpoint();

        if (ArrayUtil.contains(AUTH_ENDPOINTS, endpoint))
            return server.getAuthenticator().submitRequest(action);
        else if (ArrayUtil.contains(ENTITY_ENDPOINTS, endpoint))
            return server.getEntityProvider().submitRequest(action);

        throw new RuntimeException("Not implemented");
    }
}
