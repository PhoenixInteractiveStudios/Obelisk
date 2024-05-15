package org.burrow_studios.obelisk.util.tuple;

import java.util.Map;

public record Pair<A,B>(
        A first,
        B second
) implements Map.Entry<A, B> {
    @Override
    public A getKey() {
        return this.first();
    }

    @Override
    public B getValue() {
        return this.second();
    }

    @Override
    public B setValue(Object value) {
        throw new UnsupportedOperationException();
    }
}
