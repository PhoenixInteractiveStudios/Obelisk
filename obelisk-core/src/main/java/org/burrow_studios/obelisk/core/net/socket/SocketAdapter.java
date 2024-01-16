package org.burrow_studios.obelisk.core.net.socket;

import org.burrow_studios.obelisk.core.net.NetworkHandler;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class SocketAdapter {
    private final @NotNull NetworkHandler networkHandler;
    private SocketIO socketIO;

    public SocketAdapter(@NotNull NetworkHandler networkHandler) {
        this.networkHandler = networkHandler;
    }

    public void connect(@NotNull String host, int port) {
        if (this.socketIO != null)
            this.socketIO.shutdown(null);

        final SocketAddress address = new InetSocketAddress(host, port);

        this.socketIO = new SocketIO(address);
        this.socketIO.onReceive(bytes -> {
            // TODO
        });
        this.socketIO.onShutdown(throwable -> {
            // TODO
        });
    }
}
