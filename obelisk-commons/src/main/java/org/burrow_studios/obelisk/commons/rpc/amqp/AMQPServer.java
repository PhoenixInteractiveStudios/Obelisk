package org.burrow_studios.obelisk.commons.rpc.amqp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rabbitmq.client.*;
import org.burrow_studios.obelisk.commons.rpc.Method;
import org.burrow_studios.obelisk.commons.rpc.RPCRequest;
import org.burrow_studios.obelisk.commons.rpc.RPCResponse;
import org.burrow_studios.obelisk.commons.rpc.RPCServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/** A simple AMQP {@link RPCServer} implementation utilizing the RabbitMQ Java library. */
public class AMQPServer extends RPCServer<AMQPServer> {
    private final Connection connection;
    private final Channel channel;

    public AMQPServer(@NotNull String host, int port, @NotNull String user, @NotNull String pass) throws IOException, TimeoutException {
        this.connection = AMQPConnections.getConnection(host, port, user, pass);
        this.channel    = this.connection.createChannel();

        this.channel.queueDeclare("QUEUE_NAME", false, false, false, null);
        this.channel.queuePurge("QUEUE_NAME");

        this.channel.basicQos(1);

        this.channel.basicConsume("QUEUE_NAME", false, (consumerTag, delivery) -> {
            try {
                this.handle(consumerTag, delivery);
            } catch (Exception e) {
                // TODO: log e

            }
        }, (consumerTag) -> { });
    }

    private void handle(String consumerTag, Delivery delivery) throws IOException {
        RPCRequest.Builder requestBuilder = new RPCRequest.Builder();
        
        final String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
        final JsonObject  requestJson = AMQPUtils.GSON.fromJson(message, JsonObject.class);
        final JsonObject responseJson = new JsonObject();
        
        // request path
        requestBuilder.setPath(requestJson.get("path").getAsString());
        
        // request method
        final String methodStr = requestJson.get("method").getAsString();
        final Method method    = Method.valueOf(methodStr);
        requestBuilder.setMethod(method);
        
        // request headers
        final JsonObject headers = requestJson.getAsJsonObject("headers");
        for (Map.Entry<String, JsonElement> header : headers.entrySet())
            requestBuilder.setHeaders(header.getKey(), header.getValue().getAsJsonArray());

        // request body
        final JsonElement requestBody = requestJson.get("body");
        requestBuilder.setBody(requestBody);

        // request id
        final long id = requestJson.get("id").getAsLong();

        RPCRequest  request  = requestBuilder.build(id);
        RPCResponse response = this.handle(request);

        // response id
        responseJson.addProperty("id", id);

        // response headers
        responseJson.add("headers", response.getHeaders());

        // response status
        responseJson.addProperty("status", response.getStatus().name());

        // response body
        if (response.getBody() != null)
            responseJson.add("body", response.getBody());

        AMQP.BasicProperties replyProperties = new AMQP.BasicProperties.Builder()
                .correlationId(delivery.getProperties().getCorrelationId())
                .build();

        final byte[] rawResponse = AMQPUtils.GSON.toJson(responseJson).getBytes(StandardCharsets.UTF_8);

        channel.basicPublish("", delivery.getProperties().getReplyTo(), replyProperties, rawResponse);
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }
}
