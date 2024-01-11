package org.burrow_studios.obelisk.core.entities;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.core.ObeliskImpl;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.entities.impl.TurtleImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UpdateHelper {
    private UpdateHelper() { }

    public static <U> void handleUpdate(
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

    public static <U extends TurtleImpl> void handleUpdateTurtle(
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull Function<Long, U> supplier,
            @NotNull Consumer<U> consumer
    ) {
        handleUpdate(json, path, jsonElement -> supplier.apply(jsonElement.getAsLong()), consumer);
    }

    public static <U extends TurtleImpl> void handleUpdateTurtle(
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull ObeliskImpl api,
            @NotNull BiFunction<ObeliskImpl, Long, U> supplier,
            @NotNull Consumer<U> consumer
    )  {
        handleUpdateTurtle(json, path, id -> supplier.apply(api, id), consumer);
    }

    public static <U extends Enum<U>> void handleUpdateEnum(
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

    public static <U> void handleUpdateObject(
            @NotNull JsonObject json,
            @NotNull String path,
            @NotNull ObeliskImpl api,
            @NotNull Function<JsonObject, U> map,
            @NotNull Consumer<U> consumer
    ) {
        if (!json.has(path)) return;

        final JsonObject jsonObj = json.getAsJsonObject(path);
        final U          obj     = map.apply(jsonObj);

        consumer.accept(obj);
    }

    public static <U, C extends Collection<U>> void handleUpdateArray(
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

    public static void handleUpdateTurtles(
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
