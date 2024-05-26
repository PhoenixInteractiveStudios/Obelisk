package org.burrow_studios.obelisk.client.socket.handlers;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.client.ObeliskImpl;
import org.burrow_studios.obelisk.core.socket.Connection;
import org.burrow_studios.obelisk.core.socket.Opcode;
import org.burrow_studios.obelisk.core.socket.Packet;
import org.burrow_studios.obelisk.core.socket.PacketHandler;
import org.burrow_studios.obelisk.util.crypto.EncryptionException;
import org.burrow_studios.obelisk.util.crypto.RSAPublicEncryption;
import org.burrow_studios.obelisk.util.crypto.SimpleSymmetricEncryption;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class HelloHandler implements PacketHandler {
    private final ObeliskImpl obelisk;

    public HelloHandler(@NotNull ObeliskImpl obelisk) {
        this.obelisk = obelisk;
    }

    @Override
    public void handle(@NotNull Connection connection, @NotNull Packet packet) {
        JsonObject json = packet.toJson();
        String key = json.get("key").getAsString();

        byte[] pubKey = Base64.getDecoder().decode(key);
        try {
            RSAPublicEncryption encryption = new RSAPublicEncryption(pubKey);
            connection.setEncryption(encryption);
        } catch (EncryptionException e) {
            throw new Error("Probable implementation error", e);
        }

        String token = this.obelisk.getAuthConfig().getToken();

        SimpleSymmetricEncryption encryption = new SimpleSymmetricEncryption(token.toCharArray());

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("token", token);

        Packet response = new Packet(Opcode.IDENTIFY, responseJson);
        connection.send(response);

        // decrypt next incoming message with token to receive actual symmetric encryption key
        connection.setEncryption(encryption);
    }
}
