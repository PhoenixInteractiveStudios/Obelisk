package org.burrow_studios.obelisk.commons.util;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

public class JsonUtil {
    private JsonUtil() { }

    public static void add(@NotNull JsonObject json, @NotNull String path, Object obj) {
        json.add(path, toElement(obj));
    }

    public static void add(@NotNull JsonArray json, Object obj) {
        json.add(toElement(obj));
    }

    public static @NotNull JsonElement toElement(Object obj) {
        if (obj == null)
            return JsonNull.INSTANCE;
        else if (obj instanceof String s)
            return new JsonPrimitive(s);
        else if (obj instanceof Character c)
            return new JsonPrimitive(c);
        else if (obj instanceof Number n)
            return new JsonPrimitive(n);
        else if (obj instanceof Boolean b)
            return new JsonPrimitive(b);
        else
            return new JsonPrimitive(String.valueOf(obj));
    }
}
