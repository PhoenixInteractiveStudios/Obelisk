package org.burrow_studios.obelisk.api;

import org.jetbrains.annotations.NotNull;

public interface ObeliskBuilder {
    static @NotNull ObeliskBuilder create() {
        try {
            Class<?> implClass = Class.forName("org.burrow_studios.obelisk.core.ObeliskBuilderImpl");
            return (ObeliskBuilder) implClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Caught an unexpected exception when attempting to create ObeliskBuilder. Please inform the devs.", e);
        }
    }

    @NotNull ObeliskBuilder setToken(String token);

    @NotNull Obelisk build() throws IllegalArgumentException;
}
