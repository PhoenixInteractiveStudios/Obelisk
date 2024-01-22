package org.burrow_studios.obelisk.server.net.http.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.burrow_studios.obelisk.core.net.http.Method;
import org.burrow_studios.obelisk.server.net.NetworkHandler;
import org.burrow_studios.obelisk.server.net.http.APIHandler;
import org.burrow_studios.obelisk.server.net.http.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

public class SunServerImpl extends APIHandler implements HttpHandler {
    private final HttpServer server;

    public SunServerImpl(@NotNull NetworkHandler networkHandler, int port) throws IOException {
        super(networkHandler);
        this.server = HttpServer.create();
        this.server.createContext("/", this);
        this.server.bind(new InetSocketAddress(port), 0);
        this.server.start();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            this.handle0(exchange);
        } catch (Exception e) {
            // TODO: log e

            exchange.sendResponseHeaders(500, -1);
        }
        exchange.close();
    }

    private void handle0(HttpExchange exchange) throws IOException {
        final URI uri = exchange.getRequestURI();

        final String methodStr = exchange.getRequestMethod();
        final Method method    = Method.valueOf(methodStr);

        final Map<String, String> headers = exchange.getRequestHeaders().entrySet().stream()
                // combine duplicate header fields into one (separated by comma)
                .map(entry -> Map.entry(entry.getKey(), String.join(", ", entry.getValue())))
                // collect as Map<String, String>
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        final String body = new String(exchange.getRequestBody().readAllBytes());

        final Response response = this.handle(method, uri.getPath(), headers, body);

        final Headers responseHeaders = exchange.getResponseHeaders();
        response.headers().forEach(responseHeaders::add);

        exchange.sendResponseHeaders(response.code(), response.bodyLength());

        if (response.hasBody())
            exchange.getResponseBody().write(response.bodyBytes());
    }
}
