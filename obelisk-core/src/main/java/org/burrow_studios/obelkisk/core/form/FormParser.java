package org.burrow_studios.obelkisk.core.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FormParser {
    private FormParser() { }

    public static FormElement fromJson(@NotNull JsonObject json) {
        throw new Error("Not implemented");
    }

    public static JsonObject toJson(@NotNull FormElement element) {
        throw new Error("Not implemented");
    }

    public static List<FormElement> fromJson(@NotNull JsonArray json) {
        ArrayList<FormElement> elements = new ArrayList<>(json.size());
        for (JsonElement element : json) {
            JsonObject obj = element.getAsJsonObject();
            elements.add(fromJson(obj));
        }
        return Collections.unmodifiableList(elements);
    }

    public static JsonArray toJson(@NotNull Iterable<FormElement> elements) {
        JsonArray arr = new JsonArray();
        for (FormElement element : elements)
            arr.add(toJson(element));
        return arr;
    }
}
