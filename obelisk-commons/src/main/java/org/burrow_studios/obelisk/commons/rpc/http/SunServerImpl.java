package org.burrow_studios.obelisk.commons.rpc.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.burrow_studios.obelisk.commons.rpc.*;
import org.burrow_studios.obelisk.commons.turtle.TimeBasedIdGenerator;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/** A simple HTTP {@link RPCServer} implementation utilizing the lightweight {@link HttpServer} that is shipped with the JDK. */
public final class SunServerImpl extends RPCServer<SunServerImpl> {
    /** Internal lightweight HttpServer. */
    private final HttpServer server;

    private final TimeBasedIdGenerator requestIds = TimeBasedIdGenerator.get();

    public SunServerImpl(int port) throws IOException {
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

    /** Receives an HTTP request in form of an {@link HttpExchange} and relais it to {@link RPCServer#handle(RPCRequest)}. */
    private void handle(HttpExchange exchange) throws IOException {
        RPCRequest.Builder requestBuilder = new RPCRequest.Builder();

        // request path
        final URI uri = exchange.getRequestURI();
        requestBuilder.setPath(uri.getPath());

        // request method
        final String methodStr = exchange.getRequestMethod();
        final Method method    = Method.valueOf(methodStr);
        requestBuilder.setMethod(method);

        // request headers
        exchange.getRequestHeaders().forEach((key, val) -> requestBuilder.setHeaders(key, val));

        // request body
        requestBuilder.setBody(exchange.getRequestBody().readAllBytes());

        // request id
        long id = requestIds.newId();

        RPCRequest  request  = requestBuilder.build(id);
        RPCResponse response = this.handle(request);

        // response headers
        final Headers responseHeaders = exchange.getResponseHeaders();
        response.getHeaderMap().forEach(responseHeaders::add);

        // response body (pre)
        String responseBody = response.getBodyString();

        // response status (& body length)
        exchange.sendResponseHeaders(response.getStatus().getCode(), (responseBody != null && !responseBody.isEmpty()) ? responseBody.length() : -1);

        // response body
        if (responseBody != null)
            exchange.getResponseBody().write(responseBody.getBytes());
    }

    @Override
    public void close() {
        // TODO: await pending requests?
        this.server.stop((int) TimeoutContext.DEFAULT.asTimeout(TimeUnit.SECONDS));
    }
}
