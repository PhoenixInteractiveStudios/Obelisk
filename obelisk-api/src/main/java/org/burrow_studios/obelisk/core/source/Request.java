package org.burrow_studios.obelisk.core.source;

import com.google.gson.JsonElement;
import org.burrow_studios.obelisk.commons.http.CompiledEndpoint;
import org.burrow_studios.obelisk.core.net.TimeoutContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class Request {
    private final @NotNull DataProvider provider;
    private final long id;

    private final @NotNull CompiledEndpoint endpoint;
    private final @Nullable JsonElement content;
    private final @NotNull TimeoutContext timeout;

    private final @NotNull CompletableFuture<Response> future;

    public Request(
            @NotNull DataProvider provider,
            long id,
            @NotNull CompiledEndpoint endpoint,
            @Nullable JsonElement content,
            @NotNull TimeoutContext timeout
    ) {
        this.provider = provider;
        this.id = id;

        this.endpoint = endpoint;
        this.content = content;
        this.timeout = timeout;

        this.future = new CompletableFuture<>();
    }

    public @NotNull DataProvider getProvider() {
        return provider;
    }

    public long getId() {
        return id;
    }

    public @NotNull CompiledEndpoint getEndpoint() {
        return endpoint;
    }

    public @Nullable JsonElement getContent() {
        return content;
    }

    public @NotNull Optional<JsonElement> optionalContent() {
        return Optional.ofNullable(this.getContent());
    }

    public @NotNull TimeoutContext getTimeout() {
        return timeout;
    }

    public @NotNull CompletableFuture<Response> getFuture() {
        return future;
    }

    public @NotNull Response respond(int code, @Nullable JsonElement content) {
        return new Response(provider, id, code, content);
    }
}
