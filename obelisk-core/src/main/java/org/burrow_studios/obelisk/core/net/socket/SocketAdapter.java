package org.burrow_studios.obelisk.core.net.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.api.event.GatewayEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityCreateEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityDeleteEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityEvent;
import org.burrow_studios.obelisk.api.event.entity.EntityUpdateEvent;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.entities.EntityData;
import org.burrow_studios.obelisk.core.entities.EntityLifecycleHelper;
import org.burrow_studios.obelisk.core.entities.EntityUpdater;
import org.burrow_studios.obelisk.core.event.gateway.GatewayOpcodes;
import org.burrow_studios.obelisk.core.net.NetworkHandler;
import org.burrow_studios.obelisk.core.net.socket.crypto.EncryptionException;
import org.burrow_studios.obelisk.core.net.socket.crypto.SimpleSymmetricEncryption;
import org.burrow_studios.obelisk.core.source.EventProvider;
import org.burrow_studios.obelisk.util.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SocketAdapter implements EventProvider {
    private final @NotNull NetworkHandler networkHandler;
    private final TurtleGenerator turtleGenerator = TurtleGenerator.get("Socket");
    private SimpleSymmetricEncryption sokCrypto;
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

        this.sokCrypto = new SimpleSymmetricEncryption(sok.toCharArray());

        this.socketIO = new SocketIO(address);
        this.socketIO.onReceiveString(this::receiveHandshake);
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
        final JsonObject json = this.gson.fromJson(new String(data), JsonObject.class);

        final long id = json.get("id").getAsLong();
        final int  op = json.get("op").getAsInt();

        final JsonObject c = json.get("c").getAsJsonObject();

        Class<? extends GatewayEvent> eventType = GatewayOpcodes.get(op);

        if (EntityEvent.class.isAssignableFrom(eventType)) {
            final long entityId = c.get("id").getAsLong();
            final EntityData entityData = new EntityData(c);

            if (EntityUpdateEvent.class.isAssignableFrom(eventType)) {
                Turtle entity = this.getAPI().getTurtle(entityId);

                if (entity == null)
                    throw new NetworkException("Unknown entity: " + entityId + " (opcode " + op + ")");

                EntityUpdater.update(entity, entityData, id);
            } else if (EntityCreateEvent.class.isAssignableFrom(eventType)) {
                EntityLifecycleHelper.createEntity(getAPI(), entityData, id, eventType);
            } else if (EntityDeleteEvent.class.isAssignableFrom(eventType)) {
                EntityLifecycleHelper.deleteEntity(getAPI(), entityId, id, eventType);
            }
        } else {
            throw new NetworkException("Unknown opcode: " + op);
        }
    }

    private void receiveHandshake(@NotNull String data) throws NetworkException, EncryptionException {
        this.receiveHandshake(gson.fromJson(data, JsonObject.class));
    }

    private synchronized void receiveHandshake(@NotNull JsonObject json) throws NetworkException, EncryptionException {
        final long id = json.get("id").getAsLong();
        final int  op = json.get("op").getAsInt();

        final JsonObject c = json.getAsJsonObject("c");

        switch (op) {
            case GatewayOpcodes.DISCONNECT          -> this.onDisconnect(c);
            case GatewayOpcodes.HANDSHAKE_CHALLENGE -> this.onChallenge(c);
            case GatewayOpcodes.HANDSHAKE_ASCEND    -> this.onAscend(c);
            default -> { /* TODO: shutdown */ }
        }
    }

    private void onChallenge(@NotNull JsonObject json) throws NetworkException, EncryptionException {
        // decrypt the new encryption key
        final String obfuscatedKey = json.get("c").getAsString();
        final String key = new String(this.sokCrypto.decrypt(obfuscatedKey.getBytes()));

        // apply new encryption
        final SimpleSymmetricEncryption crypto = new SimpleSymmetricEncryption(key.toCharArray());
        this.socketIO.setCrypto(crypto);

        // send encrypted acknowledgement
        JsonObject response = new JsonObject();
        response.addProperty("id", turtleGenerator.newId());
        response.addProperty("op", GatewayOpcodes.HANDSHAKE_RESPONSE);

        this.socketIO.send(gson.toJson(response));
    }

    private void onAscend(JsonObject json) {
        // re-endpoint receiver
        this.socketIO.onReceive(this::receive);
    }

    private void onDisconnect(JsonObject json) {
        this.socketIO.shutdown(null);
        // TODO: cascade fail
    }

    private void handleShutdown(Throwable throwable) throws Exception {
        // TODO
    }

    @Override
    public @NotNull ObeliskImpl getAPI() {
        return this.networkHandler.getAPI();
    }
}
