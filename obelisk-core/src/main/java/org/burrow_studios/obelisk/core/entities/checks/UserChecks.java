package org.burrow_studios.obelisk.core.entities.checks;

import org.burrow_studios.obelisk.core.util.validation.Validation;

public class UserChecks {
    private UserChecks() { }

    public static void checkName(String name) throws IllegalArgumentException {
        Validation.of("name", name)
                .checkNonNull()
                .checkLength(2, 256)
                .checkNotBlank();
    }
}
