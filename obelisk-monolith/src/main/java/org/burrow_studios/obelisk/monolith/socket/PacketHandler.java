package org.burrow_studios.obelisk.monolith.socket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Opcode;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.monolith.ObeliskMonolith;
import org.burrow_studios.obelisk.util.crypto.PassGen;
import org.burrow_studios.obelisk.util.crypto.SimpleSymmetricEncryption;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PacketHandler {
    private final ObeliskMonolith obelisk;

    public PacketHandler(@NotNull ObeliskMonolith obelisk) {
        this.obelisk = obelisk;
    }

    public void onDisconnect(@NotNull Connection connection, @NotNull Packet packet) {
        try {
            connection.close();
        } catch (IOException ignored) {
            // TODO: handle or log?
        }
    }

    public void onIdentify(@NotNull Connection connection, @NotNull Packet packet) {
        JsonObject requestJson = packet.toJson();
        // TODO: authenticate

        // TODO: get initial encryption key
        char[] initialKey = new char[]{};
        SimpleSymmetricEncryption initialEncryption = new SimpleSymmetricEncryption(initialKey);
        connection.setEncryption(initialEncryption);

        // generate encryption key
        char[] encryptionKey = PassGen.generate(256).toCharArray();
        SimpleSymmetricEncryption encryption = new SimpleSymmetricEncryption(encryptionKey);

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("key", new String(encryptionKey));
        connection.send(new Packet(Opcode.ENCRYPTION, responseJson));

        // upgrade encryption for all subsequent packets
        connection.setEncryption(encryption);
    }

    public void onHeartbeat(@NotNull Connection connection, @NotNull Packet packet) {
        // acknowledge heartbeat
        connection.send(new Packet(Opcode.HEARTBEAT_ACK));
    }

    public void onCacheRequest(@NotNull Connection connection, @NotNull Packet packet) {
        CacheWorker cacheWorker = new CacheWorker(this.obelisk, connection);
        cacheWorker.start();
    }

    public void onUnexpected(@NotNull Connection connection, @NotNull Packet packet) {
        JsonObject json = new JsonObject();
        json.addProperty("reason", "Unexpected packet");
        connection.send(new Packet(Opcode.DISCONNECT, json));

        try {
            connection.close();
        } catch (IOException e) {
            // TODO: handle or log?
        }
    }
}
