package org.burrow_studios.obelisk.commons.rpc.amqp;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Delivery;
import org.burrow_studios.obelisk.commons.rpc.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/** A simple AMQP {@link RPCServer} implementation utilizing the RabbitMQ Java library. */
public class AMQPServer extends RPCServer<AMQPServer> {
    private final Connection connection;
    private final Channel channel;

    private final String exchange;
    private final String queue;

    public AMQPServer(
            @NotNull String host,
            int port,
            @NotNull String user,
            @NotNull String pass,
            @NotNull String exchange,
            @NotNull String queue
    ) throws IOException, TimeoutException {
        super();

        this.connection = AMQPConnections.getConnection(host, port, user, pass);
        this.channel    = this.connection.createChannel();

        this.exchange = exchange;
        this.queue = queue;

        this.channel.basicQos(1);
        this.channel.queueDeclare(queue, false, false, false, null);
        this.channel.exchangeDeclare(this.exchange, "topic");
        this.channel.basicConsume(queue, false, (consumerTag, delivery) -> {
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

        // request time
        final String  timeStr = requestJson.get("time").getAsString();
        final Instant time    = Instant.parse(timeStr);
        requestBuilder.setTime(time);

        // request timeout / deadline
        final String  timeoutStr     = requestJson.get("timeout").getAsString();
        final Instant timeoutInstant = Instant.parse(timeoutStr);
        final TimeoutContext timeout = TimeoutContext.deadline(timeoutInstant);
        requestBuilder.setTimeout(timeout);

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

        // response time
        responseJson.addProperty("time", response.getTime().toString());

        // response headers
        responseJson.add("headers", response.getHeaders());

        // response status
        responseJson.addProperty("status", response.getStatus().name());

        // response body
        if (response.getBody() != null)
            responseJson.add("body", response.getBody());

        AMQP.BasicProperties replyProperties = new AMQP.BasicProperties.Builder()
                .correlationId(delivery.getProperties().getCorrelationId())
                .contentType("application/json")
                .contentEncoding("UTF-8")
                .build();

        final byte[] rawResponse = AMQPUtils.GSON.toJson(responseJson).getBytes(StandardCharsets.UTF_8);

        channel.basicPublish(exchange, delivery.getProperties().getReplyTo(), replyProperties, rawResponse);
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
    }

    @Override
    public void close() throws IOException {
        // TODO: await pending requests?
        this.connection.close();
    }
}
