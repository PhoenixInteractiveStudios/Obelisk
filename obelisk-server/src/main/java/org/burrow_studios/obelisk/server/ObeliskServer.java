package org.burrow_studios.obelisk.server;

import org.burrow_studios.obelisk.api.ObeliskBuilder;
import org.burrow_studios.obelisk.core.ObeliskBuilderImpl;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.net.socket.NetworkException;
import org.burrow_studios.obelisk.server.auth.Authenticator;
import org.burrow_studios.obelisk.server.db.EntityProvider;
import org.burrow_studios.obelisk.server.event.EventManager;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.permissions.PermissionManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class ObeliskServer {
    private final Authenticator     authenticator;
    private final EntityProvider    entityProvider;
    private final EventManager      eventManager;
    private final NetworkHandler    networkHandler;
    private final PermissionManager permissionManager;

    private final ObeliskImpl api;

    ObeliskServer() throws IOException, NetworkException {
        this.authenticator     = new Authenticator(this);
        this.entityProvider    = new EntityProvider(this);
        this.eventManager      = new EventManager(this);
        this.networkHandler    = new NetworkHandler(this);
        this.permissionManager = new PermissionManager(this);

        this.api = ((ObeliskBuilderImpl) ObeliskBuilder.create())
                .setDataProvider(obelisk -> entityProvider)
                .build();
    }

    void stop() {

    }

    public @NotNull Authenticator getAuthenticator() {
        return authenticator;
    }

    public @NotNull EntityProvider getEntityProvider() {
        return entityProvider;
    }

    public @NotNull EventManager getEventManager() {
        return eventManager;
    }

    public @NotNull NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public @NotNull PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public @NotNull ObeliskImpl getAPI() {
        return api;
    }
}
