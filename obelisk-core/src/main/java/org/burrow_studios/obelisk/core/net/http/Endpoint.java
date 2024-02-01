package org.burrow_studios.obelisk.core.net.http;

import org.burrow_studios.obelisk.common.function.ExceptionalFunction;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class Endpoint {
    private final @NotNull Method method;
    private final @NotNull String path;
    private final @NotNull Segment[] segments;
    private final @NotNull AuthLevel privilege;

    public Endpoint(@NotNull Method method, @NotNull String path, @NotNull AuthLevel privilege) {
        this.method = method;
        this.path = path;
        this.privilege = privilege;

        // FIXME: placeholder
        assert !path.endsWith("/");

        this.segments = buildSegments(path);
    }

    public @NotNull EndpointBuilder builder() {
        return new EndpointBuilder(this);
    }

    public @NotNull Method getMethod() {
        return this.method;
    }

    @NotNull String compile(Object[] params) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < segments.length; i++) {
            builder.append("/");
            if (segments[i] instanceof VanillaSegment vSeg)
                builder.append(vSeg.segment());
            if (segments[i] instanceof ParameterSegment<?>)
                builder.append(params[i]);
        }
        return builder.toString();
    }

    public boolean isPrivileged() {
        return !getPrivilege().equals(AuthLevel.NONE);
    }

    public @NotNull AuthLevel getPrivilege() {
        return privilege;
    }

    public int getSegmentLength() {
        return this.segments.length;
    }

    public int getNextParameterIndex(int start) {
        for (int i = start; i < this.segments.length; i++)
            if (segments[i] instanceof ParameterSegment<?>)
                return i;
        return segments.length;
    }

    public boolean matchPath(@NotNull String[] segments) {
        if (this.segments.length != segments.length) return false;

        for (int i = 0; i < this.segments.length; i++)
            if (!this.segments[i].match(segments[i]))
                return false;

        return true;
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(@NotNull String str, int i) {
        final String[] segments = str.substring(1).split("/");

        if (segments.length != this.segments.length)
            throw new IllegalArgumentException("Mismatch in segment count");

        ParameterSegment<T> segment = (ParameterSegment<T>) this.segments[i];
        return segment.mapper.apply(segments[i]);
    }

    private interface Segment {
        boolean match(@NotNull String str);
    }

    private record VanillaSegment(
            @NotNull String segment
    ) implements Segment {
        @Override
        public boolean match(@NotNull String str) {
            return segment.equals(str);
        }
    }

    private record ParameterSegment<T>(
            @NotNull ExceptionalFunction<String, T, RuntimeException> mapper
    ) implements Segment {
        @Override
        public boolean match(@NotNull String str) {
            try {
                mapper.apply(str);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private static @NotNull Segment[] buildSegments(@NotNull String path) {
        final  String[] segmentPaths = path.substring(1).split("/");
        final Segment[] segments     = new Segment[segmentPaths.length];

        for (int i = 0; i < segments.length; i++) {
            final String sPath = segmentPaths[i];

            if (!sPath.startsWith(":")) {
                segments[i] = new VanillaSegment(sPath);
                continue;
            }

            final String type = sPath.substring(1).toLowerCase();

            segments[i] = switch (type) {
                case "int"    -> new ParameterSegment<>(Integer::parseInt);
                case "long"   -> new ParameterSegment<>(Long::parseLong);
                case "float"  -> new ParameterSegment<>(Float::parseFloat);
                case "double" -> new ParameterSegment<>(Double::parseDouble);
                case "short"  -> new ParameterSegment<>(Short::parseShort);
                case "byte"   -> new ParameterSegment<>(Byte::parseByte);
                case "bool"   -> new ParameterSegment<>(Boolean::parseBoolean);
                case "string" -> new ParameterSegment<>(s -> s);
                case "uuid"   -> new ParameterSegment<>(UUID::fromString);
                default -> new VanillaSegment(sPath);
            };
        }

        return segments;
    }
}
