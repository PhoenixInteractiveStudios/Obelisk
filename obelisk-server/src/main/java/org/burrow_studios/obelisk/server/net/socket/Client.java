package org.burrow_studios.obelisk.server.net.socket;

import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.event.gateway.GatewayOpcodes;
import org.burrow_studios.obelisk.core.net.socket.NetworkException;
import org.burrow_studios.obelisk.core.net.socket.SocketIO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Thread {
    private final EventDispatcher eventDispatcher;
    private final SocketIO socketIO;

    private final Object lock = new Object();

    private final LinkedBlockingQueue<Packet> outboundQueue = new LinkedBlockingQueue<>();

    public Client(@NotNull EventDispatcher eventDispatcher, @NotNull SocketIO socketIO) {
        this.eventDispatcher = eventDispatcher;
        this.socketIO = socketIO;
        this.socketIO.onReceiveString(this::receive);
        this.socketIO.onShutdown(this::onShutdown);

        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            if (outboundQueue.isEmpty()) {
                try {
                    this.lock.wait();
                } catch (InterruptedException ignored) { }
            }

            final Packet packet = outboundQueue.poll();
            if (packet == null) continue;

            final String data = eventDispatcher.gson.toJson(packet.getJson());
            try {
                socketIO.send(data);
            } catch (NetworkException ignored) { }
        }
    }

    private void receive(@NotNull String data) throws Exception {
        final JsonObject json = eventDispatcher.gson.fromJson(data, JsonObject.class);

        final long id = json.get("id").getAsLong();
        final int  op = json.get("op").getAsInt();

        if (op != GatewayOpcodes.DISCONNECT) {
            // TODO: fail
            return;
        }

        // TODO: shutdown
    }

    private void onShutdown(@Nullable Throwable cause) throws Exception {
        // TODO
    }

    public synchronized void queue(@NotNull Packet packet) {
        this.outboundQueue.add(packet);
        this.lock.notifyAll();
    }

    public void queue(@NotNull JsonObject packet) {
        this.queue(new Packet(packet));
    }
}
