package org.burrow_studios.obelisk.commons.yaml;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class YamlList extends YamlElement implements Iterable<YamlElement> {
    private final ArrayList<YamlElement> elements;

    public YamlList() {
        this.elements = new ArrayList<>();
    }

    public YamlList(int initialCapacity) {
        this.elements = new ArrayList<>(initialCapacity);
    }

    public void add(YamlElement element) {
        this.elements.add(element != null ? element : YamlNull.INSTANCE);
    }

    public void addAll(@NotNull Collection<YamlElement> elements) {
        elements.forEach(this::add);
    }

    @Override
    public @NotNull Iterator<YamlElement> iterator() {
        return this.elements.iterator();
    }

    static @NotNull YamlList parse(@NotNull List<Object> elements) {
        YamlList list = new YamlList();
        for (Object element : elements)
            list.add(YamlElement.parse(element));
        return list;
    }
}
