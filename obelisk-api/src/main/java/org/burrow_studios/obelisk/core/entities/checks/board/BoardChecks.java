package org.burrow_studios.obelisk.core.entities.checks.board;

import org.burrow_studios.obelisk.common.util.validation.Validation;

public class BoardChecks {
    private BoardChecks() { }

    public static void checkTitle(String title) throws IllegalArgumentException {
        Validation.of("title", title)
                .checkNonNull()
                .checkLength(2, 256)
                .checkNotBlank();
    }
}
