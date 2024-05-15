package org.burrow_studios.obelisk.api.request;

import org.jetbrains.annotations.NotNull;

public enum ErrorResponse {
    SERVER_ERROR(0, "Internal Server Error");

    private final int code;
    private final String message;

    ErrorResponse(int code, @NotNull String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public @NotNull String getMessage() {
        return message;
    }

    /* - - - */

    public static @NotNull ErrorResponse fromCode(int code) {
        for (ErrorResponse value : values())
            if (value.getCode() == code)
                return value;
        return SERVER_ERROR;
    }
}
