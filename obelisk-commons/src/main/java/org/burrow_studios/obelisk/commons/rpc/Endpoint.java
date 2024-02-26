package org.burrow_studios.obelisk.commons.rpc;

import org.burrow_studios.obelisk.commons.rpc.authentication.AuthenticationLevel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;

/**
 * Represents an API endpoint. Each endpoint has a {@link Method} and a path (as part of the resource identifier).
 * <p> This class also handles some high-level authentication and authorization logic in the form of an
 * {@link AuthenticationLevel} and zero or more intents, which are required to access this endpoint.
 */
public final class Endpoint {
    /** Characters that can be used in an endpoint path. */
    private static final String LEGAL_CHARS_PATH = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~!$&'()*+,;=:@";

    /** Request method */
    private final @NotNull Method method;
    /** The request path, split into {@link Segment Segments}. */
    private final @NotNull Segment[] segments;
    /** Required level of authentication to access this endpoint. This is used for server-side checks. */
    private final @NotNull AuthenticationLevel authenticationLevel;
    /** Required intents that are required to access this endpoint. This is used for server-side checks. */
    private final @NotNull String[] intents;

    /** Private constructor. Called by {@link Endpoint#build(Method, String, AuthenticationLevel, String...)}. */
    Endpoint(@NotNull Method method, @NotNull Segment[] segments, @NotNull AuthenticationLevel authenticationLevel, @NotNull String[] intents) {
        this.method = method;
        this.segments = segments;
        this.authenticationLevel = authenticationLevel;
        this.intents = intents;
    }

    /**
     * Returns the request method of this endpoint.
     * @return Request method.
     */
    public @NotNull Method getMethod() {
        return method;
    }

    /**
     * Returns a String, representing the path of this endpoint.
     * @return Request path.
     */
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

    /**
     * Returns the required level of authentication to access this endpoint. This is used for server-side checks.
     * @return Required authentication level.
     * @see AuthenticationLevel
     */
    public @NotNull AuthenticationLevel getAuthenticationLevel() {
        return this.authenticationLevel;
    }

    /**
     * Return an array of intents required to access this endpoint. This is used for server-side checks.
     * @return Required intents.
     */
    public String[] getIntents() {
        return intents.clone();
    }

    /**
     * Creates a new {@link RPCRequest.Builder request builder} based on this endpoint. If this endpoint contained
     * parameterized segments, the {@code params} parameter should be used to pass any arguments to these segments,
     * indexed in parallel to the respective segments.
     * @param params Arguments for parameterized segments; Parallel indices.
     * @return Builder for a reuest to this endpoint.
     */
    public @NotNull RPCRequest.Builder builder(@NotNull Object... params) {
        String[] str = new String[params.length];
        for (int i = 0; i < params.length; i++)
            str[i] = String.valueOf(params[i]);
        return this.builder(str);
    }

    /**
     * Creates a new {@link RPCRequest.Builder request builder} based on this endpoint. If this endpoint contained
     * parameterized segments, the {@code params} parameter should be used to pass any arguments to these segments,
     * indexed in parallel to the respective segments.
     * @param params Arguments for parameterized segments; Parallel indices.
     * @return Builder for a reuest to this endpoint.
     */
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

    /**
     * Checks whether the provided path segments match this endpoint. This includes type-matching for parameterized
     * segments. If this endpoint matches the given segments, it indicates that any {@link EndpointHandler} assigned to
     * this Endpoint may be invoked with a {@link RPCRequest request} to the path of these segments.
     * @param segments Array of path segments to match.
     * @return {@code true} if this Endpoint matches the provided segments.
     */
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
