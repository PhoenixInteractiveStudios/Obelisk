package org.burrow_studios.obelisk.core.entities.checks;

import org.burrow_studios.obelisk.commons.util.validation.Validation;

public class ProjectChecks {
    private ProjectChecks() { }

    public static void checkTitle(String title) throws IllegalArgumentException {
        Validation.of("title", title)
                .checkNonNull()
                .checkLength(2, 256)
                .checkNotBlank();
    }
}
