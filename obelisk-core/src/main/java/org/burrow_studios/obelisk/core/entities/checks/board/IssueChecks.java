package org.burrow_studios.obelisk.core.entities.checks.board;

import org.burrow_studios.obelisk.core.util.validation.Validation;

public class IssueChecks {
    private IssueChecks() { }

    public static void checkTitle(String title) throws IllegalArgumentException {
        Validation.of("title", title)
                .checkNonNull()
                .checkLength(2, 256)
                .checkNotBlank();
    }
}
