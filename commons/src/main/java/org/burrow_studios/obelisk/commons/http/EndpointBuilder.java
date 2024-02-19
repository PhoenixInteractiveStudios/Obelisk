package org.burrow_studios.obelisk.commons.http;

import org.jetbrains.annotations.NotNull;

public class EndpointBuilder {
    private final @NotNull Endpoint endpoint;
    private final Object[] args;
    private int index;

    public EndpointBuilder(@NotNull Endpoint endpoint) {
        this.endpoint = endpoint;
        this.args  = new String[endpoint.getSegmentLength()];
        this.index = 0;
    }

    public @NotNull EndpointBuilder withArg(@NotNull Object arg) throws ArrayIndexOutOfBoundsException {
        this.index = this.endpoint.getNextParameterIndex(this.index);
        this.args[index++] = arg;
        return this;
    }

    public @NotNull EndpointBuilder withArg(int index, @NotNull Object arg) throws ArrayIndexOutOfBoundsException {
        this.args[index] = arg;
        return this;
    }

    public @NotNull CompiledEndpoint compile() {
        return new CompiledEndpoint(endpoint, endpoint.compile(args), args);
    }
}
