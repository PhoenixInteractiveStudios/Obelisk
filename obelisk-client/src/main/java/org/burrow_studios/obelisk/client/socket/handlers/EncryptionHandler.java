package org.burrow_studios.obelisk.client.socket.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Opcode;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.core.socket.PacketHandler;
import org.burrow_studios.obelisk.util.crypto.SimpleSymmetricEncryption;
import org.jetbrains.annotations.NotNull;

public class EncryptionHandler implements PacketHandler {
    @Override
    public void handle(@NotNull Connection connection, @NotNull Packet packet) {
        JsonObject json = packet.toJson();
        String keyStr = json.get("key").getAsString();

        char[] encryptionKey = keyStr.toCharArray();
        SimpleSymmetricEncryption encryption = new SimpleSymmetricEncryption(encryptionKey);

        connection.setEncryption(encryption);

        connection.send(new Packet(Opcode.CACHE_REQUEST));
    }
}
