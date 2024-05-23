package org.burrow_studios.obelisk.core.http;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Path {
    /** Characters that can be used in an endpoint path. */
    private static final String LEGAL_CHARS_PATH = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~!$&'()*+,;=:@";

    private final String path;
    private final String[] segments;

    public Path(@NotNull String path) {
        this.path = path;

        this.segments = path.split("/");
        for (int i = 0; i < this.segments.length; i++) {
            String segment = this.segments[i];

            if (segment.startsWith("{") && segment.endsWith("}")) {
                this.segments[i] = null;
                continue;
            }

            for (char c : segment.toCharArray()) {
                if (LEGAL_CHARS_PATH.contains(String.valueOf(c))) continue;

                throw new IllegalArgumentException("Illegal character in path: '" + c + "'");
            }
        }
    }

    public @NotNull String getPath() {
        return this.path;
    }

    public int getParamCount() {
        int params = 0;
        for (String segment : this.segments)
            if (segment == null)
                params++;
        return params;
    }

    public boolean match(@NotNull Path path) {
        if (this.segments.length != path.segments.length) return false;

        for (int i = 0; i < this.segments.length; i++) {
            String thisSegment = this.segments[i];
            String thatSegment = path.segments[i];

            if (thisSegment == null) continue;
            if (!thisSegment.equals(thatSegment)) return false;
        }

        return true;
    }

    public <T> T parsePathSegment(int index, @NotNull Function<String, T> func) {
        return func.apply(this.segments[index]);
    }
}
