package org.burrow_studios.obelisk.monolith.socket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Opcode;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.monolith.auth.ApplicationContext;
import org.burrow_studios.obelisk.monolith.exceptions.AuthenticationException;
import org.burrow_studios.obelisk.util.crypto.PassGen;
import org.burrow_studios.obelisk.util.crypto.SimpleSymmetricEncryption;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PacketHandler {
    private static final Logger LOG = LoggerFactory.getLogger(PacketHandler.class);

    private final ObeliskMonolith obelisk;
    private final HeartbeatManager heartbeatManager;

    public PacketHandler(@NotNull ObeliskMonolith obelisk, @NotNull HeartbeatManager heartbeatManager) {
        this.obelisk = obelisk;
        this.heartbeatManager = heartbeatManager;
    }

    public void onDisconnect(@NotNull Connection connection, @NotNull Packet packet) {
        try {
            connection.close();
        } catch (IOException e) {
            LOG.warn("Encountered an IOException when attempting to close connection", e);
        }
    }

    public void onIdentify(@NotNull Connection connection, @NotNull Packet packet) {
        JsonObject requestJson = packet.toJson();
        String token = requestJson.get("token").getAsString();

        try {
            ApplicationContext appCtx = this.obelisk.getAuthManager().authenticate(token);

            if (!appCtx.hasIntent("gateway")) {
                this.disconnect(connection, "Missing gateway intent");
                return;
            }
        } catch (AuthenticationException e) {
            this.disconnect(connection, "Invalid token");
            return;
        }

        // temporary encryption
        SimpleSymmetricEncryption tempCrypto = new SimpleSymmetricEncryption(token.toCharArray());
        connection.setEncryption(tempCrypto);

        // generate encryption key
        char[] encryptionKey = PassGen.generate(256).toCharArray();
        SimpleSymmetricEncryption encryption = new SimpleSymmetricEncryption(encryptionKey);

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("key", new String(encryptionKey));
        responseJson.addProperty("heartbeat_interval", HeartbeatManager.HEARTBEAT_INTERVAL);
        connection.send(new Packet(Opcode.ENCRYPTION, responseJson));

        // upgrade encryption for all subsequent packets
        connection.setEncryption(encryption);
    }

    public void onHeartbeat(@NotNull Connection connection, @NotNull Packet packet) {
        this.heartbeatManager.onHeartbeat(connection);

        // acknowledge heartbeat
        connection.send(new Packet(Opcode.HEARTBEAT_ACK));
    }

    public void onCacheRequest(@NotNull Connection connection, @NotNull Packet packet) {
        CacheWorker cacheWorker = new CacheWorker(this.obelisk, connection);
        cacheWorker.start();
    }

    public void onUnexpected(@NotNull Connection connection, @NotNull Packet packet) {
        this.disconnect(connection, "Unexpected packet");
    }

    private void disconnect(@NotNull Connection connection, @NotNull String reason) {
        JsonObject json = new JsonObject();
        json.addProperty("reason", reason);
        connection.send(new Packet(Opcode.DISCONNECT, json));

        try {
            connection.close();
        } catch (IOException e) {
            LOG.warn("Encountered an IOException when attempting to close connection", e);
        }
    }
}
