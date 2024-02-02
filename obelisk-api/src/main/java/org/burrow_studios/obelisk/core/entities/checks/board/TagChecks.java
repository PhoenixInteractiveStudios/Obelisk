package org.burrow_studios.obelisk.core.entities.checks.board;

import org.burrow_studios.obelisk.commons.util.validation.Validation;

public class TagChecks {
    private TagChecks() { }

    public static void checkName(String name) throws IllegalArgumentException {
        Validation.of("name", name)
                .checkNonNull()
                .checkLength(2, 256)
                .checkNotBlank();
    }
}
