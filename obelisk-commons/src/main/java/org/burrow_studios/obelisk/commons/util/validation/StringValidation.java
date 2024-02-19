package org.burrow_studios.obelisk.commons.util.validation;

import org.jetbrains.annotations.NotNull;

public final class StringValidation extends Validation<String, StringValidation> {
    StringValidation(@NotNull String name, String value) {
        super(name, value);
    }

    public @NotNull StringValidation checkLength(int min, int max) throws IllegalArgumentException {
        if (value.length() < min)
            throw newIAE("{name} must be " + min + " characters or longer");

        if (value.length() > max)
            throw newIAE("{name} must not be longer than " + max + " characters");
        return this;
    }

    public @NotNull StringValidation checkNotBlank() throws IllegalArgumentException {
        if (value.isBlank())
            throw newIAE("{name} may not be blank");
        return this;
    }
}
