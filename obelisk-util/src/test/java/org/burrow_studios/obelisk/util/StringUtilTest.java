package org.burrow_studios.obelisk.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringUtilTest {
    @Test
    void replaceNamedWithNumberedParameters_emptyString() {
        String str = "";

        String res = StringUtil.replaceNamedWithNumberedParameters(str);

        assertEquals(str, res);
    }

    @Test
    void replaceNamedWithNumberedParameters_noParameters() {
        String str = "Hello World";

        String res = StringUtil.replaceNamedWithNumberedParameters(str);

        assertEquals(str, res);
    }

    @Test
    void replaceNamedWithNumberedParameters_singleParameter() {
        String str = "Hello {world}";
        String exp = "Hello {0}";

        String res = StringUtil.replaceNamedWithNumberedParameters(str);

        assertEquals(exp, res);
    }

    @Test
    void replaceNamedWithNumberedParameters_differentParameters() {
        String str = "Hello {world}, I am {name}.";
        String exp = "Hello {0}, I am {1}.";

        String res = StringUtil.replaceNamedWithNumberedParameters(str);

        assertEquals(exp, res);
    }

    @Test
    void replaceNamedWithNumberedParameters_repeatingParameter() {
        String str = "Hello {world}, I am {name} and you are {world}.";
        String exp = "Hello {0}, I am {1} and you are {0}.";

        String res = StringUtil.replaceNamedWithNumberedParameters(str);

        assertEquals(exp, res);
    }

    @SuppressWarnings("DataFlowIssue") // literally what we are testing here
    @Test
    void replaceNamedWithNumberedParameters_null() {
        assertThrows(IllegalArgumentException.class, () -> StringUtil.replaceNamedWithNumberedParameters(null));
    }
}