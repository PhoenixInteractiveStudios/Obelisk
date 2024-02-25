package org.burrow_studios.obelisk.commons.rpc;

import org.burrow_studios.obelisk.commons.rpc.authentication.AuthenticationLevel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

public final class Endpoint {
    private static final String LEGAL_CHARS_PATH = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~!$&'()*+,;=:@";

    private final @NotNull Method method;
    private final @NotNull Segment[] segments;
    private final @NotNull AuthenticationLevel authenticationLevel;
    private final @NotNull String[] intents;

    Endpoint(@NotNull Method method, @NotNull Segment[] segments, @NotNull AuthenticationLevel authenticationLevel, @NotNull String[] intents) {
        this.method = method;
        this.segments = segments;
        this.authenticationLevel = authenticationLevel;
        this.intents = intents;
    }

    public @NotNull Method getMethod() {
        return method;
    }

    public @NotNull String getPath() {
        StringBuilder builder = new StringBuilder();

        for (Segment segment : segments) {
            builder.append("/");
            if (segment instanceof VanillaSegment vSeg)
                builder.append(vSeg.string());
            if (segment instanceof ParameterSegment<?> pSeg)
                builder.append(":").append(pSeg.identifier());
            // noinspection StatementWithEmptyBody
            if (segment instanceof EmptySegment eSeg) { }
        }

        return builder.toString();
    }

    public @NotNull AuthenticationLevel getAuthenticationLevel() {
        return this.authenticationLevel;
    }

    public String[] getIntents() {
        return intents.clone();
    }

    public @NotNull RPCRequest.Builder builder(@NotNull Object... params) {
        String[] str = new String[params.length];
        for (int i = 0; i < params.length; i++)
            str[i] = String.valueOf(params[i]);
        return this.builder(str);
    }

    public @NotNull RPCRequest.Builder builder(@NotNull String... params) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < segments.length; i++) {
            builder.append("/");
            if (segments[i] instanceof VanillaSegment vSeg)
                builder.append(vSeg.string());
            if (segments[i] instanceof ParameterSegment<?> pSeg)
                builder.append(pSeg.mapper().apply(params[i]));
            // noinspection StatementWithEmptyBody
            if (segments[i] instanceof EmptySegment eSeg) { }
        }

        return new RPCRequest.Builder()
                .setMethod(this.method)
                .setPath(builder.toString());
    }

    public boolean match(@NotNull String[] segments) {
        if (this.segments.length != segments.length) return false;

        for (int i = 0; i < segments.length; i++)
            if (!this.segments[i].match(segments[i]))
                return false;

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Endpoint endpoint)) return false;

        if (endpoint.method != this.method) return false;
        if (!Arrays.equals(endpoint.segments, this.segments)) return false;
        if (endpoint.authenticationLevel != this.authenticationLevel) return false;
        return Arrays.equals(endpoint.intents, this.intents);
    }

    public static @NotNull Endpoint build(@NotNull Method method, @NotNull String path, @NotNull AuthenticationLevel authenticationLevel, @NotNull String... intents) {
        String[] pathSegments = path.substring(1).split("/");

        ArrayList<Segment> segments = new ArrayList<>();

        for (int i = 0; i < pathSegments.length; i++) {
            String pathSegment = pathSegments[i];

            validateSegment(pathSegment);

            Segment segment = parseSegment(pathSegment);

            if (segment instanceof EmptySegment)
                if (i < pathSegments.length - 1)
                    throw new IllegalArgumentException("Only the last path segment may be empty");

            segments.add(segment);
        }

        return new Endpoint(method, segments.toArray(Segment[]::new), authenticationLevel, intents);
    }

    private static void validateSegment(@NotNull String segment) throws IllegalArgumentException {
        char[] chars = segment.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (LEGAL_CHARS_PATH.contains(String.valueOf(chars[i])))
                continue;

            if (chars[i] == '%') {
                if (chars.length <= i + 2)
                    throw new IllegalArgumentException("Path contains '%' that is not followed by 2 hexadecimal digits");

                for (int j = i + 1; j <= i + 2; j++) {
                    if (HexFormat.isHexDigit(chars[j])) continue;
                    throw new IllegalArgumentException("Path contains '%' that is not followed by 2 hexadecimal digits");
                }

                continue;
            }

            throw new IllegalArgumentException("Path contains illegal character '" + chars[i] + "'");
        }
    }

    private static @NotNull Segment parseSegment(@NotNull String segment) throws IllegalArgumentException {
        if (segment.isEmpty())
            return new EmptySegment();


        if (segment.startsWith(":")) {
            String type = segment.substring(1);

            return switch (type) {
                case "int"    -> new ParameterSegment<>("int"   , Integer::parseInt);
                case "long"   -> new ParameterSegment<>("long"  , Long::parseLong);
                case "float"  -> new ParameterSegment<>("float" , Float::parseFloat);
                case "double" -> new ParameterSegment<>("double", Double::parseDouble);
                case "short"  -> new ParameterSegment<>("short" , Short::parseShort);
                case "byte"   -> new ParameterSegment<>("byte"  , Byte::parseByte);
                case "bool"   -> new ParameterSegment<>("bool"  , Boolean::parseBoolean);
                case "string" -> new ParameterSegment<>("string", s -> s);
                case "uuid"   -> new ParameterSegment<>("uuid"  , UUID::fromString);
                // don't interpret segment as parameterized if the type is unknown
                default -> new VanillaSegment(segment);
            };
        }

        return new VanillaSegment(segment);
    }

    private interface Segment {
        boolean match(@NotNull String str);
    }

    private record VanillaSegment(
            @NotNull String string
    ) implements Segment {
        @Override
        public boolean match(@NotNull String str) {
            return string.equals(str);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof VanillaSegment vSeg)) return false;
            return vSeg.string().equals(this.string);
        }
    }

    private record ParameterSegment<T>(
            @NotNull String identifier,
            @NotNull Function<String, T> mapper
    ) implements Segment {
        @Override
        public boolean match(@NotNull String str) {
            try {
                mapper.apply(str);
            } catch (RuntimeException e) {
                return false;
            }
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof ParameterSegment<?> pSeg)) return false;
            return pSeg.identifier().equals(this.identifier());
        }
    }

    private record EmptySegment() implements Segment {
        @Override
        public boolean match(@NotNull String str) {
            return str.isEmpty();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof EmptySegment;
        }
    }
}
