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

    public @NotNull StringValidation checkCharWhitelist(@NotNull String legalChars, @NotNull String legalDelimiters) throws IllegalArgumentException {
        for (int i = 0; i < value.length(); i++) {
            if (legalChars.contains(String.valueOf(value.charAt(i))))
                continue;

            if (i == 0 || i == value.length() - 1)
                if (legalDelimiters.contains(String.valueOf(value.charAt(i))))
                    continue;

            throw newIAE("{name} may not contain '" + value.charAt(i) + "'");
        }
        return this;
    }

    public @NotNull StringValidation checkCharWhitelist(@NotNull String legalChars) throws IllegalArgumentException {
        return this.checkCharWhitelist(legalChars, "");
    }
}
