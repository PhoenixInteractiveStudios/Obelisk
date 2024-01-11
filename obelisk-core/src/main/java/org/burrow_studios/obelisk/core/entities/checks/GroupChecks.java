package org.burrow_studios.obelisk.core.entities.checks;

import org.burrow_studios.obelisk.core.util.validation.Validation;

public class GroupChecks {
    private GroupChecks() { }

    public static void checkName(String name) throws IllegalArgumentException {
        Validation.of("name", name)
                .checkNonNull()
                .checkLength(2, 256)
                .checkNotBlank();
    }

    public static void checkPosition(int position) throws IllegalArgumentException {
        Validation.of("position", position)
                .checkRange(0, Integer.MAX_VALUE);
    }
}
