package org.burrow_studios.obelisk.core.net.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.event.gateway.GatewayOpcodes;
import org.burrow_studios.obelisk.core.net.NetworkHandler;
import org.burrow_studios.obelisk.core.net.socket.crypto.EncryptionException;
import org.burrow_studios.obelisk.core.net.socket.crypto.EncryptionHandler;
import org.burrow_studios.obelisk.core.net.socket.crypto.SimpleSymmetricEncryption;
import org.burrow_studios.obelisk.util.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SocketAdapter {
    private final @NotNull NetworkHandler networkHandler;
    private final TurtleGenerator turtleGenerator = TurtleGenerator.get("Socket");
    private SocketIO socketIO;
    final Gson gson;

    public SocketAdapter(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    public void connect(@NotNull String host, int port, @NotNull String sub, @NotNull String sid, @NotNull String sok) throws NetworkException {
        if (this.socketIO != null)
            this.socketIO.shutdown(null);

        final SocketAddress address = new InetSocketAddress(host, port);

        final SimpleSymmetricEncryption tmpCrypto = new SimpleSymmetricEncryption(sok.toCharArray());

        this.socketIO = new SocketIO(address);
        this.socketIO.onReceive(bytes -> receiveHandshake(bytes, tmpCrypto));
        this.socketIO.onShutdown(this::handleShutdown);
        this.socketIO.start();

        // build initial hello
        JsonObject json = new JsonObject();
        json.addProperty("id", turtleGenerator.newId());
        json.addProperty("op", GatewayOpcodes.HELLO);
        json.addProperty("sub", sub);
        json.addProperty("sid", sid);

        // send hello
        this.socketIO.send(gson.toJson(json));
    }

    private void receive(byte[] data) throws Exception {
        // TODO
    }

    private void receiveHandshake(byte[] data, @NotNull EncryptionHandler tmpCrypto) throws NetworkException, EncryptionException {
        JsonObject json = gson.fromJson(new String(data), JsonObject.class);

        final long op = json.get("op").getAsLong();

        if (op == GatewayOpcodes.DISCONNECT) {
            // TODO: shutdown?
            this.socketIO.shutdown(null);
            return;
        }

        if (op != GatewayOpcodes.HANDSHAKE_CHALLENGE) {
            // TODO: fail
            return;
        }

        final String obfuscatedKey = json.get("c").getAsString();
        final String key = new String(tmpCrypto.decrypt(obfuscatedKey.getBytes()));

        final SimpleSymmetricEncryption crypto = new SimpleSymmetricEncryption(key.toCharArray());
        this.socketIO.setCrypto(crypto);

        JsonObject response = new JsonObject();
        response.addProperty("id", turtleGenerator.newId());
        response.addProperty("op", GatewayOpcodes.HANDSHAKE_RESPONSE);

        this.socketIO.onReceive(this::receive);
        this.socketIO.send(gson.toJson(response));
    }

    private void handleShutdown(Throwable throwable) throws Exception {
        // TODO
    }
}
