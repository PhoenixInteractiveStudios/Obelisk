package org.burrow_studios.obelisk.internal.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.internal.EntityBuilder;
import org.burrow_studios.obelisk.internal.ObeliskImpl;
import org.burrow_studios.obelisk.internal.cache.DelegatingTurtleCacheView;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Data<T extends Turtle> {
    private final @NotNull JsonObject json;

    protected Data() {
        this.json = new JsonObject();
    }

    protected Data(long id) {
        this.json = new JsonObject();
        this.json.addProperty("id", id);
    }

    protected Data(@NotNull JsonObject json) {
        this.json = json;
    }

    public final @NotNull JsonObject toJson() {
        return this.json.deepCopy();
    }

    public abstract @NotNull T build(@NotNull EntityBuilder builder);

    public abstract void update(@NotNull T entity);

    /* - - - */

    protected final void set(@NotNull String path, @NotNull JsonElement json) {
        this.json.add(path, json);
    }

    protected final void addToArray(@NotNull String path, @NotNull JsonArray elements) {
        final JsonElement currentElement = this.json.get("path");
        final JsonArray array = currentElement != null
                ? currentElement.getAsJsonArray()
                : new JsonArray();

        array.addAll(elements);

        this.json.add(path, array);
    }

    protected final void removeFromArray(@NotNull String path, @NotNull JsonArray elements) {
        final JsonElement currentElement = this.json.get("path");

        if (currentElement == null) return;

        final JsonArray array = currentElement.getAsJsonArray();

        for (JsonElement element : elements)
            array.remove(element);

        this.json.add(path, array);
    }

    /* - - - */

    protected static <U> void handleUpdate(
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull Function<JsonElement, U> map,
            @NotNull Consumer<U> consumer
    ) {
        if (!json.has(path)) return;

        final JsonElement element = json.get(path);
        final U obj = map.apply(element);
        consumer.accept(obj);
    }

    protected static <U extends Enum<U>> void handleUpdateEnum(
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull Class<U> enumClass,
            @NotNull Consumer<U> consumer
    ) {
        if (!json.has("path")) return;

        final JsonElement element = json.get(path);
        final U obj = Enum.valueOf(enumClass, element.getAsString());
        consumer.accept(obj);
    }

    protected static <U> void handleUpdateObject(
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull ObeliskImpl api,
            @NotNull BiFunction<EntityBuilder, JsonObject, U> map,
            @NotNull Consumer<U> consumer
    ) {
        if (!json.has(path)) return;

        final JsonObject jsonObj = json.getAsJsonObject(path);
        final U          obj     = map.apply(api.getEntityBuilder(), jsonObj);

        consumer.accept(obj);
    }

    protected static <U, C extends Collection<U>> void handleUpdateArray(
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull Function<JsonElement, U> map,
            @NotNull C collection
    ) {
        if (!json.has(path)) return;

        final List<U> elements = json.get(path)
                .getAsJsonArray()
                .asList().stream()
                .map(map)
                .toList();

        collection.clear();
        collection.addAll(elements);
    }

    protected static void handleUpdateTurtles(
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull Supplier<DelegatingTurtleCacheView<?>> cacheSupplier
    ) {
        if (!json.has(path)) return;

        final Set<Long> updatedIds = json.get(path)
                .getAsJsonArray()
                .asList().stream()
                .map(JsonElement::getAsLong)
                .collect(Collectors.toSet());

        final DelegatingTurtleCacheView<?> cacheView = cacheSupplier.get();

        cacheView.addAllIds(updatedIds);
        cacheView.retainIds(updatedIds);
    }
}