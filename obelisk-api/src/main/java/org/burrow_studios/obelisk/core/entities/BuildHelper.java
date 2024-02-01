package org.burrow_studios.obelisk.core.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.burrow_studios.obelisk.api.entities.Turtle;
import org.burrow_studios.obelisk.core.cache.DelegatingTurtleCacheView;
import org.burrow_studios.obelisk.core.cache.TurtleCache;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Function;

public class BuildHelper {
    private BuildHelper() { }

    public static <T extends Turtle> @NotNull DelegatingTurtleCacheView<T> buildDelegatingCacheView(@NotNull JsonObject json, @NotNull String path, @NotNull TurtleCache<? super T> cache, @NotNull Class<T> type) {
        return buildDelegatingCacheView(json.getAsJsonArray(path), cache, type);
    }

    public static <T extends Turtle> @NotNull DelegatingTurtleCacheView<T> buildDelegatingCacheView(@NotNull JsonArray ids, @NotNull TurtleCache<? super T> cache, @NotNull Class<T> type) {
        final DelegatingTurtleCacheView<T> entities = new DelegatingTurtleCacheView<>(cache, type);
        for (JsonElement idElement : ids)
            entities.add(idElement.getAsLong());
        return entities;
    }

    public static <T> @NotNull ArrayList<T> buildList(@NotNull JsonObject json, @NotNull String path, @NotNull Function<JsonElement, T> mappingFunction) {
        return buildList(json.getAsJsonArray(path), mappingFunction);
    }

    public static <T> @NotNull ArrayList<T> buildList(@NotNull JsonArray elements, @NotNull Function<JsonElement, T> mappingFunction) {
        ArrayList<T> list = new ArrayList<>();
        for (JsonElement element : elements)
            list.add(mappingFunction.apply(element));
        return list;
    }
}
