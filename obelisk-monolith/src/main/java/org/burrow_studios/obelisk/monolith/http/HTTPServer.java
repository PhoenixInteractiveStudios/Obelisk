package org.burrow_studios.obelisk.monolith.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.burrow_studios.obelisk.core.http.Route;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HTTPServer {
    static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .create();

    private final HttpServer internalServer;
    private final Map<Route, RequestHandler> handlers;

    public HTTPServer(int port) throws IOException {
        this.internalServer = HttpServer.create();
        this.internalServer.bind(new InetSocketAddress(port), -1);
        this.internalServer.createContext("/", this::handle);

        this.handlers = new ConcurrentHashMap<>();
    }

    public void start() {
        this.internalServer.start();
    }

    public void addHandler(@NotNull Route route, @NotNull RequestHandler handler) {
        this.handlers.put(route, handler);
    }

    private void handle(@NotNull HttpExchange exchange) throws IOException {
        // TODO
    }
}
