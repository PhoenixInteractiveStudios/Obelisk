package org.burrow_studios.obelisk.server.net.socket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.event.gateway.GatewayOpcodes;
import org.burrow_studios.obelisk.core.net.socket.NetworkException;
import org.burrow_studios.obelisk.core.net.socket.SocketIO;
import org.burrow_studios.obelisk.common.crypto.EncryptionException;
import org.burrow_studios.obelisk.common.crypto.SimpleSymmetricEncryption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;
import java.security.SecureRandom;

public class ClientBuilder {
    private final EventDispatcher eventDispatcher;
    private SocketIO socketIO;

    private Long subBuffer;
    private Long sidBuffer;

    private boolean encrypted = false;

    private final byte[] encryptionKey;

    public ClientBuilder(@NotNull EventDispatcher eventDispatcher, @NotNull Socket socket) throws NetworkException {
        this.eventDispatcher = eventDispatcher;
        this.socketIO = new SocketIO(socket);

        this.encryptionKey = new byte[512];
        new SecureRandom().nextBytes(this.encryptionKey);

        this.socketIO.onReceiveString(this::receive);
        this.socketIO.onShutdown(this::shutdown);
        this.socketIO.start();

        JsonObject json = new JsonObject();
        json.addProperty("id", eventDispatcher.turtleGenerator.newId());
        json.addProperty("op", GatewayOpcodes.HANDSHAKE_CHALLENGE);
        json.addProperty("c", new String(encryptionKey));

        this.socketIO.send(eventDispatcher.gson.toJson(json));
    }

    private synchronized void ascend() {
        if (this.socketIO == null)
            throw new IllegalStateException("Already ascended");

        final Client client = new Client(eventDispatcher, socketIO);

        this.eventDispatcher.clients.add(client);
        this.eventDispatcher.pendingClients.remove(this);

        // clean up (ClientBuilders should not be re-used)
        this.socketIO = null;
    }

    private void receive(@NotNull String data) throws Exception {
        this.receive(eventDispatcher.gson.fromJson(data, JsonObject.class));
    }

    private synchronized void receive(@NotNull JsonObject json) throws Exception {
        final long id = json.get("id").getAsLong();
        final int  op = json.get("op").getAsInt();

        final JsonObject c = json.getAsJsonObject("c");

        switch (op) {
            case GatewayOpcodes.DISCONNECT         -> this.onDisconnect(c);
            case GatewayOpcodes.HELLO              -> this.onHello(c);
            case GatewayOpcodes.HANDSHAKE_RESPONSE -> this.onResponse(c);
            default -> this.shutdown(null);
        }
    }

    private void shutdown(@Nullable Throwable cause) throws Exception {
        eventDispatcher.pendingClients.remove(this);

        JsonObject json = new JsonObject();
        json.addProperty("id", eventDispatcher.turtleGenerator.newId());
        json.addProperty("op", GatewayOpcodes.DISCONNECT);

        this.socketIO.send(eventDispatcher.gson.toJson(json));
    }

    /* - - - */

    private void onHello(@NotNull JsonObject json) throws NetworkException, EncryptionException {
        this.subBuffer = json.get("sub").getAsLong();
        this.sidBuffer = json.get("sid").getAsLong();

        // encrypt the actual encryption key with the initial socket key
        final String key = this.eventDispatcher.getNetworkHandler().getServer().getAuthenticator().getTokenManager().getSocketKey(subBuffer, sidBuffer);
        final SimpleSymmetricEncryption tmpCrypto = new SimpleSymmetricEncryption(key.toCharArray());
        final String obfuscatedKey = new String(tmpCrypto.encrypt(encryptionKey));

        // send key (semi-encrypted)
        JsonObject response = new JsonObject();
        response.addProperty("id", eventDispatcher.turtleGenerator.newId());
        response.addProperty("op", GatewayOpcodes.HANDSHAKE_CHALLENGE);
        response.addProperty("c", obfuscatedKey);

        // pause receiver until new encryption is established on the server-side
        this.socketIO.setListen(false);
        this.socketIO.send(eventDispatcher.gson.toJson(response));

        // apply new encryption
        final SimpleSymmetricEncryption crypto = new SimpleSymmetricEncryption(new String(encryptionKey).toCharArray());
        this.socketIO.setCrypto(crypto);
        this.encrypted = true;

        // resume listening (now encrypted)
        this.socketIO.setListen(true);
    }

    private void onResponse(@NotNull JsonObject json) throws NetworkException {
        // check if the connection is already encrypted or if this packet was received as cleartext
        if (!encrypted) {
            JsonObject response = new JsonObject();
            response.addProperty("id", eventDispatcher.turtleGenerator.newId());
            response.addProperty("op", GatewayOpcodes.DISCONNECT);
            this.socketIO.send(eventDispatcher.gson.toJson(response));
            this.onDisconnect(null);
            return;
        }

        // reaching this point means the packed was encrypted properly

        JsonObject response = new JsonObject();
        response.addProperty("id", eventDispatcher.turtleGenerator.newId());
        response.addProperty("op", GatewayOpcodes.HANDSHAKE_ASCEND);

        this.socketIO.send(eventDispatcher.gson.toJson(response));

        this.ascend();
    }

    private void onDisconnect(JsonObject json) {
        this.socketIO.shutdown(null);
        this.subBuffer = null;
        this.sidBuffer = null;

        eventDispatcher.pendingClients.remove(this);
    }
}
