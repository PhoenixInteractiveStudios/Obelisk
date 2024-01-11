package org.burrow_studios.obelisk.core.net.http;

import org.jetbrains.annotations.NotNull;

public class RouteBuilder {
    private final @NotNull Route route;
    private final String[] args;
    private int index;

    public RouteBuilder(@NotNull Route route) {
        this.route = route;
        this.args  = new String[route.getParamCount()];
        this.index = 0;
    }

    public @NotNull RouteBuilder withArg(@NotNull Object arg) throws ArrayIndexOutOfBoundsException {
        this.args[index++] = String.valueOf(arg);
        return this;
    }

    public @NotNull RouteBuilder withArg(int index, @NotNull Object arg) throws ArrayIndexOutOfBoundsException {
        this.args[index] = String.valueOf(arg);
        return this;
    }

    public @NotNull CompiledRoute compile() {
        return new CompiledRoute(route, route.getPath().formatted((Object[]) args));
    }
}
