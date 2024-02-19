package org.burrow_studios.obelisk.commons.util.validation;

import org.jetbrains.annotations.NotNull;

public class IntValidation extends Validation<Integer, IntValidation> {
    IntValidation(@NotNull String name, Integer value) {
        super(name, value);
    }

    public @NotNull IntValidation checkRange(int min, int max) throws IllegalArgumentException {
        if (value < min)
            throw newIAE("{name} must be " + min + " or higher");
        if (value > max)
            throw newIAE("{name} must be lower than " + max);
        return this;
    }
}
