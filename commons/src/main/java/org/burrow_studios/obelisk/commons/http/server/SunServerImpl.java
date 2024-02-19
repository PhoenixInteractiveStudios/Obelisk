package org.burrow_studios.obelisk.commons.http.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.burrow_studios.obelisk.commons.http.Method;
import org.burrow_studios.obelisk.commons.http.HTTPResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.stream.Collectors;

/** A simple {@link HTTPServer} implementation utilizing the lightweight {@link HttpServer} that is shipped with the JDK. */
public final class SunServerImpl extends HTTPServer {
    /** Internal lightweight HttpServer. */
    private final HttpServer server;

    public SunServerImpl(@NotNull Authorizer authorizer, int port) throws IOException {
        super(authorizer);

        this.server = HttpServer.create();
        this.server.createContext("/", exchange -> {
            try {
                this.handle(exchange);
            } catch (Exception e) {
                // TODO: log e

                exchange.sendResponseHeaders(500, -1);
            }
            exchange.close();
        });
        this.server.bind(new InetSocketAddress(port), 0);
        this.server.start();
    }

    /** Receives an HTTP request in form of an {@link HttpExchange} and relais it to {@link HTTPServer#handle(Method, String, Map, String)}. */
    private void handle(HttpExchange exchange) throws IOException {
        final URI uri = exchange.getRequestURI();

        final String methodStr = exchange.getRequestMethod();
        final Method method    = Method.valueOf(methodStr);

        final Map<String, String> headers = exchange.getRequestHeaders().entrySet().stream()
                // combine duplicate header fields into one (separated by comma)
                .map(entry -> Map.entry(entry.getKey(), String.join(", ", entry.getValue())))
                // collect as Map<String, String>
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));

        final String body = new String(exchange.getRequestBody().readAllBytes());

        final HTTPResponse response = this.handle(method, uri.getPath(), headers, body);

        final Headers responseHeaders = exchange.getResponseHeaders();
        response.headers().forEach(responseHeaders::add);

        exchange.sendResponseHeaders(response.code(), response.bodyLength());

        if (response.hasBody())
            exchange.getResponseBody().write(response.bodyBytes());
    }
}
