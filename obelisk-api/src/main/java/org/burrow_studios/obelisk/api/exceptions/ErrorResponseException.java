package org.burrow_studios.obelisk.api.exceptions;

import org.burrow_studios.obelisk.api.request.ErrorResponse;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ErrorResponseException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ErrorResponseException(@NotNull ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    public ErrorResponseException(@NotNull String message, @Nullable String details, @NotNull ErrorResponse errorResponse) {
        super(errorResponse.getMessage() + " (" + message + ": " + details + ")");
        this.errorResponse = errorResponse;
    }
}
