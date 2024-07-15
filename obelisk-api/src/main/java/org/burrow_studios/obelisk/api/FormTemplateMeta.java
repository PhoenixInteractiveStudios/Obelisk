package org.burrow_studios.obelisk.api;

import org.jetbrains.annotations.NotNull;

public record FormTemplateMeta(
        @NotNull String identifier,
        long accessChannel
) { }
