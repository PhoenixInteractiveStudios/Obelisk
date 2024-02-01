package org.burrow_studios.obelisk.common.util.validation;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public class Validation<T, V extends Validation<T, V>> {
    protected final @NotNull String name;
    protected final T value;

    Validation(@NotNull String name, T value) {
        this.name  = name;
        this.value = value;
    }

    public static @NotNull StringValidation of(@NotNull String name, String value) {
        return new StringValidation(name, value);
    }

    public static @NotNull IntValidation of(@NotNull String name, Integer value) {
        return new IntValidation(name, value);
    }

    public static <V extends Validation<Object, V>> @NotNull V of(@NotNull String name, Object obj) {
        return (V) new Validation<>(name, obj);
    }

    protected final @NotNull IllegalArgumentException newIAE(@NotNull String message) {
        return new IllegalArgumentException(message.replaceAll("\\{name}", this.name));
    }

    public @NotNull V checkNonNull() throws IllegalArgumentException {
        if (value == null)
            throw newIAE("{name} must not be null");
        return (V) this;
    }
}
