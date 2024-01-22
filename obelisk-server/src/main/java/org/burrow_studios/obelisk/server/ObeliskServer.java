package org.burrow_studios.obelisk.server;

import com.google.gson.*;
import org.burrow_studios.obelisk.api.ObeliskBuilder;
import org.burrow_studios.obelisk.core.ObeliskBuilderImpl;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.net.socket.NetworkException;
import org.burrow_studios.obelisk.server.auth.Authenticator;
import org.burrow_studios.obelisk.server.db.DatabaseException;
import org.burrow_studios.obelisk.server.db.EntityProvider;
import org.burrow_studios.obelisk.server.event.EventManager;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.permissions.PermissionManager;
import org.burrow_studios.obelisk.util.ResourceTools;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public final class ObeliskServer {
    private final Gson gson;
    private final File configFile;
    private final JsonObject config;

    private final Authenticator     authenticator;
    private final EntityProvider    entityProvider;
    private final EventManager      eventManager;
    private final NetworkHandler    networkHandler;
    private final PermissionManager permissionManager;

    private final ObeliskImpl api;

    ObeliskServer() throws IOException, DatabaseException, NetworkException {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        this.configFile = new File(Main.DIR, "config.json");
        if (this.configFile.createNewFile()) {
            InputStream defaultConfig = ResourceTools.get(Main.class).getResource("config.json");
            if (defaultConfig == null)
                throw new Error("Implementation error: Missing default config");
            Files.copy(defaultConfig, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        try {
            this.config = this.gson.fromJson(new FileReader(this.configFile), JsonObject.class);
        } catch (FileNotFoundException | JsonIOException | JsonSyntaxException e) {
            throw new IOException("Unable to read config file", e);
        }

        if (this.config == null)
            throw new IOException("Empty config file");

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
        try {
            this.gson.toJson(this.config, new FileWriter(this.configFile, false));
        } catch (IOException e) {
            // TODO: log
        }
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

    public @NotNull JsonObject getConfig() {
        return config;
    }
}
