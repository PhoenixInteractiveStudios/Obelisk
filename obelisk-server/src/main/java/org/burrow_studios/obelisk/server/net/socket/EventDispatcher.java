package org.burrow_studios.obelisk.server.net.socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.burrow_studios.obelisk.core.net.socket.NetworkException;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.common.turtle.TurtleGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventDispatcher extends Thread {
    private static final Logger LOG = Logger.getLogger(EventDispatcher.class.getSimpleName());

    private final NetworkHandler networkHandler;
    private final ServerSocket server;

    final TurtleGenerator turtleGenerator = TurtleGenerator.get("Socket");
    final Gson gson;

    final Set<Client>        clients        = ConcurrentHashMap.newKeySet();
    final Set<ClientBuilder> pendingClients = ConcurrentHashMap.newKeySet();

    public EventDispatcher(@NotNull NetworkHandler networkHandler, int port) throws NetworkException {
        this.networkHandler = networkHandler;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        try {
            this.server = new ServerSocket(port);
        } catch (IOException e) {
            throw new NetworkException(e);
        }
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                final Socket socket = this.server.accept();

                final ClientBuilder pendingClient = new ClientBuilder(this, socket);
                this.pendingClients.add(pendingClient);
            } catch (IOException e) {
                LOG.log(Level.WARNING, "Could not handle incoming connection request due to an IOException", e);
            } catch (Throwable t) {
                LOG.log(Level.WARNING, "Could not handle incoming connection request due to an unknown internal error", t);
            }
        }
    }

    public @NotNull NetworkHandler getNetworkHandler() {
        return this.networkHandler;
    }
}
