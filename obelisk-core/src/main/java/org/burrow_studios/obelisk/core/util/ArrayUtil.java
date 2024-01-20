package org.burrow_studios.obelisk.core.util;

import java.util.Objects;

public class ArrayUtil {
    private ArrayUtil() { }

    public static boolean contains(Object[] arr, Object obj) {
        for (Object element : arr)
            if (Objects.equals(element, obj))
                return true;
        return false;
    }
}
