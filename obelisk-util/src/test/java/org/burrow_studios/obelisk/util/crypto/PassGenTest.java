package org.burrow_studios.obelisk.util.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PassGenTest {
    @Test
    void testMinLength() {
        String pass;

        for (int i = 1; i < 1000; i++) {
            pass = PassGen.generate(i);

            assertEquals(i, pass.length());
        }
    }

    @Test
    void testZeroLength() {
        assertThrows(IllegalArgumentException.class, () -> PassGen.generate(0), "Length must be at least 1");
    }

    @Test
    void testNegativeLength() {
        assertThrows(IllegalArgumentException.class, () -> PassGen.generate(-1), "Length must be at least 1");
    }

    @Test
    void testImpossibleConditions() {
        assertThrows(IllegalArgumentException.class, () -> PassGen.generate(10, 11, 0));
        assertThrows(IllegalArgumentException.class, () -> PassGen.generate(10, 6, 6));
        assertThrows(IllegalArgumentException.class, () -> PassGen.generate(10, false, false, false, false, 0, 0, ""), "Must at least have one character type enabled");
    }
}