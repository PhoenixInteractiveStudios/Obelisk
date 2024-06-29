package org.burrow_studios.obelkisk.core.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        if (type.equals(ChoiceQuery.IDENTIFIER)) {
            List<String> options = new ArrayList<>();

            Optional.ofNullable(json.get("options"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsJsonArray)
                    .ifPresent(arr -> {
                        arr.forEach(element -> {
                            options.add(element.getAsString());
                        });
                    });

            boolean optional = json.get("optional").getAsBoolean();

            String defaultValue = Optional.ofNullable(json.get("default"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsString)
                    .orElse(null);

            String selected = Optional.ofNullable(json.get("selected"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsString)
                    .orElse(null);

            boolean done = json.get("selected") != null;

            return new ChoiceQuery(id, title, options, optional, defaultValue, selected, done);
        }

        if (type.equals(IntQuery.IDENTIFIER)) {
            Integer min = Optional.ofNullable(json.get("min"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsInt)
                    .orElse(Integer.MIN_VALUE);

            Integer max = Optional.ofNullable(json.get("max"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsInt)
                    .orElse(Integer.MAX_VALUE);

            boolean optional = json.get("optional").getAsBoolean();

            Integer defaultValue = Optional.ofNullable(json.get("default"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsInt)
                    .orElse(null);

            Integer val = Optional.ofNullable(json.get("value"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsInt)
                    .orElse(null);

            boolean done = json.get("value") != null;

            return new IntQuery(id, title, min, max, optional, defaultValue, val, done);
        }

        if (type.equals(TextQuery.IDENTIFIER)) {
            Integer min = Optional.ofNullable(json.get("min"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsInt)
                    .orElse(Integer.MIN_VALUE);

            Integer max = Optional.ofNullable(json.get("max"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsInt)
                    .orElse(Integer.MAX_VALUE);

            boolean optional = json.get("optional").getAsBoolean();

            String defaultValue = Optional.ofNullable(json.get("default"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsString)
                    .orElse(null);

            String input = Optional.ofNullable(json.get("input"))
                    .filter(e -> !e.isJsonNull())
                    .map(JsonElement::getAsString)
                    .orElse(null);

            boolean done = json.get("input") != null;

            return new TextQuery(id, title, min, max, optional, defaultValue, input, done);
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

        if (element instanceof QueryElement<?> queryElement) {
            json.addProperty("optional", queryElement.isOptional());
        }

        if (element instanceof ChoiceQuery choiceQuery) {
            json.addProperty("type", ChoiceQuery.IDENTIFIER);
            json.addProperty("default", choiceQuery.getDefaultValue());
            json.addProperty("selected", choiceQuery.getValue());

            JsonArray arr = new JsonArray();
            for (String option : choiceQuery.getOptions())
                arr.add(option);
            json.add("options", arr);

            return json;
        }

        if (element instanceof IntQuery intQuery) {
            json.addProperty("type", TextQuery.IDENTIFIER);
            json.addProperty("min", intQuery.getMin());
            json.addProperty("max", intQuery.getMax());
            json.addProperty("default", intQuery.getDefaultValue());
            json.addProperty("value", intQuery.getValue());

            return json;
        }

        if (element instanceof TextQuery textQuery) {
            json.addProperty("type", TextQuery.IDENTIFIER);
            json.addProperty("min", textQuery.getMinLength());
            json.addProperty("max", textQuery.getMaxLength());
            json.addProperty("default", textQuery.getDefaultValue());
            json.addProperty("input", textQuery.getValue());

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
