package org.burrow_studios.obelisk.util;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class EnvUtil {
    private EnvUtil() { }

    public static String getString(@NotNull String name) {
        return System.getenv(name);
    }

    public static String getString(@NotNull String name, String defaultValue) {
        return Optional.ofNullable(System.getenv(name))
                .orElse(defaultValue);
    }

    public static int getInt(@NotNull String name, int defaultValue) {
        return Optional.ofNullable(System.getenv(name))
                .map(Integer::parseInt)
                .orElse(defaultValue);
    }
}
