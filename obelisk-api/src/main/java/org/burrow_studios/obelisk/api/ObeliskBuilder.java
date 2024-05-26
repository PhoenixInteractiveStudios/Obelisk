package org.burrow_studios.obelisk.api;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface ObeliskBuilder {
    static @NotNull ObeliskBuilder create() {
        try {
            Class<?> clazz = Class.forName("org.burrow_studios.obelisk.client.ObeliskBuilderImpl");
            Method method = clazz.getMethod("create");
            return ((ObeliskBuilder) method.invoke(null));
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new Error("Implementation error");
        }
    }

    static @NotNull ObeliskBuilder createDefault() {
        return create().setHost("api.burrow-studios.org/v1/");
    }

    @NotNull Obelisk build() throws IllegalArgumentException;

    @NotNull ObeliskBuilder setHost(String host);

    @NotNull ObeliskBuilder setGateway(String host, int port);

    @NotNull ObeliskBuilder setToken(String token);
}
