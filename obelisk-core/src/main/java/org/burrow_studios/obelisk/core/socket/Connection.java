package org.burrow_studios.obelisk.core.socket;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Connection implements Closeable {
    private final SocketIO socketIO;
    private final Map<Opcode, PacketHandler> handlers;

    public Connection(@NotNull Socket socket) throws IOException {
        this.socketIO = new SocketIO(socket, this::receive);
        this.handlers = new ConcurrentHashMap<>();
    }

    public Connection(@NotNull Socket socket, @NotNull SocketServer server) throws IOException {
        this.socketIO = new SocketIO(socket, this::receive);
        this.handlers = server.getHandlers();
    }

    private void receive(@NotNull JsonObject json) {
        final int op = json.get("op").getAsInt();
        final Opcode opcode = Opcode.get(op);

        PacketHandler handler = handlers.get(opcode);
        Packet packet = new Packet(opcode, json);

        // TODO: throw error or log?
        if (handler == null) return;

        handler.handle(this, packet);
    }

    public void send(@NotNull Packet packet) {
       this.socketIO.send(packet.toJson());
    }

    public void addHandler(@NotNull Opcode opcode, @NotNull PacketHandler handler) {
        this.handlers.put(opcode, handler);
    }

    @Override
    public void close() throws IOException {
        this.socketIO.close();
    }
}
