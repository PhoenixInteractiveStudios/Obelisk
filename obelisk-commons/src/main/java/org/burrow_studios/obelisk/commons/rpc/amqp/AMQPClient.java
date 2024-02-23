package org.burrow_studios.obelisk.commons.rpc.amqp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rabbitmq.client.*;
import org.burrow_studios.obelisk.commons.rpc.RPCClient;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.Status;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

public class AMQPClient implements RPCClient {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private final Connection connection;
    private final Channel channel;

    public AMQPClient(@NotNull String host, int port, @NotNull String user, @NotNull String pass) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(pass);

        this.connection = connectionFactory.newConnection();
        this.channel    = this.connection.createChannel();
    }

    @Override
    public @NotNull CompletableFuture<RPCResponse> send(@NotNull RPCRequest request) throws IOException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .replyTo(replyQueueName)
                .correlationId(corrId)
                .build();

        final JsonObject requestJson = request.toJson();
        final String     requestStr  = GSON.toJson(requestJson);
        final byte[]     requestRaw  = requestStr.getBytes(StandardCharsets.UTF_8);

        channel.basicPublish("", "REQUEST_QUEUE_NAME", properties, requestRaw);

        CompletableFuture<RPCResponse> future = new CompletableFuture<>();

        channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            try {
                this.handleResponse(future, request, corrId, consumerTag, delivery);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, c -> {
            if (!future.isDone())
                future.completeExceptionally(new InterruptedException());
        });

        return future;
    }

    private void handleResponse(@NotNull CompletableFuture<RPCResponse> callback, @NotNull RPCRequest request, @NotNull String corrId, @NotNull String consumerTag, @NotNull Delivery delivery) {
        final String correlationId = delivery.getProperties().getCorrelationId();

        if (!correlationId.equals(corrId)) return;

        RPCResponse.Builder responseBuilder = new RPCResponse.Builder(request);

        final byte[]     responseRaw  = delivery.getBody();
        final String     responseStr  = new String(responseRaw, StandardCharsets.UTF_8);
        final JsonObject responseJson = GSON.fromJson(responseStr, JsonObject.class);

        // response headers
        final JsonObject headers = responseJson.getAsJsonObject("headers");
        for (Map.Entry<String, JsonElement> header : headers.entrySet())
            responseBuilder.setHeaders(header.getKey(), header.getValue().getAsJsonArray());

        // response status
        final String statusStr = responseJson.get("status").getAsString();
        final Status status    = Status.valueOf(statusStr);
        responseBuilder.setStatus(status);

        // response body
        final JsonElement requestBody = responseJson.get("body");
        responseBuilder.setBody(requestBody);

        callback.complete(responseBuilder.build());

        try {
            channel.basicCancel(consumerTag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
