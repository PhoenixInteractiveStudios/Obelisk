package org.burrow_studios.obelisk.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObeliskBuilderTest {
    @Test
    void testRequireHost() {
        ObeliskBuilderImpl builder = ObeliskBuilderImpl.create();

        builder.setToken("xyz");

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void testRequireToken() {
        ObeliskBuilderImpl builder = ObeliskBuilderImpl.create();

        builder.setHost("localhost");

        assertThrows(IllegalArgumentException.class, builder::build);
    }

    @Test
    void testBuild() {
        ObeliskBuilderImpl builder = ObeliskBuilderImpl.create();

        builder.setHost("localhost");
        builder.setToken("xyz");

        assertDoesNotThrow(builder::build);
    }
}