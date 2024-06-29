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
        String type = json.get("type").getAsString();

        String id    = json.get("id").getAsString();
        String title = json.get("title").getAsString();

        if (type.equals(TextElement.IDENTIFIER)) {
            String content = json.get("content").getAsString();

            return new TextElement(id, title, content);
        }

        throw new Error("Not implemented");
    }

    public static JsonObject toJson(@NotNull FormElement element) {
        JsonObject json = new JsonObject();

        json.addProperty("id", element.getId());
        json.addProperty("title", element.getTitle());

        if (element instanceof TextElement textElement) {
            json.addProperty("type", TextElement.IDENTIFIER);
            json.addProperty("content", textElement.getContent());

            return json;
        }

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
