package org.burrow_studios.obelisk.api.exceptions;

import org.burrow_studios.obelisk.api.request.ErrorResponse;
import org.jetbrains.annotations.NotNull;

public class ErrorResponseException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ErrorResponseException(@NotNull ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }
}
